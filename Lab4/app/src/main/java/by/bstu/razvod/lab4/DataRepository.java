package by.bstu.razvod.lab4;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    private DatabaseReference mydb;
    final FirebaseAuth auth = FirebaseAuth.getInstance();
    private String CONTACT_KEY = auth.getCurrentUser().getUid();

    private Context context;

    @Inject
    DataRepository(@ApplicationContext Context context) {
        this.context = context;
       // getData();

        mydb = FirebaseDatabase.getInstance().getReference(CONTACT_KEY);
    }

    public BehaviorSubject<List<ContactModel>> contactLiveData = BehaviorSubject.createDefault(new ArrayList<ContactModel>());

    public void addNewContact(ContactModel contactModel) {
        contacts.add(contactModel);
        setNewContact();
        writeData(contactModel);
    }

    public void addContact(ContactModel contactModel) {
        contacts.add(contactModel);
        DatabaseReference ref = mydb.push();
        setNewContact();
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
            DatabaseReference ref = mydb.push();

            mydb.push().setValue(newContact);

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
