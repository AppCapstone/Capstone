package me.vynl.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public abstract class ListAdapter {
    protected DatabaseHelper dbHelper;
    protected SQLiteDatabase db;
    private final Context context;

    private static final String TAG = "ListAdapter";
    private static final String DB_NAME = "Vinyl";
    private static final String TABLE_MARKET = "Marketplace";
    private static final String TABLE_USERLIST = "UserList";
    private static final String TABLE_VINYL = "VinylList";
    private static final int DB_VERSION = 1;

    public static final String KEY_ID = "_id";
    public static final String KEY_BAND = "band";
    public static final String KEY_ALBUM = "album";
    public static final String KEY_PRICE = "price";
    public static final String KEY_PHONE = "phone";

    private static final String CREATE_MARKET =
            "CREATE TABLE if not exists " + TABLE_MARKET + " (" +
                    KEY_ID + " integer PRIMARY KEY autoincrement," +
                    KEY_BAND + "," +
                    KEY_ALBUM + "," +
                    KEY_PRICE + "," +
                    KEY_PHONE + "," +
                    " UNIQUE (" + KEY_BAND + "));";

    private static final String CREATE_USERLIST =
            "CREATE TABLE if not exists " + TABLE_USERLIST + " (" +
                    KEY_ID + " integer PRIMARY KEY autoincrement," +
                    KEY_BAND + "," +
                    KEY_ALBUM + "," +
                    " UNIQUE (" + KEY_BAND + "," + KEY_ALBUM + "));";

    private static final String CREATE_ALLVINYL =
            "CREATE TABLE if not exists " + TABLE_VINYL + " (" +
                    KEY_ID + " integer PRIMARY KEY autoincrement," +
                    KEY_BAND + "," +
                    KEY_ALBUM + "," +
                    " UNIQUE (" + KEY_BAND + "," + KEY_ALBUM + "));";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, CREATE_MARKET);
            db.execSQL(CREATE_MARKET);
            Log.w(TAG, CREATE_USERLIST);
            db.execSQL(CREATE_USERLIST);
            Log.w(TAG, CREATE_ALLVINYL);
            db.execSQL(CREATE_ALLVINYL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARKET);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERLIST);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_VINYL);
            onCreate(db);
        }
    }

    public ListAdapter(Context context) {
        this.context = context;
    }

    public ListAdapter open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    public long createVinyl(String band, String album, String price, String phone) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_BAND, band);
        initialValues.put(KEY_ALBUM, album);
        db.insert(TABLE_VINYL, null, initialValues);

        initialValues.put(KEY_PRICE, price);
        initialValues.put(KEY_PHONE, phone);
        return db.insert(TABLE_MARKET, null, initialValues);
    }

    public long createVinyl(String band, String album) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_BAND, band);
        initialValues.put(KEY_ALBUM, album);

        db.insert(TABLE_VINYL, null, initialValues);
        return db.insert(TABLE_USERLIST, null, initialValues);
    }

    public abstract boolean isEmpty();

    public abstract Cursor fetchVinyl();

    public abstract Cursor fetchVinylByName(String inputText);

    public abstract boolean deleteVinyl(int row);
}
