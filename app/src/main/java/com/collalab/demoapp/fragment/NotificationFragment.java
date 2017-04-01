package com.collalab.demoapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.collalab.demoapp.DemoApp;
import com.collalab.demoapp.R;
import com.collalab.demoapp.adapter.KhoHangAdapter;
import com.collalab.demoapp.adapter.NotificationAdapter;
import com.collalab.demoapp.entity.NotificationEntity;
import com.collalab.demoapp.event.EventNotification;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    ArrayList<NotificationEntity> notificationList = new ArrayList<>();
    NotificationAdapter notificationAdapter;
    int totalBadge = 5;

    private LinearLayoutManager mLayoutManager;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.main_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    public NotificationFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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
        initDummyData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification_framgnet, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(mLayoutManager);

        notificationAdapter = new NotificationAdapter(getContext(), notificationList);
        notificationAdapter.setOnItemClick(mOnItemClick);
        recyclerView.setAdapter(notificationAdapter);
    }

    @OnClick(R.id.btn_back)
    public void OnBackClick() {
        getActivity().onBackPressed();
    }

    NotificationAdapter.OnItemClick mOnItemClick = new NotificationAdapter.OnItemClick() {
        @Override
        public void onItemClick(int position) {
            if (notificationList.get(position).getHas_opened()) {

            } else {
                EventNotification eventNotification = new EventNotification();
                if (totalBadge > 0) {
                    totalBadge--;
                    DemoApp.totalBadge = totalBadge;
                    eventNotification.num_badge = totalBadge;
                    EventBus.getDefault().post(eventNotification);
                }
            }
            showDetailContent(notificationList.get(position));
        }
    };

    private void showDetailContent(NotificationEntity notificationEntity) {
        NotificationDetailFragment notificationDetailFragment = NotificationDetailFragment.newInstance(notificationEntity);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.notification_fragment_container, notificationDetailFragment).addToBackStack(null).commit();
    }

    private void initDummyData() {
        notificationList.add(new NotificationEntity(getResources().getString(R.string.string_sample_title_1), getResources().getString(R.string.string_sample_content_1), new Date(), false));
        notificationList.add(new NotificationEntity(getResources().getString(R.string.string_sample_title_2), getResources().getString(R.string.string_sample_content_1), new Date(), true));
        notificationList.add(new NotificationEntity(getResources().getString(R.string.string_sample_title_3), getResources().getString(R.string.string_sample_content_1), new Date(), true));
        notificationList.add(new NotificationEntity(getResources().getString(R.string.string_sample_title_4), getResources().getString(R.string.string_sample_content_1), new Date(), false));
        notificationList.add(new NotificationEntity(getResources().getString(R.string.string_sample_title_5), getResources().getString(R.string.string_sample_content_1), new Date(), false));
        notificationList.add(new NotificationEntity(getResources().getString(R.string.string_sample_title_6), getResources().getString(R.string.string_sample_content_1), new Date(), false));
        notificationList.add(new NotificationEntity(getResources().getString(R.string.string_sample_title_7), getResources().getString(R.string.string_sample_content_1), new Date(), true));
        notificationList.add(new NotificationEntity(getResources().getString(R.string.string_sample_title_8), getResources().getString(R.string.string_sample_content_1), new Date(), false));
        notificationList.add(new NotificationEntity(getResources().getString(R.string.string_sample_title_9), getResources().getString(R.string.string_sample_content_1), new Date(), true));
        notificationList.add(new NotificationEntity(getResources().getString(R.string.string_sample_title_10), getResources().getString(R.string.string_sample_content_1), new Date(), true));
    }

    private Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
