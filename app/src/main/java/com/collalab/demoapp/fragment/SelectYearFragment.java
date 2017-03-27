package com.collalab.demoapp.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.collalab.demoapp.R;
import com.collalab.demoapp.event.EventSelectYear;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectYearFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @BindView(R.id.radio_group_method)
    RadioGroup radioGroup;

    public SelectYearFragment() {
        // Required empty public constructor
    }

    public static SelectYearFragment newInstance(String param1, String param2) {
        SelectYearFragment fragment = new SelectYearFragment();
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
        View view = inflater.inflate(R.layout.fragment_select_year, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                String selectedYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                switch (i) {
                    case R.id.radio_2009:
                        selectedYear = "2009";
                        break;
                    case R.id.radio_2011:
                        selectedYear = "2011";
                        break;
                    case R.id.radio_2017:
                        selectedYear = "2017";
                        break;
                }
                EventSelectYear eventSelectYear = new EventSelectYear();
                eventSelectYear.selectedYear = selectedYear;
                EventBus.getDefault().post(eventSelectYear);
            }
        });

    }
}
