package com.phoenix2k.priorityreminder.model;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;

import com.phoenix2k.priorityreminder.DataStore;
import com.phoenix2k.priorityreminder.ProjectsColumns;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.utils.DataUtils;
import com.phoenix2k.priorityreminder.utils.IDGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Pushpan on 06/02/17.
 */

public class Project {

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
        project.mTitleQuadrants.put(TaskItem.QuadrantType.Q1, context.getString(R.string.lbl_title_quadrant1));
        project.mTitleQuadrants.put(TaskItem.QuadrantType.Q2, context.getString(R.string.lbl_title_quadrant2));
        project.mTitleQuadrants.put(TaskItem.QuadrantType.Q3, context.getString(R.string.lbl_title_quadrant3));
        project.mTitleQuadrants.put(TaskItem.QuadrantType.Q4, context.getString(R.string.lbl_title_quadrant4));
        project.mColorQuadrants.put(TaskItem.QuadrantType.Q1, ContextCompat.getColor(context, R.color.color_default_q1));
        project.mColorQuadrants.put(TaskItem.QuadrantType.Q2, ContextCompat.getColor(context, R.color.color_default_q2));
        project.mColorQuadrants.put(TaskItem.QuadrantType.Q3, ContextCompat.getColor(context, R.color.color_default_q3));
        project.mColorQuadrants.put(TaskItem.QuadrantType.Q4, ContextCompat.getColor(context, R.color.color_default_q4));
        project.mQuadrants.put(TaskItem.QuadrantType.Q1, new ArrayList<TaskItem>());
        project.mQuadrants.put(TaskItem.QuadrantType.Q2, new ArrayList<TaskItem>());
        project.mQuadrants.put(TaskItem.QuadrantType.Q3, new ArrayList<TaskItem>());
        project.mQuadrants.put(TaskItem.QuadrantType.Q4, new ArrayList<TaskItem>());
        return project;
    }

    public static Project getProjectFrom(Context context, List<Object> values) {
        if (values != null) {
            Project project = Project.newProject(context);
            for (int i = 0; i < values.size(); i++) {
                String value = (String) values.get(i);
                switch (ProjectsColumns.values()[i]) {
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
                        project.mTitleQuadrants.put(TaskItem.QuadrantType.Q1, value);
                        break;
                    case Q2_TITLE:
                        project.mTitleQuadrants.put(TaskItem.QuadrantType.Q2, value);
                        break;
                    case Q3_TITLE:
                        project.mTitleQuadrants.put(TaskItem.QuadrantType.Q3, value);
                        break;
                    case Q4_TITLE:
                        project.mTitleQuadrants.put(TaskItem.QuadrantType.Q4, value);
                        break;
                    case Q1_COLOR:
                        project.mColorQuadrants.put(TaskItem.QuadrantType.Q1, DataUtils.parseIntValue(value));
                        break;
                    case Q2_COLOR:
                        project.mColorQuadrants.put(TaskItem.QuadrantType.Q2, DataUtils.parseIntValue(value));
                        break;
                    case Q3_COLOR:
                        project.mColorQuadrants.put(TaskItem.QuadrantType.Q3, DataUtils.parseIntValue(value));
                        break;
                    case Q4_COLOR:
                        project.mColorQuadrants.put(TaskItem.QuadrantType.Q4, DataUtils.parseIntValue(value));
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

    @Override
    public String toString() {
        return
                "{\nId:" + mId +
                        "\nmPosition:" + mPosition +
                        "\nmTitle:" + mTitle +
                        "\nmIndex:" + mIndex +
                        "\nmProjectType:" + mProjectType.name() +
                        "\nmColorQ1:" + mColorQuadrants.get(TaskItem.QuadrantType.Q1) +
                        "\nmColorQ2:" + mColorQuadrants.get(TaskItem.QuadrantType.Q2) +
                        "\nmColorQ3:" + mColorQuadrants.get(TaskItem.QuadrantType.Q3) +
                        "\nmColorQ4:" + mColorQuadrants.get(TaskItem.QuadrantType.Q4) +
                        "\nmCreatedOn:" + mCreatedOn +
                        "\nmUpdatedOn:" + mUpdatedOn +
                        "\n}";
    }
}
