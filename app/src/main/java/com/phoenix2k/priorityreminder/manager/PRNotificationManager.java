package com.phoenix2k.priorityreminder.manager;

import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.phoenix2k.priorityreminder.BuildConfig;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.model.TaskItem;
import com.phoenix2k.priorityreminder.pref.PreferenceHelper;
import com.phoenix2k.priorityreminder.receiver.AlarmReceiver;

public class PRNotificationManager {
    private static final String TAG = "PMNotificationManager";
    public static final int NOTIFICATION_CONSTANT = 6000;

    private Context mContext;
    private int notificationCount;
    private static PRNotificationManager mInstance;

    public PRNotificationManager(Context c) {
        this.mContext = c;
    }

    public static void init(Context c) {
        mInstance = new PRNotificationManager(c);
    }

    public static PRNotificationManager getInstance() {
        return mInstance;
    }

    /**
     * @param taskItem
     */
    public void scheduleNotfication(TaskItem taskItem) {
        long now = System.currentTimeMillis();
        long itemStartTime = taskItem.mStartTime;
        // start date not set
        if (itemStartTime == 0) {
            // due date not set
            itemStartTime = taskItem.mDueTime;
            if (itemStartTime == 0) {
                return;
            }
        }
        int notificationId = -1;
        if (taskItem.mQuadrantType == TaskItem.QuadrantType.Q1_OR_UPCOMING) {
            // check if not started yet
            if (itemStartTime > now) {
                notificationId = notificationCount++;
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "NON_REPEAT @" + showDate(itemStartTime)
                            + " Id=" + notificationId);
                }
                // get a Calendar object with current time
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(itemStartTime);
                Intent intent = new Intent(getContext(), AlarmReceiver.class);
                intent.putExtra(getContext().getString(R.string.intent_taskitem_message), taskItem.toJSON().toString());

                PreferenceHelper.addNotification(getContext(), notificationId);

                intent.putExtra(getContext().getString(R.string.intent_notification_id), notificationId);

                PendingIntent sender = PendingIntent.getBroadcast(getContext(),
                        NOTIFICATION_CONSTANT + notificationId, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                // Get the AlarmManager service
                AlarmManager am = (AlarmManager) getContext().getSystemService(
                        getContext().ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
                if (BuildConfig.DEBUG) {
                    Log.e(TAG,
                            "Added Reminder @ "
                                    + showDate(cal.getTimeInMillis()) + " Id="
                                    + (NOTIFICATION_CONSTANT + notificationId));
                }
            }
        }
    }

    public String showDate(long millis) {
        return new Date(millis).toLocaleString();
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public int getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(int notificationCount) {
        this.notificationCount = notificationCount;
    }

    public void clearAllAlarmNotifications() {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "clearAllAlarm");
        }
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        int[] notArr = PreferenceHelper.getNotifications(getContext());
        PendingIntent pi = null;
        AlarmManager alarmManager = (AlarmManager) getContext()
                .getSystemService(Context.ALARM_SERVICE);
        for (int i = 0; i < notArr.length; i++) {
            pi = PendingIntent.getBroadcast(getContext(), NOTIFICATION_CONSTANT
                    + notArr[i], intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pi);
        }
    }
}
