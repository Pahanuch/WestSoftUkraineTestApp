package ua.it.pavlo.tykhonov.westsoftukrainetestapp.loaders.async.cursor;

import android.content.Context;
import android.database.Cursor;

import ua.it.pavlo.tykhonov.westsoftukrainetestapp.database.tables.UsersTable;

public class UsersLoader extends BaseLoader {

    private int from;
    private int defMaxElements;

    public UsersLoader(Context context, int from, int defMaxElements) {
        super(context);
        this.from = from;
        this.defMaxElements = defMaxElements;
    }

    @Override
    protected void onForceLoad() {

        String sortOrder = UsersTable.Columns._ID + " ASC limit " + defMaxElements + " offset " + from;
        Cursor  cursor = getContext().getContentResolver().query(UsersTable.URI,
                null, null, null, sortOrder);
        deliverResult(cursor);
    }
}




