

package com.pega.ri;

import com.pega.framework.elmt.*;
import com.pega.util.*;

public interface Wizard extends Frame {
    String VERSION = "$Id: Wizard.java 208871 2016-09-15 13:06:30Z VenkatasrikarVadlamudi $";
    String REFRESH_BTN_XPATH = XPathUtil.getMenuItemXPath("Refresh");
    String ACTION_BUTTON_XPATH = XPathUtil.getButtonpzButtonXpath(LocalizationUtil.getLocalizedWord("Actions")) + "|" + XPathUtil.getButtonPzBtnMidXPath("Actions");
    String HEADER_AGENTS_DIV_XPATH = "//*[text()='Agents']";
    String HEADER_TITLE_SPAN_XPATH = "//span[@class='workarea_header_titles']";
    String NEXTBUTTON_XPATH = "//button[not(contains(@data-click,'disabled'))][div//div[@class='pzbtn-mid' and contains(text(),'Next')]]";
    String FINISHBUTTON_XPATH = "//button[not(contains(@data-click,'disabled'))][div//div[@class='pzbtn-mid' and contains(text(),'Finish')]]";
    String DONE_BTN_XPATH = XPathUtil.getButtonpzButtonXpath("Done");
    String CLOSE_BUTTON_XPATH = "//i[contains(@class,'pi-close')]|" + XPathUtil.getButtonPzBtnMidXPath(LocalizationUtil.getLocalizedWord("Close"));
    String LAST_TAB_NAME_LABEL_XPATH = "//div[@id='workarea']//ol[@title='Currently open']/following-sibling::div//ul[@role='tablist']//li[last()-1]//span[contains(@style,'display')]";
    String NEWOP_BTN_XPATH = XPathUtil.getButtonTdBtnXpath("New");
    String SHORTDESC_INPUT = "//input[contains(@name,'pyLabel')]";
    String OPERATORID_INPUT = "//input[contains(@name,'pyUserIdentifier')]";
    String CREATEANDOPEN_BUTTON = XPathUtil.getButtonPzBtnMidXPath("Create and open");
    String ACESSGRPUPRADIO_XPATH = "//*[@id='DefaultAG']";
    String ACESSGRPUP_INPUT_XPATH = "//input[contains(@name,'pyLabel')]";
    String SAVE_BUTTON1 = "//button[contains(text(),'Save') and not(@data-test-id='Save_and_compare_button')]";
    String UPDATE_BUTTON_XPATH = XPathUtil.getButtonPzBtnMidXPath("Update");
    String UPDATEORG_BTN_XPATH = XPathUtil.getButtonTdBtnXpath("Update");
    String RULECHECKOUT_XPATH = "//*[contains(@name,'pyAllowRuleCheckOut') and @type='checkbox']";
    String SAVE_AND_RUN_BTN_XPATH = XPathUtil.getButtonPzhcBtnXPath("Save and run");

    void close();

    void next();

    void finish();

    void switchTab(final String p0);

    String getTitle();

    void refresh();

    String getHeaderTitle();

    void done();

    void newOperator(final String p0, final String p1);

    void updateOrganization(final String p0);

    void ruleCheckOut();

    void save();

    void saveAndRun();

    Wizard findWizard(final String p0);
}
