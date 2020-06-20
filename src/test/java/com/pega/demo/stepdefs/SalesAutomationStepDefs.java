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

import com.google.inject.Inject;
import com.pega.TestEnvironment;
import com.pega.demo.portal.SalesAutomationPortal;

import com.pega.demo.workobjects.Forecast;
import com.pega.demo.workobjects.Spaces;
import com.pega.framework.PegaWebDriver;
import com.pega.demo.MyAppBrowser;
import com.pega.demo.MyAppObjectsBean;
import com.pega.demo.MyAppTestEnvironment;

import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class SalesAutomationStepDefs {
	TestEnvironment testEnv;
	MyAppBrowser browser;
	private PegaWebDriver pegaDriver;
	private SalesAutomationPortal salesManagerPortal;

	@Inject
	public SalesAutomationStepDefs(MyAppTestEnvironment testEnv) {
		this.testEnv = testEnv;
		pegaDriver = testEnv.getPegaDriver();
		browser = (MyAppBrowser) testEnv.getBrowser();
		salesManagerPortal = browser.getPortal(SalesAutomationPortal.class);

	}

	@When("^user opens the forecast workobjects page$")
	public void user_opens_the_forecast_workobjects_page() {
		Forecast forecast = salesManagerPortal.openForecast();
		MyAppObjectsBean.setForecast(forecast); // Sets the forecast object returned by openForecast method in a static
												// variable, so that can be retrieved in other step definition classes
	}

	@When("^user opens the spaces workobjects page$")
	public void user_opens_the_spaces_workobjects_page() {
		Spaces spaces = salesManagerPortal.openSpaces();
		MyAppObjectsBean.setSpaces(spaces);
	}
}
