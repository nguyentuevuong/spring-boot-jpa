package com.nittsu.kinjirou.core.common;

/**
 * Created by vuongnv
 */
public class ThreadUtil {
    public static void start(Runnable runnable) {
        new Thread(runnable).start();
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}