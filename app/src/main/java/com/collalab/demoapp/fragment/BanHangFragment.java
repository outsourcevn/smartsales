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

import com.collalab.demoapp.R;
import com.collalab.demoapp.adapter.CodeYearAdapter;
import com.collalab.demoapp.entity.ExportProductEntity;
import com.collalab.demoapp.event.EventBanHang;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BanHangFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    ArrayList<ExportProductEntity> exportProductEntities = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.main_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    CodeYearAdapter codeYearAdapter;

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
        initDummyData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ban_hang, container, false);
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

        codeYearAdapter = new CodeYearAdapter(getContext(), exportProductEntities);
        codeYearAdapter.setOnItemClick(mOnItemClick);
        recyclerView.setAdapter(codeYearAdapter);
        codeYearAdapter.hideFooter();
    }

    @OnClick(R.id.tv_retail)
    public void onRetailClick() {
        KhachLeFragment khachLeFragment = KhachLeFragment.newInstance("", "");
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_ban_hang_container, khachLeFragment).addToBackStack(null).commit();
    }

    @OnClick(R.id.btn_add)
    public void onAddClick() {
        ThemMaBanHang themMaBanHang = ThemMaBanHang.newInstance("", "");
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_ban_hang_container, themMaBanHang).addToBackStack(null).commit();
    }

    private void initDummyData() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 10);
        exportProductEntities.add(new ExportProductEntity(calendar.getTime(), 12, "EUTNAVTEIT" + 1021, false));

        calendar.set(Calendar.DAY_OF_MONTH, 12);
        exportProductEntities.add(new ExportProductEntity(calendar.getTime(), 31, "EUTNAVTEIT" + 1011, false));

        calendar.set(Calendar.DAY_OF_MONTH, 18);
        exportProductEntities.add(new ExportProductEntity(calendar.getTime(), 17, "EUTNAVTEIT" + 1032, true));

        calendar.set(Calendar.DAY_OF_MONTH, 21);
        exportProductEntities.add(new ExportProductEntity(calendar.getTime(), 22, "EUTNAVTEIT" + 1025, false));
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Subscribe
    public void onEvent(EventBanHang eventBanHang) {
        exportProductEntities.add(eventBanHang.exportProductEntity);
        codeYearAdapter.notifyItemInserted(exportProductEntities.size());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    CodeYearAdapter.OnItemClick mOnItemClick = new CodeYearAdapter.OnItemClick() {
        @Override
        public void onItemClick(int position) {
            QuetVaLietKeFragment quetVaLietKeFragment = QuetVaLietKeFragment.newInstance("ban_hang");
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layout_ban_hang_container, quetVaLietKeFragment).addToBackStack(null).commit();
        }

        @Override
        public void onEditClick(int position) {
            ThemMaBanHang themMaBanHang = ThemMaBanHang.newInstance(exportProductEntities.get(position));
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layout_ban_hang_container, themMaBanHang).addToBackStack(null).commit();
        }
    };
}
