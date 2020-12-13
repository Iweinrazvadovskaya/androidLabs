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


    public void DBcontext() {
        SQLiteDatabase db = context.openOrCreateDatabase(FILE_NAME, MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS contacts (" +
                "id integer primary key," +
                "name varchar(50)," +
                "email varchar(100)," +
                "location varchar(100)," +
                "phone varchar(50)," +
                "profile varchar(100)" +
                ");");

    }

    private void getData() {
        try {
            SQLiteDatabase db = context.openOrCreateDatabase(FILE_NAME, MODE_PRIVATE, null);
            List<ContactModel> contactModels = new ArrayList<>();
            Cursor getAllData = db.rawQuery("SELECT * FROM contacts;", null);

            if (getAllData.moveToFirst()) {
                do {
                    contactModels.add(new ContactModel(getAllData.getInt(0),
                            getAllData.getString(1),
                            getAllData.getString(2),
                            getAllData.getString(3),
                            getAllData.getString(4),
                            getAllData.getString(5)));

                } while (getAllData.moveToNext());
            }

            if (contactModels != null) {
                setNewContact(contactModels);
            }
            getAllData.close();
            db.close();
        } catch (Exception ignored) {

        }
    }

    private void setNewContact(@NonNull List<ContactModel> contacts) {
        this.contacts = contacts;
        contactLiveData.onNext(this.contacts);
    }
}
