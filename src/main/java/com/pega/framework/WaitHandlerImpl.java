

package com.pega.framework;

import com.pega.*;
import com.pega.exceptions.*;
import com.pega.objectrepo.*;
import com.pega.sync.*;
import com.pega.util.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;
import java.util.function.*;

public class WaitHandlerImpl implements WaitHandler {
    private static final String VERSION = "$Id: WaitHandlerImpl.java 190774 2016-05-04 09:14:20Z ShakkariSakethkumar $";
    private static final Logger LOGGER;
    private PegaWebDriver pegaDriver;
    private WebDriver driver;
    private TestEnvironment testEnv;

    static {
        LOGGER = LoggerFactory.getLogger(WaitHandler.class.getName());
    }

    public WaitHandlerImpl(final TestEnvironment testEnv) {
        this.testEnv = testEnv;
        this.pegaDriver = testEnv.getPegaDriver();
        this.driver = this.pegaDriver.getDriver();
    }

    @Override
    public void waitForFramesToBeLoaded() {
        final Wait<WebDriver> wait = new WebDriverWait(this.driver, GlobalConstants.getGLOBAL_TIMEOUT());
        wait.until((Function) checkFramesStatus(this.pegaDriver.getDefaultFrameTabCntDiff(false)));
    }

    private static com.google.common.base.Function<WebDriver, Boolean> checkFramesStatus(final int defaultFrameTabCntDiff) {
        return new com.google.common.base.Function<WebDriver, Boolean>() {
            public Boolean apply(final WebDriver driver) {
                final List<WebElement> tabs = driver.findElements(WaitHandlerImpl.ALL_OPENED_TABS);
                WaitHandlerImpl.LOGGER.debug("Tabs Count: " + tabs.size());
                final int tabSize = tabs.size();
                if (tabSize == 0) {
                    return true;
                }
                final List<WebElement> currentFrames = driver.findElements(WaitHandlerImpl.ALL_AVAILABLE_FRAMES);
                WaitHandlerImpl.LOGGER.debug("Frames Count: " + currentFrames.size());
                return currentFrames.size() == tabSize - defaultFrameTabCntDiff;
            }
        };
    }

    @Override
    public void waitForElementVisibility(final By locator) {
        final Wait<WebDriver> wait = new WebDriverWait(this.driver, GlobalConstants.getGLOBAL_TIMEOUT());
        wait.until((Function) ExpectedConditions.visibilityOfElementLocated(locator));
    }

    @Override
    public void waitForTextPresence(final By locator, final String text) {
        final Wait<WebDriver> wait = new WebDriverWait(this.driver, GlobalConstants.getGLOBAL_TIMEOUT());
        wait.until((Function) ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    @Override
    public void waitForElementVisibility(final WebElement element) {
        this.waitForElementVisibility(element, GlobalConstants.getGLOBAL_TIMEOUT());
    }

    @Override
    public void waitForElementVisibility(final WebElement element, final int timeOutInSeconds) {
        final Wait<WebDriver> wait = new WebDriverWait(this.driver, timeOutInSeconds);
        wait.until((Function) ExpectedConditions.visibilityOf(element));
    }

    @Override
    public void waitTillElementIsEnabled(final By locator) {
        this.waitForElementPresence(locator);
        for (int i = 1; i <= GlobalConstants.getGLOBAL_TIMEOUT(); ++i) {
            final WebElement ele = this.driver.findElement(locator);
            if (ele.isEnabled()) {
                return;
            }
            this.sleep(1L);
        }
    }

    @Override
    public void waitForElementNotVisible(final By locator) {
        final Wait<WebDriver> wait = new WebDriverWait(this.driver, GlobalConstants.getGLOBAL_TIMEOUT());
        wait.until((Function) ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    @Override
    public void waitForElementClickable(final By locator) {
        final Wait<WebDriver> wait = new WebDriverWait(this.driver, GlobalConstants.getGLOBAL_TIMEOUT());
        wait.until((Function) ExpectedConditions.elementToBeClickable(locator));
    }

    @Override
    public void waitForPageLoaded() {
        final Wait<WebDriver> wait = new WebDriverWait(this.driver, GlobalConstants.getGLOBAL_TIMEOUT());
        try {
            wait.until((Function) checkPageLoadStatus());
        } catch (Throwable t) {
        }
    }

    @Override
    public void waitForNewFrameAndSwitchToIt() {
        this.driver.switchTo().defaultContent();
        final String currentFrameId = GlobalConstants.getACTIVE_FRAME_ID();
        for (int i = 1; i <= GlobalConstants.getGLOBAL_TIMEOUT(); ++i) {
            final WebElement activeFrame = this.driver.findElement(By.xpath(GlobalRepo.getActiveFrameXpath()));
            final String frameId = activeFrame.getAttribute("id");
            if (frameId != null && !frameId.equals(GlobalConstants.getACTIVE_FRAME_ID())) {
                GlobalConstants.setACTIVE_FRAME_ID(frameId);
                this.driver.switchTo().frame(activeFrame);
                break;
            }
            this.sleep(1L);
        }
        if (currentFrameId.equals(GlobalConstants.getACTIVE_FRAME_ID())) {
            throw new WebDriverException("Timeout occured trying to find new frame");
        }
    }

    @Override
    public void clickAndWait(final By source, final By target, final boolean visibility, final boolean clickable) {
        this.waitForElementPresence(source);
        if (visibility) {
            this.waitForElementVisibility(source);
        }
        if (clickable) {
            this.waitForElementClickable(source);
        }
        this.driver.findElement(source).click();
        if (target != null) {
            this.waitForElementPresence(target);
        }
    }

    @Override
    public void clickAndWait(final By source, final By target, final boolean visibility, final boolean clickable, final PegaWebElement.CheckLocalized checkLocalized) {
        this.waitForElementPresence(source);
        if (visibility) {
            this.waitForElementVisibility(source);
        }
        if (clickable) {
            this.waitForElementClickable(source);
        }
        this.pegaDriver.findElement(source).click(false, checkLocalized);
        if (target != null) {
            this.waitForElementPresence(target);
        }
    }

    @Override
    public PegaWebElement waitAndGetElmt(final By locator, final boolean visible) {
        this.waitForElementPresence(locator);
        if (visible) {
            this.waitForElementVisibility(locator);
        }
        return this.pegaDriver.findElement(locator);
    }

    @Override
    public void waitForElementPresence(final By locator) {
        this.waitForElementPresence(locator, GlobalConstants.getGLOBAL_TIMEOUT());
    }

    @Override
    public void waitForElementPresence(final By locator, final int maxTimeOutInSeconds) {
        final Wait<WebDriver> wait = new WebDriverWait(this.driver, maxTimeOutInSeconds);
        try {
            wait.until((Function) this.presenceOfElementLocated(locator));
        } catch (TimeoutException te) {
            throw new PegaElementNotFoundException(te.getCause().getMessage());
        }
    }

    @Override
    public void waitTillTitleContains(final String text) {
        final Wait<WebDriver> wait = new WebDriverWait(this.driver, GlobalConstants.getGLOBAL_TIMEOUT());
        wait.until((Function) ExpectedConditions.titleContains(text));
    }

    @Override
    public void waitForWindowPresence(final Integer requiredWindowNo) {
        final Wait<WebDriver> wait = new WebDriverWait(this.driver, GlobalConstants.getGLOBAL_TIMEOUT());
        wait.until((Function) checkWindowPresence(requiredWindowNo.toString()));
    }

    @Override
    public void waitForCondition(final String condition) {
        final Wait<WebDriver> wait = new WebDriverWait(this.driver, GlobalConstants.getGLOBAL_TIMEOUT());
        try {
            wait.until((Function) myCondition(condition));
        } catch (Throwable error) {
            WaitHandlerImpl.LOGGER.error(error.getMessage());
        }
    }

    @Override
    public void sleep(final long seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void waitForNoThrobber() {
        if (!this.testEnv.getConfiguration().getMobileConfig().isMobileExecution()) {
            new Throbber(this.pegaDriver).waitForNoThrobber();
            this.pegaDriver.switchTo().defaultContent();
            if (!this.pegaDriver.findElements(By.xpath(GlobalRepo.getActiveFrameXpath())).isEmpty()) {
                this.pegaDriver.getActiveFrameId(true);
            }
        }
    }

    @Override
    public void waitForDocStateReady() {
        if (!this.pegaDriver.getTestEnv().getConfiguration().getMobileConfig().isMobileExecution() || this.testEnv.getConfiguration().getMobileConfig().isMobileDocStateAware()) {
            this.pegaDriver.switchTo().defaultContent();
            new WaitForDocStateReady(this.pegaDriver).waitForDocStateReady();
            if (!this.pegaDriver.findElements(By.xpath(GlobalRepo.getActiveFrameXpath())).isEmpty()) {
                this.pegaDriver.getActiveFrameId(true);
            }
        }
    }

    @Override
    public void waitForDocStateReady(final int commStartTimeInSecs) {
        if (!this.pegaDriver.getTestEnv().getConfiguration().getMobileConfig().isMobileExecution() || this.testEnv.getConfiguration().getMobileConfig().isMobileDocStateAware()) {
            this.pegaDriver.switchTo().defaultContent();
            new WaitForDocStateReady(this.pegaDriver).waitForDocStateReady(commStartTimeInSecs);
            if (!this.pegaDriver.findElements(By.xpath(GlobalRepo.getActiveFrameXpath())).isEmpty()) {
                this.pegaDriver.getActiveFrameId(true);
            }
        }
    }

    @Deprecated
    @Override
    public void waitForDocStateReady(final boolean setActiveFrame) {
        if (!this.pegaDriver.getTestEnv().getConfiguration().getMobileConfig().isMobileExecution() || this.testEnv.getConfiguration().getMobileConfig().isMobileDocStateAware()) {
            this.waitForDocStateReady();
            if (setActiveFrame) {
                WebElement applicationWindow = null;
                applicationWindow = this.driver.findElement(By.xpath("(//iframe[contains(@id,'PegaGadget')])[last()]"));
                this.driver.switchTo().frame(applicationWindow);
            }
        }
    }

    private com.google.common.base.Function<WebDriver, WebElement> presenceOfElementLocated(final By locator) {
        return new com.google.common.base.Function<WebDriver, WebElement>() {
            public WebElement apply(final WebDriver driver) {
                return driver.findElement(locator);
            }
        };
    }

    private static com.google.common.base.Function<WebDriver, Boolean> checkPageLoadStatus() {
        return new com.google.common.base.Function<WebDriver, Boolean>() {
            public Boolean apply(final WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };
    }

    private static com.google.common.base.Function<WebDriver, Boolean> myCondition(final String condition) {
        return new com.google.common.base.Function<WebDriver, Boolean>() {
            public Boolean apply(final WebDriver driver) {
                return (Boolean) ((JavascriptExecutor) driver).executeScript("return " + condition, new Object[0]);
            }
        };
    }

    private static com.google.common.base.Function<WebDriver, Boolean> checkWindowPresence(final String requireWindowCount) {
        return new com.google.common.base.Function<WebDriver, Boolean>() {
            public Boolean apply(final WebDriver driver) {
                final int actualCount = driver.getWindowHandles().size();
                int requiredWindowNo = 0;
                try {
                    requiredWindowNo = Integer.parseInt(requireWindowCount);
                } catch (NumberFormatException e) {
                    throw new PegaWebDriverException("Invalid value given for window count. It must be an Integer");
                }
                return requiredWindowNo <= actualCount;
            }
        };
    }

    @Override
    public void waitForAlert() {
        int i = 0;
        while (i++ < GlobalConstants.getGLOBAL_TIMEOUT()) {
            try {
                this.driver.switchTo().alert();
                break;
            } catch (NoAlertPresentException e) {
                this.sleep(1L);
            }
        }
    }

    @Override
    public void waitForElementNotPresence(final By locator) {
        final WebDriverWait wait = new WebDriverWait(this.driver, GlobalConstants.getGLOBAL_TIMEOUT());
        wait.until((Function) this.checkElementNotPresence(locator));
    }

    @Override
    public void waitForElementNotPresence(final By locator, final int timeOutInSeconds) {
        final Wait<WebDriver> wait = new WebDriverWait(this.driver, timeOutInSeconds);
        wait.until((Function) this.checkElementNotPresence(locator));
    }

    private com.google.common.base.Function<WebDriver, Boolean> checkElementNotPresence(final By locator) {
        return new com.google.common.base.Function<WebDriver, Boolean>() {
            public Boolean apply(final WebDriver driver) {
                return driver.findElements(locator).isEmpty();
            }
        };
    }

    @Override
    public void verifyAndWaitIfThrobberPresent() {
        if (!this.testEnv.getConfiguration().getMobileConfig().isMobileExecution()) {
            final By throbberpath = By.xpath("//*[@class='throbber']");
            try {
                if (this.pegaDriver.verifyElement(throbberpath) && this.driver.findElement(By.xpath("//*[@class='throbber']")).isDisplayed()) {
                    this.waitForElementNotVisible(By.xpath("//*[@class='throbber']"));
                }
            } catch (Exception e) {
                WaitHandlerImpl.LOGGER.error("Error while waiting for throbber in verifyAndWaitIfThrobberPresent():" + e.getMessage());
            }
        }
    }

    @Override
    public void waitForTextPresence(final By locator, final String text, final long timeOutInSeconds) {
        final Wait<WebDriver> wait = new WebDriverWait(this.driver, timeOutInSeconds);
        wait.until((Function) ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    @Override
    public void getPortalReady() {
        this.pegaDriver.switchTo().defaultContent();
        boolean isNotReady = true;
        int counter = 1;
        do {
            try {
                isNotReady = this.pegaDriver.verifyElementVisible(By.xpath("//*[text()='Loading...']"));
                this.pegaDriver.handleWaits().sleep(60L);
                ++counter;
            } catch (Exception e) {
                break;
            }
        } while (isNotReady && counter < 11);
    }

    @Override
    public void waitForFileDownload(final String fileName, final int timeOutInSeconds) {
        final long timeout = timeOutInSeconds * 1000;
        final File file = new File(this.testEnv.getBrowser().getDownloadDir() + "/" + fileName);
        for (long startTime = Calendar.getInstance().getTimeInMillis(), diff = 0L; diff < timeout && !file.exists(); diff = Calendar.getInstance().getTimeInMillis() - startTime) {
            WaitHandlerImpl.LOGGER.info("waiting for " + fileName + " to download...");
            this.pegaDriver.handleWaits().sleep(1L);
        }
    }
}
