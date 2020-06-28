package com.pega.portal;

import com.pega.*;
import com.pega.page.*;
import org.openqa.selenium.*;

public class SalesRepPortal extends PortalImpl {

	public static final By FORECAST = By.xpath("//span[text()='Forecast']");
	public static final By SPACES = By.xpath("//span[text()='Spaces']");

	public SalesRepPortal(TestEnvironment testEnv) {
		super(testEnv);
	}
}
