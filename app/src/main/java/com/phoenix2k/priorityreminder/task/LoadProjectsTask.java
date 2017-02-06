package com.phoenix2k.priorityreminder.task;

import android.content.Context;
import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.phoenix2k.priorityreminder.ProjectsColumns;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.model.Project;
import com.phoenix2k.priorityreminder.pref.PreferenceHelper;
import com.phoenix2k.priorityreminder.utils.IdGenerator;
import com.phoenix2k.priorityreminder.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Pushpan on 06/02/17.
 */

public class LoadProjectsTask extends BasicTask {
    public LoadProjectsTask(Context context, GoogleAccountCredential credential, TaskListener listener) {
        super(context, credential, listener);

    }

    @Override
    public ServiceType getServiceType() {
        return ServiceType.Spreadsheet;
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
        String spreadsheetId = PreferenceHelper.getSavedDataFileId(getContext());
        String range = "Project!A2:I";
        try {
            List<Project> results = new ArrayList<>();
            ValueRange response;
            response = ((Sheets) getService()).spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();

            List<List<Object>> values = response.getValues();
            if (values != null) {
                for (List row : values) {
                    Project project = new Project();
                    for (int i = 0; i < row.size(); i++) {
                        String value = (String) row.get(i);
                        switch (ProjectsColumns.values()[i]){
                            case ID:
                                project.mId = value;
                                break;
                            case NAME:
                                project.mTitle = value;
                                break;
                            case Q1_COLOR:
                                project.mColorQ1 = value;
                                break;
                            case Q2_COLOR:
                                project.mColorQ2 = value;
                                break;
                            case Q3_COLOR:
                                project.mColorQ3 = value;
                                break;
                            case Q4_COLOR:
                                project.mColorQ4 = value;
                                break;
                            case TYPE:
                                project.mProjectType = Project.ProjectType.values()[Integer.valueOf(value)];
                                break;
                            case CREATED_ON:
                                project.mCreatedOn= Long.getLong(value);
                                break;
                            case UPDATED_ON:
                                project.mUpdatedOn = Long.getLong(value);
                                break;
                        }
                    }
                    results.add(project);
                }
            }
            return results;
        } catch (Exception e) {
            setLastError(e);
            LogUtils.printException(e);
        }
        return null;
    }

}
