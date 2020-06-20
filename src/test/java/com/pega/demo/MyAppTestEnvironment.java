package com.pega.demo;

import com.google.inject.Inject;
import com.pega.Browser;
import com.pega.config.test.TestBase;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.runtime.java.guice.ScenarioScoped;


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


	public Scenario getScenario()
	{
		return scenario;
	}
	
}
