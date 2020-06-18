package com.pega.crm.portal;

import org.openqa.selenium.By;

import com.pega.TestEnvironment;
import com.pega.page.PortalImpl;

public class SalesRepPortal extends PortalImpl {

	public static final By FORECAST = By.xpath("//span[text()='Forecast']");
	public static final By SPACES = By.xpath("//span[text()='Spaces']");

	public SalesRepPortal(TestEnvironment testEnv) {
		super(testEnv);
	}
}
