package com.collalab.demoapp.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.collalab.demoapp.Common;
import com.collalab.demoapp.R;
import com.collalab.demoapp.persistence.PreferenceUtils;
import com.collalab.demoapp.persistence.PrefsKey;
import com.collalab.demoapp.widget.RoundedCornersTranformation;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ImagePickerActivity;
import com.esafirm.imagepicker.features.camera.CameraModule;
import com.esafirm.imagepicker.features.camera.ImmediateCameraModule;
import com.esafirm.imagepicker.features.camera.OnImageReadyListener;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.Geofence;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
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

import static android.app.Activity.RESULT_OK;

public class StoreImageFragment extends Fragment implements OnLocationUpdatedListener, OnActivityUpdatedListener, OnGeofencingTransitionListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private static final int RC_CODE_PICKER = 2000;
    private static final int RC_CAMERA = 3000;

    private ArrayList<Image> images = new ArrayList<>();
    private CameraModule cameraModule;

    @BindView(R.id.img_store)
    ImageView imgStore;
    @BindView(R.id.tv_created_at)
    TextView tvCreatedAt;
    @BindView(R.id.tv_gps_location)
    TextView tvGpsLocation;

    private LocationGooglePlayServicesProvider provider;

    private static final int LOCATION_PERMISSION_ID = 1001;

    public StoreImageFragment() {
        // Required empty public constructor
    }

    public static StoreImageFragment newInstance(String param1, String param2) {
        StoreImageFragment fragment = new StoreImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void getLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_ID);
            return;
        }
        startLocation();
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        PreferenceUtils.init(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_image, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String imgPath = PreferenceUtils.getString(PrefsKey.KEY_STORE_IMG, "");
        long created_at = PreferenceUtils.getLong(PrefsKey.KEY_STORE_IMG_CREATED_AT, 0);
        String imageLocation = PreferenceUtils.getString(PrefsKey.KEY_IMAGE_LOCATION, "");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(created_at);
        tvCreatedAt.setText("Thời điểm chụp:" + Common.getDateInStringHHMM(calendar.getTime()));
        if (!TextUtils.isEmpty(imgPath)) {
            Glide.with(getContext())
                    .load(Uri.fromFile(new File(imgPath)))
                    .centerCrop()
                    .bitmapTransform(new RoundedCornersTranformation(getContext(), 30, 0,
                            RoundedCornersTranformation.CornerType.ALL))
                    .placeholder(R.drawable.ic_shop_place_holder)
                    .centerCrop()
                    .into(imgStore);
        }
        tvGpsLocation.setText("Vị trí chụp: " + imageLocation);
    }

    @OnClick(R.id.btn_back)
    public void onBackClick() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.btn_update_image)
    public void onUpdateImageClick() {
        start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_CAMERA) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureImage();
            }
        }

        if (requestCode == LOCATION_PERMISSION_ID && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocation();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void captureImage() {
        startActivityForResult(
                getCameraModule().getCameraIntent(getContext()), RC_CAMERA);
    }

    private ImmediateCameraModule getCameraModule() {
        if (cameraModule == null) {
            cameraModule = new ImmediateCameraModule();
        }
        return (ImmediateCameraModule) cameraModule;
    }

    // Recommended builder
    public void start() {
        ImagePicker imagePicker = ImagePicker.create(this)
                .returnAfterFirst(true) // set whether pick action or camera action should return immediate result or not. Only works in single mode for image picker
                .folderMode(true) // set folder mode (false by default)
                .folderTitle("Folder") // folder selection title
                .imageTitle("Tap to select"); // image selection title

        if (true) {
            imagePicker.single();
        } else {
            imagePicker.multi(); // multi mode (default mode)
        }

        imagePicker.limit(1) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera")   // captured image directory name ("Camera" folder by default)
                .origin(images) // original selected images, used in multi mode
                .start(RC_CODE_PICKER); // start image picker activity with request code
    }

    // Traditional intent
    public void startWithIntent() {
        Intent intent = new Intent(getContext(), ImagePickerActivity.class);
        intent.putExtra(ImagePicker.EXTRA_FOLDER_MODE, true);
        intent.putExtra(ImagePicker.EXTRA_MODE, ImagePicker.MODE_MULTIPLE);
        intent.putExtra(ImagePicker.EXTRA_LIMIT, 10);
        intent.putExtra(ImagePicker.EXTRA_SHOW_CAMERA, true);
        intent.putExtra(ImagePicker.EXTRA_SELECTED_IMAGES, images);
        intent.putExtra(ImagePicker.EXTRA_FOLDER_TITLE, "Album");
        intent.putExtra(ImagePicker.EXTRA_IMAGE_TITLE, "Tap to select images");
        intent.putExtra(ImagePicker.EXTRA_IMAGE_DIRECTORY, "Camera");

        /* Will force ImagePicker to single pick */
        intent.putExtra(ImagePicker.EXTRA_RETURN_AFTER_FIRST, true);

        startActivityForResult(intent, RC_CODE_PICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            images = (ArrayList<Image>) ImagePicker.getImages(data);
            bind2ImageView(images);
            return;
        }

        if (requestCode == RC_CAMERA && resultCode == RESULT_OK) {
            getCameraModule().getImage(getContext(), data, new OnImageReadyListener() {
                @Override
                public void onImageReady(List<Image> resultImages) {
                    images = (ArrayList<Image>) resultImages;
                    bind2ImageView(images);
                }
            });
        }
        super.onActivityResult(requestCode, resultCode, data);

        if (provider != null) {
            provider.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void bind2ImageView(List<Image> images) {
        if (images == null) return;
        Glide
                .with(getContext())
                .load(Uri.fromFile(new File(images.get(0).getPath())))
                .centerCrop()
                .bitmapTransform(new RoundedCornersTranformation(getContext(), 30, 0,
                        RoundedCornersTranformation.CornerType.ALL))
                .placeholder(R.drawable.ic_shop_place_holder)
                .centerCrop()
                .into(imgStore);
        File file = new File(images.get(0).getPath());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(file.lastModified());
        tvCreatedAt.setText("Thời điểm chụp:" + Common.getDateInStringHHMM(calendar.getTime()));
        PreferenceUtils.commitString(PrefsKey.KEY_STORE_IMG, images.get(0).getPath());
        PreferenceUtils.commitLong(PrefsKey.KEY_STORE_IMG_CREATED_AT, file.lastModified());
        getLocation();
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

            final String text = String.format("Latitude %.6f\nLongitude %.6f",
                    location.getLatitude(),
                    location.getLongitude());
            tvGpsLocation.setText("Vị trí chụp: " + text);
            PreferenceUtils.commitString(PrefsKey.KEY_IMAGE_LOCATION, text);

            // We are going to get the address for the current position
            SmartLocation.with(getContext()).geocoding().reverse(location, new OnReverseGeocodingListener() {
                @Override
                public void onAddressResolved(Location original, List<Address> results) {
                    if (results.size() > 0) {
                        Address result = results.get(0);
                        StringBuilder builder = new StringBuilder();
                        builder.append("\n");
                        List<String> addressElements = new ArrayList<>();
                        for (int i = 0; i <= result.getMaxAddressLineIndex(); i++) {
                            addressElements.add(result.getAddressLine(i));
                        }
                        builder.append(TextUtils.join(", ", addressElements));
                        tvGpsLocation.setText("Vị trí chụp: " + builder.toString());
                        PreferenceUtils.commitString(PrefsKey.KEY_IMAGE_LOCATION, builder.toString());
                    }
                }
            });
        } else {
            tvGpsLocation.setText("Vị trị chụp: Không xác định được");
        }
    }

}
