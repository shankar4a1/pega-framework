

package com.pega.sync;

import com.pega.framework.*;
import com.pega.util.*;
import org.apache.commons.lang3.time.*;
import org.openqa.selenium.*;
import org.slf4j.*;

import java.util.*;

public class WaitForDocStateReady {
    String COPYRIGHT;
    private static final String VERSION = "$Id: WaitForDocStateReady.java 164932 2015-11-23 13:49:36Z BalanaveenreddyKappeta $";
    private static final Logger LOGGER;
    private PegaWebDriver pegaDriver;
    private int QUIET_START;
    private StopWatch gStopWatch;
    int TIMEOUT_WCOMM;
    private static int currentDocCounter;
    private static int currentAjaxCounter;
    private static int currentbusyCounter;
    private String currentAjaxStatus;
    private String currentDocStatus;

    static {
        LOGGER = LoggerFactory.getLogger(WaitForDocStateReady.class.getName());
        WaitForDocStateReady.currentDocCounter = -1;
        WaitForDocStateReady.currentAjaxCounter = -1;
        WaitForDocStateReady.currentbusyCounter = -1;
    }

    public WaitForDocStateReady(final PegaWebDriver pegaDriver) {
        this.COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
        this.pegaDriver = null;
        this.QUIET_START = 1000;
        this.gStopWatch = new StopWatch();
        this.TIMEOUT_WCOMM = 1000;
        this.currentAjaxStatus = null;
        this.currentDocStatus = null;
        this.pegaDriver = pegaDriver;
    }

    public boolean waitForDocStateReady() {
        try {
            return this.waitForDocStateReady(false);
        } finally {
            if (this.gStopWatch.isStarted()) {
                this.gStopWatch.stop();
            }
        }
    }

    public boolean waitForDocStateReady(final int commStartTimeInMillis) {
        try {
            this.TIMEOUT_WCOMM = commStartTimeInMillis * 1000;
            return this.waitForDocStateReady(false);
        } finally {
            if (this.gStopWatch.isStarted()) {
                this.gStopWatch.stop();
            }
        }
    }

    @Deprecated
    private boolean waitForDocStateReady(final boolean debug) {
        boolean everActive = false;
        final int TIMEOUT_START = 30000;
        final int TIMEOUT_MAX = GlobalConstants.getGLOBAL_TIMEOUT() * 1000;
        long waitTimeout = TIMEOUT_START;
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        WaitForDocStateReady.LOGGER.debug("Entering DocStateReady...");
        if (!this.gStopWatch.isStarted()) {
            this.gStopWatch.reset();
        }
        boolean everExisted = false;
        while (stopWatch.getTime() < waitTimeout) {
            try {
                this.pegaDriver.handleWaits().waitForPageLoaded();
                final List<WebElement> elements = this.pegaDriver.findElements(By.xpath("//div[@class='document-statetracker']"));
                if (elements.size() > 0) {
                    everExisted = true;
                    final String busyStatus = elements.get(0).getAttribute("data-state-busy-status");
                    if (busyStatus.equals("none")) {
                        if (everActive) {
                            System.out.println();
                            break;
                        }
                        break;
                    } else {
                        if (!everActive) {
                            everActive = true;
                            WaitForDocStateReady.LOGGER.info("Document currently active, waiting entire document to load");
                        } else {
                            System.out.print(".");
                        }
                        if (everActive) {
                            waitTimeout = TIMEOUT_MAX;
                        } else {
                            waitTimeout = this.TIMEOUT_WCOMM;
                        }
                    }
                } else {
                    if (debug) {
                        WaitForDocStateReady.LOGGER.debug("DEBUG: Doc State element not found. (Don't panic!)", true);
                    }
                    this.sleep();
                }
            } catch (Exception e) {
                WaitForDocStateReady.LOGGER.error("Unexpected exception, will keep trying..." + e.toString(), true);
                this.sleep();
            }
        }
        if (!everExisted) {
            WaitForDocStateReady.LOGGER.info("!!! Document state div was never identified...are you sure your system is doc state aware?");
        } else if (stopWatch.getTime() >= waitTimeout) {
            WaitForDocStateReady.LOGGER.info("!!! Still looks like there was active calls, EVEN AFTER THE TIMEOUT (" + waitTimeout + ")!");
        }
        this.pegaDriver.handleWaits().waitForPageLoaded();
        return everActive;
    }

    private void syncDocStateCounters(final WebElement element) {
        WaitForDocStateReady.currentDocCounter = Integer.parseInt(element.getAttribute("data-state-doc-counter"));
        WaitForDocStateReady.currentAjaxCounter = Integer.parseInt(element.getAttribute("data-state-ajax-counter"));
        this.currentDocStatus = element.getAttribute("data-state-doc-status");
        this.currentAjaxStatus = element.getAttribute("data-state-ajax-status");
    }

    private void sleep() {
        try {
            Thread.sleep(100L);
        } catch (Exception ex) {
        }
    }
}
