package com.collalab.demoapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.collalab.demoapp.Common;
import com.collalab.demoapp.R;
import com.collalab.demoapp.persistence.PreferenceUtils;
import com.collalab.demoapp.persistence.PrefsKey;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompanyInformationFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    @BindView(R.id.edt_address)
    EditText edtAddress;

    @BindView(R.id.edt_ID_number)
    EditText edtIdNumber;

    @BindView(R.id.spinner_contract_status)
    Spinner spinnerContractStatus;

    public CompanyInformationFragment() {
    }

    public static CompanyInformationFragment newInstance(String param1, String param2) {
        CompanyInformationFragment fragment = new CompanyInformationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_information, container, false);
        ButterKnife.bind(this, view);
        PreferenceUtils.init(getContext());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!TextUtils.isEmpty(PreferenceUtils.getString(PrefsKey.KEY_ADDRESS, ""))) {
            edtAddress.setText(PreferenceUtils.getString(PrefsKey.KEY_ADDRESS, ""));
        }
        if (!TextUtils.isEmpty(PreferenceUtils.getString(PrefsKey.KEY_ID_NUM, ""))) {
            edtIdNumber.setText(PreferenceUtils.getString(PrefsKey.KEY_ID_NUM, ""));
        }
        if (PreferenceUtils.getInt(PrefsKey.KEY_CONTRACT_STATUS, -1) > -1) {
            spinnerContractStatus.setSelection(PreferenceUtils.getInt(PrefsKey.KEY_CONTRACT_STATUS, -1));
        }
    }

    @OnClick(R.id.btn_save)
    public void onSaveClick() {
        PreferenceUtils.commitString(PrefsKey.KEY_ADDRESS, edtAddress.getEditableText().toString().trim());
        PreferenceUtils.commitString(PrefsKey.KEY_ID_NUM, edtIdNumber.getEditableText().toString().trim());
        PreferenceUtils.commitInt(PrefsKey.KEY_CONTRACT_STATUS, spinnerContractStatus.getSelectedItemPosition());
        Toast.makeText(getContext(), "Đã lưu thông tin thành công!", Toast.LENGTH_SHORT).show();

        Common.hideKeyBoard(getActivity());

        getActivity().onBackPressed();
    }

    @OnClick(R.id.btn_cancel)
    public void onCancelClick() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.btn_back)
    public void onBackClick() {
        getActivity().onBackPressed();
    }
}
