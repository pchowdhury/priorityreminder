package com.phoenix2k.priorityreminder.task;

import android.content.Context;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.model.Project;
import com.phoenix2k.priorityreminder.model.TaskItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pushpan on 06/02/17.
 */

public class LoadAllTasks extends SpreadsheetTask {
    public LoadAllTasks(Context context, GoogleAccountCredential credential, TaskListener listener) {
        super(context, credential, listener);
    }

    @Override
    public APIType getAPITypeForTask() {
        return APIType.Sheet_Load_All_Tasks;
    }

    @Override
    public String getProgressMessage() {
        return getContext().getString(R.string.progress_loading_projects);
    }

    @Override
    public Object getDataFromApi() {
        ArrayList<TaskItem> results = null;
        String range = "A1:K";
        List<List<Object>> values = readSheet(range, getDataSpreadsheetId());
        if (values != null) {
            results = new ArrayList<>();
            for (List row : values) {
                TaskItem taskItem = TaskItem.getTaskItemFrom(row);
                results.add(taskItem);
            }
        }
        return results;
    }
}