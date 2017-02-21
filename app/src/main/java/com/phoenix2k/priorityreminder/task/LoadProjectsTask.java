package com.phoenix2k.priorityreminder.task;

import android.content.Context;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.phoenix2k.priorityreminder.DataStore;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.model.Project;
import com.phoenix2k.priorityreminder.utils.LogUtils;
import com.phoenix2k.priorityreminder.utils.StaticDataProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        //load from cache if required
        if (StaticDataProvider.getLiveInstance().isUsingEnableStaticEngine() && !StaticDataProvider.getLiveInstance().shouldGenerateCacheForDebug()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                try {
                    JSONObject json = StaticDataProvider.getLiveInstance().getJSONFor(DataStore.PROJECT_FILE_NAME);
                    JSONArray jsonArr = null;
                    if (json.has(DataStore.PROJECT_FILE_NAME)) {
                        jsonArr = json.getJSONArray(DataStore.PROJECT_FILE_NAME);
                    }
                    results = new ArrayList<>();
                    for (int i = 0; i < jsonArr.length(); i++) {
                        results.add(Project.getProjectFromJSON(jsonArr.getJSONObject(i)));
                    }
                } catch (JSONException e) {
                    LogUtils.printException(e);
                }
            }
        } else {
            String range = "A1:P";
            List<List<Object>> values = readSheet(range, getProjectSpreadsheetId());
            if (values != null) {
                results = new ArrayList<>();
                for (List row : values) {
                    Project project = Project.getProjectFrom(getContext(), row);
                    results.add(project);
                }
                //create cache if required
                if (StaticDataProvider.getLiveInstance().shouldGenerateCacheForDebug()) {
                    JSONArray jsonArr = new JSONArray();
                    for (Project project : results) {
                        jsonArr.put(project.toJSON());
                    }
                    JSONObject json = new JSONObject();
                    try {
                        json.put(DataStore.PROJECT_FILE_NAME, jsonArr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    StaticDataProvider.getLiveInstance().generateMockup(DataStore.PROJECT_FILE_NAME, json.toString().getBytes());
                    LogUtils.logI("Project Cached", json.toString());
                }
            }
        }
        return results;
    }
}