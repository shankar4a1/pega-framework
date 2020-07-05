

package com.pega.framework;

import com.pega.*;
import com.pega.framework.elmt.*;
import com.pega.ri.*;
import org.openqa.selenium.*;

import java.util.*;

public interface PegaWebDriver extends WebDriver {


    WebDriver getDriver();

    void waitForDocStateReady();

    void waitForDocStateReady(final int p0);

    void waitForDocStateReady(final boolean p0);

    void waitForDocStateReady(final int p0, final boolean p1);

    PegaWebElement findElement(final By p0);

    PegaWebElement findElement(final By p0, final boolean p1);

    @Deprecated
    Frame findFrame(final String p0);

    Wizard findWizard(final String p0);

    DropDown findSelectBox(final By p0);

    AutoComplete findAutoComplete(final By p0);

    void switchToActiveFrame();

    @Deprecated
    String getActiveFrameId();

    String getActiveFrameId(final boolean p0);

    boolean verifyElement(final PegaWebElement p0, final By p1);

    boolean verifyElement(final By p0);

    void switchToActiveFrame(final String p0);

    WaitHandler handleWaits();

    PegaWebElement convertToPegaWebElmt(final WebElement p0, final By p1, final Frame p2);

    void loadCustomScripts();

    boolean verifyElementVisible(final By p0);

    int getDefaultFrameTabCntDiff(final boolean p0);

    TestEnvironment getTestEnv();

    void waitForDocStateReady(final int p0, final boolean p1, final boolean p2);

    void switchToActiveFrame(final LinkedHashSet<By> p0);

    Frame findFrame(final PegaWebElement p0);

    Wizard findWizard(final PegaWebElement p0);

    String getTextStaleException(final WebElement p0);
}
