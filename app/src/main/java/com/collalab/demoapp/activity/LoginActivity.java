package com.collalab.demoapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.collalab.demoapp.Common;
import com.collalab.demoapp.R;
import com.collalab.demoapp.persistence.PreferenceUtils;
import com.collalab.demoapp.persistence.PrefsKey;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.edt_main_phone)
    EditText edtMainPhone;
    @BindView(R.id.edt_sub_phone)
    EditText edtSubPhone;

    boolean isSelectMainPhone = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        PreferenceUtils.init(this);
    }

    @OnClick(R.id.tv_register)
    public void OnSignInClick() {
        if (TextUtils.isEmpty(edtMainPhone.getEditableText().toString()) && TextUtils.isEmpty(edtSubPhone.getEditableText().toString())) {

        }

        if (!TextUtils.isEmpty(edtMainPhone.getEditableText().toString()) && !TextUtils.isEmpty(edtSubPhone.getEditableText().toString())) {

        }

        Common.hideKeyBoard(this);

        PreferenceUtils.commitBoolean(PrefsKey.KEY_IS_LOGGED_IN, true);
        Intent intent = new Intent(this, TabActivity.class);
        startActivity(intent);
    }

}
