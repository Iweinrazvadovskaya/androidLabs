package by.bstu.razvod.listtviewcontextapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextWatcher secondsWatcher;
    private EditText seconds;
    private ListView listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = (ListView) findViewById(R.id.listview);

        ListAdapter adapter = new ListAdapter(this, new ArrayList<>(), mainViewPresentation -> {
            viewModel.changeSelection(mainViewPresentation);
        }, new ContextMenuListener() {
            @Override
            public void remove(MainViewPresentation presentation) {
                viewModel.deleteContact(presentation);
            }

            @Override
            public void edit(MainViewPresentation presentation) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("number", presentation.getModel().getPhoneNumber());
                clipboard.setPrimaryClip(clip);
            }
        });
        listview.setAdapter(adapter);
    }
}