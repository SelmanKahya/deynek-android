package com.deynek.app.model;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;
import com.parse.ParseObject;

public class MyApplication extends Application {

    private static Context context;

    public void onCreate(){
        super.onCreate();
        MyApplication.context = getApplicationContext();

        Parse.initialize(this, "uBvKK1Jp7kFACydtr6R2irr3DrLTPlNhUeMyZFKE", "3Ps8N8nfDp3jy4dcOdglaCJwQB7hsdT6mQ6BCYFQ");
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}
