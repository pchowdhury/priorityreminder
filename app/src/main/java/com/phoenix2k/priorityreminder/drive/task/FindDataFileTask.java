package com.phoenix2k.priorityreminder.drive.task;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.phoenix2k.priorityreminder.DataStore;
import com.phoenix2k.priorityreminder.drive.DriveAPIType;

import java.util.List;

/**
 * Created by Pushpan on 05/02/17.
 */

public class FindDataFileTask extends BasicTask {
    private String mAppFolderId;

    public FindDataFileTask(String appName, String appFolderId, GoogleAccountCredential credential, GoogleDriveListener listener) {
        super(appName, credential, listener);
        this.mAppFolderId = appFolderId;
    }

    @Override
    public Object getDataFromApi() {
        // Get a list of up to 10 files.
        FileList result;
        try {
            result = getService().files().list().setQ("mimeType = 'application/vnd.google-apps.spreadsheet' and 'root' in parents and trashed=false and '" + mAppFolderId + "' in parents")
                    .setFields("files(id, name)")
                    .execute();

            List<File> files = result.getFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().equals(DataStore.APP_DATA_FILE_NAME + ".gsheet")) {
                        return file.getId();
                    }
                }
            }
        } catch (Exception e) {
            setLastError(e);
            cancel(true);
            return null;
        }
        return null;
    }

    @Override
    public void handleResult(Object result) {
        if (getLastError() != null) {
            onError(getLastError().getMessage());
        } else {
            onFinishQuery(DriveAPIType.File_List, result);
        }
    }
}
