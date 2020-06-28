

package com.pega.drivers;

import com.pega.*;
import com.pega.config.*;
import com.pega.util.*;
import io.appium.java_client.android.*;
import org.openqa.selenium.remote.*;
import org.slf4j.*;

import java.net.*;

public class PegaAndriodDriver extends AbstractDriver {
    private static final Logger LOGGER;

    static {
        LOGGER = LoggerFactory.getLogger(PegaAndriodDriver.class);
    }

    public PegaAndriodDriver(final TestEnvironment testEnv, final String browserName, final DesiredCapabilities caps, final String platformName) {
        super(testEnv, browserName, caps, platformName);
    }

    @Override
    public void initializeDriver() {
        final DesiredCapabilities capabilities = this.caps;
        this.config.getMobileConfig().setMobExec(true);
        final AppiumHandler appiumHandler = new AppiumHandler(this.testEnv);
        final AppiumConfig appiumConfig = this.config.getAppiumConfig();
        try {
            appiumHandler.stopAppium();
            PegaAndriodDriver.LOGGER.debug("=======================" + appiumConfig.getAppiumServerURL().substring(7, appiumConfig.getAppiumServerURL().lastIndexOf(58)) + "\n" + appiumConfig.getAppiumNodePath() + "\n" + appiumConfig.getAppiumJsPath() + "\n" + appiumConfig.getAppiumLogsPath());
            appiumHandler.startAppium(appiumConfig.getAppiumNodePath(), appiumConfig.getAppiumJsPath(), appiumConfig.getAppiumLogsPath(), appiumConfig.getAppiumServerURL().substring(7, appiumConfig.getAppiumServerURL().lastIndexOf(58)), appiumConfig.getAppiumServerURL().substring(appiumConfig.getAppiumServerURL().lastIndexOf(58), appiumConfig.getAppiumServerURL().lastIndexOf(58) + 4));
        } catch (Exception e) {
            PegaAndriodDriver.LOGGER.error("Appium start failed with " + e.getMessage());
        }
        if ("WebApp".equalsIgnoreCase(this.config.getMobileConfig().getAppType())) {
            PegaAndriodDriver.LOGGER.debug("***************This is mobile device ********************");
            capabilities.setCapability("browserName", this.browserName);
        } else {
            PegaAndriodDriver.LOGGER.debug("*************** NativeAPP ********************");
            PegaAndriodDriver.LOGGER.debug(this.config.getMobileConfig().getAppPackage() + ":" + this.config.getMobileConfig().getAppActivity());
            capabilities.setCapability("appPackage", this.config.getMobileConfig().getAppPackage());
            capabilities.setCapability("appActivity", this.config.getMobileConfig().getAppActivity());
        }
        capabilities.setCapability("deviceName", this.config.getMobileConfig().getDeviceName());
        capabilities.setCapability("platformName", this.config.getPlatformDetails().getPlatform());
        capabilities.setCapability("newCommandTimeout", 360);
        capabilities.setCapability("platformVersion", this.config.getMobileConfig().getDeviceOSVersion());
        final String appiumServerURL = appiumConfig.getAppiumServerURL();
        try {
            this.driver = new AndroidDriver(new URL(appiumServerURL), capabilities);
            PegaAndriodDriver.LOGGER.info("Android Driver launched successfully");
        } catch (MalformedURLException e2) {
            e2.printStackTrace();
            PegaAndriodDriver.LOGGER.error("Android Driver launch failed with " + e2.getMessage());
        }
    }

    @Override
    public boolean matches() {
        return this.config.getPlatformDetails().isWindows() && this.config.getPlatformDetails().getPlatform().equalsIgnoreCase("android");
    }
}
