package by.bstu.razvod.lab4;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import by.bstu.razvod.lab4.model.ContactModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

import static android.content.Context.MODE_PRIVATE;

@Singleton

public class DataSqlRepository {

        private List<ContactModel> contacts = new ArrayList<>();
        private static final String FILE_NAME = "contact.db";

        private Context context;

        @Inject
        DataSqlRepository(@ApplicationContext Context context) {
            this.context = context;
            DBcontext();
            getData();
        }

        public BehaviorSubject<List<ContactModel>> contactLiveData = BehaviorSubject.createDefault(new ArrayList<ContactModel>());


    public void DBcontext()
    {
        SQLiteDatabase db = context.openOrCreateDatabase(FILE_NAME, MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS contacts ("+
                "id integer primary key," +
                "name varchar(50),"+
                "email varchar(100),"+
                "location varchar(100),"+
                "phone varchar(50),"+
                "profile varchar(100)"+
                ");");

    }
        public void addNewContact(ContactModel contactModel) {
            contacts.add(contactModel);
            setNewContact(contacts);
            writeData(this.contacts);
        }

        public void removeContact(ContactModel contactModel) {
            ContactModel contact = contacts.stream()
                    .filter(contact__ -> contactModel.equals(contact__))
                    .findAny()
                    .orElse(null);
            this.contacts.remove(contact);
            setNewContact(contacts);
            writeData(this.contacts);
        }

        private void writeData(@NonNull List<ContactModel> newContact) {
            assert newContact != null;
            try {
        SQLiteDatabase db = context.openOrCreateDatabase(FILE_NAME, MODE_PRIVATE, null);

                for (int i = 0; i < newContact.size(); i++) {

                    db.execSQL("INSERT INTO contacts (name, id, email, location, phone, profile) VALUES" +
                            "('" + newContact.get(i).getContactName() + "'," +
                            "'" + newContact.get(i).getId() + "'," +
                            "'" + newContact.get(i).getEmail() + "'," +
                            "'" +  newContact.get(i).getLocation() + "'," +
                            "'" + newContact.get(i).getPhoneNumber() + "'," +
                            "'" + newContact.get(i).getLinkSocialNetwork() + "')");
                }
                setNewContact(newContact);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        private void getData() {
            try {
                SQLiteDatabase db = context.openOrCreateDatabase(FILE_NAME, MODE_PRIVATE, null);
                List<ContactModel> contactModels = new ArrayList<>();
        Cursor getAllData = db.rawQuery("SELECT * FROM contacts;",null);

                        if(getAllData.moveToFirst()){
            do{
                contactModels.add(new ContactModel(getAllData.getInt(0),
                        getAllData.getString(1),
                        getAllData.getString(2),
                        getAllData.getString(3),
                        getAllData.getString(4),
                        getAllData.getString(5)));

            }while(getAllData.moveToNext());
        }

                if (contactModels != null) {
                    setNewContact(contactModels);
                }
                getAllData.close();
                db.close();
            } catch (Exception ignored) {

            }

        }

        public ContactModel getContact(int id){
            return contacts.stream()
                    .filter(contact__ -> contact__.getId() == id)
                    .findAny()
                    .orElse(null);
        }

        private void setNewContact(@NonNull List<ContactModel> contacts) {
            this.contacts = contacts;
            contactLiveData.onNext(this.contacts);
        }

}
