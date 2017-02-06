package com.phoenix2k.priorityreminder.utils;

import java.util.UUID;

/**
 * Created by Pushpan on 06/02/17.
 */

public class IdGenerator {
//    public static int generateUniqueId() {
//        UUID idOne = UUID.randomUUID();
//        String str = "" + idOne;
//        int uid = str.hashCode();
//        String filterStr = "" + uid;
//        str = filterStr.replaceAll("-", "");
//        return Integer.parseInt(str);
//    }

    public static long generateUniqueId() {
        return System.nanoTime();
    }

}
