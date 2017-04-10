package com.phoenix2k.priorityreminder;

import android.content.Context;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.phoenix2k.priorityreminder.task.APIType;
import com.phoenix2k.priorityreminder.task.LoadProjectsTask;
import com.phoenix2k.priorityreminder.task.SyncTask;
import com.phoenix2k.priorityreminder.task.TaskListener;
import com.phoenix2k.priorityreminder.utils.LogUtils;

import java.util.ArrayList;

/**
 * Created by Pushpan on 13/02/17.
 */

public class SyncManager implements TaskListener {
    private static final String TAG = "SyncManager";
    private static SyncManager mInstance;
    private ArrayList<SyncListener> mListeners = new ArrayList<>();

    public static SyncManager getInstance() {
        if (mInstance == null) {
            mInstance = new SyncManager();
        }
        return mInstance;
    }

    public void startListening(SyncListener listener) {
        if (listener != null && !mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    public void stopListening(SyncListener listener) {
        if (listener != null && mListeners.contains(listener)) {
            mListeners.remove(listener);
        }
    }

    public void startSync(Context context, GoogleAccountCredential credentials, ArrayList<Object> items) {
        new SyncTask(context, credentials, this).updateItems(items).execute();
    }

    @Override
    public void onGoogleServiceAvailibilityError(int statusCode) {

    }

    @Override
    public void onUserRecoverableAuthorizationError(UserRecoverableAuthIOException error) {

    }

    @Override
    public void onDisplayInfo(String msg) {
        for(SyncListener listener : mListeners){
            listener.onSyncCompleteFailed(msg);
        }
    }

    @Override
    public void onStartProcess() {
        for(SyncListener listener : mListeners){
            listener.onSyncStart();
        }
    }

    @Override
    public void onProgress(boolean show, String msg) {

    }

    @Override
    public void onFinishQuery(APIType type, Object result) {
        switch (type) {
            case Sheet_Push_Updates:
                if (result != null) {
                    boolean isUpdated = (Boolean) result;
                    if (isUpdated) {
                        for(SyncListener listener : mListeners){
                            listener.onSyncCompleteWithSucess();
                        }
                    } else {
                        onDisplayInfo("Couldn't finish Sync");
                    }
                } else {
                    onDisplayInfo("Error Syncing");
                }
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

    public interface SyncListener {

        void onSyncStart();

        void onSyncCompleteWithSucess();

        void onSyncCompleteFailed(String errorMsg);
    }
}
