package com.collalab.demoapp.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.collalab.demoapp.R;
import com.collalab.demoapp.entity.EventScan;
import com.collalab.demoapp.persistence.PreferenceUtils;
import com.collalab.demoapp.persistence.PrefsKey;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.camera.OnImageReadyListener;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.Geofence;
import com.google.zxing.Result;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.nlopez.smartlocation.OnActivityUpdatedListener;
import io.nlopez.smartlocation.OnGeofencingTransitionListener;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.geofencing.model.GeofenceModel;
import io.nlopez.smartlocation.geofencing.utils.TransitionGeofence;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.app.Activity.RESULT_OK;

public class ScanCodeFragment extends Fragment implements ZXingScannerView.ResultHandler, OnLocationUpdatedListener, OnActivityUpdatedListener, OnGeofencingTransitionListener {
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

    double lat, lng;
    String address;

    private LocationGooglePlayServicesProvider provider;

    private static final int LOCATION_PERMISSION_ID = 1001;

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
        getLocation();
    }

    private void getLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_ID);
            return;
        }
        startLocation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan_code, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void startLocation() {

        provider = new LocationGooglePlayServicesProvider();
        provider.setCheckLocationSettings(true);

        SmartLocation smartLocation = new SmartLocation.Builder(getContext()).logging(true).build();

        smartLocation.location(provider).start(this);
        smartLocation.activity().start(this);

        // Create some geofences
        GeofenceModel mestalla = new GeofenceModel.Builder("1").setTransition(Geofence.GEOFENCE_TRANSITION_ENTER).setLatitude(39.47453120000001).setLongitude(-0.358065799999963).setRadius(500).build();
        smartLocation.geofencing().add(mestalla).start(this);
    }

    private void stopLocation() {
        SmartLocation.with(getContext()).location().stop();

        SmartLocation.with(getContext()).activity().stop();

        SmartLocation.with(getContext()).geofencing().stop();
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
        eventScan.lat = lat;
        eventScan.lng = lng;
        eventScan.address = address;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Thành công");
        alertDialogBuilder
                .setMessage("Bạn đã quét thành công mã sản phẩm: " + code + "")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        EventBus.getDefault().post(eventScan);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.cancel();
                }
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
                            eventScan.lat = lat;
                            eventScan.lng = lng;
                            eventScan.address = address;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (provider != null) {
            provider.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == LOCATION_PERMISSION_ID && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocation();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityUpdated(DetectedActivity detectedActivity) {

    }

    @Override
    public void onGeofenceTransition(TransitionGeofence transitionGeofence) {

    }

    @Override
    public void onLocationUpdated(Location location) {
        showLocation(location);
    }

    private void showLocation(Location location) {
        if (location != null) {

            lat = location.getLatitude();
            lng = location.getLongitude();

            // We are going to get the address for the current position
            SmartLocation.with(getContext()).geocoding().reverse(location, new OnReverseGeocodingListener() {
                @Override
                public void onAddressResolved(Location original, List<Address> results) {
                    if (results.size() > 0) {
                        Address result = results.get(0);
                        StringBuilder builder = new StringBuilder();
                        List<String> addressElements = new ArrayList<>();
                        for (int i = 0; i <= result.getMaxAddressLineIndex(); i++) {
                            addressElements.add(result.getAddressLine(i));
                        }
                        builder.append(TextUtils.join(", ", addressElements));
                        address = builder.toString();
                        PreferenceUtils.commitString(PrefsKey.KEY_IMAGE_LOCATION, builder.toString());
                    }
                }
            });
        } else {

        }
    }
}
