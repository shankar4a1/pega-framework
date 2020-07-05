

package com.pega.framework.elmt;

import com.pega.*;
import com.pega.framework.*;
import com.pega.objectrepo.*;
import com.pega.util.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.*;
import org.openqa.selenium.support.ui.*;
import org.slf4j.*;

import java.util.*;
import java.util.function.*;

public class FrameImpl implements Frame {

    private static final Logger LOGGER;
    private String frameDoc;
    private WebDriver driver;
    protected PegaWebDriver pegaDriver;
    private By by;
    private String domPointer;
    private static Set<String> uniqueReturnElements;
    protected Actions actions;
    protected ScriptExecutor scriptExecutor;
    protected TestEnvironment testEnv;
    private WebElement elmt;
    private String elmtId;
    private Frame frame;
    private LinkedHashSet<By> frames;

    static {
        LOGGER = LoggerFactory.getLogger(Frame.class.getName());
        FrameImpl.uniqueReturnElements = new HashSet<String>();
    }

    @Override
    public LinkedHashSet<By> getFramesSet() {
        return this.frames;
    }

    @Override
    public TestEnvironment getTestEnvironment() {
        return this.testEnv;
    }

    public FrameImpl(final PegaWebElement element) {
        this.frames = null;
        this.testEnv = element.getTestEnvironment();
        this.elmt = element;
        this._setEnvironment(this.testEnv, element);
    }

    public FrameImpl(final String frameID, final TestEnvironment testEnv) {
        this.frames = null;
        this._setEnvironment(this.testEnv = testEnv, frameID);
    }

    public FrameImpl(final Frame frame) {
        this.frames = null;
        this.testEnv = frame.getTestEnvironment();
        this.pegaDriver = this.testEnv.getPegaDriver();
        this.driver = this.testEnv.getPegaDriver().getDriver();
        this.actions = new Actions(this.driver);
        this.scriptExecutor = this.testEnv.getScriptExecutor();
        this._setDOMPointer(frame.getDOMPointer());
        this.frames = frame.getFramesSet();
    }

    @Override
    public PegaWebElement findElement(final By by) {
        return this.findElement(by, true);
    }

    @Override
    public PegaWebElement findElement(final String xpathValue, final String valueToInsert) {
        By.xpath(String.format(xpathValue, valueToInsert));
        return this.findElement(By.xpath(String.format(xpathValue, valueToInsert)), true);
    }

    @Override
    public String getDOMPointer() {
        return this.domPointer;
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
    public PegaWebElement findElement(final By by, final boolean wait) {
        FrameImpl.LOGGER.debug("driver current location:" + this.driver.getCurrentUrl());
        FrameImpl.LOGGER.debug("Element Dom Pointer: " + this.getDOMPointer());
        FrameImpl.LOGGER.info("element identification by logic:" + by.toString());
        this.pegaDriver.handleWaits().verifyAndWaitIfThrobberPresent();
        this.switchToCurrentFrame();
        if (wait) {
            this.testEnv.getPegaDriver().handleWaits().waitForElementPresence(by);
        }
        final WebElement elmt = this.driver.findElement(by);
        if (this.testEnv.getConfiguration().analyseDataTestId()) {
            this.ReturnElementsFromApp(by, elmt);
        }
        final PegaWebElement pegaWebElmt = new PegaWebElementImpl(elmt);
        pegaWebElmt._setEnvironment(this.testEnv, by, this.getFrameDocument(), this.frames);
        if (this.testEnv.getConfiguration().analyseDataTestId()) {
            ElementUtil.findElmtsWithDTI(pegaWebElmt);
        }
        return pegaWebElmt;
    }

    private void switchToCurrentFrame() {
        if (GlobalConstants.getCURRENT_FRAME_BY() == null || GlobalConstants.getCURRENT_FRAME_BY() != this.by) {
            this.pegaDriver.switchTo().defaultContent();
            //altered by SG
            this.pegaDriver.switchTo().frame("FormFactoriFrame");
            for (final By frame : this.frames) {
                final WebElement element = this.pegaDriver.getDriver().findElement(frame);
                this.pegaDriver.switchTo().frame(element);
            }
            GlobalConstants.setCURRENT_FRAME_BY(this.by);
        }
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
        FrameImpl.LOGGER.info("element identification by logic in Find Element pega web Element impl:" + searchElement);
        FrameImpl.LOGGER.debug("before if condition in find element in pwga web Element impl condition value" + by.toString().contains("data-test-id") + "by.tostring value: " + by.toString());
        if (by.toString() != null && !by.toString().contains("data-test-id")) {
            FrameImpl.LOGGER.debug("Entered If condition in the find element method:" + by);
            if (element.getAttribute("data-test-id") == null) {
                FrameImpl.uniqueReturnElements.add(searchElement + "~'" + element.getAttribute("data-test-id") + "'~0~" + element.getTagName() + "~N");
            } else if (this.findElements(By.xpath("//" + element.getTagName() + "[@data-test-id = '" + element.getAttribute("data-test-id") + "']")).size() > 1) {
                FrameImpl.uniqueReturnElements.add(searchElement + "~'" + element.getAttribute("data-test-id") + "'~" + this.findElements(By.xpath("//" + element.getTagName() + "[@data-test-id = '" + element.getAttribute("data-test-id") + "']")).size() + "~" + element.getTagName() + "~N");
            } else {
                FrameImpl.uniqueReturnElements.add(searchElement + "~'" + element.getAttribute("data-test-id") + "'~" + this.findElements(By.xpath("//" + element.getTagName() + "[@data-test-id = '" + element.getAttribute("data-test-id") + "']")).size() + "~" + element.getTagName() + "~N");
            }
        } else {
            FrameImpl.uniqueReturnElements.add(searchElement + "~'dataTestIdUsed'~datatestIdused~" + element.getTagName() + "~Y");
        }
    }

    @Override
    public Frame findFrame(final String frameId) {
        this.switchToCurrentFrame();
        this.pegaDriver.handleWaits().waitForElementPresence(By.id(frameId));
        final Frame frame = new FrameImpl(frameId, this.testEnv);
        frame._initialize(this.testEnv, By.id(frameId));
        frame._setDOMPointer(this.getFrameDocument() + ".defaultView.frames['" + frameId + "']");
        frame._setFrames(this.frames, By.id(frameId));
        FrameImpl.LOGGER.debug("driver location before frame switch:" + this.driver.getCurrentUrl());
        this.driver.switchTo().frame(frameId);
        FrameImpl.LOGGER.debug("driver location after frame switch:" + this.driver.getCurrentUrl());
        return frame;
    }

    @Override
    public Frame findFrame(final PegaWebElement element) {
        this.switchToCurrentFrame();
        this.pegaDriver.handleWaits().waitForElementPresence(element.getBy());
        final Frame frame = new FrameImpl(element);
        frame._initialize(this.testEnv, element.getBy());
        frame._setDOMPointer(this.generateDOMPath(this.getFrameDocument(), element.getBy()));
        frame._setFrames(this.frames, element.getBy());
        FrameImpl.LOGGER.debug("driver location before frame switch:" + this.driver.getCurrentUrl());
        this.driver.switchTo().frame(this.findElement(element.getBy()).getWebElement());
        FrameImpl.LOGGER.debug("driver location after frame switch:" + this.driver.getCurrentUrl());
        return frame;
    }

    @Override
    public Frame findFrame(final String frameId, final boolean waitForFramePresence) {
        if (waitForFramePresence) {
            final WebDriverWait wait = new WebDriverWait(this.driver, GlobalConstants.getGLOBAL_TIMEOUT());
            wait.until((Function) ExpectedConditions.presenceOfElementLocated(By.id(frameId)));
        }
        return this.findFrame(frameId);
    }

    private String identifyRightDoc() {
        Object result = null;
        try {
            result = this.scriptExecutor.executeJavaScript("return " + this.getDOMPointer() + ".contentDocument");
        } catch (WebDriverException ex) {
        }
        String document = null;
        if (result == null) {
            document = ".document";
        } else {
            document = ".contentDocument";
        }
        this.testEnv.getPegaDriver().switchToActiveFrame(this.frames);
        this.testEnv.getPegaDriver().handleWaits().sleep(2L);
        return document;
    }

    @Override
    public DropDown findSelectBox(final By by) {
        FrameImpl.LOGGER.debug("driver current location:" + this.driver.getCurrentUrl());
        this.pegaDriver.handleWaits().verifyAndWaitIfThrobberPresent();
        this.switchToCurrentFrame();
        this.testEnv.getPegaDriver().handleWaits().waitForElementPresence(by);
        final WebElement elmt = this.driver.findElement(by);
        if (this.testEnv.getConfiguration().analyseDataTestId()) {
            this.ReturnElementsFromApp(by, elmt);
        }
        final DropDown dropdown = new DropDownImpl(elmt);
        dropdown._setEnvironment(this.testEnv, by, this.getFrameDocument(), this.frames);
        return dropdown;
    }

    @Override
    public AutoComplete findAutoComplete(final By by) {
        FrameImpl.LOGGER.debug("driver current location:" + this.driver.getCurrentUrl());
        this.switchToCurrentFrame();
        this.testEnv.getPegaDriver().handleWaits().waitForElementPresence(by);
        final WebElement elmt = this.driver.findElement(by);
        if (this.testEnv.getConfiguration().analyseDataTestId()) {
            this.ReturnElementsFromApp(by, elmt);
        }
        final AutoComplete autoComplete = new AutoCompleteImpl(elmt);
        autoComplete._setEnvironment(this.testEnv, by, this.getFrameDocument(), this.frames);
        return autoComplete;
    }

    @Override
    public AnyPicker findAnyPicker(final String anyPickerHandleXpath) {
        final By by = By.xpath(anyPickerHandleXpath);
        FrameImpl.LOGGER.debug("driver current location:" + this.driver.getCurrentUrl());
        this.switchToCurrentFrame();
        this.testEnv.getPegaDriver().handleWaits().waitForElementPresence(by);
        final WebElement elmt = this.driver.findElement(by);
        if (this.testEnv.getConfiguration().analyseDataTestId()) {
            this.ReturnElementsFromApp(by, elmt);
        }
        final AnyPicker anyPicker = new AnyPickerImpl(elmt);
        anyPicker._setEnvironment(this.testEnv, by, this.getFrameDocument(), this.frames);
        return anyPicker;
    }

    @Override
    public ConditionBuilder findConditionBuilder(final By by) {
        ConditionBuilder conditionBuilder = null;
        FrameImpl.LOGGER.debug("driver current location:" + this.driver.getCurrentUrl());
        this.switchToCurrentFrame();
        this.testEnv.getPegaDriver().handleWaits().waitForElementPresence(by);
        final List<WebElement> elmts = this.driver.findElements(by);
        if (elmts.size() == 1) {
            conditionBuilder = new ConditionBuilderImpl(this.testEnv, elmts.get(0));
            conditionBuilder._setEnvironment(this.testEnv, by, this.getFrameDocument(), this.frames);
        }
        conditionBuilder.bootstrapRow();
        return conditionBuilder;
    }

    @Override
    public String getFrameDocument() {
        if (this.frameDoc == null) {
            if (this.getDOMPointer() != null) {
                final String document = this.identifyRightDoc();
                this.frameDoc = this.getDOMPointer() + document;
            } else {
                this.frameDoc = "document";
            }
        }
        return this.frameDoc;
    }

    @Override
    public String getFrameDocument(final boolean force) {
        FrameImpl.LOGGER.debug("FrameDoc: " + this.frameDoc);
        if (force) {
            final String document = this.identifyRightDoc();
            this.frameDoc = this.getDOMPointer() + document;
        }
        return this.frameDoc;
    }

    @Override
    public boolean verifyElement(final By by) {
        boolean isElmtPresent = true;
        this.switchToCurrentFrame();
        try {
            FrameImpl.LOGGER.info("Verify element for presence of: " + by);
            this.pegaDriver.getDriver().findElement(by);
        } catch (NoSuchElementException e) {
            isElmtPresent = false;
        }
        return isElmtPresent;
    }

    @Override
    public List<WebElement> findElements(final By by) {
        this.pegaDriver.handleWaits().verifyAndWaitIfThrobberPresent();
        this.switchToCurrentFrame();
        return this.pegaDriver.getDriver().findElements(by);
    }

    @Override
    public Frame findFrameByName(final String frameName) {
        this.pegaDriver.handleWaits().waitForElementPresence(By.name(frameName));
        final Frame frame = new FrameImpl(frameName, this.testEnv);
        frame._initialize(this.testEnv, By.name(frameName));
        frame._setDOMPointer(this.getFrameDocument() + ".defaultView.frames['" + frameName + "']");
        FrameImpl.LOGGER.debug("driver location before frame switch:" + this.driver.getCurrentUrl());
        this.driver.switchTo().frame(frameName);
        FrameImpl.LOGGER.debug("driver location after frame switch:" + this.driver.getCurrentUrl());
        return frame;
    }

    @Override
    public String getActiveFrameId(final boolean switchToActiveFrame) {
        return this.pegaDriver.getActiveFrameId(switchToActiveFrame);
    }

    @Override
    public String getActiveFrameIdWithInThisFrame() {
        this.pegaDriver.handleWaits().sleep(2L);
        this.switchToCurrentFrame();
        String frameId = null;
        try {
            final WebElement activeFrame = this.driver.findElement(By.xpath(GlobalRepo.getActiveFrameXpath()));
            frameId = activeFrame.getAttribute("id");
            FrameImpl.LOGGER.info("Current Active Frame ID: " + frameId);
        } catch (NoSuchElementException nse) {
            FrameImpl.LOGGER.error("No active frames available on application: " + frameId);
        }
        return frameId;
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
    public void _setDOMPointer(final String domPointer) {
        this.domPointer = domPointer;
    }

    private void _setEnvironment(final TestEnvironment testEnv, final String frameId) {
        this.elmtId = frameId;
        this._initialize(testEnv, By.id(frameId));
        if (!frameId.equals("default")) {
            this._setDOMPointer("window.frames['" + frameId + "']");
            this._setFrames(this.frames, By.id(frameId));
        }
    }

    private void _setEnvironment(final TestEnvironment testEnv, final PegaWebElement element) {
        this.elmt = element;
        this._initialize(testEnv, element.getBy());
        this._setDOMPointer(element.getDOMPointer());
        this._setFrames(this.frames, element.getBy());
    }

    @Override
    public String getId() {
        return this.elmtId;
    }

    @Override
    public WebElement getWebElement() {
        return this.elmt;
    }

    @Override
    public void _setInnerFrameEnv(final TestEnvironment testEnv, final String frameId, final String frameDOM) {
        this._initialize(testEnv, By.id(frameId));
        this._setDOMPointer(frameDOM);
    }

    @Override
    public void _setFrames(final LinkedHashSet<By> frames, final By by) {
        if (frames != null) {
            this.frames = new LinkedHashSet<By>(frames);
        } else {
            this.frames = new LinkedHashSet<By>();
        }
        this.frames.add(by);
        FrameImpl.LOGGER.debug("List of all frames: " + this.frames);
    }

    @Override
    public void switchToParentFrame() {
        this.pegaDriver.getDriver().switchTo().parentFrame();
    }

    @Override
    public boolean verifyElementVisible(final By locator) {
        return this.findElement(locator).isVisible();
    }

    @Override
    public boolean verifyElement(final PegaWebElement elmt, final By locator) {
        return this.pegaDriver.verifyElement(elmt, locator);
    }
}
