

package com.pega.drivers;

import com.pega.*;
import com.pega.config.*;
import com.pega.config.browser.*;
import org.openqa.selenium.remote.*;
import org.slf4j.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class PegaRemoteWebDriver extends AbstractDriver {
    private static final Logger LOGGER;

    static {
        LOGGER = LoggerFactory.getLogger(PegaRemoteWebDriver.class);
    }

    public PegaRemoteWebDriver(final TestEnvironment testEnv, final String browserName, final DesiredCapabilities caps, final String platformName) {
        super(testEnv, browserName, caps, platformName);
    }

    @Override
    public void initializeDriver() {
        final RemoteDriverConfig remoteDriverConfig = (RemoteDriverConfig) this.config.getBrowserConfig();
        final DesiredCapabilities capabilities = this.caps;
        capabilities.setBrowserName(this.browserName);
        capabilities.setVersion(remoteDriverConfig.getBrowserVersion());
        final Map<String, String> hubCapabilities = remoteDriverConfig.getCapabilities();
        PegaRemoteWebDriver.LOGGER.debug("Capabilities Requested:" + hubCapabilities);
        for (final String key : hubCapabilities.keySet()) {
            capabilities.setCapability(key, hubCapabilities.get(key));
        }
        if (!remoteDriverConfig.getTunnelCommand().equals("")) {
            try {
                PegaRemoteWebDriver.LOGGER.debug("Tunnel Command is \n" + System.getProperty("user.dir") + File.separator + remoteDriverConfig.getTunnelCommand());
                Runtime.getRuntime().exec(System.getProperty("user.dir") + File.separator + remoteDriverConfig.getTunnelCommand());
                Thread.sleep(10000L);
                PegaRemoteWebDriver.LOGGER.debug("Tunnelling established");
            } catch (Exception e) {
                PegaRemoteWebDriver.LOGGER.error("Unable to tunnel with the tunneling command provided");
                e.printStackTrace();
            }
        }
        if (remoteDriverConfig.isBrowserStackCapblty()) {
            final String browserstackLocal = System.getenv("BROWSERSTACK_LOCAL");
            final String browserstackLocalIdentifier = System.getenv("BROWSERSTACK_LOCAL_IDENTIFIER");
            PegaRemoteWebDriver.LOGGER.info("BROWSERSTACK_LOCAL : " + browserstackLocal);
            PegaRemoteWebDriver.LOGGER.info("BROWSERSTACK_LOCAL_IDENTIFIER : " + browserstackLocalIdentifier);
            capabilities.setCapability("browserstack.local", browserstackLocal);
            capabilities.setCapability("browserstack.localIdentifier", browserstackLocalIdentifier);
        }
        try {
            this.driver = new RemoteWebDriver(new URL(remoteDriverConfig.getHubUrl()), capabilities);
            ((RemoteWebDriver) this.driver).setFileDetector(new LocalFileDetector());
            if (this.config.getPlatformDetails().isWindows() && !this.config.getSUTConfig().isEnableFullScreenMode()) {
                this.driver.manage().window().maximize();
            }
        } catch (MalformedURLException e2) {
            e2.printStackTrace();
        }
    }

    @Override
    public boolean matches() {
        final BrowserConfig browserConfig = this.config.getBrowserConfig();
        return browserConfig instanceof RemoteDriverConfig;
    }
}
