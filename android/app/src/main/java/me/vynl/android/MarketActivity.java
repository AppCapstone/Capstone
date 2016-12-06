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
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class MarketActivity extends Activity {

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
                    startActivity(new Intent(MarketActivity.this, AddVinylActivity.class));
                    finish();
                }
            });

            dbHelper = new MarketDbAdapter(MarketActivity.this);
            dbHelper.open();
            displayListView();

        }

        private void displayListView() {


            Cursor cursor = dbHelper.fetchVinyl();

            String[] columns = new String[] {
                    MarketDbAdapter.KEY_BAND,
                    MarketDbAdapter.KEY_ALBUM,
                    MarketDbAdapter.KEY_PRICE,
                    MarketDbAdapter.KEY_PHONE
            };

            int[] to = new int[] {
                    R.id.band,
                    R.id.album,
                    R.id.price,
                    R.id.phone,
            };

            dataAdapter = new SimpleCursorAdapter(
                    this, R.layout.activity_market,
                    cursor,
                    columns,
                    to,
                    0);

            ListView listView = (ListView) findViewById(R.id.listView1);
            listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
            listView.setAdapter(dataAdapter);


            listView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> listView, View view,
                                        int position, long id) {
                    Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                    final int vinylID = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                    final String vinylName = cursor.getString(cursor.getColumnIndexOrThrow("album"));

                    AlertDialog.Builder alert = new AlertDialog.Builder(MarketActivity.this);
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

    @Override
    public void onBackPressed() {
        dbHelper.close();
        Intent intent = new Intent(MarketActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
