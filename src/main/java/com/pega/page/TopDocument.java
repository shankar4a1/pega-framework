

package com.pega.page;

import com.pega.*;
import com.pega.framework.*;
import com.pega.framework.elmt.*;
import com.pega.ri.*;
import org.openqa.selenium.*;

import java.util.*;

public interface TopDocument {
    String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    String VERSION = "$Id: PegaWebDriver.java 174698 2016-02-08 08:24:26Z SachinVellanki $";

    PegaWebElement findElement(final By p0);

    PegaWebElement findElement(final By p0, final boolean p1);

    @Deprecated
    Frame findFrame(final String p0);

    Wizard findWizard(final String p0);

    DropDown findSelectBox(final By p0);

    AutoComplete findAutoComplete(final By p0);

    String getActiveFrameId(final boolean p0);

    boolean verifyElement(final PegaWebElement p0, final By p1);

    boolean verifyElement(final By p0);

    boolean verifyElementVisible(final By p0);

    TestEnvironment getTestEnv();

    Frame findFrame(final PegaWebElement p0);

    Wizard findWizard(final PegaWebElement p0);

    List<WebElement> findElements(final By p0);
}
