

package com.pega.config;

import java.util.*;

public class MobileConfig {
    private String platform;
    private String deviceOSVersion;
    private String deviceName;
    private String appType;
    private String appPath;
    private String appPackage;
    private String appActivity;
    private boolean isMobExec;
    private Boolean isMobileDocStateAware;
    private String bundleID;
    private String iOSDeviceUDID;

    public MobileConfig(final Properties prop) {
        this.isMobExec = false;
        if (System.getenv("platform") != null) {
            this.platform = System.getenv("platform").trim();
        } else if (System.getProperty("platform") != null) {
            this.platform = System.getProperty("platform").trim();
        } else {
            this.platform = prop.getProperty("platform", "windows").trim();
        }
        if (System.getenv("deviceOSVersion") == null) {
            this.deviceOSVersion = prop.getProperty("deviceOSVersion", "").trim();
        } else {
            this.deviceOSVersion = System.getenv("deviceOSVersion").trim();
        }
        if (System.getenv("deviceName") == null) {
            this.deviceName = prop.getProperty("deviceName", "").trim();
        } else {
            this.deviceName = System.getenv("deviceName").trim();
        }
        if (System.getenv("appType") == null) {
            this.appType = prop.getProperty("appType", "").trim();
        } else {
            this.appType = System.getenv("appType").trim();
        }
        if (System.getenv("appPath") == null) {
            this.appPath = prop.getProperty("appPath", "").trim();
        } else {
            this.appPath = System.getenv("appPath").trim();
        }
        if (System.getenv("appPackage") == null) {
            this.appPackage = prop.getProperty("appPackage", "").trim();
        } else {
            this.appPackage = System.getenv("appPackage").trim();
        }
        if (System.getenv("appActivity") == null) {
            this.appActivity = prop.getProperty("appActivity", "").trim();
        } else {
            this.appActivity = System.getenv("appActivity").trim();
        }
        if (System.getenv("isMobileDocStateAware") == null) {
            this.isMobileDocStateAware = Boolean.parseBoolean(prop.getProperty("isMobileDocStateAware", "false").trim());
        } else {
            this.isMobileDocStateAware = Boolean.parseBoolean(System.getenv("isMobileDocStateAware").trim());
        }
        if (System.getenv("iOSDeviceUDID") == null) {
            this.iOSDeviceUDID = prop.getProperty("iOSDeviceUDID", "").trim();
        } else {
            this.iOSDeviceUDID = System.getenv("iOSDeviceUDID").trim();
        }
        if (System.getenv("bundleID") == null) {
            this.bundleID = prop.getProperty("bundleID", "").trim();
        } else {
            this.bundleID = System.getenv("bundleID").trim();
        }
    }

    public String getDeviceOSVersion() {
        return this.deviceOSVersion;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String getAppType() {
        return this.appType;
    }

    public String getAppPath() {
        return this.appPath;
    }

    public String getAppPackage() {
        return this.appPackage;
    }

    public String getAppActivity() {
        return this.appActivity;
    }

    public boolean isMobile() {
        return "iPhone".equalsIgnoreCase(this.platform) || "android".equalsIgnoreCase(this.platform);
    }

    public boolean isIos() {
        return System.getProperty("os.name").toLowerCase().contains("ios");
    }

    public boolean isMobileExecution() {
        return this.isMobExec;
    }

    public void setMobExec(final boolean isMobExec) {
        this.isMobExec = isMobExec;
    }

    public boolean isMobileDocStateAware() {
        return this.isMobileDocStateAware;
    }

    public String getIOSDeviceUDID() {
        return this.iOSDeviceUDID;
    }

    public String getbundleID() {
        return this.bundleID;
    }
}
