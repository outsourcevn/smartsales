package com.collalab.demoapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.collalab.demoapp.R;
import com.collalab.demoapp.persistence.PreferenceUtils;
import com.collalab.demoapp.persistence.PrefsKey;
import com.collalab.demoapp.widget.AwesomeFontTextView;
import com.transitionseverywhere.TransitionManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_icon_show_hide)
    AwesomeFontTextView tvShowHide;

    @BindView(R.id.layout_register)
    ViewGroup layoutRegisterPhone;

    @BindView(R.id.edt_main_phone)
    EditText edtMainPhone;

    @BindView(R.id.edt_sub_phone)
    EditText edtSubPhone;

    @BindView(R.id.edt_password)
    EditText edtPassword;

    private boolean isShowSubPhone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        PreferenceUtils.init(this);
    }

    @OnClick(R.id.tv_icon_show_hide)
    public void onHideShowClick() {
        TransitionManager.beginDelayedTransition(layoutRegisterPhone);
        if (isShowSubPhone) {
            edtSubPhone.setVisibility(View.GONE);
            isShowSubPhone = false;
        } else {
            edtSubPhone.setVisibility(View.VISIBLE);
            isShowSubPhone = true;
        }
    }

    @OnClick(R.id.tv_register)
    public void onRegister() {
        if(!invalidate()) {
            return;
        }
        PreferenceUtils.commitString(PrefsKey.KEY_PHONE_MAIN,edtMainPhone.getEditableText().toString());
        PreferenceUtils.commitString(PrefsKey.KEY_PHONE_SUB,edtSubPhone.getEditableText().toString());
        Intent intent = new Intent(MainActivity.this, EnterCodeActivity.class);
        startActivity(intent);
    }

    private boolean invalidate() {
        if (TextUtils.isEmpty(edtMainPhone.getEditableText().toString().trim())) {
            edtMainPhone.setError("Vui lòng nhập số điện thoại");
            return false;
        }

        if (TextUtils.isEmpty(edtPassword.getEditableText().toString())) {
            edtPassword.setError("Vui lòng nhập mật khẩu");
            return false;
        }
        return true;
    }
}