

package com.pega.config.util;

import java.io.*;
import java.net.*;

public class FailureAnalyzer {


    public FailureAnalyzer() {

    }

    public static void main(final String[] a) throws IOException {
        final String jenkinsUrl = System.getenv("JENKINS_URL");
        String urlPath = "";
        String urlPath2 = "";
        String urlPath3 = "";
        if (jenkinsUrl != null && jenkinsUrl.toLowerCase().contains("wvellsw7hyd")) {
            urlPath = "http://wvellsw7hyd:7070/719/postval?job=" + System.getenv("JOB_NAME").replace(" ", "%20") + "&testRun=" + System.getenv("BUILD_NUMBER");
            urlPath2 = "http://wvellsw7hyd:7070/719/getAll?job=" + System.getenv("JOB_NAME").replace(" ", "%20") + "&testRun=" + System.getenv("BUILD_NUMBER");
            urlPath3 = "http://wvellsw7hyd:7070/719/takeBackUp";
        } else {
            urlPath = "http://quality:7070/719/postval?job=" + System.getenv("JOB_NAME").replace(" ", "%20") + "&testRun=" + System.getenv("BUILD_NUMBER");
            urlPath2 = "http://quality:7070/719/getAll?job=" + System.getenv("JOB_NAME").replace(" ", "%20") + "&testRun=" + System.getenv("BUILD_NUMBER");
            urlPath3 = "http://quality:7070/719/takeBackUp";
        }
        URL url = new URL(urlPath);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            System.out.println("Given URL: " + urlPath + " is not available. It returned HTTP Code: " + responseCode);
        }
        url = new URL(urlPath2);
        connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        connection.setRequestMethod("GET");
        responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            System.out.println("Given URL: " + urlPath2 + " is not available. It returned HTTP Code: " + responseCode);
        }
        url = new URL(urlPath3);
        connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        connection.setRequestMethod("GET");
        responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            System.out.println("Given URL: " + urlPath3 + " is not available. It returned HTTP Code: " + responseCode);
        }
        final File f = new File("Link.txt");
        if (f.exists()) {
            f.delete();
        }
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileOutputStream(f));
            out.println("Failure Analysis\t" + urlPath);
        } finally {
            out.close();
        }
        out.close();
    }
}
