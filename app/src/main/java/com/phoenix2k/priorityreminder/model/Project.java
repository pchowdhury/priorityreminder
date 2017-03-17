package com.phoenix2k.priorityreminder.model;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;

import com.phoenix2k.priorityreminder.DataStore;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.utils.DataUtils;
import com.phoenix2k.priorityreminder.utils.IDGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Pushpan on 06/02/17.
 */

public class Project {

    public enum Column {
        ID,
        POSITION,
        TITLE,
        INDEX,
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
        UPDATED_ON
    }

    public enum ProjectType {
        Simple,
        State
    }

    public String mId;
    public String mPosition;
    public String mTitle;
    public int mIndex;
    public HashMap<TaskItem.QuadrantType, String> mTitleQuadrants = new HashMap<>();
    public HashMap<TaskItem.QuadrantType, Integer> mColorQuadrants = new HashMap<>();
    public ProjectType mProjectType = ProjectType.Simple;
    public Point mCenterInPercent = new Point();
    public long mCreatedOn;
    public long mUpdatedOn;

    private HashMap<TaskItem.QuadrantType, ArrayList<TaskItem>> mQuadrants = new HashMap<>();

    public Project() {
        mQuadrants.put(TaskItem.QuadrantType.Q1_OR_UPCOMING, new ArrayList<TaskItem>());
        mQuadrants.put(TaskItem.QuadrantType.Q2_OR_DUE, new ArrayList<TaskItem>());
        mQuadrants.put(TaskItem.QuadrantType.Q3_OR_IN_PROGRESS, new ArrayList<TaskItem>());
        mQuadrants.put(TaskItem.QuadrantType.Q4_OR_COMPLETED, new ArrayList<TaskItem>());
    }

    public static Project newProject(Context context) {
        Project project = new Project();
        project.mId = IDGenerator.generateUniqueId() + "";
        project.mPosition = DataStore.getInstance().getLastProjectPosition() + 1 + "";
        project.mTitle = "";
        project.mIndex = 0;
        project.mProjectType = ProjectType.Simple;
        project.mCreatedOn = IDGenerator.generateUniqueId();
        project.mUpdatedOn = project.mCreatedOn;
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

    public void copyTo(Project project) {
        project.mId = mId;
        project.mPosition = mPosition;
        project.mTitle = mTitle;
        project.mIndex = mIndex;
        project.mProjectType = mProjectType;
        project.mCreatedOn = mCreatedOn;
        project.mUpdatedOn = mCreatedOn;
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
                    case POSITION:
                        project.mPosition = value;
                        break;
                    case TITLE:
                        project.mTitle = value;
                        break;
                    case INDEX:
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
            if (item.mProjectId.equalsIgnoreCase(mId)) {
                mQuadrants.get(item.mQuadrantType).add(item);
                list.remove(item);
                i--;
            }
        }
    }

    public ArrayList<TaskItem> getTaskListForQuadrant(TaskItem.QuadrantType type) {
        return mQuadrants.get(type);
    }

    public static List<List<Object>> getProjectWriteback(final Project project) {
        List<List<Object>> values = new ArrayList<>();
        ArrayList<Object> projectValues = new ArrayList() {{
            add(project.mId + "");
            add(project.mPosition + "");
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
        }};
        values.add(projectValues);
        return values;
    }

    @Override
    public String toString() {
        return
                "{\nId:" + mId +
                        "\nmPosition:" + mPosition +
                        "\nmTitle:" + mTitle +
                        "\nmIndex:" + mIndex +
                        "\nmProjectType:" + mProjectType.name() +
                        "\nmColorQ1:" + mColorQuadrants.get(TaskItem.QuadrantType.Q1_OR_UPCOMING) +
                        "\nmColorQ2:" + mColorQuadrants.get(TaskItem.QuadrantType.Q2_OR_DUE) +
                        "\nmColorQ3:" + mColorQuadrants.get(TaskItem.QuadrantType.Q3_OR_IN_PROGRESS) +
                        "\nmColorQ4:" + mColorQuadrants.get(TaskItem.QuadrantType.Q4_OR_COMPLETED) +
                        "\nmCreatedOn:" + mCreatedOn +
                        "\nmUpdatedOn:" + mUpdatedOn +
                        "\n}";
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put(Column.ID.name(), mId);
            json.put(Column.POSITION.name(), mPosition);
            json.put(Column.TITLE.name(), mTitle);
            json.put(Column.INDEX.name(), mIndex);
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
            if (json.has(Project.Column.POSITION.name())) {
                project.mPosition = json.getString(Project.Column.POSITION.name());
            }
            if (json.has(Project.Column.TITLE.name())) {
                project.mTitle = json.getString(Project.Column.TITLE.name());
            }
            if (json.has(Project.Column.INDEX.name())) {
                project.mIndex = json.getInt(Project.Column.INDEX.name());
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return project;
    }
}
