package com.deynek.app.activity.mark;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.deynek.app.R;
import com.deynek.app.model.MyActivity;
import com.deynek.app.session.ApplicationStateManager;
import com.deynek.app.session.ParkInfoManager;


public class LeavingSpotActivity extends MyActivity {

    private TextView minutes_text;
    private  MyCountDownTimer countdowntimer;
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

    public void startTimer(){
        int secs = park.getLeftSeconds();
        countdowntimer = new MyCountDownTimer(secs, 1000);
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

    public class MyCountDownTimer extends CountDownTimer {
        private int starttime;
        private boolean isrunning=false;

        public MyCountDownTimer(int secs, long interval){
            super(secs * 1000, interval);
            this.starttime = secs;
        }

        public String getTimeString(int time){
            int mins = time/60;
            int secs = time % 60;
            String secsString = String.format("%02d", secs);
            return mins + ":" + secsString;
        }

        public void startCountDown(){
            isrunning = true;
            minutes_text.setText(getTimeString(starttime));
            start();
        }

        @Override
        public void onFinish(){
            isrunning = false;
            ApplicationStateManager.saveState(ApplicationStateManager.STATES.LEFT);
            Intent i = new Intent(getApplicationContext(), LeftSpotActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }

        @Override
        public void onTick(long millisUntilFinished){
            int secs = (int) millisUntilFinished/1000;
            minutes_text.setText(getTimeString(secs));
        }

        public boolean isRunning(){
            return isrunning;
        }
    }
}
