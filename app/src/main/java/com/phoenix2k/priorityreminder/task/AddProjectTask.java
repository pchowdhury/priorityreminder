package com.phoenix2k.priorityreminder.task;

import android.content.Context;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.phoenix2k.priorityreminder.DataStore;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.model.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pushpan on 06/02/17.
 */

public class AddProjectTask extends SpreadsheetTask {
    public AddProjectTask(Context context, GoogleAccountCredential credential, TaskListener listener) {
        super(context, credential, listener);
    }

    @Override
    public ServiceType getServiceType() {
        return ServiceType.Spreadsheet;
    }

    @Override
    public APIType getAPITypeForTask() {
        return APIType.Sheet_Add_Project;
    }

    @Override
    public String getProgressMessage() {
        return getContext().getString(R.string.progress_adding_project);
    }

    @Override
    public Object getDataFromApi() {
        int currentLastPosition = DataStore.getInstance().getLastProjectPosition();
        String range =  "A" + (currentLastPosition + 1) + ":J";
        Boolean isUpdated = updateSheet(range, getData());
        if (!isUpdated) {
            onDisplayInfo("Could not add project");
        }
        return isUpdated;
    }

    public List<List<Object>> getData() {
        final Project project = DataStore.getInstance().getNewProject();
        List<List<Object>> values = new ArrayList<>();
        ArrayList<Object> projectValues = new ArrayList() {{
            add(project.mId + "");
            add(project.mTitle + "");
            add(project.mIndex + "");
            add(project.mProjectType.ordinal() + "");
            add(project.mColorQ1 + "");
            add(project.mColorQ2 + "");
            add(project.mColorQ3 + "");
            add(project.mColorQ4 + "");
            add(project.mCreatedOn + "");
            add(project.mUpdatedOn + "");
        }};
        values.add(projectValues);
        return values;
    }

}
