/*
 * Copyright (c) 2013 Inkoniq
 * All Rights Reserved.
 * @since 05-May-2013 
 * @author Pushpan
 */
package com.phoenix2k.priorityreminder.utils;

/**
 * @author Pushpan
 */
public class ConstantUtils {

	public static final String STAGE_DOMAIN = "stage.appfluence.com";
	public static final String PRODUCTION_DOMAIN = "sync.appfluence.com";

	public static final String DOMAIN = PRODUCTION_DOMAIN;

	public static final String SERVER = "http"
			+ (DOMAIN.equalsIgnoreCase(PRODUCTION_DOMAIN) ? "s" : "") + "://"
			+ DOMAIN + "/";

	public static final int ACTION_COPY = 0;
	public static final int ACTION_MOVE = 1;
	public static final int ACTION_DELETE = 2;
	public static final int ACTION_DONE = 3;

	public static final int SWIPE_MIN_DISTANCE = 120;
	public static final int SWIPE_MAX_OFF_PATH = 250;
	public static final int SWIPE_THRESHOLD_VELOCITY = 200;

	public static final int STATE_UNCHANGED = 0;
	public static final int STATE_CHANGED = 1;
	public static final int STATE_SEND = 2;
	public static final int STATE_DEFECTIVE = 3;

	public static final String PRIORITY_MATRIX_PREF = "PRIORITY_MATRIX_PREF";
	public static final String API_PROJECT_PATH = "/api/v1/project/";

	public static final String DATE_FORMAT_SYNC = "yyyy-MM-d HH:mm:ss";
	public static final String DATE_FORMAT_TODO = "MMMM d, yyyy hh:mm a";
	public static final String DATE_FORMAT_TODO_DATE = "MMMM d, yyyy hh:mm a";
	public static final String DATE_FORMAT_TODO_ALL_DAY = "MMMM d, yyyy";
	public static final String DATE_FORMAT_MASTER = "MMM dd, yyyy";

	public static final boolean DEBUG = true;

	public static final boolean SYNC_GREY_ENABLED = false;

	public static final int NUM_FONT_MODES = 8;

	public static final int INDEX_SPACING = 1000;

	public static final int IDD_SPACING = 1000;

	public static final String LOGIN_URL = SERVER + "accounts/login/";

	public static final String LOGOUT_URL = SERVER + "accounts/logout/";

	public static final String CHECK_LOGGED_IN = SERVER + "loginforsync/";

	public static final String POST_LOGIN_LANDING_URL = SERVER;

	public static final String GET_PROJECTS = "api/v1/project/";

	public static final String GET_ITEMS = "api/v1/item/";

	public static final String DUMMY_API_URL = "loginforsync/";

	public static final String SYNC_URL_FULLSYNC = "sync/fullSyncV4";

	public static final String GET_HELP = "sync/returnAndroidHelpList";

	public static final String METHOD_ALL_PROJECTS = "AllProjects";

	public static final String METHOD_ALL_ITEMS = "AllTodoItems";

	public static final String METHOD_UPDATED_TODOLIST = "UpdatedTodoList";

	public static final String METHOD_UPDATED_TODO_ITEM = "UpdatedTodoItems";

	public static final String METHOD_CONFIG = "Config";

	public static final String METHOD_FREQUENT_ICONS = "FrequentIcons";

	public static final String ACCEPT_ENCODING = "Accept-Encoding";

	public static final String ENCODING_GZIP = "gzip";

	public static final String SHOW_DATE_FILTER = "SHOW_DATE_FILTER";

	public static final int TODOITEM_START_DATE = 0;

	public static final int TODOITEM_DUE_DATE = 1;

	public static final int TODOITEM_COMPLETION_DATE = 2;

	public static final int TODOITEM_UNTIL_DATE = 3;

	public static final String REGISTER = SERVER + "/accounts/register/";

	public static final String IS_REGISTER = "isRegister";

	public static final String TAKE_SNAPSHOT_AND_EMAIL = "TAKE_SNAPSHOT_AND_EMAIL";

	public static final String GET_CONFIG = "api/v1/rest_config/7/";

	public static final String GET_ICON_FREQUENCY = "api/v1/me/frequent_icons/";

	public static final int DIALOG_TYPE_DELETE_PROJECT_CONFIRMATION = 0;
	public static final int DIALOG_TYPE_DELETE_TASK_CONFIRMATION = 1;
	public static final int DIALOG_TYPE_INVALID_DUE_DATE_ALERT = 2;
	public static final int DIALOG_TYPE_DELETE_LAST_PROJECT = 3;

	// broadcast actions
	public static final int SYNC_STARTED = 0;
	public static final int SYNC_COMPLETED = 1;
	public static final int SYNC_AUTH_ERROR = 2;
	public static final int SYNC_ERROR = 3;
	public static final int SYNC_DATA_UPDATED = 4;
	public static final int SYNC_BLOCKED_FOR_EDITING = 4;

	// Delays
	public static final int DELAY_SPLASH = 3000;
	public static final int DELAY_POLLING = 10 * 1000;

}
