

package com.pega.config.browser;

import com.pega.config.*;

import java.util.*;

public class RemoteDriverConfig extends BrowserConfig {
    private String browserVersion;
    private String hubUrl;
    private boolean browserStackCapblty;
    private String tunnelCommand;

    public RemoteDriverConfig(final Properties prop) {
        super(prop);
        if (System.getenv("browser.version") != null) {
            this.browserVersion = System.getenv("browser.version").trim();
        } else if (System.getProperty("browser.version") != null) {
            this.browserVersion = System.getProperty("browser.version").trim();
        } else {
            this.browserVersion = prop.getProperty("browser.version", "").trim();
        }
        if (System.getenv("hub.url") != null) {
            this.hubUrl = System.getenv("hub.url").trim();
        } else if (System.getProperty("hub.url") != null) {
            this.hubUrl = System.getProperty("hub.url", "").trim();
        } else {
            this.hubUrl = prop.getProperty("hub.url", "").trim();
        }
        if (System.getenv("browserstack.capability") != null) {
            this.setBrowserStackCapblty(Boolean.valueOf(System.getenv("browserstack.capability").trim()));
        } else if (System.getProperty("browserstack.capability") != null) {
            this.setBrowserStackCapblty(Boolean.valueOf(System.getProperty("browserstack.capability", "").trim()));
        } else {
            this.setBrowserStackCapblty(Boolean.valueOf(prop.getProperty("browserstack.capability", "").trim()));
        }
        if (System.getenv("tunnel.command") != null) {
            this.tunnelCommand = System.getenv("tunnel.command").trim();
        } else if (System.getProperty("tunnel.command") != null) {
            this.tunnelCommand = System.getProperty("tunnel.command", "").trim();
        } else {
            this.tunnelCommand = prop.getProperty("tunnel.command", "").trim();
        }
    }

    public String getBrowserVersion() {
        return this.browserVersion;
    }

    public String getHubUrl() {
        return this.hubUrl;
    }

    public boolean isBrowserStackCapblty() {
        return this.browserStackCapblty;
    }

    private void setBrowserStackCapblty(final boolean browserStackCapblty) {
        this.browserStackCapblty = browserStackCapblty;
    }

    public String getTunnelCommand() {
        return this.tunnelCommand;
    }
}
