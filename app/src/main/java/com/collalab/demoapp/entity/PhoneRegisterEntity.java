package com.collalab.demoapp.entity;

import io.realm.RealmObject;

/**
 * Created by laptop88 on 3/24/2017.
 */

public class PhoneRegisterEntity extends RealmObject {
    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
