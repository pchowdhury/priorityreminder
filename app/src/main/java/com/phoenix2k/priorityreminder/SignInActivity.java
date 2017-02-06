package com.phoenix2k.priorityreminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.phoenix2k.priorityreminder.task.DriveAPIType;
import com.phoenix2k.priorityreminder.task.CreateAppFolderTask;
import com.phoenix2k.priorityreminder.task.CreateDataFileTask;
import com.phoenix2k.priorityreminder.task.FindAppFolderTask;
import com.phoenix2k.priorityreminder.task.FindDataFileTask;
import com.phoenix2k.priorityreminder.task.GoogleDriveListener;
import com.phoenix2k.priorityreminder.pref.PreferenceHelper;
import com.phoenix2k.priorityreminder.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pushpan on 05/02/17.
 */

public class SignInActivity extends BasicDriveActivity implements GoogleDriveListener {

    private static final String TAG = "SignInActivity";
    @BindView(R.id.progress)
    View mProgressView;
    @BindView(R.id.text_log)
    TextView mTextLog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);
    }

    @Override
    public void onGoogleServiceAvailibilityError(int statusCode) {
        showGooglePlayServicesAvailabilityErrorDialog(statusCode);
    }

    @Override
    public void onUserRecoverableAuthorizationError(UserRecoverableAuthIOException error) {
        startActivityForResult(error.getIntent(), BasicDriveActivity.REQUEST_AUTHORIZATION);
    }

    @Override
    public void onSignInComplete() {
        if (PreferenceHelper.getSavedDataFileId(this) == null) {
            new FindAppFolderTask(this, mCredential, this).execute();
        } else {
            onSetupValidationComplete();
        }
    }

    @Override
    public void onError(String err) {
        mTextLog.setText(err);
    }

    @Override
    public void onDisplayInfo(String msg) {
        mTextLog.setText(msg);
    }

    @Override
    public void onProgress(boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onFinishQuery(DriveAPIType type, Object result) {
        switch (type) {
            case Folder_List:
                if (result != null) {
                    PreferenceHelper.setAppFolderId(this, result.toString());
                    LogUtils.logI(TAG, "Folder Id =" + result.toString());
                    onDisplayInfo("Folder Id =" + result.toString());
                    new FindDataFileTask(this, mCredential, this).execute();
                } else {
                    LogUtils.logI(TAG, "Folder Id not found");
                    onDisplayInfo("Folder Id not found");
                    new CreateAppFolderTask(this, mCredential, this).execute();
                }
                break;
            case Folder_Create:
                if (result != null) {
                    PreferenceHelper.setAppFolderId(this, result.toString());
                    LogUtils.logI(TAG, "Folder Id created =" + result.toString());
                    onDisplayInfo("Folder Id created =" + result.toString());
                    new FindDataFileTask(this, mCredential, this).execute();
                } else {
                    LogUtils.logI(TAG, "Folder couldn't be created");
                    onDisplayInfo("Folder couldn't be created");
                }
                break;
            case File_List:
                if (result != null) {
                    PreferenceHelper.setDataFileId(this, result.toString());
                    LogUtils.logI(TAG, "File Id =" + result.toString());
                    onDisplayInfo("File Id =" + result.toString());
                    onSetupValidationComplete();
                } else {
                    LogUtils.logI(TAG, "File Id not found");
                    onDisplayInfo("File Id not found");
                    new CreateDataFileTask(this, mCredential, this).execute();
                }
                break;
            case File_Create:
                if (result != null) {
                    PreferenceHelper.setDataFileId(this, result.toString());
                    LogUtils.logI(TAG, "File Id created =" + result.toString());
                    onDisplayInfo("File Id created =" + result.toString());
                    onSetupValidationComplete();
                } else {
                    LogUtils.logI(TAG, "Data file couldn't be created");
                    onDisplayInfo("Data file couldn't be created");
                }
                break;

        }
    }

    private void onSetupValidationComplete() {
        Intent intent = new Intent(this, DashboardActivity.class);
//        startActivity(intent);
    }
}
