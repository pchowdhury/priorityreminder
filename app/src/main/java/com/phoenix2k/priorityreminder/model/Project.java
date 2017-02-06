package com.phoenix2k.priorityreminder.model;

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
    public String mColorQ1;
    public String mColorQ2;
    public String mColorQ3;
    public String mColorQ4;

    public ProjectType mProjectType = ProjectType.Simple;
    public long mCreatedOn;
    public long mUpdatedOn;


    public String getColorCode(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }

    public int getColorCode(String color) {
        try {
            return Integer.parseInt(color.replaceFirst("#", ""), 16);
        }catch(Exception e){
            return 0;
        }
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
