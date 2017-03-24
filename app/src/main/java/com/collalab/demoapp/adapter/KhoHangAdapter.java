package com.collalab.demoapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.collalab.demoapp.Common;
import com.collalab.demoapp.R;
import com.collalab.demoapp.entity.ProductEntity;
import com.collalab.demoapp.widget.RichTextView;

import java.util.ArrayList;

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
        @BindView(R.id.tv_product_name)
        TextView tvProductName;
        @BindView(R.id.tv_import_date)
        RichTextView tvImportedDate;
        @BindView(R.id.tv_product_id)
        RichTextView tvProductCode;
        @BindView(R.id.tv_remain_count)
        RichTextView tvRemainProductCount;
        @BindView(R.id.tv_number_sold)
        RichTextView tvNumberProductSold;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onBindViewWithData(RecyclerView.ViewHolder holderRecycle, int position) {
        ProductEntity productEntity = productEntities.get(position);
        ViewHolder viewHolder = (ViewHolder) holderRecycle;
        if (!TextUtils.isEmpty(productEntity.getProduct_name())) {
            viewHolder.tvProductName.setText(productEntity.getProduct_name());
        } else {
            viewHolder.tvProductName.setText("Chưa cập nhật tên sản phẩm");
        }
        if (productEntity.getImport_date() != null) {
            viewHolder.tvImportedDate.setText(Common.getDateInString(productEntity.getImport_date()));
        }
        if (!TextUtils.isEmpty(productEntity.getProduct_code())) {
            viewHolder.tvProductCode.setText("Mã sản phẩm: ");
        }
    }

    @Override
    public RecyclerView.ViewHolder inflateView(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_kho_hang_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getDataLength() {
//        return productEntities != null ? productEntities.size() : 0;
        return 10;
    }
}
