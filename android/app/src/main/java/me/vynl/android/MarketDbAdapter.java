package me.vynl.android;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

public class MarketDbAdapter extends ListAdapter {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_BAND = "band";
    public static final String KEY_ALBUM = "album";
    public static final String KEY_PRICE = "price";
    public static final String KEY_PHONE = "phone";

    private static final String TAG = "MarketDbAdapter";

    private static final String SQLITE_TABLE = "Marketplace";

    public MarketDbAdapter(Context ctx) {
        super(ctx);
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
                            KEY_BAND, KEY_ALBUM, KEY_PRICE, KEY_PHONE},
                    null, null, null, null, null);

        }
        else {
            mCursor = db.query(true, SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_BAND, KEY_ALBUM, KEY_PRICE, KEY_PHONE},
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
                        KEY_BAND, KEY_ALBUM, KEY_PRICE, KEY_PHONE},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void insertSomeVinyl() {

        createVinyl("Earl Sweatshirt","I Don't Like Shit, I Don't Go Outside","$75.00","828-037-8834");
        createVinyl("Vince Staples","Prima Donna","$35.00","828-399-1922");
        createVinyl("MF Doom","Madvillainy","$20.00","828-139-4723");
        createVinyl("Danny Brown","Atrocity Exhibition","$35.00","704-149-0223");
        createVinyl("Mac Miller","The Divine Feminine","$110.00","704-437-8212");
        createVinyl("Flatbush Zombies","3001: A laced Odyssey","$40.00","828-922-8377");
        createVinyl("Joey Bada$$","B4.DA.$$","$70.00","704-123-9823");

    }

}