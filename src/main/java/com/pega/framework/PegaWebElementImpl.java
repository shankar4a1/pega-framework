

package com.pega.framework;

import com.pega.*;
import com.pega.framework.elmt.*;
import com.pega.ri.*;
import com.pega.util.*;
import io.appium.java_client.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.*;
import org.openqa.selenium.support.ui.*;
import org.slf4j.*;

import java.awt.Point;
import java.util.List;
import java.util.*;


public class PegaWebElementImpl extends InstanceImpl implements PegaWebElement {
    private static final Logger LOGGER;
    private static Set<String> uniqueReturnElements;
    private WebElement elmt;
    private String elmtId;
    private String domPointer;
    protected WebDriver driver;
    protected Actions actions;
    protected ScriptExecutor scriptExecutor;
    protected PegaWebDriver pegaDriver;
    private By by;
    private LinkedHashSet<By> framesSet;

    static {
        LOGGER = LoggerFactory.getLogger(PegaWebElement.class.getName());
        PegaWebElementImpl.uniqueReturnElements = new HashSet<String>();
    }

    public PegaWebElementImpl(final WebElement elmt) {
        this.elmt = elmt;
    }

    public PegaWebElementImpl(final String frameID, final TestEnvironment testEnv) {
        this.testEnv = testEnv;
    }

    public PegaWebElementImpl(final WebElement elmt, final String elmtId) {
        this.elmt = elmt;
        this.elmtId = elmtId;
    }

    @Override
    public String getDOMPointer() {
        return this.domPointer;
    }

    @Override
    public String getId() {
        return this.elmtId;
    }

    @Override
    public void _setDOMPointer(final String domPointer) {
        this.domPointer = domPointer;
    }

    @Override
    public PegaWebElement findElement(final By by) {
        PegaWebElementImpl.LOGGER.info("element identification by logic:" + by.toString());
        final WebElement newElmt = this.elmt.findElement(by);
        this.by = by;
        if (this.testEnv.getConfiguration().analyseDataTestId()) {
            this.ReturnElementsFromApp(by, newElmt);
        }
        final PegaWebElement pegaWebElmt = new PegaWebElementImpl(newElmt);
        pegaWebElmt._setEnvironment(this.testEnv, by, this.getParentDocument());
        ElementUtil.findElmtsWithDTI(pegaWebElmt);
        return pegaWebElmt;
    }

    protected String generateDOMPath(final String document, final By by) {
        String domPinter = null;
        final String byLoc = (by != null) ? by.toString() : null;
        if (byLoc != null && byLoc.contains(":")) {
            final int i = byLoc.indexOf(":");
            final String type = byLoc.substring(0, i);
            String locator = byLoc.substring(i + 1).trim();
            if (type.contains("id")) {
                domPinter = document + ".getElementById('" + locator + "')";
            } else if (type.contains("name")) {
                domPinter = document + ".getElementsByName('" + locator + "')[0]";
            } else if (type.toLowerCase().contains("selector")) {
                locator = locator.replace("'", "\\'");
                domPinter = document + ".querySelector('" + locator + "')";
            } else if (type.contains("xpath")) {
                domPinter = document + ".evaluate(\"" + locator + "\"," + document + ".body, null, XPathResult.ANY_TYPE, null).iterateNext()";
            } else if (type.contains("linkText")) {
                domPinter = document + ".evaluate(\"//a[text()='" + locator + "']\"," + document + ".body, null, XPathResult.ANY_TYPE, null).iterateNext()";
            } else if (type.contains("partialLinkText")) {
                domPinter = document + ".evaluate(\"//a[contains(text(), '" + locator + "')]\"," + document + ".body, null, XPathResult.ANY_TYPE, null).iterateNext()";
            } else if (type.contains("className")) {
                domPinter = document + ".getElementsByClassName('" + locator + "')[0]";
            } else if (type.contains("tagName")) {
                domPinter = document + ".getElementsByTagName('" + locator + "')[0]";
            }
        }
        return domPinter;
    }

    @Override
    public String getTagName() {
        return this.elmt.getTagName();
    }

    @Override
    public void moveMouseToThis() {
        this.moveMouseToThis(0, 0);
    }

    @Override
    public void moveMouseToThis(final int x, final int y) {
        PegaWebElementImpl.LOGGER.debug("Moving mouse to " + x + "," + y);
        final int height = this.getElementHeight();
        final int width = this.getElementWidth();
        final int top = this.getElementTop();
        final int left = this.getElementLeft();
        this.testEnv.getMouse().moveTo(left + width / 2 + x, top + height / 2 + y);
        this.testEnv.getPegaDriver().switchToActiveFrame(this.framesSet);
    }

    @Override
    public void doClickWithMouse() {
        this.moveMouseToThis();
        this.testEnv.getMouse().click();
    }

    @Override
    public void doDoubleClickWithMouse() {
        this.moveMouseToThis();
        this.testEnv.getPegaDriver().handleWaits().sleep(2L);
        this.testEnv.getMouse().doubleClick();
    }

    @Override
    public void doRightClickWithMouse() {
        this.moveMouseToThis();
        this.testEnv.getMouse().rightClick();
    }

    @Override
    public void doRightClickWithMouse(final Point anchorPoint) {
        this.moveMouseToThis(anchorPoint.x, anchorPoint.y);
        this.testEnv.getMouse().rightClick();
    }

    @Override
    public void doDoubleClickWithMouse(final Point anchorPoint) {
        this.moveMouseToThis(anchorPoint.x, anchorPoint.y);
        if (this.testEnv.getConfiguration().getPlatformDetails().isWindows()) {
            if (this.testEnv.getConfiguration().getPlatformDetails().isWindows()) {
                this.testEnv.getMouse().doubleClick();
            }
        } else if (this.testEnv.getConfiguration().getPlatformDetails().isLinux()) {
            this.actions.doubleClick(this.elmt).build().perform();
        }
    }

    @Override
    public void doClickWithMouse(final Point anchorPoint) {
        this.moveMouseToThis(anchorPoint.x, anchorPoint.y);
        this.testEnv.getMouse().click();
    }

    @Override
    public void rightClick() {
        this.actions.contextClick(this.elmt).build().perform();
    }

    @Override
    public String getAttribute(final String sAttrName) {
        return this.elmt.getAttribute(sAttrName);
    }

    @Override
    public void scrollIntoView() {
        if (this.testEnv.getConfiguration().getPlatformDetails().isLinux()) {
            this.driver.switchTo().defaultContent();
            final String script = "var elmt=" + this.getDOMPointer() + ";elmt.scrollIntoView();";
            PegaWebElementImpl.LOGGER.debug("scrollIntoView::" + script);
            this.scriptExecutor.executeJavaScript(script);
            this.testEnv.getPegaDriver().switchToActiveFrame(this.framesSet);
        } else {
            ((JavascriptExecutor) this.pegaDriver.getDriver()).executeScript("arguments[0].scrollIntoView();", this.getWebElement());
        }
    }

    @Override
    public void doubleClick() {
        this.doubleClick(true);
    }

    @Override
    public void doubleClick(final boolean wait) {
        this.actions.doubleClick(this.elmt).build().perform();
        this.testEnv.getPegaDriver().waitForDocStateReady();
        this.generatePerformanceMetrics();
        if (wait) {
            this.testEnv.getPegaDriver().waitForDocStateReady();
            this.testEnv.getPegaDriver().switchToActiveFrame(this.framesSet);
        }
    }

    @Override
    public void blur() {
        this.driver.switchTo().defaultContent();
        final String script = "var elmt=" + this.getDOMPointer() + ";elmt.blur();";
        PegaWebElementImpl.LOGGER.debug("Blur::" + script);
        this.scriptExecutor.executeJavaScript(script);
        this.testEnv.getPegaDriver().switchToActiveFrame(this.framesSet);
    }

    @Override
    public void dragAndDrop(final PegaWebElement targetElmt) {
        this.dragAndDrop(targetElmt, 0, 0);
    }

    @Override
    public void dragAndDrop(final PegaWebElement targetElmt, final int xEnd, final int yEnd) {
        this.dragAndDrop(targetElmt, xEnd, yEnd, false);
    }

    @Override
    public void dragAndDrop(final PegaWebElement targetElmt, final int xEnd, final int yEnd, final boolean beside) {
        this.dragAndDrop(targetElmt, 0, 0, xEnd, yEnd, beside);
    }

    @Override
    public void dragAndDrop(final PegaWebElement targetElmt, final int xStart, final int yStart, final int xEnd, final int yEnd, final boolean beside) {
        int targetHeight = 0;
        int targetWidth = 0;
        int targetTop = 0;
        int targetLeft = 0;
        if (targetElmt != null) {
            targetHeight = targetElmt.getElementHeight();
            targetWidth = targetElmt.getElementWidth();
            targetTop = targetElmt.getElementTop();
            targetLeft = targetElmt.getElementLeft();
        }
        final Mouse mouse = this.testEnv.getMouse();
        this.moveMouseToThis(xStart, yStart);
        this.testEnv.getPegaDriver().handleWaits().sleep(2L);
        mouse.pressLeftButton();
        mouse.moveRelative(45, 0);
        this.testEnv.getPegaDriver().handleWaits().sleep(3L);
        if (beside) {
            mouse.moveTo(targetLeft + targetWidth + xEnd, targetTop + targetHeight + yEnd);
        } else {
            mouse.moveTo(targetLeft + targetWidth / 3 + xEnd, targetTop + targetHeight / 3 + yEnd);
        }
        this.testEnv.getPegaDriver().handleWaits().sleep(3L);
        mouse.releaseLeftButton();
        this.testEnv.getPegaDriver().handleWaits().sleep(3L);
        mouse.moveTo(800, 20);
        this.testEnv.getPegaDriver().handleWaits().sleep(1L);
        this.testEnv.getPegaDriver().waitForDocStateReady();
        if (targetElmt != null) {
            this.testEnv.getPegaDriver().switchToActiveFrame(targetElmt.getFramesSet());
        }
    }

    @Override
    public void dragAndDrop(final int xEnd, final int yEnd) {
        this.dragAndDrop(xEnd, yEnd, true);
    }

    @Override
    public void dragAndDrop(final int xEnd, final int yEnd, final boolean relativeTo) {
        if (relativeTo) {
            this.dragAndDrop(this, xEnd, yEnd);
        } else {
            this.dragAndDrop(null, xEnd, yEnd);
        }
    }

    @Override
    public boolean isReadOnly() {
        return this.elmt.isEnabled();
    }

    @Override
    public int getElementLeft() {
        this.driver.switchTo().defaultContent();
        final String script = "var elmt=" + this.getDOMPointer() + ";return getAbsoluteElementOffsetLeft(elmt);";
        PegaWebElementImpl.LOGGER.debug("getElementLeft::" + script);
        final long left;
        left = (long) this.scriptExecutor.executeJavaScript(script);
        // left = (long)convertToLong(this.scriptExecutor.executeJavaScript(script));
        return (int) left;
    }

    //altered by SG
    public Long convertToLong(Object o) {

        String stringToConvert = String.valueOf(o);
        Long convertedLong = Long.parseLong(stringToConvert);

        return convertedLong;

    }

    @Override
    public int getElementTop() {
        this.driver.switchTo().defaultContent();
        final String script = "var elmt=" + this.getDOMPointer() + ";return getAbsoluteElementOffsetTop(elmt);";
        PegaWebElementImpl.LOGGER.debug("getElementTop::" + script);
        final long top = (long) this.scriptExecutor.executeJavaScript(script);
        return (int) top;
    }

    @Override
    public int getElementWidth() {
        this.driver.switchTo().defaultContent();
        final String script = "var elmt=" + this.getDOMPointer() + ";return Math.round(elmt.getBoundingClientRect().width);";
        PegaWebElementImpl.LOGGER.debug("getElementWidth::" + script);
        final long top = (long) this.scriptExecutor.executeJavaScript(script);
        return (int) top;
    }

    @Override
    public int getElementHeight() {
        this.driver.switchTo().defaultContent();
        final String script = "var elmt=" + this.getDOMPointer() + ";return Math.round(elmt.getBoundingClientRect().height);";
        PegaWebElementImpl.LOGGER.debug("getElementHeight::" + script);
        final long top = (long) this.scriptExecutor.executeJavaScript(script);
        return (int) top;
    }

    @Override
    public void check() {
        this.elmt.click();
    }

    @Override
    public void _initialize(final TestEnvironment testEnv, final By by) {
        if (this.driver == null) {
            this.testEnv = testEnv;
            this.pegaDriver = testEnv.getPegaDriver();
            this.driver = testEnv.getPegaDriver().getDriver();
            this.actions = new Actions(this.driver);
            this.scriptExecutor = testEnv.getScriptExecutor();
            this.by = by;
        }
    }

    @Override
    public void sendKeys(final CharSequence... keysToSend) {
        this.elmt.sendKeys(keysToSend);
    }

    @Override
    public void click() {
        this.click(true);
    }

    @Override
    public void click(final CheckLocalized aCheckLocalized) {
        this.click(true, aCheckLocalized);
    }

    @Override
    public void clear() {
        this.elmt.clear();
    }

    @Override
    public WebElement getWebElement() {
        return this.elmt;
    }

    public void submit() {
        this.elmt.submit();
    }

    public boolean isSelected() {
        return this.elmt.isSelected();
    }

    public String getText() {
        return this.elmt.getText();
    }

    public List<WebElement> findElements(final By by) {
        final WebElement elmt1 = this.elmt.findElement(by);
        if (this.testEnv.getConfiguration().analyseDataTestId()) {
            this.ReturnElementsFromApp(by, elmt1);
        }
        return this.elmt.findElements(by);
    }

    public boolean isDisplayed() {
        return this.elmt.isDisplayed();
    }

    @Override
    public boolean isEnabled() {
        return this.elmt.isEnabled();
    }

    public org.openqa.selenium.Point getLocation() {
        return this.elmt.getLocation();
    }

    public Dimension getSize() {
        return this.elmt.getSize();
    }

    public String getCssValue(final String propertyName) {
        return this.elmt.getCssValue(propertyName);
    }

    @Override
    public boolean isVisible() {
        final WebDriverWait wait = new WebDriverWait(this.driver, 1L);
        try {
            // wait.until((Function)ExpectedConditions.visibilityOf(this.elmt));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public By getBy() {
        return this.by;
    }

    @Override
    public void focus() {
        this.driver.switchTo().defaultContent();
        final String script = "var elmt=" + this.getDOMPointer() + ";elmt.focus();";
        PegaWebElementImpl.LOGGER.debug(script);
        this.scriptExecutor.executeJavaScript(script);
        this.testEnv.getPegaDriver().switchToActiveFrame(this.framesSet);
    }

    @Override
    public void click(final boolean wait) {
        this.elmt.click();
        if (this.testEnv.getConfiguration().isCaptureClientPerformanceMetrics() && !this.testEnv.getPegaDriver().getTitle().contains("Pega Platform")) {
            this.generatePerformanceMetrics();
        }
        if (wait) {
            final long startTime = Calendar.getInstance().getTimeInMillis();
            this.testEnv.getPegaDriver().waitForDocStateReady(3);
            this.testEnv.getPegaDriver().handleWaits().waitForNoThrobber();
            this.testEnv.getPegaDriver().switchToActiveFrame(this.framesSet);
            final long endTime = Calendar.getInstance().getTimeInMillis();
            PegaWebElementImpl.LOGGER.debug("Time taken for wait after click on element<" + this.getDOMPointer() + "> is: " + (endTime - startTime) + " ms");
        }
    }

    @Override
    public void click(final boolean wait, final CheckLocalized aCheckLocalized) {
        this.elmt.click();
        if (wait) {
            final long startTime = Calendar.getInstance().getTimeInMillis();
            this.testEnv.getPegaDriver().waitForDocStateReady(3);
            this.testEnv.getPegaDriver().handleWaits().waitForNoThrobber();
            this.testEnv.getPegaDriver().switchToActiveFrame(this.framesSet);
            final long endTime = Calendar.getInstance().getTimeInMillis();
            PegaWebElementImpl.LOGGER.debug("Time taken for wait after click on element<" + this.getDOMPointer() + "> is: " + (endTime - startTime) + " ms");
        }
        if (aCheckLocalized == CheckLocalized.TRUE && !this.testEnv.getConfiguration().getL10NConfig().getL10NLanguage().equals("EN")) {
            LocalizationUtil.verifyNonLocalizedWords(this.pegaDriver);
        }
    }

    @Override
    public void _setEnvironment(final TestEnvironment testEnv, final By by, final String document) {
        this._setEnvironment(testEnv, by, document, null);
    }

    @Override
    public void _setEnvironment(final TestEnvironment testEnv, final By by, final String document, final LinkedHashSet<By> frameSet) {
        final String dom = this.generateDOMPath(document, by);
        this._initialize(testEnv, by);
        this._setDOMPointer(dom);
        this._setFrames(frameSet);
    }

    private void _setFrames(final LinkedHashSet<By> frameSet) {
        this.framesSet = frameSet;
    }

    @Override
    public void _setEnvironment(final TestEnvironment testEnv, final String frameId) {
        this._initialize(testEnv, By.id(frameId));
        if (!frameId.equals("default")) {
            this._setDOMPointer("window.frames['" + frameId + "']");
        }
    }

    @Override
    public void _setInnerFrameEnv(final TestEnvironment testEnv, final String frameId, final String frameDOM) {
        this._initialize(testEnv, By.id(frameId));
        this._setDOMPointer(frameDOM);
    }

    @Override
    public DropDown findSelectBox(final By by) {
        this.pegaDriver.handleWaits().waitForElementPresence(by);
        final WebElement elmt = this.findElement(by);
        if (this.testEnv.getConfiguration().analyseDataTestId()) {
            this.ReturnElementsFromApp(by, elmt);
        }
        final DropDown dropdown = new DropDownImpl(elmt);
        dropdown._setEnvironment(this.testEnv, by, this.getParentDocument());
        PegaWebElementImpl.LOGGER.debug("DOM Pointer for element identified by: \"" + by + "\" is : " + dropdown.getDOMPointer());
        return dropdown;
    }

    @Override
    public void mouseOver() {
        this.actions.moveToElement(this.getWebElement()).build().perform();
    }

    @Override
    public String getParentDocument() {
        final String dom = this.getDOMPointer();
        final int i = dom.lastIndexOf(".");
        return dom.substring(0, i);
    }

    @Override
    public AutoComplete findAutoComplete(final By by) {
        this.pegaDriver.handleWaits().waitForElementPresence(by);
        final WebElement elmt = this.findElement(by);
        if (this.testEnv.getConfiguration().analyseDataTestId()) {
            this.ReturnElementsFromApp(by, elmt);
        }
        final AutoComplete autoComplete = new AutoCompleteImpl(elmt);
        autoComplete._setEnvironment(this.testEnv, by, this.getParentDocument());
        PegaWebElementImpl.LOGGER.debug("DOM Pointer for element identified by: \"" + by + "\" is : " + autoComplete.getDOMPointer());
        return autoComplete;
    }

    public <X> X getScreenshotAs(final OutputType<X> arg0) throws WebDriverException {
        return (X) this.elmt.getScreenshotAs((OutputType) arg0);
    }

    @Override
    public boolean verifyElement(final By by) {
        boolean isElmtFound = true;
        try {
            PegaWebElementImpl.LOGGER.info(by.toString());
            this.elmt.findElement(by);
        } catch (NoSuchElementException e) {
            isElmtFound = false;
        }
        return isElmtFound;
    }

    public Rectangle getRect() {
        return this.elmt.getRect();
    }

    @Override
    public MobileElement getMobileElement() {
        return (MobileElement) this.elmt;
    }

    public void ReturnElementsFromApp(final By by, final WebElement element) {
        final String ElementName = by.toString();
        final String[] by2 = ElementName.split(":");
        String searchElement = null;
        String instring = "";
        if (by2.length < 3) {
            for (int i = 1; i < by2.length; ++i) {
                try {
                    instring = instring + by2[i];
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (int i = 1; i < by2.length; ++i) {
                try {
                    if (i < by2.length - 1) {
                        instring = instring + by2[i] + ":";
                    } else {
                        instring = instring + by2[i];
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        }
        searchElement = by2[0] + "(\"" + instring.trim() + "\")";
        PegaWebElementImpl.LOGGER.info("element identification by logic in Find Element pega web Element impl:" + searchElement);
        PegaWebElementImpl.LOGGER.debug("before if condition in find element in pwga web Element impl condition value" + by.toString().contains("data-test-id") + "by.tostring value: " + by.toString());
        if (by.toString() != null && !by.toString().contains("data-test-id")) {
            PegaWebElementImpl.LOGGER.debug("Entered If condition in the find element method:" + by);
            if (element.getAttribute("data-test-id") == null) {
                PegaWebElementImpl.uniqueReturnElements.add(searchElement + "~'" + element.getAttribute("data-test-id") + "'~0~" + element.getTagName() + "~N");
            } else if (this.findElements(By.xpath("//" + element.getTagName() + "[@data-test-id = '" + element.getAttribute("data-test-id") + "']")).size() > 1) {
                PegaWebElementImpl.uniqueReturnElements.add(searchElement + "~'" + element.getAttribute("data-test-id") + "'~" + this.findElements(By.xpath("//" + element.getTagName() + "[@data-test-id = '" + element.getAttribute("data-test-id") + "']")).size() + "~" + element.getTagName() + "~N");
            } else {
                PegaWebElementImpl.uniqueReturnElements.add(searchElement + "~'" + element.getAttribute("data-test-id") + "'~" + this.findElements(By.xpath("//" + element.getTagName() + "[@data-test-id = '" + element.getAttribute("data-test-id") + "']")).size() + "~" + element.getTagName() + "~N");
            }
        } else {
            PegaWebElementImpl.uniqueReturnElements.add(searchElement + "~'dataTestIdUsed'~datatestIdused~" + element.getTagName() + "~Y");
        }
    }

    public static Set<String> getElements() {
        return PegaWebElementImpl.uniqueReturnElements;
    }

    public static void setElementsEmpty() {
        if (!PegaWebElementImpl.uniqueReturnElements.isEmpty()) {
            PegaWebElementImpl.uniqueReturnElements.clear();
        }
    }

    @Override
    public String getStyle(final String styleProp) {
        return (String) this.scriptExecutor.executeJavaScript("window.getComputedStyle(" + this.getDOMPointer() + " ,null).getPropertyValue('" + styleProp + "')");
    }

    @Override
    public LinkedHashSet<By> getFramesSet() {
        return this.framesSet;
    }

    private void generatePerformanceMetrics() {
        if (this.testEnv.getConfiguration().isCaptureClientPerformanceMetrics()) {
            final String elementIdentification = this.getBy().toString();
            // final StepDefinitionMatch match = ThreadLocalStepDefinitionMatch.get();
            ///  final String stepName = match.getStepName();
            //  this.testEnv.getClientPerformance().captureMetrics(true, elementIdentification, stepName);
        }
    }


}
