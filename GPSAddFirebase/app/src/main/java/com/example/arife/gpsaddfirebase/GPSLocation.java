package com.example.arife.gpsaddfirebase;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;


import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Arife on 14.08.2017.
 */

public class GPSLocation implements LocationListener{
    private LocationManager locationManager;
    private Context context;


    public GPSLocation(Context context){
        this.context = context;
    }

    public Location getLocation() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("error:","permissionda hata");
            return null;
        }
        try{
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,6000,10,this);
                Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Log.d("loc",""+loc);
                return loc;
            }
            else{
                Log.e("error:","locationda");
            }
        }
        catch (Exception e){
            Log.e("error",""+e);
            e.printStackTrace();

        }

        return null;

    }



    @Override
    public void onLocationChanged(Location location) {
        Log.d("location",""+location.getLatitude()+location.getLongitude());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

}