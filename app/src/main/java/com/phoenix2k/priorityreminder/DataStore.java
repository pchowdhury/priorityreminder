package com.phoenix2k.priorityreminder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pushpan on 08/01/17.
 */

public class DataStore {
    public static final String APP_FOLDER_NAME = "Priority Reminder";
    public static final String APP_DATA_FILE_NAME = "AppData";
    private final String TAG = "DataStore";
    private static DataStore mInstance;
    private ArrayList<String> mProjects = new ArrayList<>() ;

    public static DataStore getInstance(){
        if(mInstance==null){
            mInstance= new DataStore();
        }
        return mInstance;
    }

    public void setProjects(ArrayList<String> projects) {
        this.mProjects = projects;
    }

    public ArrayList<String> getProjects(){
        return mProjects;
    }
}
