package com.collalab.demoapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.collalab.demoapp.R;
import com.collalab.demoapp.adapter.KhoHangAdapter;
import com.collalab.demoapp.adapter.NhapHangAdapter;
import com.collalab.demoapp.entity.DateData;
import com.collalab.demoapp.entity.ImportProductEntity;
import com.collalab.demoapp.entity.ProductEntity;
import com.collalab.demoapp.event.EventNhapHang;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;


public class NhanHangFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private LinearLayoutManager mLayoutManager;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.main_swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    NhapHangAdapter nhapHangAdapter;
    ArrayList<ImportProductEntity> listProduct = new ArrayList<>();

    Realm realm;

    public NhanHangFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public static NhanHangFragment newInstance(String param1, String param2) {
        NhanHangFragment fragment = new NhanHangFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nhan_hang, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        realm = Realm.getDefaultInstance();
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(mLayoutManager);

        nhapHangAdapter = new NhapHangAdapter(getContext(), listProduct);
        nhapHangAdapter.setOnItemClick(onItemClick);
        recyclerView.setAdapter(nhapHangAdapter);

//        getAllImportedProduct();
    }

    private void getAllImportedProduct() {
        RealmResults<ImportProductEntity> query = realm.where(ImportProductEntity.class).findAllSorted("created_at", Sort.DESCENDING);
        if (query != null && query.size() > 0) {
            int size = query.size();
            for (int i = 0; i < size; i++) {
                listProduct.add(query.get(i));
            }
        }
    }

    @OnClick(R.id.btn_notification)
    public void onNotificationClick() {
        NotificationFragment notificationFragment = NotificationFragment.newInstance("", "");
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_nhan_hang, notificationFragment).addToBackStack(null).commit();
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

        nhapHangAdapter.notifyItemInserted(listProduct.size());
        nhapHangAdapter.notifyDataSetChanged();
        if (listProduct.size() > 0) {
            nhapHangAdapter.hideFooter();
        }
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }

    NhapHangAdapter.OnItemClick onItemClick = new NhapHangAdapter.OnItemClick() {
        @Override
        public void onItemClick(int position) {
            MaNhanFragment maNhanFragment = MaNhanFragment.newInstance("","");
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_nhan_hang, maNhanFragment).addToBackStack(null).commit();
        }

        @Override
        public void onEditClick(int position) {
            if(listProduct != null && listProduct.size() > position) {
                ImportProductEntity importProductEntity = listProduct.get(position);
                DateData dateData = new DateData();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(importProductEntity.getCreated_at());
                dateData.year = calendar.get(Calendar.YEAR);
                dateData.month = calendar.get(Calendar.MONTH);
                dateData.day = calendar.get(Calendar.DAY_OF_MONTH);
                FragmentManager fm = getChildFragmentManager();
                NhapKhoAddDialogFragment editNameDialogFragment = NhapKhoAddDialogFragment.newInstance(importProductEntity.getProduct_code(),dateData);
                editNameDialogFragment.show(fm, "fragment_view_info");
            }
        }
    };
}
