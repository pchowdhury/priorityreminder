package com.phoenix2k.priorityreminder.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.phoenix2k.priorityreminder.DataStore;
import com.phoenix2k.priorityreminder.model.Project;
import com.phoenix2k.priorityreminder.model.TaskItem;

import java.util.ArrayList;

/**
 * Created by Pushpan on 22/03/17.
 */

public class SQLDataStore {
    private static SQLDataStore mInstance;
    private DbHelper mDbHelper;

    private SQLDataStore(Context context) {
        this.mDbHelper = new DbHelper(context);
    }

    public ArrayList<Project> getProjects(String[] ids, DataStore.SortType sortType) {
        StringBuffer query = new StringBuffer("SELECT * FROM " + Project.TAG);
        String orderBy = null;
        if (sortType != null) {
            switch (sortType) {
                case Alphabetic:
                    orderBy = Project.Column.TITLE.name();
                    break;
                case Chronological:
                    orderBy = Project.Column.ID.name();
                    break;
                case Index:
                    orderBy = Project.Column.ITEM_INDEX.name();
                    break;
            }
        }

        if (ids != null && ids.length > 0) {
            query.append(" WHERE");
            for (int i = 0; i < ids.length; i++) {
                if (i != 0) {
                    query.append(" OR");
                }
                query.append(" " + Project.Column.ID.name() + " = " + ids[i]);
            }
        }

        if (orderBy != null) {
            query.append(" ORDERBY " + orderBy);
        }

        ArrayList<Project> projects = readProjects(mDbHelper.getQueryResults(query.toString()));
        return projects;
    }

    public ArrayList<TaskItem> getTaskItems(String projectId, TaskItem.QuadrantType quarter, DataStore.SortType sortType) {
        StringBuffer query = new StringBuffer("SELECT * FROM " + TaskItem.TAG);
        String orderBy = null;
        if (sortType != null) {
            switch (sortType) {
                case Alphabetic:
                    orderBy = TaskItem.Column.TITLE.name();
                    break;
                case Chronological:
                    orderBy = TaskItem.Column.ID.name();
                    break;
                case Index:
                    orderBy = TaskItem.Column.ITEM_INDEX.name();
                    break;
            }
        }
        if (projectId != null) {
            query.append(" WHERE " + TaskItem.Column.PROJECT_ID + " = " + projectId);
        }
        if (quarter != null) {
            if (projectId == null) {
                query.append(" WHERE ");
            }
            query.append("  " + TaskItem.Column.QUARTER + " = " + quarter.ordinal());

        }
        if (orderBy != null) {
            query.append(" ORDERBY " + orderBy);
        }
        ArrayList<TaskItem> taskItems = readTaskItem(mDbHelper.getQueryResults(query.toString()));
        return taskItems;
    }

    public ArrayList<Project> getAllProjects() {
        ArrayList<Project> projects = readProjects(mDbHelper.getQueryResults("SELECT * from " + Project.TAG));
        return projects;
    }

    private ArrayList<Project> readProjects(Cursor cursor) {
        ArrayList<Project> projects = new ArrayList<>();
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        projects.add(Project.readProjectFromCursor(cursor));
                    } while (cursor.moveToNext());
                }
            }
        }
        return projects;
    }

    private ArrayList<TaskItem> readTaskItem(Cursor cursor) {
        ArrayList<TaskItem> taskItems = new ArrayList<>();
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        taskItems.add(TaskItem.readTaskItemFromCursor(cursor));
                    } while (cursor.moveToNext());
                }
            }
        }
        return taskItems;
    }

    public void addProjects(ArrayList<Project> projects) {
        ArrayList<ContentValues> list = new ArrayList<>();
        for (Project project : projects) {
            list.add(Project.getProjectContentValues(project));
        }
        ArrayList<Long> ids = mDbHelper.bulkInsertValues(Project.TAG, list);
        for (int i = 0; i < projects.size(); i++) {
            Project project = projects.get(i);
            project.mId = ids.get(i);
            projects.remove(project);
        }
    }

    public void addTaskItems(ArrayList<TaskItem> taskItems) {
        ArrayList<ContentValues> list = new ArrayList<>();
        for (TaskItem taskItem : taskItems) {
            list.add(TaskItem.getTaskItemContentValues(taskItem));
        }
        ArrayList<Long> ids = mDbHelper.bulkInsertValues(TaskItem.TAG, list);
        for (int i = 0; i < taskItems.size(); i++) {
            TaskItem taskItem = taskItems.get(i);
            taskItem.mId = ids.get(i);
        }
    }

    public void updateProjects(ArrayList<Project> projects) {
        ArrayList<ContentValues> list = new ArrayList<>();
        for (Project project : projects) {
            list.add(Project.getProjectContentValues(project));
        }
        ArrayList<String> ids = new ArrayList<>();
        for (int i = 0; i < projects.size(); i++) {
            ids.add(projects.get(i).mId + "");
        }
        mDbHelper.bulkUpdateValues(Project.TAG, Project.Column.ID.name(), ids, list);
    }

    public void updateTaskItems(ArrayList<TaskItem> taskItems) {
        ArrayList<ContentValues> list = new ArrayList<>();
        for (TaskItem project : taskItems) {
            list.add(TaskItem.getTaskItemContentValues(project));
        }
        ArrayList<String> ids = new ArrayList<>();
        for (int i = 0; i < taskItems.size(); i++) {
            ids.add(taskItems.get(i).mId + "");
        }
        mDbHelper.bulkUpdateValues(TaskItem.TAG, TaskItem.Column.ID.name(), ids, list);
    }

    public void updateItems(ArrayList<Object> items) {
        ArrayList<Project> projectsAdd = new ArrayList<>();
        ArrayList<TaskItem> taskItemsAdd = new ArrayList<>();
        ArrayList<Project> projectsUpdate = new ArrayList<>();
        ArrayList<TaskItem> taskItemsUpdate = new ArrayList<>();
        ArrayList<Project> projectsDeleted = new ArrayList<>();
        ArrayList<TaskItem> taskItemsDeleted = new ArrayList<>();
        for (Object obj : items) {
            if (obj instanceof Project) {
                Project project = (Project) obj;
                if (project.mId == -1) {
                    projectsAdd.add(project);
                } else if (project.mMarkDeleted) {
                    projectsDeleted.add(project);
                } else {
                    projectsUpdate.add(project);
                }
            } else if (obj instanceof TaskItem) {
                TaskItem taskItem = (TaskItem) obj;
                if (taskItem.mId == -1) {
                    taskItemsAdd.add(taskItem);
                } else if (taskItem.mMarkDeleted) {
                    taskItemsDeleted.add(taskItem);
                } else {
                    taskItemsUpdate.add(taskItem);
                }
            }
        }

        if (projectsAdd.size() > 0) {
            addProjects(projectsAdd);
            items.removeAll(projectsAdd);
        }
        if (projectsUpdate.size() > 0) {
            updateProjects(projectsUpdate);
            items.removeAll(projectsUpdate);
        }
        if (projectsDeleted.size() > 0) {
            deleteProjects(projectsDeleted);
            items.removeAll(projectsDeleted);
        }
        if (taskItemsAdd.size() > 0) {
            addTaskItems(taskItemsAdd);
            items.removeAll(taskItemsAdd);
        }
        if (taskItemsUpdate.size() > 0) {
            updateTaskItems(taskItemsUpdate);
            items.removeAll(taskItemsUpdate);
        }
        if (taskItemsDeleted.size() > 0) {
            deleteTaskItems(taskItemsDeleted);
            items.removeAll(taskItemsDeleted);
        }
    }

    public void deleteProjects(ArrayList<Project> projects) {
        ArrayList<String> ids = new ArrayList<>();
        for (int i = 0; i < projects.size(); i++) {
            ids.add(projects.get(i).mId + "");
        }
        mDbHelper.bulkDeleteItems(Project.TAG, Project.Column.ID.name(), ids);
    }

    public void deleteTaskItems(ArrayList<TaskItem> taskItems) {
        ArrayList<String> ids = new ArrayList<>();
        for (int i = 0; i < taskItems.size(); i++) {
            ids.add(taskItems.get(i).mId + "");
        }
        mDbHelper.bulkDeleteItems(TaskItem.TAG, TaskItem.Column.ID.name(), ids);
    }

    public boolean deleteTaskItems(TaskItem taskItem) {
        return mDbHelper.deleteTaskItem(taskItem.mId) != 0;
    }

    public static void init(Context context) {
        mInstance = new SQLDataStore(context);
    }

    public static SQLDataStore getInstance() {
        return mInstance;
    }

    public static void deInit() {
        mInstance = null;
    }
}
