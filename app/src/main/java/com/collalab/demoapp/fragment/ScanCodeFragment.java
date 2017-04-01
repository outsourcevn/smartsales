package com.collalab.demoapp.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.collalab.demoapp.R;
import com.collalab.demoapp.entity.EventScan;
import com.google.zxing.Result;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanCodeFragment extends Fragment implements ZXingScannerView.ResultHandler {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    boolean isSubsequenceScan = false;

    private final int TYPE_SUBSEQUENCE = 1;
    private final int TYPE_SMS = 2;
    private final int TYPE_INTERNET = 3;

    @BindView(R.id.scanner_view)
    ZXingScannerView mScannerView;

    public ScanCodeFragment() {

    }

    public static ScanCodeFragment newInstance(boolean quetLienTuc) {
        ScanCodeFragment fragment = new ScanCodeFragment();
        Bundle args = new Bundle();
        args.putBoolean("quet_lien_tuc", quetLienTuc);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isSubsequenceScan = getArguments().getBoolean("quet_lien_tuc");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan_code, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void handleResult(Result result) {
        MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.raw_tick);
        mediaPlayer.start();
        mScannerView.stopCamera();
        if (isSubsequenceScan) {
            showProceedSubsequence(result.getText());
        } else {
            showProceedInternetMethod(result.getText());
        }
    }

    @OnClick(R.id.btn_back)
    public void onBackClick() {
        getActivity().onBackPressed();
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

    private void showProceedSubsequence(final String code) {
        EventScan eventScan = new EventScan();
        eventScan.code = code;
        eventScan.success = false;
        if (isSubsequenceScan) {
            eventScan.isImportProcess = true;
        } else {
            eventScan.isImportProcess = false;
        }
        EventBus.getDefault().post(eventScan);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.setResultHandler(ScanCodeFragment.this);
                mScannerView.startCamera();
            }
        }, 2000);
    }

    private void showProceedInternetMethod(final String code) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        alertDialogBuilder.setTitle("Thành công");
        alertDialogBuilder
                .setMessage("Bạn đã quét thành công mã sản phẩm: " + code + ".\nBạn có muốn lưu lại không?")
                .setCancelable(false)
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isConnected()) {
                            EventScan eventScan = new EventScan();
                            eventScan.code = code;
                            eventScan.success = true;
                            eventScan.processType = "internet";
                            if (isSubsequenceScan) {
                                eventScan.isImportProcess = true;
                            } else {
                                eventScan.isImportProcess = false;
                            }
                            getActivity().onBackPressed();
                            EventBus.getDefault().post(eventScan);
                        } else {
                            showNoNetworkConnectionAlert(code);
                        }
                    }
                })
                .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getActivity().onBackPressed();
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showNoNetworkConnectionAlert(final String code) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        alertDialogBuilder.setTitle("Thông báo");
        alertDialogBuilder
                .setMessage("Hiện tại không có kết nối mạng, do vậy bạn không thể gửi kết quả quét qua Internet!" +
                        "\nXin vui lòng bật kết nối mạng của bạn gửi lại!")
                .setCancelable(false)
                .setPositiveButton("Cài đặt mạng", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        EventScan eventScan = new EventScan();
                        eventScan.code = code;
                        eventScan.success = false;
                        eventScan.processType = "internet";
                        if (isSubsequenceScan) {
                            eventScan.isImportProcess = true;
                        } else {
                            eventScan.isImportProcess = false;
                        }
                        EventBus.getDefault().post(eventScan);
                        getActivity().onBackPressed();
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        getActivity().onBackPressed();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
