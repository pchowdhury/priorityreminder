package com.phoenix2k.priorityreminder.utils;

import android.util.Log;

public class LogUtils {
    public static boolean ENABLE_LOGGING = true;

    public static void printException(Throwable e) {
        if (ENABLE_LOGGING) {
            e.printStackTrace();
        }
    }

    public static void printException(Error e){
        if(ENABLE_LOGGING){
            e.printStackTrace();
        }
    }


    public static void logI(String tag, String message){
        if(ENABLE_LOGGING){
            Log.i(tag, message);
        }
    }

    public static void logI(String tag, String message, Throwable t){
        if(ENABLE_LOGGING){
            Log.i(tag, message, t);
        }
    }

    public static void logE(String tag, String message){
        if(ENABLE_LOGGING){
            Log.e(tag, message);
        }
    }

    public static void logE(String tag, String message, Throwable e){
        if(ENABLE_LOGGING){
            Log.e(tag, message, e);
        }
    }

    public static void logD(String tag, String message){
        if(ENABLE_LOGGING){
            Log.d(tag, message);
        }
    }

    public static void logD(String tag, String message, Throwable t){
        if(ENABLE_LOGGING){
            Log.d(tag, message, t);
        }
    }

    public static void logW(String tag, String message, Throwable t){
        if(ENABLE_LOGGING){
            Log.w(tag, message, t);
        }
    }

    public static void logW(String tag, String message) {
        if(ENABLE_LOGGING){
            Log.w(tag, message);
        }
    }
}

