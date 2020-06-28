

package com.pega.config;

import java.io.*;
import java.util.*;

public class DriversConfig {
    private String chromeDriverPath;
    private String chromeDriverLinuxPath;
    private String firefoxDriverPath;
    private String firefoxDriverLinuxPath;
    private String ieDriverPath;
    private String edgeDriverPath;
    private String phantomJSDriverPath;

    public DriversConfig(final Properties prop) {
        this.chromeDriverPath = prop.getProperty("chrome.driver", "binaries" + System.getProperty("file.separator") + "chromedriver.exe").trim();
        this.chromeDriverLinuxPath = prop.getProperty("chrome.driver.linux", "binaries" + File.separator + "chromedriver");
        this.firefoxDriverPath = prop.getProperty("gecko.driver", "binaries" + File.separator + "geckodriver.exe");
        this.firefoxDriverLinuxPath = prop.getProperty("gecko.driver.linux", "binaries" + File.separator + "geckodriver");
        this.ieDriverPath = prop.getProperty("ie.driver", "binaries" + System.getProperty("file.separator") + "IEDriverServer.exe").trim();
        this.edgeDriverPath = prop.getProperty("edge.driver", "binaries" + System.getProperty("file.separator") + "MicrosoftWebDriver.exe").trim();
        this.phantomJSDriverPath = prop.getProperty("phontomjs.driver", "binaries" + System.getProperty("file.separator") + "phantomjs.exe").trim();
    }

    public String getChromeDriverPath() {
        return this.chromeDriverPath;
    }

    public String getFirefoxDriverPath() {
        return this.firefoxDriverPath;
    }

    public String getIEDriverPath() {
        return this.ieDriverPath;
    }

    public String getEdgeDriverPath() {
        return this.edgeDriverPath;
    }

    public String getPhantomJSDriverPath() {
        return this.phantomJSDriverPath;
    }

    public String getFirefoxDriverLinuxPath() {
        return this.firefoxDriverLinuxPath;
    }

    public String getChromeDriverLinuxPath() {
        return this.chromeDriverLinuxPath;
    }
}
