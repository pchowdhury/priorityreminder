package com.phoenix2k.priorityreminder.task;

import android.content.Context;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.phoenix2k.priorityreminder.DataStore;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.pref.PreferenceHelper;
import com.phoenix2k.priorityreminder.utils.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

/**
 * Created by Pushpan on 05/02/17.
 */

public class CreateDataFileTask extends BasicTask {
    private static final String TAG = "CreateDataFileTask";

    public CreateDataFileTask(Context context, GoogleAccountCredential credential, GoogleDriveListener listener) {
        super(context, credential, listener);
    }

    @Override
    public ServiceType getServiceType() {
        return ServiceType.Drive;
    }

    @Override
    public Object getDataFromApi() {
        // Get a list of up to 10 files.
        copyCsvToTemporaryPath();
        try {
            File fileMetadata = new File();
            fileMetadata.setName(DataStore.APP_DATA_FILE_NAME);
            fileMetadata.setMimeType("application/vnd.google-apps.spreadsheet");
            fileMetadata.setParents(Collections.singletonList(PreferenceHelper.getSavedAppFolderId(getContext())));

            java.io.File filePath = new java.io.File(getContext().getCacheDir().getAbsolutePath() + "/sample.csv");
            FileContent mediaContent = new FileContent("text/csv", filePath);
            File file = ((Drive)getService()).files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();
            if(filePath.delete()){
                LogUtils.logI(TAG, "temp file deleted");
            }
            return file.getId();
        } catch (Exception e) {
            setLastError(e);
            cancel(true);
        }
        return null;
    }

    private void copyCsvToTemporaryPath() {
        InputStream input = getContext().getResources().openRawResource(R.raw.sample);
        java.io.File file;
        java.io.FileOutputStream output = null;

        try {
            file = new java.io.File(getContext().getCacheDir(), "sample.csv");
            output = new java.io.FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024]; // or other buffer size
            int read;

            while ((read = input.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            output.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(output!=null){

                output.close();}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void handleResult(Object result) {
        if (getLastError() != null) {
            onError(getLastError().getMessage());
        } else {
            onFinishQuery(DriveAPIType.File_Create, result);
        }
    }
}
