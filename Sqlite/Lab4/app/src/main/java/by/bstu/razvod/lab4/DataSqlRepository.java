package by.bstu.razvod.lab4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import by.bstu.razvod.lab4.model.ContactModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

import static android.content.Context.MODE_PRIVATE;

@Singleton
public class DataSqlRepository {

    private List<ContactModel> contacts = new ArrayList<>();
    private static final String FILE_NAME = "contact1.db";
    private SQLiteDatabase _db;
    private Context context;
    private Object mutex = new Object();

    @Inject
    DataSqlRepository(@ApplicationContext Context context) {
        this.context = context;
        _db = context.openOrCreateDatabase(FILE_NAME, MODE_PRIVATE, null);
        DBcontext();
        getData();
    }

    public BehaviorSubject<List<ContactModel>> contactLiveData = BehaviorSubject.createDefault(new ArrayList<ContactModel>());


    public void DBcontext() {
        _db = context.openOrCreateDatabase(FILE_NAME, MODE_PRIVATE, null);
        _db.execSQL("CREATE TABLE IF NOT EXISTS contacts (" +
                "id integer primary key," +
                "name varchar(50)," +
                "email varchar(100)," +
                "location varchar(100)," +
                "phone varchar(50)," +
                "profile varchar(100)," +
                "favoriteContact integer" +
                ");");

    }

    public void addNewContact(ContactModel contactModel) {
        contacts.add(contactModel);
        writeData(contactModel);
    }

    public void removeContact(ContactModel contactModel) {
        ContactModel contact = contacts.stream()
                .filter(contact__ -> contactModel.equals(contact__))
                .findAny()
                .orElse(null);
        this.contacts.remove(contact);
        removeData(contactModel);
    }

    public Completable updateContact(ContactModel contactModel) {
        ContactModel contact = contacts.stream()
                .filter(contact__ -> contactModel.getId() == contact__.getId())
                .findAny()
                .orElse(null);
        this.contacts.remove(contact);
        contacts.add(contactModel);
        return updateData(contactModel);
    }

    private void removeData(@NonNull ContactModel newContact) {
        try {
            _db = context.openOrCreateDatabase(FILE_NAME, MODE_PRIVATE, null);
            _db.delete("contacts", "id = " + newContact.getId(), null);
            setNewContact();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeQuietly();
        }
    }

    private void closeQuietly() {
        if (_db != null) {
            try {
                _db.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void findElement(String name) {
        try {
            _db = context.openOrCreateDatabase(FILE_NAME, MODE_PRIVATE, null);
            Cursor cursor = _db.rawQuery("SELECT * FROM contacts WHERE name like '%" + name + "%'", null);
            List<ContactModel> contactModels = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    contactModels.add(new ContactModel((long)cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getInt(6)));

                } while (cursor.moveToNext());
            }

            if (contactModels != null) {
                contacts = contactModels;
                setNewContact();
            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeQuietly();
        }
    }

    private void writeData(@NonNull ContactModel newContact) {
        try {
            _db = context.openOrCreateDatabase(FILE_NAME, MODE_PRIVATE, null);
            ContentValues newValues = new ContentValues();
            newValues.put("id", newContact.getId());
            newValues.put("name", newContact.getContactName());
            newValues.put("email", newContact.getEmail());
            newValues.put("location", newContact.getLocation());
            newValues.put("phone", newContact.getPhoneNumber());
            newValues.put("profile", newContact.getLinkSocialNetwork());
            newValues.put("favoriteContact", newContact.getFavoritesContact());

            _db.insert("contacts", null, newValues);
            setNewContact();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeQuietly();
        }
    }

    private Completable updateData(@NonNull ContactModel contactModel) {
        return Completable.create(emitter -> {
            synchronized (mutex) {
                try {
                    _db = context.openOrCreateDatabase(FILE_NAME, MODE_PRIVATE, null);
                    ContentValues updatedValues = new ContentValues();

                    updatedValues.put("id", contactModel.getId());
                    updatedValues.put("name", contactModel.getContactName());
                    updatedValues.put("email", contactModel.getEmail());
                    updatedValues.put("location", contactModel.getLocation());
                    updatedValues.put("phone", contactModel.getPhoneNumber());
                    updatedValues.put("profile", contactModel.getLinkSocialNetwork());
                    updatedValues.put("favoriteContact", contactModel.getFavoritesContact());

                    String where = "id=" + contactModel.getId();

                    int result = _db.update("contacts", updatedValues, where, null);
                    Log.d("TAG", String.format("%d", result));
                    emitter.onComplete();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (!emitter.isDisposed()) {
                        emitter.onError(e);
                    }
                } finally {
                    closeQuietly();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    private void getData() {
        synchronized (mutex) {
            try {
                _db = context.openOrCreateDatabase(FILE_NAME, MODE_PRIVATE, null);
                List<ContactModel> contactModels = new ArrayList<>();
                Cursor getAllData = _db.rawQuery("SELECT * FROM contacts;", null);

                if (getAllData.moveToFirst()) {

                    do {
                        contactModels.add(new ContactModel((long)getAllData.getInt(0),
                                getAllData.getString(1),
                                getAllData.getString(2),
                                getAllData.getString(3),
                                getAllData.getString(4),
                                getAllData.getString(5),
                                getAllData.getInt(6)));

                    } while (getAllData.moveToNext());
                }

                if (contactModels != null) {
                    contacts = contactModels;
                    setNewContact();
                }
                getAllData.close();
            } catch (Exception ignored) {

            } finally {
                closeQuietly();
            }
        }
    }

    public boolean selectFavorite() {

        List<ContactModel> favoriteContacts = contacts.stream()
                .filter(contact__ -> contact__.getFavoritesContact() == 1)
                .collect(Collectors.toList());

        contacts = favoriteContacts;
        setNewContact();
        return true;
    }

    public boolean findElements(String name) {
        List<ContactModel> resultContacts = contacts.stream()
                .filter(contact__ -> contact__.getContactName().contains(name))
                .collect(Collectors.toList());

        this.contacts = resultContacts;
        setNewContact();
        return true;
    }

    public ContactModel getContact(Long id) {
        return contacts.stream()
                .filter(contact__ -> contact__.getId() == id)
                .findAny()
                .orElse(null);
    }

    private void setNewContact() {
        contactLiveData.onNext(this.contacts);
    }

}
