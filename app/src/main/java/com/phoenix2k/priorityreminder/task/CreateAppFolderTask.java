package com.phoenix2k.priorityreminder.task;

import android.content.Context;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.phoenix2k.priorityreminder.DataStore;
import com.phoenix2k.priorityreminder.R;

/**
 * Created by Pushpan on 05/02/17.
 */

public class CreateAppFolderTask extends BasicTask {
    public CreateAppFolderTask(Context context, GoogleAccountCredential credential, TaskListener listener) {
        super(context, credential, listener);
    }

    @Override
    public ServiceType getServiceType() {
        return ServiceType.Drive;
    }

    @Override
    public APIType getAPITypeForTask() {
        return APIType.Drive_Folder_Create;
    }

    @Override
    public String getProgressMessage() {
        return getContext().getString(R.string.progress_validating_setup);
    }

    @Override
    public Object getDataFromApi() {
        // Get a list of up to 10 files.
        try {
            File fileMetadata = new File();
            fileMetadata.setName(DataStore.APP_FOLDER_NAME);
            fileMetadata.setMimeType("application/vnd.google-apps.folder");

            File file = ((Drive)getService()).files().create(fileMetadata)
                    .setFields("id")
                    .execute();
        return file.getId();
        } catch (Exception e) {
            setLastError(e);
            cancel(true);
        }
        return null;
    }
}
