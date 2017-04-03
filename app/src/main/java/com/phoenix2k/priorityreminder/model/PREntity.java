package com.phoenix2k.priorityreminder.model;

/**
 * Created by Pushpan on 01/04/17.
 */

public class PREntity {
    public Long mId;
    public String mTitle;
    public int mIndex;
    public long mCreatedOn;
    public long mUpdatedOn;
    public boolean mMarkDeleted;

    public PREntity() {
        mId = Long.valueOf(-1);
        mTitle = "";
        mIndex = 0;
        mCreatedOn = System.currentTimeMillis();
        mUpdatedOn = mCreatedOn;
    }

    public void copyTo(PREntity entity) {
        entity.mId = mId;
        entity.mTitle = mTitle;
        entity.mIndex = mIndex;
        entity.mCreatedOn = mCreatedOn;
        entity.mCreatedOn = mUpdatedOn;
    }


}
