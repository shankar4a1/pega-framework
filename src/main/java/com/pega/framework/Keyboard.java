

package com.pega.framework;

import org.openqa.selenium.*;

public interface Keyboard {
    String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    String VERSION = "$Id: Keyboard.java 125139 2015-02-22 15:23:22Z SachinVellanki $";

    void keyPress(final Keys p0);

    void keyRelease(final Keys p0);

    void keyPressAndRelease(final Keys p0);

    void sendKeys(final Keys p0);

    void typeString(final String p0);

    void shiftPress(final Keys p0);

    void ctrlPress(final Keys p0);

    void ctrlPress(final String p0);

    void shiftPress(final String p0);

    void ctrlKeyDown();

    void ctrlKeyUp();

    void shiftKeyDown();

    void shiftKeyUp();
}
