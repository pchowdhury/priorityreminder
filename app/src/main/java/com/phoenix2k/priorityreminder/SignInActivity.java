package com.phoenix2k.priorityreminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.phoenix2k.priorityreminder.pref.PreferenceHelper;
import com.phoenix2k.priorityreminder.task.APIType;
import com.phoenix2k.priorityreminder.task.CreateAppFolderTask;
import com.phoenix2k.priorityreminder.task.CreateFileTask;
import com.phoenix2k.priorityreminder.task.FindAppFolderTask;
import com.phoenix2k.priorityreminder.task.SearchFileTask;
import com.phoenix2k.priorityreminder.utils.IDGenerator;
import com.phoenix2k.priorityreminder.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pushpan on 05/02/17.
 */

public class SignInActivity extends BasicCommunicationActivity {

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
        attemptSignIn();
    }
    @Override
    public void onAccountValidationComplete() {
        if (PreferenceHelper.getSavedDataFileId(this) == null || PreferenceHelper.getSavedProjectFileId(this) == null) {
            new FindAppFolderTask(this, getUserCredentials(), this).execute();
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
    public void onProgress(boolean show, String message) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onFinishQuery(APIType type, Object result) {
        switch (type) {
            case Drive_Folder_List:
                if (result != null) {
                    PreferenceHelper.setAppFolderId(this, result.toString());
                    LogUtils.logI(TAG, "Folder Id =" + result.toString());
                    onDisplayInfo("Folder Id =" + result.toString());
                    new SearchFileTask(this, getUserCredentials(), this, APIType.Drive_Search_Project_File).execute();
                } else {
                    LogUtils.logI(TAG, "Folder Id not found");
                    onDisplayInfo("Folder Id not found");
                    new CreateAppFolderTask(this, getUserCredentials(), this).execute();
                }
                break;
            case Drive_Folder_Create:
                if (result != null) {
                    PreferenceHelper.setAppFolderId(this, result.toString());
                    LogUtils.logI(TAG, "Folder Id created =" + result.toString());
                    onDisplayInfo("Folder Id created =" + result.toString());
                    new SearchFileTask(this, getUserCredentials(), this, APIType.Drive_Search_Project_File).execute();
                } else {
                    LogUtils.logI(TAG, "Folder couldn't be created");
                    onDisplayInfo("Folder couldn't be created");
                }
                break;
            case Drive_Search_Project_File:
                if (result != null) {
                    PreferenceHelper.setProjectFileId(this, result.toString());
                    LogUtils.logI(TAG, "Project File Id =" + result.toString());
                    onDisplayInfo("project File Id =" + result.toString());
                    new SearchFileTask(this, getUserCredentials(), this, APIType.Drive_Search_Data_File).execute();
                } else {
                    LogUtils.logI(TAG, "Project File Id not found");
                    onDisplayInfo("Project File Id not found");
                    new CreateFileTask(APIType.Drive_Project_File_Create, this, getUserCredentials(), this).execute();
                }
                break;
            case Drive_Project_File_Create:
                if (result != null) {
                    PreferenceHelper.setProjectFileId(this, result.toString());
                    LogUtils.logI(TAG, "Project File Id created =" + result.toString());
                    onDisplayInfo("Project File Id created =" + result.toString());
                    new SearchFileTask(this, getUserCredentials(), this, APIType.Drive_Search_Data_File).execute();
//                    new CreateFileTask(APIType.Drive_Data_File_Create, this, getUserCredentials(), this).execute();
                } else {
                    LogUtils.logI(TAG, "Project file couldn't be created");
                    onDisplayInfo("Project file couldn't be created");
                }
                break;
            case Drive_Search_Data_File:
                if (result != null) {
                    PreferenceHelper.setDataFileId(this, result.toString());
                    LogUtils.logI(TAG, "Data File Id =" + result.toString());
                    onDisplayInfo("Data File Id =" + result.toString());
                    onSetupValidationComplete();
                } else {
                    LogUtils.logI(TAG, "Data File Id not found");
                    onDisplayInfo("Data File Id not found");
                    new CreateFileTask(APIType.Drive_Data_File_Create, this, getUserCredentials(), this).execute();
                }
                break;
            case Drive_Data_File_Create:
                if (result != null) {
                    PreferenceHelper.setDataFileId(this, result.toString());
                    LogUtils.logI(TAG, "Data File Id created =" + result.toString());
                    onDisplayInfo("Data File Id created =" + result.toString());
                    onSetupValidationComplete();
                } else {
                    LogUtils.logI(TAG, "Data file couldn't be created");
                    onDisplayInfo("Data file couldn't be created");
                }
                break;
        }
    }

    private void onSetupValidationComplete() {
        IDGenerator.init();
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}
