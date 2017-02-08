package com.phoenix2k.priorityreminder;

import com.phoenix2k.priorityreminder.model.Project;

import java.util.ArrayList;

/**
 * Created by Pushpan on 08/01/17.
 */

public class DataStore {
    private final String TAG = "DataStore";
    public static final String APP_FOLDER_NAME = "Priority Reminder";
    public static final String PROJECT_FILE_NAME = "Project";
    public static final String DATA_FILE_NAME = "Data";
    private static DataStore mInstance;
    private ArrayList<Project> mProjects = new ArrayList<>();
    private Project mNewProject;

    public static DataStore getInstance() {
        if (mInstance == null) {
            mInstance = new DataStore();
        }
        return mInstance;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.mProjects = projects;
    }

    public ArrayList<Project> getProjects() {
        return mProjects;
    }

    public void setNewProject(Project project) {
        mNewProject = project;
        if (mNewProject != null) {
            mNewProject.mIndex = getLastProjectIndex() + 1;
        }
    }

    public Project getNewProject() {
        return mNewProject;
    }

    public void confirmSaveNewProject() {
        getProjects().add(getNewProject());
        setNewProject(null);
    }

    public int getLastProjectPosition() {
        return mProjects.size();
    }

    public int getLastProjectIndex() {
        if (mProjects.size() > 0) {
            return mProjects.get(mProjects.size() - 1).mIndex;
        } else {
            return -1;
        }
    }
}
