package com.pega;

import com.google.inject.*;
import com.pega.page.*;
import com.pega.portal.*;
import com.pega.util.*;
import io.cucumber.guice.*;
import io.cucumber.java.en.*;
import org.openqa.selenium.*;


@ScenarioScoped
public class MyAppBrowser extends BrowserImpl {

	private Configuration configuration;
	private MyAppTestEnvironment myAppTestEnv;

	private static final By OPERATOR_MENU = By.xpath(
			"//img[@alt='Show User Profile']|//i[@data-test-id='px-opr-image-ctrl']|//i[contains(@class, 'icons avatar name')]");
	private static final By LOG_OFF_BUTTON = By.xpath("//*[text()='Log off']");

	@Inject
	public MyAppBrowser(MyAppTestEnvironment testEnv) {
		super(testEnv);
		this.myAppTestEnv = testEnv;
		configuration = testEnv.getConfiguration();
	}

	public <T extends Portal> T getPortal(Class<T> type) {
		T portal = null;
		String className = type.getName();
		if (className.contains("SalesAutomation")) {
			portal = type.cast(new SalesAutomationPortal(myAppTestEnv));
		}
		if (className.equals("SalesRep")) {
			portal = type.cast(new SalesRepPortal(myAppTestEnv));
		}
		return portal;
	}

	public void logout() {
		super.logout();

	}

	@Given("^A User logs in with \"(.*?)\" and \"(.*?)\"$")
	public void login(String username, String password) {
		open();
		super.login(username, password);
		ScreenshotUtil.captureScreenshot(myAppTestEnv);


	}

	@When("^User logs off from portal$")
	public void user_logs_off_from_portal() {
		super.logout();
	}

	@Given("^A User logs in with Administrator credentials$")
	public void a_User_logs_in_with_Administrator_credentials() {
		open();
		super.login(configuration.getCredential("ADMIN_USER_ID"), configuration.getCredential("ADMIN_PASSWORD"));
	}


}