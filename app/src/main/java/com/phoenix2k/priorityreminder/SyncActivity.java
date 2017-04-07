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
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pushpan on 05/02/17.
 */

public class SyncActivity extends BasicCommunicationActivity {
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
            case Sheet_Load_Projects_Metadata:
                if (result != null) {
                    mRemoteProjects = (ArrayList<Project>) result;
                    new LoadAllTasks(this, getUserCredentials(), this);
                } else {
                    LogUtils.logI(TAG, "Error loading projects");
                    onDisplayInfo("Error loading projects");
                }
                break;
            case Sheet_Load_All_Tasks:
                if (result != null) {
                    mRemoteTasks = (ArrayList<TaskItem>) result;
                    mergeWithRemote(mRemoteProjects, mRemoteTasks);
                } else {
                    LogUtils.logI(TAG, "Error loading projects");
                    onDisplayInfo("Error loading projects");
                }
                break;
            case Sheet_Push_Updates:
                if (result != null) {
                    boolean isUpdated = (Boolean) result;
                    if (isUpdated) {
                        onSyncComplete();
                    } else {
                        LogUtils.logI(TAG, "Couldn't finish Sync");
                        onDisplayInfo("Couldn't finish Sync");
                    }
                } else {
                    LogUtils.logI(TAG, "Error Syncing");
                    onDisplayInfo("Error Syncing");
                }
                break;
        }
    }


    public void mergeWithRemote(ArrayList<Project> remoteProjects, ArrayList<TaskItem> remoteTasks) {

        //STEP 1: get latest updated tiem for both

//        Comparator<Project> projectComparator = new Comparator<Project>() {
//            @Override
//            public int compare(Project p1, Project p2) {
//                return Long.valueOf(p1.mUpdatedOn).compareTo(Long.valueOf(p2.mUpdatedOn));
//            }
//        };
//
//        Comparator<TaskItem> taskItemComparator = new Comparator<TaskItem>() {
//            @Override
//            public int compare(TaskItem p1, TaskItem p2) {
//                return Long.valueOf(p1.mUpdatedOn).compareTo(Long.valueOf(p2.mUpdatedOn));
//            }
//        };


        if (remoteProjects != null) {
            remoteProjects = new ArrayList<>();
        }
        if (remoteTasks != null) {
            remoteTasks = new ArrayList<>();
        }

        int remoteProjectCount = remoteProjects.size();
        int remoteTaskItemCount = remoteTasks.size();
        //assuming remote and local maintains correct time;

        ArrayList<Project> localProjects = SQLDataStore.getInstance().getAllProjects();
        ArrayList<TaskItem> localTasks = SQLDataStore.getInstance().getTaskItems(null, null, null);

        if (localProjects != null) {
            localProjects = new ArrayList<>();
        }
        if (localTasks != null) {
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

        while (remoteProjects.size() > 0) {
            Project remoteProject = remoteProjects.get(0);
            Project localProject = localProjectMap.get(remoteProject.mId + "");
            if (localProject != null) {
                //project is common, keep the latest project and reject the old
                if (localProject.mUpdatedOn >= remoteProject.mUpdatedOn) {
                    mergedProjects.add(localProject);
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
        }

        //STEP 3b: Compare taskItem

        while (remoteTasks.size() > 0) {
            TaskItem remoteTaskItem = remoteTasks.get(0);
            TaskItem localTaskItem = localTaskItemMap.get(remoteTaskItem.mId + "");
            if (localTaskItem != null) {
                //task is common, keep the latest project and reject the old
                if (localTaskItem.mUpdatedOn >= remoteTaskItem.mUpdatedOn) {
                    mergedTaskItems.add(localTaskItem);
                } else {
                    mergedTaskItems.add(remoteTaskItem);
                }
                remoteTasks.remove(remoteTaskItem);
                localTasks.remove(localTaskItem);
            } else {
                //taskItem only present in remote
                mergedTaskItems.add(remoteTaskItem);
                remoteTasks.remove(remoteTaskItem);
            }
        }

        //add remaining local projects
        if (localTasks.size() > 0) {
            mergedTaskItems.addAll(localTasks);
            localTasks.clear();
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


        SQLDataStore.getInstance().deleteAllProjects();
        SQLDataStore.getInstance().deleteAllTaskItems();
        SQLDataStore.getInstance().addProjects(mergedProjects);
        SQLDataStore.getInstance().addTaskItems(mergedTaskItems);
        SyncManager.getInstance().startSync(this, getUserCredentials(), mergedItems);
    }

    private void onSetupValidationComplete() {
        new LoadProjectsTask(this, getUserCredentials(), this);
    }


    private void onSyncComplete() {
        Toast.makeText(this, "Project synced", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void onSyncFailed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
