package com.collalab.demoapp.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.collalab.demoapp.Common;
import com.collalab.demoapp.R;
import com.collalab.demoapp.entity.DateData;
import com.collalab.demoapp.entity.ExportProductEntity;
import com.collalab.demoapp.event.EventNhapHang;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ThemMaKichHoat extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    Calendar calendar;

    @BindView(R.id.edt_product_code)
    EditText edtCode;
    @BindView(R.id.tv_date_created)
    TextView tvDate;

    String productCode;
    DateData createAt;

    ExportProductEntity exportProductEntity;

    public ThemMaKichHoat() {
    }

    public static ThemMaKichHoat newInstance(String param1, String param2) {
        ThemMaKichHoat fragment = new ThemMaKichHoat();
        return fragment;
    }

    public static ThemMaKichHoat newInstance(String productCode, DateData createdAt) {
        ThemMaKichHoat fragment = new ThemMaKichHoat();
        Bundle args = new Bundle();
        args.putString("product_code", productCode);
        args.putSerializable("created_at", createdAt);
        fragment.setArguments(args);
        return fragment;
    }

    public static ThemMaKichHoat newInstance(ExportProductEntity exportProductEntity) {
        ThemMaKichHoat fragment = new ThemMaKichHoat();
        Bundle args = new Bundle();
        args.putSerializable("export_product", exportProductEntity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        calendar = Calendar.getInstance();

        if (getArguments() != null && getArguments().containsKey("product_code")) {
            if (getArguments() != null) {
                productCode = getArguments().getString("product_code");

                createAt = (DateData) getArguments().getSerializable("created_at");
                calendar.set(Calendar.YEAR, createAt.year);
                calendar.set(Calendar.MONTH, createAt.month);
                calendar.set(Calendar.DAY_OF_MONTH, createAt.day);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_add_ma_kich_hoat, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!TextUtils.isEmpty(productCode)) {
            edtCode.setText(productCode);
        }
        tvDate.setText(Common.getDateInString(calendar.getTime()));
    }

    @OnClick(R.id.btn_back)
    public void onBackClick() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.btn_retry)
    public void onRetryClick() {
        edtCode.setText("");
    }

    @OnClick(R.id.tv_date_created)
    public void onDateClick() {
        new DatePickerDialog(getContext(), endAtListener, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.btn_save)
    public void onSaveClick() {

        if (TextUtils.isEmpty(edtCode.getEditableText().toString().trim())) {
            edtCode.setError("Vui lòng nhập mã công trình!");
            return;
        }

        if (TextUtils.isEmpty(edtCode.getEditableText().toString().trim())) {
            edtCode.setError("Vui lòng nhập mã sản phẩm!");
        }

        EventNhapHang eventNhapHang = new EventNhapHang();
        eventNhapHang.created_at = calendar.getTime();
        eventNhapHang.product_code = edtCode.getEditableText().toString();

        Common.hideKeyBoard(getActivity());

        EventBus.getDefault().post(eventNhapHang);
        getActivity().onBackPressed();
    }

    private DatePickerDialog.OnDateSetListener endAtListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            tvDate.setText(Common.getDateInString(calendar.getTime()));
        }
    };
}
