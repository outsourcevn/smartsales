package com.collalab.demoapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.collalab.demoapp.R;
import com.collalab.demoapp.adapter.KhoHangAdapter;
import com.collalab.demoapp.entity.ProductEntity;
import com.collalab.demoapp.event.EventFilter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class KhoHangFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private LinearLayoutManager mLayoutManager;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.main_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.btn_filter)
    TextView mTextFilter;

    KhoHangAdapter khoHangAdapter;
    ArrayList<ProductEntity> listProduct = new ArrayList<>();
    boolean isOnFilter = false;

    public KhoHangFragment() {
        // Required empty public constructor
    }

    public static KhoHangFragment newInstance(String param1, String param2) {
        KhoHangFragment fragment = new KhoHangFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_kho_hang, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(mLayoutManager);

        khoHangAdapter = new KhoHangAdapter(getContext(), listProduct);
        recyclerView.setAdapter(khoHangAdapter);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @OnClick(R.id.btn_filter)
    public void onFilterClick() {
        if (isOnFilter) {
            isOnFilter = false;
            mTextFilter.setText("Tìm kiếm");
        } else {
            FilterProductFragment filterProductFragment = FilterProductFragment.newInstance("", "");
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.filter_container, filterProductFragment).addToBackStack(null).commit();
        }
    }

    @OnClick(R.id.btn_notification)
    public void onNotificationClick() {
        NotificationFragment notificationFragment = NotificationFragment.newInstance("", "");
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.filter_container, notificationFragment).addToBackStack(null).commit();
    }

    @Subscribe
    public void onEvent(EventFilter eventFilter) {
        isOnFilter = true;
        mTextFilter.setText("Tất cả");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
