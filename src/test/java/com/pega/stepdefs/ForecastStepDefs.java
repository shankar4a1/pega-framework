/*
 * $Id$
 *
 *
 * All rights reserved.
 *
 * This  software  has  been  provided pursuant  to  a  License
 * Agreement  containing  restrictions on  its  use.   The  software
 * contains  valuable  trade secrets and proprietary information  of
 * Pegasystems Inc and is protected by  federal
 * may  not be copied,  modified,  translated or distributed in  any
 * form or medium,  disclosed to third parties or used in any manner
 * not provided for in  said  License Agreement except with  written
 * authorization from Pegasystems Inc.
 */

package com.pega.stepdefs;

import com.google.inject.*;
import com.pega.*;
import com.pega.framework.*;
import com.pega.workobjects.*;
import io.cucumber.guice.*;
import io.cucumber.java.en.*;
import org.testng.*;



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
