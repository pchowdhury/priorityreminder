package com.phoenix2k.priorityreminder.task;

import android.content.Context;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.model.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pushpan on 06/02/17.
 */

public class LoadProjectsTask extends SpreadsheetTask {
    public LoadProjectsTask(Context context, GoogleAccountCredential credential, TaskListener listener) {
        super(context, credential, listener);
    }

    @Override
    public APIType getAPITypeForTask() {
        return APIType.Sheet_Load_Projects_Metadata;
    }

    @Override
    public String getProgressMessage() {
        return getContext().getString(R.string.progress_loading_projects);
    }

    @Override
    public Object getDataFromApi() {
        ArrayList<Project> results = null;
        String range = "Project!A2:J";
        List<List<Object>> values = readSheet(range);
        if (values != null) {
            results = new ArrayList<>();
            for (List row : values) {
                Project project = Project.getProjectFrom(getContext(), row);
                results.add(project);
            }
        }
        return results;
    }
}