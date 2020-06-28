

package com.pega.drivers;

import com.pega.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.*;
import org.openqa.selenium.remote.*;
import org.slf4j.*;

import java.io.*;

public class PegaFirefoxProxyDriver extends AbstractDriver {
    private static final Logger LOGGER;

    static {
        LOGGER = LoggerFactory.getLogger(PegaFirefoxProxyDriver.class);
    }

    public PegaFirefoxProxyDriver(final TestEnvironment testEnv, final String browserName, final DesiredCapabilities caps, final String platformName) {
        super(testEnv, browserName, caps, platformName);
    }

    @Override
    public void initializeDriver() {
        final String PROXY = this.config.getBrowserConfig().getProxyHost() + ":" + this.config.getBrowserConfig().getProxyPort();
        PegaFirefoxProxyDriver.LOGGER.info("Proxy : " + PROXY);
        final Proxy proxy = new Proxy();
        proxy.setHttpProxy(PROXY).setFtpProxy(PROXY).setSslProxy(PROXY);
        final DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("proxy", proxy);
        if (this.config.getPlatformDetails().isWindows()) {
            System.setProperty("webdriver.gecko.driver", this.config.getDriversConfig().getFirefoxDriverPath());
        } else if (!this.config.getSUTConfig().isPipelineRun()) {
            PegaFirefoxProxyDriver.LOGGER.debug("Setting the linux chrome driver path to:" + this.config.getDriversConfig().getFirefoxDriverLinuxPath() + ". If you want in different location set it using chrome.driver.linux in Global Settings Properties file");
            final File f = new File(this.config.getDriversConfig().getFirefoxDriverLinuxPath());
            f.setExecutable(true);
            System.setProperty("webdriver.gecko.driver", this.config.getDriversConfig().getFirefoxDriverLinuxPath());
        }
        this.driver = new FirefoxDriver(cap);
        this.driver.manage().window().maximize();
    }

    @Override
    public boolean matches() {
        return "proxy".equalsIgnoreCase(this.browserName) && !"android".equalsIgnoreCase(this.platformName) && !"iphone".equalsIgnoreCase(this.platformName) && !"ios".equalsIgnoreCase(this.platformName);
    }
}
