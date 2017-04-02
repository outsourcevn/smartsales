package com.collalab.demoapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.collalab.demoapp.R;
import com.collalab.demoapp.adapter.CodeYearAdapter;
import com.collalab.demoapp.entity.ExportProductEntity;
import com.collalab.demoapp.event.EventItemClick;
import com.collalab.demoapp.event.EventSelectYear;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CodeByYearFragment extends Fragment {
    private static final String ARG_PARAM1 = "year_selected";
    private static final String ARG_PARAM2 = "param2";
    private int yearSelected;
    private String mParam2;

    ArrayList<ExportProductEntity> exportProductEntities;

    CodeYearAdapter codeYearAdapter;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;

    @BindView(R.id.tv_total_item)
    TextView tvTotalNumberItem;
    @BindView(R.id.view_divider)
    View dividerView;

    public CodeByYearFragment() {
        // Required empty public constructor
    }

    public static CodeByYearFragment newInstance(int year) {
        CodeByYearFragment fragment = new CodeByYearFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, year);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            yearSelected = getArguments().getInt(ARG_PARAM1);
        }
        initDummyData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_code_by_year, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        codeYearAdapter = new CodeYearAdapter(getContext(), exportProductEntities);
        codeYearAdapter.setOnItemClick(onItemClick);
        recyclerView.setAdapter(codeYearAdapter);
        if(exportProductEntities != null && exportProductEntities.size() > 0 && codeYearAdapter != null) {
            tvTotalNumberItem.setVisibility(View.VISIBLE);
            dividerView.setVisibility(View.VISIBLE);
            tvTotalNumberItem.setText("Tổng số: " + exportProductEntities.size());
        } else {
            tvTotalNumberItem.setVisibility(View.GONE);
            dividerView.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void onEvent(EventSelectYear eventSelectYear) {
        yearSelected = Integer.valueOf(eventSelectYear.selectedYear);
        initDummyData();
        if(exportProductEntities != null && exportProductEntities.size() > 0 && codeYearAdapter != null) {
            tvTotalNumberItem.setVisibility(View.VISIBLE);
            dividerView.setVisibility(View.VISIBLE);
            tvTotalNumberItem.setText("Tổng số: " + exportProductEntities.size());
        } else {
            tvTotalNumberItem.setVisibility(View.GONE);
            dividerView.setVisibility(View.GONE);
        }
        codeYearAdapter.notifyDataSetChanged();
    }

    private void initDummyData() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, yearSelected);


        exportProductEntities = new ArrayList<>();
        calendar.set(Calendar.MONTH, 1);
        exportProductEntities.add(new ExportProductEntity(calendar.getTime(), 0, "15061994", false));

        calendar.set(Calendar.MONTH, 2);
        exportProductEntities.add(new ExportProductEntity(calendar.getTime(), 0, "15061994", false));

        calendar.set(Calendar.MONTH, 3);
        exportProductEntities.add(new ExportProductEntity(calendar.getTime(), 0, "15061994", false));

        calendar.set(Calendar.MONTH, 4);
        exportProductEntities.add(new ExportProductEntity(calendar.getTime(), 0, "15061994", false));

        calendar.set(Calendar.MONTH, 5);
        exportProductEntities.add(new ExportProductEntity(calendar.getTime(), 0, "15061994", false));

        calendar.set(Calendar.MONTH, 6);
        exportProductEntities.add(new ExportProductEntity(calendar.getTime(), 0, "15061994", false));

        calendar.set(Calendar.MONTH, 7);
        exportProductEntities.add(new ExportProductEntity(calendar.getTime(), 0, "15061994", false));

        calendar.set(Calendar.MONTH, 8);
        exportProductEntities.add(new ExportProductEntity(calendar.getTime(), 0, "15061994", false));

        calendar.set(Calendar.MONTH, 9);
        exportProductEntities.add(new ExportProductEntity(calendar.getTime(), 0, "15061994", false));

        calendar.set(Calendar.MONTH, 10);
        exportProductEntities.add(new ExportProductEntity(calendar.getTime(), 0, "15061994", false));
    }

    CodeYearAdapter.OnItemClick onItemClick = new CodeYearAdapter.OnItemClick() {
        @Override
        public void onItemClick(int position) {
            EventBus.getDefault().post(new EventItemClick());
        }

        @Override
        public void onEditClick(int position) {

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
