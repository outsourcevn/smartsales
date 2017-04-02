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
import com.collalab.demoapp.entity.ScanItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by VietMac on 2017-03-24.
 */

public class ScanAdapter extends AdapterFooterView {

    Context context;
    ArrayList<ScanItem> scanItems;

    public ScanAdapter(Context context, ArrayList<ScanItem> productEntities) {
        super(context);
        this.context = context;
        this.scanItems = productEntities;
    }

    OnScanItemAction onScanItemAction;

    public interface OnScanItemAction {
        void onSendSMS(int position);

        void onSendInternet(int position);
    }

    public void setOnScanItemAction(OnScanItemAction onScanItemAction) {
        this.onScanItemAction = onScanItemAction;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_code)
        TextView tvCode;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.btn_delete)
        View btnDelete;
        @BindView(R.id.btn_send_internet)
        View btnSendInternet;
        @BindView(R.id.tv_created_at)
        TextView tvCreatedAt;
        @BindView(R.id.tv_address)
        TextView tvAddress;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onBindViewWithData(RecyclerView.ViewHolder holderRecycle, final int position) {
        ViewHolder viewHolder = (ViewHolder) holderRecycle;
        final ScanItem scanItem = scanItems.get(position);
        viewHolder.tvCode.setText("Mã: " + scanItem.getCode());

        if (scanItem.isSuccess()) {
            viewHolder.tvStatus.setText("Trạng thái: Đã xử lý");
            viewHolder.btnDelete.setVisibility(View.GONE);
            viewHolder.btnSendInternet.setVisibility(View.GONE);
        } else {
            viewHolder.tvStatus.setText("Trạng thái: Chưa xử lý");
            viewHolder.btnDelete.setVisibility(View.VISIBLE);
            viewHolder.btnSendInternet.setVisibility(View.VISIBLE);
        }
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanItems.remove(position);
                notifyItemRemoved(position);
            }
        });

        viewHolder.btnSendInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onScanItemAction != null) {
                    onScanItemAction.onSendInternet(position);
                }
            }
        });
        viewHolder.tvCreatedAt.setText("Quét lúc:" + Common.getDateInStringHHMM(scanItem.getCreated_at()));
        if(!TextUtils.isEmpty(scanItem.getAddress())) {
            viewHolder.tvAddress.setText("Quét tại:" + scanItem.getAddress());
        } else {
            viewHolder.tvAddress.setText("Quét tại" + "Latitude: " + scanItem.getLat() + " Longtitude: " + scanItem.getLng());
        }
    }

    @Override
    public RecyclerView.ViewHolder inflateView(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_liet_ke_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getDataLength() {
        return scanItems != null ? scanItems.size() : 0;
    }
}
