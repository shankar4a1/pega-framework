

package com.pega.framework.elmt;

import com.pega.framework.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.util.*;

public class DropDownImpl extends PegaWebElementImpl implements DropDown {
    private static final String VERSION = "$Id: DropDownImpl.java 208484 2016-09-06 14:05:05Z PavanBeri $";
    private Select selectbox;

    public DropDownImpl(final WebElement elmt) {
        super(elmt);
        this.selectbox = new Select(elmt);
    }

    public DropDownImpl(final WebElement elmt, final String elmtId) {
        super(elmt, elmtId);
        this.selectbox = new Select(elmt);
    }

    @Override
    public boolean isMultiple() {
        return this.selectbox.isMultiple();
    }

    @Override
    public List<WebElement> getOptions() {
        return this.selectbox.getOptions();
    }

    @Override
    public List<WebElement> getAllSelectedOptions() {
        return this.selectbox.getAllSelectedOptions();
    }

    @Override
    public WebElement getFirstSelectedOption() {
        return this.selectbox.getFirstSelectedOption();
    }

    @Override
    public void selectByVisibleText(final String text) {
        this.selectByVisibleText(text, true);
    }

    @Override
    public void selectByVisibleText(final String text, final boolean isWait) {
        if (this.testEnv.getConfiguration().getBrowserConfig().getBrowserName().equalsIgnoreCase("edge")) {
            this.sendKeys("");
        }
        this.pegaDriver.handleWaits().waitForElementPresence(By.xpath("//option[contains(text(),\"" + text + "\")]"));
        this.selectbox.selectByVisibleText(text);
        if (isWait) {
            this.pegaDriver.waitForDocStateReady(2);
        }
    }

    @Override
    public void selectByIndex(final int index) {
        if (this.testEnv.getConfiguration().getBrowserConfig().getBrowserName().equalsIgnoreCase("edge")) {
            this.sendKeys("");
        }
        this.selectbox.selectByIndex(index);
        this.pegaDriver.waitForDocStateReady(2);
    }

    @Override
    public void selectByValue(final String value) {
        this.selectByValue(value, true);
    }

    @Override
    public void selectByValue(final String value, final boolean isWait) {
        if (this.testEnv.getConfiguration().getBrowserConfig().getBrowserName().equalsIgnoreCase("edge")) {
            this.sendKeys("");
        }
        this.selectbox.selectByValue(value);
        if (isWait) {
            this.pegaDriver.waitForDocStateReady(2);
        }
    }

    @Override
    public void deselectAll() {
        this.selectbox.deselectAll();
    }

    @Override
    public void deselectByValue(final String value) {
        this.selectbox.deselectByValue(value);
    }

    @Override
    public void deselectByIndex(final int index) {
        this.selectbox.deselectByIndex(index);
    }

    @Override
    public void deselectByVisibleText(final String text) {
        this.selectbox.deselectByVisibleText(text);
    }
}
