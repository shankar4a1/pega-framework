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


@ScenarioScoped
public class SpacesStepDefs {
	TestEnvironment testEnv;
	MyAppBrowser browser;
	private PegaWebDriver pegaDriver;
	private Spaces spaces;

	@Inject
	public SpacesStepDefs(MyAppTestEnvironment testEnv) {
		this.testEnv = testEnv;
		pegaDriver = testEnv.getPegaDriver();
		browser = (MyAppBrowser) testEnv.getBrowser();

	}

	@When("^user navigates to new space wizard$")
	public void user_creates_a_new_space_with_name() {
		spaces = MyAppObjectsBean.getSpaces();
		Space_PegaSocialGroup space_PegaSocialGroup = spaces.createSpace();
		MyAppObjectsBean.setSpace_PegaSocialGroup(space_PegaSocialGroup);
	}
}
