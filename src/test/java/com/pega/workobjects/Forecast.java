package com.pega.workobjects;

import com.pega.*;
import com.pega.framework.*;
import com.pega.framework.elmt.*;
import org.openqa.selenium.*;

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