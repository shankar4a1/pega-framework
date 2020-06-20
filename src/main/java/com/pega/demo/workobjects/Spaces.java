package com.pega.demo.workobjects;

import org.openqa.selenium.By;

import com.pega.TestEnvironment;
import com.pega.framework.PegaWebElement;
import com.pega.framework.elmt.FrameImpl;
import com.pega.util.XPathUtil;

public class Spaces extends FrameImpl {
	public Spaces(String frameID, TestEnvironment testEnv) {
		super(frameID, testEnv);
	}

	public static final By CREATE_SPACE = By.xpath(XPathUtil.getDataTestIDXpath("20180321021414044515551"));

	/**
	 * Create a new space/pega social group
	 */
	public Space_PegaSocialGroup createSpace() {
		PegaWebElement elem = findElement(CREATE_SPACE);
		elem.click();
		return new Space_PegaSocialGroup(getActiveFrameId(true), testEnv);
	}

}