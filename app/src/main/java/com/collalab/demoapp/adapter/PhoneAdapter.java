package com.collalab.demoapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.collalab.demoapp.R;
import com.collalab.demoapp.entity.EventScan;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by laptop88 on 3/26/2017.
 */

public class PhoneAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<String> listPhone;
    boolean isMainUser = false;

    public PhoneAdapter(Context context, ArrayList<String> listPhone, boolean isMainUser) {
        this.context = context;
        this.listPhone = listPhone;
        this.isMainUser = true; //TODO cái này để force show delete phone, nếu chạy product thì phải truyền vào biến xác định
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_phone_number)
        TextView tvPhoneNum;
        @BindView(R.id.btn_delete)
        View btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_phone_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (listPhone.size() > position) {
            viewHolder.tvPhoneNum.setText(listPhone.get(position));
        }

        if (isMainUser) {
            viewHolder.btnDelete.setVisibility(View.VISIBLE);
        } else {
            viewHolder.btnDelete.setVisibility(View.GONE);
        }

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setTitle("Thông báo");
                alertDialogBuilder
                        .setMessage("Bạn có thực sự muốn xóa số điện thoại này khỏi danh sách không?")
                        .setCancelable(false)
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (listPhone != null && listPhone.size() > position)
                                    listPhone.remove(position);
                                notifyItemRangeRemoved(position, 1);
                            }
                        })
                        .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        viewHolder.tvPhoneNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String tel = "tel:" + listPhone.get(position);
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
                callIntent.setData(Uri.parse(tel));
                context.startActivity(callIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPhone != null ? listPhone.size() : 0;
    }
}
