package com.phoenix2k.priorityreminder.task;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

/**
 * Created by Pushpan on 05/02/17.
 */

public interface GoogleDriveListener extends ErrorCallback{
    void onGoogleServiceAvailibilityError(int statusCode);
    void onUserRecoverableAuthorizationError(UserRecoverableAuthIOException error);
    void onDisplayInfo(String msg);
    void onProgress(boolean show);
    void onFinishQuery(DriveAPIType type, Object result);
}
