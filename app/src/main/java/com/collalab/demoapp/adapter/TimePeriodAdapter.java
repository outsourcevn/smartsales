package com.collalab.demoapp.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.collalab.demoapp.R;
import com.collalab.demoapp.entity.ImportProductEntity;
import com.collalab.demoapp.entity.PeriodTime;
import com.collalab.demoapp.viewholder.ImportProductViewHolder;
import com.collalab.demoapp.viewholder.PeriodTimeViewHolder;

import java.util.List;

public class TimePeriodAdapter extends ExpandableRecyclerAdapter<PeriodTime, ImportProductEntity, PeriodTimeViewHolder, ImportProductViewHolder> {

    private static final int PARENT_VEGETARIAN = 0;
    private static final int PARENT_NORMAL = 1;
    private static final int CHILD_VEGETARIAN = 2;
    private static final int CHILD_NORMAL = 3;

    private LayoutInflater mInflater;
    private List<PeriodTime> mPeriodTimes;

    public TimePeriodAdapter(Context context, @NonNull List<PeriodTime> recipeList) {
        super(recipeList);
        mPeriodTimes = recipeList;
        mInflater = LayoutInflater.from(context);
    }

    @UiThread
    @NonNull
    @Override
    public PeriodTimeViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View view;
        view = mInflater.inflate(R.layout.layout_time_period, parentViewGroup, false);
        return new PeriodTimeViewHolder(view);
    }

    @UiThread
    @NonNull
    @Override
    public ImportProductViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View ingredientView;
        ingredientView = mInflater.inflate(R.layout.layout_nhan_hang_item, childViewGroup, false);
        return new ImportProductViewHolder(ingredientView);
    }

    @UiThread
    @Override
    public void onBindParentViewHolder(@NonNull PeriodTimeViewHolder recipeViewHolder, int parentPosition, @NonNull PeriodTime recipe) {
        recipeViewHolder.bind(recipe);
    }

    @UiThread
    @Override
    public void onBindChildViewHolder(@NonNull ImportProductViewHolder ingredientViewHolder, int parentPosition, int childPosition, @NonNull ImportProductEntity ingredient) {
        ingredientViewHolder.bind(ingredient);
    }

    @Override
    public int getParentViewType(int parentPosition) {
        return PARENT_NORMAL;
    }

    @Override
    public int getChildViewType(int parentPosition, int childPosition) {
        return CHILD_NORMAL;
    }

    @Override
    public boolean isParentViewType(int viewType) {
        return viewType == PARENT_NORMAL;
    }

}
