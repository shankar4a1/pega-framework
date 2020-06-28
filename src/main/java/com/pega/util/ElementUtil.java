

package com.pega.util;

import com.pega.framework.*;
import org.slf4j.*;

public class ElementUtil {
    private static final Logger LOGGER;

    static {
        LOGGER = LoggerFactory.getLogger(ElementUtil.class.getName());
    }

    public static void findElmtsWithDTI(final PegaWebElement elmt) {
        ElementUtil.LOGGER.debug("Element's DOM pinter: " + elmt.getDOMPointer());
        ElementUtil.LOGGER.debug("ELEMENT_TYPE::" + elmt.getTagName());
        if (!elmt.getTestEnvironment().getConfiguration().getMobileConfig().isMobileExecution()) {
            final String val = elmt.getAttribute("data-test-id");
            if (val != null) {
                if (val.matches("\\d+")) {
                    ElementUtil.LOGGER.debug("Numeric_Data_Test_ID::" + val);
                } else {
                    ElementUtil.LOGGER.debug("Meaningful_Data_Test_ID::" + val);
                }
            } else {
                ElementUtil.LOGGER.debug("No_Data_Test_ID::" + val);
            }
        }
    }
}
