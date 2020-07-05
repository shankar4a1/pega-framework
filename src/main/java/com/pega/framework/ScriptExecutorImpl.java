

package com.pega.framework;

import com.pega.*;
import org.openqa.selenium.*;
import org.slf4j.*;

public class ScriptExecutorImpl implements ScriptExecutor {

    private static final Logger LOGGER;
    private WebDriver driver;
    private JavascriptExecutor jsExecutor;
    private TestEnvironment testEnv;

    static {
        LOGGER = LoggerFactory.getLogger(ScriptExecutorImpl.class.getName());
    }

    public ScriptExecutorImpl(final TestEnvironment testEnv) {
        this.testEnv = testEnv;
        this.driver = testEnv.getPegaDriver().getDriver();
        this.jsExecutor = (JavascriptExecutor) this.driver;
    }

    @Override
    public void clear(final String identifier) {
        final String locator = identifier.substring(identifier.indexOf("=") + 1);
        if (identifier.startsWith("xpath")) {
            final String script_fireClickEvent = "document.evaluate(\"" + locator + "\", document.body, null, XPathResult.ANY_TYPE, null).iterateNext().value=\"\";";
            this.jsExecutor.executeScript(script_fireClickEvent);
        } else if (identifier.startsWith("id")) {
            final String script_fireClickEvent = "document.getElementById('" + locator + "').value=\"\";";
            this.jsExecutor.executeScript(script_fireClickEvent);
        }
    }

    @Override
    public void click(final String identifier) {
        if (identifier.startsWith("xpath")) {
            final String locator = identifier.substring(identifier.indexOf("=") + 1);
            final String script_fireClickEvent = "document.evaluate(\"" + locator + "\", document.body, null, XPathResult.ANY_TYPE, null).iterateNext().click();";
            this.jsExecutor.executeScript(script_fireClickEvent);
        }
    }

    @Override
    public Object executeJavaScript(final String script) {
        this.driver.switchTo().defaultContent();
        ScriptExecutorImpl.LOGGER.debug(this.getClass().getName() + "::executeJavaScript::" + script);
        return this.jsExecutor.executeScript(script);
    }

    @Override
    public void mouseOver(final PegaWebElement elmt) {
        this.driver.switchTo().defaultContent();
        final String script = "var elem=" + elmt.getDOMPointer() + ";" + "var evt = elem.ownerDocument.createEvent('MouseEvents');" + "evt.initMouseEvent('mouseover',true,true,elem.ownerDocument.defaultView,0,0,0,0,0,false,false,false,false,0,null);" + "elem.dispatchEvent(evt);";
        ScriptExecutorImpl.LOGGER.debug(this.getClass().getName() + "::mouseOver::" + script);
        this.jsExecutor.executeScript(script);
        this.testEnv.getPegaDriver().switchToActiveFrame(elmt.getFramesSet());
    }

    @Override
    public void clear(final PegaWebElement elmt) {
        this.driver.switchTo().defaultContent();
        final String script = "var elem=" + elmt.getDOMPointer() + ";elem.value=\"\";";
        ScriptExecutorImpl.LOGGER.debug(this.getClass().getName() + "::clear::" + script);
        this.jsExecutor.executeScript(script);
        this.testEnv.getPegaDriver().switchToActiveFrame(elmt.getFramesSet());
    }

    @Override
    public void click(final PegaWebElement elmt) {
        this.click(elmt, true);
    }

    @Override
    public void click(final PegaWebElement elmt, final boolean waitForDocStateReady) {
        this.driver.switchTo().defaultContent();
        final String script = "var elem=" + elmt.getDOMPointer() + ";elem.click();";
        ScriptExecutorImpl.LOGGER.debug(this.getClass().getName() + "::click::" + script);
        this.jsExecutor.executeScript(script);
        if (waitForDocStateReady) {
            this.testEnv.getPegaDriver().waitForDocStateReady();
        }
    }

    @Override
    public void click(final WebElement elmt) {
        this.jsExecutor.executeScript("arguments[0].click()", elmt);
    }

    @Override
    public void sendKeys(final PegaWebElement elmt, final String text) {
        this.sendKeys(elmt, text, null);
    }

    @Override
    public void sendKeys(final PegaWebElement elmt, String text, final String event) {
        this.driver.switchTo().defaultContent();
        text = text.replace("'", "'");
        final String script = "var elem=" + elmt.getDOMPointer() + ";elem.value='" + text + "';";
        ScriptExecutorImpl.LOGGER.debug(this.getClass().getName() + "::sendkeys::" + script);
        this.jsExecutor.executeScript(script);
        if (event != null) {
            this.fireKeyboardEvent(elmt, event);
        }
        this.testEnv.getPegaDriver().switchToActiveFrame(elmt.getFramesSet());
    }

    @Override
    public void fireKeyboardEvent(final PegaWebElement elmt, final String event) {
        this.driver.switchTo().defaultContent();
        final String script = "var elem=" + elmt.getDOMPointer() + ";" + "var evt = elem.ownerDocument.createEvent('KeyboardEvent');" + "evt.initKeyboardEvent('" + event + "', true, true, null, false, false, false, false, 0, 0);" + "elem.dispatchEvent(evt);";
        ScriptExecutorImpl.LOGGER.debug(this.getClass().getName() + "::mouseOver::" + script);
        this.jsExecutor.executeScript(script);
        this.testEnv.getPegaDriver().switchToActiveFrame(elmt.getFramesSet());
    }

    @Override
    public void rightClick(final PegaWebElement elmt) {
        this.driver.switchTo().defaultContent();
        final String script = "var elem=" + elmt.getDOMPointer() + ";" + "var evt = elem.ownerDocument.createEvent('MouseEvents');" + "evt.initMouseEvent('click', true, false, window,0,0,0,0,0,false,false,false,false,2,null);" + "elem.dispatchEvent(evt);";
        ScriptExecutorImpl.LOGGER.debug(this.getClass().getName() + "::mouseOver::" + script);
        this.jsExecutor.executeScript(script);
        this.testEnv.getPegaDriver().switchToActiveFrame(elmt.getFramesSet());
    }

    @Override
    public String getInnerText(final WebDriver driver, final WebElement webElement) {
        final String script = "return arguments[0].innerText || arguments[0].textContent";
        final JavascriptExecutor js = (JavascriptExecutor) driver;
        return (String) js.executeScript(script, new Object[]{webElement});
    }

    @Override
    public String getInnerText(final PegaWebElement webElement) {
        final String script = "return arguments[0].innerText || arguments[0].textContent";
        final JavascriptExecutor js = (JavascriptExecutor) webElement.getTestEnvironment().getPegaDriver().getDriver();
        return (String) js.executeScript(script, new Object[]{webElement.getWebElement()});
    }
}
