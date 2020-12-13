package by.bstu.razvod.lab4.main;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

import by.bstu.razvod.lab4.MainViewPresentation;
import by.bstu.razvod.lab4.R;
import by.bstu.razvod.lab4.addcontact.AddContactActivity;
import by.bstu.razvod.lab4.details.DetailsFragment;
import by.bstu.razvod.lab4.extendes.ContextMenuListener;
import by.bstu.razvod.lab4.util.EmptyDisposableCompletableObserver;
import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private RecyclerView listview;
    private View itemFavorite;
    private GridLayoutManager layoutManager;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setContentView(R.layout.activity_main);
        listview = (RecyclerView) findViewById(R.id.listview);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, android.R.string.ok, android.R.string.no);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        MainAdapter adapter = new MainAdapter(this, new ArrayList<>(), mainViewPresentation -> {
            viewModel.changeSelection(mainViewPresentation);
        }, presentation -> {
            long id = presentation.getModel().getContactID();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, DetailsFragment.newInstance(id), "tag")
                    .addToBackStack(String.valueOf(new Random()))
                    .commit();
        }, new ContextMenuListener() {
            @Override
            public void remove(MainViewPresentation presentation) {
                viewModel.deleteContact(presentation);
            }

            @Override
            public void favorite(MainViewPresentation presentation) {
                viewModel.makeFavorite(presentation)
                        .subscribe(new EmptyDisposableCompletableObserver(compositeDisposable));
            }

            @Override
            public void copy(MainViewPresentation presentation) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("number", presentation.getModel().getPhoneNumber());
                clipboard.setPrimaryClip(clip);
            }

        });
        layoutManager = new GridLayoutManager(MainActivity.this, 2);
        listview.setLayoutManager(layoutManager);

        listview.setAdapter(adapter);

        viewModel.contactsLiveData.observe(this, contactModels -> {
            adapter.setItems(contactModels);

            adapter.notifyDataSetChanged();
        });

        itemFavorite = (View) findViewById(R.id.favorite);

//        viewModel.showFavorite.observe(this, value -> {
//            if (value){
//                itemFavorite.setAccessibilityPaneTitle("All contacts");
//            } else {
//                itemFavorite.setAccessibilityPaneTitle("@string/menu_favorite");
//            }
//        });

    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                viewModel.submitQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.submitQuery(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new Intent(this, AddContactActivity.class);
                startActivity(intent);
                break;
            case R.id.remove:

                for (int i = 0; i < viewModel.contactsLiveData.getValue().size(); i++) {
                    if (viewModel.contactsLiveData.getValue().get(i).isSelected()) {
                        viewModel.deleteContact(viewModel.contactsLiveData.getValue().get(i));
                    }
                }
            case R.id.favorite:
//                viewModel.showFavorite();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}