

package com.pega.util;

import com.pega.exceptions.*;

public class SubUIAssert {
    String COPYRIGHT;
    private static final String VERSION = "$Id: SubUIAssert.java 208110 2016-08-25 09:21:14Z BalanaveenreddyKappeta $";

    public SubUIAssert() {
        this.COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    }

    public static void assertRuleFormErrorsNotPresent(final String responseContent) {
        if (responseContent.contains("'ruleName':'pzErrorInfo'") || responseContent.contains("pxMessageSummary.pxErrors")) {
            throw new SubUIAssertException("There are error messages on the output response");
        }
    }
}
