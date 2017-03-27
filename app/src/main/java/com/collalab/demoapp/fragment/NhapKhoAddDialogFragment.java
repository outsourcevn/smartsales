package com.collalab.demoapp.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.collalab.demoapp.Common;
import com.collalab.demoapp.R;
import com.collalab.demoapp.entity.DateData;
import com.collalab.demoapp.event.EventNhapHang;
import com.collalab.demoapp.persistence.PreferenceUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by VietMac on 2017-03-10.
 */

public class NhapKhoAddDialogFragment extends DialogFragment {

    Calendar nhapHangCalendar;

    @BindView(R.id.edt_product_code)
    EditText edtProductCode;
    @BindView(R.id.tv_date_created)
    TextView tvDateCreated;
    @BindView(R.id.btn_close_dialog)
    View btnCloseDialog;

    String productCode;
    DateData createAt;

    public NhapKhoAddDialogFragment() {

    }

    public static NhapKhoAddDialogFragment newInstance(String productCode, DateData createdAt) {
        NhapKhoAddDialogFragment fragment = new NhapKhoAddDialogFragment();
        Bundle args = new Bundle();
        args.putString("product_code", productCode);
        args.putSerializable("created_at", createdAt);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_add_nhap_hang, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nhapHangCalendar = Calendar.getInstance();
        if (getArguments() != null) {
            productCode = getArguments().getString("product_code");

            createAt = (DateData) getArguments().getSerializable("created_at");
            nhapHangCalendar.set(Calendar.YEAR, createAt.year);
            nhapHangCalendar.set(Calendar.MONTH, createAt.month);
            nhapHangCalendar.set(Calendar.DAY_OF_MONTH, createAt.day);
        }
    }

    private void bindData2View() {
        if (!TextUtils.isEmpty(productCode)) {
            edtProductCode.setText(productCode);
        }
        tvDateCreated.setText(Common.getDateInString(nhapHangCalendar.getTime()));
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getDialog().setCanceledOnTouchOutside(false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        bindData2View();
    }

    @OnClick(R.id.tv_date_created)
    public void onDateClick() {
        new DatePickerDialog(getContext(), onDateSelectListener, nhapHangCalendar.get(Calendar.YEAR), nhapHangCalendar.get(Calendar.MONTH), nhapHangCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.btn_retry)
    public void onRetryClick() {
        edtProductCode.setText("");
        nhapHangCalendar.setTime(Calendar.getInstance().getTime());
        tvDateCreated.setText(Common.getDateInString(nhapHangCalendar.getTime()));
    }

    @OnClick(R.id.btn_save)
    public void onSaveClick() {

        if (TextUtils.isEmpty(edtProductCode.getEditableText().toString().trim())) {
            edtProductCode.setError("Vui lòng nhập mã sản phẩm!");
        }

        EventNhapHang eventNhapHang = new EventNhapHang();
        eventNhapHang.created_at = nhapHangCalendar.getTime();
        eventNhapHang.product_code = edtProductCode.getEditableText().toString();
        EventBus.getDefault().post(eventNhapHang);
        dismiss();
    }

    @OnClick(R.id.btn_close_dialog)
    public void onClickDialog() {
        getDialog().dismiss();
    }

    private DatePickerDialog.OnDateSetListener onDateSelectListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            nhapHangCalendar.set(Calendar.YEAR, year);
            nhapHangCalendar.set(Calendar.MONTH, month);
            nhapHangCalendar.set(Calendar.DAY_OF_MONTH, day);
            tvDateCreated.setText(Common.getDateInString(nhapHangCalendar.getTime()));
        }
    };

}