

package com.pega.util;

import com.pega.*;
import io.cucumber.java.*;
import org.openqa.selenium.*;

import java.lang.reflect.*;

public class ScreenshotUtil {


    public ScreenshotUtil() {

    }

    public static void captureScreenshot(final TestEnvironment testEnv, String description) {
        try {
            final Class c = Class.forName("com.pega.MyAppTestEnvironment");
            final Method m = c.getDeclaredMethod("getScenario", new Class[0]);
            final Scenario scenario = (Scenario) m.invoke(c.cast(testEnv), new Object[0]);
            //altered by SG
            testEnv.getPegaDriver().waitForDocStateReady(5);
            try {

                final byte[] screenshot = ((TakesScreenshot) testEnv.getPegaDriver().getDriver()).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", description);
            } catch (Exception e2) {
                scenario.log("Unable to take screenshot<br/>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
