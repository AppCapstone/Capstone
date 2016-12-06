package me.vynl.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AllVinylActivity extends Activity {

    private ListAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market);
        dbHelper = new AllListAdapter(this);
        dbHelper.open();
        displayListView();

    }

    private void displayListView() {
        Cursor cursor = dbHelper.fetchVinyl();

        String[] columns = new String[] {
                VinylAdapter.KEY_BAND,
                VinylAdapter.KEY_ALBUM,
        };
        int[] to = new int[] {
                R.id.band,
                R.id.album
        };
        dataAdapter = new AlternateRows(
                this, R.layout.activity_userlist,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                final int vinylID = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                final String vinylName = cursor.getString(cursor.getColumnIndexOrThrow("album"));

                AlertDialog.Builder alert = new AlertDialog.Builder(AllVinylActivity.this);
                alert.setTitle("Would you like to delete this entry?");
                alert.setMessage(vinylName);
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteVinyl(vinylID);
                        displayListView();
                        Toast.makeText(getApplicationContext(),
                                vinylName + " deleted.", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.show();
            }
        });

        EditText myFilter = (EditText) findViewById(R.id.myFilter);
        myFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbHelper.fetchVinylByName(constraint.toString());
            }
        });

    }

    private class AlternateRows extends SimpleCursorAdapter {

        public AlternateRows(Context context, int layout, Cursor c,
                             String[] from, int[] to, int flags) {
            super(context,layout,c,from,to,flags);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            if(position % 2 == 0){
                view.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null));
            }
            else {
                view.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null));
            }
            return view;
        }
    }

    @Override
    public void onBackPressed() {
        dbHelper.close();
        Intent intent = new Intent(AllVinylActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
