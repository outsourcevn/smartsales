package com.collalab.demoapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.collalab.demoapp.Common;
import com.collalab.demoapp.R;
import com.collalab.demoapp.entity.ProductEntity;
import com.collalab.demoapp.widget.RichTextView;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by VietMac on 2017-03-24.
 */

public class KhoHangAdapter extends AdapterFooterView {

    Context context;
    ArrayList<ProductEntity> productEntities;

    public KhoHangAdapter(Context context, ArrayList<ProductEntity> productEntities) {
        super(context);
        this.context = context;
        this.productEntities = productEntities;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_import_date)
        RichTextView tvImportedDate;
        @BindView(R.id.tv_product_id)
        RichTextView tvProductCode;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onBindViewWithData(RecyclerView.ViewHolder holderRecycle, int position) {
        ViewHolder viewHolder = (ViewHolder) holderRecycle;
        viewHolder.tvImportedDate.setText("Ngày nhập kho: " + Common.getDateInString(Calendar.getInstance().getTime()));
        viewHolder.tvProductCode.setText("Mã sản phẩm: " + " 21566789");

    }

    @Override
    public RecyclerView.ViewHolder inflateView(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_kho_hang_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getDataLength() {
//        return scanItems != null ? scanItems.size() : 0;
        return 10;
    }
}
