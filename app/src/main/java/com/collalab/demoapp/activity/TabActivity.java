package com.collalab.demoapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.collalab.demoapp.R;

public class TabActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
