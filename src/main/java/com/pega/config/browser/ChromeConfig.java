

package com.pega.config.browser;

import com.pega.config.*;

import java.util.*;

public class ChromeConfig extends BrowserConfig {
    private Boolean addAddOns;
    private String chromeExtensionsPath;
    private Boolean isChromeAutodownload;
    private String[] chromeOptionArguments;
    HashSet<String> chromeArgumentList;

    public ChromeConfig(final Properties prop) {
        super(prop);
        this.chromeArgumentList = new HashSet<String>();
        if (System.getenv("launch.broswer.with.addOns") == null) {
            this.addAddOns = Boolean.parseBoolean(prop.getProperty("launch.broswer.with.addOns", "false").trim());
        } else {
            this.addAddOns = Boolean.parseBoolean(System.getenv("launch.broswer.with.addOns").trim());
        }
        if (System.getenv("chrome.extension.paths") == null) {
            this.chromeExtensionsPath = prop.getProperty("chrome.extension.paths", "").trim();
        } else {
            this.chromeExtensionsPath = System.getenv("chrome.extension.paths").trim();
        }
        if (System.getenv("isChromeAutoDownload") == null) {
            this.isChromeAutodownload = Boolean.parseBoolean(prop.getProperty("isChromeAutoDownload", "true").trim());
        } else {
            this.isChromeAutodownload = Boolean.parseBoolean(System.getenv("isChromeAutoDownload").trim());
        }
        if (System.getenv("chrome.options.arguments") != null && !System.getenv("chrome.options.arguments").trim().equals("")) {
            this.chromeOptionArguments = prop.getProperty("chrome.options.arguments").split(";");
        } else if (System.getProperty("chrome.options.arguments") != null && !System.getProperty("chrome.options.arguments").trim().equals("")) {
            this.chromeOptionArguments = System.getProperty("chrome.options.arguments").trim().split(";");
        } else if (prop.getProperty("chrome.options.arguments") != null && !prop.getProperty("chrome.options.arguments").trim().equals("")) {
            this.chromeOptionArguments = prop.getProperty("chrome.options.arguments").trim().split(";");
        }
        if (this.chromeOptionArguments != null) {
            for (int i = 0; i < this.chromeOptionArguments.length; ++i) {
                this.chromeArgumentList.add(this.chromeOptionArguments[i]);
            }
        }
    }

    public Boolean launchBrowserWithAddOns() {
        return this.addAddOns;
    }

    public String getChromeExtensionPath() {
        return this.chromeExtensionsPath;
    }

    public boolean isChromeAutoDownload() {
        return this.isChromeAutodownload;
    }

    public HashSet<String> getChromeOptionsArguments() {
        return this.chromeArgumentList;
    }

    public void setChromeOptionsArguments(final HashSet<String> optionArg) {
        this.chromeArgumentList = optionArg;
    }
}
