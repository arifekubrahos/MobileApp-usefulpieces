package com.example.arife.gpsaddfirebase;

import android.*;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.media.audiofx.BassBoost;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;

import com.google.android.gms.location.LocationRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private double latitude;
    private double langitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        GPSLocation gps = new GPSLocation(getApplicationContext());
        Location location = gps.getLocation();
        Log.d("anasayfa",""+location);


        if(location == null){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("GPS'i aç:");
            alertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent in = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(in);
                }
            });
            alertDialog.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alertDialog.show();
        }else{
            Log.d("latitude",""+location.getLatitude());
            Log.d("longlitude",""+location.getLongitude());
        }






        //GPSModel model = new GPSModel();
        //model.setLangitude(location.getLongitude());
        //model.setLatitude(location.getLatitude());

    }

}
