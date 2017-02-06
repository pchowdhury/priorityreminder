package com.phoenix2k.priorityreminder;

import com.phoenix2k.priorityreminder.model.Project;

import java.util.ArrayList;

/**
 * Created by Pushpan on 08/01/17.
 */

public class DataStore {
    private final String TAG = "DataStore";
    public static final String APP_FOLDER_NAME = "Priority Reminder";
    public static final String APP_DATA_FILE_NAME = "AppData";
    private static DataStore mInstance;
    private ArrayList<Project> mProjects = new ArrayList<>() ;

    public static DataStore getInstance(){
        if(mInstance==null){
            mInstance= new DataStore();
        }
        return mInstance;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.mProjects = projects;
    }

    public ArrayList<Project> getProjects(){
        return mProjects;
    }
}
