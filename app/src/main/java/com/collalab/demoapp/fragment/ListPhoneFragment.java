package com.collalab.demoapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.collalab.demoapp.R;
import com.collalab.demoapp.adapter.PhoneAdapter;
import com.collalab.demoapp.persistence.PreferenceUtils;
import com.collalab.demoapp.persistence.PrefsKey;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListPhoneFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    PhoneAdapter phoneAdapter;
    ArrayList<String> listPhone = new ArrayList<>();


    public ListPhoneFragment() {
        // Required empty public constructor
    }

    public static ListPhoneFragment newInstance(String param1, String param2) {
        ListPhoneFragment fragment = new ListPhoneFragment();
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
        PreferenceUtils.init(getContext());
        initDummyPhone();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_phone, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        phoneAdapter = new PhoneAdapter(getContext(), listPhone, PreferenceUtils.getBoolean(PrefsKey.KEY_IS_MAIN_NUMBER, false));
        recyclerView.setAdapter(phoneAdapter);

    }

    @OnClick(R.id.btn_back)
    public void onBackClick() {
        getActivity().onBackPressed();
    }

    private void initDummyPhone() {
        listPhone.add("09398846876");
        listPhone.add("0982717666");
        listPhone.add("01649587563");
        listPhone.add("0934655054");
        listPhone.add("0988363606");
        listPhone.add("0974090259");
    }
}
