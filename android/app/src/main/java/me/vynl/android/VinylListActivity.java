package me.vynl.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class VinylListActivity extends Activity {

    private ListAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;
    private FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VinylListActivity.this, AddVinylUserActivity.class));
                finish();
            }
        });
        dbHelper = new VinylAdapter(this);
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

        dataAdapter = new SimpleCursorAdapter(
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
                final String vinylID = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
                final String vinylName = cursor.getString(cursor.getColumnIndexOrThrow("album"));
                final String vinylArtist = cursor.getString(cursor.getColumnIndexOrThrow("band"));

                AlertDialog.Builder alert = new AlertDialog.Builder(VinylListActivity.this);
                alert.setTitle("View this entry?");
                alert.setMessage(vinylName + " by " + vinylArtist);
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(VinylListActivity.this, ViewVinylActivity.class);
                        intent.putExtra("vinyl", vinylName);
                        intent.putExtra("artist", vinylArtist);
                        intent.putExtra("_id", vinylID);
                        startActivity(intent);
                        finish();
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

    @Override
    public void onBackPressed() {
        dbHelper.close();
        Intent intent = new Intent(VinylListActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
