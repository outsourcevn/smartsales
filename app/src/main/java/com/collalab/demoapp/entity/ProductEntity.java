package com.collalab.demoapp.entity;

import java.util.Date;

import io.realm.RealmObject;

public class ProductEntity extends RealmObject {
    private String product_code;
    private String product_name;
    private Date import_date;
    private Integer sales_number;
    private Integer remain_product_number;
    private Date modifided_date;
    private Boolean has_sold_out;

    public ProductEntity() {

    }

    public ProductEntity(String product_code, String product_name, Date import_date, Integer sales_number, Integer remain_product_number, Date modifided_date, Boolean has_sold_out) {
        this.product_code = product_code;
        this.product_name = product_name;
        this.import_date = import_date;
        this.sales_number = sales_number;
        this.remain_product_number = remain_product_number;
        this.modifided_date = modifided_date;
        this.has_sold_out = has_sold_out;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public Date getImport_date() {
        return import_date;
    }

    public void setImport_date(Date import_date) {
        this.import_date = import_date;
    }

    public Integer getSales_number() {
        return sales_number;
    }

    public void setSales_number(Integer sales_number) {
        this.sales_number = sales_number;
    }

    public Integer getRemain_product_number() {
        return remain_product_number;
    }

    public void setRemain_product_number(Integer remain_product_number) {
        this.remain_product_number = remain_product_number;
    }

    public Date getModifided_date() {
        return modifided_date;
    }

    public void setModifided_date(Date modifided_date) {
        this.modifided_date = modifided_date;
    }

    public Boolean getHas_sold_out() {
        return has_sold_out;
    }

    public void setHas_sold_out(Boolean has_sold_out) {
        this.has_sold_out = has_sold_out;
    }
}
