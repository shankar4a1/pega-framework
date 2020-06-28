

package com.pega.framework.elmt;

import com.pega.*;
import com.pega.framework.*;
import com.pega.util.*;
import org.openqa.selenium.*;

import java.util.*;

public class ConditionBuilderImpl extends PegaWebElementImpl implements ConditionBuilder {
    private PegaWebElement curRow;
    private int curRowIndex;
    private String curRowXPath;
    private GlobalConstants.RecordType curRowType;
    private String eleXpath;
    private AnyPicker anyPicker;
    private List<String> conditionLabels;
    private String curLogicStyle;
    private String curAdvancedLogicString;
    private String curRowIdentifier;

    public ConditionBuilderImpl(final TestEnvironment testEnv, final WebElement elmt) {
        super(elmt);
        this.curRow = null;
        this.curRowIndex = 1;
        this.conditionLabels = null;
        this.curLogicStyle = null;
        this.curAdvancedLogicString = null;
        this.curRowIdentifier = null;
    }

    @Override
    public void addCriterion(final String ruleName, final String recordType, final String operator, final String value) {
        this.addCriterion(ruleName, this.getRecordTypeEnum(recordType), operator, value);
    }

    @Override
    public void addCriterion(final String ruleName, final GlobalConstants.RecordType recordType, final String operator, final String value) {
        this.setLeftField(ruleName, recordType);
        this.setOperator(operator);
        this.setRightField(value);
        this.curRowType = recordType;
    }

    @Override
    public void addCriterion(final String ruleName, final boolean value) {
        this.setLeftField(ruleName, GlobalConstants.RecordType.WHEN);
        this.setBooleanField(value);
    }

    @Override
    public void bootstrapRow() {
        this.curRowXPath = this.getCriterionRowXpath(this.curRowIndex);
        this.curRow = this.findElement(By.xpath(this.curRowXPath));
        this.anyPicker = this.findAnyPicker(this.curRowXPath + "//div[@data-test-id='20160509045644070512696']");
        this.pegaDriver.switchToActiveFrame();
        if (this.curLogicStyle == null) {
            this.curLogicStyle = this.getLogicStyle();
        }
        if (this.curAdvancedLogicString == null) {
            this.curAdvancedLogicString = this.getLogicString();
            System.out.println("BOOTSTRAP ON ULL TIME");
            System.out.println(this.curAdvancedLogicString);
        }
    }

    @Override
    public void addRow(final String recordType) {
        this.addRow(recordType, this.curRowIndex = this.getCriterionCount());
    }

    @Override
    public void addRow(final String recordType, int rowToAppend) {
        this.findElement(By.xpath(this.curRowXPath + "//div[@data-test-id='201803161342510972763']" + "//a")).click();
        this.pegaDriver.switchToActiveFrame();
        this.curRowIndex = ++rowToAppend;
        this.curRowType = this.getRecordTypeEnum(recordType);
        this.bootstrapRow();
        this.curRowIdentifier = this.findElement(By.xpath(this.curRowXPath + "//span[@data-test-id='2018110409142809601240']")).getText();
        this.setLogicString(this.curAdvancedLogicString = this.curAdvancedLogicString + " AND " + this.curRowIdentifier);
    }

    @Override
    public void setLogicStyle(final String logicStyle) {
        this.curRow.findSelectBox(By.xpath("//select[@data-test-id='20181101071726006049121']")).selectByVisibleText(logicStyle);
        final List<WebElement> advancedMode = this.pegaDriver.findElements(By.xpath("//button[@data-test-id='201608041031140988489']"));
        if (advancedMode.size() > 0) {
            this.findElement(By.xpath("//button[@data-test-id='201608041031140988489']")).click();
        }
        this.curLogicStyle = logicStyle;
    }

    @Override
    public String getLogicStyle() {
        return this.curRow.findSelectBox(By.xpath("//select[@data-test-id='20181101071726006049121']")).getText();
    }

    private void setLogicString(final String advancedLogicString) {
        final PegaWebElement logicStringEl = this.curRow.findElement(By.xpath("//span/input[@data-test-id='201510140801590246503763']"));
        logicStringEl.clear();
        logicStringEl.sendKeys(advancedLogicString);
    }

    private String getLogicString() {
        return this.findElement(By.xpath("//span/input[@data-test-id='201510140801590246503763']")).getAttribute("value");
    }

    @Override
    public List<List<String>> getTopLevelLeftFieldContents() {
        return this.anyPicker.getTopLevelContents();
    }

    @Override
    public List<List<String>> getRuleTypeLeftFieldContents(final String recordType) {
        return this.anyPicker.getRuleTypeContents(recordType);
    }

    private int getCriterionCount() {
        return this.findElements(By.xpath("//tr[starts-with(@data-test-id, '201811040938300671830-R')]")).size();
    }

    private AnyPicker findAnyPicker(final String anyPickerHandleXpath) {
        final By by = By.xpath(anyPickerHandleXpath);
        final WebElement elmt = this.findElement(by);
        final AnyPicker anyPicker = new AnyPickerImpl(elmt);
        anyPicker._setEnvironment(this.testEnv, by, this.pegaDriver.getActiveFrameId(false));
        return anyPicker;
    }

    private String getCriterionRowXpath(final int rowNum) {
        return "//tr[@data-test-id='201811040938300671830-R" + rowNum + "']";
    }

    private void setLeftField(final String ruleName, final GlobalConstants.RecordType recordType) {
        if (recordType.getType() == null) {
            this.anyPicker.selectValue(ruleName);
        } else {
            this.anyPicker.selectValue(recordType.getType(), ruleName);
        }
        this.pegaDriver.switchToActiveFrame();
    }

    private void setOperator(final String operator) {
        this.curRow.findSelectBox(By.xpath(this.curRowXPath + "//select[@data-test-id='2016030403503300729539']")).selectByVisibleText(operator);
        this.pegaDriver.switchToActiveFrame();
    }

    private void setRightField(final String value) {
        if (this.curRowType.getType() == "Strategy") {
            this.curRow.findSelectBox(By.xpath(this.curRowXPath + "//select[@data-test-id='20151012085636097134858']")).selectByVisibleText(value);
        } else {
            this.curRow.findElement(By.xpath(this.curRowXPath + "//input[@data-test-id='20151012085636097134858']")).sendKeys(value);
        }
        this.pegaDriver.switchToActiveFrame();
    }


    private void setBooleanField(final boolean value) {
        this.curRow.findSelectBox(By.xpath(this.curRowXPath + "//select[@data-test-id='2016030403503300729539']")).selectByVisibleText("is " + value);
    }

    private GlobalConstants.RecordType getRecordTypeEnum(final String recordType) {
        final String lowerCase;
        switch (lowerCase = recordType.toLowerCase()) {
            case "when": {
                return GlobalConstants.RecordType.WHEN;
            }
            case "strategyresult": {
                return GlobalConstants.RecordType.STRATEGY_RESULT;
            }
            case "strategy": {
                return GlobalConstants.RecordType.STRATEGY;
            }
            default:
                break;
        }
        return GlobalConstants.RecordType.PROPERTY;
    }
}
