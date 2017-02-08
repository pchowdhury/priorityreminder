package com.phoenix2k.priorityreminder.utils;

/**
 * Created by Pushpan on 08/02/17.
 */

public class DataUtils {
    public static int parseIntValue(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            LogUtils.printException(e);
        }
        return 0;
    }

    public static long parseLongValue(String value) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            LogUtils.printException(e);
        }
        return 0;
    }
}
