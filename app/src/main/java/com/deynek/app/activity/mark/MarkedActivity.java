package com.deynek.app.activity.mark;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.deynek.app.R;
import com.deynek.app.activity.MainActivity;
import com.deynek.app.model.MyActivity;


public class MarkedActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        setContentView(R.layout.activity_marked);
    }

    public void onGoToButtonClick(View v) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}
