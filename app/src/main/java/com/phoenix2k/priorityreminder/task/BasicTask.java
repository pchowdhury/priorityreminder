package com.phoenix2k.priorityreminder.task;

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
import com.phoenix2k.priorityreminder.R;

import java.io.IOException;

/**
 * Created by Pushpan on 05/02/17.
 */

public abstract class BasicTask extends AsyncTask<Void, Void, Object> {
    private AbstractGoogleJsonClient mService = null;
    private Exception mLastError = null;
    private TaskListener mTaskListener;
    private Context mContext;

    public enum ServiceType {
        Drive, Spreadsheet
    }

    ;

    public BasicTask(Context context, GoogleAccountCredential credential, TaskListener listener) {
        this.mContext = context;
        this.mTaskListener = listener;
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        switch (getServiceType()) {
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

    public Context getContext() {
        return mContext;
    }

    public abstract ServiceType getServiceType();

    public abstract APIType getAPITypeForTask();

    public AbstractGoogleJsonClient getService() {
        return mService;
    }

    public void setLastError(Exception e) {
        this.mLastError = e;
    }

    public Exception getLastError() {
        return this.mLastError;
    }

    public abstract String getProgressMessage();

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
        if (mTaskListener != null) {
            mTaskListener.onStartProcess();
            mTaskListener.onProgress(true, getProgressMessage());
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        if (mTaskListener != null) {
            mTaskListener.onProgress(false, getProgressMessage());
        }
        handleResult(result);
    }

    public void handleResult(Object result) {
        if (mTaskListener != null) {
            if (getLastError() != null) {
                mTaskListener.onError(getLastError().getMessage());
            } else {
                mTaskListener.onFinishQuery(getAPITypeForTask(), result);
            }
        }
    }

    @Override
    protected void onCancelled() {
        if (mTaskListener != null) {
            mTaskListener.onProgress(false, getProgressMessage());
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    mTaskListener.onGoogleServiceAvailibilityError(((GooglePlayServicesAvailabilityIOException) mLastError).getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    mTaskListener.onUserRecoverableAuthorizationError((UserRecoverableAuthIOException) mLastError);
                } else {
                    mTaskListener.onError("The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
                mTaskListener.onDisplayInfo("Request cancelled.");
            }
        }
    }

    public void onDisplayInfo(String info) {
        if (mTaskListener != null) {
            mTaskListener.onDisplayInfo(info);
        }
    }
}