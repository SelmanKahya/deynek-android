package com.deynek.app.activity.find;

import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.deynek.app.R;
import com.deynek.app.model.MyActivity;
import com.deynek.app.model.MyApplication;
import com.deynek.app.service.TrackLocation;
import com.deynek.app.session.ApplicationStateManager;
import com.deynek.app.session.ParkInfoManager;

public class FindingActivity extends MyActivity {

    private ProgressBar spinner;
    private Context ctx;
    private Intent service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        setContentView(R.layout.activity_finding);
        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);

        ctx = getApplicationContext();
        service = new Intent(ctx, TrackLocation.class);

        final Handler h = new Handler();

        Runnable r1 = new Runnable() {

            @Override
            public void run() {
                try {
                    ParkInfoManager park = new ParkInfoManager();
                    park.saveParkingLocation("37.7233111,-122.4771351");

                    startService(service);

                    ApplicationStateManager.saveState(ApplicationStateManager.STATES.ARRIVED);
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=37.7233111,-122.4771351"));
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                } catch (ActivityNotFoundException innerEx) {
                    Toast.makeText(MyApplication.getAppContext(), "Please install a maps application", Toast.LENGTH_LONG).show();
                }
            }
        };

        h.postDelayed(r1, 3500); // 5 second delay
    }
}
