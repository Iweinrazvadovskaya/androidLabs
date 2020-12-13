package by.bstu.razvod.lab4.contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;

import by.bstu.razvod.lab4.MainViewPresentation;
import by.bstu.razvod.lab4.R;
import by.bstu.razvod.lab4.details.DetailsActivity;
import by.bstu.razvod.lab4.main.MainViewModel;
import by.bstu.razvod.lab4.model.ContactModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddContactActivity extends AppCompatActivity {

    private MainViewModel viewModel;

    private TextInputEditText nameInput;
    private EditText phoneNumber;
    private TextInputEditText emailInput;
    private TextInputEditText locationInput;
    private TextInputEditText socialLinkInput;
    private ContactModel contactModel = null;
    private Button createContact;

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

        int idFromExtra = getIntent().getIntExtra("id", -1);
        if (idFromExtra != -1) {
            contactModel = viewModel.getContact(idFromExtra);
            createContact.setText(R.string.action_edit);
            nameInput.setText(contactModel.getContactName());
            phoneNumber.setText(contactModel.getPhoneNumber());
            emailInput.setText(contactModel.getEmail());
            locationInput.setText(contactModel.getLocation());
            socialLinkInput.setText(contactModel.getLinkSocialNetwork());
        }

    }

    public void addContact(View view) {
        int id;
        if (contactModel != null){
            MainViewPresentation contactPresentation = viewModel.contactLiveData.getValue().stream()
                    .filter(contact__ -> contactModel.getId() == (contact__.getModel().getId()))
                    .findAny()
                    .orElse(null);
            contactPresentation.setSelected(true);
            viewModel.changeSelection(contactPresentation);
            if (contactPresentation != null){
                viewModel.deleteContact(contactPresentation);
            }
            id = contactModel.getId();
        } else {
            id = viewModel.contactLiveData.getValue().size();
        }

        ContactModel c = new ContactModel(id, nameInput.getText().toString(), emailInput.getText().toString(),
                locationInput.getText().toString(), phoneNumber.getText().toString(),
                socialLinkInput.getText().toString());
        viewModel.addNewContact(c);

        if (contactModel != null) {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }
        finish();
    }
}