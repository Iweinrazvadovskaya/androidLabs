package by.bstu.razvod.lab4.details;

import android.util.Log;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.TimeUnit;

import by.bstu.razvod.lab4.database.ContactEntity;
import by.bstu.razvod.lab4.database.LearnDataSource;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class DetailsViewModel extends ViewModel {

    private LearnDataSource learnDataSource;

    private BehaviorSubject<Long> selectedIDState = BehaviorSubject.create();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private MutableLiveData<ContactEntity> stateLiveData = new MutableLiveData<>();

    private static String TAG = "TAG";

    @ViewModelInject
    DetailsViewModel(LearnDataSource dataSource, @Assisted SavedStateHandle savedStateHandle) {
        this.learnDataSource = dataSource;
        compositeDisposable.add(selectedIDState
                .sample(150, TimeUnit.MILLISECONDS)
                .flatMap(newId -> {
                    return learnDataSource.getById(newId);
                }).subscribe(contactEntity -> {
                    stateLiveData.postValue(contactEntity);
                }, error -> {
                    Log.d(TAG, error.toString());
                }));
    }

    void initialize(Long idToFetch) {
        selectedIDState.onNext(idToFetch);
    }

    public LiveData<ContactEntity> getContact() {
        return stateLiveData;
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
