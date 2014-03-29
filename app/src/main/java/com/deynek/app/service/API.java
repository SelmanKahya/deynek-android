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
        locs.add(new LatLng(37.784820, -122.413470));
        locs.add(new LatLng(37.784879, -122.412665));
        locs.add(new LatLng(37.784464, -122.412740));
        locs.add(new LatLng(37.783082, -122.412064));
        return locs;
    }
}
