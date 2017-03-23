package com.collalab.demoapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.collalab.demoapp.persistence.PreferenceUtils;
import com.collalab.demoapp.persistence.PrefsKey;

public class SonHaDispatcherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceUtils.init(this);

        boolean isLoggedIn = PreferenceUtils.getBoolean(PrefsKey.KEY_IS_LOGGED_IN,false);
        boolean isFirstTime = PreferenceUtils.getBoolean(PrefsKey.KEY_FIRST_TIME,true);
        Intent intent;
        if(isLoggedIn) {
            intent = new Intent(SonHaDispatcherActivity.this,TabActivity.class);
        } else {
            if(isFirstTime) {
                intent = new Intent(SonHaDispatcherActivity.this,MainActivity.class);
            } else {
                intent = new Intent(SonHaDispatcherActivity.this,MainActivity.class);
            }
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
