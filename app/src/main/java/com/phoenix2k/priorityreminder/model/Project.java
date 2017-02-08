package com.phoenix2k.priorityreminder.model;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.phoenix2k.priorityreminder.ProjectsColumns;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.utils.DataUtils;
import com.phoenix2k.priorityreminder.utils.IDGenerator;

import java.util.ArrayList;
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
    public String mTitle;
    public int mIndex;
    public int mColorQ1;
    public int mColorQ2;
    public int mColorQ3;
    public int mColorQ4;

    public ProjectType mProjectType = ProjectType.Simple;
    public long mCreatedOn;
    public long mUpdatedOn;

    public static Project newProject(Context context) {
        Project project = new Project();
        project.mId = IDGenerator.generateUniqueId() + "";
        project.mTitle = "";
        project.mIndex = 0;
        project.mCreatedOn = IDGenerator.generateUniqueId();
        project.mUpdatedOn = project.mCreatedOn;
        project.mProjectType = ProjectType.Simple;
        project.mColorQ1 = ContextCompat.getColor(context, R.color.color_default_q1);
        project.mColorQ2 = ContextCompat.getColor(context, R.color.color_default_q2);
        project.mColorQ3 = ContextCompat.getColor(context, R.color.color_default_q3);
        project.mColorQ4 = ContextCompat.getColor(context, R.color.color_default_q4);
        return project;
    }

    public String getColorCode(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }

    public int getColorCode(String color) {
        try {
            return Integer.parseInt(color.replaceFirst("#", ""), 16);
        } catch (Exception e) {
            return 0;
        }
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
                    case NAME:
                        project.mTitle = value;
                        break;
                    case Q1_COLOR:
                        project.mColorQ1 = DataUtils.parseIntValue(value);
                        break;
                    case Q2_COLOR:
                        project.mColorQ2 = DataUtils.parseIntValue(value);
                        break;
                    case Q3_COLOR:
                        project.mColorQ3 = DataUtils.parseIntValue(value);
                        break;
                    case Q4_COLOR:
                        project.mColorQ4 = DataUtils.parseIntValue(value);
                        break;
                    case TYPE:
                        project.mProjectType = Project.ProjectType.values()[Integer.valueOf(value)];
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

    @Override
    public String toString() {
        return
                "{\nId:" + mId +
                        "\nmTitle:" + mTitle +
                        "\nmColorQ1:" + mColorQ1 +
                        "\nmColorQ2:" + mColorQ2 +
                        "\nmColorQ3:" + mColorQ3 +
                        "\nmColorQ4:" + mColorQ4 +
                        "\nmProjectType:" + mProjectType.name() +
                        "\nmCreatedOn:" + mCreatedOn +
                        "\nmUpdatedOn:" + mUpdatedOn +
                        "\n}";
    }
}
