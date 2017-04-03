package com.phoenix2k.priorityreminder;

/**
 * Created by Pushpan on 08/02/17.
 */

public interface UpdateListener {
    void onNewProjectAdded();
    boolean onSelectBack();
    void onTaskUpdated();
    void onCancelEdit();
}
