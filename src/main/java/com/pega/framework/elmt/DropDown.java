

package com.pega.framework.elmt;

import com.pega.framework.*;
import org.openqa.selenium.*;

import java.util.*;

public interface DropDown extends PegaWebElement {


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
