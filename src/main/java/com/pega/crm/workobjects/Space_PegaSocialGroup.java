package com.pega.crm.workobjects;

import org.openqa.selenium.By;

import com.pega.TestEnvironment;
import com.pega.framework.elmt.FrameImpl;
import com.pega.util.XPathUtil;

public class Space_PegaSocialGroup extends FrameImpl {
	public Space_PegaSocialGroup(String frameID, TestEnvironment testEnv) {
		super(frameID, testEnv);
	}

	public static final By NAME = By.xpath(XPathUtil.getDataTestIDXpath("20180404041454077323962"));
	public static final By DESCRIPTION = By.xpath(XPathUtil.getDataTestIDXpath("20180321013709017931436"));
	public static final By DONE = By.xpath("//button[text()='Done']");

	/**
	 * Toggle to ClosePlans tab from forecast tab, within the same page/frame
	 */
	public void createSpace(String name) {
		findElement(NAME).sendKeys(name);
		findElement(DESCRIPTION).sendKeys(name);
		findElement(DONE).click();
	}

	public boolean verifySpaceHeader(String spaceName) {
		return verifyElement(By.xpath("//*[contains(@class,'header-title') and text()='" + spaceName + "']"));
	}

}