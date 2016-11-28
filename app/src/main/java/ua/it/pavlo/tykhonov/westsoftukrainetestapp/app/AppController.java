package ua.it.pavlo.tykhonov.westsoftukrainetestapp.app;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ua.it.pavlo.tykhonov.westsoftukrainetestapp.content.User;
import ua.it.pavlo.tykhonov.westsoftukrainetestapp.database.tables.UsersTable;

public class AppController extends android.app.Application {

    private static final int LIST_SIZE = 1000;

    @Override
    public void onCreate() {
        super.onCreate();
        if (checkTable() == false)
            insertData();
    }

    private boolean checkTable() {

        Cursor cursor = getApplicationContext().getContentResolver().query(UsersTable.URI,
                null, null, null, null);

        if (cursor.getCount() != 0) {
            if (!cursor.isClosed())
                cursor.close();
            return true;
        } else {
            if (!cursor.isClosed())
                cursor.close();
            return false;
        }
    }

    private void insertData() {
        List<User> usersList = new ArrayList<>();
        for (int i=0; i < LIST_SIZE; i++) {
            User user = new User();
            user.setFirstName("FirstName " + (i+1));
            user.setLastName("SecondName " + (i+1));
            usersList.add(user);
        }
        UsersTable.save(getApplicationContext(), usersList);
    }

}
