package com.collalab.demoapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        @BindView(R.id.layout_do_action)
        View layoutDoAction;
        @BindView(R.id.btn_delete)
        View btnDelete;
        @BindView(R.id.btn_send_internet)
        View btnSendInternet;
        @BindView(R.id.btn_send_sms)
        View btnSendSMS;

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
            if("sms".equalsIgnoreCase(scanItem.getProcessType())) {
                viewHolder.tvStatus.setText("Trạng thái: Đã xử lý (Bằng gửi SMS)");
            } else if("internet".equalsIgnoreCase(scanItem.getProcessType())) {
                viewHolder.tvStatus.setText("Trạng thái: Đã xử lý (Bằng gửi qua Internet)");
            }
            viewHolder.layoutDoAction.setVisibility(View.GONE);
        } else {
            viewHolder.tvStatus.setText("Trạng thái: Chưa xử lý");
            viewHolder.layoutDoAction.setVisibility(View.VISIBLE);
        }
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanItems.remove(position);
                notifyItemRemoved(position);
            }
        });

        viewHolder.btnSendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onScanItemAction != null) {
                    onScanItemAction.onSendSMS(position);
                }
            }
        });

        viewHolder.btnSendInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onScanItemAction != null) {
                    onScanItemAction.onSendInternet(position);
                }
            }
        });
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
