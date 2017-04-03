package com.phoenix2k.priorityreminder.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.phoenix2k.priorityreminder.utils.DataUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pushpan on 06/02/17.
 */

public class TaskItem extends PREntity{
    public static final String TAG = "TaskItem";

    public enum Column {
        ID,
        PROJECT_ID,
        TITLE,
        ITEM_INDEX,
        QUARTER,
        DESCRIPTION,
        ICON,
        STARTS_ON,
        DUE_ON,
        REPEAT,
        CREATED_ON,
        UPDATED_ON
    }

    public enum QuadrantType {
        Q1_OR_UPCOMING, Q2_OR_DUE, Q3_OR_IN_PROGRESS, Q4_OR_COMPLETED
    }

    public enum RepeatType {
        None,
        Once,
        Daily,
        Weekly,
        Monthly,
        Yearly
    }

    public Long mProjectId;
    public QuadrantType mQuadrantType;
    public String mDescription;
    public int mIcon;
    public long mStartTime;
    public long mDueTime;

    public RepeatType mRepeatType = RepeatType.None;

    public static TaskItem newTaskItem() {
        TaskItem item = new TaskItem();
        item.mDescription = "";
        item.mIcon = 0;
        item.mQuadrantType = QuadrantType.Q1_OR_UPCOMING;
        item.mStartTime = 0;
        item.mDueTime = 0;
        item.mRepeatType = RepeatType.None;
        return item;
    }

    public static TaskItem newBlankTaskItem() {
        TaskItem item = new TaskItem();
        item.mId = Long.valueOf(-1);
        return item;
    }


    public static TaskItem getTaskItemFrom(List<Object> values) {
        if (values != null) {
            TaskItem taskItem = TaskItem.newTaskItem();
            for (int i = 0; i < values.size(); i++) {
                String value = (String) values.get(i);
                switch (Column.values()[i]) {
                    case ID:
                        taskItem.mId = Long.valueOf(value);
                        break;
                    case PROJECT_ID:
                        taskItem.mProjectId = Long.valueOf(value);
                        break;
                    case TITLE:
                        taskItem.mTitle = value;
                        break;
                    case ITEM_INDEX:
                        taskItem.mIndex = DataUtils.parseIntValue(value);
                        break;
                    case QUARTER:
                        taskItem.mQuadrantType = QuadrantType.values()[Integer.valueOf(value)];
                        break;
                    case DESCRIPTION:
                        taskItem.mDescription = value;
                        break;
                    case ICON:
                        taskItem.mIcon = Integer.valueOf(value);
                        break;
                    case STARTS_ON:
                        taskItem.mStartTime = Long.valueOf(value);
                        break;
                    case DUE_ON:
                        taskItem.mDueTime = Long.valueOf(value);
                        break;
                    case REPEAT:
                        taskItem.mRepeatType = RepeatType.values()[Integer.valueOf(value)];
                        break;
                    case CREATED_ON:
                        taskItem.mCreatedOn = DataUtils.parseLongValue(value);
                        break;
                    case UPDATED_ON:
                        taskItem.mUpdatedOn = DataUtils.parseLongValue(value);
                        break;
                }
            }
            return taskItem;
        }
        return null;
    }


    public static List<List<Object>> getTaskItemWriteback(final TaskItem taskItem) {
        List<List<Object>> values = new ArrayList<>();
        ArrayList<Object> taskItemValues = new ArrayList() {{
            add(taskItem.mId + "");
            add(taskItem.mProjectId + "");
            add(taskItem.mTitle + "");
            add(taskItem.mIndex + "");
            add(taskItem.mQuadrantType.ordinal() + "");
            add(taskItem.mDescription + "");
            add(taskItem.mIcon + "");
            add(taskItem.mStartTime + "");
            add(taskItem.mDueTime + "");
            add(taskItem.mRepeatType.ordinal() + "");
            add(taskItem.mCreatedOn + "");
            add(taskItem.mUpdatedOn + "");
        }};
        values.add(taskItemValues);
        return values;
    }

    @Override
    public String toString() {
        return
                "{\nId:" + mId +
                        "\nmProjectId:" + mProjectId +
                        "\nmTitle:" + mTitle +
                        "\nmQuadrantType:" + mQuadrantType.name() +
                        "\nmIndex:" + mIndex +
                        "\nmDescription:" + mDescription +
                        "\nmIcon:" + mIcon +
                        "\nmStartTime:" + mStartTime +
                        "\nmDueTime:" + mDueTime +
                        "\nmRepeatType:" + mRepeatType.name() +
                        "\nmCreatedOn:" + mCreatedOn +
                        "\nmUpdatedOn:" + mUpdatedOn +
                        "\n}";
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put(Column.ID.name(), mId);
            json.put(Column.PROJECT_ID.name(), mProjectId);
            json.put(Column.TITLE.name(), mTitle);
            json.put(Column.ITEM_INDEX.name(), mIndex);
            json.put(Column.QUARTER.name(), mQuadrantType.ordinal());
            json.put(Column.DESCRIPTION.name(), mDescription);
            json.put(Column.ICON.name(), mIcon);
            json.put(Column.STARTS_ON.name(), mStartTime + "");
            json.put(Column.DUE_ON.name(), mDueTime + "");
            json.put(Column.REPEAT.name(), mRepeatType.ordinal());
            json.put(Column.CREATED_ON.name(), mCreatedOn);
            json.put(Column.UPDATED_ON.name(), mUpdatedOn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static TaskItem getTaskFromJSON(JSONObject json) {
        TaskItem taskItem = new TaskItem();
        try {
            if (json.has(Column.ID.name())) {
                taskItem.mId = json.getLong(Column.ID.name());
            }
            if (json.has(Column.PROJECT_ID.name())) {
                taskItem.mProjectId = json.getLong(Column.PROJECT_ID.name());
            }
            if (json.has(Column.TITLE.name())) {
                taskItem.mTitle = json.getString(Column.TITLE.name());
            }
            if (json.has(Column.ITEM_INDEX.name())) {
                taskItem.mIndex = json.getInt(Column.ITEM_INDEX.name());
            }
            if (json.has(Column.QUARTER.name())) {
                taskItem.mQuadrantType = QuadrantType.values()[json.getInt(Column.QUARTER.name())];
            }
            if (json.has(Column.DESCRIPTION.name())) {
                taskItem.mDescription = json.getString(Column.DESCRIPTION.name());
            }
            if (json.has(Column.ICON.name())) {
                taskItem.mIcon = json.getInt(Column.ICON.name());
            }
            if (json.has(Column.STARTS_ON.name())) {
                taskItem.mStartTime = json.getLong(Column.STARTS_ON.name());
            }
            if (json.has(Column.DUE_ON.name())) {
                taskItem.mDueTime = json.getLong(Column.DUE_ON.name());
            }
            if (json.has(Column.REPEAT.name())) {
                taskItem.mRepeatType = RepeatType.values()[json.getInt(Column.REPEAT.name())];
            }
            if (json.has(Column.CREATED_ON.name())) {
                taskItem.mCreatedOn = json.getLong(Column.CREATED_ON.name());
            }
            if (json.has(Column.UPDATED_ON.name())) {
                taskItem.mUpdatedOn = json.getLong(Column.UPDATED_ON.name());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return taskItem;
    }

    public static ContentValues getTaskItemContentValues(TaskItem taskItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Column.PROJECT_ID.name(), taskItem.mProjectId);
        contentValues.put(Column.TITLE.name(), taskItem.mTitle);
        contentValues.put(Column.ITEM_INDEX.name(), taskItem.mIndex);
        contentValues.put(Column.QUARTER.name(), taskItem.mQuadrantType.ordinal());
        contentValues.put(Column.DESCRIPTION.name(), taskItem.mDescription);
        contentValues.put(Column.ICON.name(), taskItem.mIcon);
        contentValues.put(Column.STARTS_ON.name(), taskItem.mStartTime);
        contentValues.put(Column.DUE_ON.name(), taskItem.mDueTime);
        contentValues.put(Column.REPEAT.name(), taskItem.mRepeatType.ordinal());
        contentValues.put(Column.CREATED_ON.name(), taskItem.mCreatedOn);
        contentValues.put(Column.UPDATED_ON.name(), taskItem.mUpdatedOn);
        return contentValues;
    }

    public static TaskItem readTaskItemFromCursor(Cursor cursor) {
        TaskItem item = new TaskItem();
        item.mId = cursor.getLong(cursor.getColumnIndex(Column.ID.name()));
        item.mProjectId = cursor.getLong(cursor.getColumnIndex(Column.PROJECT_ID.name()));
        item.mTitle = cursor.getString(cursor.getColumnIndex(Column.TITLE.name()));
        item.mIndex = cursor.getInt(cursor.getColumnIndex(Column.ITEM_INDEX.name()));
        item.mDescription = cursor.getString(cursor.getColumnIndex(Column.DESCRIPTION.name()));
        item.mIcon = cursor.getInt(cursor.getColumnIndex(Column.ICON.name()));
        item.mQuadrantType = QuadrantType.values()[cursor.getInt(cursor.getColumnIndex(Column.QUARTER.name()))];
        item.mStartTime = cursor.getLong(cursor.getColumnIndex(Column.STARTS_ON.name()));
        item.mDueTime = cursor.getLong(cursor.getColumnIndex(Column.DUE_ON.name()));
        item.mRepeatType = RepeatType.values()[cursor.getInt(cursor.getColumnIndex(Column.REPEAT.name()))];
        item.mCreatedOn = cursor.getLong(cursor.getColumnIndex(Column.CREATED_ON.name()));
        item.mUpdatedOn = cursor.getLong(cursor.getColumnIndex(Column.UPDATED_ON.name()));
        return item;
    }


}
