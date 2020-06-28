package com.pega;

import com.google.inject.*;
import com.pega.config.test.*;
import io.cucumber.guice.*;
import io.cucumber.java.*;
import org.apache.commons.io.*;
import org.openqa.selenium.*;

import java.io.*;


@ScenarioScoped
public class MyAppTestEnvironment extends TestBase{
	
	
	String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
	String VERSION = "$Id: MyTestEnvironment.java 209030 2016-09-22 06:52:49Z SachinVellanki $";

	private Browser browser;
	private Scenario scenario;

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
		startRecording(scenario);
		configureBrowser();
	}

	@After
	public void tearDown(Scenario scenario) {

		tearDown(scenario, true, alwaysSaveVideo);


	}


	public Scenario getScenario() {
		return scenario;
	}

	//altered by SG
	private void captureScreenshot(Scenario scenario, String description) {
		if (scenario.isFailed()) {
			try {
				final byte[] screenshot = ((TakesScreenshot) getPegaDriver().getDriver())
						.getScreenshotAs(OutputType.BYTES);
				scenario.attach(screenshot, "image/png", description);
				File temp = ((TakesScreenshot) getPegaDriver().getDriver()).getScreenshotAs(OutputType.FILE);
				File dest = new File("target/" + scenario.getName() + ".png");
				FileUtils.copyFile(temp, dest);
			} catch (Exception e) {
				scenario.log("Unable to take screenshot<br/>");
			}
		}
	}


}
