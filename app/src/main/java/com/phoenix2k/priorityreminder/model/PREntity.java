package com.phoenix2k.priorityreminder.model;

import com.phoenix2k.priorityreminder.utils.IDGenerator;

/**
 * Created by Pushpan on 01/04/17.
 */

public class PREntity {
    public String mId;
    public String mTitle;
    public int mIndex;
    public long mCreatedOn;
    public long mUpdatedOn;
    public boolean mTrashed;

    public PREntity() {
        mId = IDGenerator.generateUniqueId();
        mTitle = "";
        mIndex = 0;
        mCreatedOn = IDGenerator.getCurrentTimeStamp();
        mUpdatedOn = -1;
        mTrashed = false;
    }

    public void copyTo(PREntity entity) {
        entity.mId = mId;
        entity.mTitle = mTitle;
        entity.mIndex = mIndex;
        entity.mCreatedOn = mCreatedOn;
        entity.mCreatedOn = mUpdatedOn;
        entity.mTrashed = mTrashed;
    }


}
