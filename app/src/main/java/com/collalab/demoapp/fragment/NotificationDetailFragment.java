package com.collalab.demoapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.collalab.demoapp.R;
import com.collalab.demoapp.entity.NotificationEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationDetailFragment extends Fragment {
    private static final String ARG_PARAM1 = "notification_entity";
    private static final String ARG_PARAM2 = "param2";

    private NotificationEntity notificationEntity;
    private String mParam2;

    @BindView(R.id.tv_title)
    TextView tvTittle;
    @BindView(R.id.tv_detail)
    TextView tvContent;

    public NotificationDetailFragment() {
        // Required empty public constructor
    }

    public static NotificationDetailFragment newInstance(NotificationEntity notificationEntity) {
        NotificationDetailFragment fragment = new NotificationDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, notificationEntity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            notificationEntity = (NotificationEntity) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_back)
    public void OnBackClick() {
        getActivity().onBackPressed();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (notificationEntity != null) {
            tvTittle.setText(notificationEntity.getTitle());
            tvContent.setText(notificationEntity.getContent());
        }
    }
}
