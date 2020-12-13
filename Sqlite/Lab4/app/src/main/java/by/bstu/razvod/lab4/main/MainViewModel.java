package by.bstu.razvod.lab4.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import by.bstu.razvod.lab4.MainViewPresentation;
import by.bstu.razvod.lab4.database.ContactEntity;
import by.bstu.razvod.lab4.database.LearnDataSource;
import by.bstu.razvod.lab4.util.AutoDisposableCompletableObserver;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class MainViewModel extends AndroidViewModel {

    //    private DataSqlRepository dataRepository;
    private LearnDataSource learnDataSource;

    private BehaviorSubject<ArrayList<ContactEntity>> selectedSubject = BehaviorSubject.createDefault(new ArrayList<ContactEntity>());
    public MutableLiveData<Boolean> showFavorite = new MutableLiveData<>();

    private BehaviorSubject<String> queryState = BehaviorSubject.createDefault("");

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @ViewModelInject
    public MainViewModel(@NonNull Application application, LearnDataSource learnDataSource) {
        super(application);
//        this.dataRepository = dataRepository;
        this.learnDataSource = learnDataSource;
        Disposable disposable = Observable.combineLatest(queryState.debounce(150, TimeUnit.MILLISECONDS)
                .flatMap(query -> {
                    if (query.isEmpty()) {
                        return learnDataSource.getData();
                    } else {
                        return learnDataSource.findElement(query);
                    }
                }), selectedSubject, (contactModels, selected) -> {
            return contactModels.stream().map(item -> new MainViewPresentation(item, selected.contains(item))).collect(Collectors.toCollection(ArrayList::new));
        }).debounce(50, TimeUnit.MILLISECONDS)
                .subscribe(list -> {
                    contactsLiveData.postValue(list);
                });
        compositeDisposable.add(disposable);
    }

    public void updateDataSet(ContactEntity newContact) {
        learnDataSource.update(newContact)
                .subscribe(new AutoDisposableCompletableObserver(compositeDisposable) {
                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                });
    }

    public void changeSelection(MainViewPresentation mainViewPresentation) {
        ArrayList<ContactEntity> list = selectedSubject.getValue();
        if (list.contains(mainViewPresentation.getModel())) {
            list.remove(mainViewPresentation.getModel());
        } else {
            list.add(mainViewPresentation.getModel());
        }
        selectedSubject.onNext(list);
    }

    public MutableLiveData<List<MainViewPresentation>> contactsLiveData = new MutableLiveData<>();

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }


    public Completable addNewContact(ContactEntity contactEntity) {
        return learnDataSource.insert(contactEntity);
    }

    public void submitQuery(String name) {
        queryState.onNext(name);
    }

    public Completable editContact(ContactEntity contactEntity) {
        return learnDataSource.update(contactEntity);
    }

    public Completable makeFavorite(MainViewPresentation mainViewPresentation) {
        ContactEntity contactModel = mainViewPresentation.getModel();
        return learnDataSource.update(contactModel);
    }

    public Observable<ContactEntity> getContact(long id) {
        return learnDataSource.getById(id);
    }

    public void deleteContact(MainViewPresentation mainViewPresentation) {
        learnDataSource.delete(mainViewPresentation.getModel().getContactID())
                .subscribe(new AutoDisposableCompletableObserver(compositeDisposable) {
                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if (selectedSubject.getValue().contains(mainViewPresentation.getModel())) {
                            ArrayList<ContactEntity> list = selectedSubject.getValue();
                            list.remove(mainViewPresentation.getModel());
                            selectedSubject.onNext(list);
                        }
                    }
                });
    }


}
