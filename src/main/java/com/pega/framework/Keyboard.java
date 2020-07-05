

package com.pega.framework;

import org.openqa.selenium.*;

public interface Keyboard {


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
