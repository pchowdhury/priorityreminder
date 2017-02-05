package com.phoenix2k.priorityreminder.drive.task;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.phoenix2k.priorityreminder.drive.DriveAPIType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pushpan on 05/02/17.
 */

public class ListFilesTask extends AsyncTask<Void, Void, List<String>> {
    private com.google.api.services.drive.Drive mService = null;
    private Exception mLastError = null;
    private GoogleDriveListener mGoogleDriveListener;

    public ListFilesTask(String appName, GoogleAccountCredential credential, GoogleDriveListener listener) {
        this.mGoogleDriveListener = listener;
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.drive.Drive.Builder(
                transport, jsonFactory, credential)
                .setApplicationName(appName)
                .build();
    }

    /**
     * Background task to call Drive API.
     *
     * @param params no parameters needed for this task.
     */
    @Override
    protected List<String> doInBackground(Void... params) {
        try {
            return getDataFromApi();
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }

    /**
     * Fetch a list of up to 10 file names and IDs.
     *
     * @return List of Strings describing files, or an empty list if no files
     * found.
     * @throws java.io.IOException
     */
    private List<String> getDataFromApi() throws IOException {
        // Get a list of up to 10 files.
        List<String> fileInfo = new ArrayList<String>();
        FileList result = mService.files().list()
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)")
                .execute();
        List<File> files = result.getFiles();
        if (files != null) {
            for (File file : files) {
                fileInfo.add(String.format("%s (%s)\n",
                        file.getName(), file.getId()));
            }
        }
        return fileInfo;
    }


    @Override
    protected void onPreExecute() {
        mGoogleDriveListener.onDisplayInfo("");
        mGoogleDriveListener.onProgress(true);
    }

    @Override
    protected void onPostExecute(List<String> output) {
        mGoogleDriveListener.onProgress(false);
        if (output == null || output.size() == 0) {
            mGoogleDriveListener.onDisplayInfo("No results returned.");
        } else {
            output.add(0, "Data retrieved using the Drive API:");
            mGoogleDriveListener.onFinishQuery(DriveAPIType.File_List, TextUtils.join("\n", output));
        }
    }

    @Override
    protected void onCancelled() {
        mGoogleDriveListener.onProgress(false);
        if (mLastError != null) {
            if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                mGoogleDriveListener.onGoogleServiceAvailibilityError(((GooglePlayServicesAvailabilityIOException) mLastError).getConnectionStatusCode());
            } else if (mLastError instanceof UserRecoverableAuthIOException) {
                mGoogleDriveListener.onUserRecoverableAuthorizationError((UserRecoverableAuthIOException) mLastError);
            } else {
                mGoogleDriveListener.onError("The following error occurred:\n"
                        + mLastError.getMessage());
            }
        } else {
            mGoogleDriveListener.onDisplayInfo("Request cancelled.");
        }
    }
}