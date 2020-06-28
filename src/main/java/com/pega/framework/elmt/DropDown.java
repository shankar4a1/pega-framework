

package com.pega.framework.elmt;

import com.pega.framework.*;
import org.openqa.selenium.*;

import java.util.*;

public interface DropDown extends PegaWebElement {
    String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    String VERSION = "$Id: DropDown.java 125139 2015-02-22 15:23:22Z SachinVellanki $";

    boolean isMultiple();

    List<WebElement> getOptions();

    List<WebElement> getAllSelectedOptions();

    WebElement getFirstSelectedOption();

    void selectByVisibleText(final String p0);

    void selectByVisibleText(final String p0, final boolean p1);

    void selectByIndex(final int p0);

    void selectByValue(final String p0);

    void selectByValue(final String p0, final boolean p1);

    void deselectAll();

    void deselectByValue(final String p0);

    void deselectByIndex(final int p0);

    void deselectByVisibleText(final String p0);
}
