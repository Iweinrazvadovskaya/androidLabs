package by.bstu.razvod.lab4.database;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

 abstract public class LearnDataSource {
    public abstract Observable<List<ContactEntity>> getData() ;
    public abstract Completable insert(ContactEntity ... contactEntity);
    public abstract Completable update(ContactEntity contactEntity);
    public abstract Observable<List<ContactEntity>> findElement(String contactName);
    public abstract Completable delete(long id);
    public abstract Observable<ContactEntity> getById(long id);
}
