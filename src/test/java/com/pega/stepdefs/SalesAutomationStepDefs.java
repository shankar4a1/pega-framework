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

package com.pega.stepdefs;

import com.google.inject.*;
import com.pega.*;
import com.pega.framework.*;
import com.pega.portal.*;
import com.pega.workobjects.*;
import io.cucumber.guice.*;
import io.cucumber.java.en.*;


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
