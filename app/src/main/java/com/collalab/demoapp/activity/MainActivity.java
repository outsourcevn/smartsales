package com.collalab.demoapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.collalab.demoapp.Common;
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
            edtMainPhone.setEnabled(true);
            isShowSubPhone = false;
            tvShowHide.setText(getResources().getString(R.string.string_icon_arrow_down));
        } else {
            edtSubPhone.setVisibility(View.VISIBLE);
            edtMainPhone.setEnabled(false);
            isShowSubPhone = true;
            tvShowHide.setText(getResources().getString(R.string.string_icon_arrow_up));
        }
    }

    @OnClick(R.id.tv_register)
    public void onRegister() {
        if (!invalidate()) {
            return;
        }
        if (edtMainPhone.isEnabled()) {
            PreferenceUtils.commitString(PrefsKey.KEY_PHONE_MAIN, edtMainPhone.getEditableText().toString());
            PreferenceUtils.commitBoolean(PrefsKey.KEY_IS_MAIN_NUMBER, true);
        } else {
            PreferenceUtils.commitString(PrefsKey.KEY_PHONE_SUB, edtSubPhone.getEditableText().toString());
            PreferenceUtils.commitBoolean(PrefsKey.KEY_IS_MAIN_NUMBER, false);
        }
        Intent intent = new Intent(MainActivity.this, EnterCodeActivity.class);
        Common.hideKeyBoard(this);
        startActivity(intent);
    }

    @OnClick(R.id.tv_contact)
    public void onContactClick() {

    }

    private boolean invalidate() {
        if (TextUtils.isEmpty(edtMainPhone.getEditableText().toString().trim()) && edtMainPhone.isEnabled()) {
            edtMainPhone.setError("Vui lòng nhập số điện thoại chính");
            return false;
        }

        if (TextUtils.isEmpty(edtSubPhone.getEditableText().toString().trim()) && !edtMainPhone.isEnabled()) {
            edtSubPhone.setError("Vui lòng nhập số điện thoại phụ");
            return false;
        }

        return true;
    }
}
