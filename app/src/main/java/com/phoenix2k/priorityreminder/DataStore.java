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
    private Project mNewProject;
    private Project mCurrentProject;

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

    public void switchNewProjectToState(Context context, boolean checked) {
        if (mNewProject != null) {
            mNewProject.mProjectType = checked ? Project.ProjectType.State : Project.ProjectType.Simple;
            int[] resId = {R.string.lbl_title_quadrant1, R.string.lbl_title_quadrant2, R.string.lbl_title_quadrant3, R.string.lbl_title_quadrant4};
            int[] resIdState = {R.string.lbl_title_state_quadrant1, R.string.lbl_title_state_quadrant2, R.string.lbl_title_state_quadrant3, R.string.lbl_title_state_quadrant4};
            if (checked) {
                for (TaskItem.QuadrantType type : TaskItem.QuadrantType.values()) {
                    mNewProject.mTitleQuadrants.put(type, context.getString(checked ? resIdState[type.ordinal()] : resId[type.ordinal()]));
                }
            }
        }
    }

    public void updateQuadrantTitle(Project newProject, TaskItem.QuadrantType type, String value) {
        if (newProject != null) {
            newProject.mTitleQuadrants.put(type, value);
        }
    }

    public void updateTitle(Project newProject, String value) {
        if (newProject != null) {
            newProject.mTitle = value;
        }
    }
}
