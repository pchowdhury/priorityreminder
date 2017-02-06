package com.phoenix2k.priorityreminder.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.util.ArrayList;
import java.util.List;

public class LogUtils {
    public static boolean ENABLE_LOGGING = true;

    public static void printList(List<com.google.api.services.drive.model.File> files) {
        if (ENABLE_LOGGING) {
            List<String> fileInfo = new ArrayList<>();
            if (files != null) {
                for (File file : files) {
                    fileInfo.add(String.format("%s (%s)\n",
                            file.getName(), file.getId()));
                }
            }
            Log.i("List", TextUtils.join("\n", fileInfo));
        }
    }

    public static void printException(Throwable e) {
        if (ENABLE_LOGGING) {
            e.printStackTrace();
        }
    }

    public static void printException(Error e) {
        if (ENABLE_LOGGING) {
            e.printStackTrace();
        }
    }


    public static void logI(String tag, String message) {
        if (ENABLE_LOGGING) {
            Log.i(tag, message);
        }
    }

    public static void logI(String tag, String message, Throwable t) {
        if (ENABLE_LOGGING) {
            Log.i(tag, message, t);
        }
    }

    public static void logE(String tag, String message) {
        if (ENABLE_LOGGING) {
            Log.e(tag, message);
        }
    }

    public static void logE(String tag, String message, Throwable e) {
        if (ENABLE_LOGGING) {
            Log.e(tag, message, e);
        }
    }

    public static void logD(String tag, String message) {
        if (ENABLE_LOGGING) {
            Log.d(tag, message);
        }
    }

    public static void logD(String tag, String message, Throwable t) {
        if (ENABLE_LOGGING) {
            Log.d(tag, message, t);
        }
    }

    public static void logW(String tag, String message, Throwable t) {
        if (ENABLE_LOGGING) {
            Log.w(tag, message, t);
        }
    }

    public static void logW(String tag, String message) {
        if (ENABLE_LOGGING) {
            Log.w(tag, message);
        }
    }
}

