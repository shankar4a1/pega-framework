package com.pega.util;

import com.pega.*;
import org.openqa.selenium.*;

import java.util.*;

public class ClientPerformanceUtil {
    private double domLoadTime;
    private double domInteractiveTime;
    private double netWorkDelay;
    private double pageLoadTime;
    private double totalPageLoadTime;
    private double dataDstBusy;
    private String dataDstHttp;
    private String elementIdentification;
    private String stepDescription;
    private List<List<String>> finalisedClientPerformanceAttributes;
    private TestEnvironment testEnv;

    public ClientPerformanceUtil(final TestEnvironment testEnv) {
        this.finalisedClientPerformanceAttributes = new ArrayList<List<String>>();
        this.testEnv = testEnv;
    }

    public void captureMetrics(final String desc) {
        //   final StepDefinitionMatch match = ThreadLocalStepDefinitionMatch.get();
        //     final String stepName = match.getStepName();
        //   this.captureMetrics(desc, stepName);
    }

    public void captureMetrics(final String elementIdentification, final String desc) {
        this.captureMetrics(false, elementIdentification, desc);
    }

    public void captureMetrics(final boolean autoCaptureMetrics, final String elementIdentification, final String stepName) {
        final WebDriver driver = this.testEnv.getPegaDriver().getDriver();
        driver.switchTo().defaultContent();
        final WebElement clientPerformanceEle = driver.findElement(By.xpath("//div[@class='client-performance']"));
        final WebElement docStateTrackerEle = driver.findElement(By.xpath("//div[@class='document-statetracker']"));
        final String fileName = "ClientPerformanceMetric.csv";
        final String[] headerValues = {"Domloadtime", "Dominteractivetime", "Networkdelay", "Pageloadtime", "Totalpageloadtime", "BusyDuration", "HttpDurations", "StepName", "ElementIdentification/Description"};
        final List<String> header = Arrays.asList(headerValues);
        final String[] clientPerformanceAttributes = this.storeValues(clientPerformanceEle, docStateTrackerEle, elementIdentification, stepName);
        final List<String> clientPerformanceAttributesValues = Arrays.asList(clientPerformanceAttributes);
        this.finalisedClientPerformanceAttributes.add(clientPerformanceAttributesValues);
        FileUtil.writeCsvFile(fileName, this.finalisedClientPerformanceAttributes, header);
    }

    private String[] storeValues(final WebElement clientPerformanceEle, final WebElement docStateTrackerEle, final String description, final String stepname) {
        this.domLoadTime = this.ParseDouble(clientPerformanceEle.getAttribute("domloadtime"));
        this.domInteractiveTime = this.ParseDouble(clientPerformanceEle.getAttribute("dominteractivetime"));
        this.netWorkDelay = this.ParseDouble(clientPerformanceEle.getAttribute("networkdelay"));
        this.pageLoadTime = this.ParseDouble(clientPerformanceEle.getAttribute("pageloadtime"));
        this.totalPageLoadTime = this.ParseDouble(clientPerformanceEle.getAttribute("totalpageloadtime"));
        this.dataDstBusy = this.ParseDouble(docStateTrackerEle.getAttribute("data-dst-busy-duration"));
        this.elementIdentification = escapeComma(description);
        this.dataDstHttp = docStateTrackerEle.getAttribute("data-dst-http-durations");
        this.dataDstHttp = escapeComma(this.dataDstHttp);
        this.stepDescription = stepname;
        final String[] clientPerformanceAttributes = {Double.toString(this.domLoadTime), Double.toString(this.domInteractiveTime), Double.toString(this.netWorkDelay), Double.toString(this.pageLoadTime), Double.toString(this.totalPageLoadTime), Double.toString(this.dataDstBusy), this.dataDstHttp, this.stepDescription, this.elementIdentification};
        return clientPerformanceAttributes;
    }

    private static String escapeComma(final String str) {
        return "\"" + str + "\"";
    }

    double ParseDouble(final String strNumber) {
        if (strNumber != null && strNumber.length() > 0) {
            try {
                return Double.parseDouble(strNumber);
            } catch (Exception e) {
                return -1.0;
            }
        }
        return 0.0;
    }
}
