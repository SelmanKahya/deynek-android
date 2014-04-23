package com.deynek.app.activity.find;

import android.content.ActivityNotFoundException;
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
import com.deynek.app.session.ApplicationStateManager;
import com.deynek.app.session.ParkInfoManager;

public class FindingActivity extends MyActivity {

    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        setContentView(R.layout.activity_finding);
        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);

        final Handler h = new Handler();

        Runnable r1 = new Runnable() {

            @Override
            public void run() {
                try {
                    ParkInfoManager park = new ParkInfoManager();
                    park.saveParkingLocation("37.7238256,-122.4767228");

                    ApplicationStateManager.saveState(ApplicationStateManager.STATES.ARRIVED);
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=37.7238256,-122.4767228"));
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
