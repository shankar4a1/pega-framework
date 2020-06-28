

package com.pega.framework;

import com.pega.*;
import com.pega.framework.elmt.*;
import com.pega.objectrepo.*;
import com.pega.ri.*;
import com.pega.sync.*;
import com.pega.util.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.slf4j.*;

import java.util.*;
import java.util.regex.*;

public class PegaWebDriverImpl implements PegaWebDriver {
    private static final String VERSION = "$Id: PegaWebDriverImpl.java 195418 2016-06-01 08:59:34Z AnilBattinapati $";
    private static final Logger LOGGER;
    private static Set<String> uniqueReturnElements;
    protected WebDriver driver;
    protected TestEnvironment testEnv;
    private WaitHandler waitHandler;
    private int tabFrameCntDiff;

    static {
        LOGGER = LoggerFactory.getLogger(PegaWebDriver.class.getName());
        PegaWebDriverImpl.uniqueReturnElements = new HashSet<String>();
    }

    public PegaWebDriverImpl(final WebDriver driver, final TestEnvironment testEnv) {
        this.tabFrameCntDiff = -1;
        this.driver = driver;
        this.testEnv = testEnv;
    }

    public PegaWebDriverImpl(final TestEnvironment testEnv) {
        this.tabFrameCntDiff = -1;
        this.testEnv = testEnv;
        this.driver = testEnv.getPegaDriver().getDriver();
    }

    @Override
    public PegaWebElement findElement(final By by, final boolean switchToDefaultContent) {
        PegaWebDriverImpl.LOGGER.info("element identification by logic:" + by.toString());
        if (GlobalConstants.getCURRENT_FRAME_BY() != null && switchToDefaultContent) {
            PegaWebDriverImpl.LOGGER.debug("switching to top document automatically");
            this.switchTo().defaultContent();
            GlobalConstants.setCURRENT_FRAME_BY(null);
        }
        this.handleWaits().verifyAndWaitIfThrobberPresent();
        this.handleWaits().waitForElementPresence(by);
        final WebElement elmt = this.driver.findElement(by);
        if (this.testEnv.getConfiguration().analyseDataTestId()) {
            this.ReturnElementsFromApp(by, elmt);
        }
        final PegaWebElement pegaWebElmt = new PegaWebElementImpl(elmt);
        pegaWebElmt._setEnvironment(this.testEnv, by, "document");
        PegaWebDriverImpl.LOGGER.debug("DOM Pointer for element identified by: \"" + by + "\" is : " + pegaWebElmt.getDOMPointer());
        ElementUtil.findElmtsWithDTI(pegaWebElmt);
        return pegaWebElmt;
    }

    @Override
    public PegaWebElement findElement(final By by) {
        return this.findElement(by, this.testEnv.getConfiguration().isAutoSwitchToDefaultContent());
    }

    protected String generateDOMPath(final String document, final By by) {
        String domPointer = null;
        final String byLoc = by.toString();
        if (byLoc != null && byLoc.contains(":")) {
            final String[] parts = byLoc.split(":");
            final String type = parts[0];
            final String locator = parts[1].trim();
            if (type.contains("id")) {
                domPointer = document + ".getElementById('" + locator + "')";
            } else if (type.contains("name")) {
                domPointer = document + ".getElementsByName('" + locator + "')[0]";
            } else if (type.contains("selector")) {
                domPointer = document + ".querySelector('" + locator + "')";
            } else if (type.contains("xpath")) {
                domPointer = document + ".evaluate(\"" + locator + "\"," + document + ".body, null, XPathResult.ANY_TYPE, null).iterateNext()";
            } else if (type.contains("linkText")) {
                domPointer = document + ".evaluate(\"//a[text()='" + locator + "']\"," + document + ".body, null, XPathResult.ANY_TYPE, null).iterateNext()";
            } else if (type.contains("partialLinkText")) {
                domPointer = document + ".evaluate(\"//a[contains(text(), '" + locator + "')]\"," + document + ".body, null, XPathResult.ANY_TYPE, null).iterateNext()";
            } else if (type.contains("className")) {
                domPointer = document + ".getElementsByClassName('" + locator + "')[0]";
            } else if (type.contains("tagName")) {
                domPointer = document + ".getElementsByTagName('" + locator + "')[0]";
            }
        }
        return domPointer;
    }

    @Override
    public Frame findFrame(final String frameId) {
        if (GlobalConstants.getCURRENT_FRAME_BY() != null && this.testEnv.getConfiguration().isAutoSwitchToDefaultContent()) {
            PegaWebDriverImpl.LOGGER.debug("switching to top document automatically");
            this.switchTo().defaultContent();
            GlobalConstants.setCURRENT_FRAME_BY(null);
        }
        this.handleWaits().waitForElementPresence(By.id(frameId));
        final Frame frame = new FrameImpl(frameId, this.testEnv);
        frame._setFrames(null, By.id(frameId));
        this.driver.switchTo().frame(frameId);
        return frame;
    }

    public void get(final String url) {
        this.driver.get(url);
        this.loadCustomScripts();
    }

    public String getCurrentUrl() {
        return this.driver.getCurrentUrl();
    }

    public String getTitle() {
        return this.driver.getTitle();
    }

    public List<WebElement> findElements(final By by) {
        this.handleWaits().verifyAndWaitIfThrobberPresent();
        if (GlobalConstants.getCURRENT_FRAME_BY() != null && this.testEnv.getConfiguration().isAutoSwitchToDefaultContent()) {
            this.switchTo().defaultContent();
            GlobalConstants.setCURRENT_FRAME_BY(null);
        }
        return this.driver.findElements(by);
    }

    public String getPageSource() {
        return this.driver.getPageSource();
    }

    public void close() {
        this.driver.close();
    }

    public void quit() {
        this.driver.quit();
    }

    public Set<String> getWindowHandles() {
        return this.driver.getWindowHandles();
    }

    public String getWindowHandle() {
        return this.driver.getWindowHandle();
    }

    public WebDriver.TargetLocator switchTo() {
        return this.driver.switchTo();
    }

    public WebDriver.Navigation navigate() {
        return this.driver.navigate();
    }

    public WebDriver.Options manage() {
        return this.driver.manage();
    }

    @Override
    public WebDriver getDriver() {
        return this.driver;
    }

    @Override
    public void waitForDocStateReady() {
        this.waitForDocStateReady(0);
    }

    @Override
    public void waitForDocStateReady(final int commStartTimeInSecs) {
        this.waitForDocStateReady(commStartTimeInSecs, true);
    }

    @Override
    public void waitForDocStateReady(final int commStartTimeInSecs, final boolean setActiveFrame) {
        if (!this.testEnv.getConfiguration().getMobileConfig().isMobileExecution() || this.testEnv.getConfiguration().getMobileConfig().isMobileDocStateAware()) {
            this.switchTo().defaultContent();
            if (commStartTimeInSecs == 0) {
                new WaitForDocStateReady(this).waitForDocStateReady();
            } else {
                new WaitForDocStateReady(this).waitForDocStateReady(commStartTimeInSecs);
            }
            if (setActiveFrame && !this.findElements(By.xpath(GlobalRepo.getActiveFrameXpath())).isEmpty()) {
                this.switchToActiveFrame();
            }
        }
    }

    @Override
    public void waitForDocStateReady(final int commStartTimeInSecs, final boolean setActiveFrame, final boolean forceExecute) {
        if (forceExecute) {
            this.switchTo().defaultContent();
            if (commStartTimeInSecs == 0) {
                new WaitForDocStateReady(this).waitForDocStateReady();
            } else {
                new WaitForDocStateReady(this).waitForDocStateReady(commStartTimeInSecs);
            }
            if (setActiveFrame && !this.findElements(By.xpath(GlobalRepo.getActiveFrameXpath())).isEmpty()) {
                this.switchToActiveFrame();
            }
        } else {
            this.waitForDocStateReady(commStartTimeInSecs, setActiveFrame);
        }
    }

    @Override
    public void waitForDocStateReady(final boolean setActiveFrame) {
        this.waitForDocStateReady(0, setActiveFrame);
    }

    @Override
    public boolean verifyElement(final PegaWebElement elmt, final By locator) {
        boolean isElmtFound = true;
        try {
            elmt.findElement(locator);
        } catch (NoSuchElementException e) {
            isElmtFound = false;
        }
        return isElmtFound;
    }

    @Override
    public boolean verifyElement(final By locator) {
        boolean isElmtFound = true;
        try {
            this.driver.findElement(locator);
        } catch (NoSuchElementException e) {
            isElmtFound = false;
        }
        return isElmtFound;
    }

    @Override
    public void switchToActiveFrame() {
        try {
            this.getActiveFrameId(true);
        } catch (WebDriverException e) {
            e.printStackTrace();
            PegaWebDriverImpl.LOGGER.error("No active frame");
        }
    }

    @Override
    public String getActiveFrameId() {
        return this.getActiveFrameId(false);
    }

    @Override
    public String getActiveFrameId(final boolean switchToActiveFrame) {
        this.driver.switchTo().defaultContent();
        WebElement activeFrame = null;
        final long startTime = Calendar.getInstance().getTimeInMillis();
        Object isLoaded = null;
        final Object tabElmt = this.testEnv.getScriptExecutor().executeJavaScript("return document.evaluate(\"//div[@id='workarea']//li[@role='tab'][@tabgroupname!='']\",document.body, null, XPathResult.ANY_TYPE, null).iterateNext()");
        final boolean isTabPresent = tabElmt != null && !"null".equals(tabElmt.toString());
        if (isTabPresent) {
            try {
                isLoaded = this.testEnv.getScriptExecutor().executeJavaScript("return isTabLoaded();");
            } catch (Exception e) {
                PegaWebDriverImpl.LOGGER.error("IsTabLoadExecutionException::" + e.getMessage());
            }
        } else {
            PegaWebDriverImpl.LOGGER.debug("Pega Gadget frames are not present inside tabs. Hence, not checking for tabs to load...");
        }
        final int timeOut = 180;
        PegaWebDriverImpl.LOGGER.debug("Looks like Tab is not loaded yet, will wait and see if it loads in next " + timeOut / 60 + " mins");
        if (isLoaded != null) {
            boolean status = (boolean) isLoaded;
            int counter = 0;
            while (!status && counter < timeOut) {
                this.handleWaits().sleep(1L);
                ++counter;
                status = (boolean) this.testEnv.getScriptExecutor().executeJavaScript("return isTabLoaded();");
                if (counter % 60 == 0) {
                    PegaWebDriverImpl.LOGGER.debug((timeOut - counter) / 60 + " more minutes to go");
                }
            }
        }
        final long endTime = Calendar.getInstance().getTimeInMillis();
        PegaWebDriverImpl.LOGGER.debug("Time to load new tab is::" + (endTime - startTime) + "ms");
        if (this.verifyElement(By.xpath("//div[@class='document-statetracker']"))) {
            this.waitForDocStateReady(false);
        } else {
            this.handleWaits().waitForPageLoaded();
            try {
                this.handleWaits().waitForElementPresence(By.xpath(GlobalRepo.getActiveFrameXpath()), 20);
            } catch (Exception ex) {
            }
        }
        this.driver.switchTo().defaultContent();
        if (isTabPresent) {
            this.handleWaits().waitForFramesToBeLoaded();
        }
        this.driver.switchTo().defaultContent();
        String frameId = null;
        try {
            activeFrame = this.driver.findElement(By.xpath(GlobalRepo.getActiveFrameXpath()));
            frameId = activeFrame.getAttribute("id");
            PegaWebDriverImpl.LOGGER.info("Current Active Frame ID: " + frameId);
            if (switchToActiveFrame) {
                this.driver.switchTo().frame(frameId);
                GlobalConstants.setCURRENT_FRAME_BY(By.id(frameId));
            }
        } catch (NoSuchElementException nse) {
            PegaWebDriverImpl.LOGGER.error("No active frames available on application: " + frameId);
        }
        return frameId;
    }

    @Override
    public void switchToActiveFrame(final String eleDOM) {
        String dom = eleDOM;
        this.driver.switchTo().defaultContent();
        if (dom.contains("evaluate")) {
            dom = dom.substring(0, dom.indexOf("evaluate"));
        }
        final Pattern p = Pattern.compile("frames\\['.*?'\\]");
        final Matcher m = p.matcher(dom);
        while (m.find()) {
            final String matchedText = m.group();
            String frameId = matchedText.replace("frames['", "");
            frameId = frameId.replace("']", "");
            if (!"default".equals(frameId)) {
                this.driver.switchTo().frame(frameId);
            }
        }
    }

    @Override
    public WaitHandler handleWaits() {
        if (this.waitHandler == null) {
            this.waitHandler = new WaitHandlerImpl(this.testEnv);
        }
        return this.waitHandler;
    }

    @Override
    public PegaWebElement convertToPegaWebElmt(final WebElement elmt, final By by, final Frame frame) {
        final PegaWebElement pegaWebElmt = new PegaWebElementImpl(elmt);
        final String document = (frame == null) ? "document" : (frame.getDOMPointer() + ".ownerDocument");
        pegaWebElmt._setEnvironment(this.testEnv, by, document);
        PegaWebDriverImpl.LOGGER.debug("DOM Pointer for element identified by: \"" + by + "\" is : " + pegaWebElmt.getDOMPointer());
        return pegaWebElmt;
    }

    @Override
    public DropDown findSelectBox(final By by) {
        this.handleWaits().verifyAndWaitIfThrobberPresent();
        if (GlobalConstants.getCURRENT_FRAME_BY() != null && this.testEnv.getConfiguration().isAutoSwitchToDefaultContent()) {
            this.switchTo().defaultContent();
            GlobalConstants.setCURRENT_FRAME_BY(null);
        }
        this.handleWaits().waitForElementPresence(by);
        final WebElement elmt = this.driver.findElement(by);
        if (this.testEnv.getConfiguration().analyseDataTestId()) {
            this.ReturnElementsFromApp(by, elmt);
        }
        final DropDown dropdown = new DropDownImpl(elmt);
        dropdown._setEnvironment(this.testEnv, by, "document");
        PegaWebDriverImpl.LOGGER.debug("DOM Pointer for element identified by: \"" + by + "\" is : " + dropdown.getDOMPointer());
        return dropdown;
    }

    @Override
    public void loadCustomScripts() {
        this.testEnv.getScriptExecutor().executeJavaScript("window.getAbsoluteElementOffsetLeft=function getAbsoluteElementOffsetLeft(elmt){var clientRect = elmt.getBoundingClientRect();var absoluteLeft = clientRect.left;var parent = elmt.ownerDocument.defaultView.frameElement;while(parent!=null){absoluteLeft = absoluteLeft+parent.getBoundingClientRect().left;parent = parent.ownerDocument.defaultView.frameElement;}return Math.round(absoluteLeft);}");
        this.testEnv.getScriptExecutor().executeJavaScript("window.getAbsoluteElementOffsetTop=function getAbsoluteElementOffsetTop(elmt){var clientRect = elmt.getBoundingClientRect();var absoluteTop = clientRect.top;var parent = elmt.ownerDocument.defaultView.frameElement;while(parent!=null){absoluteTop = absoluteTop+parent.getBoundingClientRect().top;parent = parent.ownerDocument.defaultView.frameElement;}return Math.round(absoluteTop);}");
        this.testEnv.getScriptExecutor().executeJavaScript("window.helpMe = function helpMe(elmt, action){var info = '';var frames = getFrameStack(elmt);info = 'Frames stack: ' + frames + 'Top Document\\n';var id = elmt.id == '' ? null : elmt.id;var text = elmt.textContent == '' ? null : elmt.textContent;info = info + 'Id: ' + id + '\\n';info = info + 'Name: ' + elmt.getAttribute('name') + '\\n';info = info + 'Data Test Id: ' + elmt.getAttribute('data-test-id') + '\\n';info = info + 'Tag: ' + elmt.tagName + '\\n';info = info + 'TextContent: ' + text + '\\n-------------------------\\n';var design = frames == '' ? 'Since the inspected element is in Top Document, the page class should extend TopDocument. \\nEx: public interface <InterfaceName> extends TopDocument.' : 'Since the inspected element is in Iframe, the page class should extend Frame. \\nEx: public interface <InterfaceName> extends Frame.';design = design + ' \\nIn case of exisiting class review the parent class hierarchy.';info = info + 'Suggestions for best possible find options in priority order: \\n' + getAllFindOptions(elmt);info = info + '\\n---------------------\\nSuggestions for page class design/review: \\n' + design;if(action) {info = info + '\\n---------------------\\nSuggestions for framework methods to do given action in priority order: \\n' + getActionsList(action) + '\\nNote: <elmt> = findElement(<By>);'}return info;};function getActionsList(action) {var actions = '';action = action.toLowerCase();if(action == 'click') {actions = actions + '<elmt>.click(); \\nscriptExecutor.click(<elmt>); \\n<elmt>.doClickWithMouse();\\n'} else if(action == 'type' || action == 'sendkeys') {actions = actions + '<elmt>.sendKeys(<input>); \\nscriptExecutor.sendKeys(<elmt>, <input>); \\n'}return actions;}function getFrameStack(elmt) {var frames = '';var parent = elmt.ownerDocument.defaultView.frameElement;while(parent!=null){frames = frames + parent.id  + ' -> ';parent = parent.ownerDocument.defaultView.frameElement;}return frames;}function getAllFindOptions(elmt) {var findOptions = '';var text = elmt.textContent == '' ? null : elmt.textContent;var idStr = elmt.id && elmt.id !='' && elmt.id.match(\"^\\d+\") == null ? elmt.id : '';var nameStr = elmt.name && elmt.name !='' ? elmt.name : '';var linkStr = elmt.tagName == 'A' ? elmt.textContent : '';var dataTestId = elmt.getAttribute('data-test-id');var cssStr = elmt.getAttribute('data-test-id') != null ? elmt.tagName.toLowerCase() +'[data-test-id=\\'' + dataTestId + '\\']' : \"\";cssStr = elmt.getAttribute('title') != null && elmt.getAttribute('title') != '' && cssStr !=\"\" ? elmt.tagName.toLowerCase() + '[title=\\'' + elmt.getAttribute('title') + '\\']' : cssStr;cssStr = elmt.getAttribute('alt') != null && elmt.getAttribute('alt') != '' && cssStr !=\"\" ? elmt.tagName.toLowerCase() + '[alt=\\'' + elmt.getAttribute('alt') + '\\']' : cssStr;cssStr = elmt.getAttribute('placeholder') != null && elmt.getAttribute('placeholder') != '' && cssStr !=\"\" ? elmt.tagName.toLowerCase() + '[placeholder=\\'' + elmt.getAttribute('placeholder') + '\\']' : cssStr;var xpathStr = getXpathExpr(elmt);findOptions = idStr!='' ? getMethodName(elmt) + '(By.id(\"'+idStr+'\"))' + '\\n': findOptions;findOptions = nameStr!='' ? findOptions + getMethodName(elmt) + '(By.name(\"'+nameStr+'\"))' + '\\n' : findOptions;findOptions = linkStr!='' ? findOptions + getMethodName(elmt) + '(By.linkText(\"'+linkStr+'\"))' + '\\n' : findOptions;findOptions = cssStr!='' ? findOptions + getMethodName(elmt) + '(By.cssSelector(\"'+cssStr+'\"))' + '\\n' : findOptions;findOptions = xpathStr!='' ? findOptions + getMethodName(elmt) + '(By.xpath('+xpathStr+'))' : findOptions;findOptions = findOptions=='' ? 'Not Found' : findOptions.substring(0, findOptions.length - 1);return findOptions;}function getXpathExpr(elmt) {var text = elmt.textContent == '' ? null : elmt.textContent;var xpathStr = elmt.tagName.toLowerCase()=='span' && elmt.className.indexOf('menu-item-title') != -1 ? 'XPathUtil.getMenuItemXPath(\"'+elmt.textContent+'\")' : '';xpathStr = elmt.tagName.toLowerCase()=='button' && elmt.className.indexOf('pzbutton') != -1 ? 'XPathUtil.getButtonpzButtonXpath(\"'+elmt.textContent+'\")' : xpathStr;xpathStr = text != null && xpathStr==\"\" ? '\"//' + elmt.tagName.toLowerCase() + '[text()=\\'' + text + '\\']\"' : xpathStr;return xpathStr;}function getMethodName(elmt) {var methodName='findElement';if(elmt.tagName.toLowerCase()=='select') {methodName='findSelectBox';} else if(elmt.tagName.toLowerCase()=='input' && elmt.className.indexOf('autocomplete_input') != -1) {methodName='findAutoComplete'}return methodName;}");
        this.testEnv.getScriptExecutor().executeJavaScript("window.isTabLoaded=function isTabLoaded(){var tabs = document.querySelectorAll(\"ul[class*='Temporary_top_tabsList'] li[role='tab']\");for(var i=0; i<tabs.length; i++){var curr = tabs[i];if(!curr.hasAttribute('title')){return false;}var title = curr.getAttribute('title');if(title.indexOf('Opening')!=-1){return false;}}return true;}");
    }

    @Override
    public Wizard findWizard(final String frameId) {
        Wizard wizard = null;
        if (frameId != null) {
            this.handleWaits().waitForElementPresence(By.id(frameId));
            wizard = new WizardImpl(frameId, this.testEnv);
            wizard._setFrames(null, By.id(frameId));
            this.driver.switchTo().frame(frameId);
        } else {
            wizard = new WizardImpl("default", this.testEnv);
            this.driver.switchTo().defaultContent();
        }
        return wizard;
    }

    @Override
    public boolean verifyElementVisible(final By locator) {
        if (GlobalConstants.getCURRENT_FRAME_BY() != null && this.testEnv.getConfiguration().isAutoSwitchToDefaultContent()) {
            this.switchTo().defaultContent();
            GlobalConstants.setCURRENT_FRAME_BY(null);
        }
        return this.findElement(locator).isVisible();
    }

    private void verifyAndWaitIfThrobberPresent() {
        final By throbberpath = By.xpath("//*[@class='throbber']");
        try {
            if (this.verifyElement(throbberpath) && this.driver.findElement(By.xpath("//*[@class='throbber']")).isDisplayed()) {
                this.handleWaits().waitForElementNotVisible(By.xpath("//*[@class='throbber']"));
            }
        } catch (Exception e) {
            PegaWebDriverImpl.LOGGER.error("Error while waiting for throbber in verifyAndWaitIfThrobberPresent():" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public AutoComplete findAutoComplete(final By by) {
        this.verifyAndWaitIfThrobberPresent();
        if (GlobalConstants.getCURRENT_FRAME_BY() != null && this.testEnv.getConfiguration().isAutoSwitchToDefaultContent()) {
            this.switchTo().defaultContent();
            GlobalConstants.setCURRENT_FRAME_BY(null);
        }
        this.handleWaits().waitForElementPresence(by);
        final WebElement elmt = this.driver.findElement(by);
        if (this.testEnv.getConfiguration().analyseDataTestId()) {
            this.ReturnElementsFromApp(by, elmt);
        }
        final AutoComplete autoComplete = new AutoCompleteImpl(elmt);
        autoComplete._setEnvironment(this.testEnv, by, "document");
        PegaWebDriverImpl.LOGGER.debug("DOM Pointer for element identified by: \"" + by + "\" is : " + autoComplete.getDOMPointer(), true);
        return autoComplete;
    }

    @Override
    public TestEnvironment getTestEnv() {
        return this.testEnv;
    }

    @Override
    public int getDefaultFrameTabCntDiff(final boolean login) {
        if (login) {
            PegaWebDriverImpl.LOGGER.debug("Initializing default tab and frame count difference after login ", true);
            final int defFrameCnt = this.driver.findElements(By.xpath("//iframe[contains(@id,'PegaGadget')]")).size();
            final int defTabCnt = this.driver.findElements(By.cssSelector("ul[class*='Temporary_top_tabsList'] li[role='tab']")).size();
            this.tabFrameCntDiff = defTabCnt - defFrameCnt;
            PegaWebDriverImpl.LOGGER.debug("Tab frame count difference is : " + this.tabFrameCntDiff, true);
        }
        return this.tabFrameCntDiff;
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
        PegaWebDriverImpl.LOGGER.info("element identification by logic in Find Element pwga web Element impl:" + searchElement);
        PegaWebDriverImpl.LOGGER.debug("before if condition in find element in pega web Element impl condition value" + by.toString().contains("data-test-id") + "by.tostring value: " + by.toString());
        if (by.toString() != null && !by.toString().contains("data-test-id")) {
            PegaWebDriverImpl.LOGGER.debug("Entered If condition in the find element method:" + by);
            if (element.getAttribute("data-test-id") == null) {
                PegaWebDriverImpl.uniqueReturnElements.add(searchElement + "~'" + element.getAttribute("data-test-id") + "'~0~" + element.getTagName() + "~N");
            } else if (this.findElements(By.xpath("//" + element.getTagName() + "[@data-test-id = '" + element.getAttribute("data-test-id") + "']")).size() > 1) {
                PegaWebDriverImpl.uniqueReturnElements.add(searchElement + "~'" + element.getAttribute("data-test-id") + "'~" + this.findElements(By.xpath("//" + element.getTagName() + "[@data-test-id = '" + element.getAttribute("data-test-id") + "']")).size() + "~" + element.getTagName() + "~N");
            } else {
                PegaWebDriverImpl.uniqueReturnElements.add(searchElement + "~'" + element.getAttribute("data-test-id") + "'~" + this.findElements(By.xpath("//" + element.getTagName() + "[@data-test-id = '" + element.getAttribute("data-test-id") + "']")).size() + "~" + element.getTagName() + "~N");
            }
        } else {
            PegaWebDriverImpl.uniqueReturnElements.add(searchElement + "~'dataTestIdUsed'~datatestIdused~" + element.getTagName() + "~Y");
        }
    }

    public static Set<String> getElements() {
        return PegaWebDriverImpl.uniqueReturnElements;
    }

    public static void setElementsEmpty() {
        if (!PegaWebDriverImpl.uniqueReturnElements.isEmpty()) {
            PegaWebDriverImpl.uniqueReturnElements.clear();
        }
    }

    @Override
    public Frame findFrame(final PegaWebElement element) {
        final Frame frame = new FrameImpl(element);
        frame._setFrames(null, element.getBy());
        this.driver.switchTo().frame(element.getWebElement());
        return frame;
    }

    @Override
    public Wizard findWizard(final PegaWebElement element) {
        Wizard wizard = null;
        if (element != null) {
            wizard = new WizardImpl(element);
            wizard._setFrames(null, element.getBy());
            this.driver.switchTo().frame(element.getWebElement());
        } else {
            wizard = new WizardImpl("default", this.testEnv);
            this.driver.switchTo().defaultContent();
        }
        return wizard;
    }

    @Override
    public void switchToActiveFrame(final LinkedHashSet<By> frames) {
        this.driver.switchTo().defaultContent();
        if (frames != null) {
            for (final By frame : frames) {
                final WebElement element = this.driver.findElement(frame);
                this.driver.switchTo().frame(element);
                GlobalConstants.setCURRENT_FRAME_BY(frame);
            }
        }
    }

    @Override
    public String getTextStaleException(final WebElement element) {
        String date;
        try {
            date = element.getText();
        } catch (StaleElementReferenceException ex) {
            date = element.getText();
            return date;
        } finally {
            System.out.println("StaleElementReferenceException @ com.pega.dsm.junit.canvas.strategy.pagemodel.InlinePanel.getTextAlter");
        }
        System.out.println("StaleElementReferenceException @ com.pega.dsm.junit.canvas.strategy.pagemodel.InlinePanel.getTextAlter");
        return date;
    }
}
