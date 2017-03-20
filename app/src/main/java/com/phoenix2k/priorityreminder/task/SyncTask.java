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

    public SyncTask(Context context, GoogleAccountCredential credential, TaskListener listener) {
        super(context, credential, listener);
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
        for (Object obj : DataStore.getInstance().getUpdates()) {
            if (obj instanceof Project) {
                final Project project = (Project) obj;
                int len = Project.Column.values().length;
                char ch = (char) (64 + len);
                String range = "A" + project.mPosition + ":" + ch;
                if (project.mId == null) {
                    projectsToBeCleared.add(project);
                    projectClearRanges.add(range);
                } else {
                    projectsToBeUpdated.add(project);
                    projectRanges.add(range);
                    List<List<Object>> itemData = Project.getProjectWriteback(project);
                    projectData.add(itemData);
                }
            } else {
                final TaskItem taskItem = (TaskItem) obj;
                int len = TaskItem.Column.values().length;
                char ch = (char) (64 + len);
                String range = "A" + taskItem.mPosition + ":" + ch;
                if (taskItem.mId == null) {
                    tasksToBeCleared.add(taskItem);
                    taskClearRanges.add(range);
                } else {
                    tasksToBeUpdated.add(taskItem);
                    taskRanges.add(range);
                    List<List<Object>> itemData = TaskItem.getTaskItemWriteback(taskItem);
                    taskData.add(itemData);
                }
            }
        }

        Boolean isUpdatedProject = true;
        Boolean isUpdatedTasks = true;
        Boolean isClearProject = true;
        Boolean isClearTaskItem = true;
        if (projectRanges.size() > 0) {
            isUpdatedProject = updateMultipleItemsSheet(projectRanges, projectData, getProjectSpreadsheetId());
            if (isUpdatedProject) {
                DataStore.getInstance().clearUpdatedItems(projectsToBeUpdated);
            }
        }
        if (projectClearRanges.size() > 0) {
            isClearProject = clearMultipleItemsSheet(projectClearRanges, getProjectSpreadsheetId());
            if (isClearProject) {
                DataStore.getInstance().clearUpdatedItems(projectsToBeUpdated);
            }
        }

        if (taskRanges.size() > 0) {
            isUpdatedTasks = updateMultipleItemsSheet(taskRanges, taskData, getDataSpreadsheetId());
            if (isUpdatedTasks) {
                DataStore.getInstance().clearUpdatedItems(tasksToBeUpdated);
            }
        }
        if (taskClearRanges.size() > 0) {
            isClearTaskItem = clearMultipleItemsSheet(taskClearRanges, getDataSpreadsheetId());
            if (isClearTaskItem) {
                DataStore.getInstance().clearUpdatedItems(tasksToBeCleared);
            }
        }

        if (!isUpdatedTasks || !isUpdatedTasks || !isClearProject || !isClearTaskItem) {
            onDisplayInfo("Could not push updates");
        }
        return (isUpdatedTasks && isUpdatedTasks && isClearProject && isClearTaskItem);
    }

}
