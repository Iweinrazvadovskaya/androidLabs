package by.bstu.razvod.lab4.addContact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import by.bstu.razvod.lab4.MainViewPresentation;
import by.bstu.razvod.lab4.R;
import by.bstu.razvod.lab4.database.ContactEntity;
import by.bstu.razvod.lab4.details.DetailsFragment;
import by.bstu.razvod.lab4.main.MainActivity;
import by.bstu.razvod.lab4.main.MainViewModel;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

@AndroidEntryPoint
public class AddContactActivity extends AppCompatActivity {

    private MainViewModel viewModel;

    private TextInputEditText nameInput;
    private EditText phoneNumber;
    private TextInputEditText emailInput;
    private TextInputEditText locationInput;
    private TextInputEditText socialLinkInput;
    private Button createContact;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    ContactEntity contactModel_ = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        createContact = (Button) findViewById(R.id.createContact);
        nameInput = (TextInputEditText) findViewById(R.id.name);
        phoneNumber = (EditText) findViewById(R.id.phoneInput);
        emailInput = (TextInputEditText) findViewById(R.id.email);
        socialLinkInput = (TextInputEditText) findViewById(R.id.slink);
        locationInput = (TextInputEditText) findViewById(R.id.location);

        viewModel.contactLiveData.observe(this, new Observer<List<MainViewPresentation>>() {
            @Override
            public void onChanged(List<MainViewPresentation> mainViewPresentations) {
                if(mainViewPresentations.isEmpty()){
                    return;
                }
                long idFromExtra = getIntent().getLongExtra("id", -1);
                ContactEntity contactModel = mainViewPresentations.stream().findFirst().get().getModel();
                if (idFromExtra == -1){
                    contactModel_ = null;
                } else {
                    contactModel_ = contactModel;
                }
                if (idFromExtra != -1) {
                    viewModel.getContact(idFromExtra).subscribe(new io.reactivex.rxjava3.core.Observer<ContactEntity>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                        }

                        @Override
                        public void onNext(@NonNull ContactEntity contactEntity) {
                            contactModel_ = contactEntity;
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                        }
                    });
                    contactModel_ = contactModel;
                    createContact.setText("Edit");
                    nameInput.setText(contactModel.contactName);
                    phoneNumber.setText(contactModel.phoneNumber);
                    emailInput.setText(contactModel.contactEmail);
                    locationInput.setText(contactModel.contactLocation);
                    socialLinkInput.setText(contactModel.linkSocialNetwork);
                }
            }
        });
    }

    public void AddContact(View view) {
        long id;
        if (contactModel_ != null){
            id = contactModel_.contactID;
            ContactEntity c = new ContactEntity(id, nameInput.getText().toString(), emailInput.getText().toString(), locationInput.getText().toString(), phoneNumber.getText().toString(), socialLinkInput.getText().toString(), 0);
            viewModel.updateDataSet(c);
            Disposable disposable =  viewModel.editContact(c)
                    .toSingleDefault("")
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Throwable {
                            if (contactModel_ != null) {
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.frame_layout, DetailsFragment.newInstance(id), "tag")
                                        .commit();
                            }
                            finish();
                        }
                    });
            compositeDisposable.add(disposable);
        } else {
            id = viewModel.contactLiveData.getValue().size();
            ContactEntity c = new ContactEntity(id, nameInput.getText().toString(), emailInput.getText().toString(), locationInput.getText().toString(), phoneNumber.getText().toString(), socialLinkInput.getText().toString(), 0);
            viewModel.addNewContact(c);
            Intent intent = new Intent(AddContactActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}