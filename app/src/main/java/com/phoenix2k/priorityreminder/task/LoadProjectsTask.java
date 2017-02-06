package com.phoenix2k.priorityreminder.task;

import android.content.Context;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.pref.PreferenceHelper;
import com.phoenix2k.priorityreminder.utils.LogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        String range = "AppData!A2:E";
        try {
            List<String> results = new ArrayList<>();
            ValueRange response;
            response = ((Sheets) getService()).spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();

            List<List<Object>> values = response.getValues();
            if (values != null) {
                for (List row : values) {
                    String rowStr = "";
                    for (int i = 0; i < row.size(); i++) {
                        Object obj = row.get(i);
                        if (i != 0) {
                            rowStr += ", ";
                        }
                        rowStr += obj.toString();
                    }
                    results.add(rowStr);
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
