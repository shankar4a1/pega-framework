

package com.pega.util;

import org.openqa.selenium.*;

import java.util.*;

public class GlobalConstants {
    String COPYRIGHT;
    private static final String VERSION = "$Id: GlobalConstants.java 200183 2016-06-24 05:43:37Z SachinVellanki $";
    private static int GLOBAL_TIMEOUT;
    public static final int THROBBER_INITIAL_WAIT_TIMEOUT = 3;
    private static String ACTIVE_FRAME_ID;
    private static By CURRENT_FRAME_BY;
    public static final String DEV_STUDIO_FRMAE_ID = "Developer";
    public static final int SHORT_TIMEOUT_IM_MSECS = 500;
    public static final int MEDIUM_TIMEOUT = 10;
    public static final int LONG_TIMEOUT = 30;
    public static final int VERY_LONG_TIMEOUT = 300;
    public static final String DEFAULT_DATA_FILE_FORMAT = ".csv";
    public static final String DEFAULT_REPORTS_FOLDER = "test-output";
    public static final String SCREENSHOTS_FOLDER_NAME = "Screenshots";
    public static final String IMAGE_FILE_FORMAT = ".jpg";
    public static final String VIDEOS_FOLDER_NAME = "Videos";
    public static final String VIDEO_FILE_FORMAT = ".avi";
    public static final String CONFIG_PROPERTIES_FILE_NAME = "GlobalProperties.properties";
    public static final String GLOBAL_SETTINGS_PROPERTIES_FILE_NAME = "global-settings.properties";
    public static final String LATEST_RELEASE_VERSION = "Universal SMA v7.4";
    public static final String PRPC_ML_VERSION = "7.3";
    public static final String LATEST_REPORTS_FOLDER_NAME = "LatestReports";
    public static final String ARCHIVED_REPORTS_FOLDER_NAME = "ArchivedReports";
    public static final String AGGREGATED_REPORTS_FOLDER_NAME = "AggregatedReports";
    public static final String SUCCESS_RESPONSE_CODE = "200";
    public static final String DATA_FOLDER_NAME = "Data";
    public static final String ALTERNATE_DATA_FOLDER_NAME = "data";
    public static final String CONSOLIDATED_REPORT_NAME;
    public static final String EMAIL_PASSWORD = "mission123$";
    public static final String ATTACHMENTS_DIR_PATH = "Attachments";
    public static final int CURRENT_DAY_OF_MONTH;
    public static final String BOX_USER_NAME = "prashant.naidu.s@gmail.com";
    public static final String BOX_PASSWORD = "Twister16$";
    public static final String PREDICTION_STUDIO_FRAME_ID = "pxPredictionStudio";

    static {
        GlobalConstants.GLOBAL_TIMEOUT = 300;
        GlobalConstants.ACTIVE_FRAME_ID = "PegaGadget0Ifr";
        GlobalConstants.CURRENT_FRAME_BY = null;
        CONSOLIDATED_REPORT_NAME = System.getenv("BUILD_NUMBER") + ".json";
        CURRENT_DAY_OF_MONTH = Calendar.getInstance().get(5);
    }

    public GlobalConstants() {
        this.COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    }

    public static int getGLOBAL_TIMEOUT() {
        return GlobalConstants.GLOBAL_TIMEOUT;
    }

    public static void setGLOBAL_TIMEOUT(final int gLOBAL_TIMEOUT) {
        GlobalConstants.GLOBAL_TIMEOUT = gLOBAL_TIMEOUT;
    }

    public static String getACTIVE_FRAME_ID() {
        return GlobalConstants.ACTIVE_FRAME_ID;
    }

    public static void setACTIVE_FRAME_ID(final String aCTIVE_FRAME_ID) {
        GlobalConstants.ACTIVE_FRAME_ID = aCTIVE_FRAME_ID;
    }

    public static By getCURRENT_FRAME_BY() {
        return GlobalConstants.CURRENT_FRAME_BY;
    }

    public static void setCURRENT_FRAME_BY(final By cURRENT_FRAME_BY) {
        GlobalConstants.CURRENT_FRAME_BY = cURRENT_FRAME_BY;
    }

    public enum RecordType {
        CORRESPONDENCE("CORRESPONDENCE", 0, "Correspondence"),
        FEED_SOURCE("FEED_SOURCE", 1, "Feed source"),
        FLOW("FLOW", 2, "Flow"),
        FLOW_ACTION("FLOW_ACTION", 3, "Flow Action"),
        HARNESS("HARNESS", 4, " Harness (Page)"),
        NOTIFICATION("NOTIFICATION", 5, "Notification"),
        PARAGRAPH("PARAGRAPH", 6, "Paragraph"),
        PROPERTY("PROPERTY", 7, "Property"),
        SECTION("SECTION", 8, "Section"),
        SERVICE_LEVEL_AGREEMENT("SERVICE_LEVEL_AGREEMENT", 9, "Service Level Agreement"),
        STRATEGY("STRATEGY", 10, "Strategy"),
        STRATEGY_RESULT("STRATEGY_RESULT", 11, "StrategyResult"),
        WHEN("WHEN", 12, "When Conditions");

        private String type;

        RecordType(final String name, final int ordinal, final String type) {
            this.type = type;
        }

        public String getType() {
            return this.type;
        }
    }
}
