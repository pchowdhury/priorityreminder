package com.phoenix2k.priorityreminder.drive.task;

import android.content.Context;
import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.drive.DriveAPIType;

import java.io.IOException;

/**
 * Created by Pushpan on 05/02/17.
 */

public abstract class BasicTask extends AsyncTask<Void, Void, Object> {
    private AbstractGoogleJsonClient mService = null;
    private Exception mLastError = null;
    private GoogleDriveListener mGoogleDriveListener;
    private Context mContext;

    public enum ServiceType {
        Drive, Spreadsheet
    }

    ;

    public BasicTask(Context context, GoogleAccountCredential credential, GoogleDriveListener listener) {
        this.mContext = context;
        this.mGoogleDriveListener = listener;
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        switch (getServiceType()){
            case Drive:
                mService = new com.google.api.services.drive.Drive.Builder(
                        transport, jsonFactory, credential)
                        .setApplicationName(mContext.getString(R.string.app_name))
                        .build();
                break;
            case Spreadsheet:
                mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                        transport, jsonFactory, credential)
                        .setApplicationName(mContext.getString(R.string.app_name))
                        .build();
                break;

        }

    }

    public Context getContext(){
        return mContext;
    }
    public abstract ServiceType getServiceType();

    public AbstractGoogleJsonClient getService() {
        return mService;
    }

    public void setLastError(Exception e) {
        this.mLastError = e;
    }

    public Exception getLastError() {
        return this.mLastError;
    }

    /**
     * Background task to call Drive API.
     *
     * @param params no parameters needed for this task.
     */
    @Override
    protected Object doInBackground(Void... params) {
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
     * @throws IOException
     */
    public abstract Object getDataFromApi();


    @Override
    protected void onPreExecute() {
        if (mGoogleDriveListener != null) {
            mGoogleDriveListener.onDisplayInfo("");
            mGoogleDriveListener.onProgress(true);
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        if (mGoogleDriveListener != null) {
            mGoogleDriveListener.onProgress(false);
        }
        handleResult(result);
    }

    public abstract void handleResult(Object result);

    @Override
    protected void onCancelled() {
        if (mGoogleDriveListener != null) {
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

    public void onDisplayInfo(String info) {
        if (mGoogleDriveListener != null) {
            mGoogleDriveListener.onDisplayInfo(info);
        }
    }

    public void onFinishQuery(DriveAPIType type, Object result) {
        if (mGoogleDriveListener != null) {
            mGoogleDriveListener.onFinishQuery(type, result);
        }
    }

    public void onError(String error) {
        if (mGoogleDriveListener != null) {
            mGoogleDriveListener.onError(error);
        }
    }
}