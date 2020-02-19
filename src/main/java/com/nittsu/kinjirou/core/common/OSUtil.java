package com.nittsu.kinjirou.core.common;

/**
 * Created by vuongnv
 */

import java.util.Locale;

public class OSUtil {
    private static String OS = System.getProperty("os.name", "unknown").toLowerCase(Locale.ROOT);

    public static boolean isWindows() {
        return OS.contains("win");
    }

    public static boolean isMac() {
        return OS.contains("mac");
    }

    public static boolean isUnix() {
        return OS.contains("nux");
    }
}