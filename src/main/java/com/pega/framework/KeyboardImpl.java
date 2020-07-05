

package com.pega.framework;

import com.pega.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.*;

public class KeyboardImpl implements Keyboard {

    private Actions actions;
    private WebDriver driver;
    private org.openqa.selenium.interactions.Keyboard keyboard;

    public KeyboardImpl(final TestEnvironment testEnv) {
        this.driver = testEnv.getPegaDriver().getDriver();
        this.keyboard = ((HasInputDevices) this.driver).getKeyboard();
        this.actions = testEnv.getDriverActions();
    }

    @Override
    public void keyPress(final Keys key) {
        this.keyboard.pressKey(key);
    }

    @Override
    public void keyRelease(final Keys key) {
        this.keyboard.releaseKey(key);
    }

    @Override
    public void keyPressAndRelease(final Keys key) {
        this.keyPress(key);
        this.keyRelease(key);
    }

    @Override
    public void sendKeys(final Keys key) {
        this.keyboard.sendKeys(key);
    }

    @Override
    public void typeString(final String text) {
        this.actions.sendKeys(text).build().perform();
    }

    @Override
    public void shiftPress(final Keys key) {
        this.actions.keyDown(Keys.SHIFT).build().perform();
        this.keyPressAndRelease(key);
        this.actions.keyUp(Keys.SHIFT).build().perform();
    }

    @Override
    public void ctrlPress(final Keys key) {
        this.ctrlKeyDown();
        this.keyPressAndRelease(key);
        this.ctrlKeyUp();
    }

    @Override
    public void ctrlKeyDown() {
        this.actions.keyDown(Keys.CONTROL).build().perform();
    }

    @Override
    public void ctrlKeyUp() {
        this.actions.keyUp(Keys.CONTROL).build().perform();
    }

    @Override
    public void shiftKeyDown() {
        this.actions.keyDown(Keys.SHIFT).build().perform();
    }

    @Override
    public void shiftKeyUp() {
        this.actions.keyUp(Keys.SHIFT).build().perform();
    }

    @Override
    public void ctrlPress(final String s) {
        this.actions.keyDown(Keys.CONTROL).sendKeys(s).keyUp(Keys.CONTROL).build().perform();
    }

    @Override
    public void shiftPress(final String s) {
        this.actions.keyDown(Keys.SHIFT).sendKeys(s).keyUp(Keys.SHIFT).build().perform();
    }
}
