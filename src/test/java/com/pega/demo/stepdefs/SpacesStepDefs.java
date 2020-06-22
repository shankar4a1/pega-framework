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

import com.pega.demo.workobjects.Space_PegaSocialGroup;
import com.pega.demo.workobjects.Spaces;
import com.pega.framework.PegaWebDriver;
import com.pega.demo.MyAppBrowser;
import com.pega.demo.MyAppObjectsBean;
import com.pega.demo.MyAppTestEnvironment;

import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

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
