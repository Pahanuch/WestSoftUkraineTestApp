package ua.it.pavlo.tykhonov.westsoftukrainetestapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import ua.it.pavlo.tykhonov.westsoftukrainetestapp.database.tables.UsersTable;

/**
 * Created by Tikho on 11.11.2016.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String CONTENT_AUTHORITY = "ua.it.pavlo.tykhonov.westsoftukrainetestapp.testapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final String DATABASE_NAME = "ua.it.pavlo.tykhonov.westsoftukrainetestapp.database.db";

    private static final int DATABASE_VERSION = 1;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UsersTable.Requests.CREATION_REQUEST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(UsersTable.Requests.DROP_REQUEST);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
