package com.phoenix2k.priorityreminder.utils;

import android.graphics.Color;

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

    public static String getColorCode(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }

    public static int getColorCode(String color) {
        try {
            return Integer.parseInt(color.replaceFirst("#", ""), 16);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getPaleColor(int argb) {
        float[] hsv = new float[3];
        Color.colorToHSV(argb, hsv);
        hsv[1] /= 3;
        hsv[2] = (2+hsv[2])/3;
        return Color.HSVToColor(hsv);
    }
}
