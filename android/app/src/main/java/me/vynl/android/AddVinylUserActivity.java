package me.vynl.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class AddVinylUserActivity extends AppCompatActivity {
    EditText artist, vinyl;
    private ListAdapter dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vinyl_user);
        dbHelper = new VinylAdapter(this);
        dbHelper.open();
        artist = (EditText) findViewById(R.id.vinyl_artist);
        vinyl = (EditText) findViewById(R.id.vinyl_name);

    }

    @Override
    public void onBackPressed() {
        String artistName = artist.getText().toString().trim();
        String vinylName = vinyl.getText().toString().trim();
        dbHelper.createVinyl(artistName, vinylName);
        Intent intent = new Intent(AddVinylUserActivity.this, VinylListActivity.class);
        startActivity(intent);
        finish();
    }

}
