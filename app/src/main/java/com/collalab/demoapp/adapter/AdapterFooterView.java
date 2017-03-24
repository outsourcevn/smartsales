package com.collalab.demoapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.collalab.demoapp.R;

/**
 * Created by VietMac on 2017-03-24.
 */

public abstract class AdapterFooterView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected static final int TYPE_ITEM = 1;
    protected static final int TYPE_FOOTER = 2;
    protected Context context;
    protected boolean shouldHideFooter;
    protected boolean notShowFooterSpace;
    protected boolean shouldHideHeader;
    protected String noResultText = "";

    public AdapterFooterView(Context context) {
        this.context = context;
    }

    public class RecyclerFooterViewHolder extends RecyclerView.ViewHolder {
        public TextView tvFooterContent;
        public View pbLoading;
        public View footerView;

        public RecyclerFooterViewHolder(View itemView) {
            super(itemView);
            tvFooterContent = (TextView) itemView.findViewById(R.id.tv_no_result);
            pbLoading = itemView.findViewById(R.id.pb_footer_progress);
            footerView = itemView.findViewById(R.id.footer_view);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        if (viewType == TYPE_ITEM) {
            return inflateView(parent);
        } else {
            final View view = LayoutInflater.from(context).inflate(R.layout.footer_loading, parent, false);
            return new RecyclerFooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderRecycle, int position) {
        if (!isPositionFooter(position)) {
            onBindViewWithData(holderRecycle, position);
        } else if (isPositionFooter(position)) {
            if (shouldHideFooter) {
                RecyclerFooterViewHolder recyclerFooterViewHolder = (RecyclerFooterViewHolder) holderRecycle;
                recyclerFooterViewHolder.tvFooterContent.setText(noResultText);
                recyclerFooterViewHolder.tvFooterContent.setVisibility(View.VISIBLE);
                recyclerFooterViewHolder.pbLoading.setVisibility(View.GONE);
            } else if (isPositionFooter(position)) {
                try {
                    RecyclerFooterViewHolder recyclerFooterViewHolder = (RecyclerFooterViewHolder) holderRecycle;
                    recyclerFooterViewHolder.tvFooterContent.setVisibility(View.GONE);
                    recyclerFooterViewHolder.pbLoading.setVisibility(View.VISIBLE);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
            if (notShowFooterSpace) {
                RecyclerFooterViewHolder recyclerFooterViewHolder = (RecyclerFooterViewHolder) holderRecycle;
                recyclerFooterViewHolder.footerView.setVisibility(View.GONE);
            }
        }
    }

    public abstract void onBindViewWithData(RecyclerView.ViewHolder holderRecycle, int position);

    public abstract RecyclerView.ViewHolder inflateView(ViewGroup parent);

    public abstract int getDataLength();

    @Override
    public int getItemCount() {
        return getDataLength() + 1;
    }

    protected boolean isPositionFooter(int position) {
        return position == getDataLength();
    }

    public int getItemViewType(int position) {
        if (isPositionFooter(position)) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    public void hideFooter() {
        shouldHideFooter = true;
    }

    public void hideFooterImmediately() {
        shouldHideFooter = true;
        notifyItemChanged(getDataLength());
    }

    public void hideFooterSpace() {
        notShowFooterSpace = true;
        notifyItemChanged(getDataLength());
    }

    public void hideFooterWithText(String noResultText) {
        this.noResultText = noResultText;
        hideFooterImmediately();
    }

    public void showFooter() {
        shouldHideFooter = false;
    }
}
