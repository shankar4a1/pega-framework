

package com.pega.ri;

import com.pega.*;
import com.pega.framework.*;
import com.pega.framework.elmt.*;
import org.openqa.selenium.*;
import org.slf4j.*;

public class WizardImpl extends FrameImpl implements Wizard {
    String OrgName;
    String ORGANIZATION_EXPAND_XPATH;
    String DIV_EXPAND_XPATH;
    String UNIT_BUTTON_XPATH;

    private static final Logger LOGGER;

    static {
        LOGGER = LoggerFactory.getLogger(Wizard.class.getName());
    }

    public WizardImpl(final String frameId, final TestEnvironment testEnv) {
        super(frameId, testEnv);
        this.ORGANIZATION_EXPAND_XPATH = ".//span[contains(text(),'" + this.OrgName + "')]//ancestor::li//a[@class='expandNode']";
        this.DIV_EXPAND_XPATH = ".//span[contains(text(),'Div')]//ancestor::li//a[@class='expandNode']";
        this.UNIT_BUTTON_XPATH = "//table[contains(@param_name,'EXPANDEDSubSection')][.//span[contains(text(),'Update Organizational Unit')]]//span[text()='Unit'][not(.//ancestor::*[contains(@style,'display: none')])]";
    }

    public WizardImpl(final PegaWebElement element) {
        super(element);
        this.ORGANIZATION_EXPAND_XPATH = ".//span[contains(text(),'" + this.OrgName + "')]//ancestor::li//a[@class='expandNode']";
        this.DIV_EXPAND_XPATH = ".//span[contains(text(),'Div')]//ancestor::li//a[@class='expandNode']";
        this.UNIT_BUTTON_XPATH = "//table[contains(@param_name,'EXPANDEDSubSection')][.//span[contains(text(),'Update Organizational Unit')]]//span[text()='Unit'][not(.//ancestor::*[contains(@style,'display: none')])]";
    }

    @Override
    public void close() {
        this.pegaDriver.switchToActiveFrame();
        this.findElement(By.xpath(WizardImpl.CLOSE_BUTTON_XPATH)).click(false);
        this.pegaDriver.waitForDocStateReady(3);
        this.pegaDriver.switchToActiveFrame();
    }

    @Override
    public void switchTab(final String tabName) {
        this.findElement(By.xpath(".//*[contains(text(),'" + tabName + "') and @class='layout-group-item-title']|.//*[contains(text(),'" + tabName + "') and @class='textIn']")).click();
    }

    @Override
    public String getTitle() {
        this.pegaDriver.switchTo().defaultContent();
        final String tabName = this.pegaDriver.findElement(By.xpath("//div[@id='workarea']//ol[@title='Currently open']/following-sibling::div//ul[@role='tablist']//li[last()-1]//span[contains(@style,'display')]")).getText();
        this.pegaDriver.switchToActiveFrame();
        return tabName;
    }

    @Override
    public void refresh() {
        this.pegaDriver.switchToActiveFrame();
        final PegaWebElement actions = this.findElement(By.xpath(WizardImpl.ACTION_BUTTON_XPATH));
        this.pegaDriver.handleWaits().waitForElementVisibility(By.xpath(WizardImpl.ACTION_BUTTON_XPATH));
        this.pegaDriver.handleWaits().waitForElementClickable(By.xpath(WizardImpl.ACTION_BUTTON_XPATH));
        actions.click();
        final PegaWebElement refLink = this.findElement(By.xpath(WizardImpl.REFRESH_BTN_XPATH));
        this.pegaDriver.handleWaits().waitForElementVisibility(refLink);
        this.pegaDriver.handleWaits().waitForElementClickable(By.xpath(WizardImpl.REFRESH_BTN_XPATH));
        refLink.click();
    }

    @Override
    public String getHeaderTitle() {
        return this.findElement(By.xpath("//span[@class='workarea_header_titles']")).getText();
    }

    @Override
    public void next() {
        this.findElement(By.xpath("//button[not(contains(@data-click,'disabled'))][div//div[@class='pzbtn-mid' and contains(text(),'Next')]]")).click();
    }

    @Override
    public void finish() {
        this.findElement(By.xpath("//button[not(contains(@data-click,'disabled'))][div//div[@class='pzbtn-mid' and contains(text(),'Finish')]]")).click();
    }

    @Override
    public void done() {
        if (this.pegaDriver.verifyElement(By.xpath(WizardImpl.DONE_BTN_XPATH))) {
            this.findElement(By.xpath(WizardImpl.DONE_BTN_XPATH)).click(false);
        }
        this.pegaDriver.waitForDocStateReady();
        this.pegaDriver.switchToActiveFrame();
    }

    @Override
    public void newOperator(final String operator, final String applicationaname) {
        this.pegaDriver.waitForDocStateReady();
        this.pegaDriver.switchToActiveFrame();
        this.findElement(By.xpath(WizardImpl.NEWOP_BTN_XPATH)).click(false);
        this.pegaDriver.waitForDocStateReady();
        this.pegaDriver.switchToActiveFrame();
        this.findElement(By.xpath("//input[contains(@name,'pyLabel')]")).sendKeys(operator);
        this.findElement(By.xpath("//input[contains(@name,'pyUserIdentifier')]")).sendKeys(operator);
        this.findElement(By.xpath(WizardImpl.CREATEANDOPEN_BUTTON)).click();
        this.pegaDriver.waitForDocStateReady();
        this.findElement(By.xpath("//input[contains(@name,'pyLabel')]")).sendKeys(applicationaname + ":Administrators");
        this.findElement(By.xpath("//input[contains(@name,'pyLabel')]")).sendKeys(Keys.TAB);
        this.pegaDriver.waitForDocStateReady();
    }

    @Override
    public void save() {
        this.pegaDriver.switchToActiveFrame();
        this.findElement(By.xpath("//button[contains(text(),'Save') and not(@data-test-id='Save_and_compare_button')]")).click(false);
        this.pegaDriver.waitForDocStateReady();
    }

    @Override
    public void saveAndRun() {
        this.findElement(By.xpath(WizardImpl.SAVE_AND_RUN_BTN_XPATH)).click(false);
        this.pegaDriver.waitForDocStateReady();
    }

    @Override
    public void updateOrganization(final String orgName) {
        this.pegaDriver.waitForDocStateReady();
        this.findElement(By.xpath(WizardImpl.UPDATE_BUTTON_XPATH)).click(false);
        this.pegaDriver.waitForDocStateReady();
        this.findElement(By.xpath(".//span[contains(text(),'" + orgName + "')]//ancestor::li//a[@class='expandNode']")).click();
        this.pegaDriver.waitForDocStateReady();
        this.findElement(By.xpath(this.DIV_EXPAND_XPATH)).click();
        this.pegaDriver.waitForDocStateReady();
        this.findElement(By.xpath(this.UNIT_BUTTON_XPATH)).click();
        this.pegaDriver.waitForDocStateReady();
        this.findElement(By.xpath(WizardImpl.UPDATEORG_BTN_XPATH)).click();
        this.pegaDriver.waitForDocStateReady();
    }

    @Override
    public void ruleCheckOut() {
        this.pegaDriver.waitForDocStateReady();
        if (!this.findElement(By.xpath("//*[contains(@name,'pyAllowRuleCheckOut') and @type='checkbox']")).isSelected()) {
            this.findElement(By.xpath("//*[contains(@name,'pyAllowRuleCheckOut') and @type='checkbox']")).click();
        }
        this.pegaDriver.waitForDocStateReady();
    }

    @Override
    public Wizard findWizard(final String frameId) {
        this.pegaDriver.handleWaits().waitForElementPresence(By.id(frameId));
        final Wizard wizard = new WizardImpl(frameId, this.testEnv);
        wizard._initialize(this.testEnv, By.id(frameId));
        wizard._setDOMPointer(this.getFrameDocument() + ".defaultView.frames['" + frameId + "']");
        wizard._setFrames(null, By.id(frameId));
        WizardImpl.LOGGER.debug("driver location before frame switch:" + this.pegaDriver.getCurrentUrl());
        this.pegaDriver.switchTo().frame(frameId);
        WizardImpl.LOGGER.debug("driver location after frame switch:" + this.pegaDriver.getCurrentUrl());
        return wizard;
    }
}
