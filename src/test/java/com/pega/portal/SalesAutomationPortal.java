package com.pega.portal;

import com.pega.*;
import com.pega.framework.*;
import com.pega.page.*;
import com.pega.util.*;
import com.pega.workobjects.*;
import org.openqa.selenium.*;

public class SalesAutomationPortal extends PortalImpl {

	public static final By FORECAST = By.xpath("//span[text()='Forecast']");
	public static final By SPACES = By.xpath("//span[text()='Spaces']");

	public SalesAutomationPortal(TestEnvironment testEnv) {
		super(testEnv);
	}

	public Forecast openForecast() {
		pegaDriver.switchTo().frame("FormFactoriFrame");
		PegaWebElement element = findElement(FORECAST);
		element.click();
		ScreenshotUtil.captureScreenshot(testEnv, "Forecast Page");
		return new Forecast(getActiveFrameId(true), testEnv);
	}

	public Spaces openSpaces() {
		pegaDriver.switchTo().frame("FormFactoriFrame");
		PegaWebElement element = findElement(SPACES);
		element.click();
		ScreenshotUtil.captureScreenshot(testEnv, "Spaces Page");
		return new Spaces(getActiveFrameId(true), testEnv);
	}

}
