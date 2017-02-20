package com.phoenix2k.priorityreminder.model;

import com.phoenix2k.priorityreminder.DataStore;
import com.phoenix2k.priorityreminder.utils.DataUtils;
import com.phoenix2k.priorityreminder.utils.IDGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pushpan on 06/02/17.
 */

public class TaskItem {

    public enum Column {
        ID,
        POSITION,
        PROJECT_ID,
        TITLE,
        INDEX,
        QUARTER,
        DESCRIPTION,
        STATUS,
        REPEAT,
        CREATED_ON,
        UPDATED_ON
    }

    public enum QuadrantType {
        Q1, Q2, Q3, Q4
    }

    public enum RepeatType {
        None,
        Once,
        Daily,
        Weekly,
        Monthly,
        Yearly
    }

    public enum Status {
        NotStarted,
        Started,
        Done,
        Postpone
    }

    public String mId;
    public String mPosition;
    public String mProjectId;
    public String mTitle;
    public int mIndex;
    public QuadrantType mQuadrantType;
    public String mDescription;

    public Status mStatus = Status.NotStarted;
    public RepeatType mRepeatType = RepeatType.None;

    public long mCreatedOn;
    public long mUpdatedOn;


    public static TaskItem newTaskItem() {
        TaskItem item = new TaskItem();
        item.mId = IDGenerator.generateUniqueId() + "";
        item.mPosition = DataStore.getInstance().getLastTaskPosition() + 1 + "";
        item.mTitle = "";
        item.mIndex = 0;
        item.mDescription = "";
        item.mStatus = Status.NotStarted;
        item.mQuadrantType = QuadrantType.Q1;
        item.mRepeatType = RepeatType.None;
        item.mCreatedOn = IDGenerator.generateUniqueId();
        item.mUpdatedOn = item.mCreatedOn;
        return item;
    }


    public static TaskItem getTaskItemFrom(List<Object> values) {
        if (values != null) {
            TaskItem taskItem = TaskItem.newTaskItem();
            for (int i = 0; i < values.size(); i++) {
                String value = (String) values.get(i);
                switch (Column.values()[i]) {
                    case ID:
                        taskItem.mId = value;
                        break;
                    case POSITION:
                        taskItem.mPosition = value;
                        break;
                    case PROJECT_ID:
                        taskItem.mProjectId = value;
                        break;
                    case TITLE:
                        taskItem.mTitle = value;
                        break;
                    case INDEX:
                        taskItem.mIndex = DataUtils.parseIntValue(value);
                        break;
                    case QUARTER:
                        taskItem.mQuadrantType = QuadrantType.values()[Integer.valueOf(value)];
                        break;
                    case DESCRIPTION:
                        taskItem.mDescription = value;
                        break;
                    case STATUS:
                        taskItem.mStatus = Status.values()[Integer.valueOf(value)];
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


    public  static List<List<Object>> getTaskItemWriteback(final TaskItem taskItem) {
        List<List<Object>> values = new ArrayList<>();
        ArrayList<Object> taskItemValues = new ArrayList() {{
            add(taskItem.mId + "");
            add(taskItem.mPosition + "");
            add(taskItem.mProjectId + "");
            add(taskItem.mTitle + "");
            add(taskItem.mIndex + "");
            add(taskItem.mQuadrantType.ordinal() + "");
            add(taskItem.mDescription + "");
            add(taskItem.mStatus.ordinal() + "");
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
                        "\nmPosition:" + mPosition +
                        "\nmProjectId:" + mProjectId +
                        "\nmTitle:" + mTitle +
                        "\nmQuadrantType:" + mQuadrantType.name() +
                        "\nmIndex:" + mIndex +
                        "\nmDescription:" + mDescription +
                        "\nmStatus:" + mStatus.name() +
                        "\nmRepeatType:" + mRepeatType.name() +
                        "\nmCreatedOn:" + mCreatedOn +
                        "\nmUpdatedOn:" + mUpdatedOn +
                        "\n}";
    }
}
