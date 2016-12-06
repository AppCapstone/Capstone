package me.vynl.android;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;


public class AllListAdapter extends ListAdapter{

    public static final String KEY_ROWID = "_id";
    public static final String KEY_BAND = "band";
    public static final String KEY_ALBUM = "album";

    private static final String TAG = "AllListAdapter";

    private static final String SQLITE_TABLE = "VinylList";

    public AllListAdapter(Context context) {
        super(context);
    }

    public boolean isEmpty() {
        String query = "Select * from " + SQLITE_TABLE;
        Cursor mCursor = db.rawQuery(query, null);
        if (mCursor.getCount() <= 0) {
            mCursor.close();
            return true;
        }
        mCursor.close();
        return false;
    }


    public boolean deleteVinyl(int row) {
        String whereClause = "_id=?";
        String[] whereArgs = new String[] { String.valueOf(row)};
        int doneDelete = 0;
        doneDelete = db.delete(SQLITE_TABLE, whereClause , whereArgs);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    public Cursor fetchVinylByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = db.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_BAND, KEY_ALBUM},
                    null, null, null, null, null);

        }
        else {
            mCursor = db.query(true, SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_BAND, KEY_ALBUM},
                    KEY_ALBUM + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchVinyl() {

        Cursor mCursor = db.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                        KEY_BAND, KEY_ALBUM},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

}