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

import com.pega.demo.workobjects.Space_PegaSocialGroup;
import org.testng.Assert;

import com.google.inject.Inject;
import com.pega.TestEnvironment;

import com.pega.framework.PegaWebDriver;
import com.pega.demo.MyAppBrowser;
import com.pega.demo.MyAppObjectsBean;
import com.pega.demo.MyAppTestEnvironment;

import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@ScenarioScoped
public class Space_PegaSocialGroupStepDefs {
	TestEnvironment testEnv;
	MyAppBrowser browser;
	private PegaWebDriver pegaDriver;
	private Space_PegaSocialGroup space_PegaSocialGroup;
	private String name;

	@Inject
	public Space_PegaSocialGroupStepDefs(MyAppTestEnvironment testEnv) {
		this.testEnv = testEnv;
		pegaDriver = testEnv.getPegaDriver();
		browser = (MyAppBrowser) testEnv.getBrowser();

	}

	@When("^creates a new space with name \"([^\"]*)\"$")
	public void user_creates_a_new_space_with_name(String name) {
		this.name = MyAppObjectsBean.putTimeStampedValue(name); // putTimeStampedValue method appends a random time
																// stamp value to the end of given argument and stores
																// it in a variable, it can be retrieved at a later
																// stage using getTimeStampedValue using the same
																// argument
		space_PegaSocialGroup = MyAppObjectsBean.getSpace_PegaSocialGroup();
		space_PegaSocialGroup.createSpace(this.name);
	}

	@Then("^the new space should be successfully created$")
	public void the_new_space_should_be_successfully_created() {
		Assert.assertTrue(space_PegaSocialGroup.verifySpaceHeader(name));
	}

}
