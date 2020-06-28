

package com.pega.drivers;

import com.pega.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.remote.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;

public class PegaFirefoxDriver extends AbstractDriver {
    private static final Logger LOGGER;

    static {
        LOGGER = LoggerFactory.getLogger(PegaFirefoxDriver.class);
    }

    public PegaFirefoxDriver(final TestEnvironment testEnv, final String browserName, final DesiredCapabilities caps, final String platformName) {
        super(testEnv, browserName, caps, platformName);
    }

    @Override
    public void initializeDriver() {
        FirefoxOptions opt = null;
        if (this.config.getPlatformDetails().isLinux()) {
            PegaFirefoxDriver.LOGGER.debug("Setting DISPLAY parameter...");
            System.setProperty("DISPLAY", ":3");
        }
        if (!this.config.getBrowserConfig().getProxyHost().equals("") || !this.config.getBrowserConfig().getProxyPort().equals("")) {
            final String PROXY = this.config.getBrowserConfig().getProxyHost() + ":" + this.config.getBrowserConfig().getProxyPort();
            PegaFirefoxDriver.LOGGER.info("Setting Firefox Proxy : " + PROXY);
            final Proxy proxy = new Proxy();
            proxy.setHttpProxy(PROXY).setFtpProxy(PROXY).setSslProxy(PROXY);
            opt = new FirefoxOptions();
            opt.setCapability("proxy", proxy);
        }
        if (this.config.getPlatformDetails().isWindows()) {
            System.setProperty("webdriver.gecko.driver", this.config.getDriversConfig().getFirefoxDriverPath());
        } else if (!this.config.getSUTConfig().isPipelineRun()) {
            PegaFirefoxDriver.LOGGER.debug("Setting the linux chrome driver path to:" + this.config.getDriversConfig().getFirefoxDriverLinuxPath() + ". If you want in different location set it using chrome.driver.linux in Global Settings Properties file");
            final File f = new File(this.config.getDriversConfig().getFirefoxDriverLinuxPath());
            f.setExecutable(true);
            System.setProperty("webdriver.gecko.driver", this.config.getDriversConfig().getFirefoxDriverLinuxPath());
        }
        if (this.config.getBrowserConfig().getCapabilities() != null) {
            PegaFirefoxDriver.LOGGER.debug("Firefox Options Requested:" + this.config.getBrowserConfig().getCapabilities());
            final Map<String, String> capbs = this.config.getBrowserConfig().getCapabilities();
            for (final String key : capbs.keySet()) {
                opt.setCapability(key, capbs.get(key));
            }
        }
        if (opt != null) {
            this.driver = new FirefoxDriver(opt);
        } else {
            this.driver = new FirefoxDriver();
        }
        this.driver.manage().window().maximize();
    }

    @Override
    public boolean matches() {
        return "firefox".equalsIgnoreCase(this.browserName) && !"android".equalsIgnoreCase(this.platformName) && !"iphone".equalsIgnoreCase(this.platformName) && !"ios".equalsIgnoreCase(this.platformName);
    }
}
