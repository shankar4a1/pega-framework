

package com.pega.framework.elmt;

import com.pega.framework.*;
import com.pega.util.*;

import java.util.*;

public interface ConditionBuilder extends PegaWebElement {
    String EXPRESSION_BUILDER_INPUT_ID = "pyExpressionBuilder";
    String EXPRESSION_BUILDER_ALL_CRITERION = "//tr[starts-with(@data-test-id, '201811040938300671830-R')]";
    String EXPRESSION_BUILDER_CRITERION_PREFIX = "//tr[@data-test-id='201811040938300671830-R";
    String EXPRESSION_BUILDER_TRUEFALSE = "//select[@data-test-id='2016030403503300729539']";
    String EXPRESSION_BUILDER_LOGIC_SETTING = "//select[@data-test-id='20181101071726006049121']";
    String EXPRESSION_BUILDER_LOGIC_STRING = "//span/input[@data-test-id='201510140801590246503763']";
    String EXPRESSION_BUILDER_ROW_IDENTIFIER = "//span[@data-test-id='2018110409142809601240']";
    String EXPRESSION_BUILDER_OPERATOR = "//select[@data-test-id='2016030403503300729539']";
    String EXPRESSION_BUILDER_RIGHTFIELD_DROPDOWN = "//select[@data-test-id='20151012085636097134858']";
    String EXPRESSION_BUILDER_RIGHTFIELD_VALUE = "//input[@data-test-id='20151012085636097134858']";
    String EXPRESSION_BUILDER_ANDORDROPDOWN = "//select data-test-id='20160304043156072528706']";
    String EXPRESSION_BUILDER_CONFIRM_DIALOG_OK_BUTTON = "//button[@data-test-id='201608041031140988489']";
    String EXPRESSION_BUILDER_ADD_ROWS = "//div[@data-test-id='201803161342510972763']";
    String ANYPICKER_HANDLE = "//div[@data-test-id='20160509045644070512696']";

    void addCriterion(final String p0, final String p1, final String p2, final String p3);

    void addCriterion(final String p0, final GlobalConstants.RecordType p1, final String p2, final String p3);

    void addCriterion(final String p0, final boolean p1);

    void addRow(final String p0);

    void addRow(final String p0, final int p1);

    void bootstrapRow();

    void setLogicStyle(final String p0);

    String getLogicStyle();

    List<List<String>> getTopLevelLeftFieldContents();

    List<List<String>> getRuleTypeLeftFieldContents(final String p0);

    enum LogicStyle {
        GROUP_ANDS("GROUP_ANDS", 0, "Group ANDs"),
        GROUP_ORS("GROUP_ORS", 1, "Group ORs"),
        USE_ADVANCED_LOGIC("USE_ADVANCED_LOGIC", 2, "Use advanced logic");

        private String type;

        LogicStyle(final String name, final int ordinal, final String type) {
            this.type = type;
        }

        public String getType() {
            return this.type;
        }
    }

    enum RecordType {
        CORRESPONDENCE("CORRESPONDENCE", 0, "Correspondence"),
        FEED_SOURCE("FEED_SOURCE", 1, "Feed source"),
        FLOW("FLOW", 2, "Flow"),
        FLOW_ACTION("FLOW_ACTION", 3, "Flow Action"),
        HARNESS("HARNESS", 4, " Harness (Page)"),
        NOTIFICATION("NOTIFICATION", 5, "Notification"),
        PARAGRAPH("PARAGRAPH", 6, "Paragraph"),
        PROPERTY("PROPERTY", 7, "Property"),
        SECTION("SECTION", 8, "Section"),
        SERVICE_LEVEL_AGREEMENT("SERVICE_LEVEL_AGREEMENT", 9, "Service Level Agreement"),
        STRATEGY("STRATEGY", 10, "Strategy"),
        WHEN("WHEN", 11, "When");

        private String type;

        RecordType(final String name, final int ordinal, final String type) {
            this.type = type;
        }

        public String getType() {
            return this.type;
        }
    }
}
