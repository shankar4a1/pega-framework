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
import io.cucumber.guice.*;
import io.cucumber.java.en.*;


@ScenarioScoped
public class TestStepDefs {
	TestEnvironment testEnv;
	MyAppBrowser browser;
	private PegaWebDriver pegaDriver;

	@Inject
	public TestStepDefs(MyAppTestEnvironment testEnv) {
		this.testEnv = testEnv;
		pegaDriver = testEnv.getPegaDriver();
		browser = (MyAppBrowser) testEnv.getBrowser();

	}

	@Given("^A User prints \"([^\"]*)\"$")
	public void a_User_prints(String arg1) throws Throwable {
		System.out.println("Printing " + arg1);
	}

}
