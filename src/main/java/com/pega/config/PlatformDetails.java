

package com.pega.config;

import java.util.*;

public class PlatformDetails {
    private String platform;

    public PlatformDetails(final Properties prop) {
        if (System.getenv("platform") != null) {
            this.platform = System.getenv("platform").trim();
        } else if (System.getProperty("platform") != null) {
            this.platform = System.getProperty("platform").trim();
        } else {
            this.platform = prop.getProperty("platform", "windows").trim();
        }
    }

    public String getPlatform() {
        return this.platform;
    }

    public boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    public boolean isMac() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }

    public boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    public void setPlatform(final String platform) {
        this.platform = platform;
    }
}
