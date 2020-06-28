

package com.pega.drivers;

import com.gargoylesoftware.htmlunit.*;
import com.pega.*;
import com.pega.config.*;
import com.pega.config.browser.*;
import org.openqa.selenium.htmlunit.*;
import org.openqa.selenium.remote.*;
import org.slf4j.*;

import java.util.*;

public class PegaHtmlUnitDriver extends AbstractDriver {
    private static final Logger LOGGER;

    static {
        LOGGER = LoggerFactory.getLogger(PegaHtmlUnitDriver.class);
    }

    public PegaHtmlUnitDriver(final TestEnvironment testEnv, final String browserName, final DesiredCapabilities caps, final String platformName) {
        super(testEnv, browserName, caps, platformName);
    }

    @Override
    public void initializeDriver() {
        final BrowserConfig browserConfig = this.config.getBrowserConfig();
        DesiredCapabilities capabilities = this.caps;
        if (browserConfig.getCapabilities() != null || !browserConfig.getCapabilities().equals("")) {
            final Map<String, String> capbs = browserConfig.getCapabilities();
            capabilities = DesiredCapabilities.htmlUnit();
            capabilities.setAcceptInsecureCerts(true);
            for (final String key : capbs.keySet()) {
                capabilities.setCapability(key, capbs.get(key));
            }
            this.driver = new HtmlUnitDriver(capabilities);
        } else {
            BrowserVersion version = BrowserVersion.CHROME;
            final HtmlUnitConfig htmlUnitConfig = (HtmlUnitConfig) this.config.getBrowserConfig();
            if (htmlUnitConfig.getHtmlUnitBrowserType().equalsIgnoreCase("chrome")) {
                version = BrowserVersion.CHROME;
            } else if (htmlUnitConfig.getHtmlUnitBrowserType().equalsIgnoreCase("ie") || htmlUnitConfig.getHtmlUnitBrowserType().equalsIgnoreCase("iexplore") || htmlUnitConfig.getHtmlUnitBrowserType().equalsIgnoreCase("internetexplorer")) {
                version = BrowserVersion.INTERNET_EXPLORER;
            } else if (htmlUnitConfig.getHtmlUnitBrowserType().equalsIgnoreCase("firefox")) {
                version = BrowserVersion.FIREFOX_60;
            }
            this.driver = new HtmlUnitDriver(version, true);
        }
        this.driver.manage().window().maximize();
        PegaHtmlUnitDriver.LOGGER.info("HtmlUnit Driver launched successfully");
    }

    @Override
    public boolean matches() {
        return "htmlunit".equalsIgnoreCase(this.browserName) && !"android".equalsIgnoreCase(this.platformName) && !"iphone".equalsIgnoreCase(this.platformName) && !"ios".equalsIgnoreCase(this.platformName);
    }
}
