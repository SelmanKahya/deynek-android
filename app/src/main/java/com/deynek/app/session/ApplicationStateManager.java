package com.deynek.app.session;

import com.deynek.app.util.PreferenceManager;

public class ApplicationStateManager {

    private static final String STATE_KEY = "DeynekAppStatus_KEY";

    private static final PreferenceManager pm = new PreferenceManager(PreferenceManager.SHARED_PREFERENCES.APP_STATE);

    public enum STATES {
        DEFAULT,
        LEAVING,
        LEFT,
        ARRIVED
    }

    public static void saveState(STATES STATES) {
        pm.setPreference(STATE_KEY, STATES.name());
    }

    public static STATES getState(){
        String state = pm.getPreference(STATE_KEY);

        // if state is not saved, set it to default
        if(state == null) {
            saveState(STATES.DEFAULT);
            return STATES.DEFAULT;
        }

        else
            return STATES.valueOf(state);
    }
}
