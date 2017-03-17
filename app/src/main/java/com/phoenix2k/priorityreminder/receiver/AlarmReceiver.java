package com.phoenix2k.priorityreminder.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.phoenix2k.priorityreminder.BuildConfig;
import com.phoenix2k.priorityreminder.DashboardActivity;
import com.phoenix2k.priorityreminder.R;
import com.phoenix2k.priorityreminder.model.TaskItem;
import com.phoenix2k.priorityreminder.utils.LogUtils;

import org.json.JSONObject;


public class AlarmReceiver extends BroadcastReceiver {
    public static final int NOTIFICATION_CONSTANT = 12000;
    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            String value = bundle
                    .getString(context.getString(R.string.intent_taskitem_message));
            if (value == null) {
                return;
            }
            TaskItem taskItem = TaskItem.getTaskFromJSON(new JSONObject(value));
            LogUtils.logI("AlarmReceiver", value + "");
            // Toast.makeText(
            // context,
            // new String(new Date(todoItem.getStartDate() * 1000)
            // .toLocaleString()), Toast.LENGTH_SHORT).show();
            int notificationId = bundle.getInt(context.getString(R.string.intent_notification_id));

            Intent activityIntent = new Intent(context, DashboardActivity.class);
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activityIntent.putExtra(context.getString(R.string.intent_taskitem_message),
                    value);

            PendingIntent pIntent = PendingIntent.getActivity(context,
                    NOTIFICATION_CONSTANT + notificationId, activityIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            // Build notification
            // Actions are just fake
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    context)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle(
                            context.getResources().getString(R.string.app_name))
                    .setContentText("Task \"" + taskItem.mTitle + "\"");
            // mBuilder.build().flags = Notification.FLAG_AUTO_CANCEL;
            // Hide the notification after its selected

            Notification notification = mBuilder.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;

            notificationManager.notify(NOTIFICATION_CONSTANT + notificationId,
                    notification);
            // broadcast to the activity if the activity is listening
            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtra(context.getString(R.string.intent_taskitem_message), taskItem.toJSON().toString());
            broadcastIntent.putExtra(context.getString(R.string.intent_notification_id),
                    NOTIFICATION_CONSTANT + notificationId);
            notifyUser(context, broadcastIntent);
            // Log.e(TAG, JsonFormat.printToString(todoItem.build()));
        } catch (Exception e) {
            // Toast.makeText(
            // context,
            // "There was an error somewhere, but we still received an alarm",
            // Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }
    }

    /**
     * @param context
     * @param broadcastIntent
     */
    protected void notifyUser(Context context, Intent broadcastIntent) {
        broadcastIntent.setAction(context.getString(R.string.action_notify));
        context.sendBroadcast(broadcastIntent);
    }

    public static void clearAllNotifications(Context context) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "clearAllNotifications");
        }
        // PMAppUtils appUtils = new PMAppUtils();
        // int[] notArr = appUtils.getNotifications(context);
        // for (int i = 0; i < notArr.length; i++) {
        // clearNotification(context, NOTIFICATION_CONSTANT + notArr[i]);
        // }
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static void clearNotification(Context context, int notificationId) {
        // Log.e(TAG, "clearNotification=" + notificationId);
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notificationId);
    }
}
