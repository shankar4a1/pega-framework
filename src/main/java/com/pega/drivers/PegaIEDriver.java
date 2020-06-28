

package com.pega.drivers;

import com.pega.*;
import com.pega.config.*;
import org.openqa.selenium.*;
import org.openqa.selenium.ie.*;
import org.openqa.selenium.remote.*;
import org.slf4j.*;

import java.util.*;

public class PegaIEDriver extends AbstractDriver {
    private static final Logger LOGGER;

    static {
        LOGGER = LoggerFactory.getLogger(PegaIEDriver.class);
    }

    public PegaIEDriver(final TestEnvironment testEnv, final String browserName, final DesiredCapabilities caps, final String platformName) {
        super(testEnv, browserName, caps, platformName);
    }

    @Override
    public void initializeDriver() {
        final BrowserConfig browserConfig = this.config.getBrowserConfig();
        final InternetExplorerOptions ieOptions = new InternetExplorerOptions();
        ieOptions.setCapability("ignoreProtectedModeSettings", false);
        ieOptions.setCapability("requireWindowFocus", true);
        ieOptions.setCapability("enablePersistentHover", false);
        ieOptions.setCapability("nativeEvents", true);
        ieOptions.setCapability("ignoreZoomSetting", true);
        ieOptions.setCapability("unexpectedAlertBehaviour", UnexpectedAlertBehaviour.IGNORE);
        ieOptions.setCapability("ie.ensureCleanSession", true);
        System.setProperty("webdriver.ie.driver", this.config.getDriversConfig().getIEDriverPath());
        if (browserConfig.getCapabilities() != null) {
            PegaIEDriver.LOGGER.debug("IE Options Requested:" + this.config.getBrowserConfig().getCapabilities());
            final Map<String, String> capbs = browserConfig.getCapabilities();
            for (final String key : capbs.keySet()) {
                ieOptions.setCapability(key, capbs.get(key));
            }
        }
        this.driver = new InternetExplorerDriver(ieOptions);
        this.driver.manage().window().maximize();
        PegaIEDriver.LOGGER.info("IE Driver launched successfully");
    }

    @Override
    public boolean matches() {
        return ("ie".equals(this.browserName) || "iexplore".equals(this.browserName) || "Internet Explorer".equalsIgnoreCase(this.browserName)) && !"android".equalsIgnoreCase(this.platformName) && !"iphone".equalsIgnoreCase(this.platformName) && !"ios".equalsIgnoreCase(this.platformName);
    }
}
