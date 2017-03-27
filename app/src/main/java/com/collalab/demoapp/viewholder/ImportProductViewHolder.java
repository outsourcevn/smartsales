package com.collalab.demoapp.viewholder;


import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.collalab.demoapp.Common;
import com.collalab.demoapp.R;
import com.collalab.demoapp.entity.ImportProductEntity;
import com.collalab.demoapp.widget.RichTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImportProductViewHolder extends ChildViewHolder {

    @BindView(R.id.tv_import_date)
    RichTextView tvImportedDate;
    @BindView(R.id.tv_product_id)
    RichTextView tvProductCode;
    @BindView(R.id.btn_edit)
    View btnEdit;
    @BindView(R.id.btn_delete)
    View btnDelete;

    public ImportProductViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(@NonNull ImportProductEntity productEntity) {
        tvImportedDate.setText("Ngày:  " + Common.getDateInString(productEntity.getCreated_at()));
        tvProductCode.setText("Mã nhận hàng:  " + productEntity.getProduct_code());
    }
}
