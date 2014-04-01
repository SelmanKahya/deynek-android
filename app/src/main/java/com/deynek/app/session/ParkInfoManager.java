package com.deynek.app.session;
import com.deynek.app.util.PreferenceManager;
import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParkInfoManager {

    public enum INFO {
        PARK_TIME,
        PARK_LOCATION,
        LEAVING_MINS_KEY
    }

    private static final String PARK_TIME = "DeynekParkInfo_PARK_TIME";
    private static final String PARK_LOCATION = "DeynekParkInfo_PARK_LOCATION";
    private static final String LEAVING_MINS_KEY = "DeynekParkInfo_LEAVING_MINS_KEY";
    private static final String FIND_PARK_LOCATION = "DeynekParkInfo_PARK_TIME";

    private static final PreferenceManager pm = new PreferenceManager(PreferenceManager.SHARED_PREFERENCES.PARK_INFO);

    public void saveLeavingTime(int mins){
        pm.setPreference(LEAVING_MINS_KEY, mins + "");
    }

    public int getLeavingTime(){
        return Integer.parseInt(pm.getPreference(LEAVING_MINS_KEY));
    }

    public String getParkingLocation(){
        return pm.getPreference(FIND_PARK_LOCATION);
    }

    public void saveParkingLocation(String loc){
        pm.setPreference(FIND_PARK_LOCATION, loc);
    }

    public void saveParkingTime(){
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
        pm.setPreference(PARK_TIME, format.format(now));
    }

    public Date getParkingTime(){

        String parkingTime = pm.getPreference(PARK_TIME);
        SimpleDateFormat format = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");

        try {
            return format.parse(parkingTime);
        }
        catch(ParseException pe) {
            System.out.println("ERROR: Cannot parse \"" + parkingTime + "\"");
        }

        return null;
    }

    public int getLeftSeconds(){
        int leavingIn = this.getLeavingTime();
        Date now = new Date();
        Date parkedDate = this.getParkingTime();

        if(parkedDate == null)
            return 5;

        long diff = now.getTime() - parkedDate.getTime();
        int left = (int) ((leavingIn * 60) - (diff / 1000));

        if(left > 0)
            return left;

        else
            return 0;
    }
}
