

package com.pega.util;

public class WaitUtil {
    String COPYRIGHT;
    private static final String VERSION = "$Id: WaitUtil.java 125139 2015-02-22 15:23:22Z SachinVellanki $";

    public WaitUtil() {
        this.COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    }

    public static void waitForShort() {
        try {
            Thread.sleep(500L);
        } catch (InterruptedException ex) {
        }
    }

    public static void waitForMedium() {
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException ex) {
        }
    }

    public static void waitForLong() {
        try {
            Thread.sleep(30000L);
        } catch (InterruptedException ex) {
        }
    }

    public static void waitForVeryLong() {
        try {
            Thread.sleep(300000L);
        } catch (InterruptedException ex) {
        }
    }
}
