package com.pega.demo.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.pega.TestEnvironment;
import com.pega.demo.MyAppBrowser;
import com.pega.demo.MyAppTestEnvironment;
import com.pega.framework.PegaWebDriver;

import io.cucumber.guice.ScenarioScoped;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.Reportable;

@ScenarioScoped
public class ReportGenerator {

    TestEnvironment testEnv;
    MyAppBrowser browser;
    private PegaWebDriver pegaDriver;



    @Inject
    public ReportGenerator(MyAppTestEnvironment testEnv) {
        this.testEnv = testEnv;
        pegaDriver = testEnv.getPegaDriver();
        browser = (MyAppBrowser) testEnv.getBrowser();

    }


    public static void main(String[] args) {
        run();

    }

    public static void run() {

        File reportOutputDirectory = new File(System.getProperty("user.dir")+"\\target\\");
        List<String> jsonFiles = new ArrayList<String>();
        jsonFiles.add(System.getProperty("user.dir") + "\\target\\cucumber-report.json");

        String projectName = "Demo";
        Configuration configuration = new Configuration(reportOutputDirectory, projectName);
        configuration.addClassifications("Platform", "Windows");
        configuration.addClassifications("Browser", "Chrome80");
        configuration.addClassifications("Environment", "SIT");

        configuration.addClassifications("URL", "https://sales-automation-trial83-prd.pegacloud.io/prweb/sso/ai2rRSYKJFcQR6ODxs26RtpopHM675OVM_urYrbHt9Q1YdD1UuP-r6w0Aj860bqSkiQ3N9IrgfE%28*/!STANDARD");

      ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
      reportBuilder.generateReports();
    }
}
