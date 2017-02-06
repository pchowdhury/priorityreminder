package com.phoenix2k.priorityreminder;

/**
 * Created by Pushpan on 08/01/17.
 */

public class DataStore {
    public static final String APP_FOLDER_NAME = "Priority Reminder";
    public static final String APP_DATA_FILE_NAME = "AppData";
    private final String TAG = "DataStore";
    private static DataStore mInstance;

    public static DataStore getInstance(){
        if(mInstance==null){
            mInstance= new DataStore();
        }
        return mInstance;
    }

}
