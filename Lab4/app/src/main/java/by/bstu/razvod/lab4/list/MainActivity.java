package by.bstu.razvod.lab4.list;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import by.bstu.razvod.lab4.MainViewPresentation;
import by.bstu.razvod.lab4.R;
import by.bstu.razvod.lab4.addContact.AddContactActivity;
import by.bstu.razvod.lab4.details.DetailsActivity;
import by.bstu.razvod.lab4.extendes.ContextMenuListener;
import by.bstu.razvod.lab4.extendes.ListAdapter;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ListViewModel viewModel;
    ListView listview;

    private FirebaseAuth mAuth;

    private DatabaseReference mydb;
    final FirebaseAuth auth = FirebaseAuth.getInstance();
    private String CONTACT_KEY = auth.getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mydb = FirebaseDatabase.getInstance().getReference(CONTACT_KEY);

        viewModel = new ViewModelProvider(this).get(ListViewModel.class);
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

    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.layout.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
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