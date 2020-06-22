package com.pega.demo;

import com.google.inject.Inject;
import com.pega.Browser;
import com.pega.config.test.TestBase;


import com.pega.util.RecorderUtil;
import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@ScenarioScoped
public class MyAppTestEnvironment extends TestBase{
	private static final Logger LOGGER = LoggerFactory.getLogger(MyAppTestEnvironment.class.getName());
	
	
	String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
	String VERSION = "$Id: MyTestEnvironment.java 209030 2016-09-22 06:52:49Z SachinVellanki $";
	private String videoFilePath = null;

	private Browser browser;
	private Scenario scenario;
	private boolean isDebugMode = false;
	private boolean isBrowserInitiailized = false;
	private static int passed = 0;
	private static int failed = 0;
	private static int total = 0;
	private boolean enableVideoRecording = false;

	@Override
	@Inject
	public Browser getBrowser() {
		if (browser == null) {
			browser = new MyAppBrowser(this);
		}
		return browser;
	}

	@Before
	public void setUp(Scenario scenario) {
		this.scenario = scenario; //this object of scenario is to send to localizationUtil to take screenshot of every step failure
		System.setProperty("is.one.step.one.def", "true");
		setUp(scenario, null);
	}

	protected void setUp(Scenario scenario, String browserName) {
		initializeStatus();
		this.startRecording(scenario);
		configureBrowser();
	}

	protected void startRecording(Scenario scenario) {
		if (this.getConfiguration().getSUTConfig().isEnableVideoRecording()) {
			String reportsDir = System.getProperty("testReportsDir", "target");
			String jobTags = System.getenv("tags");
			String tags;
			if (jobTags != null) {
				if (!reportsDir.contains(jobTags)) {
					reportsDir = reportsDir + System.getProperty("file.separator") + jobTags;
				}
			} else {
				tags = System.getenv("feature");
				if (tags != null) {
					reportsDir = "LatestReports";
					reportsDir = reportsDir + System.getProperty("file.separator") + tags;
				}
			}

			LOGGER.debug("testReportDir is" + reportsDir);

			try {
				tags = scenario.getSourceTagNames().toString();
				System.out.println("scenario tags : " + tags);
				String videoFileName = null;
				if (tags != null && tags.contains("TC-")) {
					videoFileName = this.getTCIDTag(tags);
				} else {
					videoFileName = videoFileName != null ? videoFileName : "debug";
				}

				this.videoFilePath = reportsDir + System.getProperty("file.separator") + videoFileName + ".avi";
				RecorderUtil.startRecording(reportsDir, videoFileName);
				LOGGER.debug("Video recording started...");
			} catch (Exception var6) {
				var6.printStackTrace();
				LOGGER.error("Recording could not be started");
			}
		}

	}

	@After
	public void tearDown(Scenario scenario) {

		tearDown(scenario, true, alwaysSaveVideo);


	}

	private void captureScreenshot(Scenario scenario) {
		if (scenario.isFailed()) {
			try {
				final byte[] screenshot = ((TakesScreenshot) getPegaDriver().getDriver())
						.getScreenshotAs(OutputType.BYTES);
				scenario.embed(screenshot, "image/png","FailedScreenShot");
				File temp = ((TakesScreenshot) getPegaDriver().getDriver()).getScreenshotAs(OutputType.FILE);
				File dest = new File("target/" + scenario.getName() + ".png");
				FileUtils.copyFile(temp, dest);
			} catch (Exception e) {
				scenario.write("Unable to take screenshot<br/>");
			}
		}
	}

	public void takeScreenshot(){

		try {
			final byte[] screenshot = ((TakesScreenshot) getPegaDriver().getDriver())
					.getScreenshotAs(OutputType.BYTES);
			scenario.embed(screenshot, "image/png","Login page : sucessfully logged in ");
			File temp = ((TakesScreenshot) getPegaDriver().getDriver()).getScreenshotAs(OutputType.FILE);
			File dest = new File("target/" + scenario.getName() + ".png");
			FileUtils.copyFile(temp, dest);
		} catch (Exception e) {
			scenario.write("Unable to take screenshot<br/>");
		}

	}
	protected void tearDown(Scenario scenario, boolean performLogout, boolean saveVideoForPassedScenario) {
		try {
			isDebugMode = getConfiguration().getSUTConfig().isDebugMode();
			captureScreenshot(scenario);

			if (!isDebugMode) {
				if (performLogout && !scenario.isFailed()) {
					browser.switchToWindow(1);
					logout();
				}
				terminateSession();
			}

			captureVideo(scenario, saveVideoForPassedScenario);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void logout() {
		try {
			if (isBrowserInitiailized) {
				browser.logout();
				browser.close();
				LOGGER.debug("Log out successful...", true);
			}
		} catch (Exception e) {
			LOGGER.debug("LOGOUT_FAILED::" + e.getMessage(), true);
		}
	}

	public Scenario getScenario()
	{
		return scenario;
	}

	private void terminateSession() {
		try {
			if (!isDebugMode) {
				terminate();
				LOGGER.debug("Browser terminated...", true);
			}
		} catch (Exception e) {
			LOGGER.debug("BROWSER_TERMINATE_FAILED::" + e.getMessage(), true);
		}
	}


	protected void captureVideo(Scenario scenario, boolean saveVideoForPassedScenario) {
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
						scenario.write("<a target='_blank' href='" + url + "'>Execution Video</a> (This video will be available for 3 days only)");
					} catch (Exception var7) {
						scenario.write("ERROR: Unable to add video link");
					}

					++failed;
				} else {
					++passed;
					if (!saveVideoForPassedScenario && System.getenv("save.all.videos") == null) {
						this.deleteVideoFile();
					}
				}
			}
		} catch (Exception var8) {
			var8.printStackTrace();
			LOGGER.error("ERROR: Unable to capture video: " + var8.getMessage());
			scenario.write("ERROR: Unable to capture video");
		}

	}

	private void deleteVideoFile() {
		if (System.getenv("JENKINS_URL") != null) {
			try {
				File f = new File(this.videoFilePath);
				if (f.exists()) {
					f.delete();
				}
			} catch (Exception var2) {
				LOGGER.error("Unable to delete video file: " + this.videoFilePath, 40000);
			}
		}

	}

	private String getTCIDTag(String tags) {
		int i = tags.indexOf("@TC");
		int j = tags.lastIndexOf("@TC");
		if (i != -1) {
			Pattern p;
			if (i != j) {
				p = Pattern.compile("@TC-\\S+-.*?(\\S)+|@TC-\\S+-.*");
			} else {
				p = Pattern.compile("@TC-\\S+");
			}

			Matcher m = p.matcher(tags);
			return m.find() ? m.group().replaceAll(",", "").replaceAll("]", "") : null;
		} else {
			String tag = null;
			Pattern p = Pattern.compile("\\S+");
			Matcher m = p.matcher(tags);
			if (m.find()) {
				tag = m.group();
			}

			return tag;
		}
	}
	
}
