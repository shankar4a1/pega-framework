

package com.pega.config;

import com.pega.config.util.*;
import com.pega.exceptions.*;
import com.pega.util.*;
import org.apache.commons.io.*;
import org.testng.*;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.security.*;
import java.security.cert.*;
import java.util.*;
import java.util.regex.*;

public class Configuration {
    String COPYRIGHT;
    private static final String VERSION = "$Id: Configuration.java 208557 2016-09-08 05:18:51Z SachinVellanki $";
    private Properties globalProperties;
    private File globalPropertiesFile;

    public Configuration() {
        this.COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    }

    public static void main(final String[] args) throws IOException {
        final Configuration config = new Configuration();
        String url = null;
        if (System.getenv("JENKINS_URL") != null) {
            final String runMode = System.getProperty("runMode");
            if (runMode != null && (runMode.trim().equalsIgnoreCase("cucumber") || runMode.trim().equalsIgnoreCase("cucumber-onestepdef"))) {
                config.readTagsForCucumber();
            } else {
                String tags = System.getProperty("tags");
                if (tags == null) {
                    tags = args[0];
                }
                config.readTagsForTestNG(tags);
            }
            config.cleanUpDirs();
            config.loadProperties();
            url = config.updateInstanceURLForAutoTriggers();
            if (url == null) {
                url = System.getenv("instance.url");
            }
            if (url == null) {
                url = System.getProperty("instance.url");
            }
            if (!System.getProperty("checkAvailability", "yes").equalsIgnoreCase("no")) {
                config.checkURLAvailability(url);
            }
            Reporter.log("prpcurl=" + url, true);
            final String[] details = config.fetchPRPCDetails(url);
            final String browserName = config.getBrowserName();
            try {
                final File f = new File("execution.properties");
                if (!f.exists()) {
                    f.createNewFile();
                }
                final Properties p = new Properties();
                p.load(new FileInputStream("execution.properties"));
                p.setProperty("build.name", details[4]);
                p.setProperty("server.info", details[0]);
                p.setProperty("prpc.url", url);
                p.setProperty("browser.name", browserName);
                p.setProperty("os.name", details[2]);
                p.setProperty("db.product.name", details[1]);
                p.setProperty("tenant", details[3]);
                final FileOutputStream fos = new FileOutputStream("execution.properties", true);
                p.store(fos, null);
                fos.close();
            } catch (Exception e) {
                Reporter.log("Unable to write PRPC details to propeties file", true);
            }
            Reporter.log("build.name=" + details[4], true);
            Reporter.log("ServerInfo: " + details[0], true);
            Reporter.log("DBProductName: " + details[1], true);
            Reporter.log("OS Name: " + details[2], true);
            Reporter.log("Tenant: " + details[3], true);
            if (System.getenv("test.type") != null && System.getenv("test.type").equalsIgnoreCase("subui")) {
                config.mergeABranch(url, "MyBranch");
            }
            return;
        }
        throw new PegaWebDriverException("Execution is not triggered from Jenkins");
    }

    private void readTagsForCucumber() throws IOException {
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
                final FileOutputStream fos = new FileOutputStream("execution.properties", true);
                p.setProperty("tests.total", System.getProperty("tests.total"));
                p.store(fos, null);
                fos.close();
            }
            final String browserName = System.getenv("browser.name");
            if (browserName.trim().equalsIgnoreCase("chrome")) {
                if (System.getenv("use.branch.ids") != null || System.getProperty("use.branch.ids") != null) {
                    this.writeTagsForBranch(fileData, "@notchromecompatible");
                } else {
                    this.writeTagsForVE(fileData, "@notchromecompatible");
                }
            } else if (browserName.trim().equalsIgnoreCase("firefox")) {
                this.writeTagsForVE(fileData, "@notfirefoxcompatible");
            } else {
                if (!browserName.trim().equalsIgnoreCase("internet explorer") && !browserName.trim().equalsIgnoreCase("edge")) {
                    throw new PegaWebDriverException("Invalid browser. Supported browsers are Chrome, Internet Explorer and Firefox");
                }
                this.writeTagsForVE(fileData, "@notiecompatible");
            }
        }
    }

    private void readTagsForTestNG(final String tags) throws IOException {
        Reporter.log("Tags: " + tags, true);
        String includeGroups = "";
        String excludeGroups = "";
        if (tags.contains(",")) {
            final String[] multipleTags = tags.split(",");
            for (int i = 0; i < multipleTags.length; ++i) {
                if (multipleTags[i].startsWith("~")) {
                    excludeGroups = multipleTags[i].trim() + ",";
                } else {
                    includeGroups = multipleTags[i].trim() + ",";
                }
            }
        } else if (tags.startsWith("~")) {
            excludeGroups = tags.trim() + ",";
        } else {
            includeGroups = tags.trim() + ",";
        }
        if (includeGroups.endsWith(",")) {
            includeGroups = includeGroups.substring(0, includeGroups.lastIndexOf(44));
        }
        if (excludeGroups.endsWith(",")) {
            excludeGroups = excludeGroups.substring(0, excludeGroups.lastIndexOf(44));
        }
        includeGroups = includeGroups.replace("@", "");
        excludeGroups = excludeGroups.replace("@", "");
        Reporter.log("Include groups: " + includeGroups, true);
        Reporter.log("Exclude groups: " + excludeGroups, true);
        System.setProperty("include.groups", includeGroups);
        System.setProperty("exclude.groups", excludeGroups);
        final Properties p = new Properties();
        final File f = new File("execution.properties");
        if (f.exists()) {
            f.delete();
        }
        final FileOutputStream fos = new FileOutputStream(f);
        p.setProperty("include.groups", includeGroups);
        p.setProperty("exclude.groups", excludeGroups);
        p.store(fos, null);
        fos.close();
    }

    private void loadProperties() throws IOException {
        this.globalProperties = new Properties();
        final File f = DataUtil.getGlobalSettingsFile();
        Reporter.log("Searching for global properties at: " + f.getAbsolutePath(), true);
        this.globalProperties.load(new FileInputStream(f));
    }

    private String getBrowserName() {
        String browserName = System.getenv("browser.name");
        if (browserName == null) {
            browserName = this.globalProperties.getProperty("browser.name");
        }
        return browserName;
    }

    private String updateInstanceURLForAutoTriggers() throws IOException {
        String prpcUrl = null;
        if (System.getProperty("autoTrigger") != null) {
            final String autoTriggerUrl = "http://vengwin158/rr-head-mt/rrbuild.properties";
            Reporter.log("Getting instance details from: " + autoTriggerUrl, true);
            this.downloadFile(autoTriggerUrl, System.getProperty("user.dir"));
            final Properties properties = new Properties();
            properties.load(new FileInputStream(new File("rrbuild.properties")));
            prpcUrl = properties.getProperty("prpcurl");
            Reporter.log("prpcurl=" + prpcUrl, true);
            Reporter.log("build.name=" + properties.getProperty("build"), true);
            this.globalProperties.setProperty("instance.url", prpcUrl);
            final FileOutputStream fos = new FileOutputStream(this.globalPropertiesFile);
            this.globalProperties.store(fos, null);
            fos.close();
            Reporter.log("Updated instance.url property with the value retrieved from : " + autoTriggerUrl, true);
        }
        return prpcUrl;
    }

    private String[] fetchPRPCDetails(final String prpcUrl) {
        final FetchPRPCDetails fetchDetails = new FetchPRPCDetails();
        final String[] details = fetchDetails.getEnvInfo(prpcUrl);
        return details;
    }

    private void downloadFile(final String fileURL, final String saveDir) throws IOException {
        final URL url = new URL(fileURL);
        final HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        final int responseCode = httpConn.getResponseCode();
        Label_0262:
        {
            if (responseCode == 200) {
                String fileName = "";
                final String disposition = httpConn.getHeaderField("Content-Disposition");
                if (disposition != null) {
                    final int index = disposition.indexOf("filename=");
                    if (index > 0) {
                        fileName = disposition.substring(index + 10, disposition.length() - 1);
                    }
                } else {
                    fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1);
                }
                final InputStream inputStream = httpConn.getInputStream();
                FileOutputStream outputStream = null;
                final String saveFilePath = saveDir + File.separator + fileName;
                try {
                    outputStream = new FileOutputStream(saveFilePath);
                    int bytesRead = -1;
                    final byte[] buffer = new byte[4096];
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                } catch (Exception ex) {
                    break Label_0262;
                } finally {
                    outputStream.close();
                    inputStream.close();
                }
                outputStream.close();
                inputStream.close();
            } else {
                Reporter.log("No file to download. Server replied HTTP code: " + responseCode, true);
            }
        }
        httpConn.disconnect();
        Reporter.log("Download file from: " + url, true);
    }

    private String getPRPCVersion(final String prpcUrl) throws IOException {
        this.checkURLAvailability(prpcUrl);
        final URL url = new URL(prpcUrl);
        Reporter.log("Retrieving build details from url: " + prpcUrl, true);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(60000);
        conn.setReadTimeout(30000);
        final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        final StringBuffer response = new StringBuffer();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            if (inputLine.contains("core") || inputLine.contains("cached")) {
                return inputLine.trim();
            }
            response.append(inputLine);
        }
        in.close();
        return null;
    }

    private void cleanUpDirs() throws IOException {
        final File f1 = new File(System.getenv("WORKSPACE") + System.getProperty("file.separator") + "LatestReports");
        final File f2 = new File(System.getenv("WORKSPACE") + System.getProperty("file.separator") + "AggregatedReports");
        if (f1.exists()) {
            FileUtils.deleteDirectory(f1);
        }
        if (f2.exists()) {
            FileUtils.deleteDirectory(f2);
        }
        f1.mkdir();
        f2.mkdir();
    }

    private void resetTestExecutionStatus() throws IOException {
        final String filePath = "Data/BuildWatcher/TestRunExecutionStatus.html";
        String template = "";
        BufferedWriter writer = null;
        try {
            final Scanner sc = new Scanner(new File(filePath));
            while (sc.hasNextLine()) {
                final String currLine = sc.nextLine();
                template = template + currLine + "\r\n";
            }
            sc.close();
            template = template.replaceFirst("\\['\\d+',[\\d,\\s]*\\]", "['0', 0, 0, 0]");
            writer = new BufferedWriter(new FileWriter(new File(filePath)));
            writer.write(template);
        } catch (IOException e) {
            System.out.println("Unable to reset test execution status which was last updated by previous run");
            return;
        } finally {
            writer.close();
        }
        writer.close();
    }

    private void checkURLAvailability(final String url) throws IOException {
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
            throw new PegaWebDriverException("Unable to install all trusting trust manager for accepting all SSL certificates...");
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        final HostnameVerifier allHostsValid = new HostnameVerifier() {
            @Override
            public boolean verify(final String hostname, final SSLSession session) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        Reporter.log("Checking availability of URL: " + url, true);
        final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setConnectTimeout(180000);
        connection.setReadTimeout(180000);
        connection.setRequestMethod("HEAD");
        final int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new PegaWebDriverException("Given URL is not available. It returned HTTP Code: " + responseCode);
        }
    }

    private void writeTagsForBranch(final String fileData, final String tagToIgnore) throws IOException {
        final Pattern pattern = Pattern.compile("@[^-\\s].*");
        final Matcher matcher = pattern.matcher(fileData);
        final Set<String> tagSet = new LinkedHashSet<String>();
        final Set<String> ignoredTagSet = new LinkedHashSet<String>();
        String tag = null;
        String tags = "";
        while (matcher.find()) {
            tag = matcher.group();
            final int i = tag.indexOf("@br");
            final int j = tag.lastIndexOf("@br");
            Matcher m;
            if (i != j) {
                final Pattern p = Pattern.compile("@br-\\d+-.*?(\\s)|@br-\\d+-.*");
                m = p.matcher(tag);
            } else {
                final Pattern p = Pattern.compile("@br-.*");
                m = p.matcher(tag);
            }
            if (tag.contains(tagToIgnore)) {
                if (!m.find()) {
                    continue;
                }
                ignoredTagSet.add(m.group().trim());
            } else {
                if (!m.find()) {
                    continue;
                }
                final String exactTag = m.group().trim();
                if (ignoredTagSet.contains(exactTag)) {
                    continue;
                }
                String realTag = "";
                if (exactTag.contains(" ")) {
                    realTag = exactTag.substring(0, exactTag.indexOf(" "));
                } else {
                    realTag = exactTag;
                }
                final boolean mergeStatus = this.getMergeStatus(realTag);
                if (!mergeStatus) {
                    continue;
                }
                tagSet.add(realTag);
            }
        }
        final Iterator<String> iter = tagSet.iterator();
        while (iter.hasNext()) {
            tags = tags + iter.next() + ",";
        }
        if (tags.endsWith(",")) {
            tags = tags.substring(0, tags.lastIndexOf(44));
        }
        final Properties p2 = new Properties();
        final File f1 = new File("ve.properties");
        if (f1.exists()) {
            f1.delete();
        }
        final FileOutputStream fos = new FileOutputStream(f1);
        p2.setProperty("tags", tags);
        p2.store(fos, null);
        fos.close();
        if (ignoredTagSet.size() > 0) {
            Reporter.log("Tests that are not compatible to be executed with selected browser are: " + ignoredTagSet, true);
        }
    }

    private boolean getMergeStatus(final String tag) {
        try {
            String branchName = "";
            if (tag.startsWith("@br-")) {
                branchName = tag.substring(4);
            } else {
                branchName = tag;
            }
            final URL url = new URL("http://vcentossde1:8080/prweb/PRRestService/MergePackage/Status/IsBranchMerged?branchName=" + branchName);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(60000);
            conn.setReadTimeout(30000);
            final String response = IOUtils.toString(conn.getInputStream());
            System.out.println("Branch name: " + branchName);
            System.out.println("Response: " + response);
            return response.contains("true");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void writeTagsForVE(final String fileData, final String tagToIgnore) throws IOException {
        final Pattern pattern = Pattern.compile("@[^-\\s].*");
        final Matcher matcher = pattern.matcher(fileData);
        final Set<String> tagSet = new LinkedHashSet<String>();
        final Set<String> ignoredTagSet = new LinkedHashSet<String>();
        String tag = null;
        String tags = "";
        while (matcher.find()) {
            tag = matcher.group();
            final int i = tag.indexOf("@TC");
            final int j = tag.lastIndexOf("@TC");
            Matcher m;
            if (i != j) {
                Reporter.log("more than one tag : " + tag, true);
                Pattern p = Pattern.compile("@TC-\\d+-[^ ]+");
                m = p.matcher(tag);
                if (!m.find()) {
                    p = Pattern.compile("@TC-\\d+");
                    m = p.matcher(tag);
                    if (m.find()) {
                        System.out.println("group value : " + m.group());
                    }
                }
                m.reset();
            } else {
                final Pattern p = Pattern.compile("@TC-\\d+");
                m = p.matcher(tag);
            }
            if (tag.contains(tagToIgnore)) {
                if (!m.find()) {
                    continue;
                }
                ignoredTagSet.add(m.group().trim());
            } else {
                if (!m.find()) {
                    continue;
                }
                final String exactTag = m.group().trim();
                if (ignoredTagSet.contains(exactTag)) {
                    continue;
                }
                tagSet.add(exactTag);
            }
        }
        final Iterator<String> iter = tagSet.iterator();
        while (iter.hasNext()) {
            tags = tags + iter.next() + ",";
        }
        if (tags.endsWith(",")) {
            tags = tags.substring(0, tags.lastIndexOf(44));
        }
        final Properties p2 = new Properties();
        final File f1 = new File("ve.properties");
        if (f1.exists()) {
            f1.delete();
        }
        final FileOutputStream fos = new FileOutputStream(f1);
        p2.setProperty("tags", tags);
        p2.store(fos, null);
        fos.close();
        if (ignoredTagSet.size() > 0) {
            Reporter.log("Tests that are not compatible to be executed with selected browser are: " + ignoredTagSet, true);
        }
    }

    public void mergeABranch(final String aURL, final String branchName) {
        try {
            this.handleSSLCertificates();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        HttpURLConnection conn = null;
        final int indx = (aURL.indexOf("PRServlet") == -1) ? aURL.length() : aURL.indexOf("PRServlet");
        String restURL = aURL.substring(0, indx);
        try {
            restURL = (restURL.endsWith("/") ? restURL : new StringBuilder(restURL).append("/").toString()) + "PRRestService/BuildSmoke/Application/Merge?UserIdentifier=BuildSmokeDeveloper&Password=cGVnYQ%3D%3D&branchlist=" + branchName;
            Reporter.log("REST URL: '" + restURL + "'", true);
            final URL url = new URL(restURL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(60000);
            conn.setInstanceFollowRedirects(false);
            conn.setDefaultUseCaches(false);
            conn.addRequestProperty("Connection", "Keep-Alive");
            conn.addRequestProperty("Cache-Control", "no-cache");
            final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final String inputLine = in.readLine();
            in.close();
            if (!inputLine.contains("no rules to merge") && !inputLine.contains("Merge Completed for branch")) {
                throw new Exception("REST call for rules merge didn't succeed: " + inputLine);
            }
            System.out.println(inputLine);
        } catch (Exception e2) {
            if (!e2.getMessage().contains("HTTP response code: 400")) {
                throw new RuntimeException(e2.getMessage() + "\nREST call for rules merge didn't succeed");
            }
            return;
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
}
