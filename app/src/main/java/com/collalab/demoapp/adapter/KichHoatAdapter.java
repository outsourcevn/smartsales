package com.collalab.demoapp.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.collalab.demoapp.Common;
import com.collalab.demoapp.R;
import com.collalab.demoapp.entity.ImportProductEntity;
import com.collalab.demoapp.entity.ProductEntity;
import com.collalab.demoapp.widget.RichTextView;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by VietMac on 2017-03-24.
 */

public class KichHoatAdapter extends AdapterFooterView {

    Context context;
    ArrayList<ImportProductEntity> productEntities;

    public KichHoatAdapter(Context context, ArrayList<ImportProductEntity> productEntities) {
        super(context);
        this.context = context;
        this.productEntities = productEntities;
    }

    OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        void onItemClick(int position);

        void onEditClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_import_date)
        RichTextView tvImportedDate;
        @BindView(R.id.tv_product_id)
        RichTextView tvProductCode;
        @BindView(R.id.btn_edit)
        View btnEdit;
        @BindView(R.id.btn_delete)
        View btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onBindViewWithData(RecyclerView.ViewHolder holderRecycle, final int position) {
        int type = getItemViewType(position);
        if (type == TYPE_FOOTER) {
            if (productEntities == null || productEntities.size() == 0) {
                hideFooterWithText("Chưa có mã hàng nào được nhập!");
            }
        } else {
            final ImportProductEntity productEntity = productEntities.get(position);
            ViewHolder viewHolder = (ViewHolder) holderRecycle;
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClick != null) {
                        onItemClick.onItemClick(position);
                    }
                }
            });
            viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    productEntities.remove(position);
                    notifyItemRangeRemoved(position, 1);
                }
            });
            viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClick != null) {
                        onItemClick.onEditClick(position);
                    }
                }
            });

            viewHolder.tvImportedDate.setText("Ngày: " + Common.getDateInString(productEntity.getCreated_at()));
            viewHolder.tvProductCode.setText("Mã: " + productEntity.getProduct_code());
        }
    }

    @Override
    public RecyclerView.ViewHolder inflateView(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_nhan_hang_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getDataLength() {
//        return scanItems != null ? scanItems.size() : 0;
        return productEntities.size();
    }
}
