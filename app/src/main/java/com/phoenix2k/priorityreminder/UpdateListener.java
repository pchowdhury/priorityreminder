package com.phoenix2k.priorityreminder;

import com.phoenix2k.priorityreminder.model.PREntity;

/**
 * Created by Pushpan on 08/02/17.
 */

public interface UpdateListener {
    void onNewItemAdded(PREntity item);
    void onDeleteItem(PREntity item);
    boolean onSelectBack();
    void onItemUpdated(PREntity item);
    void onCancelEdit(PREntity item);
}
