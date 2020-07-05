

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
