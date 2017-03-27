package com.collalab.demoapp.entity;

/**
 * Created by laptop88 on 3/27/2017.
 */

public class ScanItem {
    private String code;
    private boolean success;
    private boolean isImportProcess;
    private String processType;

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
