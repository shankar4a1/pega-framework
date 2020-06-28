

package com.pega.config.browser;

import com.pega.config.*;

import java.util.*;

public class HtmlUnitConfig extends BrowserConfig {
    private String htmlunitBrowserVersion;

    public HtmlUnitConfig(final Properties prop) {
        super(prop);
        if (System.getenv("htmlunit.browserversion") == null) {
            this.htmlunitBrowserVersion = prop.getProperty("htmlunit.browserversion", "chrome").trim();
        } else {
            this.htmlunitBrowserVersion = System.getenv("htmlunit.browserversion").trim();
        }
    }

    public String getHtmlUnitBrowserType() {
        return this.htmlunitBrowserVersion;
    }
}
