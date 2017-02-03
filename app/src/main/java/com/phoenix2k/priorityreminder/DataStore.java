package com.phoenix2k.priorityreminder;

/**
 * Created by Pushpan on 08/01/17.
 */

public class DataStore {
    private final String TAG = "SyncStore";
    private static DataStore mInstance;

    public static DataStore getInstance(){
        if(mInstance==null){
            mInstance= new DataStore();
        }
        return mInstance;
    }

}
