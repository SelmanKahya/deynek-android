package com.deynek.app.util;

import android.content.SharedPreferences;

import com.deynek.app.model.MyApplication;

public class PreferenceManager {

    private static final String SESSION_PREF_NAME = "DeynekSessionPref";
    private static final String PARK_INFO_PREF_NAME = "DeynekParkInfoPref";
    private static final String APP_STATUS_PREF_NAME = "DeynekAppStatusPref";

    public enum SHARED_PREFERENCES {
        SESSION,
        PARK_INFO,
        APP_STATE
    }

    private String PREF_NAME;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public PreferenceManager(SHARED_PREFERENCES request){

        String activePref;

        switch (request) {
            case SESSION:
                activePref = SESSION_PREF_NAME;
                break;

            case PARK_INFO:
                activePref = PARK_INFO_PREF_NAME;
                break;

            case APP_STATE:
                activePref = APP_STATUS_PREF_NAME;
                break;

            default:
                activePref = APP_STATUS_PREF_NAME;
                break;
        }

        pref = MyApplication.getAppContext().getSharedPreferences(activePref, 0);
        editor = pref.edit();
    }

    public String getPreference (String key){
        return pref.getString(key, null);
    }

    public void setPreference (String key, String value){
        editor.putString(key, value);
        editor.commit();
    }

    public void clearPreference (){
        editor.clear();
        editor.commit();
    }
}
