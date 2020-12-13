package by.bstu.razvod.lab4.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import by.bstu.razvod.lab4.MainViewPresentation;
import by.bstu.razvod.lab4.database.ContactEntity;
import by.bstu.razvod.lab4.database.LearnDataSource;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class MainViewModel extends AndroidViewModel {

//    private DataSqlRepository dataRepository;
    private LearnDataSource learnDataSource;

    private BehaviorSubject<ArrayList<ContactEntity>> selectedSubject = BehaviorSubject.createDefault(new ArrayList<ContactEntity>());
    public MutableLiveData<Boolean> showFavorite = new MutableLiveData<>();

    @ViewModelInject
    public MainViewModel(@NonNull Application application, LearnDataSource learnDataSource) {
        super(application);
//        this.dataRepository = dataRepository;
        compositeDisposable.add(Observable.combineLatest(learnDataSource.getData(), selectedSubject, (contactModels, selected) -> {
            return contactModels.stream().map(item -> new MainViewPresentation(item, selected.contains(item))).collect(Collectors.toCollection(ArrayList::new));
        }).debounce(50, TimeUnit.MILLISECONDS)
                .subscribe(list -> {
                    contactLiveData.postValue(list);
                }));
    }

    public void updateDataSet(ContactEntity newContact) {
        ArrayList<ContactEntity> list = selectedSubject.getValue();
        List<ContactEntity> newList = list.stream().map(new Function<ContactEntity, ContactEntity>() {

            @Override
            public ContactEntity apply(ContactEntity contactModel) {
                if (newContact.contactID == contactModel.contactID) {
                    return newContact;
                }
                else {
                    return contactModel;
                }
            }

        }).collect(Collectors.toList());
        selectedSubject.onNext(new ArrayList<>(newList));
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

    public MutableLiveData<List<MainViewPresentation>> contactLiveData = new MutableLiveData<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }


    public void addNewContact(ContactEntity contactEntity) {
        learnDataSource.insert(contactEntity);
    }

    public void findContact(String name) {
        learnDataSource.findElement(name);
    }

    public Completable editContact(ContactEntity contactEntity) {
        return learnDataSource.update(contactEntity);
    }

    public Completable makeFavorite(MainViewPresentation mainViewPresentation) {
        ContactEntity contactModel = mainViewPresentation.getModel();
//        contactModel.setFavoriteContact();
        return learnDataSource.update(contactModel);
    }

//    public void showFavorite() {
//        showFavorite.postValue(learnDataSource.selectFavorite());
//    }

    public Observable<ContactEntity> getContact(long id){
        return learnDataSource.getById(id);
    }

    public void deleteContact(MainViewPresentation mainViewPresentation) {
        learnDataSource.delete(mainViewPresentation.getModel().contactID);

        if (selectedSubject.getValue().contains(mainViewPresentation.getModel())) {
            ArrayList<ContactEntity> list = selectedSubject.getValue();
            list.remove(mainViewPresentation.getModel());
            selectedSubject.onNext(list);
        }
    }

}
