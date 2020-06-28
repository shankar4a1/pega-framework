package com.pega.portal;

import com.pega.*;
import com.pega.framework.*;
import com.pega.page.*;
import com.pega.workobjects.*;
import org.openqa.selenium.*;

public class SalesAutomationPortal extends PortalImpl {

	public static final By FORECAST = By.xpath("//span[text()='Forecast']");
	public static final By SPACES = By.xpath("//span[text()='Spaces']");

	public SalesAutomationPortal(TestEnvironment testEnv) {
		super(testEnv);
	}

	public Forecast openForecast() {
		PegaWebElement element = findElement(FORECAST);
		element.click();
		return new Forecast(getActiveFrameId(true), testEnv);
	}

	public Spaces openSpaces() {
		PegaWebElement element = findElement(SPACES);
		element.click();
		return new Spaces(getActiveFrameId(true), testEnv);
	}

}
