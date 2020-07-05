

package com.pega.config.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.*;
import org.testng.*;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.security.*;
import java.security.cert.*;
import java.util.*;
import java.util.regex.*;

public class ImportUIKit {


    private String pega7ExpRulesets;
    private static String buildwatcherApps;
    private static String pega7ExpApps;

    public ImportUIKit() {

    }

    public static void main(final String[] args) throws Exception {
        final ImportUIKit config = new ImportUIKit();
        final String url = System.getenv("instance.url");
        if (System.getenv("JENKINS_URL") != null) {
            final String runMode = System.getProperty("runMode");
            if (runMode != null && (runMode.trim().equalsIgnoreCase("cucumber") || runMode.trim().equalsIgnoreCase("Cucumber-OneStepDef"))) {
                final Collection<String> tagsExtracted = config.readTagsFromCucumber();
                final String applications = config.getApplicationsForTag(tagsExtracted);
                final String rulesets = config.getRulesetsForTag(tagsExtracted);
                if (applications != null && !applications.equals("")) {
                    boolean addExpressUIKit = false;
                    int insertAtPosition = -1;
                    if (applications.equals(ImportUIKit.pega7ExpApps)) {
                        addExpressUIKit = true;
                    }
                    final String[] allApps = applications.split(",");
                    String[] array;
                    for (int length = (array = allApps).length, i = 0; i < length; ++i) {
                        final String application = array[i];
                        insertAtPosition = (application.contains("BuildSmokeApp") ? 4 : -1);
                        final String appName = application.split(":")[0];
                        final String appVersion = application.split(":")[1];
                        config.addUIKitToApplication(url, appName, appVersion, addExpressUIKit, insertAtPosition);
                    }
                }
                if (rulesets != null && !rulesets.equals("")) {
                    final String[] allRulesets = rulesets.split(",");
                    String[] array2;
                    for (int length2 = (array2 = allRulesets).length, j = 0; j < length2; ++j) {
                        final String ruleset = array2[j];
                        final String rulesetName = ruleset.split(":")[0];
                        final String rulesetVersion = ruleset.split(":")[1];
                        config.addUIKitToRuleset(url, rulesetName, rulesetVersion);
                    }
                }
            }
        }
    }

    public static void main1(final String[] args) throws Exception {
        final ImportUIKit config = new ImportUIKit();
        final String url = "http://vengwindb192:9891/prweb/PRServlet";
        final Collection<String> tagsExtracted = new ArrayList<String>();
        tagsExtracted.add("@pega7express");
        tagsExtracted.add("@gmail.com[0m[36m\"");
        tagsExtracted.add("@pega.com'");
        tagsExtracted.add("@pega.com[0m[36m");
        tagsExtracted.add("@ PRPCTests");
        final String applications = config.getApplicationsForTag(tagsExtracted);
        final String rulesets = config.getRulesetsForTag(tagsExtracted);
        System.out.println("Applications: " + applications);
        System.out.println("Pega7Applications: " + ImportUIKit.pega7ExpApps);
        System.out.println("Pega7ExpApps = Applications?" + applications.equals(ImportUIKit.pega7ExpApps));
        System.out.println("Ruleset Applications: " + rulesets);
        if (applications != null && !applications.equals("")) {
            boolean addExpressUIKit = false;
            int insertAtPosition = -1;
            if (applications.equals(ImportUIKit.pega7ExpApps)) {
                addExpressUIKit = true;
            }
            final String[] allApps = applications.split(",");
            String[] array;
            for (int length = (array = allApps).length, i = 0; i < length; ++i) {
                final String application = array[i];
                insertAtPosition = (application.contains("BuildSmokeApp") ? 4 : -1);
                final String appName = application.split(":")[0];
                final String appVersion = application.split(":")[1];
                config.addUIKitToApplication(url, appName, appVersion, addExpressUIKit, insertAtPosition);
            }
        }
    }

    public void addUIKitToApplication(final String aURL, final String application, final String applicationVersion, final boolean addExpressUIKit, final int insertAtPosition) throws Exception {
        this.handleSSLCertificates();
        HttpURLConnection conn = null;
        final int indx = (aURL.indexOf("PRServlet") == -1) ? aURL.length() : aURL.indexOf("PRServlet");
        String restURL = aURL.substring(0, indx);
        try {
            restURL = (restURL.endsWith("/") ? restURL : new StringBuilder(restURL).append("/").toString()) + "PRRestService/BuildSmoke/Utility/AddUIKitAsBuiltOnApp?";
            restURL = restURL + "appName=" + URLEncoder.encode(application, "UTF-8");
            restURL = restURL + "&appVersion=" + applicationVersion;
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

    public void addUIKitToRuleset(final String aURL, final String ruleset, final String rulesetVersion) throws Exception {
        this.handleSSLCertificates();
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

    private void handleSSLCertificates() throws Exception {
        Reporter.log("All SSL Certificates will be accepted...", true);
        final TrustManager[] trustAllCerts = {new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(final X509Certificate[] certs, final String authType) {
            }

            @Override
            public void checkServerTrusted(final X509Certificate[] certs, final String authType) {
            }
        }};
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
        } catch (Exception e) {
            throw new Exception("Unable to install all trusting trust manager for accepting all SSL certificates...");
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        final HostnameVerifier allHostsValid = new HostnameVerifier() {
            @Override
            public boolean verify(final String hostname, final SSLSession session) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

    private Collection<String> readTagsFromCucumber() throws Exception {
        String logFileName = System.getProperty("log.file");
        if (logFileName == null) {
            logFileName = "log.txt";
        }
        final File f = new File(logFileName);
        if (f.exists()) {
            final String fileData = FileUtils.readFileToString(f);
            final Pattern pattern = Pattern.compile("\\d+(?=\\sScenarios)");
            final Matcher matcher = pattern.matcher(fileData);
            if (matcher.find()) {
                System.setProperty("tests.total", matcher.group());
            }
            if (System.getProperty("tests.total") != null) {
                Reporter.log("Total scenarios executing: " + System.getProperty("tests.total"), true);
                final Properties p = new Properties();
                final File f2 = new File("execution.properties");
                if (f2.exists()) {
                    f2.delete();
                }
                final FileOutputStream fos = new FileOutputStream(f2);
                p.setProperty("tests.total", System.getProperty("tests.total"));
                p.store(fos, null);
                fos.close();
            }
            return this.readTagsForUIKitImport(fileData);
        }
        return null;
    }

    private Collection<String> readTagsForUIKitImport(final String fileData) throws IOException {
        final Pattern pattern = Pattern.compile("@.*?[^-\\s][TC+].*");
        final Matcher matcher = pattern.matcher(fileData);
        String tag = null;
        final Set<String> set = new HashSet<String>();
        while (matcher.find()) {
            tag = matcher.group();
            tag = tag + " ";
            final Pattern p = Pattern.compile("(@[^TC].+?)\\s");
            final Matcher m = p.matcher(tag);
            while (m.find()) {
                set.add(m.group().trim());
            }
        }
        Reporter.log("Scenario Identified: " + set.toString(), true);
        return set;
    }

    public String getApplicationsForTag(final Collection<String> tags) throws Exception {
        if (tags != null) {
            Properties tagApplicationProperties = null;
            if (tagApplicationProperties == null) {
                tagApplicationProperties = new Properties();
                try {
                    File f = new File("TagsAndApplications.properties");
                    if (!f.exists()) {
                        String filePath = "Data\\TagsAndApplications.properties";
                        filePath = filePath.replace("\\", System.getProperty("file.separator"));
                        f = new File(filePath);
                    }
                    tagApplicationProperties.load(new FileInputStream(f));
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new Exception(" Unable to read TagsAndApplications.propertiess file");
                }
            }
            String applications = "";
            Reporter.log("All tags identified: " + tags, true);
            for (final String tag : tags) {
                Reporter.log("Fetching Applications for: " + tag, true);
                if (tagApplicationProperties.containsKey(tag)) {
                    final String application = tagApplicationProperties.getProperty(tag);
                    if (tag.equalsIgnoreCase("@cvt")) {
                        ImportUIKit.buildwatcherApps = application;
                    }
                    if (tag.equalsIgnoreCase("@pega7express")) {
                        ImportUIKit.pega7ExpApps = application;
                        System.out.println("Pega& Express tags: " + ImportUIKit.pega7ExpApps);
                    }
                    applications = applications + application + ",";
                }
            }
            Reporter.log("Applications for UIKit: " + applications, true);
            if (!applications.equals("")) {
                applications = applications.substring(0, applications.lastIndexOf(","));
            }
            Reporter.log("Applications for UIKit: " + applications, true);
            return applications;
        }
        return null;
    }

    public String getRulesetsForTag(final Collection<String> tags) throws Exception {
        if (tags != null) {
            Properties tagRulesetsProperties = null;
            if (tagRulesetsProperties == null) {
                tagRulesetsProperties = new Properties();
                try {
                    File f = new File("TagsAndRulesets.properties");
                    if (!f.exists()) {
                        String filePath = "Data\\TagsAndRulesets.properties";
                        filePath = filePath.replace("\\", System.getProperty("file.separator"));
                        f = new File(filePath);
                    }
                    tagRulesetsProperties.load(new FileInputStream(f));
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new Exception(" Unable to read TagsAndRulesets.propertiess file");
                }
            }
            String rulesets = "";
            Reporter.log("All tags identified: " + tags, true);
            for (final String tag : tags) {
                Reporter.log("Fetching Rulesets for: " + tag, true);
                if (tagRulesetsProperties.containsKey(tag)) {
                    final String ruleset = tagRulesetsProperties.getProperty(tag);
                    if (tag.equalsIgnoreCase("@pega7express")) {
                        this.pega7ExpRulesets = rulesets;
                        System.out.println("Pega& Express tags for rulesets: " + this.pega7ExpRulesets);
                    }
                    rulesets = rulesets + ruleset + ",";
                }
            }
            Reporter.log("Rulesets for UIKit: " + rulesets, true);
            if (!rulesets.equals("")) {
                rulesets = rulesets.substring(0, rulesets.lastIndexOf(","));
            }
            Reporter.log("Rulesets for UIKit: " + rulesets, true);
            return rulesets;
        }
        return null;
    }
}
