package com.collalab.demoapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.collalab.demoapp.Common;
import com.collalab.demoapp.R;
import com.collalab.demoapp.entity.ExportProductEntity;
import com.collalab.demoapp.entity.ReturnProductEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by VietMac on 2017-03-24.
 */

public class TraHangAdapter extends AdapterFooterView {

    Context context;
    ArrayList<ReturnProductEntity> returnProductEntities;

    public TraHangAdapter(Context context, ArrayList<ReturnProductEntity> returnProductEntities) {
        super(context);
        this.context = context;
        this.returnProductEntities = returnProductEntities;
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
            if (returnProductEntities == null || returnProductEntities.size() == 0) {
                hideFooterWithText("Không có mã hàng nào trả lại!");
            }
        } else {
            final ReturnProductEntity returnProductEntity = returnProductEntities.get(position);

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
                    if(returnProductEntities.size() > position) {
                        returnProductEntities.remove(position);
                        notifyItemRangeRemoved(position, 1);
                    }
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
            viewHolder.btnEdit.setVisibility(View.GONE);

            viewHolder.tvCode.setText("Mã hàng trả: " + returnProductEntity.getProduct_code());
            viewHolder.tvDate.setText("Ngày trả: " + Common.getDateInString(returnProductEntity.getCreated_at()));
        }
    }

    @Override
    public RecyclerView.ViewHolder inflateView(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tra_hang_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getDataLength() {
//        return scanItems != null ? scanItems.size() : 0;
        return returnProductEntities != null ? returnProductEntities.size() : 0;
    }
}
