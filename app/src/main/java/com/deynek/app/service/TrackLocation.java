package com.deynek.app.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class TrackLocation extends Service {

    private String provider;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private Handler mHandler = new Handler();
    private final IBinder mBinder = new LocalBinder();


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       throw new UnsupportedOperationException("Not yet implemented");
    }
/*
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }*/

    @Override
    public void onCreate() {
        //Toast.makeText(this, "The new Service was Created", Toast.LENGTH_LONG).show();
        startGPS();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // For time consuming an long tasks you can launch a new thread here...
        //Toast.makeText(this, " Service Started", Toast.LENGTH_LONG).show();

        /*
        mHandler.post(new Runnable(){
            public void run(){
                startGPS();
            }
        });
        */
    }

    @Override
    public void onDestroy() {
        //Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        stopGPS();
    }

    public void startGPS(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2000, // min time milis
                1, //min distance meters
                locationListener);
    }

    public void stopGPS(){
        locationManager.removeUpdates(locationListener);
    }

    public class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location loc) {

            Log.d("Map", "location changed: " + loc.toString());

            /*Toast.makeText(
                    getBaseContext(),
                    "Location stored: \nLat: " + loc.getLatitude()
                            + " \nLon: " + loc.getLongitude()
                            + " \nAlt: " + (loc.hasAltitude() ? loc.getAltitude()+"m":"?")
                            + " \nAcc: " + (loc.hasAccuracy() ? loc.getAccuracy()+"m":"?"),
                    Toast.LENGTH_SHORT).show();*/

        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

    /**
     * Class for clients to access. Because we know this service always runs in
     * the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        TrackLocation getService() {
            return TrackLocation.this;
        }
    }
}