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
        Project project = DataStore.getInstance().getNewProject();
        String range = "A" + project.mPosition + ":P";
        Boolean isUpdated = updateSheet(range, getData(), getProjectSpreadsheetId());
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
            add(project.mPosition + "");
            add(project.mTitle + "");
            add(project.mIndex + "");
            add(project.mProjectType.ordinal() + "");
            add(project.mTitleQuadrants.get(TaskItem.QuadrantType.Q1));
            add(project.mTitleQuadrants.get(TaskItem.QuadrantType.Q2));
            add(project.mTitleQuadrants.get(TaskItem.QuadrantType.Q3));
            add(project.mTitleQuadrants.get(TaskItem.QuadrantType.Q4));
            add(project.mColorQuadrants.get(TaskItem.QuadrantType.Q1) + "");
            add(project.mColorQuadrants.get(TaskItem.QuadrantType.Q2) + "");
            add(project.mColorQuadrants.get(TaskItem.QuadrantType.Q3) + "");
            add(project.mColorQuadrants.get(TaskItem.QuadrantType.Q4) + "");
            add(project.mCenterInPercent.x + "," + project.mCenterInPercent.y);
            add(project.mCreatedOn + "");
            add(project.mUpdatedOn + "");
        }};
        values.add(projectValues);
        return values;
    }

}
