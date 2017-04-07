package com.phoenix2k.priorityreminder.task;

import android.content.Context;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.phoenix2k.priorityreminder.DataStore;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.model.Project;
import com.phoenix2k.priorityreminder.model.TaskItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pushpan on 06/02/17.
 */

public class SyncTask extends SpreadsheetTask {
    ArrayList<Object> mItems;

    public SyncTask(Context context, GoogleAccountCredential credential, TaskListener listener) {
        super(context, credential, listener);
    }

    public SyncTask updateItems(ArrayList<Object> items) {
        mItems = items;
        return this;
    }

    @Override
    public ServiceType getServiceType() {
        return ServiceType.Spreadsheet;
    }

    @Override
    public APIType getAPITypeForTask() {
        return APIType.Sheet_Push_Updates;
    }

    @Override
    public String getProgressMessage() {
        return getContext().getString(R.string.progress_updating);
    }

    @Override
    public Object getDataFromApi() {
        ArrayList<String> projectRanges = new ArrayList<>();
        ArrayList<String> taskRanges = new ArrayList<>();
        ArrayList<String> projectClearRanges = new ArrayList<>();
        ArrayList<String> taskClearRanges = new ArrayList<>();
        List<List<List<Object>>> projectData = new ArrayList<>();
        List<List<List<Object>>> taskData = new ArrayList<>();
        ArrayList<Project> projectsToBeUpdated = new ArrayList<>();
        ArrayList<TaskItem> tasksToBeUpdated = new ArrayList<>();
        ArrayList<Project> projectsToBeCleared = new ArrayList<>();
        ArrayList<TaskItem> tasksToBeCleared = new ArrayList<>();
        if (mItems != null) {
            int projectCount = 0;
            int taskItemCount = 0;
            int projectClearCount = 0;
            int taskItemClearCount = 0;
            for (Object obj : mItems) {
                if (obj instanceof Project) {
                    final Project project = (Project) obj;
                    int len = Project.Column.values().length;
                    char ch = (char) (64 + len);
//                String range = "A" + project.mPosition + ":" + ch;
                    String range;
                    if (project.mId == null) {
                        projectClearCount++;
                        range = "A" + projectClearCount + ":" + ch;
                        projectsToBeCleared.add(project);
                        projectClearRanges.add(range);
                    } else {
                        projectCount++;
                        range = "A" + projectCount + ":" + ch;
                        projectsToBeUpdated.add(project);
                        projectRanges.add(range);
                        List<List<Object>> itemData = Project.getProjectWriteback(project);
                        projectData.add(itemData);
                    }
                } else {
                    final TaskItem taskItem = (TaskItem) obj;
                    int len = TaskItem.Column.values().length;
                    char ch = (char) (64 + len);
//                String range = "A" + taskItem.mPosition + ":" + ch;
                    String range;
                    if (taskItem.mId == null) {
                       taskItemClearCount++;
                        range = "A" + taskItemClearCount + ":" + ch;
                        tasksToBeCleared.add(taskItem);
                        taskClearRanges.add(range);
                    } else {
                        taskItemCount++;
                        range = "A" + taskItemCount + ":" + ch;
                        tasksToBeUpdated.add(taskItem);
                        taskRanges.add(range);
                        List<List<Object>> itemData = TaskItem.getTaskItemWriteback(taskItem);
                        taskData.add(itemData);
                    }
                }
            }
        }

        Boolean isUpdatedProject = true;
        Boolean isUpdatedTasks = true;
        Boolean isClearProject = true;
        Boolean isClearTaskItem = true;

        //Do the clear operatipn 1st
        if (projectClearRanges.size() > 0) {
            isClearProject = clearMultipleItemsSheet(projectClearRanges, getProjectSpreadsheetId());
            if (isClearProject) {
                DataStore.getInstance().clearUpdatedItems(projectsToBeUpdated);
            }
        }

        if (taskClearRanges.size() > 0) {
            isClearTaskItem = clearMultipleItemsSheet(taskClearRanges, getDataSpreadsheetId());
            if (isClearTaskItem) {
                DataStore.getInstance().clearUpdatedItems(tasksToBeCleared);
            }
        }

        if (projectRanges.size() > 0) {
            isUpdatedProject = updateMultipleItemsSheet(projectRanges, projectData, getProjectSpreadsheetId());
            if (isUpdatedProject) {
                DataStore.getInstance().clearUpdatedItems(projectsToBeUpdated);
            }
        }
        if (taskRanges.size() > 0) {
            isUpdatedTasks = updateMultipleItemsSheet(taskRanges, taskData, getDataSpreadsheetId());
            if (isUpdatedTasks) {
                DataStore.getInstance().clearUpdatedItems(tasksToBeUpdated);
            }
        }

        if (!isUpdatedTasks || !isUpdatedTasks || !isClearProject || !isClearTaskItem) {
            onDisplayInfo("Could not push updates");
        }
        return (isUpdatedTasks && isUpdatedTasks && isClearProject && isClearTaskItem);
    }

}
