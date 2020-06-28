package com.pega.workobjects;

import com.pega.*;
import com.pega.framework.*;
import com.pega.framework.elmt.*;
import com.pega.util.*;
import org.openqa.selenium.*;

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