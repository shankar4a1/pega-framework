

package com.pega.util;

import org.apache.http.*;

public class SubUIUtil {


    public SubUIUtil() {

    }

    public static void startRemoteTracing(final HttpResponse response) {
        System.out.println("Script paused for 15 seconds \n\nStart Remote Tracer with below session details.....");
        System.out.println(response.getFirstHeader("Set-Cookie"));
        try {
            Thread.sleep(15000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void stopRemoteTracing() {
        System.out.println("Script paused for 15 seconds \n\nYou can pause Remote Tracer now");
        try {
            Thread.sleep(15000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
