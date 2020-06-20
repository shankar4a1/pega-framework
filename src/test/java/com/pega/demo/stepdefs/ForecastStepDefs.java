/*
 * $Id$
 *
 * Copyright (c) 2018  Pegasystems Inc.
 * All rights reserved.
 *
 * This  software  has  been  provided pursuant  to  a  License
 * Agreement  containing  restrictions on  its  use.   The  software
 * contains  valuable  trade secrets and proprietary information  of
 * Pegasystems Inc and is protected by  federal   copyright law.  It
 * may  not be copied,  modified,  translated or distributed in  any
 * form or medium,  disclosed to third parties or used in any manner
 * not provided for in  said  License Agreement except with  written
 * authorization from Pegasystems Inc.
*/

package com.pega.demo.stepdefs;

import com.pega.demo.workobjects.Forecast;
import org.testng.Assert;

import com.google.inject.Inject;
import com.pega.TestEnvironment;

import com.pega.framework.PegaWebDriver;
import com.pega.demo.MyAppBrowser;
import com.pega.demo.MyAppObjectsBean;
import com.pega.demo.MyAppTestEnvironment;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class ForecastStepDefs {
	TestEnvironment testEnv;
	MyAppBrowser browser;
	private PegaWebDriver pegaDriver;
	private Forecast forecast;

	@Inject
	public ForecastStepDefs(MyAppTestEnvironment testEnv) {
		this.testEnv = testEnv;
		pegaDriver = testEnv.getPegaDriver();
		browser = (MyAppBrowser) testEnv.getBrowser();

	}

	@When("^switches to close plans tab$")
	public void switches_to_close_plans_tab() {
		forecast = MyAppObjectsBean.getForecast(); // This is the object that was set in SalesManagerStepDefs while
													// opening the forecast page
		forecast.switchToClosePlans();

	}

	@Then("^close plans view should be available$")
	public void close_plans_view_should_be_available() {
		Assert.assertTrue(forecast.verifyElement(Forecast.CLOSE_PLANS_TAB_SELECTED));
	}

}
