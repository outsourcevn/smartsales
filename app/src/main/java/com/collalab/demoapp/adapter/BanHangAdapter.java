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
import com.collalab.demoapp.entity.ExportProductEntity;
import com.collalab.demoapp.entity.ImportProductEntity;
import com.collalab.demoapp.widget.RichTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by VietMac on 2017-03-24.
 */

public class BanHangAdapter extends AdapterFooterView {

    Context context;
    ArrayList<ExportProductEntity> exportProductEntities;

    public BanHangAdapter(Context context, ArrayList<ExportProductEntity> productEntities) {
        super(context);
        this.context = context;
        this.exportProductEntities = productEntities;
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
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_number)
        TextView tvNumber;
        @BindView(R.id.tv_code)
        TextView tvCode;
        @BindView(R.id.btn_delete)
        View btnDelete;
        @BindView(R.id.btn_edit)
        View btnEdit;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onBindViewWithData(RecyclerView.ViewHolder holderRecycle, final int position) {
        int type = getItemViewType(position);
        if (type == TYPE_FOOTER) {
            if (exportProductEntities == null || exportProductEntities.size() == 0) {
                hideFooterWithText("Chưa có mã hàng nào được bán!");
            }
        } else {
            final ExportProductEntity exportProductEntity = exportProductEntities.get(position);
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
                    exportProductEntities.remove(position);
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

            if (exportProductEntity.isRetail) {
                viewHolder.tvCode.setVisibility(View.GONE);
            } else {
                viewHolder.tvCode.setVisibility(View.VISIBLE);
                viewHolder.tvCode.setText(exportProductEntity.code);
            }

            viewHolder.tvNumber.setText("Số lượng: " + exportProductEntity.number_product_sold);
            viewHolder.tvDate.setText("Ngày: " + Common.getDateInString(exportProductEntity.created_at));
        }
    }

    @Override
    public RecyclerView.ViewHolder inflateView(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_ban_hang_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getDataLength() {
//        return scanItems != null ? scanItems.size() : 0;
        return exportProductEntities.size();
    }
}
