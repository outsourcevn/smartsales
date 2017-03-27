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
import android.widget.TextView;

import com.collalab.demoapp.DemoApp;
import com.collalab.demoapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuetVaLietKeFragment extends Fragment {
    private static final String ARG_PARAM1 = "nhan_hang_ban_hang";
    private static final String ARG_PARAM2 = "param2";

    private String mNhanHangBanHang;
    private String mParam2;

    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    public QuetVaLietKeFragment() {
    }

    public static QuetVaLietKeFragment newInstance(String param1) {
        QuetVaLietKeFragment fragment = new QuetVaLietKeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNhanHangBanHang = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quet_va_liet_ke, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        if ("nhan_hang".equalsIgnoreCase(mNhanHangBanHang)) {
            tvTitle.setText(getResources().getString(R.string.string_ma_nhan));
        } else {
            tvTitle.setText(getResources().getString(R.string.string_ma_ban));
        }
    }

    @OnClick(R.id.btn_back)
    public void onBackClick() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.btn_add)
    public void onAddClick() {
        if ("nhan_hang".equalsIgnoreCase(mNhanHangBanHang)) {
            ScanCodeFragment scanCodeFragment = ScanCodeFragment.newInstance(true, DemoApp.sScanMethodNhanHang);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layout_quet_liet_ke_container, scanCodeFragment).addToBackStack(null).commit();
        } else {
            ScanCodeFragment scanCodeFragment = ScanCodeFragment.newInstance(false, DemoApp.sScanMethodBanHang);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layout_quet_liet_ke_container, scanCodeFragment).addToBackStack(null).commit();
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if ("nhan_hang".equalsIgnoreCase(mNhanHangBanHang)) {
                    return QuetTabFragment.newInstance("nhan_hang");
                } else {
                    return QuetTabFragment.newInstance("ban_hang");
                }

            } else {
                if ("nhan_hang".equalsIgnoreCase(mNhanHangBanHang)) {
                    return LietKeTabFragment.newInstance("nhan_hang");
                } else {
                    return LietKeTabFragment.newInstance("ban_hang");
                }
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Quét";
            } else {
                return "Liệt Kê";
            }
        }
    }
}
