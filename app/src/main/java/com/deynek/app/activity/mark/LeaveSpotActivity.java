package com.deynek.app.activity.mark;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.deynek.app.R;
import com.deynek.app.model.MyActivity;
import com.deynek.app.session.ApplicationStateManager;
import com.deynek.app.session.ParkInfoManager;

public class LeaveSpotActivity extends MyActivity {

    private TextView minutes_text;
    private SeekBar minControl;
    private final int MIN_LEAVING_MINS = 3;
    private final int MAX_LEAVING_MINS = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        setContentView(R.layout.activity_leave_spot);

        minutes_text = (TextView) findViewById(R.id.minutes_text);
        minutes_text.setText(MIN_LEAVING_MINS + " Minutes");
        minControl = (SeekBar) findViewById(R.id.min_seek_bar);
        minControl.setProgress(0);

        minControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                minutes_text.setText(calculateMin(progress) + " Minutes");
            }
        });
    }

    public void onStartTimerButtonClick(View v) {
        // save app state
        ApplicationStateManager.saveState(ApplicationStateManager.STATES.LEAVING);

        // save leaving time in pref
        ParkInfoManager park = new ParkInfoManager();
        park.saveParkingTime();
        park.saveLeavingTime(calculateMin(minControl.getProgress()));

        // start new activity
        Intent i = new Intent(getApplicationContext(), LeavingSpotActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    public int calculateMin(int progress){
        return (int) Math.floor(((float) progress / 100) * (MAX_LEAVING_MINS - MIN_LEAVING_MINS) + MIN_LEAVING_MINS);
    }
}
