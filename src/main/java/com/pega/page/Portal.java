

package com.pega.page;

import com.pega.*;
import com.pega.framework.*;
import com.pega.framework.elmt.*;
import org.openqa.selenium.*;

import java.util.*;

public interface Portal {


    String getTitle();

    void refresh();

    void deleteAllCookies();

    void navigateTo(final String p0);

    PegaWebElement findElement(final By p0);

    PegaWebElement findElement(final By p0, final boolean p1);

    Frame findFrame(final String p0);

    DropDown findSelectBox(final By p0);

    AutoComplete findAutoComplete(final By p0);

    String getActiveFrameId(final boolean p0);

    boolean verifyElement(final PegaWebElement p0, final By p1);

    boolean verifyElement(final By p0);

    boolean verifyElementVisible(final By p0);

    TestEnvironment getTestEnv();

    Frame findFrame(final PegaWebElement p0);

    List<WebElement> findElements(final By p0);
}
