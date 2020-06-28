

package com.pega.drivers;

import com.pega.*;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;

public abstract class AbstractDriver {
    protected TestEnvironment testEnv;
    protected Configuration config;
    protected String browserName;
    protected DesiredCapabilities caps;
    protected String platformName;
    protected WebDriver driver;

    public AbstractDriver(final TestEnvironment testEnv, final String browserName, final DesiredCapabilities caps, final String platformName) {
        this.testEnv = testEnv;
        this.browserName = browserName;
        this.caps = caps;
        this.platformName = platformName;
        this.config = testEnv.getConfiguration();
    }

    public abstract void initializeDriver();

    public abstract boolean matches();

    public WebDriver getDriver() {
        return this.driver;
    }
}
