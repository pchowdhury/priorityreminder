package com.phoenix2k.priorityreminder.utils;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

@SuppressLint("FloatMath")
public class DeviceUtils {
    private static String TAG = "DeviceUtils";

    public static int getAndroidSDKAPILevel() {
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.FROYO) {
            // Do something for froyo and above versions
        } else {
            // do something for phones running an SDK before froyo
        }
        return currentapiVersion;
    }

    public static int getScreenWidth(Context context) {
        int width = 0;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        if (getAndroidSDKAPILevel() < 13) {
            width = display.getWidth();
        } else {
            Point size = new Point();
            try {
                Method method = display.getClass().getDeclaredMethod("getSize",
                        Point.class);
                method.invoke(display, size);
            } catch (Exception e) {
                e.printStackTrace();
            }
            width = size.x;
        }
        return width;
    }

    public static int getScreenHeight(Context context) {
        int height = 0;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        if (getAndroidSDKAPILevel() < 13) {
            height = display.getHeight();
        } else {
            Point size = new Point();
            try {
                Method method = display.getClass().getDeclaredMethod("getSize",
                        Point.class);
                method.invoke(display, size);
            } catch (Exception e) {
                e.printStackTrace();
            }
            height = size.y;
        }
        return height;
    }

    public static int getStatusBarHeight(Activity context) {
        return (int) Math
                .ceil(25 * context.getResources().getDisplayMetrics().density);
    }

    public static boolean isSmallDevice(Activity activity) {
        if (activity != null) {
            return (getScreenWidth(activity) <= 320);
        }
        return false;
    }

    public static String getPhoneNumber(Activity activity) {
        String phoneNumber = "";
        try {
            TelephonyManager tm = (TelephonyManager) activity
                    .getApplicationContext().getSystemService(
                            Context.TELEPHONY_SERVICE);
            phoneNumber = tm.getLine1Number();
        } catch (Exception e) {
            Log.e("DeviceUtils", "Couldn't find phone number.");
        }
        return phoneNumber;
    }

    public static String getLocalIpAddress() {
        String ipAddress = null;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        ipAddress = Formatter.formatIpAddress(inetAddress
                                .hashCode());
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TAG, ex.toString());
        }
        Log.i(TAG, "IP Address = " + ipAddress);
        return ipAddress;
    }

    public static String getWifFiIPAddress(Activity activity) {
        String wiFiIp = null;
        try {
            WifiManager wim = (WifiManager) activity.getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE);
            @SuppressWarnings("unused")
            List<WifiConfiguration> l = wim.getConfiguredNetworks();
            // WifiConfiguration wc = l.get(0);
            wiFiIp = Formatter.formatIpAddress(wim.getConnectionInfo()
                    .getIpAddress());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        Log.i(TAG, "Wifi IP Address = " + wiFiIp);
        return wiFiIp;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static String getCarrierName(Context context) {
        String carrierName = "NA";
        try {
            TelephonyManager manager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            carrierName = manager.getNetworkOperatorName();
        } catch (Exception e) {

        }
        if (carrierName == null || carrierName.trim().length() == 0) {
            carrierName = "NA";
        }
        return carrierName;
    }
}
