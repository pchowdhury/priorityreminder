package com.phoenix2k.priorityreminder.drive.task;

import android.content.Context;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.phoenix2k.priorityreminder.DataStore;
import com.phoenix2k.priorityreminder.drive.DriveAPIType;
import com.phoenix2k.priorityreminder.utils.LogUtils;

import java.util.List;

/**
 * Created by Pushpan on 05/02/17.
 */

public class FindAppFolderTask extends BasicTask {
    public FindAppFolderTask(Context context, GoogleAccountCredential credential, GoogleDriveListener listener) {
        super(context, credential, listener);
    }

    @Override
    public ServiceType getServiceType() {
        return ServiceType.Drive;
    }

    @Override
    public Object getDataFromApi() {
        // Get a list of up to 10 files.
        FileList result;
        try {
            result = ((Drive) getService()).files().list().setQ("mimeType = 'application/vnd.google-apps.folder' and 'root' in parents and trashed=false and name = '" + DataStore.APP_FOLDER_NAME + "'")
                    .setFields("files(id, name)")
                    .execute();

            List<File> files = result.getFiles();
            LogUtils.printList(files);
            if (files != null) {
                for (File file : files) {
                    if (file.getName().equals(DataStore.APP_FOLDER_NAME)) {
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
            onFinishQuery(DriveAPIType.Folder_List, result);
        }
    }
}
