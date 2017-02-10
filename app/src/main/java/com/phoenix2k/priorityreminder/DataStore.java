package com.phoenix2k.priorityreminder;

import android.content.Context;

import com.phoenix2k.priorityreminder.model.Project;
import com.phoenix2k.priorityreminder.model.TaskItem;

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
    private ArrayList<TaskItem> mTasks = new ArrayList<>();
    private Project mNewProject;
    private Project mCurrentProject;
    private TaskItem mCurrentTaskItem;

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
            mNewProject.mPosition = getLastProjectPosition() + 1 + "";
        }
    }

    public Project getNewProject() {
        return mNewProject;
    }

    public void confirmSaveNewProject() {
        getProjects().add(getNewProject());
        setCurrentProject(getNewProject());
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

    public void setCurrentProject(Project project) {
        this.mCurrentProject = project;
    }

    public Project getCurrentProject() {
        return mCurrentProject;
    }

    public void applyChanges() {

    }

    public TaskItem getCurrentTaskItem() {
        return mCurrentTaskItem;
    }

    public void setCurrentTaskItem(TaskItem mCurrentTaskItem) {
        this.mCurrentTaskItem = mCurrentTaskItem;
    }

    public ArrayList<TaskItem> getTasks() {
        return mTasks;
    }

    public void setTasks(ArrayList<TaskItem> mTasks) {
        this.mTasks = mTasks;
        ArrayList<TaskItem> values = new ArrayList<>(mTasks);
        for (Project project : getProjects()) {
            project.addfromTaskList(values);
        }
    }

    public int getLastTaskPosition() {
        return getTasks().size();
    }

    public void confirmSaveTaskItem() {
        ArrayList<TaskItem> list = mCurrentProject.getTaskListForQuadrant(mCurrentTaskItem.mQuadrantType);
        list.add(mCurrentTaskItem);
        setCurrentTaskItem(null);
    }
}
