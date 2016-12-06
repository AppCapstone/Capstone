package me.vynl.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewVinylActivity extends AppCompatActivity {
    private Button marketAdd, remove;
    private String artist, vinyl;
    private int vinylID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        artist = "";
        vinyl = "";
        vinylID = 0;
        final ListAdapter list = new VinylAdapter(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vinyl);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView vinylText = (TextView) findViewById(R.id.vinyl_title);
        TextView artistText = (TextView) findViewById(R.id.artist_name);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            artist = extras.getString("artist");
            vinyl = extras.getString("vinyl");
            vinylID = Integer.parseInt(extras.getString("_id"));
        }
        vinylText.append(vinyl);
        artistText.append(artist);

        marketAdd = (Button) findViewById(R.id.btn_addmarket);
        marketAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewVinylActivity.this, AddVinylActivity.class);
                intent.putExtra("artist", artist);
                intent.putExtra("vinyl", vinyl);
                startActivity(intent);
            }
        });

        remove = (Button) findViewById(R.id.btn_remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.deleteVinyl(vinylID);
                startActivity(new Intent(ViewVinylActivity.this, VinylListActivity.class));
            }
        });
    }

}
