package com.collalab.demoapp.entity;

import java.util.Date;

/**
 * Created by laptop88 on 3/26/2017.
 */

public class ExportProductEntity {
    public Date created_at;
    public int number_product_sold;
    public String code;
    public boolean isRetail = false;

    public ExportProductEntity(Date created_at, int number_product_sold, String code, boolean isRetail) {
        this.created_at = created_at;
        this.number_product_sold = number_product_sold;
        this.code = code;
        this.isRetail = isRetail;
    }
}
