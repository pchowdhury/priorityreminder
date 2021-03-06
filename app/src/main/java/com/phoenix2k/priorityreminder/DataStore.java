package com.phoenix2k.priorityreminder;

import android.content.Context;
import android.view.DragEvent;
import android.view.View;

import com.phoenix2k.priorityreminder.model.Project;
import com.phoenix2k.priorityreminder.model.TaskItem;

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

    public enum SortType {
        Index,
        Alphabetic,
        Chronological
    }

    ;

    public static DataStore getInstance() {
        if (mInstance == null) {
            mInstance = new DataStore();
        }
        return mInstance;
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
            mNewProject.mPosition = getLastProjectPosition() + 1 + "";
        }
    }

    public Project getNewProject() {
        return mNewProject;
    }

    public void confirmSaveNewProject() {
        getProjects().add(getNewProject());
        setCurrentProject(getNewProject());
        setNewProject(null);
    }

    public int getLastProjectPosition() {
        return mProjects.size();
    }

    public int getLastProjectIndex() {
        if (mProjects.size() > 0) {
            return mProjects.get(mProjects.size() - 1).mIndex;
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

    public void setTasks(ArrayList<TaskItem> mTasks) {
        this.mTasks = mTasks;
        ArrayList<TaskItem> values = new ArrayList<>(mTasks);
        for (Project project : getProjects()) {
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

    public int getLastTaskPosition() {
        return getTasks().size();
    }

    public void confirmSaveTaskItem() {
        ArrayList<TaskItem> list = mCurrentProject.getTaskListForQuadrant(mCurrentTaskItem.mQuadrantType);
        list.add(mCurrentTaskItem);
        //add to main task list to update the new position
        mTasks.add(mCurrentTaskItem);
        setCurrentTaskItem(null);
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

    ArrayList<Comparator<TaskItem>> mSortCompartor = new ArrayList() {{
        add(new Comparator<TaskItem>() {
            @Override
            public int compare(TaskItem o1, TaskItem o2) {
                return new Integer(o1.mIndex).compareTo(new Integer(o2.mIndex));
            }
        });
        add(new Comparator<TaskItem>() {
            @Override
            public int compare(TaskItem o1, TaskItem o2) {
                return new Integer(o1.mTitle).compareTo(new Integer(o2.mTitle));
            }
        });
        add(new Comparator<TaskItem>() {
            @Override
            public int compare(TaskItem o1, TaskItem o2) {
                return new Integer(o1.mId).compareTo(new Integer(o2.mId));
            }
        });
    }};


}
