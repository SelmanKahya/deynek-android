package com.deynek.app.util;
import android.os.CountDownTimer;

public class MyCountDownTimer extends CountDownTimer {

    private boolean isRunning =false;
    private MyCountDownTimerListener listener;

    public interface MyCountDownTimerListener {
        public void onStart();
        public void onFinish();
        public void onTick(long millisUntilFinished);
    }


    public MyCountDownTimer(int secs, long interval, MyCountDownTimerListener listener){
        super(secs * 1000, interval);
        this.listener = listener;
    }
    public void startCountDown(){
        isRunning = true;
        start();
        listener.onStart();
    }

    @Override
    public void onFinish(){
        isRunning = false;
        listener.onFinish();
    }

    @Override
    public void onTick(long millisUntilFinished){
        listener.onTick(millisUntilFinished);
    }

    public boolean isRunning(){
        return isRunning;
    }
}