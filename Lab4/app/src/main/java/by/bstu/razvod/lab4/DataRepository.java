package by.bstu.razvod.lab4;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import by.bstu.razvod.lab4.model.ContactModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

@Singleton
public class DataRepository {


    private List<ContactModel> contacts = new ArrayList<>();
    private static final String FILE_NAME = "Contact-file";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    private Context context;

    @Inject
    DataRepository(@ApplicationContext Context context) {
        this.context = context;
        getData();
    }

    public BehaviorSubject<List<ContactModel>> contactLiveData = BehaviorSubject.createDefault(new ArrayList<ContactModel>());

    public void addNewContact(ContactModel contactModel) {
        contacts.add(contactModel);
        setNewContact();
        writeData(contactModel);
    }

    public void removeContact(ContactModel contactModel) {
        ContactModel contact = contacts.stream()
                .filter(contact__ -> contactModel.equals(contact__))
                .findAny()
                .orElse(null);
        this.contacts.remove(contact);
        setNewContact();
        // writeData(this.contacts);
    }

    private void writeData(ContactModel newContact) {
        assert newContact != null;
        try {
//            FileOutputStream file = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
//            String jsonValue = new Gson().toJson(newContact);
//            file.write(jsonValue.getBytes(), 0, jsonValue.length());
//            file.close();

            myRef.push().setValue(newContact);

            setNewContact();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
//        catch (IOException e) {
//            e.printStackTrace();
    }



    private void getData() {
//        try {
//            FileInputStream file = context.openFileInput(FILE_NAME);
//            String fileString = getFileContent(file, "UTF-8");
//            List<ContactModel> contactModels = new Gson().fromJson(fileString, new TypeToken<List<ContactModel>>() {}.getType());
//            if (contactModels != null) {
//                setNewContact(contactModels);
//            }
//            file.close();
//        } catch (Exception ignored) {
//
//        }

        try {


            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<ContactModel> contactModels = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        ContactModel contact = ds.getValue(ContactModel.class);
                        assert contact != null;
                        contactModels.add(contact);
                    }
                    if (contactModels != null) {
                        setNewContact();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            myRef.addValueEventListener(valueEventListener);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public ContactModel getContact(int id){
        return contacts.stream()
                .filter(contact__ -> contact__.getId() == id)
                .findAny()
                .orElse(null);
    }

    private void setNewContact() {
//        this.contacts.add(contact);
        contactLiveData.onNext(this.contacts);
    }

    private static String getFileContent(
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
