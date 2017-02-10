package com.phoenix2k.priorityreminder.task;

import android.content.Context;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.phoenix2k.priorityreminder.DataStore;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.pref.PreferenceHelper;
import com.phoenix2k.priorityreminder.utils.LogUtils;

import java.util.List;

/**
 * Created by Pushpan on 05/02/17.
 */

public class SearchFileTask extends BasicTask {
    private APIType mAPIType;

    public SearchFileTask(Context context, GoogleAccountCredential credential, TaskListener listener, APIType apiType) {
        super(context, credential, listener);
        this.mAPIType = apiType;
    }

    @Override
    public ServiceType getServiceType() {
        return ServiceType.Drive;
    }

    @Override
    public APIType getAPITypeForTask() {
        return mAPIType;
    }

    @Override
    public String getProgressMessage() {
        return getContext().getString(R.string.progress_validating_setup);
    }

    @Override
    public Object getDataFromApi() {
        // Get a list of up to 10 files.
        FileList result;
        String appFolderId = PreferenceHelper.getSavedAppFolderId(getContext());
        String searchedFileName = ((mAPIType == APIType.Drive_Search_Project_File) ? DataStore.PROJECT_FILE_NAME : DataStore.DATA_FILE_NAME);
        try {
            result = ((Drive) getService()).files().list().setQ("mimeType = 'application/vnd.google-apps.spreadsheet' and trashed=false and '" + appFolderId + "' in parents and name = '" + searchedFileName + "'")
                    .setFields("files(id, name)")
                    .execute();
            List<File> files = result.getFiles();
            LogUtils.printFileList(files);
            if (files != null) {
                for (File file : files) {
                    if (file.getName().equals(searchedFileName)) {
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
