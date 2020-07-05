

package com.pega.framework;

import io.appium.java_client.*;
import org.openqa.selenium.*;

public interface TouchActions {


    TouchAction press(final WebElement p0);

    TouchAction press(final int p0, final int p1);

    TouchAction press(final WebElement p0, final int p1, final int p2);

    TouchAction release();

    TouchAction moveTo(final WebElement p0);

    TouchAction moveTo(final int p0, final int p1);

    TouchAction moveTo(final WebElement p0, final int p1, final int p2);

    TouchAction tap(final WebElement p0);

    TouchAction tap(final int p0, final int p1);

    TouchAction tap(final WebElement p0, final int p1, final int p2);

    TouchAction waitAction();

    TouchAction waitAction(final int p0);

    TouchAction longPress(final WebElement p0);

    TouchAction longPress(final int p0, final int p1);

    TouchAction longPress(final WebElement p0, final int p1, final int p2);

    void cancel();

    TouchAction perform();

    void swipe(final int p0, final int p1, final int p2, final int p3);

    void swipe(final PegaWebElement p0, final PegaWebElement p1);

    void swipe(final PegaWebElement p0, final int p1, final int p2);

    void swipe(final PegaWebElement p0, final SwipeDirection p1);

    void scroll(final PegaWebElement p0, final SwipeDirection p1, final int p2);

    void swipe(final SwipeDirection p0);
}
