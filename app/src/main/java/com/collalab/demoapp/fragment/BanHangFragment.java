package com.collalab.demoapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.collalab.demoapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class BanHangFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public BanHangFragment() {

    }

    public static BanHangFragment newInstance(String param1, String param2) {
        BanHangFragment fragment = new BanHangFragment();
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
        View view = inflater.inflate(R.layout.fragment_ban_hang, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.tv_retail)
    public void onRetailClick() {
        KhachLeFragment khachLeFragment = KhachLeFragment.newInstance("", "");
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_ban_hang_container, khachLeFragment).addToBackStack(null).commit();
    }
}
