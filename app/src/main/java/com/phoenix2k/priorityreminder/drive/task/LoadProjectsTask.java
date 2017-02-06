package com.phoenix2k.priorityreminder.drive.task;

import android.content.Context;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.phoenix2k.priorityreminder.pref.PreferenceHelper;
import com.phoenix2k.priorityreminder.utils.LogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pushpan on 06/02/17.
 */

public class LoadProjectsTask extends BasicTask {
    public LoadProjectsTask(Context context, GoogleAccountCredential credential, GoogleDriveListener listener) {
        super(context, credential, listener);

    }

    @Override
    public ServiceType getServiceType() {
        return ServiceType.Spreadsheet;
    }

    @Override
    public Object getDataFromApi() {
        String spreadsheetId = PreferenceHelper.getSavedDataFileId(getContext());
        String range = "Class Data!A2:E";
        try {
            List<String> results = new ArrayList<String>();
            ValueRange response;
            response = ((Sheets) getService()).spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();

            List<List<Object>> values = response.getValues();
            if (values != null) {
                results.add("Name, Major");
                for (List row : values) {
                    results.add(row.get(0) + ", " + row.get(4));
                }
            }
            return results;
        } catch (IOException e) {
            LogUtils.printException(e);
        }
        return null;
    }

    @Override
    public void handleResult(Object result) {

    }
}
