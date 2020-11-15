package by.bstu.razvod.lab3.calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import by.bstu.razvod.lab3.Note;
import by.bstu.razvod.lab3.NoteDate;
import by.bstu.razvod.lab3.R;

public class MainActivity extends AppCompatActivity {

    private CalendarViewModel viewModel;
    private CalendarView calendarView;
    private TextInputEditText textInputEditText;
    private NoteDate noteDate;
    private String noteText = "";
    private List<Note> notes = new ArrayList<>();
    private Note note = null;
    private static final int REQUEST_PERMISSION_WRITE = 1001;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this.getViewModelStore(), new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(CalendarViewModel.class);
        setContentView(R.layout.activity_main);
        calendarView = findViewById(R.id.calendarView);
        textInputEditText = findViewById(R.id.edit_text);

        checkPermissions();

        viewModel.noteLiveData.observe(this, notes -> {
                this.notes = notes;
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                Note note;
                noteDate = new NoteDate(dayOfMonth, month, year);

                note = notes.stream()
                        .filter(note_ -> noteDate.equals(note_.getDate()))
                        .findFirst()
                        .orElse(null);
                if (note != null){
                    textInputEditText.setText(note.getNoteText());
                } else {
                    textInputEditText.setText("");
                }
            }
        });
    }

    public void AddRewriteNote(View view) {

        if (textInputEditText.getText() != null && noteDate != null){
            String text = textInputEditText.getText().toString();
            Note note = new Note(noteDate, text);
            viewModel.addNewNote(note);
            Toast.makeText(this, "Add/Edit", Toast.LENGTH_LONG).show();
        }
    }

    public void DeleteNote(View view) {
        if (textInputEditText.getText() != null && noteDate != null){
            viewModel.removeNote(noteDate);
            Toast.makeText(this, "Remove", Toast.LENGTH_LONG).show();
            textInputEditText.setText("");
        }
    }


    private boolean checkPermissions(){

        if(!viewModel.isExternalStorageReadable() || !viewModel.isExternalStorageWriteable()){
            Toast.makeText(this, "Внешнее хранилище не доступно", Toast.LENGTH_LONG).show();
            return false;
        }
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_WRITE);
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            case REQUEST_PERMISSION_WRITE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Разрешения получены", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(this, "Необходимо дать разрешения", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}