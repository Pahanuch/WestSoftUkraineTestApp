package ua.it.pavlo.tykhonov.westsoftukrainetestapp.database.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ua.it.pavlo.tykhonov.westsoftukrainetestapp.content.User;
import ua.it.pavlo.tykhonov.westsoftukrainetestapp.database.SQLiteHelper;


/**
 * Created by Tikho on 11.11.2016.
 */

public class UsersTable {

    public static final Uri URI = SQLiteHelper.BASE_CONTENT_URI.buildUpon().appendPath(Requests.TABLE_NAME).build();

    public static void save(Context context, @NonNull User user) {
        context.getContentResolver().insert(URI, toContentValues(user));
    }

    public static void save(Context context, @NonNull List<User> user) {
        ContentValues[] values = new ContentValues[user.size()];
        for (int i = 0; i < user.size(); i++) {
            values[i] = toContentValues(user.get(i));
        }
        context.getContentResolver().bulkInsert(URI, values);
    }

    @NonNull
    public static ContentValues toContentValues(@NonNull User user) {
        ContentValues values = new ContentValues();
        values.put(Columns.FIRSTNAME, user.getFirstName());
        values.put(Columns.LASTNAME, user.getLastName());
        return values;
    }

    @NonNull
    public static User fromCursor(@NonNull Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(Columns._ID));
        String firstname = cursor.getString(cursor.getColumnIndex(Columns.FIRSTNAME));
        String lastName = cursor.getString(cursor.getColumnIndex(Columns.LASTNAME));
        return new User(id, firstname, lastName);
    }

    @NonNull
    public static List<User> listFromCursor(@NonNull Cursor cursor) {
        List<User> user = new ArrayList<>();

        if (cursor.isClosed()) {
            return user;
        }

        if (!cursor.moveToFirst()) {
            cursor.close();
            return user;
        }

        try {
            do {
                user.add(fromCursor(cursor));
            } while (cursor.moveToNext());
            return user;
        } finally {
            cursor.close();
        }
    }

    public static void clear(Context context) {
        context.getContentResolver().delete(URI, null, null);
    }

    public interface Columns {
        String _ID = "id";
        String FIRSTNAME = "firstname";
        String LASTNAME = "lastname";
    }

    public interface Requests {

        String TABLE_NAME = UsersTable.class.getSimpleName();

        String CREATION_REQUEST = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Columns.FIRSTNAME + " VARCHAR(200), " +
                Columns.LASTNAME + " VARCHAR(200)" + ");";

        String DROP_REQUEST = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}
