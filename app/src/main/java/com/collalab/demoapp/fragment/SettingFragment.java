package com.collalab.demoapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.collalab.demoapp.DemoApp;
import com.collalab.demoapp.R;
import com.collalab.demoapp.activity.MainActivity;
import com.collalab.demoapp.event.EventNotification;
import com.collalab.demoapp.persistence.PreferenceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.nekocode.badge.BadgeDrawable;

public class SettingFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    @BindView(R.id.tv_badge)
    ImageView tvBadge;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        PreferenceUtils.init(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (DemoApp.totalBadge > 0) {
            final BadgeDrawable drawable =
                    new BadgeDrawable.Builder()
                            .type(BadgeDrawable.TYPE_NUMBER)
                            .number(DemoApp.totalBadge)
                            .build();
            tvBadge.setImageDrawable(drawable);
        } else {
            tvBadge.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void onEvent(EventNotification eventNotification) {
        if (eventNotification.num_badge > 0) {
            final BadgeDrawable drawable =
                    new BadgeDrawable.Builder()
                            .type(BadgeDrawable.TYPE_NUMBER)
                            .number(eventNotification.num_badge)
                            .build();
            tvBadge.setImageDrawable(drawable);
        } else {
            tvBadge.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.layout_phone)
    public void onLayoutPhoneClick() {
        ListPhoneFragment listPhoneFragment = ListPhoneFragment.newInstance("", "");
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.setting_fragment_container, listPhoneFragment).addToBackStack(null).commit();
    }

    @OnClick(R.id.layout_images)
    public void onLayoutImageClick() {
        StoreImageFragment storeImageFragment = StoreImageFragment.newInstance("", "");
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.setting_fragment_container, storeImageFragment).addToBackStack(null).commit();
    }

    @OnClick(R.id.layout_notification)
    public void onLayoutNotificationClick() {
        NotificationFragment notificationFragment = NotificationFragment.newInstance("", "");
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.setting_fragment_container, notificationFragment).addToBackStack(null).commit();
    }

    @OnClick(R.id.layout_infomation)
    public void onLayoutInfoClick() {
        CompanyInformationFragment informationFragment = CompanyInformationFragment.newInstance("", "");
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.setting_fragment_container, informationFragment).addToBackStack(null).commit();
    }

    @OnClick(R.id.layout_sign_out)
    public void onLayoutSignOutClick() {
        PreferenceUtils.logOut();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
