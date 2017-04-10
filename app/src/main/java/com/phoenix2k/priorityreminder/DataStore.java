package com.phoenix2k.priorityreminder;

import android.content.Context;
import android.view.View;

import com.phoenix2k.priorityreminder.manager.PRNotificationManager;
import com.phoenix2k.priorityreminder.model.PREntity;
import com.phoenix2k.priorityreminder.model.Project;
import com.phoenix2k.priorityreminder.model.TaskItem;
import com.phoenix2k.priorityreminder.pref.PreferenceHelper;
import com.phoenix2k.priorityreminder.receiver.AlarmReceiver;
import com.phoenix2k.priorityreminder.store.SQLDataStore;
import com.phoenix2k.priorityreminder.utils.DataUtils;
import com.phoenix2k.priorityreminder.utils.IDGenerator;
import com.phoenix2k.priorityreminder.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import static com.phoenix2k.priorityreminder.DataStore.SortType.Index;

/**
 * Created by Pushpan on 08/01/17.
 */

public class DataStore {
    private final String TAG = "DataStore";
    public static final String APP_FOLDER_NAME = "Priority Reminder";
    public static final String PROJECT_FILE_NAME = "Project";
    public static final String DATA_FILE_NAME = "Data";
    private static DataStore mInstance;
    private ArrayList<Project> mProjects = new ArrayList<>();
    private ArrayList<TaskItem> mTasks = new ArrayList<>();
    private Project mNewProject;
    private Project mCurrentProject;
    private TaskItem mCurrentTaskItem;
    private View.OnDragListener mDragListener;
    private HashMap<String, Project> mProjectsMap = new HashMap<>();
    private ArrayList<Object> mUpdates = new ArrayList<>();
    private SortType mSortType = Index;
    private int mCurrentProjectIndex = 0;
    private ArrayList<Integer> mIconResourceIdList = new ArrayList<>();

    public enum SortType {
        Index,
        Alphabetic,
        Chronological
    }

    ;

    public int getCurrentProjectIndex() {
        return mCurrentProjectIndex;
    }

    public void setCurrentProjectIndex(int mProjectPosition) {
        this.mCurrentProjectIndex = mProjectPosition;
    }

    public int getIconResId(int position) {
        return mIconResourceIdList.get(position);
    }

    public ArrayList<Integer> getIconResourceIdList() {
        return mIconResourceIdList;
    }

    public void setIconResourceIdList(ArrayList<Integer> mIconResourceIdList) {
        this.mIconResourceIdList = mIconResourceIdList;
    }

    public static DataStore getInstance() {
        if (mInstance == null) {
            mInstance = new DataStore();
        }
        return mInstance;
    }

    public static void deInit() {
        mInstance.mProjects.clear();
        mInstance.mTasks.clear();
        mInstance = null;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.mProjects = projects;
        mProjectsMap.clear();
        for (Project project : projects) {
            mProjectsMap.put(project.mId, project);
        }
    }

    public ArrayList<Project> getProjects() {
        return mProjects;
    }

    public void setNewProject(Project project) {
        mNewProject = project;
        if (mNewProject != null) {
            mNewProject.mIndex = getLastProjectIndex() + 1;
        }
    }

    public Project getNewProject() {
        return mNewProject;
    }

    public void confirmSaveProject() {
        if (mNewProject != null) {
            setCurrentProject(mNewProject);
            mNewProject = null;
        }
        addToUpdate(getCurrentProject());
        SQLDataStore.getInstance().updateItems(mUpdates);
        setProjects(SQLDataStore.getInstance().getAllProjects());
        mCurrentProjectIndex = mProjects.size() - 1;
    }

    public int getLastProjectIndex() {
        ArrayList<Project> projects = new ArrayList<>(mProjects);
        Collections.sort(projects, mSortCompartor.get(SortType.Index.ordinal()));
        if (projects.size() > 0) {
            return projects.get(projects.size() - 1).mIndex;
        } else {
            return -1;
        }
    }

    public void setCurrentProject(Project project) {
        this.mCurrentProject = project;
    }

    public Project getCurrentProject() {
        return mCurrentProject;
    }

    public void applyChanges() {

    }

    public TaskItem getCurrentTaskItem() {
        return mCurrentTaskItem;
    }

    public void setCurrentTaskItem(TaskItem mCurrentTaskItem) {
        this.mCurrentTaskItem = mCurrentTaskItem;
    }

    public ArrayList<TaskItem> getTasks() {
        return mTasks;
    }

    public void setTasks(ArrayList<TaskItem> tasks) {
        this.mTasks = tasks;
        ArrayList<TaskItem> values = new ArrayList<>(mTasks);
        for (Project project : getProjects()) {
            project.removeAllTasks();
            project.addfromTaskList(values);
        }
        for (Project project : getProjects()) {
            sortTasks(project, mSortType);
        }
    }

    private void sortTasks(Project project, SortType index) {
        for (TaskItem.QuadrantType q : TaskItem.QuadrantType.values()) {
            ArrayList<TaskItem> list = project.getTaskListForQuadrant(q);
            Collections.sort(list, mSortCompartor.get(index.ordinal()));
        }
    }


    public void deleteProject() {
        Project project = getCurrentProject();
        //set the new index of all projects
        ArrayList<Project> sortedProjects = new ArrayList<>(mProjects);
        Collections.sort(sortedProjects, mSortCompartor.get(SortType.Index.ordinal()));

        int startIndex = project.mIndex;
        if (startIndex < sortedProjects.size()) {
            for (int i = startIndex + 1; i < sortedProjects.size(); i++) {
                sortedProjects.get(i).mIndex = sortedProjects.get(i).mIndex - 1;
                addToUpdate(sortedProjects.get(i));
            }
        }
        //remove the item
        mProjects.remove(project);
        project.mTrashed = true;
        addToUpdate(project);
        //Delete all tasks inside project
        ArrayList<TaskItem> tasksTobeDeleted = project.getAllTasks();
        for (int i = 0; i < tasksTobeDeleted.size(); i++) {
            TaskItem item = tasksTobeDeleted.get(i);
            mTasks.remove(item);
            item.mTrashed = true;
            addToUpdate(item);
        }
        setProjects(mProjects);
        setTasks(mTasks);
        if (mCurrentProjectIndex >= mProjects.size()) {
            mCurrentProjectIndex--;
            if (mCurrentProjectIndex < 0) {
                mCurrentProjectIndex = 0;
            }
        }
    }

    public void deleteTask(TaskItem item) {
        Project project = mProjectsMap.get(item.mProjectId);
        //set the new index of all items of the quadrant
        ArrayList<TaskItem> taskList = project.getTaskListForQuadrant(item.mQuadrantType);
        int startIndex = item.mIndex;
        if (startIndex < taskList.size()) {
            for (int i = startIndex + 1; i < taskList.size(); i++) {
                taskList.get(i).mIndex = taskList.get(i).mIndex - 1;
                addToUpdate(taskList.get(i));
            }
        }
        //remove the item
        mTasks.remove(item);
        item.mTrashed = true;
        addToUpdate(item);
        setTasks(mTasks);
    }

    public int getLastTaskPosition() {
        return getTasks().size();
    }

    public void confirmSaveTaskItem(boolean isNewItem) {
        ArrayList<TaskItem> list = mCurrentProject.getTaskListForQuadrant(mCurrentTaskItem.mQuadrantType);
        list.add(mCurrentTaskItem);
        Collections.sort(list, mSortCompartor.get(mSortType.ordinal()));
        //add to main task list to update the new position
        if (isNewItem) {
            mTasks.add(mCurrentTaskItem);
        }
        setCurrentTaskItem(null);
        setUpNotifications();
        DataStore.getInstance().validateTaskStatus();
        SQLDataStore.getInstance().updateItems(DataStore.getInstance().getUpdates());
    }

    public void setUpNotifications() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (BuildConfig.DEBUG) {
                    LogUtils.logD(TAG, "setUpNotifications");
                }
                clearAllAlertsConfigurations();
                for (int i = 0; i < mProjects.size(); i++) {
                    Project project = mProjects.get(i);
                    if (project.mProjectType == Project.ProjectType.State) {
                        ArrayList<TaskItem> list = new ArrayList();
                        for (TaskItem.QuadrantType quad : TaskItem.QuadrantType.values()) {
                            list.addAll(project.getTaskListForQuadrant(quad));
                        }
                        for (TaskItem task : list) {
                            if (task.mQuadrantType == TaskItem.QuadrantType.Q1_OR_UPCOMING) {
                                PRNotificationManager.getInstance().scheduleNotfication(task);
                            }
                        }
                    }
                }
            }
        }).start();

    }

    public void clearAllAlertsConfigurations() {
        PRNotificationManager.getInstance().clearAllAlarmNotifications();
        AlarmReceiver.clearAllNotifications(PRNotificationManager.getInstance().getContext());
        PreferenceHelper.clearNotification(PRNotificationManager.getInstance().getContext());
    }


    public TaskItem getTaskItemWithId(String taskId) {
        for (TaskItem.QuadrantType quadrantType : TaskItem.QuadrantType.values()) {
            ArrayList<TaskItem> items = mCurrentProject.getTaskListForQuadrant(quadrantType);
            for (TaskItem task : items) {
                if (task.mId.equals(taskId)) {
                    return task;
                }
            }
        }
        return null;
    }

    public void moveTaskItem(TaskItem item, TaskItem with) {
        Project project = mProjectsMap.get(item.mProjectId);
        if (project != null) {
            ArrayList<TaskItem> taskList = project.getTaskListForQuadrant(item.mQuadrantType);
            if (item.mQuadrantType == with.mQuadrantType) {
                //swap item index for same list
                if (with.mUpdatedOn != -1) {
                    //swaped with middle elements
                    int index = item.mIndex;
                    item.mIndex = with.mIndex;
                    with.mIndex = index;
                    addToUpdate(item).addToUpdate(with);
                } else {
                    //dragged to bottom
                    //remove the dragged item from the current position
                    taskList.remove(item);
                    //shift up all the items below the dragged item
                    int startIndex = item.mIndex;
                    if (startIndex < taskList.size()) {
                        for (int i = startIndex; i < taskList.size(); i++) {
                            taskList.get(i).mIndex = taskList.get(i).mIndex - 1;
                            addToUpdate(taskList.get(i));
                        }
                    }
                    //assign the new index to the dragged item and add it at the bottom
                    item.mIndex = taskList.size();
                    taskList.add(item);
                    addToUpdate(item);
                }
                //sort the list
                Collections.sort(taskList, mSortCompartor.get(mSortType.ordinal()));
            } else {
                //remove the dragged item from the current position
                taskList.remove(item);
                //shift up all the items below the dragged item
                int startIndex = item.mIndex;
                if (startIndex < taskList.size()) {
                    for (int i = startIndex; i < taskList.size(); i++) {
                        taskList.get(i).mIndex = taskList.get(i).mIndex - 1;
                        addToUpdate(taskList.get(i));
                    }
                }
                //sort the list
                Collections.sort(taskList, mSortCompartor.get(mSortType.ordinal()));

                //get the list where the dragged item is dropped
                taskList = project.getTaskListForQuadrant(with.mQuadrantType);
                //assign the new index for the dragged item
                item.mIndex = with.mIndex;
                //add the item to its new position
                taskList.add(with.mIndex, item);
                //assign the new quadrant type
                item.mQuadrantType = with.mQuadrantType;
                addToUpdate(item);
                //shift down all the items below the dragged item
                startIndex = (with.mIndex + 1);
                if (startIndex < taskList.size()) {
                    for (int i = startIndex; i < taskList.size(); i++) {
                        taskList.get(i).mIndex = taskList.get(i).mIndex + 1;
                        addToUpdate(taskList.get(i));
                    }
                }
                //sort the list
                Collections.sort(taskList, mSortCompartor.get(mSortType.ordinal()));
            }
        }
        SQLDataStore.getInstance().updateItems(DataStore.getInstance().getUpdates());
    }

    /**
     * Check all the state tasks for due dates. If any of them is already in due date then change the sate to due quadrant
     * and update
     */
    public void validateTaskStatus() {
        for (int i = 0; i < mProjects.size(); i++) {
            Project project = mProjects.get(i);
            if (project.mProjectType == Project.ProjectType.State) {
                ArrayList<TaskItem> list = new ArrayList();
                for (TaskItem.QuadrantType quad : TaskItem.QuadrantType.values()) {
                    list.addAll(project.getTaskListForQuadrant(quad));
                }
                for (TaskItem task : list) {
                    if (task.mQuadrantType == TaskItem.QuadrantType.Q1_OR_UPCOMING) {
                        long now = System.currentTimeMillis();
                        long startTime = task.mStartTime;
                        LogUtils.logI("Time Now", DataUtils.getTime(now));
                        LogUtils.logI("Time Start", DataUtils.getTime(startTime));
                        if (startTime != 0 && startTime < now) {
                            ArrayList<TaskItem> duelist = project.getTaskListForQuadrant(TaskItem.QuadrantType.Q2_OR_DUE);
                            TaskItem dummyTask = getNewTaskItemPlaceHolder();
                            dummyTask.mQuadrantType = TaskItem.QuadrantType.Q2_OR_DUE;
                            if (duelist.size() > 0) {
                                dummyTask.mIndex = duelist.size();
                            } else {
                                dummyTask.mIndex = 0;
                            }
                            moveTaskItem(task, dummyTask);
                        }
                    }
                }
            }
        }
    }

    public View.OnDragListener getDragListener() {
        return mDragListener;
    }

    public void setDragListener(View.OnDragListener listener) {
        this.mDragListener = listener;
    }

    public int getQuadrantColorFor(TaskItem item) {
        Project project = mProjectsMap.get(item.mProjectId);
        return project.mColorQuadrants.get(item.mQuadrantType);
    }

    public TaskItem getNewTaskItemPlaceHolder() {
        TaskItem taskItem = TaskItem.newTaskItem();
        /** If the item doesn't have updatedOn then its just a place holder for the last item*/
        taskItem.mUpdatedOn = -1;
        return taskItem;
    }

    public DataStore addToUpdate(Object updatedItem) {
        if (!mUpdates.contains(updatedItem)) {
            mUpdates.add(updatedItem);
        }
        return this;
    }

    public ArrayList<Object> getUpdates() {
        return mUpdates;
    }

    public void clearUpdates() {
        mUpdates.clear();
    }

    public void clearUpdatedItems(ArrayList items) {
        mUpdates.removeAll(items);
    }

    ArrayList<Comparator<PREntity>> mSortCompartor = new ArrayList() {{
        add(new Comparator<PREntity>() {
            @Override
            public int compare(PREntity o1, PREntity o2) {
                return new Integer(o1.mIndex).compareTo(new Integer(o2.mIndex));
            }
        });
        add(new Comparator<PREntity>() {
            @Override
            public int compare(PREntity o1, PREntity o2) {
                return new Integer(o1.mTitle).compareTo(new Integer(o2.mTitle));
            }
        });
        add(new Comparator<PREntity>() {
            @Override
            public int compare(PREntity o1, PREntity o2) {
                return Long.valueOf(o1.mId).compareTo(Long.valueOf(o2.mId));
            }
        });
    }};

    public boolean validateFirstProject(Context context) {
        if (mProjects.size() == 0) {
            Project project = Project.newProject(context);
            setNewProject(project);
            confirmSaveProject();
            return true;
        }
        return false;
    }

    public void reloadItems(Context context) {
        setProjects(SQLDataStore.getInstance().getAllProjects());
        validateFirstProject(context);
        setTasks(SQLDataStore.getInstance().getTaskItems(null, null, null));
    }


}
