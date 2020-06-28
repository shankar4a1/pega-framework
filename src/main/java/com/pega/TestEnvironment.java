

package com.pega;

import com.pega.config.*;
import com.pega.framework.Keyboard;
import com.pega.framework.Mouse;
import com.pega.framework.*;
import com.pega.framework.http.*;
import com.pega.util.*;
import io.appium.java_client.*;
import org.openqa.selenium.interactions.*;

import java.io.*;

public interface TestEnvironment {
    String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    String VERSION = "$Id: TestEnvironment.java 170459 2016-01-08 12:18:51Z ChanukyaVempati $";

    void terminate();

    Browser getBrowser();

    Keyboard getKeyboard();

    RobotKeyboard getRobotKeyboard();

    Mouse getMouse();

    Actions getDriverActions();

    ScriptExecutor getScriptExecutor();

    PegaWebDriver getPegaDriver();

    Configuration getConfiguration();

    File getConfigFile();

    PegaHttpClient getPegaClient();

    AppiumDriver getAppiumDriver();

    TouchActions getTouchActions();

    ClientPerformanceUtil getClientPerformance();

    <T extends ObjectBean> T getObjectsBean();
}
