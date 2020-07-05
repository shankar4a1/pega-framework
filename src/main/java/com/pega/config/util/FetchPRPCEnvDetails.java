

package com.pega.config.util;

import com.pega.util.*;

import java.io.*;
import java.net.*;
import java.util.regex.*;

public class FetchPRPCEnvDetails {


    public FetchPRPCEnvDetails() {

    }

    private String fetchSysInfo(final String url) throws IOException {
        String sysInfo = null;
        String cookie = "";
        String uid = "administrator@pega.com";
        String pwd = "install";
        String location = null;
        final HttpURLConnection getConn = this.getRequest(url, cookie);
        getConn.setConnectTimeout(90000);
        getConn.setReadTimeout(90000);
        location = this.getHeader(getConn, "Location");
        if (location != null) {
            location = location.substring(0, location.indexOf("!STANDARD"));
            uid = URLEncoder.encode(uid, "UTF-8");
            pwd = URLEncoder.encode(pwd, "UTF-8");
            final String msgToBePosted = "pzAuth=guest&UserIdentifier=" + uid + "&Password=" + pwd + "&pyActivity%3DCode-Security.Login=";
            HttpURLConnection conn = this.postRequest(location + "!STANDARD", cookie, msgToBePosted);
            cookie = this.getHeader(conn, "Set-Cookie");
            conn = this.getRequest(location + "!STANDARD?pyActivity=GetWebInfo&=&target=popup&pzHarnessID=HID2916E64F515C56E8477629AED244CC6B", cookie);
            location = this.getHeader(conn, "Location");
            final String newUrl = location.substring(0, location.indexOf("!STANDARD")) + "!TABTHREAD1?pyActivity=%40baseclass.doUIAction&action=openRuleByKeys&pySystemName=pega&pxObjClass=Data-Admin-System&api=openRuleByKeys&ObjClass=Data-Admin-System&Format=harness&contentID=0f5226b6-6a6b-06a0-b607-22d4212c527d&dynamicContainerID=de12ecd7-b2b3-4849-bff1-ee2797014513&tabIndex=3&prevContentID=a65eaec5-03a2-4706-87dc-7683564ac82c&prevRecordkey=System%20-%20General&portalThreadName=STANDARD&portalName=Developer&pzHarnessID=HID77616B0B56966B3D1735110F19533FB1";
            conn = this.getRequest(newUrl, cookie);
            final String response = HTTPUtil.getStringFromInputStream(conn.getInputStream());
            if (response.contains("This is not a multi-tenant system")) {
                sysInfo = "<Tenant>Non-MT<Tenant>";
            } else if (response.contains("This is a multi-tenant system")) {
                sysInfo = "<Tenant>MT<Tenant>";
            }
            conn = this.getRequest(location, cookie);
            sysInfo = sysInfo + "\r\n" + HTTPUtil.getStringFromInputStream(conn.getInputStream());
        }
        return sysInfo;
    }

    public String[] getEnvInfo(final String url) {
        String xml = null;
        try {
            xml = this.fetchSysInfo(url);
        } catch (Exception ex) {
        }
        String server = "";
        String db = "";
        String os = "";
        String tenant = "";
        if (xml != null) {
            server = this.getVal(xml, "ServerInfo");
            db = this.getVal(xml, "DBProductName");
            os = this.getVal(xml, "os.name");
            tenant = this.getVal(xml, "Tenant");
        }
        return new String[]{server, db, os, tenant};
    }

    public boolean isMT(final String url) {
        final boolean type = true;
        return type;
    }

    private String getVal(final String xml, final String tag) {
        String val = "";
        final Pattern p = Pattern.compile("(<" + tag + ">)(.*?)<");
        final Matcher m = p.matcher(xml);
        if (m.find()) {
            val = m.group(2);
        }
        return val;
    }

    private HttpURLConnection postRequest(final String url, final String cookie, final String message) {
        HttpURLConnection connection = null;
        try {
            PrintWriter pw1 = null;
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.79 Safari/535.11");
            connection.setRequestProperty("Cookie", "");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", String.valueOf(message.length()));
            connection.setRequestProperty("Authorization", "Basic YWRtaW5pc3RyYXRvckBwZWdhLmNvbTppbnN0YWxs");
            connection.setRequestMethod("POST");
            connection.setInstanceFollowRedirects(false);
            (pw1 = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()), true)).print(message);
            pw1.flush();
            pw1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    private HttpURLConnection getRequest(final String url, final String cookie) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoOutput(false);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.79 Safari/535.11");
            connection.setRequestProperty("Cookie", cookie);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    private String getHeader(final HttpURLConnection connection, final String name) {
        String cookie = null;
        int i = 0;
        while (true) {
            final String headerName = connection.getHeaderFieldKey(i);
            final String headerValue = connection.getHeaderField(i);
            if (name.equals(headerName)) {
                cookie = ((cookie == null) ? (headerValue + ";") : (cookie + headerValue + ";"));
            }
            if (headerName == null && headerValue == null) {
                break;
            }
            ++i;
        }
        return cookie;
    }
}
