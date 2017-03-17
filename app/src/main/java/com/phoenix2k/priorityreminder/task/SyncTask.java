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
        List<List<List<Object>>> projectData = new ArrayList<>();
        List<List<List<Object>>> taskData = new ArrayList<>();

        for(Object obj : DataStore.getInstance().getUpdates()){
            if(obj instanceof TaskItem){
                final TaskItem taskItem = (TaskItem) obj;
                int len = TaskItem.Column.values().length;
                char ch = (char) (64 + len);
                String range = "A" + taskItem.mPosition + ":" + ch;
                taskRanges.add(range);
                List<List<Object>> itemData =  TaskItem.getTaskItemWriteback(taskItem);
                taskData.add(itemData);
            }else{
                final Project project = (Project) obj;
                int len = Project.Column.values().length;
                char ch = (char) (64 + len);
                String range = "A" + project.mPosition + ":" + ch;
                projectRanges.add(range);
                List<List<Object>> itemData =  Project.getProjectWriteback(project);
                projectData.add(itemData);
            }
        }

        Boolean isUpdatedProject = true;
        Boolean isUpdatedTasks = true;
        if(projectRanges.size()>0) {
             isUpdatedProject = updateMultipleItemsSheet(projectRanges, projectData, getProjectSpreadsheetId());
            if(isUpdatedProject){
                DataStore.getInstance().clearUpdatedProjects();
            }
        }
        if(taskRanges.size()>0) {
            isUpdatedTasks = updateMultipleItemsSheet(taskRanges, taskData, getDataSpreadsheetId());
            if(isUpdatedTasks){
                DataStore.getInstance().clearUpdatedTaskItems();
            }
        }

        if (!isUpdatedTasks || !isUpdatedTasks) {
            onDisplayInfo("Could not push updates");
        }
        return (isUpdatedTasks && isUpdatedTasks);
    }

}
