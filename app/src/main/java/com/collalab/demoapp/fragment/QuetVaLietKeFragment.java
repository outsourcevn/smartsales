package com.collalab.demoapp.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.collalab.demoapp.DemoApp;
import com.collalab.demoapp.R;
import com.collalab.demoapp.adapter.ScanAdapter;
import com.collalab.demoapp.entity.EventScan;
import com.collalab.demoapp.entity.ScanItem;
import com.zaaach.toprightmenu.MenuItem;
import com.zaaach.toprightmenu.TopRightMenu;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuetVaLietKeFragment extends Fragment {
    private static final String ARG_PARAM1 = "nhan_hang_ban_hang";
    private static final String ARG_PARAM2 = "param2";

    private String mNhanHangBanHang;
    private String mParam2;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_add)
    View btnMore;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    ScanAdapter scanAdapter;
    ArrayList<ScanItem> listItem = new ArrayList<>();

    private TopRightMenu mTopRightMenu;
    private boolean showIcon = true;
    private boolean dimBg = true;
    private boolean needAnim = true;

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
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        scanAdapter = new ScanAdapter(getContext(), listItem);
        scanAdapter.setOnScanItemAction(onScanItemAction);
        recyclerView.setAdapter(scanAdapter);

        if (listItem.size() == 0) {
            scanAdapter.hideFooter();
        }
    }

    @OnClick(R.id.btn_back)
    public void onBackClick() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.btn_add)
    public void onAddClick() {
//        if ("nhan_hang".equalsIgnoreCase(mNhanHangBanHang)) {
//            ScanCodeFragment scanCodeFragment = ScanCodeFragment.newInstance(true, DemoApp.sScanMethodNhanHang);
//            getActivity().getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.layout_quet_liet_ke_container, scanCodeFragment).addToBackStack(null).commit();
//        } else {
//            ScanCodeFragment scanCodeFragment = ScanCodeFragment.newInstance(false, DemoApp.sScanMethodBanHang);
//            getActivity().getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.layout_quet_liet_ke_container, scanCodeFragment).addToBackStack(null).commit();
//        }

        mTopRightMenu = new TopRightMenu(getActivity());
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(R.drawable.ic_subsequence, "Quét liên tục"));
        menuItems.add(new MenuItem(R.drawable.ic_internet, "Quét gửi internet"));
        mTopRightMenu
                .setHeight(260)     //默认高度480
                .setWidth(520)      //默认宽度wrap_content
                .showIcon(showIcon)     //显示菜单图标，默认为true
                .dimBackground(dimBg)           //背景变暗，默认为true
                .needAnimationStyle(needAnim)   //显示动画，默认为true
                .setAnimationStyle(R.style.TRM_ANIM_STYLE)  //默认为R.style.TRM_ANIM_STYLE
                .addMenuList(menuItems)
                .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                    @Override
                    public void onMenuItemClick(int position) {
                        if(position == 0) {
                            ScanCodeFragment scanCodeFragment = ScanCodeFragment.newInstance(true);
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.layout_quet_liet_ke_container, scanCodeFragment).addToBackStack(null).commit();
                        } else {
                            ScanCodeFragment scanCodeFragment = ScanCodeFragment.newInstance(false);
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.layout_quet_liet_ke_container, scanCodeFragment).addToBackStack(null).commit();
                        }
                    }
                })
                .showAsDropDown(btnMore, -225, 0);

    }

    ScanAdapter.OnScanItemAction onScanItemAction = new ScanAdapter.OnScanItemAction() {
        @Override
        public void onSendSMS(int position) {
            ScanItem scanItem = listItem.get(position);
            listItem.get(position).setSuccess(true);
            listItem.get(position).setProcessType("sms");
            scanAdapter.notifyDataSetChanged();
        }

        @Override
        public void onSendInternet(int position) {
            ScanItem scanItem = listItem.get(position);
            if (isConnected()) {
                Toast.makeText(getContext(), "Đã gửi thành công mã " + scanItem.getCode() + "!", Toast.LENGTH_SHORT).show();
                listItem.get(position).setSuccess(true);
                listItem.get(position).setProcessType("internet");
                scanAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Không có kết nối mạng, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Subscribe
    public void onEvent(EventScan eventScan) {
        ScanItem scanItem = new ScanItem();
        scanItem.setCode(eventScan.code);
        scanItem.setImportProcess(eventScan.isImportProcess);
        scanItem.setSuccess(eventScan.success);
        scanItem.setProcessType(eventScan.processType);
        listItem.add(scanItem);
        if (listItem.size() > 0) {
            scanAdapter.hideFooter();
        }
        scanAdapter.notifyDataSetChanged();
    }

    public boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
