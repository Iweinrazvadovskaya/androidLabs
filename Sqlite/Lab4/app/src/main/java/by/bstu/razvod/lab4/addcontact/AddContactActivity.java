package by.bstu.razvod.lab4.addcontact;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import by.bstu.razvod.lab4.R;
import by.bstu.razvod.lab4.database.ContactEntity;
import by.bstu.razvod.lab4.details.DetailsFragment;
import by.bstu.razvod.lab4.main.MainViewModel;
import by.bstu.razvod.lab4.util.AutoDisposableCompletableObserver;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

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
    private ContactEntity contactModel_ = null;

    private static String TAG = "TAG";

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

        viewModel.contactsLiveData.observe(this, mainViewPresentations -> {
            if (mainViewPresentations.isEmpty()) {
                return;
            }
            long idFromExtra = getIntent().getLongExtra("id", -1);
            ContactEntity contactModel = mainViewPresentations.stream().findFirst().get().getModel();
            if (idFromExtra == -1) {
                contactModel_ = null;
            } else {
                contactModel_ = contactModel;
            }
            if (idFromExtra != -1) {
                compositeDisposable.add(viewModel.getContact(idFromExtra)
                        .subscribe( contactEntity -> {
                            contactModel_ = contactEntity;
                        }, error -> {
                            Log.d(TAG, error.toString());
                        }));
                contactModel_ = contactModel;
                createContact.setText(getString(R.string.action_edit));
                nameInput.setText(contactModel.getContactName());
                phoneNumber.setText(contactModel.getPhoneNumber());
                emailInput.setText(contactModel.getContactEmail());
                locationInput.setText(contactModel.getContactLocation());
                socialLinkInput.setText(contactModel.getLinkSocialNetwork());
            }
        });
    }

    public void AddContact(View view) {
        long id;
        if (contactModel_ != null) {
            id = contactModel_.getContactID();
            ContactEntity c = new ContactEntity(id, nameInput.getText().toString(),
                    emailInput.getText().toString(), locationInput.getText().toString(),
                    phoneNumber.getText().toString(),
                    socialLinkInput.getText().toString(), 0);
            viewModel.updateDataSet(c);
            Disposable disposable = viewModel.editContact(c)
                    .toSingleDefault("")
                    .subscribe(s -> {
                        if (contactModel_ != null) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frame_layout, DetailsFragment.newInstance(id), "tag")
                                    .commit();
                        }
                        finish();
                    });
            compositeDisposable.add(disposable);
        } else {
            ContactEntity c = new ContactEntity(0, nameInput.getText().toString(),
                    emailInput.getText().toString(), locationInput.getText().toString(),
                    phoneNumber.getText().toString(),
                    socialLinkInput.getText().toString(), 0);
            viewModel.addNewContact(c)
                    .subscribe(new AutoDisposableCompletableObserver() {
                        @Override
                        public void onComplete() {
                            super.onComplete();
                            finish();
                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}