package com.collalab.demoapp.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.collalab.demoapp.R;
import com.collalab.demoapp.entity.ExportProductEntity;
import com.collalab.demoapp.event.EventBanHang;
import com.github.pwittchen.networkevents.library.BusWrapper;
import com.github.pwittchen.networkevents.library.ConnectivityStatus;
import com.github.pwittchen.networkevents.library.NetworkEvents;
import com.github.pwittchen.networkevents.library.NetworkHelper;
import com.github.pwittchen.networkevents.library.event.ConnectivityChanged;
import com.google.zxing.Result;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class KhachLeFragment extends Fragment implements ZXingScannerView.ResultHandler {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private BusWrapper busWrapper;
    private NetworkEvents networkEvents;
    RadioButton radioBtnInternet;

    @BindView(R.id.scanner_view)
    ZXingScannerView mScannerView;

    public KhachLeFragment() {
    }

    public static KhachLeFragment newInstance(String param1, String param2) {
        KhachLeFragment fragment = new KhachLeFragment();
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
        final EventBus bus = new EventBus();
        busWrapper = getGreenRobotBusWrapper(bus);
        networkEvents = new NetworkEvents(getContext(), busWrapper).enableInternetCheck();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_khach_le, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {

        showRadioButtonDialog(result.getText(), result.getBarcodeFormat().name());
        mScannerView.stopCamera();
    }

    @OnClick(R.id.btn_back)
    public void onBackClick() {
        getActivity().onBackPressed();
    }

    private void showRadioButtonDialog(final String productCode, String format) {

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_retail_proccess);

        TextView textView = (TextView) dialog.findViewById(R.id.tv_product_code);
        textView.setText("Mã hàng: " + productCode);
        RadioButton radioBtnSMS = (RadioButton) dialog.findViewById(R.id.radio_btn_sms);
        radioBtnInternet = (RadioButton) dialog.findViewById(R.id.radio_btn_internet);
        View btnClose = dialog.findViewById(R.id.btn_close_dialog);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                getActivity().onBackPressed();
            }
        });

        radioBtnSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareViaSMS(productCode);
                dialog.dismiss();
                EventBanHang eventBanHang = new EventBanHang();
                ExportProductEntity exportProductEntity = new ExportProductEntity();
                exportProductEntity.created_at = Calendar.getInstance().getTime();
                exportProductEntity.number_product_sold = 1;
                getActivity().onBackPressed();
            }
        });

        radioBtnInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                getActivity().onBackPressed();
            }
        });

        if (!NetworkHelper.isConnectedToWiFiOrMobileNetwork(getContext())) {
            radioBtnInternet.setEnabled(false);
        }

        dialog.show();
    }

    void shareViaSMS(String productcode) {
//        if (android.os.Build.VERSION.SDK_INT < 23) {
//            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
//            smsIntent.putExtra("address", 8088);
//            smsIntent.setType("vnd.android-dir/mms-sms");
//            smsIntent.putExtra("sms_body", "SONHA " + productcode);
//            startActivity(smsIntent);
//        } else {
//            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + "8088"));
//            intent.putExtra("address", 8088);
//            intent.putExtra("sms_body", "SONHA " + productcode);
//            startActivity(intent);
//        }
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + "8088"));
        intent.putExtra("address", 8088);
        intent.putExtra("sms_body", "SONHA " + productcode);
        startActivity(intent);
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(ConnectivityChanged event) {
        if (event.getConnectivityStatus() != ConnectivityStatus.OFFLINE) {
            if (radioBtnInternet != null) {
                radioBtnInternet.setEnabled(true);
            }
        } else {
            if (radioBtnInternet != null) {
                radioBtnInternet.setEnabled(false);
            }
        }
    }

    @NonNull
    private BusWrapper getGreenRobotBusWrapper(final EventBus bus) {
        return new BusWrapper() {
            @Override
            public void register(Object object) {
                bus.register(object);
            }

            @Override
            public void unregister(Object object) {
                bus.unregister(object);
            }

            @Override
            public void post(Object event) {
                bus.post(event);
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        busWrapper.register(this);
        networkEvents.register();
    }

    @Override
    public void onStop() {
        busWrapper.unregister(this);
        networkEvents.unregister();
        super.onStop();
    }

}
