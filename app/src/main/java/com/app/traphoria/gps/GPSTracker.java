package com.app.traphoria.gps;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import com.app.traphoria.preference.TraphoriaPreference;
import com.app.traphoria.utility.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class GPSTracker implements ConnectionCallbacks,
        OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    //private OnLocationChangedListener mLocationChangedListener;

    // The minimum distance to change Updates in meters
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 2 * 1; // 2 second

    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            MIN_TIME_BW_UPDATES / 2;
    private static final String TAG = "GPSTracker";
    private final Context mActivity;
    // Declaring a Location Manager
    protected LocationRequest mLocationRequest;
    // flag for GPS status
    private boolean isGPSEnabled = false;
    // flag for network status
    private boolean isNetworkEnabled = false;
    // flag for GPS status
    private boolean canGetLocation = false;
    // Current best location estimate
    private Location mBestReading;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation; // location


    public GPSTracker(Context context) {
        this.mActivity = context;
        LocationManager manager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildGoogleApiClient();
            isGPSEnabled = true;
        } else {
            showSettingsAlert();
        }
    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
        mGoogleApiClient.connect();
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(MIN_TIME_BW_UPDATES);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setSmallestDisplacement(MIN_DISTANCE_CHANGE_FOR_UPDATES);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    /**
     * Function to get latitude
     */
    public double getLatitude() {
        double latitude = 0.0;
        if (mCurrentLocation != null) {

            latitude = mCurrentLocation.getLatitude();
            TraphoriaPreference.setLatitude(mActivity, latitude);
        }
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        double longitude = 0.0;
        if (mCurrentLocation != null) {
            longitude = mCurrentLocation.getLongitude();
            TraphoriaPreference.setLongitude(mActivity, longitude);
        }
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * lauch Settings Options
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mActivity.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onLocationChanged(Location mCurrentLocation) {
        Utils.ShowLog(TAG, "onLocationChanged :" + mCurrentLocation.getLatitude() + ", " + mCurrentLocation.getLongitude());
        canGetLocation = true;
        TraphoriaPreference.setLatitude(mActivity, mCurrentLocation.getLatitude());
        TraphoriaPreference.setLongitude(mActivity, mCurrentLocation.getLongitude());
        this.mCurrentLocation = mCurrentLocation;
        //mLocationChangedListener.onReceiveLocation(mCurrentLocation, 1);
        stopLocationUpdates();
    }


    @Override
    public void onConnected(Bundle bundle) {
        try {
            if (mCurrentLocation == null) {
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mCurrentLocation != null) {
                    canGetLocation = true;
                    TraphoriaPreference.setLatitude(mActivity, mCurrentLocation.getLatitude());
                    TraphoriaPreference.setLongitude(mActivity, mCurrentLocation.getLongitude());
                    // mLocationChangedListener.onReceiveLocation(mCurrentLocation, 1);
                }else{
                    stopLocationUpdates();
                }
            }else{
                startLocationUpdates();
            }
            if (mCurrentLocation == null && mGoogleApiClient.isConnected()) {
                startLocationUpdates();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


}