

package com.pega.util;

import java.io.*;
import java.net.*;

public class JenkinsSessionUtil {
    public String login(final String jenkinsURL, String uid, String pwd) throws IOException {
        String cookie = "";
        final String url = "http://" + jenkinsURL + "/j_acegi_security_check";
        final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        uid = URLEncoder.encode(uid, "UTF-8");
        pwd = URLEncoder.encode(pwd, "UTF-8");
        final String loginStr = "j_username=" + uid + "&j_password=" + pwd + "&from=%2F&json=%7B%22j_username%22%3A+%22" + uid + "%22%2C+%22j_password%22%3A+%22" + pwd + "%22%2C+%22remember_me%22%3A+false%2C+%22from%22%3A+%22%2F%22%7D&Submit=log+in";
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.79 Safari/535.11");
        connection.setRequestProperty("Cookie", cookie);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", String.valueOf(loginStr.length()));
        connection.setInstanceFollowRedirects(false);
        final PrintWriter pw1 = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()), true);
        pw1.print(loginStr);
        pw1.flush();
        pw1.close();
        int i = 0;
        while (true) {
            final String headerName = connection.getHeaderFieldKey(i);
            final String headerValue = connection.getHeaderField(i);
            if ("Set-Cookie".equals(headerName)) {
                cookie = cookie + headerValue + ";";
            }
            if (headerName == null && headerValue == null) {
                break;
            }
            ++i;
        }
        return cookie;
    }
}
