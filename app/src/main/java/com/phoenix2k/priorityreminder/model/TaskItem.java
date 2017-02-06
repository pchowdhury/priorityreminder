package com.phoenix2k.priorityreminder.model;

/**
 * Created by Pushpan on 06/02/17.
 */

public class TaskItem {
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

    String mId;
    String mProjectId;
    public String mTitle;


    public Status mStatus = Status.NotStarted;
    public RepeatType mRepeatType = RepeatType.None;

    public long mCreatedOn;
    public long mUpdatedOn;

    @Override
    public String toString() {
        return
                "{\nId:" + mId +
                        "\nmProjectId:" + mProjectId +
                        "\nmTitle:" + mTitle +
                        "\nmRepeatType:" + mRepeatType.name() +
                        "\nmCreatedOn:" + mCreatedOn +
                        "\nmUpdatedOn:" + mUpdatedOn +
                        "\n}";
    }
}
