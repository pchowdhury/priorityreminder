package com.phoenix2k.priorityreminder.utils;

import android.text.TextUtils;
import android.util.Log;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.phoenix2k.priorityreminder.model.Project;

import java.util.ArrayList;
import java.util.List;

public class LogUtils {
    public static boolean ENABLE_LOGGING = true;


    public static void printListOfListOfList(List<List<List<Object>>> updates) {
        if (ENABLE_LOGGING) {
            String str = "";
            for (List<List<Object>> update : updates) {
                for (List<Object> row : update) {
                    for (int i = 0; i < row.size(); i++) {
                        String value = (String) row.get(i);
                        if (i != 0) {
                            str += ", ";
                        }
                        str += value;
                    }
                    str += "\n";
                }
                str += "\n------------\n";
            }
            Log.i("Tables", str);
        }
    }

    public static void printListOfLists(List<List<Object>> values) {
        if (ENABLE_LOGGING) {
            String str = "";
            if (values != null) {
                for (List row : values) {
                    for (int i = 0; i < row.size(); i++) {
                        String value = (String) row.get(i);
                        if (i != 0) {
                            str += ", ";
                        }
                        str += value;
                    }
                    str += "\n";
                }
            }
            Log.i("List", str);
        }
    }

    public static void printList(List<Object> values) {
        if (ENABLE_LOGGING) {
            String str = "";
            if (values != null) {
                    for (int i = 0; i < values.size(); i++) {
                        String value = (String) values.get(i);
                        if (i != 0) {
                            str += ", ";
                        }
                        str += value;
                    }
                    str += "\n";
            }
            Log.i("List", str);
        }
    }

    public static void printFileList(List<com.google.api.services.drive.model.File> files) {
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

