

package com.pega.framework;

import java.util.*;

public interface AnyPicker extends PegaWebElement {
    void selectValue(final String p0, final String p1);

    void selectValue(final String p0);

    List<List<String>> getTopLevelContents();

    List<List<String>> getRuleTypeContents(final String p0);

    void deleteValue(final String p0);
}
