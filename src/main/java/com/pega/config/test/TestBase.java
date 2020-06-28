

package com.pega.config.test;

import com.pega.*;
import com.pega.util.*;
import io.cucumber.java.*;
import org.apache.commons.io.*;
import org.openqa.selenium.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class TestBase extends TestEnvironmentImpl {
    private static final Logger LOGGER;
    private Browser browser;
    private static int passed;
    private static int failed;
    private static int total;
    private String videoFilePath;
    private boolean isBrowserInitiailized;
    protected boolean alwaysSaveVideo;
    private boolean isDebugMode;
    private Configuration configuration;
    private boolean enableVideoRecording;

    static {
        LOGGER = LoggerFactory.getLogger(TestBase.class.getName());
        TestBase.passed = 0;
        TestBase.failed = 0;
        TestBase.total = 0;
    }

    public TestBase() {
        this.browser = null;
        this.videoFilePath = null;
        this.isBrowserInitiailized = false;
        this.alwaysSaveVideo = false;
        this.isDebugMode = false;
        this.enableVideoRecording = false;
    }

    protected void configureBrowser() {
        (this.browser = this.getBrowser()).open();
        this.configuration = this.getConfiguration();
        this.isBrowserInitiailized = true;
        this.alwaysSaveVideo = this.configuration.getSUTConfig().isSaveVideoAlways();
        this.enableVideoRecording = this.configuration.getSUTConfig().isEnableVideoRecording();
    }

    private void captureScreenshot(final Scenario scenario, String desciption) {
        if (scenario.isFailed()) {
            try {
                final byte[] screenshot = ((TakesScreenshot) this.getPegaDriver().getDriver()).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", desciption);
                final File temp = ((TakesScreenshot) this.getPegaDriver().getDriver()).getScreenshotAs(OutputType.FILE);
                final File dest = new File("target/" + scenario.getName() + ".png");
                FileUtils.copyFile(temp, dest);
            } catch (Exception e) {
                scenario.log("Unable to take screenshot<br/>");
            }
        }
    }

    protected void tearDown(final Scenario scenario, final boolean performLogout) {
        this.tearDown(scenario, performLogout, this.alwaysSaveVideo);
    }

    protected void tearDown(final Scenario scenario, final boolean performLogout, final boolean saveVideoForPassedScenario) {
        this.isDebugMode = this.configuration.getSUTConfig().isDebugMode();
        this.captureScreenshot(scenario, "ScreenShot");
        if (!this.isDebugMode) {
            if (performLogout && !scenario.isFailed()) {
                try {
                    this.browser.switchToWindow(1);
                    this.logout();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            this.terminateSession();
        }
        this.captureVideo(scenario, saveVideoForPassedScenario);
    }

    public void clearTemp() {
        if (this.configuration.getPlatformDetails().isWindows()) {
            final File winTempFolder = new File(System.getenv("TEMP"));
            TestBase.LOGGER.debug("Deleting Temp folder for Windows" + winTempFolder.getAbsolutePath());
            try {
                this.deleteFiles(winTempFolder.listFiles());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            final File tempFolder = new File("/tmp");
            if (tempFolder != null) {
                TestBase.LOGGER.debug("Temp folder for linux is " + tempFolder.getAbsolutePath());
            }
        }
    }

    public void deleteFiles(final File... files) {
        for (final File file : files) {
            if (file.isDirectory() && file.listFiles() != null && file.listFiles().length != 0) {
                this.deleteFiles(file.listFiles());
            }
            try {
                file.delete();
            } catch (Exception ex) {
            }
        }
    }

    protected void initializeStatus() {
        if (TestBase.passed + TestBase.failed == 0) {
            final Properties p = new Properties();
            final File f = new File("execution.properties");
            try {
                p.load(new FileInputStream(f));
                TestBase.total = Integer.parseInt(p.getProperty("tests.total"));
            } catch (Exception e) {
                TestBase.LOGGER.debug("Unable to read execution.properties file");
            }
        }
    }

    protected void startRecording(final Scenario scenario) {
        if (this.getConfiguration().getSUTConfig().isEnableVideoRecording()) {
            String reportsDir = System.getProperty("testReportsDir", "target");
            final String jobTags = System.getenv("tags");
            if (jobTags != null) {
                if (!reportsDir.contains(jobTags)) {
                    reportsDir = reportsDir + System.getProperty("file.separator") + jobTags;
                }
            } else {
                final String feature = System.getenv("feature");
                if (feature != null) {
                    reportsDir = "LatestReports";
                    reportsDir = reportsDir + System.getProperty("file.separator") + feature;
                }
            }
            TestBase.LOGGER.debug("testReportDir is" + reportsDir);
            try {
                final String tags = scenario.getSourceTagNames().toString();
                System.out.println("scenario tags : " + tags);
                String videoFileName = null;
                if (tags != null && tags.contains("TC-")) {
                    videoFileName = this.getTCIDTag(tags);
                } else {
                    videoFileName = ((videoFileName != null) ? videoFileName : "debug");
                }
                this.videoFilePath = reportsDir + System.getProperty("file.separator") + videoFileName + ".avi";
                RecorderUtil.startRecording(reportsDir, videoFileName);
                TestBase.LOGGER.debug("Video recording started...");
            } catch (Exception e) {
                e.printStackTrace();
                TestBase.LOGGER.error("Recording could not be started");
            }
        }
    }

    protected void stopRecording() throws Exception {
        if (this.enableVideoRecording) {
            RecorderUtil.stopRecording();
        }
    }

    private void deleteVideoFile() {
        if (System.getenv("JENKINS_URL") != null) {
            try {
                final File f = new File(this.videoFilePath);
                if (f.exists()) {
                    f.delete();
                }
            } catch (Exception e) {
                TestBase.LOGGER.error("Unable to delete video file: " + this.videoFilePath, 40000);
            }
        }
    }

    private void terminateSession() {
        try {
            if (!this.isDebugMode) {
                this.terminate();
                TestBase.LOGGER.info("Browser terminated...");
            }
        } catch (Exception e) {
            TestBase.LOGGER.error("BROWSER_TERMINATE_FAILED::" + e.getMessage());
        }
    }

    private void killDrivers() {
        try {
            if (this.getConfiguration().getPlatformDetails().isLinux()) {
                Runtime.getRuntime().exec("killall chromedriver");
                TestBase.LOGGER.info("Chrome Driver killed on Centos/MAC...");
            } else if (this.getConfiguration().getBrowserConfig().getBrowserName().trim().equalsIgnoreCase("edge")) {
                TestBase.LOGGER.info("KIlling IE Edge driver TestBase1S1D");
                Runtime.getRuntime().exec("taskkill /F /IM MicrosoftWebDriver.exe");
            } else {
                Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
            }
            this.getPegaDriver().handleWaits().sleep(5L);
            TestBase.LOGGER.info("Driver processes killed...");
        } catch (IOException e) {
            TestBase.LOGGER.error("TASK_KILL_FAILED::" + e.getMessage());
        }
    }

    protected void captureVideo(final Scenario scenario, final boolean saveVideoForPassedScenario) {
        try {
            this.stopRecording();
            if (this.enableVideoRecording) {
                if (scenario.isFailed()) {
                    try {
                        String url = null;
                        String tags = System.getenv("tags");
                        System.out.println("job tags : " + tags);
                        String tcTags = this.getTCIDTag(scenario.getSourceTagNames().toString());
                        if (tcTags != null) {
                            tcTags = tcTags.trim();
                        }
                        if (tcTags != null && tcTags.endsWith("-")) {
                            tcTags = tcTags.substring(0, tags.lastIndexOf(45));
                        }
                        if (tcTags != null && tcTags.endsWith(",")) {
                            tcTags = tcTags.substring(0, tags.lastIndexOf(44));
                        }
                        if (System.getenv("job.name") != null) {
                            String buildId = System.getenv("zip.path");
                            buildId = buildId.substring(buildId.lastIndexOf(45) + 1);
                            if (tags == null) {
                                tags = System.getenv("feature");
                            }
                            url = System.getenv("JENKINS_URL") + "job/" + System.getenv("job.name") + "/ws/" + "ArchivedReports" + "/%23" + buildId + "- " + System.getenv("team.name") + "/" + tags + "/" + tcTags + ".avi";
                        } else {
                            url = System.getenv("JOB_URL") + "ws/" + "ArchivedReports" + "/%23" + System.getenv("BUILD_NUMBER") + "- " + System.getenv("team.name") + "/" + tags + "/" + tcTags + ".avi";
                        }
                        url.replace(" ", "%20");
                        scenario.log("<a target='_blank' href='" + url + "'>Execution Video</a> (This video will be available for 3 days only)");
                    } catch (Exception e) {
                        scenario.log("ERROR: Unable to add video link");
                    }
                    ++TestBase.failed;
                } else {
                    ++TestBase.passed;
                    if (!saveVideoForPassedScenario && System.getenv("save.all.videos") == null) {
                        this.deleteVideoFile();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            TestBase.LOGGER.error("ERROR: Unable to capture video: " + e.getMessage());
            scenario.log("ERROR: Unable to capture video");
        }
    }

    private String getTCIDTag(final String tags) {
        final int i = tags.indexOf("@TC");
        final int j = tags.lastIndexOf("@TC");
        if (i == -1) {
            String tag = null;
            final Pattern p = Pattern.compile("\\S+");
            final Matcher m = p.matcher(tags);
            if (m.find()) {
                tag = m.group();
            }
            return tag;
        }
        Pattern p2;
        if (i != j) {
            p2 = Pattern.compile("@TC-\\S+-.*?(\\S)+|@TC-\\S+-.*");
        } else {
            p2 = Pattern.compile("@TC-\\S+");
        }
        final Matcher k = p2.matcher(tags);
        if (k.find()) {
            return k.group().replaceAll(",", "").replaceAll("]", "");
        }
        return null;
    }

    private void logout() {
        try {
            if (this.isBrowserInitiailized) {
                this.browser.logout();
                this.browser.close();
                TestBase.LOGGER.debug("Log out successful...");
            }
        } catch (Exception e) {
            TestBase.LOGGER.error("LOGOUT_FAILED::" + e.getMessage());
        }
    }
}
