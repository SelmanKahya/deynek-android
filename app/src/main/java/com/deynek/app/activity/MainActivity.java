package com.deynek.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.deynek.app.R;
import com.deynek.app.activity.find.FindSpotActivity;
import com.deynek.app.activity.mark.MarkSpotActivity;
import com.deynek.app.model.MyActivity;

public class MainActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        setContentView(R.layout.activity_main);
    }

    public void onMarkButtonClick(View v) {
        Intent i = new Intent(getApplicationContext(), MarkSpotActivity.class);
        startActivity(i);
    }

    public void onLogoutClick(View v) {
        getSession().logoutUser();
    }

    public void onFindButtonClick(View v) {
        Intent i = new Intent(getApplicationContext(), FindSpotActivity.class);
        startActivity(i);
    }
}
