package com.collalab.demoapp.activity;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.collalab.demoapp.R;
import com.collalab.demoapp.fragment.BanHangFragment;
import com.collalab.demoapp.fragment.KhoHangFragment;
import com.collalab.demoapp.fragment.NhanHangFragment;
import com.collalab.demoapp.fragment.SettingFragment;
import com.collalab.demoapp.fragment.TraHangFragment;
import com.collalab.demoapp.fragment.YearQuarterMonthFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabActivity extends AppCompatActivity implements OnTabSelectListener, OnTabReselectListener {

    BanHangFragment banHangFragment;
    KhoHangFragment khoHangFragment;
    NhanHangFragment nhanHangFragment;
    TraHangFragment traHangFragment;
    SettingFragment settingFragment;
    YearQuarterMonthFragment yearQuarterMonthFragment;
    android.support.v4.app.Fragment fragments[] = new Fragment[5];
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    @BindView(R.id.bottomBar)
    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        ButterKnife.bind(this);
        initView();
        bottomBar.setOnTabSelectListener(this);
        bottomBar.setOnTabReselectListener(this);
    }

    private void initView() {
        banHangFragment = BanHangFragment.newInstance("", "");
        khoHangFragment = KhoHangFragment.newInstance("", "");
        nhanHangFragment = NhanHangFragment.newInstance("", "");
        yearQuarterMonthFragment = YearQuarterMonthFragment.newInstance("","");
        traHangFragment = TraHangFragment.newInstance("", "");
        settingFragment = SettingFragment.newInstance("", "");

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        addTabFragment(0);
    }

    private void addTabFragment(int id) {

        if (fragments[id] == null) {
            switch (id) {
                case 0:
                    fragments[0] = khoHangFragment;
                    break;
                case 1:
//                    fragments[1] = nhanHangFragment;
                    fragments[1] = yearQuarterMonthFragment;
                    break;
                case 2:
                    fragments[2] = banHangFragment;
                    break;
                case 3:
                    fragments[3] = traHangFragment;
                    break;
                case 4:
                    fragments[4] = settingFragment;
                    break;
                default:
                    break;
            }
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.contentContainer, fragments[id]);
            fragmentTransaction.commit();
            fragments[id].setUserVisibleHint(true);
        } else {
            fragmentTransaction = fragmentManager.beginTransaction();
            for (int i = 0; i < 5; i++) {
                if (i == id) {
                    fragmentTransaction.show(fragments[i]);
                    fragments[i].setUserVisibleHint(true);
                } else {
                    if (fragments[i] != null) {
                        fragments[i].setUserVisibleHint(false);
                        fragmentTransaction.hide(fragments[i]);
                    }
                }
            }
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        bottomBar.onSaveInstanceState();
    }

    @Override
    public void onTabReSelected(@IdRes int tabId) {
        addTabFragment(getPositionById(tabId));
    }

    @Override
    public void onTabSelected(@IdRes int tabId) {
        addTabFragment(getPositionById(tabId));
    }

    private int getPositionById(int tabId) {
        switch (tabId) {
            case R.id.action_kho_hang:
                return 0;
            case R.id.action_nhan_hang:
                return 1;
            case R.id.action_ban_hang:
                return 2;
            case R.id.action_nhan_hang_tra:
                return 3;
            case R.id.action_cai_dat:
                return 4;
            default:
                return 0;
        }
    }
}
