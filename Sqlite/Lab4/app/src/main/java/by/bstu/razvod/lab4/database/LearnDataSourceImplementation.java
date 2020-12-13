package by.bstu.razvod.lab4.database;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

class LearnDataSourceImplementation extends LearnDataSource {
    private LearnDao learnDao;
    LearnDataSourceImplementation(LearnDao learnDao){
        this.learnDao = learnDao;
    }

    @Override
    public Observable<List<ContactEntity>> getData() {
        return learnDao.getData();
    }

    @Override
    public Completable insert(ContactEntity... contactEntity) {
        return learnDao.insert(contactEntity);
    }

    @Override
    public Completable update(ContactEntity contactEntity) {
        return learnDao.update(contactEntity);
    }

    @Override
    public Observable<List<ContactEntity>> findElement(String contact_name) {
        return learnDao.findElement(contact_name);
    }

    @Override
    public Completable delete(long id) {
        return learnDao.delete(id);
    }

    @Override
    public Observable<ContactEntity> getById(long id) {
        return getById(id);
    }
}
