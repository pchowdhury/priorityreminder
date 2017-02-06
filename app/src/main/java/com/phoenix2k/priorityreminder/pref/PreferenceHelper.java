package com.phoenix2k.priorityreminder.pref;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Pushpan on 04/02/17.
 */

public class PreferenceHelper {
    private static final String KEY_APP_FOLDER_ID = "priorityreminder.KEY_APP_FOLDER_ID";
    private static final String KEY_DATA_FILE_ID = "priorityreminder.KEY_DATA_FILE_ID";
    private static final String KEY_SIGNIN_USER_ID = "priorityreminder.KEY_SIGNIN_USER_ID";


    /**
     * Set the app drive folder id in the preferences.
     * @param c  The current {@link Context}
     * @param id The drive id to save in the preferences
     */
    public static void setAppFolderId(Context c, String id) {
        PreferenceManager.getDefaultSharedPreferences(c).edit()
                .putString(KEY_APP_FOLDER_ID, String.valueOf(id)).apply();
    }

    /**
     * The app drive folder id
     *
     * @param c The current Context
     * @return The app folder id
     */
    public static String getSavedAppFolderId(Context c) {
        String name = PreferenceManager.getDefaultSharedPreferences(c)
                .getString(KEY_APP_FOLDER_ID, null);
       return name;
    }

    /**
     * Set the data file id in the preferences.
     * @param c  The current {@link Context}
     * @param id The drive id to save in the preferences
     */
    public static void setDataFileId(Context c, String id) {
        PreferenceManager.getDefaultSharedPreferences(c).edit()
                .putString(KEY_DATA_FILE_ID, String.valueOf(id)).apply();
    }

    /**
     * The data file id
     *
     * @param c The current Context
     * @return The data file id
     */
    public static String getSavedDataFileId(Context c) {
        String name = PreferenceManager.getDefaultSharedPreferences(c)
                .getString(KEY_DATA_FILE_ID, null);
        return name;
    }


    /**
     * Set the data file id in the preferences.
     * @param c  The current {@link Context}
     * @param userId The user id to save in the preferences
     */
    public static void setSignInUserId(Context c, String userId) {
        PreferenceManager.getDefaultSharedPreferences(c).edit()
                .putString(KEY_SIGNIN_USER_ID, String.valueOf(userId)).apply();
    }

    /**
     * The data file id
     *
     * @param c The current Context
     * @return The user id
     */
    public static String getSavedSignInUserId(Context c) {
        String userId = PreferenceManager.getDefaultSharedPreferences(c)
                .getString(KEY_SIGNIN_USER_ID, null);
        return userId;
    }

}

