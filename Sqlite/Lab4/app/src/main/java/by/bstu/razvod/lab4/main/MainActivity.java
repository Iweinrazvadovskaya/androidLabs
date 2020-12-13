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
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

import by.bstu.razvod.lab4.MainViewPresentation;
import by.bstu.razvod.lab4.R;
import by.bstu.razvod.lab4.addContact.AddContactActivity;
import by.bstu.razvod.lab4.details.DetailsFragment;
import by.bstu.razvod.lab4.extendes.ContextMenuListener;
import by.bstu.razvod.lab4.extendes.DataAdapter;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity  {

    private MainViewModel viewModel;
    private RecyclerView listview;
    private View itemFavorite;
    private GridLayoutManager layoutManager;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
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

        DataAdapter adapter = new DataAdapter(this, new ArrayList<>(), mainViewPresentation -> {
            viewModel.changeSelection(mainViewPresentation);
        }, presentation -> {
            long id = presentation.getModel().contactID;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, DetailsFragment.newInstance(id), "tag")
                    .addToBackStack(new String(String.valueOf(new Random())))
                    .commit();
        }, new ContextMenuListener() {
            @Override
            public void remove(MainViewPresentation presentation) {
                viewModel.deleteContact(presentation);
            }

            @Override
            public void favorite(MainViewPresentation presentation) {
                viewModel.makeFavorite(presentation);
            }

            @Override
            public void copy(MainViewPresentation presentation) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("number", presentation.getModel().phoneNumber);
                clipboard.setPrimaryClip(clip);
            }

        });
        layoutManager = new GridLayoutManager(MainActivity.this, 2);
        listview.setLayoutManager(layoutManager);

        listview.setAdapter(adapter);

        viewModel.contactLiveData.observe(this, contactModels -> {
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
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = searchView.getQuery().toString();
                viewModel.findContact(query);
            }
        });
//        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
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
            case R.id.favorite:
//                viewModel.showFavorite();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}