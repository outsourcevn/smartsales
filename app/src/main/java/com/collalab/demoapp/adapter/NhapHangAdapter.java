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

public class NhapHangAdapter extends AdapterFooterView {

    Context context;
    ArrayList<ImportProductEntity> productEntities;

    public NhapHangAdapter(Context context, ArrayList<ImportProductEntity> productEntities) {
        super(context);
        this.context = context;
        this.productEntities = productEntities;
    }

    OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick{
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
                    if(onItemClick != null) {
                        onItemClick.onItemClick(position);
                    }
                }
            });
            viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    productEntities.remove(position);
                    notifyItemRangeRemoved(position,1);
                }
            });
            viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClick != null) {
                        onItemClick.onEditClick(position);
                    }
                }
            });
//        if (!TextUtils.isEmpty(productEntity.getProduct_name())) {
//            viewHolder.tvProductName.setText(productEntity.getProduct_name());
//        } else {
//            viewHolder.tvProductName.setText("Chưa cập nhật tên sản phẩm");
//        }
//        if (productEntity.getImport_date() != null) {
//            viewHolder.tvImportedDate.setText(Common.getDateInString(productEntity.getImport_date()));
//        }
//        if (!TextUtils.isEmpty(productEntity.getProduct_code())) {
//            viewHolder.tvProductCode.setText("Mã sản phẩm: ");
//        }
            viewHolder.tvImportedDate.reset();
            viewHolder.tvImportedDate.addPiece(new RichTextView.SpanStyle.Builder("Ngày:  ").build());
            viewHolder.tvImportedDate.addPiece(new RichTextView.SpanStyle.Builder(Common.getDateInString(productEntity.getCreated_at()))
                    .textColor(context.getResources().getColor(R.color.colorTextHightLight))
                    .textSizeRelative(1.2f)
                    .style(new StyleSpan(Typeface.BOLD).getStyle()).build());
            viewHolder.tvImportedDate.display();

            viewHolder.tvProductCode.reset();
            viewHolder.tvProductCode.addPiece(new RichTextView.SpanStyle.Builder("Mã nhận hàng:  ").build());
            viewHolder.tvProductCode.addPiece(new RichTextView.SpanStyle.Builder(productEntity.getProduct_code()).textColor(context.getResources().getColor(R.color.colorTextHightLight))
                    .textSizeRelative(1.2f)
                    .style(new StyleSpan(Typeface.BOLD).getStyle())
                    .build());
            viewHolder.tvProductCode.display();
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
//        return productEntities != null ? productEntities.size() : 0;
        return productEntities.size();
    }
}
