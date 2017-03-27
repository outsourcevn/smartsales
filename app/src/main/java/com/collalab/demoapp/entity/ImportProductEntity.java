package com.collalab.demoapp.entity;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by VietMac on 2017-03-24.
 */

public class ImportProductEntity extends RealmObject implements Serializable {
    private String product_code;
    private Date created_at;
    private Integer number_product_import;

    public ImportProductEntity() {

    }

    public ImportProductEntity(String product_code, Date created_at, Integer number_product_import) {
        this.product_code = product_code;
        this.created_at = created_at;
        this.number_product_import = number_product_import;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Integer getNumber_product_import() {
        return number_product_import;
    }

    public void setNumber_product_import(Integer number_product_import) {
        this.number_product_import = number_product_import;
    }
}
