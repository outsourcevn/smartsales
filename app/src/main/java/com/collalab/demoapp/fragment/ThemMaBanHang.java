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
import com.collalab.demoapp.entity.ExportProductEntity;
import com.collalab.demoapp.event.EventBanHang;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ThemMaBanHang extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    Calendar calendar;

    @BindView(R.id.edt_code)
    EditText edtCode;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.edt_number)
    EditText edtNumber;

    ExportProductEntity exportProductEntity;

    public ThemMaBanHang() {
    }

    public static ThemMaBanHang newInstance(String param1, String param2) {
        ThemMaBanHang fragment = new ThemMaBanHang();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static ThemMaBanHang newInstance(ExportProductEntity exportProductEntity) {
        ThemMaBanHang fragment = new ThemMaBanHang();
        Bundle args = new Bundle();
        args.putSerializable("export_product", exportProductEntity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey("export_product")) {
            exportProductEntity = (ExportProductEntity) getArguments().getSerializable("export_product");
        }
        calendar = Calendar.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_them_ma_ban_hang, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (exportProductEntity != null) {
            calendar.setTime(exportProductEntity.created_at);
            edtCode.setText(exportProductEntity.code);
            edtNumber.setText(exportProductEntity.number_product_sold + "");
        }
        tvDate.setText(Common.getDateInString(calendar.getTime()));
    }

    @OnClick(R.id.btn_back)
    public void onBackClick() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.btn_retry)
    public void onRetryClick() {
        edtNumber.setText("");
        edtCode.setText("");
    }

    @OnClick(R.id.tv_date)
    public void onDateClick() {
        new DatePickerDialog(getContext(), endAtListener, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.btn_save)
    public void onSaveClick() {

        if (TextUtils.isEmpty(edtCode.getEditableText().toString().trim())) {
            edtCode.setError("Vui lòng nhập mã công trình!");
            return;
        }

        if (TextUtils.isEmpty(edtNumber.getEditableText().toString().trim())) {
            edtCode.setError("Vui lòng nhập số lượng!");
            return;
        }
        EventBanHang eventBanHang = new EventBanHang();
        if (exportProductEntity == null) {
            exportProductEntity.code = edtCode.getEditableText().toString().trim();
            exportProductEntity.isRetail = false;
            exportProductEntity.number_product_sold = Integer.valueOf(edtNumber.getEditableText().toString().trim());
            eventBanHang.exportProductEntity = exportProductEntity;
            exportProductEntity.created_at = calendar.getTime();
        } else {
            ExportProductEntity exportProductEntity = new ExportProductEntity();
            exportProductEntity.code = edtCode.getEditableText().toString().trim();
            exportProductEntity.isRetail = false;
            exportProductEntity.number_product_sold = Integer.valueOf(edtNumber.getEditableText().toString().trim());
            eventBanHang.exportProductEntity = exportProductEntity;
            exportProductEntity.created_at = calendar.getTime();
        }
        EventBus.getDefault().post(eventBanHang);
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
