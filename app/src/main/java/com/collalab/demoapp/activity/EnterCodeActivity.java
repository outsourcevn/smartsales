package com.collalab.demoapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.collalab.demoapp.R;
import com.collalab.demoapp.persistence.PreferenceUtils;
import com.collalab.demoapp.persistence.PrefsKey;
import com.goodiebag.pinview.Pinview;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EnterCodeActivity extends AppCompatActivity {

    @BindView(R.id.pinview)
    Pinview mPinView;
    boolean isEnableRegister = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_code);
        ButterKnife.bind(this);
        PreferenceUtils.init(this);
        mPinView.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean b) {
                if (!TextUtils.isEmpty(pinview.getValue()) && pinview.getValue().length() >= 4) {
                    isEnableRegister = true;
                } else {
                    isEnableRegister = false;
                }
            }
        });
    }

    @OnClick(R.id.tv_register)
    public void onRegisterWithCode() {
        if (isEnableRegister) {

            PreferenceUtils.commitBoolean(PrefsKey.KEY_IS_LOGGED_IN,true);
            PreferenceUtils.commitBoolean(PrefsKey.KEY_FIRST_TIME,false);

            Intent intent = new Intent(EnterCodeActivity.this, TabActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Vui lòng nhập mã để đăng ký!", Toast.LENGTH_SHORT).show();
        }
    }
}
