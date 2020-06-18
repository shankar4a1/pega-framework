package com.pega.crm.portal;

import org.openqa.selenium.By;

import com.pega.TestEnvironment;
import com.pega.crm.workobjects.Forecast;
import com.pega.crm.workobjects.Spaces;
import com.pega.framework.PegaWebElement;
import com.pega.page.PortalImpl;

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
