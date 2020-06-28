

package com.pega.testng.listeners;

import com.pega.*;
import com.pega.testng.*;
import com.pega.testng.exceptions.*;
import com.pega.util.*;
import org.apache.commons.io.*;
import org.openqa.selenium.*;
import org.testng.*;
import org.testng.internal.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

public class PegaTestExecutionListener extends TestListenerAdapter {
    private static final String VERSION = "$Id: PegaTestExecutionListener.java 121818 2015-01-26 07:18:23Z SachinVellanki $";
    private WebDriver driver;
    boolean recordTest;

    public PegaTestExecutionListener() {
        this.driver = null;
        this.recordTest = false;
    }

    public void onStart(final ITestContext arg0) {
        super.onStart(arg0);
        Reporter.log("Total Test Count: " + arg0.getSuite().getAllMethods().size(), true);
        final Map<String, Collection<ITestNGMethod>> map = arg0.getSuite().getMethodsByGroups();
        final Set<Map.Entry<String, Collection<ITestNGMethod>>> set = map.entrySet();
        for (final Map.Entry<String, Collection<ITestNGMethod>> entry : set) {
            Reporter.log("Group: " + entry.getKey() + "---> methods: " + entry.getValue().size(), true);
        }
    }

    public void onFinish(final ITestContext arg0) {
        super.onFinish(arg0);
    }

    public void beforeConfiguration(final ITestResult tr) {
        if (!LegacyTestBase.class.isAssignableFrom(tr.getTestClass().getRealClass())) {
            throw new PegaTestNGSkipException("Test class: " + tr.getTestClass().getName() + " must extend com.pega.testng.TestBase class inorder " + "to execute the tests. Tests of current test class are being skipped");
        }
        if (tr.getMethod().isBeforeClassConfiguration() && tr.getMethod().getMethodName().equalsIgnoreCase("aaaSetUp")) {
            this.startRecording(tr);
        }
    }

    public void onConfigurationSuccess(final ITestResult itr) {
        super.onConfigurationSuccess(itr);
        if (itr.getMethod().isAfterClassConfiguration() && itr.getMethod().getMethodName().equals("zzzTearDown")) {
            this.stopRecordingAndSave(itr);
        }
    }

    public void onConfigurationSkip(final ITestResult itr) {
        super.onConfigurationSkip(itr);
        if (itr.getMethod().isAfterClassConfiguration() && itr.getMethod().getMethodName().equalsIgnoreCase("zzzTearDown")) {
            this.stopRecordingAndSave(itr);
        }
    }

    public void onConfigurationFailure(final ITestResult itr) {
        super.onConfigurationFailure(itr);
        this.takeScreenshot(itr);
        if (itr.getMethod().isAfterClassConfiguration() && itr.getMethod().getMethodName().equalsIgnoreCase("zzzTearDown")) {
            this.stopRecordingAndSave(itr);
        }
    }

    public void onTestStart(final ITestResult arg0) {
        if (!LegacyTestBase.class.isAssignableFrom(arg0.getTestClass().getRealClass())) {
            throw new PegaTestNGSkipException("Test class: " + arg0.getTestClass().getName() + " must extend com.pega.testng.TestBase class inorder " + "to execute the tests. Tests of current test class are being skipped");
        }
        final ConstructorOrMethod constOrMeth = arg0.getMethod().getConstructorOrMethod();
        final Method method = constOrMeth.getMethod();
        final TestInfo testInfo = method.getAnnotation(TestInfo.class);
        if (testInfo == null) {
            throw new PegaTestNGException("@TestInfo annotation is not declared on Test method");
        }
        Reporter.log("PMF Test Case ID for test method: " + arg0.getName() + " is : " + testInfo.tcid(), true);
        Reporter.log("Execution started for test: " + arg0.getName(), 20000, true);
    }

    public void onTestSuccess(final ITestResult arg0) {
        Reporter.log("Test '" + arg0.getName() + "' has passed...", 20000, true);
        final ConstructorOrMethod constOrMeth = arg0.getMethod().getConstructorOrMethod();
        final Method method = constOrMeth.getMethod();
        final TestInfo testInfo = method.getAnnotation(TestInfo.class);
        this.recordTest = testInfo.recordTest();
    }

    public void onTestSkipped(final ITestResult arg0) {
        super.onTestSkipped(arg0);
        Reporter.log("Test '" + arg0.getName() + "' has skipped...", 20000, true);
    }

    public void onTestFailure(final ITestResult arg0) {
        super.onTestFailure(arg0);
        Reporter.log("Test '" + arg0.getName() + "' has failed...", 20000, true);
        this.takeScreenshot(arg0);
    }

    private void takeScreenshot(final ITestResult arg0) {
        try {
            if (LegacyTestBase.class.isAssignableFrom(arg0.getTestClass().getRealClass())) {
                final LegacyTestBase testBase = (LegacyTestBase) arg0.getInstance();
                final TestEnvironment testEnv = testBase.getTestEnv();
                if (testEnv != null && testEnv.getPegaDriver() != null) {
                    this.driver = testEnv.getPegaDriver().getDriver();
                    if (this.driver != null) {
                        final File f = ((TakesScreenshot) this.driver).getScreenshotAs(OutputType.FILE);
                        final String className = arg0.getTestClass().getRealClass().getSimpleName();
                        final String methodName = arg0.getName();
                        final String imageName = className + "-" + methodName + ".jpg";
                        String dirName = arg0.getTestContext().getSuite().getOutputDirectory();
                        if (dirName == null) {
                            dirName = "test-output";
                        }
                        final String imageFilePath = dirName + System.getProperty("file.separator") + "Screenshots" + System.getProperty("file.separator") + imageName;
                        FileUtils.copyFile(f, new File(imageFilePath));
                        Reporter.log("Screenshot for test/configuration: " + arg0.getName() + " saved at : " + imageFilePath, 20000, true);
                    }
                } else {
                    Reporter.log("Unable to take screenshot for failed test : " + arg0.getName() + " as the required objects are not instantiated", 40000, true);
                }
            } else {
                Reporter.log("Unable to take screenshot for failed test : " + arg0.getName() + " as the test class is not extending TestBase class", 40000, true);
            }
        } catch (Exception e) {
            Reporter.log("Unable to take screenshot for failed test : " + arg0.getName(), 40000, true);
        }
    }

    private void startRecording(final ITestResult arg0) {
        try {
            final String className = arg0.getTestClass().getRealClass().getSimpleName();
            final String methodName = arg0.getName();
            String dirName = arg0.getTestContext().getSuite().getOutputDirectory();
            if (dirName == null) {
                dirName = "test-output";
            }
            final String dirPath = dirName + System.getProperty("file.separator") + "Videos";
            final String fileName = className + "-" + methodName;
            arg0.setAttribute("RECORD.FILE.PATH", dirPath + System.getProperty("file.separator") + fileName + ".avi");
            RecorderUtil.startRecording(dirPath, fileName);
            Reporter.log("Recording started for test : " + arg0.getName(), true);
        } catch (Exception e) {
            Reporter.log("Unable to record execution of  test : " + arg0.getMethod().getMethodName(), 40000, true);
        }
    }

    private void stopRecordingAndSave(final ITestResult arg0) {
        try {
            RecorderUtil.stopRecording();
            Reporter.log("Execution video for test: " + arg0.getMethod().getMethodName() + " saved successfully...", true);
        } catch (Exception e) {
            Reporter.log("Unable to save execution recording of test: " + arg0.getMethod().getMethodName(), 40000, true);
        }
    }

    private void stopRecordingandDelete(final ITestResult arg0) {
        try {
            RecorderUtil.stopRecording();
            final File file = new File(arg0.getAttribute("RECORD.FILE.PATH").toString());
            if (file.exists()) {
                Reporter.log("Deleting execution video for test: " + arg0.getMethod().getMethodName() + "...", true);
                try {
                    file.delete();
                } catch (Exception e) {
                    Reporter.log("Unable to delete execution video for test: " + arg0.getMethod().getMethodName() + "...", true);
                }
            }
        } catch (Exception e2) {
            Reporter.log("Unable to save execution recording of test: " + arg0.getMethod().getMethodName(), 40000, true);
        }
    }
}
