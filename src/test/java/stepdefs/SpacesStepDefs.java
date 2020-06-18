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

package stepdefs;

import com.google.inject.Inject;
import com.pega.TestEnvironment;
import com.pega.crm.workobjects.Space_PegaSocialGroup;
import com.pega.crm.workobjects.Spaces;
import com.pega.framework.PegaWebDriver;
import com.pega.test.pega_sample_testframework.MyAppBrowser;
import com.pega.test.pega_sample_testframework.MyAppObjectsBean;
import com.pega.test.pega_sample_testframework.MyAppTestEnvironment;

import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class SpacesStepDefs {
	TestEnvironment testEnv;
	com.pega.test.pega_sample_testframework.MyAppBrowser browser;
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
