

package com.pega.util;

import com.pega.*;
import io.cucumber.java.*;
import org.openqa.selenium.*;

import java.lang.reflect.*;

public class ScreenshotUtil {
    String COPYRIGHT;
    private static final String VERSION = "$Id: ScreenshotUtil.java 198173 2016-06-16 05:13:57Z PavanBeri $";

    public ScreenshotUtil() {
        this.COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    }

    public static void captureScreenshot(final TestEnvironment testEnv) {
        try {
            final Class c = Class.forName("com.pega.MyAppTestEnvironment");
            final Method m = c.getDeclaredMethod("getScenario", new Class[0]);
            final Scenario scenario = (Scenario) m.invoke(c.cast(testEnv), new Object[0]);
            try {
                final byte[] screenshot = ((TakesScreenshot) testEnv.getPegaDriver().getDriver()).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "ScreenShot");
            } catch (Exception e2) {
                scenario.log("Unable to take screenshot<br/>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
