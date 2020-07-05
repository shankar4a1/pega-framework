

package com.pega.page;

import com.pega.*;
import com.pega.framework.*;
import com.pega.framework.elmt.*;
import com.pega.ri.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.*;

import java.util.*;

public class TopDocumentImpl implements TopDocument {

    protected PegaWebDriver pegaDriver;
    protected TestEnvironment testEnv;
    protected ScriptExecutor scriptExecutor;
    protected Actions actions;
    protected WaitHandler waitHandler;

    public TopDocumentImpl(final TestEnvironment testEnv) {
        this.testEnv = testEnv;
        this.pegaDriver = testEnv.getPegaDriver();
        this.actions = testEnv.getDriverActions();
        this.scriptExecutor = testEnv.getScriptExecutor();
        this.waitHandler = this.pegaDriver.handleWaits();
    }

    @Override
    public PegaWebElement findElement(final By by, final boolean switchToDefaultContent) {
        return this.pegaDriver.findElement(by, switchToDefaultContent);
    }

    @Override
    public PegaWebElement findElement(final By by) {
        return this.findElement(by, this.testEnv.getConfiguration().isAutoSwitchToDefaultContent());
    }

    @Override
    public Frame findFrame(final String frameId) {
        return this.pegaDriver.findFrame(frameId);
    }

    @Override
    public List<WebElement> findElements(final By by) {
        return this.pegaDriver.findElements(by);
    }

    @Override
    public boolean verifyElement(final PegaWebElement elmt, final By locator) {
        return this.pegaDriver.verifyElement(elmt, locator);
    }

    @Override
    public boolean verifyElement(final By locator) {
        return this.pegaDriver.verifyElement(locator);
    }

    @Override
    public String getActiveFrameId(final boolean switchToActiveFrame) {
        return this.pegaDriver.getActiveFrameId(switchToActiveFrame);
    }

    @Override
    public DropDown findSelectBox(final By by) {
        return this.pegaDriver.findSelectBox(by);
    }

    @Override
    public Wizard findWizard(final String frameId) {
        return this.pegaDriver.findWizard(frameId);
    }

    @Override
    public boolean verifyElementVisible(final By locator) {
        return this.pegaDriver.verifyElementVisible(locator);
    }

    @Override
    public AutoComplete findAutoComplete(final By by) {
        return this.pegaDriver.findAutoComplete(by);
    }

    @Override
    public TestEnvironment getTestEnv() {
        return this.testEnv;
    }

    @Override
    public Frame findFrame(final PegaWebElement element) {
        return this.pegaDriver.findFrame(element);
    }

    @Override
    public Wizard findWizard(final PegaWebElement element) {
        return this.pegaDriver.findWizard(element);
    }
}
