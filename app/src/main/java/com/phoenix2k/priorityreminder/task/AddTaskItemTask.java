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

public class AddTaskItemTask extends SpreadsheetTask {
    public AddTaskItemTask(Context context, GoogleAccountCredential credential, TaskListener listener) {
        super(context, credential, listener);
    }

    @Override
    public ServiceType getServiceType() {
        return ServiceType.Spreadsheet;
    }

    @Override
    public APIType getAPITypeForTask() {
        return APIType.Sheet_Add_Task;
    }

    @Override
    public String getProgressMessage() {
        return getContext().getString(R.string.progress_adding_task);
    }

    @Override
    public Object getDataFromApi() {
        TaskItem taskItem = DataStore.getInstance().getCurrentTaskItem();
        String range = "A" + taskItem.mPosition  + ":K";
        Boolean isUpdated = updateSheet(range, getData(), getDataSpreadsheetId());
        if (!isUpdated) {
            onDisplayInfo("Could not add task");
        }
        return isUpdated;
    }

    public List<List<Object>> getData() {
        final TaskItem taskItem = DataStore.getInstance().getCurrentTaskItem();
        List<List<Object>> values = new ArrayList<>();
        ArrayList<Object> projectValues = new ArrayList() {{
            add(taskItem.mId + "");
            add(taskItem.mPosition + "");
            add(taskItem.mProjectId + "");
            add(taskItem.mTitle + "");
            add(taskItem.mIndex + "");
            add(taskItem.mQuadrantType.ordinal()+"");
            add(taskItem.mDescription+"");
            add(taskItem.mStatus.ordinal()+"");
            add(taskItem.mRepeatType.ordinal()+"");
            add(taskItem.mCreatedOn + "");
            add(taskItem.mUpdatedOn + "");
        }};
        values.add(projectValues);
        return values;
    }

}
