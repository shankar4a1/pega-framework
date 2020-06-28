package com.pega.utility;

import com.google.inject.*;
import com.pega.*;
import com.pega.framework.*;
import io.cucumber.guice.*;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.*;

import java.io.*;
import java.util.*;

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
