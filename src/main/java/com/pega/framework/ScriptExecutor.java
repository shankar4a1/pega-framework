

package com.pega.framework;

import org.openqa.selenium.*;

public interface ScriptExecutor {
    String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    String VERSION = "$Id: ScriptExecutor.java 178345 2016-02-25 14:12:15Z BalanaveenreddyKappeta $";

    @Deprecated
    void clear(final String p0);

    @Deprecated
    void click(final String p0);

    void mouseOver(final PegaWebElement p0);

    void clear(final PegaWebElement p0);

    void sendKeys(final PegaWebElement p0, final String p1);

    void sendKeys(final PegaWebElement p0, final String p1, final String p2);

    void fireKeyboardEvent(final PegaWebElement p0, final String p1);

    void rightClick(final PegaWebElement p0);

    void click(final PegaWebElement p0);

    Object executeJavaScript(final String p0);

    void click(final WebElement p0);

    void click(final PegaWebElement p0, final boolean p1);

    String getInnerText(final WebDriver p0, final WebElement p1);

    String getInnerText(final PegaWebElement p0);
}
