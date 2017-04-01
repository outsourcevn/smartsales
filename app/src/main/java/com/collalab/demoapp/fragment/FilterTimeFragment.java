package com.collalab.demoapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.collalab.demoapp.R;
import com.collalab.demoapp.entity.ImportProductEntity;
import com.collalab.demoapp.event.EventNhapHang;
import com.collalab.demoapp.event.EventSelectYear;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterTimeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    ArrayList<ImportProductEntity> listProduct = new ArrayList<>();
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    String selectYear;
    ViewPagerAdapter adapter;

    public FilterTimeFragment() {
        // Required empty public constructor
    }

    public static FilterTimeFragment newInstance(String param1, String param2) {
        FilterTimeFragment fragment = new FilterTimeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
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
        View view = inflater.inflate(R.layout.filter_by_time_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (TextUtils.isEmpty(selectYear) && position == 3) {
                    Toast.makeText(getContext(), "Vui lòng chọn năm trước!", Toast.LENGTH_SHORT).show();
                    viewPager.setCurrentItem(0);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.btn_notification)
    public void onNotificationClick() {
        NotificationFragment notificationFragment = NotificationFragment.newInstance("", "");
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_year_quarter_month, notificationFragment).addToBackStack(null).commit();
    }

    @Subscribe
    public void onEvent(EventNhapHang eventNhapHang) {

        ImportProductEntity importProductEntity = new ImportProductEntity();
        importProductEntity.setProduct_code(eventNhapHang.product_code);
        importProductEntity.setCreated_at(eventNhapHang.created_at);

        listProduct.add(importProductEntity);
    }

    @Subscribe
    public void onEvent(EventSelectYear eventSelectYear) {
        selectYear = eventSelectYear.selectedYear;
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return SelectYearFragment.newInstance("", "");
                case 1:
                    return FilterByConditionFragment.newInstance(3, listProduct);
                case 2:
                    return FilterByConditionFragment.newInstance(2, listProduct);
                case 3:
                    if (!TextUtils.isEmpty(selectYear)) {
                        return CodeByYearFragment.newInstance(Integer.valueOf(selectYear));
                    }
            }
            return new Fragment();
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Chọn năm";
            } else if (position == 1) {
                return "Tháng";
            } else if (position == 2) {
                return "Quý";
            } else if (position == 3) {
                if (!TextUtils.isEmpty(selectYear)) {
                    return selectYear;
                } else {
                    return "Năm";
                }
            }
            return "";
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
