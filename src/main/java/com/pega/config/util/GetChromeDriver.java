

package com.pega.config.util;

import com.pega.util.*;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class GetChromeDriver {
    private static final Logger LOGGER;

    static {
        LOGGER = LoggerFactory.getLogger(GetChromeDriver.class.getName());
    }

    public static void getChromeDriverForChrome() {
        try {
            final String majorVersion = getLocalChromeVersion();
            String requiredVersion = null;
            String currentVersion = "";

            if (new File("binaries").exists()) {
                try {
                    currentVersion = FileUtil.readFile("binaries" + System.getProperty("file.separator") + "do_not_checkin_chromedriverversion.txt");
                } catch (Exception ex) {
                }
            } else {
                new File("binaries").mkdir();
            }
            String latestReleaseVersion = "https://chromedriver.storage.googleapis.com/LATEST_RELEASE";
            if (Integer.parseInt(majorVersion) > 72) {
                latestReleaseVersion = "https://chromedriver.storage.googleapis.com/LATEST_RELEASE_" + Integer.parseInt(majorVersion);
                final HttpClient client = HttpClientBuilder.create().build();
                HttpGet getRequest = new HttpGet(latestReleaseVersion);
                HttpResponse response = client.execute(getRequest);
                String latestVersion = getResponseAsString(response);
                if (latestVersion.toLowerCase().contains("error")) {
                    getRequest = new HttpGet("https://chromedriver.storage.googleapis.com/LATEST_RELEASE_" + (Integer.parseInt(majorVersion) - 1));
                    response = client.execute(getRequest);
                    latestVersion = getResponseAsString(response);
                    if (latestVersion.toLowerCase().contains("error")) {
                        getRequest = new HttpGet("https://chromedriver.storage.googleapis.com/LATEST_RELEASE");
                        response = client.execute(getRequest);
                        latestVersion = getResponseAsString(response);
                    }
                }
                requiredVersion = latestVersion;
            } else {
                requiredVersion = getChromeDriverVersion(majorVersion);
            }
            if (requiredVersion != null) {
                GetChromeDriver.LOGGER.info("Chrome Version: " + majorVersion + "\tChromeDriver version needed:" + requiredVersion);
                if (requiredVersion.equals(currentVersion)) {
                    GetChromeDriver.LOGGER.info("Required chromedriver version is already available. Not attempting to download any new version");
                    return;
                }
                getChromeDriver(requiredVersion);
            }
        } catch (Exception e) {
            GetChromeDriver.LOGGER.info("Encountered error while checking for chrome driver required for the current chrome version: " + e.getMessage() + "\n");
            GetChromeDriver.LOGGER.info("\nUsing the existing chromedriver in binaries folder and proceeding with the test\n");
        }
    }

    private static void getChromeDriver(final String chromeDriverVersion) throws IOException {
        GetChromeDriver.LOGGER.info("Downloading the required chrome driver");
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            downloadChromeDriver("https://chromedriver.storage.googleapis.com/" + chromeDriverVersion + "/chromedriver_win32.zip");
        } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            downloadChromeDriver("https://chromedriver.storage.googleapis.com/" + chromeDriverVersion + "/chromedriver_mac64.zip");
        } else if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            downloadChromeDriver("https://chromedriver.storage.googleapis.com/" + chromeDriverVersion + "/chromedriver_linux64.zip");
        }
        FileUtil.writeToFileInUTF8("binaries" + System.getProperty("file.separator") + "do_not_checkin_chromedriverversion.txt", chromeDriverVersion);
    }

    private static void downloadChromeDriver(final String url) throws IOException {
        final HttpClient client = HttpClientBuilder.create().build();
        final HttpGet getRequest = new HttpGet(url);
        final HttpResponse response = client.execute(getRequest);
        final InputStream is = response.getEntity().getContent();
        final String filePath = "binaries" + System.getProperty("file.separator") + "chromeDriver.zip";
        Label_0217:
        {
            try {
                Throwable t = null;
                try {
                    final FileOutputStream fos = new FileOutputStream(new File(filePath));
                    try {
                        int inByte;
                        while ((inByte = is.read()) != -1) {
                            fos.write(inByte);
                        }
                    } finally {
                        if (fos != null) {
                            fos.close();
                        }
                    }
                } finally {
                    if (t == null) {
                        final Throwable exception = new Throwable();
                        t = exception;
                    } else {
                        final Throwable exception = null;
                        if (t != exception) {
                            t.addSuppressed(exception);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Exception is  " + e);
                break Label_0217;
            } finally {
                is.close();
            }
            is.close();
        }
        FileUtil.unzip("binaries" + System.getProperty("file.separator") + "chromeDriver.zip", "binaries", null);
        new File(filePath).delete();
    }

    private static String getResponseAsString(final HttpResponse response) throws UnsupportedOperationException, IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String text = "";
        String line;
        while ((line = reader.readLine()) != null) {
            text = text + "\n" + line;
        }
        return text.replaceFirst("\\\n", "");
    }

    private static String getResponseAsString(final BufferedReader reader) throws UnsupportedOperationException, IOException {
        String text = "";
        String line;
        while ((line = reader.readLine()) != null) {
            text = text + "\n" + line;
        }
        return text;
    }

    private static Properties chromeProps(final String chromeDriverNotes) {
        final Properties props = new Properties();
        final Pattern pattern = Pattern.compile(".*ChromeDriver v(\\d.\\d*).*\\nSupports.*v\\d*-(\\d*)");
        final Matcher matcher = pattern.matcher(chromeDriverNotes);
        while (matcher.find()) {
            props.put(matcher.group(2), matcher.group(1));
        }
        try {
            props.store(new FileOutputStream(new File("binaries" + System.getProperty("file.separator") + "do_not_checkin_chrome2chromedriver.properties")), "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }

    private static String getLocalChromeVersion() throws Exception {
        String output = null;
        String majorVersion = null;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            String chromeBrowserPath = "";
            final String userDir = System.getProperty("user.home").split("\\\\")[2];
            if (FileUtil.isFileExists("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe")) {
                chromeBrowserPath = "C:\\\\Program Files (x86)\\\\Google\\\\Chrome\\\\Application\\\\chrome.exe";
            } else if (FileUtil.isFileExists("C:\\Users\\" + userDir + "\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe")) {
                chromeBrowserPath = "C:\\\\Users\\\\" + userDir + "\\\\AppData\\\\Local\\\\Google\\\\Chrome\\\\Application\\\\chrome.exe";
            } else {
                if (!FileUtil.isFileExists("C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe")) {
                    throw new Exception("Couldn't file path for chrome.exe, using the existing chromedriver to launch chrome using selenium");
                }
                chromeBrowserPath = "C:\\\\Program Files\\\\Google\\\\Chrome\\\\Application\\\\chrome.exe";
            }
            output = getResponseAsString(new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("cmd /c wmic datafile where name=\"" + chromeBrowserPath + "\" get Version /value").getInputStream())));
            majorVersion = output.replaceAll("\\\n", "").replace("Version=", "").split("\\.")[0];
        } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            final String macOutput = getResponseAsString(new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("/Applications/Google\\ Chrome.app/Contents/MacOS/Google\\ Chrome --version").getInputStream())));
            majorVersion = macOutput.replaceAll("\\\n", "").replace("Google Chrome ", "").split("\\.")[0];
        } else if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            final String linuxOutput = getResponseAsString(new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("google-chrome -version").getInputStream())));
            majorVersion = linuxOutput.replaceAll("\\\n", "").replace("Google Chrome ", "").split("\\.")[0];
        }
        return majorVersion;
    }

    private static String getChromeDriverVersion(final String chromeVersion) {
        final Map<String, String> chromeDriverVersions = new HashMap<String, String>();
        chromeDriverVersions.put("46", "2.18");
        chromeDriverVersions.put("47", "2.19");
        chromeDriverVersions.put("48", "2.20");
        chromeDriverVersions.put("50", "2.21");
        chromeDriverVersions.put("52", "2.22");
        chromeDriverVersions.put("53", "2.23");
        chromeDriverVersions.put("54", "2.24");
        chromeDriverVersions.put("55", "2.25");
        chromeDriverVersions.put("56", "2.27");
        chromeDriverVersions.put("57", "2.28");
        chromeDriverVersions.put("58", "2.29");
        chromeDriverVersions.put("60", "2.30");
        chromeDriverVersions.put("61", "2.32");
        chromeDriverVersions.put("62", "2.33");
        chromeDriverVersions.put("63", "2.34");
        chromeDriverVersions.put("64", "2.35");
        chromeDriverVersions.put("65", "2.36");
        chromeDriverVersions.put("66", "2.37");
        chromeDriverVersions.put("67", "2.38");
        chromeDriverVersions.put("68", "2.39");
        chromeDriverVersions.put("69", "2.41");
        chromeDriverVersions.put("70", "2.42");
        chromeDriverVersions.put("71", "2.43");
        chromeDriverVersions.put("72", "2.45");
        chromeDriverVersions.put("73", "2.46");
        return chromeDriverVersions.get(chromeVersion);
    }
}
