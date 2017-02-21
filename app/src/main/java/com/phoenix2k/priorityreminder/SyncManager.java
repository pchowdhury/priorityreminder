package com.phoenix2k.priorityreminder;

import android.content.Context;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.phoenix2k.priorityreminder.task.APIType;
import com.phoenix2k.priorityreminder.task.LoadProjectsTask;
import com.phoenix2k.priorityreminder.task.SyncTask;
import com.phoenix2k.priorityreminder.task.TaskListener;
import com.phoenix2k.priorityreminder.utils.LogUtils;

/**
 * Created by Pushpan on 13/02/17.
 */

public class SyncManager implements TaskListener {
    private static final String TAG = "SyncManager";
    private static SyncManager mInstance;
    private Context mContext;

    public static SyncManager getInstance() {
        if (mInstance == null) {
            mInstance = new SyncManager();
        }
        return mInstance;
    }

    public void startSync(Context context, GoogleAccountCredential credentials) {
        new SyncTask(context, credentials, this).execute();

    }

    public SyncManager addToUpdates(Object updatedItem) {
        DataStore.getInstance().addToUpdate(updatedItem);
        return this;
    }

    @Override
    public void onGoogleServiceAvailibilityError(int statusCode) {

    }

    @Override
    public void onUserRecoverableAuthorizationError(UserRecoverableAuthIOException error) {

    }

    @Override
    public void onDisplayInfo(String msg) {

    }

    @Override
    public void onProgress(boolean show, String msg) {

    }

    @Override
    public void onFinishQuery(APIType type, Object result) {
        switch (type) {
            case Sheet_Push_Updates:
                LogUtils.logI(TAG, "Successfully updated");
                break;
        }
    }

    @Override
    public GoogleAccountCredential getUserCredentials() {
        return null;
    }

    @Override
    public void onError(String err) {
        LogUtils.logI(TAG, err);
    }
}
