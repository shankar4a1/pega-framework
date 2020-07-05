

package com.pega.framework;

public interface AutoComplete extends PegaWebElement {


    void setValue(final String p0);

    void setValue(final String p0, final boolean p1);

    void selectValue(final String p0);

    void clear();
}
