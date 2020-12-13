package by.bstu.razvod.lab4.main;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import by.bstu.razvod.lab4.MainViewPresentation;
import by.bstu.razvod.lab4.R;
import by.bstu.razvod.lab4.contact.AddContactActivity;
import by.bstu.razvod.lab4.details.DetailsActivity;
import by.bstu.razvod.lab4.extendes.ContextMenuListener;
import by.bstu.razvod.lab4.extendes.ListAdapter;
import by.bstu.razvod.lab4.model.ContactModel;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private ListView listview;

    @Nullable
    private DatabaseReference observerRef;

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private String CONTACT_KEY = auth.getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        observerRef = FirebaseDatabase.getInstance().getReference(CONTACT_KEY);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setContentView(R.layout.activity_main);
        listview = (ListView) findViewById(R.id.listview);

        ListAdapter adapter = new ListAdapter(this, new ArrayList<>(), mainViewPresentation -> {
            viewModel.changeSelection(mainViewPresentation);
        }, presentation -> {
            Intent intent = new Intent(this, DetailsActivity.class);
            int id = presentation.getModel().getId();
            intent.putExtra("id", id);
            startActivity(intent);
        }, new ContextMenuListener() {
            @Override
            public void remove(MainViewPresentation presentation) {
                viewModel.deleteContact(presentation);
            }

            @Override
            public void copy(MainViewPresentation presentation) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("number", presentation.getModel().getPhoneNumber());
                clipboard.setPrimaryClip(clip);
            }
        });
        listview.setAdapter(adapter);

        viewModel.contactLiveData.observe(this, contactModels -> {
            adapter.setItems(contactModels);

            adapter.notifyDataSetChanged();
        });

        getDate();

    }

    private ValueEventListener valueEventListener;

    private void getDate() {
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<ContactModel> contactModelList = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ContactModel contact = ds.getValue(ContactModel.class);
                    assert contact != null;
                    contactModelList.add(contact);
                }
                viewModel.swapContacts(contactModelList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        if (valueEventListener != null) {
            observerRef.addValueEventListener(valueEventListener);
        }
    }

    @Override
    protected void onDestroy() {
        if (valueEventListener != null) {
            observerRef.removeEventListener(valueEventListener);
        }
        super.onDestroy();
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.layout.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(this, AddContactActivity.class);
                startActivity(intent);
                break;
            case R.id.remove:

                for (int i = 0; i < viewModel.contactLiveData.getValue().size(); i++) {
                    if (viewModel.contactLiveData.getValue().get(i).isSelected()) {
                        viewModel.deleteContact(viewModel.contactLiveData.getValue().get(i));
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}