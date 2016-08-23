package com.qfg.ctu.util;

/**
 * Created by rbtq on 7/31/16.
 */
public class StringUtil {
    public static String megastrip(String str, boolean left, boolean right, String what) {
        if(str == null) {
            return null;
        } else {
            int limitLeft = 0;

            int limitRight;
            for(limitRight = str.length() - 1; left && limitLeft <= limitRight && what.indexOf(str.charAt(limitLeft)) >= 0; ++limitLeft) {
                ;
            }

            while(right && limitRight >= limitLeft && what.indexOf(str.charAt(limitRight)) >= 0) {
                --limitRight;
            }

            return str.substring(limitLeft, limitRight + 1);
        }
    }

    public static String lstrip(String str) {
        return megastrip(str, true, false, " \r\n\t　   ");
    }

    public static String rstrip(String str) {
        return megastrip(str, false, true, " \r\n\t　   ");
    }

    public static String strip(String str) {
        return str == null?"":megastrip(str, true, true, " \r\n\t　   ");
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.length() == 0; // string.isEmpty() in Java 6
    }
}
