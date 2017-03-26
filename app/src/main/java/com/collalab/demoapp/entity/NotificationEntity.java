package com.collalab.demoapp.entity;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by laptop88 on 3/25/2017.
 */

public class NotificationEntity extends RealmObject implements Serializable {
    private String title;
    private String content;
    private Date created_at;
    private Boolean has_opened;

    public NotificationEntity() {

    }

    public NotificationEntity(String title, String content, Date created_at, Boolean has_opened) {
        this.title = title;
        this.content = content;
        this.created_at = created_at;
        this.has_opened = has_opened;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getHas_opened() {
        return has_opened;
    }

    public void setHas_opened(Boolean has_opened) {
        this.has_opened = has_opened;
    }
}
