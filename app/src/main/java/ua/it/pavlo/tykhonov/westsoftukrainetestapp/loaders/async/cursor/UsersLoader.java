package ua.it.pavlo.tykhonov.westsoftukrainetestapp.loaders.async.cursor;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ua.it.pavlo.tykhonov.westsoftukrainetestapp.content.User;
import ua.it.pavlo.tykhonov.westsoftukrainetestapp.database.tables.UsersTable;

public class UsersLoader extends BaseLoader {

    private static final int LIST_SIZE = 1000;
    private int from;
    private int defMaxElements;

    private Context context;

    public UsersLoader(Context context, int from, int defMaxElements) {
        super(context);
        this.context = context;
        this.from = from;
        this.defMaxElements = defMaxElements;
    }

    @Override
    protected void onForceLoad() {

       if (checkTable() == false)
           insertData();

        String sortOrder = UsersTable.Columns._ID + " ASC limit " + defMaxElements + " offset " + from;
        Cursor  cursor = getContext().getContentResolver().query(UsersTable.URI,
                null, null, null, sortOrder);
        deliverResult(cursor);
    }

    private boolean checkTable() {
        String sortOrder = UsersTable.Columns._ID + " ASC limit " + defMaxElements + " offset " + from;
        Cursor  cursor = getContext().getContentResolver().query(UsersTable.URI,
                null, null, null, sortOrder);

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
        UsersTable.save(context, usersList);
    }
}




