package com.collalab.demoapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.collalab.demoapp.R;

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
        this.isMainUser = isMainUser;
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
                if (listPhone != null && listPhone.size() > position)
                    listPhone.remove(position);
                notifyItemRangeRemoved(position, 1);
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
