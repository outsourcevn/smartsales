package com.collalab.demoapp.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.cengalabs.flatui.views.FlatToggleButton;
import com.collalab.demoapp.Common;
import com.collalab.demoapp.R;
import com.collalab.demoapp.event.EventFilter;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterProductFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    String productName;
    String productCode;
    Date startAt;
    Date endAt;
    boolean hasProduct;

    EventFilter eventFilter = new EventFilter();

    @BindView(R.id.edt_product_name)
    EditText edtProductName;

    @BindView(R.id.edt_product_code)
    EditText edtProductCode;

    @BindView(R.id.tv_start_day)
    TextView tvStartDay;

    @BindView(R.id.tv_end_day)
    TextView tvEndDay;

    @BindView(R.id.toggle_checked_enabled)
    FlatToggleButton toggleHasProduct;

    Calendar startCalendar, endCalendar;

    public FilterProductFragment() {
        // Required empty public constructor
    }

    public static FilterProductFragment newInstance(String param1, String param2) {
        FilterProductFragment fragment = new FilterProductFragment();
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
        View view = inflater.inflate(R.layout.fragment_filter_product, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();

        tvStartDay.setText(Common.getDateInString(startCalendar.getTime()));
        tvEndDay.setText(Common.getDateInString(endCalendar.getTime()));
    }

    @OnClick(R.id.tv_start_day)
    public void onStartDayClick() {
        new DatePickerDialog(getContext(), startAtListener, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.tv_end_day)
    public void onEndDayClick() {
        new DatePickerDialog(getContext(), endAtListener, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show();
    }

    @OnClick(R.id.btn_back)
    public void onBackClick() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.btn_filter)
    public void onFilterClick() {
        eventFilter.product_name = edtProductName.getEditableText().toString().trim();
        eventFilter.product_code = edtProductCode.getEditableText().toString().trim();
        eventFilter.startAt = startAt;
        eventFilter.endAt = endAt;
        eventFilter.hasProduct = toggleHasProduct.isChecked();
        EventBus.getDefault().post(eventFilter);
        getActivity().onBackPressed();
    }

    private DatePickerDialog.OnDateSetListener startAtListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            startCalendar.set(Calendar.YEAR, year);
            startCalendar.set(Calendar.MONTH, month);
            startCalendar.set(Calendar.DAY_OF_MONTH, day);
            tvStartDay.setText(Common.getDateInString(startCalendar.getTime()));
        }
    };

    private DatePickerDialog.OnDateSetListener endAtListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            endCalendar.set(Calendar.YEAR, year);
            endCalendar.set(Calendar.MONTH, month);
            endCalendar.set(Calendar.DAY_OF_MONTH, day);
            tvEndDay.setText(Common.getDateInString(endCalendar.getTime()));
        }
    };
}