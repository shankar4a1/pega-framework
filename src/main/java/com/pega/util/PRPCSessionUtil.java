

package com.pega.util;

import com.pega.*;
import org.apache.commons.codec.binary.*;
import org.testng.*;

import java.io.*;
import java.net.*;

public class PRPCSessionUtil {
    private static String URL;
    private static String cookie;

    static {
        PRPCSessionUtil.URL = null;
        PRPCSessionUtil.cookie = null;
    }

    public PRPCSessionUtil(final String url) {
        PRPCSessionUtil.URL = url;
    }

    public static String login(String user, String password) {
        String loginString = null;
        PrintWriter pw1 = null;
        HttpURLConnection connection = null;
        try {
            user = URLEncoder.encode(user, "UTF-8");
            password = URLEncoder.encode(password, "UTF-8");
            loginString = "pzAuth=guest&UserIdentifier=" + user + "&Password=" + password + "&pyActivity%3DCode-Security.Login=";
            connection = (HttpURLConnection) new URL(PRPCSessionUtil.URL).openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.79 Safari/535.11");
            connection.setRequestProperty("Cookie", PRPCSessionUtil.cookie);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", String.valueOf(loginString.length()));
            connection.setRequestMethod("POST");
            connection.setInstanceFollowRedirects(false);
            (pw1 = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()), true)).print(loginString);
            pw1.flush();
            pw1.close();
            int i = 0;
            while (true) {
                final String headerName = connection.getHeaderFieldKey(i);
                final String headerValue = connection.getHeaderField(i);
                if ("Set-Cookie".equals(headerName)) {
                    PRPCSessionUtil.cookie = ((PRPCSessionUtil.cookie == null) ? headerValue : (PRPCSessionUtil.cookie + headerValue + ";"));
                }
                if (headerName == null && headerValue == null) {
                    break;
                }
                ++i;
            }
        } catch (IOException e) {
            PRPCSessionUtil.cookie = null;
        }
        return PRPCSessionUtil.cookie;
    }

    public static void importRAP(String filePath, final TestEnvironment testEnv) throws IOException {
        Reporter.log("Importing the RAP...", true);
        HTTPUtil.handleSSLCertificates();
        filePath = filePath.replace("\\", File.separator);
        HttpURLConnection conn = null;
        final File config = testEnv.getConfigFile();
        if (config != null) {
            filePath = config.getParent() + File.separator + filePath;
        }
        Reporter.log(filePath, true);
        final File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("Given path for RAP does not exist");
        }
        String prpcUrl = testEnv.getConfiguration().getSUTConfig().getURL();
        if (prpcUrl.contains("PRServlet")) {
            prpcUrl = prpcUrl.substring(0, prpcUrl.indexOf("PRServlet"));
        } else {
            prpcUrl = prpcUrl + "/";
        }
        final String archiveName = DataUtil.getRandomNumberString("selenium.RAP.") + ".jar";
        final String restUrl = prpcUrl + "PRRestService" + "/BuildSmoke/Utility/ImportRAP?archivename=" + archiveName + "&keep=false";
        FileInputStream fileInputStream = null;
        try {
            HTTPUtil.handleSSLCertificates();
            if (prpcUrl.startsWith("https://")) {
                System.setProperty("https.protocols", "TLSv1.2");
            }
            final URL url = new URL(restUrl);
            conn = (HttpURLConnection) url.openConnection();
            final String userPassword = "newbuildsmokedeveloper:pega";
            final String encoding = Base64.encodeBase64String(userPassword.getBytes());
            final String name = file.getName().substring(0, file.getName().indexOf(46));
            final String fileName = file.getName();
            final String boundary = "*****";
            URLConnection.setFileNameMap(null);
            conn.setDoOutput(true);
            conn.setConnectTimeout(120000);
            conn.setInstanceFollowRedirects(false);
            conn.setDefaultUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Basic " + encoding);
            conn.addRequestProperty("Connection", "Keep-Alive");
            conn.addRequestProperty("Cache-Control", "no-cache");
            conn.addRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            final DataOutputStream request = new DataOutputStream(conn.getOutputStream());
            final byte[] bFile = new byte[(int) file.length()];
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            request.writeBytes("--" + boundary + "\r\n");
            request.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"; fileName=\"" + fileName + "\"\r\n");
            request.writeBytes("Content-Type: application/octet-stream\r\n");
            request.writeBytes("\r\n");
            request.write(bFile);
            request.writeBytes("\r\n");
            request.writeBytes("--" + boundary + "--\r\n");
            request.flush();
            request.close();
            final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                Reporter.log(inputLine, true);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            fileInputStream.close();
            conn.disconnect();
        }
        fileInputStream.close();
        conn.disconnect();
    }

    public static void cloneOperator(final TestEnvironment testEnv, final String aSourceOperator, final String aTargetOperator) {
        cloneOperator(testEnv, aSourceOperator, aTargetOperator, false);
    }

    public static void cloneOperator(final TestEnvironment testEnv, final String aSourceOperator, final String aTargetOperator, final boolean aKeepCaseType) {
        HTTPUtil.handleSSLCertificates();
        HttpURLConnection conn = null;
        final String aURL = testEnv.getConfiguration().getSUTConfig().getURL();
        final int indx = (aURL.indexOf("PRServlet") == -1) ? aURL.length() : aURL.indexOf("PRServlet");
        String restURL = aURL.substring(0, indx);
        try {
            restURL = (restURL.endsWith("/") ? restURL : new StringBuilder(restURL).append("/").toString()) + "PRRestService/BuildSmoke/Utility/CloneOperator?";
            restURL = restURL + "SourceOperator=" + URLEncoder.encode(aSourceOperator, "UTF-8");
            restURL = restURL + "&TargetOperator=" + URLEncoder.encode(aTargetOperator, "UTF-8");
            restURL = restURL + "&KeepCaseType=" + aKeepCaseType;
            System.out.println("REST URL: '" + restURL + "'");
            final URL url = new URL(restURL);
            conn = (HttpURLConnection) url.openConnection();
            final String userPassword = "newbuildsmokedeveloper:pega";
            final String encoding = Base64.encodeBase64String(userPassword.getBytes());
            conn.setConnectTimeout(60000);
            conn.setInstanceFollowRedirects(false);
            conn.setDefaultUseCaches(false);
            conn.setRequestProperty("Authorization", "Basic " + encoding);
            conn.addRequestProperty("Connection", "Keep-Alive");
            conn.addRequestProperty("Cache-Control", "no-cache");
            final int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                final String inputLine = in.readLine();
                in.close();
                throw new Exception("Bad HTTP Status Code from REST call. " + responseCode + ":" + inputLine);
            }
            final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final String inputLine = in.readLine();
            in.close();
            if (!inputLine.startsWith("SUCCESS")) {
                throw new Exception("REST call didn't succeed: " + inputLine);
            }
            Reporter.log("Operator " + aTargetOperator + " successfully cloned from " + aSourceOperator, true);
            testEnv.getBrowser().setNonClonedUser(aSourceOperator);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            conn.disconnect();
        }
        conn.disconnect();
    }

    public static void oatuhCloneOperator(final TestEnvironment testEnv, final String aSourceOperator, final String aTargetOperator, final boolean aKeepCaseType, final String tokenID) {
        HTTPUtil.handleSSLCertificates();
        HttpURLConnection conn = null;
        final String aURL = testEnv.getConfiguration().getSUTConfig().getURL();
        final int indx = (aURL.indexOf("PRServlet") == -1) ? aURL.length() : aURL.indexOf("PRServlet");
        String restURL = aURL.substring(0, indx);
        try {
            restURL = (restURL.endsWith("/") ? restURL : new StringBuilder(restURL).append("/").toString()) + "PRRestService/OAuth20Package/Utility/CloneOperator?";
            restURL = restURL + "SourceOperator=" + URLEncoder.encode(aSourceOperator, "UTF-8");
            restURL = restURL + "&TargetOperator=" + URLEncoder.encode(aTargetOperator, "UTF-8");
            restURL = restURL + "&KeepCaseType=" + aKeepCaseType;
            System.out.println("REST URL: '" + restURL + "'");
            final URL url = new URL(restURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(60000);
            conn.setInstanceFollowRedirects(false);
            conn.setDefaultUseCaches(false);
            System.out.println("TokenID:" + tokenID);
            conn.setRequestProperty("Authorization", "bearer " + tokenID);
            final int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                final String inputLine = in.readLine();
                in.close();
                throw new Exception("Bad HTTP Status Code from REST call. " + responseCode + ":" + inputLine);
            }
            final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final String inputLine = in.readLine();
            in.close();
            if (!inputLine.startsWith("SUCCESS")) {
                throw new Exception("REST call didn't succeed: " + inputLine);
            }
            Reporter.log("Operator " + aTargetOperator + " successfully cloned from " + aSourceOperator, true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            conn.disconnect();
        }
        conn.disconnect();
    }

    public static void addUIKitToApplication(final TestEnvironment testEnv, final String application, final String applicationVersion) {
        HTTPUtil.handleSSLCertificates();
        HttpURLConnection conn = null;
        final String aURL = testEnv.getConfiguration().getSUTConfig().getURL();
        final int indx = (aURL.indexOf("PRServlet") == -1) ? aURL.length() : aURL.indexOf("PRServlet");
        String restURL = aURL.substring(0, indx);
        try {
            restURL = (restURL.endsWith("/") ? restURL : new StringBuilder(restURL).append("/").toString()) + "PRRestService/BuildSmoke/Utility/AddUIKitToApplication?";
            restURL = restURL + "&appName=" + URLEncoder.encode(application, "UTF-8");
            restURL = restURL + "&appVersion=" + applicationVersion;
            System.out.println("REST URL: '" + restURL + "'");
            final URL url = new URL(restURL);
            conn = (HttpURLConnection) url.openConnection();
            final String userPassword = "newbuildsmokedeveloper:pega";
            final String encoding = Base64.encodeBase64String(userPassword.getBytes());
            conn.setConnectTimeout(60000);
            conn.setInstanceFollowRedirects(false);
            conn.setDefaultUseCaches(false);
            conn.setRequestProperty("Authorization", "Basic " + encoding);
            conn.addRequestProperty("Connection", "Keep-Alive");
            conn.addRequestProperty("Cache-Control", "no-cache");
            final int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                final String inputLine = in.readLine();
                in.close();
                throw new Exception("Bad HTTP Status Code from REST call. " + responseCode + ":" + inputLine);
            }
            final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final String inputLine = in.readLine();
            in.close();
            if (!inputLine.startsWith("SUCCESS")) {
                throw new Exception("REST call didn't succeed: " + inputLine);
            }
            Reporter.log("UIToolKit is successfully added to application " + application, true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            conn.disconnect();
        }
        conn.disconnect();
    }

    public static void addUIKitToApplication(final String aURL, final String application, final String applicationVersion, final boolean addExpressUIKit, final int insertAtPosition) throws Exception {
        HTTPUtil.handleSSLCertificates();
        HttpURLConnection conn = null;
        final int indx = (aURL.indexOf("PRServlet") == -1) ? aURL.length() : aURL.indexOf("PRServlet");
        String restURL = aURL.substring(0, indx);
        try {
            restURL = (restURL.endsWith("/") ? restURL : new StringBuilder(restURL).append("/").toString()) + "PRRestService/BuildSmoke/Utility/AddUIKitToApplication?";
            restURL = restURL + "appName=" + URLEncoder.encode(application, "UTF-8");
            restURL = restURL + "&appVersion=" + applicationVersion;
            restURL = restURL + "&insertAtPosition=" + insertAtPosition;
            Reporter.log("REST URL: '" + restURL + "'", true);
            final URL url = new URL(restURL);
            conn = (HttpURLConnection) url.openConnection();
            final String userPassword = "newbuildsmokedeveloper:pega";
            final String encoding = Base64.encodeBase64String(userPassword.getBytes());
            conn.setConnectTimeout(60000);
            conn.setInstanceFollowRedirects(false);
            conn.setDefaultUseCaches(false);
            conn.setRequestProperty("Authorization", "Basic " + encoding);
            conn.addRequestProperty("Connection", "Keep-Alive");
            conn.addRequestProperty("Cache-Control", "no-cache");
            final int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                final String inputLine = in.readLine();
                in.close();
                throw new Exception("Bad HTTP Status Code from REST call. " + responseCode + ":" + inputLine);
            }
            final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final String inputLine = in.readLine();
            in.close();
            if (!inputLine.startsWith("SUCCESS")) {
                throw new Exception("REST call didn't succeed: " + inputLine);
            }
            Reporter.log("UIToolKit is successfully added to application " + application, true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            conn.disconnect();
        }
        conn.disconnect();
    }

    public static void addUIKitToRuleset(final String aURL, final String ruleset, final String rulesetVersion) throws Exception {
        HTTPUtil.handleSSLCertificates();
        HttpURLConnection conn = null;
        final int indx = (aURL.indexOf("PRServlet") == -1) ? aURL.length() : aURL.indexOf("PRServlet");
        String restURL = aURL.substring(0, indx);
        try {
            restURL = (restURL.endsWith("/") ? restURL : new StringBuilder(restURL).append("/").toString()) + "PRRestService/BuildSmoke/Utility/AddUIKitToRuleset?";
            restURL = restURL + "rulesetName=" + URLEncoder.encode(ruleset, "UTF-8");
            restURL = restURL + "&rulesetVersion=" + rulesetVersion;
            Reporter.log("REST URL: '" + restURL + "'", true);
            final URL url = new URL(restURL);
            conn = (HttpURLConnection) url.openConnection();
            final String userPassword = "newbuildsmokedeveloper:pega";
            final String encoding = Base64.encodeBase64String(userPassword.getBytes());
            conn.setConnectTimeout(60000);
            conn.setInstanceFollowRedirects(false);
            conn.setDefaultUseCaches(false);
            conn.setRequestProperty("Authorization", "Basic " + encoding);
            conn.addRequestProperty("Connection", "Keep-Alive");
            conn.addRequestProperty("Cache-Control", "no-cache");
            final int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                final String inputLine = in.readLine();
                in.close();
                throw new Exception("Bad HTTP Status Code from REST call. " + responseCode + ":" + inputLine);
            }
            final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final String inputLine = in.readLine();
            in.close();
            if (!inputLine.startsWith("SUCCESS")) {
                throw new Exception("REST call didn't succeed: " + inputLine);
            }
            Reporter.log("UIToolKit is successfully added to Ruleset" + ruleset, true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            conn.disconnect();
        }
        conn.disconnect();
    }

    public static void addRulesetToApplication(final String aURL, final String application, final String applicationVersion, final String rulesetNameWithVersion) throws Exception {
        HTTPUtil.handleSSLCertificates();
        HttpURLConnection conn = null;
        final int indx = (aURL.indexOf("PRServlet") == -1) ? aURL.length() : aURL.indexOf("PRServlet");
        String restURL = aURL.substring(0, indx);
        try {
            restURL = (restURL.endsWith("/") ? restURL : new StringBuilder(restURL).append("/").toString()) + "PRRestService/BuildSmoke/Utility/AddRulesetToApplication?";
            restURL = restURL + "SourceApplicationName=" + URLEncoder.encode(application, "UTF-8");
            restURL = restURL + "&SourceApplicationVersion=" + applicationVersion;
            restURL = restURL + "&RulesetName=" + rulesetNameWithVersion;
            Reporter.log("REST URL: '" + restURL + "'", true);
            final URL url = new URL(restURL);
            conn = (HttpURLConnection) url.openConnection();
            final String userPassword = "newbuildsmokedeveloper:pega";
            final String encoding = Base64.encodeBase64String(userPassword.getBytes());
            conn.setConnectTimeout(60000);
            conn.setInstanceFollowRedirects(false);
            conn.setDefaultUseCaches(false);
            conn.setRequestProperty("Authorization", "Basic " + encoding);
            conn.addRequestProperty("Connection", "Keep-Alive");
            conn.addRequestProperty("Cache-Control", "no-cache");
            final int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                final String inputLine = in.readLine();
                in.close();
                throw new Exception("Bad HTTP Status Code from REST call. " + responseCode + ":" + inputLine);
            }
            final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final String inputLine = in.readLine();
            in.close();
            if (!inputLine.startsWith("SUCCESS")) {
                throw new Exception("REST call didn't succeed: " + inputLine);
            }
            Reporter.log("UIToolKit is successfully added to application " + application, true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            conn.disconnect();
        }
        conn.disconnect();
    }
}
