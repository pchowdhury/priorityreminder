package com.phoenix2k.priorityreminder.pref;

import android.content.Context;
import android.preference.PreferenceManager;

import com.phoenix2k.priorityreminder.utils.DataUtils;


/**
 * Created by Pushpan on 04/02/17.
 */

public class PreferenceHelper {
    private static final String KEY_APP_FOLDER_ID = "priorityreminder.KEY_APP_FOLDER_ID";
    private static final String KEY_PROJECT_FILE_ID = "priorityreminder.KEY_PROJECT_FILE_ID";
    private static final String KEY_DATA_FILE_ID = "priorityreminder.KEY_DATA_FILE_ID";
    private static final String KEY_SIGNIN_USER_ID = "priorityreminder.KEY_SIGNIN_USER_ID";
    public static final String KEY_NOTIFICATIONS = "priorityreminder.KEY_NOTIFICATIONS";


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
     * @param id The data file id to save in the preferences
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
     * @param id The project file id to save in the preferences
     */
    public static void setProjectFileId(Context c, String id) {
        PreferenceManager.getDefaultSharedPreferences(c).edit()
                .putString(KEY_PROJECT_FILE_ID, String.valueOf(id)).apply();
    }

    /**
     * The data file id
     *
     * @param c The current Context
     * @return The project file id
     */
    public static String getSavedProjectFileId(Context c) {
        String name = PreferenceManager.getDefaultSharedPreferences(c)
                .getString(KEY_PROJECT_FILE_ID, null);
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

    public static void addNotification(Context c, int notification) {
        String map = PreferenceManager.getDefaultSharedPreferences(c)
                .getString(KEY_NOTIFICATIONS, null);
        if (map != null) {
            if (map.trim().length() > 0) {
                map = map + "-" + notification;
            } else {
                map = notification + "";
            }
        } else {
            map = notification + "";
        }
        PreferenceManager.getDefaultSharedPreferences(c).edit()
                .putString(KEY_NOTIFICATIONS, String.valueOf(map)).apply();
    }

    public static void clearNotification(Context c) {
        PreferenceManager.getDefaultSharedPreferences(c).edit()
                .putString(KEY_NOTIFICATIONS, String.valueOf("")).apply();
    }

    public static int[] getNotifications(Context c) {
        String map = PreferenceManager.getDefaultSharedPreferences(c)
                .getString(KEY_NOTIFICATIONS, null);
        int[] nottificationArr = null;
        if (map != null && map.trim().length() != 0) {
            if (map != null && map.length() > 0) {
                String[] pairs = map.split("-");
                nottificationArr = new int[pairs.length];
                for (int i = 0; i < pairs.length; i++) {
                    nottificationArr[i] = DataUtils.parseIntValue(pairs[i]);
                }
            }
            if (nottificationArr != null) {
                return nottificationArr;
            }
        }
        nottificationArr = new int[0];
        return nottificationArr;
    }

}

