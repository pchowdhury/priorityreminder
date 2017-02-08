package com.phoenix2k.priorityreminder.task;

import android.content.Context;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.phoenix2k.priorityreminder.DataStore;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.utils.LogUtils;

import java.util.List;

/**
 * Created by Pushpan on 05/02/17.
 */

public class FindAppFolderTask extends BasicTask {
    public FindAppFolderTask(Context context, GoogleAccountCredential credential, TaskListener listener) {
        super(context, credential, listener);
    }

    @Override
    public ServiceType getServiceType() {
        return ServiceType.Drive;
    }

    @Override
    public APIType getAPITypeForTask() {
        return APIType.Drive_Folder_List;
    }

    @Override
    public String getProgressMessage() {
        return getContext().getString(R.string.progress_validating_setup);
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
            LogUtils.printFileList(files);
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
}
