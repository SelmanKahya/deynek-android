package com.deynek.app.activity.mark;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.deynek.app.R;
import com.deynek.app.activity.MainActivity;
import com.deynek.app.activity.account.MyProfile;
import com.deynek.app.model.MyActivity;
import com.deynek.app.session.ApplicationStateManager;


public class LeftSpotActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        setContentView(R.layout.activity_left_spot);
    }

    public void onGoToButtonClick(View v) {
        // save app state
        ApplicationStateManager.saveState(ApplicationStateManager.STATES.DEFAULT);

        // start new activity
        Intent i = new Intent(getApplicationContext(), MyProfile.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}
