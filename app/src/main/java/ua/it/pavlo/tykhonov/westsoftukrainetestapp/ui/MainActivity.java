package ua.it.pavlo.tykhonov.westsoftukrainetestapp.ui;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.it.pavlo.tykhonov.westsoftukrainetestapp.R;
import ua.it.pavlo.tykhonov.westsoftukrainetestapp.ui.adapter.UsersAdapter;
import ua.it.pavlo.tykhonov.westsoftukrainetestapp.content.User;
import ua.it.pavlo.tykhonov.westsoftukrainetestapp.database.tables.UsersTable;
import ua.it.pavlo.tykhonov.westsoftukrainetestapp.loaders.async.cursor.UsersLoader;
import ua.it.pavlo.tykhonov.westsoftukrainetestapp.utils.EndlessRecyclerViewScrollListener;

/**
 * Created by Tikho on 11.11.2016.
 */

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static int DEFAULT_MAX_ELEMENTS_IN_MEMORY = 50;
    private int from = 0;

    private boolean doubleBackToExitPressedOnce = false;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private UsersAdapter usersAdapter;
    private List<User> usersList;

    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;

    private LinearLayoutManager mLayoutManager;

    private Resources res;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_max_elements) {
            showChangeMaxElementsDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        res = getResources();

        usersList = new ArrayList<>();
        usersAdapter = new UsersAdapter(this, usersList);

        mLayoutManager = new LinearLayoutManager(this);

        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(mLayoutManager);
        } else{
            recyclerView.setLayoutManager(mLayoutManager);
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(usersAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                loadNextDataFromDb(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        recyclerView.addOnScrollListener(scrollListener);

        getSupportLoaderManager().initLoader(R.id.users_loader, Bundle.EMPTY, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case R.id.users_loader:
                return new UsersLoader(this, from, DEFAULT_MAX_ELEMENTS_IN_MEMORY);

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        int id = loader.getId();

        usersList.addAll((List) UsersTable.listFromCursor(data));
        usersAdapter.notifyDataSetChanged();

        getLoaderManager().destroyLoader(id);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void loadNextDataFromDb(int offset) {
        this.from = offset * DEFAULT_MAX_ELEMENTS_IN_MEMORY;
        getSupportLoaderManager().restartLoader(R.id.users_loader, Bundle.EMPTY, this);
    }

    private void showChangeMaxElementsDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.change_max_elements_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText etMaxElements = (EditText) dialogView.findViewById(R.id.et_max_elements);
        etMaxElements.setHint(res.getString(R.string.set_max_elements));
        etMaxElements.setText(String.valueOf(DEFAULT_MAX_ELEMENTS_IN_MEMORY));
        etMaxElements.setSelection(etMaxElements.length());

        dialogBuilder.setTitle(res.getString(R.string.change_max_elements));
        dialogBuilder.setMessage(res.getString(R.string.enter_max_elements));
        dialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if (Integer.parseInt(etMaxElements.getText().toString()) < DEFAULT_MAX_ELEMENTS_IN_MEMORY) {
                    DEFAULT_MAX_ELEMENTS_IN_MEMORY = Integer.parseInt(etMaxElements.getText().toString());
                    from = 0;
                    usersList.clear();
                    usersAdapter.notifyDataSetChanged();
                    scrollListener.resetState();
                    loadNextDataFromDb(from);
                }
            }

        });
        dialogBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    @Override
    public void onBackPressed() {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, res.getString(R.string.exit), Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
    }

}
