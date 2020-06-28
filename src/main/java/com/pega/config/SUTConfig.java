

package com.pega.config;

import org.slf4j.*;

import java.util.*;

public class SUTConfig {
    private String url;
    private String user;
    private String pwd;
    private boolean isEnableFullScreenMode;
    private String testType;
    private boolean isSaveVideoAlways;
    private boolean isEnableVideoRecording;
    private boolean isPipelineRun;
    private Boolean isDebug;
    private String loggerLevel;
    private static final Logger LOGGER;

    static {
        LOGGER = LoggerFactory.getLogger(SUTConfig.class);
    }

    public SUTConfig(final Properties prop) {
        if (System.getenv("instance.url") != null) {
            this.url = System.getenv("instance.url").trim();
        } else if (System.getProperty("instance.url") != null) {
            this.url = System.getProperty("instance.url").trim();
        } else {
            this.url = prop.getProperty("instance.url").trim();
        }
        this.user = prop.getProperty("user", "").trim();
        this.pwd = prop.getProperty("pwd", "").trim();
        this.isEnableFullScreenMode = Boolean.parseBoolean(prop.getProperty("enable.fullscreen.mode", "true").trim());
        if (System.getenv("test.type") != null) {
            this.testType = System.getenv("test.type");
        } else if (System.getProperty("test.type") != null) {
            this.testType = System.getProperty("test.type");
        } else if (prop.getProperty("test.type") != null) {
            this.testType = prop.getProperty("test.type");
        }
        if (System.getenv("always.save.video") == null) {
            this.isSaveVideoAlways = Boolean.parseBoolean(prop.getProperty("always.save.video", "false").trim());
        } else {
            this.isSaveVideoAlways = Boolean.parseBoolean(System.getenv("always.save.video").trim());
        }
        if (System.getenv("is.pipeline.run") == null) {
            this.isPipelineRun = Boolean.parseBoolean(prop.getProperty("is.pipeline.run", "false").trim());
        } else {
            this.isPipelineRun = Boolean.parseBoolean(System.getenv("is.pipeline.run").trim());
        }
        SUTConfig.LOGGER.info("Setting is.pipeline.run property to " + this.isPipelineRun);
        if (System.getenv("enable.video.recording") == null) {
            if (System.getenv("JENKINS_URL") != null && !this.isPipelineRun) {
                this.isEnableVideoRecording = true;
            } else {
                this.isEnableVideoRecording = Boolean.parseBoolean(prop.getProperty("enable.video.recording", "false").trim());
            }
        } else {
            this.isEnableVideoRecording = Boolean.parseBoolean(System.getenv("enable.video.recording").trim());
        }
        if (System.getenv("debug.mode") != null) {
            this.isDebug = Boolean.parseBoolean(System.getenv("debug.mode").trim());
        } else if (System.getProperty("debug.mode") != null) {
            this.isDebug = Boolean.parseBoolean(System.getProperty("debug.mode", "").trim());
        } else {
            this.isDebug = Boolean.parseBoolean(prop.getProperty("debug.mode", "false").trim());
        }
        SUTConfig.LOGGER.info("Debug mode is: " + this.isDebug);
        if (System.getenv("logger.level") != null) {
            this.loggerLevel = System.getenv("logger.level").trim();
        } else if (System.getProperty("logger.level") != null) {
            this.loggerLevel = System.getProperty("logger.level", "").trim();
        } else {
            this.loggerLevel = prop.getProperty("logger.level", "INFO").trim();
        }
    }

    public String getURL() {
        return this.url;
    }

    public String getUser() {
        return this.user;
    }

    public String getPwd() {
        return this.pwd;
    }

    public Boolean isEnableFullScreenMode() {
        return this.isEnableFullScreenMode;
    }

    public boolean isCloud() {
        return System.getenv("isCloud") != null;
    }

    public boolean isSubUi() {
        return "subui".equalsIgnoreCase(this.testType);
    }

    public boolean isSaveVideoAlways() {
        return this.isSaveVideoAlways;
    }

    public boolean isEnableVideoRecording() {
        return this.isEnableVideoRecording;
    }

    public boolean isPipelineRun() {
        return this.isPipelineRun;
    }

    public void setPipelineRun(final boolean isPipelineRun) {
        this.isPipelineRun = isPipelineRun;
    }

    public Boolean isDebugMode() {
        SUTConfig.LOGGER.debug("Debug mode is: " + this.isDebug);
        return this.isDebug;
    }

    public String getLoggerLevel() {
        return this.loggerLevel;
    }
}
