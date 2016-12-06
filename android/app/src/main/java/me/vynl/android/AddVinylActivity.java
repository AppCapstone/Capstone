package me.vynl.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.widget.EditText;

import java.util.Locale;

public class AddVinylActivity extends AppCompatActivity {
    EditText artist, vinyl, number, price;
    private ListAdapter dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vinyl);
        dbHelper = new MarketDbAdapter(this);
        dbHelper.open();
        artist = (EditText) findViewById(R.id.vinyl_artist);
        vinyl = (EditText) findViewById(R.id.vinyl_name);
        number = (EditText) findViewById(R.id.phone_number);
        price = (EditText) findViewById(R.id.price);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            artist.setText(extras.getString("artist"));
            vinyl.setText(extras.getString("vinyl"));
        }

    }

    @Override
    public void onBackPressed() {
        String artistName = artist.getText().toString().trim();
        String vinylName = vinyl.getText().toString().trim();
        String phoneNumber = PhoneNumberUtils.formatNumber(number.getText().toString().trim(), Locale.getDefault().getCountry());
        String askPrice = price.getText().toString().trim();

        dbHelper.createVinyl(artistName, vinylName, askPrice, phoneNumber);

        Intent intent = new Intent(AddVinylActivity.this, MarketActivity.class);
        startActivity(intent);
        finish();
    }

}
