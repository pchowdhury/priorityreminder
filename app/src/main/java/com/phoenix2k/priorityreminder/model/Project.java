package com.phoenix2k.priorityreminder.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;

import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.utils.DataUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Pushpan on 06/02/17.
 */

public class Project extends PREntity {
    public static final String TAG = "Project";

    public enum Column {
        ID,
        TITLE,
        ITEM_INDEX,
        TYPE,
        Q1_TITLE,
        Q2_TITLE,
        Q3_TITLE,
        Q4_TITLE,
        Q1_COLOR,
        Q2_COLOR,
        Q3_COLOR,
        Q4_COLOR,
        CENTER_IN_PERCENT,
        CREATED_ON,
        UPDATED_ON,
        TRASHED
    }

    public enum ProjectType {
        Simple,
        State
    }

    public HashMap<TaskItem.QuadrantType, String> mTitleQuadrants = new HashMap<>();
    public HashMap<TaskItem.QuadrantType, Integer> mColorQuadrants = new HashMap<>();
    public ProjectType mProjectType = ProjectType.Simple;
    public Point mCenterInPercent = new Point();

    private HashMap<TaskItem.QuadrantType, ArrayList<TaskItem>> mQuadrants = new HashMap<>();

    public Project() {
        mQuadrants.put(TaskItem.QuadrantType.Q1_OR_UPCOMING, new ArrayList<TaskItem>());
        mQuadrants.put(TaskItem.QuadrantType.Q2_OR_DUE, new ArrayList<TaskItem>());
        mQuadrants.put(TaskItem.QuadrantType.Q3_OR_IN_PROGRESS, new ArrayList<TaskItem>());
        mQuadrants.put(TaskItem.QuadrantType.Q4_OR_COMPLETED, new ArrayList<TaskItem>());
    }

    public static Project newProject(Context context) {
        Project project = new Project();
        project.mTitle = "Demo";
        project.mProjectType = ProjectType.Simple;
        project.mCenterInPercent.x = project.mCenterInPercent.y = 50;
        project.mTitleQuadrants.put(TaskItem.QuadrantType.Q1_OR_UPCOMING, context.getString(R.string.lbl_title_quadrant1));
        project.mTitleQuadrants.put(TaskItem.QuadrantType.Q2_OR_DUE, context.getString(R.string.lbl_title_quadrant2));
        project.mTitleQuadrants.put(TaskItem.QuadrantType.Q3_OR_IN_PROGRESS, context.getString(R.string.lbl_title_quadrant3));
        project.mTitleQuadrants.put(TaskItem.QuadrantType.Q4_OR_COMPLETED, context.getString(R.string.lbl_title_quadrant4));
        project.mColorQuadrants.put(TaskItem.QuadrantType.Q1_OR_UPCOMING, ContextCompat.getColor(context, R.color.color_default_q1));
        project.mColorQuadrants.put(TaskItem.QuadrantType.Q2_OR_DUE, ContextCompat.getColor(context, R.color.color_default_q2));
        project.mColorQuadrants.put(TaskItem.QuadrantType.Q3_OR_IN_PROGRESS, ContextCompat.getColor(context, R.color.color_default_q3));
        project.mColorQuadrants.put(TaskItem.QuadrantType.Q4_OR_COMPLETED, ContextCompat.getColor(context, R.color.color_default_q4));
        project.mQuadrants.put(TaskItem.QuadrantType.Q1_OR_UPCOMING, new ArrayList<TaskItem>());
        project.mQuadrants.put(TaskItem.QuadrantType.Q2_OR_DUE, new ArrayList<TaskItem>());
        project.mQuadrants.put(TaskItem.QuadrantType.Q3_OR_IN_PROGRESS, new ArrayList<TaskItem>());
        project.mQuadrants.put(TaskItem.QuadrantType.Q4_OR_COMPLETED, new ArrayList<TaskItem>());
        return project;
    }

    public static Project newBlankProject() {
        Project project = new Project();
        project.mId = null;
        return project;
    }

    public void copyTo(Project project) {
        super.copyTo(project);
        project.mProjectType = mProjectType;
        project.mCenterInPercent.x = mCenterInPercent.x;
        project.mCenterInPercent.y = mCenterInPercent.y;
        project.mTitleQuadrants.put(TaskItem.QuadrantType.Q1_OR_UPCOMING, mTitleQuadrants.get(TaskItem.QuadrantType.Q1_OR_UPCOMING));
        project.mTitleQuadrants.put(TaskItem.QuadrantType.Q2_OR_DUE, mTitleQuadrants.get(TaskItem.QuadrantType.Q2_OR_DUE));
        project.mTitleQuadrants.put(TaskItem.QuadrantType.Q3_OR_IN_PROGRESS, mTitleQuadrants.get(TaskItem.QuadrantType.Q3_OR_IN_PROGRESS));
        project.mTitleQuadrants.put(TaskItem.QuadrantType.Q4_OR_COMPLETED, mTitleQuadrants.get(TaskItem.QuadrantType.Q4_OR_COMPLETED));
        project.mColorQuadrants.put(TaskItem.QuadrantType.Q1_OR_UPCOMING, mColorQuadrants.get(TaskItem.QuadrantType.Q1_OR_UPCOMING));
        project.mColorQuadrants.put(TaskItem.QuadrantType.Q2_OR_DUE, mColorQuadrants.get(TaskItem.QuadrantType.Q2_OR_DUE));
        project.mColorQuadrants.put(TaskItem.QuadrantType.Q3_OR_IN_PROGRESS, mColorQuadrants.get(TaskItem.QuadrantType.Q3_OR_IN_PROGRESS));
        project.mColorQuadrants.put(TaskItem.QuadrantType.Q4_OR_COMPLETED, mColorQuadrants.get(TaskItem.QuadrantType.Q4_OR_COMPLETED));
        project.mQuadrants.put(TaskItem.QuadrantType.Q1_OR_UPCOMING, mQuadrants.get(TaskItem.QuadrantType.Q1_OR_UPCOMING));
        project.mQuadrants.put(TaskItem.QuadrantType.Q2_OR_DUE, mQuadrants.get(TaskItem.QuadrantType.Q2_OR_DUE));
        project.mQuadrants.put(TaskItem.QuadrantType.Q3_OR_IN_PROGRESS, mQuadrants.get(TaskItem.QuadrantType.Q3_OR_IN_PROGRESS));
        project.mQuadrants.put(TaskItem.QuadrantType.Q4_OR_COMPLETED, mQuadrants.get(TaskItem.QuadrantType.Q4_OR_COMPLETED));
    }

    public static Project getProjectFrom(Context context, List<Object> values) {
        if (values != null) {
            Project project = Project.newProject(context);
            for (int i = 0; i < values.size(); i++) {
                String value = (String) values.get(i);
                switch (Column.values()[i]) {
                    case ID:
                        project.mId = value;
                        break;
                    case TITLE:
                        project.mTitle = value;
                        break;
                    case ITEM_INDEX:
                        project.mIndex = DataUtils.parseIntValue(value);
                        break;
                    case TYPE:
                        project.mProjectType = Project.ProjectType.values()[Integer.valueOf(value)];
                        break;
                    case Q1_TITLE:
                        project.mTitleQuadrants.put(TaskItem.QuadrantType.Q1_OR_UPCOMING, value);
                        break;
                    case Q2_TITLE:
                        project.mTitleQuadrants.put(TaskItem.QuadrantType.Q2_OR_DUE, value);
                        break;
                    case Q3_TITLE:
                        project.mTitleQuadrants.put(TaskItem.QuadrantType.Q3_OR_IN_PROGRESS, value);
                        break;
                    case Q4_TITLE:
                        project.mTitleQuadrants.put(TaskItem.QuadrantType.Q4_OR_COMPLETED, value);
                        break;
                    case Q1_COLOR:
                        project.mColorQuadrants.put(TaskItem.QuadrantType.Q1_OR_UPCOMING, DataUtils.parseIntValue(value));
                        break;
                    case Q2_COLOR:
                        project.mColorQuadrants.put(TaskItem.QuadrantType.Q2_OR_DUE, DataUtils.parseIntValue(value));
                        break;
                    case Q3_COLOR:
                        project.mColorQuadrants.put(TaskItem.QuadrantType.Q3_OR_IN_PROGRESS, DataUtils.parseIntValue(value));
                        break;
                    case Q4_COLOR:
                        project.mColorQuadrants.put(TaskItem.QuadrantType.Q4_OR_COMPLETED, DataUtils.parseIntValue(value));
                        break;
                    case CENTER_IN_PERCENT:
                        String[] points = value.split(",");
                        project.mCenterInPercent.x = DataUtils.parseIntValue(points[0]);
                        project.mCenterInPercent.y = DataUtils.parseIntValue(points[1]);
                        break;
                    case CREATED_ON:
                        project.mCreatedOn = DataUtils.parseLongValue(value);
                        break;
                    case UPDATED_ON:
                        project.mUpdatedOn = DataUtils.parseLongValue(value);
                        break;
                    case TRASHED:
                        project.mTrashed = DataUtils.parseBooleanValue(value);
                        break;
                }
            }
            return project;
        }
        return null;
    }

    public void clearAllQuadrants() {
        mQuadrants.clear();
    }

    public void addfromTaskList(ArrayList<TaskItem> list) {
        for (int i = 0; i < list.size(); i++) {
            TaskItem item = list.get(i);
            if (item.mProjectId.equals(mId)) {
                mQuadrants.get(item.mQuadrantType).add(item);
                list.remove(item);
                i--;
            }
        }
    }

    public void removeAllTasks() {
        for (TaskItem.QuadrantType quadrantType : TaskItem.QuadrantType.values()) {
            mQuadrants.get(quadrantType).clear();
        }
    }

    public ArrayList<TaskItem> getAllTasks() {
        ArrayList<TaskItem> tasks = new ArrayList<>();
        for (TaskItem.QuadrantType quadrantType : TaskItem.QuadrantType.values()) {
            tasks.addAll(mQuadrants.get(quadrantType));
        }
        return tasks;
    }

    public ArrayList<TaskItem> getTaskListForQuadrant(TaskItem.QuadrantType type) {
        return mQuadrants.get(type);
    }

    public static List<List<Object>> getProjectWriteback(final Project project) {
        List<List<Object>> values = new ArrayList<>();
        ArrayList<Object> projectValues = new ArrayList() {{
            add(project.mId + "");
            add(project.mTitle + "");
            add(project.mIndex + "");
            add(project.mProjectType.ordinal() + "");
            add(project.mTitleQuadrants.get(TaskItem.QuadrantType.Q1_OR_UPCOMING));
            add(project.mTitleQuadrants.get(TaskItem.QuadrantType.Q2_OR_DUE));
            add(project.mTitleQuadrants.get(TaskItem.QuadrantType.Q3_OR_IN_PROGRESS));
            add(project.mTitleQuadrants.get(TaskItem.QuadrantType.Q4_OR_COMPLETED));
            add(project.mColorQuadrants.get(TaskItem.QuadrantType.Q1_OR_UPCOMING) + "");
            add(project.mColorQuadrants.get(TaskItem.QuadrantType.Q2_OR_DUE) + "");
            add(project.mColorQuadrants.get(TaskItem.QuadrantType.Q3_OR_IN_PROGRESS) + "");
            add(project.mColorQuadrants.get(TaskItem.QuadrantType.Q4_OR_COMPLETED) + "");
            add(project.mCenterInPercent.x + "," + project.mCenterInPercent.y);
            add(project.mCreatedOn + "");
            add(project.mUpdatedOn + "");
            add(project.mTrashed + "");
        }};
        values.add(projectValues);
        return values;
    }

    @Override
    public String toString() {
        return
                "{\nId:" + mId +
                        "\nmTitle:" + mTitle +
                        "\nmIndex:" + mIndex +
                        "\nmProjectType:" + mProjectType.name() +
                        "\nmColorQ1:" + mColorQuadrants.get(TaskItem.QuadrantType.Q1_OR_UPCOMING) +
                        "\nmColorQ2:" + mColorQuadrants.get(TaskItem.QuadrantType.Q2_OR_DUE) +
                        "\nmColorQ3:" + mColorQuadrants.get(TaskItem.QuadrantType.Q3_OR_IN_PROGRESS) +
                        "\nmColorQ4:" + mColorQuadrants.get(TaskItem.QuadrantType.Q4_OR_COMPLETED) +
                        "\nmCreatedOn:" + mCreatedOn +
                        "\nmUpdatedOn:" + mUpdatedOn +
                        "\nmTrashed:" + mTrashed +
                        "\n}";
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put(Column.ID.name(), mId);
            json.put(Column.TITLE.name(), mTitle);
            json.put(Column.ITEM_INDEX.name(), mIndex);
            json.put(Column.TYPE.name(), mProjectType.ordinal());
            json.put(Column.Q1_COLOR.name(), mColorQuadrants.get(TaskItem.QuadrantType.Q1_OR_UPCOMING));
            json.put(Column.Q2_COLOR.name(), mColorQuadrants.get(TaskItem.QuadrantType.Q2_OR_DUE));
            json.put(Column.Q3_COLOR.name(), mColorQuadrants.get(TaskItem.QuadrantType.Q3_OR_IN_PROGRESS));
            json.put(Column.Q4_COLOR.name(), mColorQuadrants.get(TaskItem.QuadrantType.Q4_OR_COMPLETED));
            json.put(Column.Q1_TITLE.name(), mTitleQuadrants.get(TaskItem.QuadrantType.Q1_OR_UPCOMING));
            json.put(Column.Q2_TITLE.name(), mTitleQuadrants.get(TaskItem.QuadrantType.Q2_OR_DUE));
            json.put(Column.Q3_TITLE.name(), mTitleQuadrants.get(TaskItem.QuadrantType.Q3_OR_IN_PROGRESS));
            json.put(Column.Q4_TITLE.name(), mTitleQuadrants.get(TaskItem.QuadrantType.Q4_OR_COMPLETED));
            json.put(Column.CENTER_IN_PERCENT.name(), mCenterInPercent.x + "," + mCenterInPercent.y);
            json.put(Column.CREATED_ON.name(), mCreatedOn);
            json.put(Column.UPDATED_ON.name(), mUpdatedOn);
            json.put(Column.TRASHED.name(), mTrashed);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static Project getProjectFromJSON(JSONObject json) {
        Project project = new Project();
        try {
            if (json.has(Project.Column.ID.name())) {
                project.mId = json.getString(Project.Column.ID.name());
            }
            if (json.has(Project.Column.TITLE.name())) {
                project.mTitle = json.getString(Project.Column.TITLE.name());
            }
            if (json.has(Project.Column.ITEM_INDEX.name())) {
                project.mIndex = json.getInt(Project.Column.ITEM_INDEX.name());
            }
            if (json.has(Column.TYPE.name())) {
                project.mProjectType = ProjectType.values()[json.getInt(Column.TYPE.name())];
            }
            if (json.has(Column.Q1_COLOR.name())) {
                project.mColorQuadrants.put(TaskItem.QuadrantType.Q1_OR_UPCOMING, json.getInt(Column.Q1_COLOR.name()));
            }
            if (json.has(Project.Column.Q2_COLOR.name())) {
                project.mColorQuadrants.put(TaskItem.QuadrantType.Q2_OR_DUE, json.getInt(Column.Q2_COLOR.name()));
            }
            if (json.has(Project.Column.Q3_COLOR.name())) {
                project.mColorQuadrants.put(TaskItem.QuadrantType.Q3_OR_IN_PROGRESS, json.getInt(Column.Q3_COLOR.name()));
            }
            if (json.has(Project.Column.Q4_COLOR.name())) {
                project.mColorQuadrants.put(TaskItem.QuadrantType.Q4_OR_COMPLETED, json.getInt(Column.Q4_COLOR.name()));
            }
            if (json.has(Project.Column.Q1_TITLE.name())) {
                project.mTitleQuadrants.put(TaskItem.QuadrantType.Q1_OR_UPCOMING, json.getString(Column.Q1_TITLE.name()));
            }
            if (json.has(Project.Column.Q2_TITLE.name())) {
                project.mTitleQuadrants.put(TaskItem.QuadrantType.Q2_OR_DUE, json.getString(Project.Column.Q2_TITLE.name()));
            }
            if (json.has(Project.Column.Q3_TITLE.name())) {
                project.mTitleQuadrants.put(TaskItem.QuadrantType.Q3_OR_IN_PROGRESS, json.getString(Project.Column.Q3_TITLE.name()));
            }
            if (json.has(Project.Column.Q4_TITLE.name())) {
                project.mTitleQuadrants.put(TaskItem.QuadrantType.Q4_OR_COMPLETED, json.getString(Project.Column.Q4_TITLE.name()));
            }
            if (json.has(Column.CENTER_IN_PERCENT.name())) {
                String centre = json.getString(Column.CENTER_IN_PERCENT.name());
                String[] values = centre.split(",");
                project.mCenterInPercent.x = DataUtils.parseIntValue(values[0]);
                project.mCenterInPercent.y = DataUtils.parseIntValue(values[1]);
            }
            if (json.has(Project.Column.CREATED_ON.name())) {
                project.mCreatedOn = json.getLong(Project.Column.CREATED_ON.name());
            }
            if (json.has(Project.Column.UPDATED_ON.name())) {
                project.mUpdatedOn = json.getLong(Project.Column.UPDATED_ON.name());
            }
            if (json.has(Column.TRASHED.name())) {
                project.mTrashed = json.getBoolean(Column.TRASHED.name());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return project;
    }

    public static ContentValues getProjectContentValues(Project project) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Project.Column.ID.name(), project.mId);
        contentValues.put(Project.Column.TITLE.name(), project.mTitle);
        contentValues.put(Project.Column.ITEM_INDEX.name(), project.mIndex);
        contentValues.put(Project.Column.TYPE.name(), project.mProjectType.ordinal());
        contentValues.put(Project.Column.Q1_TITLE.name(), project.mTitleQuadrants.get(TaskItem.QuadrantType.Q1_OR_UPCOMING));
        contentValues.put(Project.Column.Q2_TITLE.name(), project.mTitleQuadrants.get(TaskItem.QuadrantType.Q2_OR_DUE));
        contentValues.put(Project.Column.Q3_TITLE.name(), project.mTitleQuadrants.get(TaskItem.QuadrantType.Q3_OR_IN_PROGRESS));
        contentValues.put(Project.Column.Q4_TITLE.name(), project.mTitleQuadrants.get(TaskItem.QuadrantType.Q4_OR_COMPLETED));
        contentValues.put(Project.Column.Q1_COLOR.name(), project.mColorQuadrants.get(TaskItem.QuadrantType.Q1_OR_UPCOMING) + "");
        contentValues.put(Project.Column.Q2_COLOR.name(), project.mColorQuadrants.get(TaskItem.QuadrantType.Q2_OR_DUE) + "");
        contentValues.put(Project.Column.Q3_COLOR.name(), project.mColorQuadrants.get(TaskItem.QuadrantType.Q3_OR_IN_PROGRESS) + "");
        contentValues.put(Project.Column.Q4_COLOR.name(), project.mColorQuadrants.get(TaskItem.QuadrantType.Q4_OR_COMPLETED) + "");
        contentValues.put(Project.Column.CENTER_IN_PERCENT.name(), project.mCenterInPercent.x + "," + project.mCenterInPercent.y);
        contentValues.put(Project.Column.CREATED_ON.name(), project.mCreatedOn);
        contentValues.put(Project.Column.UPDATED_ON.name(), project.mUpdatedOn);
        contentValues.put(Column.TRASHED.name(), project.mTrashed ? 1 : 0);
        return contentValues;
    }

    public static Project readProjectFromCursor(Cursor cursor) {
        Project project = new Project();
        project.mId = cursor.getString(cursor.getColumnIndex(Column.ID.name()));
        project.mTitle = cursor.getString(cursor.getColumnIndex(Column.TITLE.name()));
        project.mIndex = cursor.getInt(cursor.getColumnIndex(Column.ID.name()));
        project.mProjectType = ProjectType.values()[cursor.getInt(cursor.getColumnIndex(Column.TYPE.name()))];
        project.mCreatedOn = cursor.getLong(cursor.getColumnIndex(Column.CREATED_ON.name()));
        project.mUpdatedOn = cursor.getLong(cursor.getColumnIndex(Column.UPDATED_ON.name()));

        String centre = cursor.getString(cursor.getColumnIndex(Project.Column.CENTER_IN_PERCENT.name()));
        String[] points = centre.split(",");
        project.mCenterInPercent.x = DataUtils.parseIntValue(points[0]);
        project.mCenterInPercent.y = DataUtils.parseIntValue(points[1]);

        project.mCenterInPercent.x = project.mCenterInPercent.y = 50;
        project.mTitleQuadrants.put(TaskItem.QuadrantType.Q1_OR_UPCOMING, cursor.getString(cursor.getColumnIndex(Column.Q1_TITLE.name())));
        project.mTitleQuadrants.put(TaskItem.QuadrantType.Q2_OR_DUE, cursor.getString(cursor.getColumnIndex(Column.Q2_TITLE.name())));
        project.mTitleQuadrants.put(TaskItem.QuadrantType.Q3_OR_IN_PROGRESS, cursor.getString(cursor.getColumnIndex(Column.Q3_TITLE.name())));
        project.mTitleQuadrants.put(TaskItem.QuadrantType.Q4_OR_COMPLETED, cursor.getString(cursor.getColumnIndex(Column.Q4_TITLE.name())));
        project.mColorQuadrants.put(TaskItem.QuadrantType.Q1_OR_UPCOMING, cursor.getInt(cursor.getColumnIndex(Column.Q1_COLOR.name())));
        project.mColorQuadrants.put(TaskItem.QuadrantType.Q2_OR_DUE, cursor.getInt(cursor.getColumnIndex(Column.Q2_COLOR.name())));
        project.mColorQuadrants.put(TaskItem.QuadrantType.Q3_OR_IN_PROGRESS, cursor.getInt(cursor.getColumnIndex(Column.Q3_COLOR.name())));
        project.mColorQuadrants.put(TaskItem.QuadrantType.Q4_OR_COMPLETED, cursor.getInt(cursor.getColumnIndex(Column.Q4_COLOR.name())));
        project.mQuadrants.put(TaskItem.QuadrantType.Q1_OR_UPCOMING, new ArrayList<TaskItem>());
        project.mQuadrants.put(TaskItem.QuadrantType.Q2_OR_DUE, new ArrayList<TaskItem>());
        project.mQuadrants.put(TaskItem.QuadrantType.Q3_OR_IN_PROGRESS, new ArrayList<TaskItem>());
        project.mQuadrants.put(TaskItem.QuadrantType.Q4_OR_COMPLETED, new ArrayList<TaskItem>());
        project.mTrashed = cursor.getInt(cursor.getColumnIndex(Column.TRASHED.name())) == 1;
        return project;

    }
}
