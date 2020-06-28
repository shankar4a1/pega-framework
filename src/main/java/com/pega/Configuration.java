

package com.pega;

import com.pega.config.*;

import java.util.*;

public interface Configuration {
    String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    String VERSION = "$Id: Configuration.java 195388 2016-06-01 06:55:22Z PavanBeri $";

    DriversConfig getDriversConfig();

    Credential getCredential();

    SUTConfig getSUTConfig();

    MobileConfig getMobileConfig();

    AppiumConfig getAppiumConfig();

    BrowserConfig getBrowserConfig();

    L10NConfig getL10NConfig();

    DataBaseConfig getDataBaseConfig();

    PlatformDetails getPlatformDetails();

    String getProperty(final String p0);

    String getCredential(final String p0);

    String getApplicationsForTag(final Collection<String> p0);

    boolean analyseDataTestId();

    boolean isAutoSwitchToDefaultContent();

    boolean isCaptureClientPerformanceMetrics();
}
