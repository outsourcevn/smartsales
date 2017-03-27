package com.collalab.demoapp.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.collalab.demoapp.DemoApp;
import com.collalab.demoapp.R;
import com.github.pwittchen.networkevents.library.BusWrapper;
import com.github.pwittchen.networkevents.library.ConnectivityStatus;
import com.github.pwittchen.networkevents.library.NetworkEvents;
import com.github.pwittchen.networkevents.library.event.ConnectivityChanged;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuetTabFragment extends Fragment {
    private static final String ARG_PARAM1 = "main_tab";
    private static final String ARG_PARAM2 = "param2";

    private String mMainTab;

    @BindView(R.id.radio_group_method)
    RadioGroup radioGroup;
    @BindView(R.id.radio_btn_internet)
    RadioButton radioBtnInternet;
    @BindView(R.id.radio_btn_subsequence)
    RadioButton radioBtnSubsequence;

    private BusWrapper busWrapper;
    private NetworkEvents networkEvents;

    public QuetTabFragment() {

    }

    public static QuetTabFragment newInstance(String param1) {
        QuetTabFragment fragment = new QuetTabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMainTab = getArguments().getString(ARG_PARAM1);
        }

        final EventBus bus = new EventBus();
        busWrapper = getGreenRobotBusWrapper(bus);
        networkEvents = new NetworkEvents(getContext(), busWrapper).enableInternetCheck();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quet_ma, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.radio_btn_subsequence:
                        if ("ban_hang".equalsIgnoreCase(mMainTab)) {
                            DemoApp.sScanMethodBanHang = 1;
                        } else {
                            DemoApp.sScanMethodNhanHang = 1;
                        }
                        break;
                    case R.id.radio_btn_sms:
                        if ("ban_hang".equalsIgnoreCase(mMainTab)) {
                            DemoApp.sScanMethodBanHang = 2;
                        } else {
                            DemoApp.sScanMethodNhanHang = 2;
                        }
                        break;
                    case R.id.radio_btn_internet:
                        if ("ban_hang".equalsIgnoreCase(mMainTab)) {
                            DemoApp.sScanMethodBanHang = 3;
                        } else {
                            DemoApp.sScanMethodNhanHang = 3;
                        }
                        break;
                }
            }
        });
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(ConnectivityChanged event) {
        if (event.getConnectivityStatus() != ConnectivityStatus.OFFLINE) {
            if (radioBtnInternet != null) {
                radioBtnInternet.setEnabled(true);
            }
        } else {
            if (radioBtnInternet != null) {
                if (radioBtnInternet.isChecked()) {
                    radioBtnSubsequence.setChecked(true);
                    radioBtnInternet.setChecked(false);
                }
                radioBtnInternet.setEnabled(false);
            }
        }
    }

    @NonNull
    private BusWrapper getGreenRobotBusWrapper(final EventBus bus) {
        return new BusWrapper() {
            @Override
            public void register(Object object) {
                bus.register(object);
            }

            @Override
            public void unregister(Object object) {
                bus.unregister(object);
            }

            @Override
            public void post(Object event) {
                bus.post(event);
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        busWrapper.register(this);
        networkEvents.register();
    }

    @Override
    public void onStop() {
        busWrapper.unregister(this);
        networkEvents.unregister();
        super.onStop();
    }
}
