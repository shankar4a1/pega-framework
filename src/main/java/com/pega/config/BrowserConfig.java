

package com.pega.config;

import org.slf4j.*;

import java.util.*;

public class BrowserConfig {
    private String browserName;
    private String proxyHost;
    private String proxyPort;
    private Map<String, String> capabilities;
    private static final Logger LOGGER;

    static {
        LOGGER = LoggerFactory.getLogger(BrowserConfig.class);
    }

    public BrowserConfig(final Properties prop) {
        this.capabilities = new HashMap<String, String>();
        if (System.getenv("browser.name") != null) {
            this.browserName = System.getenv("browser.name").trim();
        } else if (System.getProperty("browser.name") != null) {
            this.browserName = System.getProperty("browser.name").trim();
        } else {
            this.browserName = prop.getProperty("browser.name", "chrome").trim();
        }
        this.proxyHost = prop.getProperty("proxyHost", "").trim();
        this.proxyPort = prop.getProperty("proxyPort", "").trim();
        String[] capabilitiesArray = null;
        if (System.getenv("capabilities") != null && !System.getenv("capabilities").trim().equals("")) {
            capabilitiesArray = prop.getProperty("capabilities").split(",");
        } else if (System.getProperty("capabilities") != null && !System.getProperty("capabilities").trim().equals("")) {
            capabilitiesArray = System.getProperty("capabilities").trim().split(",");
        } else if (prop.getProperty("capabilities") != null && !prop.getProperty("capabilities").trim().equals("")) {
            capabilitiesArray = prop.getProperty("capabilities").trim().split(",");
        }
        if (capabilitiesArray != null) {
            for (int i = 0; i < capabilitiesArray.length; ++i) {
                final String[] caps = capabilitiesArray[i].split(":");
                if (caps.length == 2) {
                    this.capabilities.put(caps[0].trim(), caps[1].trim());
                } else {
                    BrowserConfig.LOGGER.debug("Could not add capability: " + capabilitiesArray[i]);
                }
            }
        }
    }

    public void setBrowserName(final String browser) {
        this.browserName = browser;
    }

    public String getBrowserName() {
        return this.browserName;
    }

    public String getProxyHost() {
        return this.proxyHost;
    }

    public String getProxyPort() {
        return this.proxyPort;
    }

    public Map<String, String> getCapabilities() {
        return this.capabilities;
    }
}
