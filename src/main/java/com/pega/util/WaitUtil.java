

package com.pega.util;

public class WaitUtil {


    public WaitUtil() {

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
