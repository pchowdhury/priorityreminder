package com.phoenix2k.priorityreminder.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.phoenix2k.priorityreminder.model.Project;
import com.phoenix2k.priorityreminder.model.TaskItem;
import com.phoenix2k.priorityreminder.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pushpan on 22/03/17.
 */

public class DbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FeedReader.db";

    protected DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getCreateProjectQuery());
        db.execSQL(getCreateTaskItemQuery());
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
//        db.execSQL(getDropTableQuery("Project"));
//        db.execSQL(getDropTableQuery("TaskItem"));
        onCreate(db);
    }


    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    private String getCreateProjectQuery() {
        String query = "CREATE TABLE Project (";
        for (int i = 0; i < Project.Column.values().length; i++) {
            Project.Column col = Project.Column.values()[i];
            String colStr = col.name();
            if (i == 0) {
                colStr += " INTEGER PRIMARY KEY";
            } else {
                colStr = ", " + colStr + " TEXT";
            }
            query += colStr;
        }
        query += ")";
        return query;
    }

    private String getCreateTaskItemQuery() {
        String query = "CREATE TABLE TaskItem (";
        for (int i = 0; i < TaskItem.Column.values().length; i++) {
            TaskItem.Column col = TaskItem.Column.values()[i];
            String colStr = col.name();
            if (i == 0) {
                colStr += " INTEGER PRIMARY KEY";
            } else {
                colStr = ", " + colStr + " TEXT";
            }
            query += colStr;
        }
        query += ")";
        return query;
    }

    private String getDropTableQuery(String table) {
        return "DROP TABLE IF EXISTS " + table;
    }


    private boolean insertProject(Project project) {
        SQLiteDatabase db = this.getWritableDatabase();
        Long id = db.insert(Project.TAG, null, Project.getProjectContentValues(project));
        if (id != -1) {
            project.mId = id;
            return true;
        }
        return false;
    }

    private boolean insertTaskItem(TaskItem taskItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insert(TaskItem.TAG, null, TaskItem.getTaskItemContentValues(taskItem));
        if (id != -1) {
            taskItem.mId = id;
            return true;
        } else {
            return false;
        }
    }

    protected Cursor getQueryResults(String quary) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(quary, null);
        return res;
    }

    protected int numberOfRows(String table) {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, table);
        return numRows;
    }

    protected boolean updateProject(Project project) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.update(Project.TAG, Project.getProjectContentValues(project), Project.Column.ID + " = ? ", new String[]{Long.toString(project.mId)});
        return (rows != -1);
    }

    protected boolean updateTaskItem(TaskItem taskItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.update(Project.TAG, TaskItem.getTaskItemContentValues(taskItem), TaskItem.Column.ID + " = ? ", new String[]{Long.toString(taskItem.mId)});
        return (rows != -1);
    }

    protected Integer deleteProject(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Project.TAG,
                Project.Column.ID.name() + " = ? ",
                new String[]{Long.toString(id)});
    }

    protected Integer deleteTaskItem(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TaskItem.TAG,
                TaskItem.Column.ID.name() + " = ? ",
                new String[]{Long.toString(id)});
    }

    protected ArrayList<Long> bulkInsertValues(String table, List<ContentValues> list) {
        ArrayList<Long> ids = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (ContentValues set : list) {
                ids.add(db.insert(table, null, set));
            }
            db.setTransactionSuccessful();
        } catch(SQLException e){
            LogUtils.printException(e);
        }finally {
            db.endTransaction();
        }
        return ids;
    }

    protected boolean bulkUpdateValues(String table, String idName, List<String> ids, List<ContentValues> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (int i = 0; i < list.size(); i++) {
                ContentValues set = list.get(i);
                db.update(table, set, idName + " = ?", new String[]{ids.get(i) + ""});
            }
            db.setTransactionSuccessful();
        }catch(SQLException e){
            LogUtils.printException(e);
            return false;
        } finally {
            db.endTransaction();
        }
        return true;
    }

    protected boolean bulkDeleteItems(String table, String idName, List<String> ids) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (int i = 0; i < ids.size(); i++) {
                db.delete(table, idName + " = ?", new String[]{ids.get(i) + ""});
            }
            db.setTransactionSuccessful();
        } catch(SQLException e){
            LogUtils.printException(e);
            return false;
        }finally {
            db.endTransaction();
        }
        return true;
    }
}


