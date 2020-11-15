package by.bstu.razvod.lab4.list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import by.bstu.razvod.lab4.DataRepository;
import by.bstu.razvod.lab4.MainViewPresentation;
import by.bstu.razvod.lab4.model.ContactModel;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class ListViewModel extends AndroidViewModel {

    private DataRepository dataRepository;

    private BehaviorSubject<ArrayList<ContactModel>> selectedSubject = BehaviorSubject.createDefault(new ArrayList<ContactModel>());

    @ViewModelInject
    public ListViewModel(@NonNull Application application, DataRepository dataRepository) {
        super(application);
        this.dataRepository = dataRepository;
        compositeDisposable.add(Observable.combineLatest(dataRepository.contactLiveData, selectedSubject, (contactModels, selected) -> {
            return contactModels.stream().map(item -> new MainViewPresentation(item, selected.contains(item))).collect(Collectors.toCollection(ArrayList::new));
        }).debounce(50, TimeUnit.MILLISECONDS)
                .subscribe(list -> {
                    contactLiveData.postValue(list);
                }));
    }

    public void changeSelection(MainViewPresentation mainViewPresentation) {
        ArrayList<ContactModel> list = selectedSubject.getValue();
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


    public void addNewContact(ContactModel contactModel) {
        dataRepository.addNewContact(contactModel);
    }

    public ContactModel getContact(int id){
        return dataRepository.getContact(id);
    }

    public void deleteContact(MainViewPresentation mainViewPresentation) {
        dataRepository.removeContact(mainViewPresentation.getModel());

        if (selectedSubject.getValue().contains(mainViewPresentation.getModel())) {
            ArrayList<ContactModel> list = selectedSubject.getValue();
            list.remove(mainViewPresentation.getModel());
            selectedSubject.onNext(list);
        }
    }

}
