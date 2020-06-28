

package com.pega.drivers;

import com.pega.*;
import org.openqa.selenium.edge.*;
import org.openqa.selenium.remote.*;
import org.slf4j.*;

import java.awt.*;

public class PegaEdgeDriver extends AbstractDriver {
    private static final Logger LOGGER;

    static {
        LOGGER = LoggerFactory.getLogger(PegaEdgeDriver.class);
    }

    public PegaEdgeDriver(final TestEnvironment testEnv, final String browserName, final DesiredCapabilities caps, final String platformName) {
        super(testEnv, browserName, caps, platformName);
    }

    @Override
    public void initializeDriver() {
        System.setProperty("webdriver.edge.driver", this.config.getDriversConfig().getEdgeDriverPath());
        final EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.setPageLoadStrategy("eager");
        this.driver = new EdgeDriver(edgeOptions);
        this.driver.manage().window().maximize();
        if (this.config.getSUTConfig().isEnableFullScreenMode()) {
            Robot r = null;
            try {
                r = new Robot();
            } catch (AWTException e) {
                e.printStackTrace();
            }
            r.keyPress(524);
            r.keyPress(16);
            r.keyPress(10);
            r.keyRelease(10);
            r.keyRelease(16);
            r.keyRelease(524);
        }
        this.driver.manage().deleteAllCookies();
        PegaEdgeDriver.LOGGER.info("Edge Driver launched successfully");
    }

    @Override
    public boolean matches() {
        return "edge".equalsIgnoreCase(this.browserName) && !"android".equalsIgnoreCase(this.platformName) && !"iphone".equalsIgnoreCase(this.platformName) && !"ios".equalsIgnoreCase(this.platformName);
    }
}
