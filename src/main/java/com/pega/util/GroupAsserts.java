

package com.pega.util;

import com.pega.*;
import com.pega.exceptions.*;
import org.testng.*;

import java.util.*;

public class GroupAsserts {


    private static List<String> assertionFailures;

    static {
        GroupAsserts.assertionFailures = new ArrayList<String>();
    }

    public GroupAsserts() {

    }

    public static void assertTrue(final boolean condition, final String message) {
        try {
            Assert.assertTrue(condition, message);
        } catch (AssertionError e) {
            GroupAsserts.assertionFailures.add(e.getMessage());
        }
        Reporter.log("Group assertTrue statement executed", true);
    }

    public static void assertTrue(final boolean condition) {
        try {
            Assert.assertTrue(condition);
        } catch (AssertionError e) {
            GroupAsserts.assertionFailures.add(e.getMessage());
        }
        Reporter.log("Group assertTrue statement executed", true);
    }

    public static void assertEquals(final String actual, final String expected, final String message) {
        try {
            Assert.assertEquals(actual, expected, message);
        } catch (AssertionError e) {
            GroupAsserts.assertionFailures.add(e.getMessage());
        }
        Reporter.log("Group assertEquals statement executed for Expected: " + expected + " Actual: " + actual, true);
    }

    public static void assertEquals(final String actual, final String expected) {
        try {
            Assert.assertEquals(actual, expected);
        } catch (AssertionError e) {
            GroupAsserts.assertionFailures.add(e.getMessage());
        }
        Reporter.log("Group assertEquals statement executed for Expected: " + expected + " Actual: " + actual, true);
    }

    public static void assertEquals(final int actual, final int expected, final String message) {
        try {
            Assert.assertEquals(actual, expected, message);
        } catch (AssertionError e) {
            GroupAsserts.assertionFailures.add(e.getMessage());
        }
        Reporter.log("Group assertEquals statement executed for Expected: " + expected + " Actual: " + actual, true);
    }

    public static void assertEquals(final int actual, final int expected) {
        try {
            Assert.assertEquals(actual, expected);
        } catch (AssertionError e) {
            GroupAsserts.assertionFailures.add(e.getMessage());
        }
        Reporter.log("Group assertEquals statement executed for Expected: " + expected + " Actual: " + actual, true);
    }

    public static void throwAssertFailures(final TestEnvironment testEnv, String description) throws GroupAssertException {
        String message = "";
        if (GroupAsserts.assertionFailures.size() > 0) {
            final Iterator iterator = GroupAsserts.assertionFailures.iterator();
            while (iterator.hasNext()) {
                message = message + iterator.next() + "\n";
            }
            GroupAsserts.assertionFailures.clear();
            ScreenshotUtil.captureScreenshot(testEnv, description);
        }
        if (!message.equals("")) {
            throw new GroupAssertException(message);
        }
    }
}
