package com.example.arife.gpsgoogleapi;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent in = new Intent(this,GPSLocation.class);
        startActivity(in);

        GPSLocation gps = new GPSLocation(MainActivity.this);
        Location loc = gps.checkLocation();
        if(loc != null){
            Log.d("lat", ""+loc.getLongitude());
            Log.d("log", ""+loc.getLatitude());
        }
    }
}
