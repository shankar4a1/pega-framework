package com.pega.crm.workobjects;

import org.openqa.selenium.By;

import com.pega.TestEnvironment;
import com.pega.framework.PegaWebElement;
import com.pega.framework.elmt.FrameImpl;

public class Forecast extends FrameImpl {
	public Forecast(String frameID, TestEnvironment testEnv) {
		super(frameID, testEnv);
	}

	public static final By CLOSE_PLANS = By.xpath("//h3[text()='Close plans']");
	public static final By CLOSE_PLANS_TAB_SELECTED = By
			.xpath("//div[@role='tab' and @aria-label='Close plans' and @aria-selected='true']");

	/**
	 * Toggle to ClosePlans tab from forecast tab, within the same page/frame
	 */
	public void switchToClosePlans() {
		PegaWebElement elem = findElement(CLOSE_PLANS);
		elem.click();
	}

}