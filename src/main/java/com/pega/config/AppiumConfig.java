

package com.pega.config;

import java.util.*;

public class AppiumConfig {
    private String appiumServerURL;
    private String appiumNodePath;
    private String appiumJsPath;
    private String appiumLogsPath;

    public AppiumConfig(final Properties prop) {
        if (System.getenv("appiumServerURL") == null) {
            this.appiumServerURL = prop.getProperty("appiumServerURL", "http://127.0.0.1:4723/wd/hub").trim();
        } else {
            this.appiumServerURL = System.getenv("appiumServerURL").trim();
        }
        if (System.getenv("appiumNodePath") == null) {
            this.appiumNodePath = prop.getProperty("appiumNodePath", "").trim();
        } else {
            this.appiumNodePath = System.getenv("appiumNodePath").trim();
        }
        if (System.getenv("appiumJsPath") == null) {
            this.appiumJsPath = prop.getProperty("appiumJsPath", "").trim();
        } else {
            this.appiumJsPath = System.getenv("appiumJsPath").trim();
        }
        if (System.getenv("appiumLogsPath") == null) {
            this.appiumLogsPath = prop.getProperty("appiumLogsPath", "").trim();
        } else {
            this.appiumLogsPath = System.getenv("appiumLogsPath").trim();
        }
    }

    public String getAppiumServerURL() {
        return this.appiumServerURL;
    }

    public String getAppiumNodePath() {
        return this.appiumNodePath;
    }

    public String getAppiumJsPath() {
        return this.appiumJsPath;
    }

    public String getAppiumLogsPath() {
        return this.appiumLogsPath;
    }
}
