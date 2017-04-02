package com.collalab.demoapp.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by laptop88 on 3/27/2017.
 */

public class ScanItem implements Serializable {
    private String code;
    private boolean success;
    private boolean isImportProcess;
    private String processType;
    private Date created_at;
    private double lat, lng;
    private String address;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isImportProcess() {
        return isImportProcess;
    }

    public void setImportProcess(boolean importProcess) {
        isImportProcess = importProcess;
    }
}
