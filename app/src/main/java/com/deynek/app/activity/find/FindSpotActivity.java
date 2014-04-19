package com.deynek.app.activity.find;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
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
import com.deynek.app.activity.mark.MarkedActivity;
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
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class FindSpotActivity extends MyActivity {

    private GoogleMap map;
    private boolean initialPositionSet = false;
    private Marker pinMarker;
    private ArrayList<Marker> markers = new ArrayList<Marker>();

    private Circle drawnCircle;

    private String provider;
    private LocationListener locationListener;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        setContentView(R.layout.activity_find_spot);

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

            // get LocationManager object from System Service LOCATION_SERVICE
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, true);
            Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
            drawPin(API.findParkingSpots(map.getMyLocation()));

            // zoom to last known position
            if (lastKnownLocation != null)
                focusOnLocation(lastKnownLocation);

            // create location listener
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.d("Map", "location changed, accuracy: " + location.getAccuracy());
                    if(!initialPositionSet && location.getAccuracy() < 35){
                        initialPositionSet = true;
                        focusOnLocation(map.getMyLocation());
                        Log.d("Map", "initial positions set");
                    }
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
            };

            startGPS();

            // set listeners to see if user started interacting with the map
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng point) {
                    Log.d("Map", "onMapClick clicked: " + point.toString());

                    CircleOptions circle = new CircleOptions()
                            .center(new LatLng(point.latitude, point.longitude))
                            .radius(100)
                            .strokeColor(Color.RED)
                            .strokeWidth(5)
                            .fillColor(0x40ff0000);  //semi-transparent

                    if(drawnCircle != null)
                        drawnCircle.remove();

                    drawnCircle = map.addCircle(circle);
                }
            });

            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    String locString = marker.getPosition().latitude + "," + marker.getPosition().longitude;
                    try {

                        ParkInfoManager park = new ParkInfoManager();
                        park.saveParkingLocation(locString);

                        ApplicationStateManager.saveState(ApplicationStateManager.STATES.ARRIVED);
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + locString));
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();

                        /*
                        double latitude = marker.getPosition().latitude;
                        double longitude = marker.getPosition().longitude;
                        String label = "ABC Label";
                        String uriBegin = "geo:" + latitude + "," + longitude;
                        String query = latitude + "," + longitude + "(" + label + ")";
                        String encodedQuery = Uri.encode(query);
                        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                        Uri uri = Uri.parse(uriString);
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                         */
                    }
                    catch(ActivityNotFoundException innerEx) {
                        Toast.makeText(MyApplication.getAppContext(), "Please install a maps application", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }

    private void drawPin(ArrayList<LatLng> locs) {
        map.clear();

        String[] names = {"Maria", "John", "Carol", "Nick", "Lilly"};
        String[] minutes = {"1", "4", "3", "8", "12"};

        for(int i=0; i<locs.size(); i++){
            LatLng loc = locs.get(i);
            // Add marker of user's position
            MarkerOptions userIndicator = new MarkerOptions()
                    .position(loc)
                    .title(names[i] + " leaving in " + minutes[i] + " mins.")
                    .snippet("Would you like to park here? Click me!");
            map.addMarker(userIndicator);
        }
    }

    // centers camera and zoom in
    private void focusOnLocation(Location loc) {
        if(loc != null){
            CameraUpdate center =
                    CameraUpdateFactory.newLatLng(new LatLng(loc.getLatitude(), loc.getLongitude()));
            map.moveCamera(center);
            map.animateCamera(CameraUpdateFactory.zoomTo(19));
        }
    }

    public void startGPS(){
        locationManager.requestLocationUpdates(provider, 20000, 0, locationListener);
    }

    public void stopGPS(){
        locationManager.removeUpdates(locationListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopGPS();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopGPS();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopGPS();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startGPS();
    }
}
