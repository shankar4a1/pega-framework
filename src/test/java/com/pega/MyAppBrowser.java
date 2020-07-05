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

        ScreenshotUtil.captureScreenshot(myAppTestEnv, "LoginPage");
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


	/*@Given("^navigates to \"([^\"]*)\" List page$")
	public void navigates_to_page(String LeftNavItem) {
		sfaPortal = getPortal(SFAPortal.class);

		switch (LeftNavItem) {
			case "Organizations": {
				orgList = sfaPortal.getLeftNav().getOrganizationList();
				break;
			}
			case "Households": {
				hhList = sfaPortal.getLeftNav().getHouseholdList();
				break;
			}
			case "Accounts": {
				accList = sfaPortal.getLeftNav().getAccountList();
				break;
			}
			case "Contacts": {
				conList = sfaPortal.getLeftNav().getContactList();
				break;
			}
			case "Leads": {
				leadList = sfaPortal.getLeftNav().getLeadsList();
				break;
			}
			case "Opportunities": {

				String CAMPAIGN_LIST_XPATH = "//span[text()='Campaign']";

				if (pegaDriver.verifyElement(By.xpath(CAMPAIGN_LIST_XPATH)))
					campaignExists = true;
				oppList = sfaPortal.getLeftNav().getOpportunityList();
				break;
			}
			case "Territories": {
				terrList = sfaPortal.getLeftNav().getTerritoriesList();
				break;
			}
			case "Operators": {
				oprList = sfaPortal.getLeftNav().getOperatorsList();
				break;
			}
			case "Partners": {
				parList = sfaPortal.getLeftNav().getPartnersList();
				break;
			}
			case "Forecast": {
				forecast = sfaPortal.getLeftNav().getForecast();
				break;
			}
			case "Close plans": {
				closeplans = sfaPortal.getLeftNav().getClosePlans();
				break;
			}

		}
	}*/


}