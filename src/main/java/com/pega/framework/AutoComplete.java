

package com.pega.framework;

public interface AutoComplete extends PegaWebElement {
    String VERSION = "$Id: AutoComplete.java 209062 2016-09-22 17:27:22Z SachinVellanki $";

    void setValue(final String p0);

    void setValue(final String p0, final boolean p1);

    void selectValue(final String p0);

    void clear();
}
