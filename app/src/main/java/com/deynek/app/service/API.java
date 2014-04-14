package com.deynek.app.service;

import android.location.Location;

import com.deynek.app.model.User;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

// will be used to make api calls
public class API {
    public static boolean validateUser(String username, String password) {
        // Check if username, password is filled
        if(username.trim().length() > 0 && password.trim().length() > 0){
            if(username.equals("test") && password.equals("098f6bcd4621d373cade4e832627b4f6")){
                return true;
            }
        }

        return false;
    }

    public static ArrayList findParkingSpots(Location loc) {
        ArrayList<LatLng> locs = new ArrayList<LatLng>();
        locs.add(new LatLng(37.7238256,-122.4767228));
        locs.add(new LatLng(37.7238155,-122.4765565));
        locs.add(new LatLng(37.7238049,-122.4760267));
        locs.add(new LatLng(37.7235609,-122.4766289));
        locs.add(new LatLng(37.7233111,-122.4771351));
        return locs;
    }
}
