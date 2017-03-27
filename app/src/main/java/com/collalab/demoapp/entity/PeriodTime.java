package com.collalab.demoapp.entity;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.List;

/**
 * Created by laptop88 on 3/27/2017.
 */

public class PeriodTime implements Parent<ImportProductEntity> {

    private String mName;
    private List<ImportProductEntity> mImportProductEntities;

    public PeriodTime(String name, List<ImportProductEntity> items) {
        mName = name;
        mImportProductEntities = items;
    }

    public String getName() {
        return mName;
    }

    @Override
    public List<ImportProductEntity> getChildList() {
        return mImportProductEntities;
    }

    public ImportProductEntity getIMImportProductEntity(int position) {
        return mImportProductEntities.get(position);
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
