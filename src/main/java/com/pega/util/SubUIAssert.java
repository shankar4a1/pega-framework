

package com.pega.util;

import com.pega.exceptions.*;

public class SubUIAssert {


    public SubUIAssert() {

    }

    public static void assertRuleFormErrorsNotPresent(final String responseContent) {
        if (responseContent.contains("'ruleName':'pzErrorInfo'") || responseContent.contains("pxMessageSummary.pxErrors")) {
            throw new SubUIAssertException("There are error messages on the output response");
        }
    }
}
