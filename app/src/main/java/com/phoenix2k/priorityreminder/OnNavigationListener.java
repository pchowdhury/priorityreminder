package com.phoenix2k.priorityreminder;

/**
 * Created by Pushpan on 07/02/17.
 */

import android.support.annotation.NonNull;

/**
 * Listener for handling events on navigation items.
 */
public interface OnNavigationListener {

    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item
     *
     * @return true to display the item as the selected item
     */
     boolean onNavigationItemSelected(@NonNull Object item);
     void onAddNewProject();
}