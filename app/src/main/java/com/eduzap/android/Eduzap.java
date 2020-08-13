package com.eduzap.android;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class Eduzap extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
