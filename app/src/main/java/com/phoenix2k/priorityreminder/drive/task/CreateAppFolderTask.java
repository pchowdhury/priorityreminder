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

public class CreateAppFolderTask extends BasicTask {
    public CreateAppFolderTask(String appName, GoogleAccountCredential credential, GoogleDriveListener listener) {
        super(appName, credential, listener);
    }

    @Override
    public Object getDataFromApi() {
        // Get a list of up to 10 files.
        try {
            File fileMetadata = new File();
            fileMetadata.setName(DataStore.APP_FOLDER_NAME);
            fileMetadata.setMimeType("application/vnd.google-apps.folder");

            File file = getService().files().create(fileMetadata)
                    .setFields("id")
                    .execute();
        return file.getId();
        } catch (Exception e) {
            setLastError(e);
            cancel(true);
        }
        return null;
    }

    @Override
    public void handleResult(Object result) {
        if(getLastError()!=null){
            onError(getLastError().getMessage());
        }else{
            onFinishQuery(DriveAPIType.Folder_Create, result);
        }
    }
}
