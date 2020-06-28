

package com.pega.sync;

import com.pega.framework.*;
import com.pega.util.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.slf4j.*;

import java.util.*;
import java.util.function.*;

public class Throbber {
    String COPYRIGHT;
    private static final String VERSION = "$Id: Throbber.java 164932 2015-11-23 13:49:36Z BalanaveenreddyKappeta $";
    private static final Logger LOGGER;
    private PegaWebDriver pegaWebDriver;

    static {
        LOGGER = LoggerFactory.getLogger(Throbber.class.getName());
    }

    public Throbber(final PegaWebDriver pegaWebDriver) {
        this.COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
        this.pegaWebDriver = pegaWebDriver;
    }

    private void waitForNoThrobber(final int giveUp) {
        if (!this.pegaWebDriver.getTestEnv().getConfiguration().getMobileConfig().isMobileExecution()) {
            boolean throbbing = this.waitForThrobber(giveUp);
            if (throbbing) {
                throbbing = this.waitForNoThrobberTillTimeout(GlobalConstants.getGLOBAL_TIMEOUT());
                if (throbbing) {
                    Throbber.LOGGER.info("Throbbing is still going on even after time out has occured");
                } else {
                    Throbber.LOGGER.info("Throbbing stopped but checking if there are any more throbbing about to start");
                    this.waitForNoThrobber();
                }
            } else {
                Throbber.LOGGER.info("Throbbing never started");
            }
        }
    }

    private boolean waitForNoThrobberTillTimeout(final int timeOut) {
        boolean throbbing = false;
        for (int i = 1; i < timeOut; ++i) {
            throbbing = this.isThrobbing();
            if (!throbbing) {
                break;
            }
            try {
                Thread.sleep(1000L);
            } catch (Exception ex) {
            }
        }
        return throbbing;
    }

    public void waitForNoThrobber() {
        this.waitForNoThrobber(3);
    }

    private boolean waitForThrobber(final int giveUp) {
        boolean throbbing = false;
        for (int i = 1; i <= giveUp; ++i) {
            throbbing = this.isThrobbingOnAllFrames();
            if (!throbbing) {
                break;
            }
            try {
                Thread.sleep(1000L);
            } catch (Exception ex) {
            }
        }
        return throbbing;
    }

    private boolean isThrobbingOnAllFrames() {
        this.pegaWebDriver.getDriver().switchTo().defaultContent();
        if (this.isThrobbing()) {
            return true;
        }
        final List<WebElement> frames = this.pegaWebDriver.findElements(By.tagName("iframe"));
        for (final WebElement frame : frames) {
            this.pegaWebDriver.getDriver().switchTo().defaultContent();
            this.pegaWebDriver.getDriver().switchTo().frame(frame);
            if (this.isThrobbing()) {
                return true;
            }
        }
        return false;
    }

    private boolean isThrobbing() {
        final WebDriverWait webDriverWait = new WebDriverWait(this.pegaWebDriver.getDriver(), 1L);
        WebElement throbberElement;
        try {
            throbberElement = (WebElement) webDriverWait.until((Function) ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@class='throbber']")));
        } catch (Exception e) {
            return false;
        }
        return this.isVisible(throbberElement);
    }

    private boolean isVisible(final WebElement element) {
        final WebDriverWait wait = new WebDriverWait(this.pegaWebDriver.getDriver(), 1L);
        try {
            wait.until((Function) ExpectedConditions.visibilityOf(element));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
