package com.collalab.demoapp.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.collalab.demoapp.R;
import com.collalab.demoapp.fragment.BanHangFragment;
import com.collalab.demoapp.fragment.KhoHangFragment;
import com.collalab.demoapp.fragment.NhanHangFragment;
import com.collalab.demoapp.fragment.SettingFragment;
import com.collalab.demoapp.fragment.TraHangFragment;

public class TabActivity extends AppCompatActivity {

    BanHangFragment banHangFragment;
    KhoHangFragment khoHangFragment;
    NhanHangFragment nhanHangFragment;
    TraHangFragment traHangFragment;
    SettingFragment settingFragment;
    android.support.v4.app.Fragment fragments[] = new Fragment[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        initView();

    }

    private void initView() {
        banHangFragment = BanHangFragment.newInstance("", "");
        khoHangFragment = KhoHangFragment.newInstance("", "");
        nhanHangFragment = NhanHangFragment.newInstance("", "");
        traHangFragment = TraHangFragment.newInstance("", "");
        settingFragment = SettingFragment.newInstance("", "");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
