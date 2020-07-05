

package com.pega;

import com.pega.config.*;

import java.util.*;

public interface Configuration {


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
