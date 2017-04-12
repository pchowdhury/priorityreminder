package com.phoenix2k.priorityreminder;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.phoenix2k.priorityreminder.model.Project;
import com.phoenix2k.priorityreminder.model.TaskItem;
import com.phoenix2k.priorityreminder.pref.PreferenceHelper;
import com.phoenix2k.priorityreminder.store.SQLDataStore;
import com.phoenix2k.priorityreminder.task.APIType;
import com.phoenix2k.priorityreminder.task.CreateAppFolderTask;
import com.phoenix2k.priorityreminder.task.CreateFileTask;
import com.phoenix2k.priorityreminder.task.FindAppFolderTask;
import com.phoenix2k.priorityreminder.task.LoadAllTasks;
import com.phoenix2k.priorityreminder.task.LoadProjectsTask;
import com.phoenix2k.priorityreminder.task.SearchFileTask;
import com.phoenix2k.priorityreminder.utils.LogUtils;
import com.phoenix2k.priorityreminder.utils.StaticDataProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pushpan on 05/02/17.
 */

public class SyncActivity extends BasicCommunicationActivity implements SyncManager.SyncListener {
    private static final String TAG = "SignInActivity";
    private static final boolean ENABLE_CACHE = false;
    private static final boolean GENERATE_CACHE = false;
    private static final boolean USE_ASSET_CACHE = true;
    @BindView(R.id.progress)
    View mProgressView;
    @BindView(R.id.text_log)
    TextView mTextLog;

    private ArrayList<Project> mRemoteProjects;
    private ArrayList<TaskItem> mRemoteTasks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_signin);
        ButterKnife.bind(this);
        setUpCache();
        SyncManager.getInstance().startListening(this);
        attemptSignIn();
    }

    private void setUpCache() {
        StaticDataProvider.init(this).setEnableStaticEngine(ENABLE_CACHE).setUseAssetCacheDebugOption(USE_ASSET_CACHE).setUseSDCard(true).setGenerateCacheDebugOption(GENERATE_CACHE);
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
        onDisplayInfo(err);
    }

    @Override
    public void onDisplayInfo(String msg) {
        mTextLog.setText(msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        onSyncFailed();
    }

    @Override
    public void onStartProcess() {

    }

    @Override
    public void onProgress(boolean show, String message) {
        if (show) {
            mProgressView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFinishQuery(APIType type, Object result) {
        switch (type) {
            case Drive_Folder_List:
                if (result != null) {
                    PreferenceHelper.setAppFolderId(this, result.toString());
                    LogUtils.logI(TAG, "Folder Id =" + result.toString());
                    new SearchFileTask(this, getUserCredentials(), this, APIType.Drive_Search_Project_File).execute();
                } else {
                    LogUtils.logI(TAG, "Folder Id not found");
                    new CreateAppFolderTask(this, getUserCredentials(), this).execute();
                }
                break;
            case Drive_Folder_Create:
                if (result != null) {
                    PreferenceHelper.setAppFolderId(this, result.toString());
                    LogUtils.logI(TAG, "Folder Id created =" + result.toString());
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
                    new SearchFileTask(this, getUserCredentials(), this, APIType.Drive_Search_Data_File).execute();
                } else {
                    LogUtils.logI(TAG, "Project File Id not found");
                    new CreateFileTask(APIType.Drive_Project_File_Create, this, getUserCredentials(), this).execute();
                }
                break;
            case Drive_Project_File_Create:
                if (result != null) {
                    PreferenceHelper.setProjectFileId(this, result.toString());
                    LogUtils.logI(TAG, "Project File Id created =" + result.toString());
                    new SearchFileTask(this, getUserCredentials(), this, APIType.Drive_Search_Data_File).execute();
                } else {
                    LogUtils.logI(TAG, "Project file couldn't be created");
                    onDisplayInfo("Project file couldn't be created");
                }
                break;
            case Drive_Search_Data_File:
                if (result != null) {
                    PreferenceHelper.setDataFileId(this, result.toString());
                    LogUtils.logI(TAG, "Data File Id =" + result.toString());
                    onSetupValidationComplete();
                } else {
                    LogUtils.logI(TAG, "Data File Id not found");
                    new CreateFileTask(APIType.Drive_Data_File_Create, this, getUserCredentials(), this).execute();
                }
                break;
            case Drive_Data_File_Create:
                if (result != null) {
                    PreferenceHelper.setDataFileId(this, result.toString());
                    LogUtils.logI(TAG, "Data File Id created =" + result.toString());
                    onSetupValidationComplete();
                } else {
                    LogUtils.logI(TAG, "Data file couldn't be created");
                    onDisplayInfo("Data file couldn't be created");
                }
                break;
            case Sheet_Load_Projects_Metadata:
                if (result != null) {
                    mRemoteProjects = (ArrayList<Project>) result;
                    new LoadAllTasks(this, getUserCredentials(), this).execute();
                } else {
                    LogUtils.logI(TAG, "No projects");
                    mRemoteProjects = null;
                    mRemoteTasks = null;
                    mergeWithRemote(mRemoteProjects, mRemoteTasks);
                }
                break;
            case Sheet_Load_All_Tasks:
                if (result != null) {
                    mRemoteTasks = (ArrayList<TaskItem>) result;
                    mergeWithRemote(mRemoteProjects, mRemoteTasks);
                } else {
                    LogUtils.logI(TAG, "No TaskItem found");
                    mRemoteTasks = null;
                    mergeWithRemote(mRemoteProjects, mRemoteTasks);
                }
                break;
        }
    }


    public void mergeWithRemote(ArrayList<Project> remoteProjects, ArrayList<TaskItem> remoteTasks) {

        if (remoteProjects == null) {
            remoteProjects = new ArrayList<>();
        }
        if (remoteTasks == null) {
            remoteTasks = new ArrayList<>();
        }

        int remoteProjectCount = remoteProjects.size();
        int remoteTaskItemCount = remoteTasks.size();
        //assuming remote and local maintains correct time;

        ArrayList<Project> localProjects = SQLDataStore.getInstance().getProjects(true);
        ArrayList<TaskItem> localTasks = SQLDataStore.getInstance().getTaskItems(null, null, null, true);

        if (localProjects == null) {
            localProjects = new ArrayList<>();
        }
        if (localTasks == null) {
            localTasks = new ArrayList<>();
        }

        //STEP 2: Get projects and task map
        HashMap<String, Project> localProjectMap = new HashMap<>();
        for (Project project : localProjects) {
            localProjectMap.put(project.mId + "", project);
        }

        HashMap<String, TaskItem> localTaskItemMap = new HashMap<>();
        for (TaskItem taskItem : localTasks) {
            localTaskItemMap.put(taskItem.mId + "", taskItem);
        }

        ArrayList<Project> mergedProjects = new ArrayList<>();
        ArrayList<TaskItem> mergedTaskItems = new ArrayList<>();

        //STEP 3a: Compare Projects
        boolean hasLocalUpdateSinceLastSync = false;
        while (remoteProjects.size() > 0) {
            Project remoteProject = remoteProjects.get(0);
            Project localProject = localProjectMap.get(remoteProject.mId + "");
            if (localProject != null) {
                //project is common, keep the latest project and reject the old
                if (localProject.mUpdatedOn > remoteProject.mUpdatedOn) {
                    mergedProjects.add(localProject);
                    hasLocalUpdateSinceLastSync = true;
                } else {
                    mergedProjects.add(remoteProject);
                }
                remoteProjects.remove(remoteProject);
                localProjects.remove(localProject);
            } else {
                //project only present in remote
                mergedProjects.add(remoteProject);
                remoteProjects.remove(remoteProject);
            }
        }

        //add remaining local projects
        if (localProjects.size() > 0) {
            mergedProjects.addAll(localProjects);
            localProjects.clear();
            hasLocalUpdateSinceLastSync = true;
        }

        HashMap<String, Project> projectMap = new HashMap<>();
        DataStore.sortedProjectByIndex(mergedProjects);
        //re-indexing
        for (int i = 0; i < mergedProjects.size(); i++) {
            Project project = mergedProjects.get(i);
            project.mIndex = i;
            projectMap.put(project.mId, project);
        }

        //STEP 3b: Compare taskItem

        while (remoteTasks.size() > 0) {
            TaskItem remoteTaskItem = remoteTasks.get(0);
            TaskItem localTaskItem = localTaskItemMap.get(remoteTaskItem.mId + "");
            if (localTaskItem != null) {
                //task is common, keep the latest project and reject the old
                if (localTaskItem.mUpdatedOn >= remoteTaskItem.mUpdatedOn) {
                    mergedTaskItems.add(localTaskItem);
                    hasLocalUpdateSinceLastSync = true;
                    projectMap.get(localTaskItem.mProjectId).getTaskListForQuadrant(localTaskItem.mQuadrantType).add(localTaskItem);
                } else {
                    mergedTaskItems.add(remoteTaskItem);
                    projectMap.get(remoteTaskItem.mProjectId).getTaskListForQuadrant(remoteTaskItem.mQuadrantType).add(remoteTaskItem);
                }
                remoteTasks.remove(remoteTaskItem);
                localTasks.remove(localTaskItem);
            } else {
                //taskItem only present in remote
                mergedTaskItems.add(remoteTaskItem);
                remoteTasks.remove(remoteTaskItem);
                projectMap.get(remoteTaskItem.mProjectId).getTaskListForQuadrant(remoteTaskItem.mQuadrantType).add(remoteTaskItem);
            }
        }

        //add remaining local projects
        if (localTasks.size() > 0) {
            mergedTaskItems.addAll(localTasks);
            for (int i = 0; i < localTasks.size(); i++) {
                TaskItem item = localTasks.get(i);
                projectMap.get(item.mProjectId).getTaskListForQuadrant(item.mQuadrantType).add(item);
            }
            localTasks.clear();
            hasLocalUpdateSinceLastSync = true;
        }

        //re-indexing tasks
        for (int i = 0; i < mergedProjects.size(); i++) {
            Project project = mergedProjects.get(i);
            for (TaskItem.QuadrantType type : TaskItem.QuadrantType.values()) {
                ArrayList<TaskItem> items = project.getTaskListForQuadrant(type);
                DataStore.sortedTaskByIndex(items);
                for (int j = 0; j < items.size(); j++) {
                    items.get(j).mIndex = j;
                }
            }
        }

        if (!hasLocalUpdateSinceLastSync) {
            LogUtils.logD(TAG, "Already up to date");
            onSyncComplete();
            return;
        }

        ArrayList<Object> mergedItems = new ArrayList<>();
        mergedItems.addAll(mergedProjects);
        mergedItems.addAll(mergedTaskItems);


        //create clear all cells request
        for (int i = 0; i < remoteProjectCount; i++) {
            Project clearProject = Project.newBlankProject();
            mergedItems.add(clearProject);
        }

        //create clear all cells request
        for (int i = 0; i < remoteTaskItemCount; i++) {
            Project clearProject = Project.newBlankProject();
            mergedItems.add(clearProject);
        }

        //replace the Projects
        if (remoteProjectCount > 0) {
            SQLDataStore.getInstance().deleteAllProjects();
            SQLDataStore.getInstance().addProjects(mergedProjects);
        }
        //replace the TaskItems
        if (remoteTaskItemCount > 0) {
            SQLDataStore.getInstance().deleteAllTaskItems();
            SQLDataStore.getInstance().addTaskItems(mergedTaskItems);
        }

        if (mergedItems.size() > 0) {
            SyncManager.getInstance().startSync(this, getUserCredentials(), mergedItems);
        } else {
            LogUtils.logD(TAG, "Already up to date");
            onSyncComplete();
        }
    }

    private void onSetupValidationComplete() {
        new LoadProjectsTask(this, getUserCredentials(), this).execute();
    }


    private void onSyncComplete() {
        Toast.makeText(this, "Syncing completed successfully", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void onSyncFailed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onSyncStart() {

    }

    @Override
    public void onSyncCompleteWithSucess() {
        onSyncComplete();
    }

    @Override
    public void onSyncCompleteFailed(String errorMsg) {
        onDisplayInfo("Couldn't finish Sync");
        onSyncFailed();
    }

    @Override
    protected void onDestroy() {
        SyncManager.getInstance().stopListening(this);
        super.onDestroy();
    }
}
