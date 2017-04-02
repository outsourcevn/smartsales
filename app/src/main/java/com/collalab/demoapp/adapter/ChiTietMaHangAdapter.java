package com.collalab.demoapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.collalab.demoapp.Common;
import com.collalab.demoapp.R;
import com.collalab.demoapp.entity.ProductEntity;
import com.collalab.demoapp.entity.ProductItemEntity;
import com.collalab.demoapp.widget.RichTextView;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by laptop88 on 4/2/2017.
 */

public class ChiTietMaHangAdapter extends AdapterFooterView {

    Context context;
    ArrayList<ProductItemEntity> productItemEntities;

    public ChiTietMaHangAdapter(Context context, ArrayList<ProductItemEntity> productEntities) {
        super(context);
        this.context = context;
        this.productItemEntities = productEntities;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_item_code)
        TextView tvItemCode;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onBindViewWithData(RecyclerView.ViewHolder holderRecycle, int position) {
        ViewHolder viewHolder = (ViewHolder) holderRecycle;
        viewHolder.tvDate.setText("Ngày : " + Common.getDateInString(Calendar.getInstance().getTime()));
        viewHolder.tvItemCode.setText("Mã : " + " 17199104");

    }

    @Override
    public RecyclerView.ViewHolder inflateView(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chi_tiet_ma_hang_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getDataLength() {
//        return scanItems != null ? scanItems.size() : 0;
        return 10;
    }
}
