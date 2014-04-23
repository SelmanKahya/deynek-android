package com.deynek.app.activity.find;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.deynek.app.R;
import com.deynek.app.model.MyActivity;
import com.deynek.app.model.MyApplication;
import com.deynek.app.service.API;
import com.deynek.app.session.ApplicationStateManager;
import com.deynek.app.session.ParkInfoManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PickLocationActivity extends MyActivity {

    private GoogleMap map;
    private Circle drawnCircle;
    private LatLng userLoc;
    private int walkingTime = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        setContentView(R.layout.activity_pick_location);

        // get user loc
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            walkingTime = extras.getInt("USER_MINS");
            String value = extras.getString("USER_LOC");

            if(value != null){
                List<String> coords = Arrays.asList(value.split(","));
                if(coords.size() == 2){
                    double latitude = Double.parseDouble(coords.get(0));
                    double longitude = Double.parseDouble(coords.get(1));
                    userLoc = new LatLng(latitude, longitude);
                }
            }
        }

        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        boolean gps_enabled=false;
        boolean network_enabled=false;

        //exceptions will be thrown if provider is not permitted.
        try{gps_enabled=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);}catch(Exception ex){}
        try{network_enabled=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);}catch(Exception ex){}

        Location net_loc=null, gps_loc=null;
        if(gps_enabled)
            gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(network_enabled)
            net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        //if there are both values use the latest one
        if(gps_loc!=null && net_loc!=null){
            if(gps_loc.getTime()>net_loc.getTime())
                userLoc = new LatLng(gps_loc.getLatitude(), gps_loc.getLongitude());
            else
                userLoc = new LatLng(net_loc.getLatitude(), net_loc.getLongitude());
        }

        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        }

        // Google Play Services are available
        else {
            // assign map object
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            map.setMyLocationEnabled(true);
            map.setBuildingsEnabled(true);
            map.setIndoorEnabled(true);

            focusOnLocation();

            // set listeners to see if user started interacting with the map
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng point) {
                    Log.d("Map", "onMapClick clicked: " + point.toString());

                    // meters
                    int radius = (walkingTime * 90) / 2;

                    CircleOptions circle = new CircleOptions()
                            .center(new LatLng(point.latitude, point.longitude))
                            .radius(radius)
                            .strokeColor(R.color.background)
                            .strokeWidth(5)
                            .fillColor(0x40ff0000);  //semi-transparent

                    if(drawnCircle != null)
                        drawnCircle.remove();

                    drawnCircle = map.addCircle(circle);
                }
            });
        }
    }

    // centers camera and zoom in
    private void focusOnLocation() {
        if(userLoc != null){
            CameraUpdate center = CameraUpdateFactory.newLatLng(userLoc);
            map.moveCamera(center);
            map.animateCamera(CameraUpdateFactory.zoomTo(14));
        }
    }

    public void onMatchButtonClick(View v) {
        Intent i = new Intent(getApplicationContext(), FindingActivity.class);
        startActivity(i);
    }
}
