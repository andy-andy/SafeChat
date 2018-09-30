package com.at.safechat;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Data store
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("3e0a988f6a4ef79e3223f7ad40e6f38f0758138a")
                .clientKey("d91aa3aca2fd9177d0d2e39d225cb3207253a511")
                .server("http://18.216.18.238:80/parse/")
                .build()
        );

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
