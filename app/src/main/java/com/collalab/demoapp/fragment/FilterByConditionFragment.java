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
import com.collalab.demoapp.adapter.TimePeriodAdapter;
import com.collalab.demoapp.entity.ImportProductEntity;
import com.collalab.demoapp.entity.PeriodTime;
import com.collalab.demoapp.event.EventItemClick;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterByConditionFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int typeFilter;
    private String mParam2;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    List<PeriodTime> listPeriod;
    TimePeriodAdapter timePeriodAdapter;
    ArrayList<ImportProductEntity> importProductEntities = new ArrayList<>();

    public FilterByConditionFragment() {
    }

    public static FilterByConditionFragment newInstance(int type, ArrayList<ImportProductEntity> importProductEntities) {
        FilterByConditionFragment fragment = new FilterByConditionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, type);
        args.putSerializable(ARG_PARAM2, importProductEntities);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            typeFilter = getArguments().getInt(ARG_PARAM1);
            importProductEntities = (ArrayList<ImportProductEntity>) getArguments().getSerializable(ARG_PARAM2);
        }
        initDummy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_by_condition, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        timePeriodAdapter = new TimePeriodAdapter(getContext(), listPeriod);
        timePeriodAdapter.setOnItemClick(onItemClick);
        recyclerView.setAdapter(timePeriodAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    TimePeriodAdapter.OnItemClick onItemClick = new TimePeriodAdapter.OnItemClick() {
        @Override
        public void onClick(int parentPos, int childPos) {
            EventBus.getDefault().post(new EventItemClick());
        }
    };

    private void initDummy() {
        //Trong business day se la ham loc cac ket qua theo nam, quy, thang
        Calendar calendar = Calendar.getInstance();
        if (typeFilter == 1) {
            calendar.set(Calendar.YEAR, 2009);
            ImportProductEntity product1 = new ImportProductEntity();
            product1.setProduct_code("88092148092");
            product1.setCreated_at(calendar.getTime());


            ImportProductEntity product2 = new ImportProductEntity();
            product2.setProduct_code("87349580249");
            product2.setCreated_at(calendar.getTime());

            PeriodTime time2009 = new PeriodTime("2009", Arrays.asList(product1, product2));

            calendar.set(Calendar.YEAR, 2011);
            ImportProductEntity product3 = new ImportProductEntity();
            product3.setProduct_code("16349812345");
            product3.setCreated_at(calendar.getTime());


            ImportProductEntity product4 = new ImportProductEntity();
            product4.setProduct_code("12346743245");
            product4.setCreated_at(calendar.getTime());

            PeriodTime time2011 = new PeriodTime("2011", Arrays.asList(product3, product4));

            calendar.set(Calendar.YEAR, 2017);
            ImportProductEntity product5 = new ImportProductEntity();
            product5.setProduct_code("88092148092");
            product5.setCreated_at(calendar.getTime());


            ImportProductEntity product6 = new ImportProductEntity();
            product6.setProduct_code("87349580249");
            product6.setCreated_at(calendar.getTime());
            PeriodTime time2017 = new PeriodTime("2017", Arrays.asList(product5, product6));

            listPeriod = Arrays.asList(time2009, time2011, time2017);

        } else if (typeFilter == 2) {

            calendar.set(Calendar.MONTH, 1);
            ImportProductEntity product1 = new ImportProductEntity();
            product1.setProduct_code("88092148092");
            product1.setCreated_at(calendar.getTime());

            calendar.set(Calendar.MONTH, 2);
            ImportProductEntity product2 = new ImportProductEntity();
            product2.setProduct_code("87349580249");
            product2.setCreated_at(calendar.getTime());

            PeriodTime time2009 = new PeriodTime("Quý 1", Arrays.asList(product1, product2));

            calendar.set(Calendar.MONTH, 4);
            ImportProductEntity product3 = new ImportProductEntity();
            product3.setProduct_code("16349812345");
            product3.setCreated_at(calendar.getTime());

            calendar.set(Calendar.MONTH, 6);
            ImportProductEntity product4 = new ImportProductEntity();
            product4.setProduct_code("12346743245");
            product4.setCreated_at(calendar.getTime());

            PeriodTime time2011 = new PeriodTime("Quý 2", Arrays.asList(product3, product4));

            calendar.set(Calendar.MONTH, 10);
            ImportProductEntity product5 = new ImportProductEntity();
            product5.setProduct_code("88092148092");
            product5.setCreated_at(calendar.getTime());

            calendar.set(Calendar.MONTH, 12);
            ImportProductEntity product6 = new ImportProductEntity();
            product6.setProduct_code("87349580249");
            product6.setCreated_at(calendar.getTime());
            PeriodTime time2017 = new PeriodTime("Quý 4", Arrays.asList(product5, product6));

            listPeriod = Arrays.asList(time2009, time2011, time2017);

        } else if (typeFilter == 3) {
            calendar.set(Calendar.MONTH, 1);
            ImportProductEntity product1 = new ImportProductEntity();
            product1.setProduct_code("88092148092");
            product1.setCreated_at(calendar.getTime());

            ImportProductEntity product2 = new ImportProductEntity();
            product2.setProduct_code("87349580249");
            product2.setCreated_at(calendar.getTime());

            PeriodTime time2009 = new PeriodTime("Tháng 1", Arrays.asList(product1, product2));

            calendar.set(Calendar.MONTH, 2);
            ImportProductEntity product3 = new ImportProductEntity();
            product3.setProduct_code("16349812345");
            product3.setCreated_at(calendar.getTime());


            ImportProductEntity product4 = new ImportProductEntity();
            product4.setProduct_code("12346743245");
            product4.setCreated_at(calendar.getTime());

            PeriodTime time2011 = new PeriodTime("Tháng 2", Arrays.asList(product3, product4));

            calendar.set(Calendar.MONTH, 3);
            ImportProductEntity product5 = new ImportProductEntity();
            product5.setProduct_code("88092148092");
            product5.setCreated_at(calendar.getTime());

            ImportProductEntity product6 = new ImportProductEntity();
            product6.setProduct_code("87349580249");
            product6.setCreated_at(calendar.getTime());
            PeriodTime time2017 = new PeriodTime("Tháng 3", Arrays.asList(product5, product6));

            listPeriod = Arrays.asList(time2009, time2011, time2017);
        }

    }

}
