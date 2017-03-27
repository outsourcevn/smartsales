package com.collalab.demoapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.collalab.demoapp.R;
import com.collalab.demoapp.adapter.PhoneAdapter;
import com.collalab.demoapp.adapter.TraHangAdapter;
import com.collalab.demoapp.entity.ReturnProductEntity;
import com.collalab.demoapp.persistence.PreferenceUtils;
import com.collalab.demoapp.persistence.PrefsKey;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TraHangFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    ArrayList<ReturnProductEntity> returnProductEntities = new ArrayList<>();
    TraHangAdapter traHangAdapter;

    public TraHangFragment() {
        // Required empty public constructor
    }

    public static TraHangFragment newInstance(String param1, String param2) {
        TraHangFragment fragment = new TraHangFragment();
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

        initDummy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tra_hang, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        traHangAdapter = new TraHangAdapter(getContext(), returnProductEntities);
        recyclerView.setAdapter(traHangAdapter);
        traHangAdapter.hideFooter();
    }

    @OnClick(R.id.btn_notification)
    public void onNotificationClick() {
        NotificationFragment notificationFragment = NotificationFragment.newInstance("", "");
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.tra_hang_container, notificationFragment).addToBackStack(null).commit();
    }

    private void initDummy() {
        returnProductEntities.add(new ReturnProductEntity("780871203487", new Date()));
        returnProductEntities.add(new ReturnProductEntity("929348092830", new Date()));
        returnProductEntities.add(new ReturnProductEntity("423456464565", new Date()));
        returnProductEntities.add(new ReturnProductEntity("234234234342", new Date()));
        returnProductEntities.add(new ReturnProductEntity("656546456456", new Date()));
    }

}
