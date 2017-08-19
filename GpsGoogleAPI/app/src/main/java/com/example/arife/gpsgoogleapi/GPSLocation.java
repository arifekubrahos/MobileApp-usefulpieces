package com.example.arife.gpsgoogleapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.ArrayList;

/**
 * Created by Arife on 17.08.2017.
 */

public class GPSLocation extends Activity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private LocationRequest locationRequest;
    private LocationManager locationManager;
    private LocationSettingsRequest.Builder locationSettingsRequest;
    private GoogleApiClient googleApiClient;
    private Context context;
    private PendingResult<LocationSettingsResult> pendingResult;
    private Location location; //return this

    public static final long UPDATE_INTERVAL = 5000; //sec
    public static final long FASTEST_INTERVAL = 3000;//sec
    public static final int DISTANCE_REPLACEMENT = 10; //METER
    public static final int REQUEST_LOCATION = 100;

    boolean isGpsGranted = false;

    private static final String TAG = GPSLocation.class.getSimpleName();

    private final static int PLAY_SERVICES_REQUEST = 1000;
    public GPSLocation(Context context){
        this.context = context;
    }
    public Location checkLocation(){
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("error:", "permissionda hata");
            return  null;
        }
        else{

            location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if(location == null){
                if(checkPlayServices()){
                    if(buildGoogleAPI()){
                        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    }
                }

            }
            return location;
        }

    }
    public synchronized boolean  buildGoogleAPI(){

        //api hazırlıyoruz gps bağlantısı kuruyoruz
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();

        //request gönderriyoruz
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setSmallestDisplacement(DISTANCE_REPLACEMENT);
        locationSettingsRequest = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        //buda bağlantı sonucunu dönüyor
        pendingResult = LocationServices.SettingsApi.checkLocationSettings(
                googleApiClient, locationSettingsRequest.build());
        pendingResult.setResultCallback(new ResultCallbacks<LocationSettingsResult>() {
            @Override
            public void onSuccess(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()){

                    case LocationSettingsStatusCodes.SUCCESS:
                        isGpsGranted = true;
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(GPSLocation.this ,REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        isGpsGranted = false;
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Status status) {

            }
        });
        return isGpsGranted;
    }

    //callback method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        isGpsGranted = true;
                        Toast.makeText(context, "Gps enabled", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        isGpsGranted =false;
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(context, "Gps Canceled", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    private boolean checkPlayServices() {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this,resultCode,
                        PLAY_SERVICES_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

    /*
    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
        Log.i("PERMISSION PARTIALLY","GRANTED");
    }

    @Override
    public void PermissionDenied(int request_code) {
        Log.i("PERMISSION","DENIED");
    }

    @Override
    public void PermissionGranted(int request_code) {
        Log.i("PERMISSION","GRANTED");
        isPermissionChecked=true;
    }

    @Override
    public void NeverAskAgain(int request_code) {
        Log.i("PERMISSION","NEVER ASK AGAIN");
    }*/
}
