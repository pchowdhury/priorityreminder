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
        ArrayList<String> ranges = new ArrayList<>();
        List<List<List<Object>>> data = new ArrayList<>();

        for(Object obj : DataStore.getInstance().getUpdates()){
            if(obj instanceof TaskItem){
                final TaskItem taskItem = (TaskItem) obj;
                String range = "A" + taskItem.mPosition + ":K";
                ranges.add(range);
                List<List<Object>> itemData =  TaskItem.getTaskItemWriteback(taskItem);
                data.add(itemData);
            }else{
                final Project project = (Project) obj;
                String range = "A" + project.mPosition + ":P";
                ranges.add(range);
                List<List<Object>> itemData =  Project.getProjectWriteback(project);
                data.add(itemData);
            }
        }
        Boolean isUpdated = updateMultipleItemsSheet(ranges, data, getDataSpreadsheetId());
        if(isUpdated){
            DataStore.getInstance().clearUpdates();
        }
        if (!isUpdated) {
            onDisplayInfo("Could not push updates");
        }
        return isUpdated;
    }

}
