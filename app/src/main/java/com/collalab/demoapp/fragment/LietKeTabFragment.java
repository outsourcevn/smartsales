package com.collalab.demoapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import com.collalab.demoapp.adapter.PhoneAdapter;
import com.collalab.demoapp.adapter.ScanAdapter;
import com.collalab.demoapp.entity.EventScan;
import com.collalab.demoapp.entity.ScanItem;
import com.collalab.demoapp.persistence.PreferenceUtils;
import com.collalab.demoapp.persistence.PrefsKey;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LietKeTabFragment extends Fragment {
    private static final String ARG_PARAM1 = "main_tab";

    private String mMainTab;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    ScanAdapter scanAdapter;
    ArrayList<ScanItem> listItem = new ArrayList<>();

    public LietKeTabFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public static LietKeTabFragment newInstance(String param1) {
        LietKeTabFragment fragment = new LietKeTabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMainTab = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liet_ke, container, false);
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

    ScanAdapter.OnScanItemAction onScanItemAction = new ScanAdapter.OnScanItemAction() {
        @Override
        public void onSendSMS(int position) {
            ScanItem scanItem = listItem.get(position);
            sendSMS(scanItem.getCode());
            listItem.get(position).setSuccess(true);
            scanAdapter.notifyDataSetChanged();
        }

        @Override
        public void onSendInternet(int position) {
            ScanItem scanItem = listItem.get(position);
            if (isConnected()) {
                Toast.makeText(getContext(), "Đã gửi thành công mã " + scanItem.getCode() + "!", Toast.LENGTH_SHORT).show();
                listItem.get(position).setSuccess(true);
                scanAdapter.notifyItemChanged(position);
            } else {
                Toast.makeText(getContext(), "Không có kết nối mạng, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void sendSMS(String productcode) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + "8088"));
        intent.putExtra("address", 8088);
        intent.putExtra("sms_body", "SONHA " + productcode);
        startActivity(intent);
    }

    @Subscribe
    public void onEvent(EventScan eventScan) {
        ScanItem scanItem = new ScanItem();
        scanItem.setCode(eventScan.code);
        scanItem.setImportProcess(eventScan.isImportProcess);
        scanItem.setSuccess(eventScan.success);
        listItem.add(scanItem);
        if (listItem.size() > 0) {
            scanAdapter.hideFooter();
        }
        scanAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
