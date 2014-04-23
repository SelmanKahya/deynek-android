package com.deynek.app.activity.find;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import com.deynek.app.R;
import com.deynek.app.model.MyActivity;

public class FindActivity extends MyActivity {

    private TextView minutes_text;
    private SeekBar maxControl;
    private final int MIN_MINS = 1;
    private final int MAX_MINS = 25;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, this);
        setContentView(R.layout.activity_find);

        minutes_text = (TextView) findViewById(R.id.minutes_text);
        minutes_text.setText(MIN_MINS + " Minutes");
        maxControl = (SeekBar) findViewById(R.id.max_seek_bar);
        maxControl.setProgress(0);

        maxControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                minutes_text.setText(calculateMin(progress) + " Minutes");
            }
        });
    }

    public void onMatchButtonClick(View v) {
        Intent i = new Intent(getApplicationContext(), PickLocationActivity.class);
        i.putExtra("USER_MINS", calculateMin(maxControl.getProgress()));
        startActivity(i);
    }

    public int calculateMin(int progress){
        return (int) Math.floor(((float) progress / 100) * (MAX_MINS - MIN_MINS) + MIN_MINS);
    }
}
