package by.bstu.razvod.lab3.calendar;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import by.bstu.razvod.lab3.Note;
import by.bstu.razvod.lab3.NoteDate;

public class CalendarViewModel extends AndroidViewModel {

    private List<Note> notes = new ArrayList<>();
    private static final String FILE_NAME = "Notes-file";

    public MutableLiveData<List<Note>> noteLiveData = new MutableLiveData<>();

    public CalendarViewModel(@NonNull Application application) {
        super(application);
        getData();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void writeFileExternalStorage(@NonNull List<Note> newNotes) {
        assert newNotes != null;
        String state = Environment.getExternalStorageState();
        //external storage availability check
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return;
        }
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), FILE_NAME);


        FileOutputStream outputStream = null;
        try {
            file.createNewFile();
            //second argument of FileOutputStream constructor indicates whether to append or create new file if one exists
            outputStream = getApplication().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);

            String jsonValue = new Gson().toJson(newNotes);
            outputStream.write(jsonValue.getBytes(), 0, jsonValue.length());
            outputStream.close();
            setNewNotes(newNotes);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isExternalStorageWriteable(){
        String state = Environment.getExternalStorageState();
        return  Environment.MEDIA_MOUNTED.equals(state);
    }
    // проверяем, доступно ли внешнее хранилище хотя бы только для чтения
    public boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        return  (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    public void addNewNote(Note note) {
        notes.add(note);
        setNewNotes(notes);
        writeData(this.notes);
    }

    public void removeNote(NoteDate noteDate) {
        Note note = notes.stream()
                .filter(note_ -> noteDate.equals(note_.getDate()))
                .findAny()
                .orElse(null);
        notes.remove(note);
        setNewNotes(notes);
        writeData(this.notes);
    }

    private void writeData(@NonNull List<Note> newNotes) {
        assert newNotes != null;
        try {
            FileOutputStream file = getApplication().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            String jsonValue = new Gson().toJson(newNotes);
            file.write(jsonValue.getBytes(), 0, jsonValue.length());
            file.close();
            setNewNotes(newNotes);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setNewNotes(@NonNull List<Note> notes) {
        this.notes = notes;
        noteLiveData.setValue(this.notes);
    }

    private void getData() {
        try {
            FileInputStream file = getApplication().openFileInput(FILE_NAME);
            String fileString = getFileContent(file, "UTF-8");
            List<Note> newNotes = new Gson().fromJson(fileString, new TypeToken<List<Note>>() {}.getType());
            if (newNotes != null) {
                setNewNotes(newNotes);
            }
            file.close();
        } catch (Exception ignored) {

        }
    }

    public static String getFileContent(
            FileInputStream fis,
            String encoding) throws IOException {
        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(fis, encoding))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            return sb.toString();
        }
    }
}
