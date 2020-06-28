

package com.pega.drivers;

import com.pega.*;
import com.pega.config.*;
import com.pega.util.*;
import io.appium.java_client.ios.*;
import org.openqa.selenium.remote.*;
import org.slf4j.*;

import java.net.*;

public class PegaIOSDriver extends AbstractDriver {
    private static final Logger LOGGER;

    static {
        LOGGER = LoggerFactory.getLogger(PegaIOSDriver.class);
    }

    public PegaIOSDriver(final TestEnvironment testEnv, final String browserName, final DesiredCapabilities caps, final String platformName) {
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
            PegaIOSDriver.LOGGER.debug("=======================" + appiumConfig.getAppiumServerURL().substring(7, appiumConfig.getAppiumServerURL().lastIndexOf(58)) + "\n" + appiumConfig.getAppiumNodePath() + "\n" + appiumConfig.getAppiumJsPath() + "\n" + appiumConfig.getAppiumLogsPath() + "\n" + appiumConfig.getAppiumServerURL().substring(7, appiumConfig.getAppiumServerURL().lastIndexOf(58)) + "\n" + appiumConfig.getAppiumServerURL().substring(appiumConfig.getAppiumServerURL().lastIndexOf(58), appiumConfig.getAppiumServerURL().lastIndexOf(58) + 4));
            appiumHandler.startAppium(appiumConfig.getAppiumNodePath(), appiumConfig.getAppiumJsPath(), appiumConfig.getAppiumLogsPath(), appiumConfig.getAppiumServerURL().substring(7, appiumConfig.getAppiumServerURL().lastIndexOf(58)), appiumConfig.getAppiumServerURL().substring(appiumConfig.getAppiumServerURL().lastIndexOf(58) + 1, appiumConfig.getAppiumServerURL().lastIndexOf(58) + 5));
            appiumHandler.startProxy(this.config.getMobileConfig().getIOSDeviceUDID());
        } catch (Exception e) {
            PegaIOSDriver.LOGGER.error("Appium start failed with " + e.getMessage());
        }
        if ("WebApp".equalsIgnoreCase(this.config.getMobileConfig().getAppType())) {
            PegaIOSDriver.LOGGER.debug("***************This is mobile device ********************");
            capabilities.setCapability("browserName", this.browserName);
        } else {
            PegaIOSDriver.LOGGER.debug("*************** NativeAPP ********************");
            PegaIOSDriver.LOGGER.debug(this.config.getMobileConfig().getAppPackage() + ":" + this.config.getMobileConfig().getAppActivity());
            capabilities.setCapability("appPackage", this.config.getMobileConfig().getAppPackage());
            capabilities.setCapability("appActivity", this.config.getMobileConfig().getAppActivity());
        }
        capabilities.setCapability("deviceName", this.config.getMobileConfig().getDeviceName());
        capabilities.setCapability("platformName", this.config.getPlatformDetails().getPlatform());
        capabilities.setCapability("newCommandTimeout", 360);
        capabilities.setCapability("platformVersion", this.config.getMobileConfig().getDeviceOSVersion());
        final String appiumServerURL = appiumConfig.getAppiumServerURL();
        capabilities.setCapability("udid", this.config.getMobileConfig().getIOSDeviceUDID());
        capabilities.setCapability("bundleId", this.config.getMobileConfig().getbundleID());
        try {
            this.driver = new IOSDriver(new URL(appiumServerURL), capabilities);
            PegaIOSDriver.LOGGER.info("IOS Driver launched successfully");
        } catch (MalformedURLException e2) {
            e2.printStackTrace();
            PegaIOSDriver.LOGGER.error("IOS Driver launch failed with " + e2.getMessage());
        }
    }

    @Override
    public boolean matches() {
        return this.config.getPlatformDetails().isMac() && "ios".equalsIgnoreCase(this.config.getPlatformDetails().getPlatform());
    }
}
