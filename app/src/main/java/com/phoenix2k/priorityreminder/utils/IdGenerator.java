package com.phoenix2k.priorityreminder.utils;

import android.text.format.DateFormat;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Pushpan on 06/02/17.
 */

public class IDGenerator {

    private static IDGenerator mInstance;
    private long mCurrentMillis;
    private Timer mTimer;


//    public static int generateUniqueId() {
//        UUID idOne = UUID.randomUUID();
//        String str = "" + idOne;
//        int uid = str.hashCode();
//        String filterStr = "" + uid;
//        str = filterStr.replaceAll("-", "");
//        return Integer.parseInt(str);
//    }


    public static long generateUniqueId() {
        return mInstance.mCurrentMillis;
    }

    public static void init() {
        mInstance = new IDGenerator();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL obj = new URL("https://www.google.com/");
                    URLConnection conn;
                    conn = obj.openConnection();
                    String dateStr = conn.getHeaderField("Date");
                    LogUtils.logI("Date", dateStr);
                    //Tue, 07 Feb 2017 17:42:57 GMT
                    SimpleDateFormat format = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");
                    final Date date = format.parse(dateStr);
                    mInstance.mCurrentMillis = date.getTime();
                    mInstance.mTimer = new Timer();
                    mInstance.mTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            mInstance.mCurrentMillis += 1;
//                            LogUtils.logI("Time", mInstance.mCurrentMillis+"");
                        }
                    }, 0, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void deInit() {
        if (mInstance.mTimer != null) {
            mInstance.mTimer.cancel();
        }
        mInstance = null;
    }

}
