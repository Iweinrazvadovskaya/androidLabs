package by.bstu.razvod.lab4.database;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.Executors;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

class LearnDataSourceImplementation extends LearnDataSource {

    @NonNull
    private final LearnDao learnDao;

    @NonNull
    private final Scheduler databaseScheduler = Schedulers.from(Executors.newFixedThreadPool(8));

    LearnDataSourceImplementation(@NonNull LearnDao learnDao) {
        this.learnDao = learnDao;
    }

    @Override
    public Observable<List<ContactEntity>> getData() {
        return learnDao.getData()
                .subscribeOn(databaseScheduler);
    }

    @Override
    public Completable insert(ContactEntity... contactEntity) {
        return learnDao.insert(contactEntity)
                .subscribeOn(databaseScheduler);
    }

    @Override
    public Completable update(ContactEntity contactEntity) {
        return learnDao.update(contactEntity)
                .subscribeOn(databaseScheduler);
    }

    @Override
    public Observable<List<ContactEntity>> findElement(String contactName) {
        return learnDao.findElement("%" + contactName + "%")
                .subscribeOn(databaseScheduler);
    }

    @Override
    public Completable delete(long id) {
        return learnDao.delete(id)
                .subscribeOn(databaseScheduler);
    }

    @Override
    public Observable<ContactEntity> getById(long id) {
        return learnDao.getById(id)
                .subscribeOn(databaseScheduler);
    }
}
