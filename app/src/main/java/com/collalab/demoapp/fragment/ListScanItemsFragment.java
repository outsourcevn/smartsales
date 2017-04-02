package com.collalab.demoapp.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.collalab.demoapp.R;
import com.collalab.demoapp.adapter.ScanAdapter;
import com.collalab.demoapp.entity.ScanItem;
import com.collalab.demoapp.event.EventListItemScan;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Array;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListScanItemsFragment extends Fragment {
    public static int TYPE_ALL = 0;
    public static int TYPE_NOT_PROCESSED = 1;
    public static int TYPE_PROCESSED = 2;
    private static final String FILTER_TYPE = "type";
    private static final String ITEMS = "items";

    ArrayList<ScanItem> rawListItem = new ArrayList<>();
    ArrayList<ScanItem> listItem = new ArrayList<>();
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    ScanAdapter scanAdapter;

    @BindView(R.id.layout_process_all)
    View layoutProcessAll;
    private int mFilterType;

    public ListScanItemsFragment() {

    }

    public static ListScanItemsFragment newInstance(int param1) {
        ListScanItemsFragment fragment = new ListScanItemsFragment();
        Bundle args = new Bundle();
        args.putInt(FILTER_TYPE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFilterType = getArguments().getInt(FILTER_TYPE);
        }
    }

    public void setItemList(ArrayList<ScanItem> scanItems) {
        rawListItem = scanItems;
        getItemByType();
    }

    private void getItemByType() {
        listItem.clear();
        if (mFilterType == TYPE_ALL) {
            listItem.addAll(rawListItem);
        } else if (mFilterType == TYPE_NOT_PROCESSED) {
            int size = rawListItem != null ? rawListItem.size() : 0;
            for (int i = 0; i < size; i++) {
                if (!rawListItem.get(i).isSuccess()) {
                    listItem.add(rawListItem.get(i));
                }
            }
        } else if (mFilterType == TYPE_PROCESSED) {
            int size = rawListItem != null ? rawListItem.size() : 0;
            for (int i = 0; i < size; i++) {
                if (rawListItem.get(i).isSuccess()) {
                    listItem.add(rawListItem.get(i));
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_scan_items, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mFilterType == TYPE_NOT_PROCESSED && listItem.size() > 0) {
            layoutProcessAll.setVisibility(View.VISIBLE);
        } else {
            layoutProcessAll.setVisibility(View.GONE);
        }

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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {

            if(mFilterType == TYPE_NOT_PROCESSED) {
                int size = listItem != null ? listItem.size() : 0;
                for(int i = size - 1; i >= 0 ; i--) {
                    if(listItem.get(i).isSuccess()) {
                        listItem.remove(i);
                    }
                }
            }

            if(scanAdapter != null) {
                scanAdapter.notifyDataSetChanged();
            }
        }
    }

    @OnClick(R.id.layout_process_all)
    public void onProcessAll() {

        if (mFilterType != TYPE_NOT_PROCESSED) {
            return;
        }

        if (isConnected()) {
            int size = listItem != null ? listItem.size() : 0;
            for (int i = 0; i < size; i++) {
                listItem.get(i).setSuccess(true);
            }
            scanAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getContext(), "Không có kết nối mạng, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
        }
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
                if(mFilterType == TYPE_NOT_PROCESSED) {
                    listItem.remove(position);
                }
                scanAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "Không có kết nối mạng, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    public boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(sticky = true)
    public void onEvent(EventListItemScan event) {
        rawListItem = event.listScanItems;
        getItemByType();
        if(scanAdapter != null) {
            scanAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
