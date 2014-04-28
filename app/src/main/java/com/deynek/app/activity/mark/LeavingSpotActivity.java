package com.deynek.app.activity.mark;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.deynek.app.R;
import com.deynek.app.model.MyActivity;
import com.deynek.app.session.ApplicationStateManager;
import com.deynek.app.session.ParkInfoManager;
import com.deynek.app.util.MyCountDownTimer;


public class LeavingSpotActivity extends MyActivity {

    private TextView minutes_text;
    private MyCountDownTimer countdowntimer;
    private ParkInfoManager park = new ParkInfoManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        setContentView(R.layout.activity_leaving_spot);
        minutes_text = (TextView) findViewById(R.id.minutes_text);
        startTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    public String getTimeString(int time){
        int mins = time/60;
        int secs = time % 60;
        String secsString = String.format("%02d", secs);
        return mins + ":" + secsString;
    }

    public void startTimer(){
        int secs = park.getLeftSeconds();
        countdowntimer = new MyCountDownTimer(secs, 1000, new MyCountDownTimer.MyCountDownTimerListener(){

            @Override
            public void onStart() {
                minutes_text.setText("");
            }

            @Override
            public void onFinish() {
                ApplicationStateManager.saveState(ApplicationStateManager.STATES.LEFT);
                Intent i = new Intent(getApplicationContext(), LeftSpotActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }

            @Override
            public void onTick(long millisUntilFinished) {
                int secs = (int) millisUntilFinished/1000;
                minutes_text.setText(getTimeString(secs));
            }
        });
        countdowntimer.startCountDown();
    }

    public void onLeftButtonClick(View v) {
        // save app state
        ApplicationStateManager.saveState(ApplicationStateManager.STATES.LEFT);

        // start new activity
        Intent i = new Intent(getApplicationContext(), LeftSpotActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}
