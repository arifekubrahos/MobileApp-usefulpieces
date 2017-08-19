package com.example.arife.gpsaddfirebase;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Arife on 14.08.2017.
 */

public class GPSLocation implements LocationListener{

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    private Location location;
    private LocationManager locationManager;
    private Context context;


    public GPSLocation(Context context){
        this.context = context;
    }

    public Location getLocation() {
        //check permissions are allowed.
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("error:","permissionda hata");
            return null;
        }

        try{
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            //getting gps status
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            //getting network status
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            //none of provider enabled
            if(!isNetworkEnabled&&!isGPSEnabled){
                Log.e("error:","locationda");
            }
            else{
                // First get location from Network Provider
                if(isNetworkEnabled){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_DISTANCE_CHANGE_FOR_UPDATES,MIN_TIME_BW_UPDATES,this);
                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                    else{
                        Log.e("Error:","location manager is null, network");
                    }
                }
                //second gps enabled
                if(isGPSEnabled){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_DISTANCE_CHANGE_FOR_UPDATES,MIN_TIME_BW_UPDATES,this);
                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                    else{
                        Log.e("Error: ", "Location manager is null, gps");
                    }
                }

            }

        }
        catch (Exception e){
            Log.e("error",""+e);
            e.printStackTrace();

        }

        return location;

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