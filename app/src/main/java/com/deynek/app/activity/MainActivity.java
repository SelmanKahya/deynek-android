package com.deynek.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.deynek.app.R;
import com.deynek.app.activity.account.MyProfile;
import com.deynek.app.activity.find.FindActivity;
import com.deynek.app.activity.find.PickLocationActivity;
import com.deynek.app.activity.mark.LeftSpotActivity;
import com.deynek.app.activity.mark.MarkSpotActivity;
import com.deynek.app.model.MyActivity;
import com.deynek.app.session.ApplicationStateManager;
import com.deynek.app.util.MyCountDownTimer;

public class MainActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        setContentView(R.layout.activity_main);
        MyCountDownTimer timer = new MyCountDownTimer(2, 1000, new MyCountDownTimer.MyCountDownTimerListener(){
            @Override
            public void onStart() {}

            @Override
            public void onFinish() {
                Intent i = new Intent(getApplicationContext(), MyProfile.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }

            @Override
            public void onTick(long millisUntilFinished) {}
        });

        timer.startCountDown();
    }
}
