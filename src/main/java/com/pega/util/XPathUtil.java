

package com.pega.util;

public class XPathUtil {
    String COPYRIGHT;
    private static final String VERSION = "$Id: XPathUtil.java 208767 2016-09-13 18:24:16Z SachinVellanki $";

    public XPathUtil() {
        this.COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    }

    public static String getDataTestIDXpath(final String dataTestID) {
        return "//*[@data-test-id='" + dataTestID + "']";
    }

    public static String getMenuItemXPath(final String menuItemText) {
        return "(//span[@class='menu-item-title' and text()='" + menuItemText + "'])[last()]";
    }

    public static String getTdMenuItemXPath(final String menuItemText) {
        return "//td[@id='ItemMiddle' and text()='" + menuItemText + "']";
    }

    public static String getLiMenuItemXPath(final String menuItemText) {
        return "//li[@title='" + menuItemText + "']";
    }

    public static String getButtonPzBtnMidXPath(final String buttonText) {
        return "//div[@class='pzbtn-mid' and contains(text(),'" + buttonText + "')]";
    }

    public static String getButtonStrongPzhcPzBtnXPath(final String buttonText) {
        return "//button[@class='Strong pzhc pzbutton' and contains(text(),'" + buttonText + "')]";
    }

    public static String getButtonSimplePzhcPzBtnXPath(final String buttonText) {
        return "//button[@class='Simple pzhc pzbutton' and contains(text(),'" + buttonText + "')]";
    }

    public static String getButtonPzBtnMidXPathWithoutDisplayNone(final String buttonText) {
        return "//div[@class='pzbtn-mid' and contains(text(),'" + buttonText + "')][not(.//ancestor::div[contains(@style,'none')])]";
    }

    public static String getButtonPzBtnRndXPath(final String buttonText) {
        return "//div[@class='pzbtn-rnd' and contains(text(),'" + buttonText + "')]";
    }

    public static String getButtonPzhcBtnXPath(final String buttonText) {
        return "//button[@class='pzhc pzbutton' and contains(text(),'" + buttonText + "')]";
    }

    public static String getButtonTdBtnXpath(final String buttonText) {
        return "//button[@class='buttonTdButton' and contains(text(),'" + buttonText + "')]";
    }

    public static String getTabHeaderXpath(final String tabHeader) {
        return "//a[contains(@tabtitle,'" + tabHeader + "')]";
    }

    public static String getColHeaderXpath(final String colHeader) {
        return "//div[text()='" + colHeader + "']";
    }

    public static String getButtonXpathByDataID(final String dataID) {
        return "//button[@data-test-id='" + dataID + "']";
    }

    public static String getInputXpathByDataID(final String dataID) {
        return "//input[@data-test-id='" + dataID + "']";
    }

    public static String getAnchorXpathByDataID(final String dataID) {
        return "//a[@data-test-id='" + dataID + "']";
    }

    public static String getSpanXpathByDataID(final String dataID) {
        return "//span[@data-test-id='" + dataID + "']";
    }

    public static String getCreateCaseMenuItemXpath(final String caseName) {
        return "//ul[@role='menu' and contains(@style,'block') and contains(@id,'ppyElements')]//a[@role='menuitem' and contains(@data-click,'createNewWork')]//span[contains(text(),'" + caseName + "')]";
    }

    public static String getAutoCompleteMenuItem(final String itemText) {
        return "//div[contains(@class,'autocompleteAG')]//span[text()='" + itemText + "']";
    }

    public static String getLayoutXpath(final String layoutNumber) {
        return "(//fieldset[.//span[text()='" + layoutNumber + "']])[position()=last()]//div[@id='dragSimpleLayout-DIV']";
    }

    public static String getRepeatingGridLayoutXpath(final String layoutNumber) {
        return "//fieldset[.//span[text()='" + layoutNumber + "']][@id='repeatingFieldSet']";
    }

    public static String getButtonpzButtonXpath(final String buttonText) {
        return "//button[contains(@class,'pzbutton') and contains(text(),'" + buttonText + "')]";
    }

    public static String getButtonpzButtonXpathWithoutDisplayNone(final String buttonText) {
        return "//button[contains(@class,'pzbutton') and contains(text(),'" + buttonText + "')][not(.//ancestor::div[contains(@style,'none')])]";
    }

    public static String getSpanXpathByText(final String text) {
        return "//span[text()='" + text + "']";
    }

    public static String getRadioLabelXPath(final String label) {
        return "//label[contains(@class, 'radioLabel') and text()='" + label + "']";
    }

    public static String getRadioLabelOrSpanXpath(final String text) {
        return "//label[contains(@class,'radioLabel') and text()='" + text + "']" + "|" + "//span[text()='" + text + "']";
    }

    public static String getMatchHighlightSpanXpath(final String text) {
        return "//span[@class='match-highlight'][text()='" + text + "']";
    }

    public static String getWorkObjectStepInstructionXpath(final String text) {
        return "//*[contains(text(),'" + text + "')]";
    }

    public static String getCreateApplicationViewButtonXPath(final String buttonText) {
        return "//*[@data-test-id='channel-type-tile' and text()='" + buttonText + "']";
    }

    public static String getCheckboxContainingNameXpath(final String name) {
        return "//input[@type='checkbox' and contains(@name,'" + name + "')]";
    }

    public static String getInputContainingNameXpath(final String name) {
        return "//input[contains(@name,'" + name + "') or contains(@id,'" + name + "')]";
    }

    public static String getSelectContainingNameXpath(final String name) {
        return "//select[contains(@name,'" + name + "')]";
    }

    public static String getClentPerformanceXpath() {
        return "//div[@class='client-performance']";
    }
}
