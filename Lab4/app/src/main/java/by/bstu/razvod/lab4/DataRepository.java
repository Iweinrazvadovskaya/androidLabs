package by.bstu.razvod.lab4;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import by.bstu.razvod.lab4.model.ContactModel;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

@Singleton
public class DataRepository {
    private List<ContactModel> contacts = new ArrayList<>();
    private DatabaseReference mydb;
    final FirebaseAuth auth = FirebaseAuth.getInstance();
    private String CONTACT_KEY = auth.getCurrentUser().getUid();

    @Inject
    DataRepository() {
        mydb = FirebaseDatabase.getInstance().getReference(CONTACT_KEY);
    }

    public BehaviorSubject<List<ContactModel>> contactLiveData = BehaviorSubject.createDefault(new ArrayList<ContactModel>());

    public void insertContact(ContactModel contactModel) {
      //  contacts.add(contactModel);
        setNewContact();
        writeData(contactModel);
    }

    public void swapContacts(List<ContactModel> contactModel) {
        contacts.clear();
        contacts.addAll(contactModel);
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
            mydb.push().setValue(newContact);

            setNewContact();
        }
        catch (Exception e) {
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
        contactLiveData.onNext(this.contacts);
    }
}
