package by.bstu.razvod.lab4.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import by.bstu.razvod.lab4.R;
import by.bstu.razvod.lab4.contact.AddContactActivity;
import by.bstu.razvod.lab4.main.MainViewModel;
import by.bstu.razvod.lab4.model.ContactModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DetailsActivity extends AppCompatActivity {

    public static ContactModel contactModel;
    private MainViewModel viewModel;

    private TextView name;
    private TextView phoneNumber;
    private TextView email;
    private TextView location;
    private TextView socialLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        int id = getIntent().getIntExtra("id", 0);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        name = (TextView) findViewById(R.id.name);
        phoneNumber = (TextView) findViewById(R.id.phone);
        email = (TextView) findViewById(R.id.mail);
        socialLink = (TextView) findViewById(R.id.slink);
        location = (TextView) findViewById(R.id.location);

        contactModel = viewModel.getContact(id);

        name.setText(contactModel.getContactName());
        phoneNumber.setText(contactModel.getPhoneNumber());
        email.setText(contactModel.getEmail());
        location.setText(contactModel.getLocation());
        socialLink.setText(contactModel.getLinkSocialNetwork());

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMap(v);
            }
        });
    }

    public void goToMap(View view){
        String geo = "geo:0,0?q=" + location.getText().toString();
        Uri geoUri =Uri.parse(geo);
        Intent intent = new Intent(Intent.ACTION_VIEW, geoUri);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }

        startActivity(intent);
    }

    public void goEdit(View view) {
        Intent intent = new Intent(this, AddContactActivity.class);
        intent.putExtra("id", contactModel.getId());
        startActivity(intent);
        finish();
    }
}