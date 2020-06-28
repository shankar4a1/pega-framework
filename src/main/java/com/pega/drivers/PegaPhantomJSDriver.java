

package com.pega.drivers;

import com.pega.*;
import com.pega.config.*;
import org.openqa.selenium.phantomjs.*;
import org.openqa.selenium.remote.*;
import org.slf4j.*;

import java.util.*;

public class PegaPhantomJSDriver extends AbstractDriver {
    private static final Logger LOGGER;

    static {
        LOGGER = LoggerFactory.getLogger(PegaPhantomJSDriver.class);
    }

    public PegaPhantomJSDriver(final TestEnvironment testEnv, final String browserName, final DesiredCapabilities caps, final String platformName) {
        super(testEnv, browserName, caps, platformName);
    }

    @Override
    public void initializeDriver() {
        final BrowserConfig browserConfig = this.config.getBrowserConfig();
        final DesiredCapabilities capabilities = this.caps;
        System.setProperty("phantomjs.binary.path", this.config.getDriversConfig().getPhantomJSDriverPath());
        if (browserConfig.getCapabilities() != null || !"".equals(browserConfig.getCapabilities())) {
            final Map<String, String> capbs = browserConfig.getCapabilities();
            capabilities.setAcceptInsecureCerts(true);
            for (final String key : capbs.keySet()) {
                capabilities.setCapability(key, capbs.get(key));
            }
            this.driver = new PhantomJSDriver(capabilities);
        } else {
            this.driver = new PhantomJSDriver();
        }
        PegaPhantomJSDriver.LOGGER.info("PhantomJS Driver launched successfully");
    }

    @Override
    public boolean matches() {
        return "phantomjs".equalsIgnoreCase(this.browserName) && !"android".equalsIgnoreCase(this.platformName) && !"iphone".equalsIgnoreCase(this.platformName) && !"ios".equalsIgnoreCase(this.platformName);
    }
}
