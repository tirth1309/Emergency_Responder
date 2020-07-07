package com.minorproject.android.esrf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class alertResponseActivity extends AppCompatActivity {
    Intent intent;
    final String TAG = "Inside ALertResponse";
    TextView name,locationtv;
    Button clickMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_response);
        String user = getIntent().getExtras().getString("name");
        Double lat = getIntent().getExtras().getDouble("lat");
        Double lon = getIntent().getExtras().getDouble("long");
        String location = getIntent().getExtras().getString("location");
        final String lats = Double.toString(lat);
        final String lons = Double.toString(lon);

        name =(TextView)findViewById(R.id.name);
        locationtv = (TextView)findViewById(R.id.location);
        clickMap = findViewById(R.id.button);

        name.setText(user);
        locationtv.setText(location);

        clickMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+lats+","+lons);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        //intent = getIntent();
        //String username = intent.getStringExtra("user");
        //String lat = intent.getStringExtra("lat");
        //String lon = intent.getStringExtra("long");
        //String location = intent.getStringExtra("location");
        //Log.d(TAG,username);
        //Log.d(TAG,lat);
        //Log.d(TAG,lon);
        //Log.d(TAG,location);
    }
}
