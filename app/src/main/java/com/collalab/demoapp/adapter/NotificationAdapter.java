package com.collalab.demoapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.collalab.demoapp.Common;
import com.collalab.demoapp.R;
import com.collalab.demoapp.entity.NotificationEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<NotificationEntity> notificationList;
    boolean isMainUser = false;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    OnItemClick onItemClick;

    public interface OnItemClick {
        void onItemClick(int position);
    }

    public NotificationAdapter(Context context, ArrayList<NotificationEntity> notificationEntities) {
        this.context = context;
        this.notificationList = notificationEntities;
        this.isMainUser = isMainUser;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_date_created)
        TextView tvDateCreated;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notification_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final NotificationEntity notificationEntity = notificationList.get(position);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!notificationEntity.getHas_opened()) {
                    viewHolder.itemView.setBackgroundColor(Color.WHITE);
                }
                if (onItemClick != null) {
                    onItemClick.onItemClick(position);
                }
            }
        });
        if (!notificationEntity.getHas_opened()) {
            viewHolder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorUnreadNotification));
        } else {
            viewHolder.itemView.setBackgroundColor(Color.WHITE);
        }
        viewHolder.tvTitle.setText(notificationEntity.getTitle());
        viewHolder.tvContent.setText(notificationEntity.getContent());
        viewHolder.tvDateCreated.setText(Common.getDateInString(notificationEntity.getCreated_at()));
    }

    @Override
    public int getItemCount() {
        return notificationList != null ? notificationList.size() : 0;
    }
}
