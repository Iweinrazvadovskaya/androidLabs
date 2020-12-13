package by.bstu.razvod.lab4.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

@Dao
public interface LearnDao {
    @Query("SELECT * FROM contacts;")
    Observable<List<ContactEntity>> getData() ;

    @Insert
    Completable insert(ContactEntity ... contactEntity);

    @Update
    Completable update(ContactEntity contactEntity);

    @Query("Delete FROM contacts WHERE contactID = :id")
    Completable delete(long id);

//    void writeData();
    @Query("SELECT * FROM contacts WHERE contact_name like :contact_name")
    Observable<List<ContactEntity>> findElement(String contact_name);

    @Query("SELECT * FROM contacts WHERE contactID = :id")
    Observable<ContactEntity> getById(long id);
}
