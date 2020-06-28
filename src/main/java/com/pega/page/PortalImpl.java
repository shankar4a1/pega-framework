

package com.pega.page;

import com.pega.*;
import com.pega.framework.*;
import com.pega.framework.elmt.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.*;
import org.slf4j.*;

import java.util.*;

public class PortalImpl implements Portal {
    private static final String VERSION = "$Id: PortalImpl.java 186528 2016-04-09 12:01:15Z SachinVellanki $";
    private static final Logger LOGGER;
    protected PegaWebDriver pegaDriver;
    protected TestEnvironment testEnv;
    protected ScriptExecutor scriptExecutor;
    protected Actions actions;
    protected WaitHandler waitHandler;
    private Frame frame;

    static {
        LOGGER = LoggerFactory.getLogger(Portal.class.getName());
    }

    public PortalImpl(final TestEnvironment testEnv) {
        this.testEnv = testEnv;
        this.pegaDriver = testEnv.getPegaDriver();
        this.actions = testEnv.getDriverActions();
        this.scriptExecutor = testEnv.getScriptExecutor();
        this.waitHandler = this.pegaDriver.handleWaits();
    }

    public PortalImpl(final Frame frame) {
        this.frame = frame;
        this.testEnv = this.frame.getTestEnvironment();
        this.pegaDriver = this.testEnv.getPegaDriver();
        this.actions = this.testEnv.getDriverActions();
        this.scriptExecutor = this.testEnv.getScriptExecutor();
        this.waitHandler = this.pegaDriver.handleWaits();
    }

    @Override
    public String getTitle() {
        return this.pegaDriver.getTitle();
    }

    @Override
    public void refresh() {
        PortalImpl.LOGGER.info("Refreshing the page...");
        this.testEnv.getBrowser().refresh();
    }

    @Override
    public void deleteAllCookies() {
        this.pegaDriver.manage().deleteAllCookies();
    }

    @Override
    public void navigateTo(final String newURL) {
        this.pegaDriver.navigate().to(newURL);
    }

    @Override
    public PegaWebElement findElement(final By by) {
        return (this.frame != null) ? this.frame.findElement(by) : this.pegaDriver.findElement(by);
    }

    @Override
    public boolean verifyElement(final By by) {
        return (this.frame != null) ? this.frame.verifyElement(by) : this.pegaDriver.verifyElement(by);
    }

    @Override
    public DropDown findSelectBox(final By by) {
        return (this.frame != null) ? this.frame.findSelectBox(by) : this.pegaDriver.findSelectBox(by);
    }

    @Override
    public AutoComplete findAutoComplete(final By by) {
        return (this.frame != null) ? this.frame.findAutoComplete(by) : this.pegaDriver.findAutoComplete(by);
    }

    @Override
    public PegaWebElement findElement(final By by, final boolean switchToDefault) {
        return (this.frame != null) ? this.frame.findElement(by, switchToDefault) : this.pegaDriver.findElement(by, switchToDefault);
    }

    @Override
    public Frame findFrame(final String id) {
        return (this.frame != null) ? this.frame.findFrame(id) : this.pegaDriver.findFrame(id);
    }

    @Override
    public String getActiveFrameId(final boolean switchToActiveFrame) {
        return (this.frame != null) ? this.frame.getActiveFrameId(switchToActiveFrame) : this.pegaDriver.getActiveFrameId(switchToActiveFrame);
    }

    @Override
    public boolean verifyElement(final PegaWebElement elmt, final By locator) {
        return (this.frame != null) ? this.frame.verifyElement(elmt, locator) : this.pegaDriver.verifyElement(elmt, locator);
    }

    @Override
    public boolean verifyElementVisible(final By locator) {
        return (this.frame != null) ? this.frame.verifyElementVisible(locator) : this.pegaDriver.verifyElementVisible(locator);
    }

    @Override
    public TestEnvironment getTestEnv() {
        return this.testEnv;
    }

    @Override
    public Frame findFrame(final PegaWebElement element) {
        return (this.frame != null) ? this.frame.findFrame(element) : this.pegaDriver.findFrame(element);
    }

    @Override
    public List<WebElement> findElements(final By by) {
        return (this.frame != null) ? this.frame.findElements(by) : this.pegaDriver.findElements(by);
    }
}
