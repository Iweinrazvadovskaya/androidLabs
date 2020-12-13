package by.bstu.razvod.lab4.details;

import androidx.hilt.Assisted;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import by.bstu.razvod.lab4.DataSqlRepository;
import by.bstu.razvod.lab4.model.ContactModel;

public class DetailsViewModel extends ViewModel {

    private DataSqlRepository dataRepository;

    @ViewModelInject
    DetailsViewModel(DataSqlRepository dataRepo, @Assisted SavedStateHandle savedStateHandle) {
        this.dataRepository = dataRepo;
    }

    public ContactModel getContact(Long id){
        return dataRepository.getContact(id);
    }

}
