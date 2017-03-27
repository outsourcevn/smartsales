package com.collalab.demoapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.collalab.demoapp.R;
import com.collalab.demoapp.entity.ImportProductEntity;
import com.collalab.demoapp.event.EventNhapHang;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class YearQuarterMonthFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    ArrayList<ImportProductEntity> listProduct = new ArrayList<>();
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    public YearQuarterMonthFragment() {
        // Required empty public constructor
    }

    public static YearQuarterMonthFragment newInstance(String param1, String param2) {
        YearQuarterMonthFragment fragment = new YearQuarterMonthFragment();
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
        View view = inflater.inflate(R.layout.fragment_year_quarter_month, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @OnClick(R.id.btn_notification)
    public void onNotificationClick() {
        NotificationFragment notificationFragment = NotificationFragment.newInstance("", "");
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_year_quarter_month, notificationFragment).addToBackStack(null).commit();
    }

    @OnClick(R.id.btn_add)
    public void onAddClick() {
        FragmentManager fm = getChildFragmentManager();
        NhapKhoAddDialogFragment editNameDialogFragment = new NhapKhoAddDialogFragment();
        editNameDialogFragment.show(fm, "fragment_view_info");
    }

    @Subscribe
    public void onEvent(EventNhapHang eventNhapHang) {

        ImportProductEntity importProductEntity = new ImportProductEntity();
        importProductEntity.setProduct_code(eventNhapHang.product_code);
        importProductEntity.setCreated_at(eventNhapHang.created_at);

        listProduct.add(importProductEntity);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return FilterByConditionFragment.newInstance(position + 1, listProduct);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Năm";
            } else if (position == 1) {
                return "Quý";
            } else {
                return "Tháng";
            }
        }
    }

}
