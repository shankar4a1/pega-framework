

package com.pega;

import com.pega.config.*;
import com.pega.config.browser.*;
import com.pega.exceptions.*;
import com.pega.util.*;

import java.io.*;
import java.util.*;

public class ConfigurationImpl implements Configuration {
    private static final String VERSION = "$Id: ConfigurationImpl.java 208262 2016-08-30 13:11:32Z BalanaveenreddyKappeta $";
    private Boolean isEnableFullScreenMode;
    private Properties prop;
    private Properties usersProperties;
    private Credential credential;
    private File configFile;
    private Properties tagApplicationProperties;
    private boolean captureClientPerformanceMetrics;
    private boolean analyseDataTestId;
    private boolean autoSwitchToDefaultContent;
    private DriversConfig driverConfig;
    private SUTConfig sutConfig;
    private MobileConfig mobileConfig;
    private AppiumConfig appiumConfig;
    private BrowserConfig browserConfig;
    private L10NConfig l10nConfig;
    private DataBaseConfig dbConfig;
    private PlatformDetails platformDetails;

    public ConfigurationImpl(final File file) {
        this.autoSwitchToDefaultContent = true;
        this.configFile = file;
        this.prop = new Properties();
        try {
            this.prop.load(new FileInputStream(file));
        } catch (IOException e) {
            throw new PegaWebDriverException("Please check for Global Settings Properties file path", e);
        }
        if (System.getenv("analyseDataTestId") == null) {
            this.analyseDataTestId = Boolean.parseBoolean(this.prop.getProperty("analyseDataTestId", "").trim());
        } else {
            this.analyseDataTestId = Boolean.parseBoolean(System.getenv("analyseDataTestId").trim());
        }
        if (System.getenv("autoswitch.defaultcontent") == null) {
            this.autoSwitchToDefaultContent = Boolean.parseBoolean(this.prop.getProperty("autoswitch.defaultcontent", "true").trim());
        } else {
            this.autoSwitchToDefaultContent = Boolean.parseBoolean(System.getenv("autoswitch.defaultcontent").trim());
        }
        if (System.getenv("captureClientPerformanceMetrics") == null) {
            this.captureClientPerformanceMetrics = Boolean.parseBoolean(this.prop.getProperty("capture.clientPerformanceMetrics", "false"));
        } else {
            this.captureClientPerformanceMetrics = Boolean.parseBoolean(System.getenv("capture.clientPerformanceMetrics"));
        }
    }

    public Boolean isEnableFullScreenMode() {
        return this.isEnableFullScreenMode;
    }

    @Override
    public String getProperty(final String propName) {
        return this.prop.getProperty(propName, null);
    }

    @Override
    public String getCredential(final String propertyName) {
        if (this.usersProperties == null) {
            this.usersProperties = new Properties();
            try {
                File f = new File("users.properties");
                if (!f.exists()) {
                    f = new File(DataUtil.getDataFolder() + System.getProperty("file.separator") + "users.properties");
                }
                final String parent = this.configFile.getParent();
                if (parent != null && parent.contains("giza")) {
                    f = new File(parent + System.getProperty("file.separator") + "users.properties");
                }
                this.usersProperties.load(new FileInputStream(f));
            } catch (IOException e) {
                e.printStackTrace();
                throw new PegaWebDriverException(" Unable to read users.properties file");
            }
        }
        return this.usersProperties.getProperty(propertyName);
    }

    @Override
    public Credential getCredential() {
        if (this.usersProperties == null) {
            this.usersProperties = new Properties();
            try {
                File f = new File("users.properties");
                if (!f.exists()) {
                    f = new File(DataUtil.getDataFolder() + System.getProperty("file.separator") + "users.properties");
                }
                this.usersProperties.load(new FileInputStream(f));
            } catch (IOException e) {
                e.printStackTrace();
                throw new PegaWebDriverException(" Unable to read users.propertiess file");
            }
        }
        if (this.credential == null) {
            this.credential = new Credential(this.usersProperties);
        }
        return this.credential;
    }

    @Override
    public String getApplicationsForTag(final Collection<String> tags) {
        if (this.tagApplicationProperties == null) {
            this.tagApplicationProperties = new Properties();
            try {
                File f = new File("TagsAndApplications.properties");
                if (!f.exists()) {
                    f = new File(DataUtil.getDataFolder() + System.getProperty("file.separator") + "TagsAndApplications.properties");
                }
                this.tagApplicationProperties.load(new FileInputStream(f));
            } catch (IOException e) {
                e.printStackTrace();
                throw new PegaWebDriverException(" Unable to read users.propertiess file");
            }
        }
        String applications = "";
        for (final String tag : tags) {
            if (this.tagApplicationProperties.containsKey(tag)) {
                applications = applications + this.tagApplicationProperties.getProperty(tag) + ",";
            }
        }
        if (!"".equals(applications)) {
            applications = applications.substring(0, applications.lastIndexOf(","));
        }
        System.out.println("Applications for UIKit: " + applications);
        return applications;
    }

    @Override
    public boolean analyseDataTestId() {
        return this.analyseDataTestId;
    }

    @Override
    public boolean isAutoSwitchToDefaultContent() {
        return this.autoSwitchToDefaultContent;
    }

    @Override
    public boolean isCaptureClientPerformanceMetrics() {
        return this.captureClientPerformanceMetrics;
    }

    @Override
    public DriversConfig getDriversConfig() {
        if (this.driverConfig == null) {
            this.driverConfig = new DriversConfig(this.prop);
        }
        return this.driverConfig;
    }

    @Override
    public SUTConfig getSUTConfig() {
        if (this.sutConfig == null) {
            this.sutConfig = new SUTConfig(this.prop);
        }
        return this.sutConfig;
    }

    @Override
    public MobileConfig getMobileConfig() {
        if (this.mobileConfig == null) {
            this.mobileConfig = new MobileConfig(this.prop);
        }
        return this.mobileConfig;
    }

    private String getBrowserName() {
        String browserName = null;
        if (System.getenv("browser.name") != null) {
            browserName = System.getenv("browser.name").trim();
        } else if (System.getProperty("browser.name") != null) {
            browserName = System.getProperty("browser.name").trim();
        } else {
            browserName = this.prop.getProperty("browser.name", "chrome").trim();
        }
        return browserName;
    }

    @Override
    public AppiumConfig getAppiumConfig() {
        if (this.appiumConfig == null) {
            this.appiumConfig = new AppiumConfig(this.prop);
        }
        return this.appiumConfig;
    }

    @Override
    public BrowserConfig getBrowserConfig() {
        if (this.browserConfig == null) {
            if (!"".equals(this.getHubUrl())) {
                this.browserConfig = new RemoteDriverConfig(this.prop);
            } else if ("chrome".equalsIgnoreCase(this.getBrowserName())) {
                this.browserConfig = new ChromeConfig(this.prop);
            } else if ("firefox".equalsIgnoreCase(this.getBrowserName())) {
                this.browserConfig = new FirefoxConfig(this.prop);
            } else if ("htmlunit".equalsIgnoreCase(this.getBrowserName())) {
                this.browserConfig = new HtmlUnitConfig(this.prop);
            }
        }
        return this.browserConfig;
    }

    private String getHubUrl() {
        String hubUrl = "";
        if (System.getenv("hub.url") != null) {
            hubUrl = System.getenv("hub.url").trim();
        } else if (System.getProperty("hub.url") != null) {
            hubUrl = System.getProperty("hub.url", "").trim();
        } else {
            hubUrl = this.prop.getProperty("hub.url", "").trim();
        }
        return hubUrl;
    }

    @Override
    public L10NConfig getL10NConfig() {
        if (this.l10nConfig == null) {
            this.l10nConfig = new L10NConfig(this.prop);
        }
        return this.l10nConfig;
    }

    @Override
    public DataBaseConfig getDataBaseConfig() {
        if (this.dbConfig == null) {
            this.dbConfig = new DataBaseConfig(this.prop);
        }
        return this.dbConfig;
    }

    @Override
    public PlatformDetails getPlatformDetails() {
        if (this.platformDetails == null) {
            this.platformDetails = new PlatformDetails(this.prop);
        }
        return this.platformDetails;
    }
}
