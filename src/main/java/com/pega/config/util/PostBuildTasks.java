

package com.pega.config.util;

import com.pega.exceptions.*;
import org.apache.commons.io.*;
import org.testng.*;
import org.w3c.dom.*;
import org.xml.sax.*;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.*;
import javax.mail.internet.*;
import javax.xml.parsers.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class PostBuildTasks {
    String COPYRIGHT;
    private static final String VERSION = "$Id: PostBuildTasks.java 208709 2016-09-13 06:53:08Z PrashantSammeta $";
    private String jenkinsURL;
    private String teamName;
    private String buildId;
    private String buildURL;
    private String instanceURL;
    private Properties executionProperties;
    private String prpcBuild;
    private String browser;
    private String tags;
    private boolean isTeamJobExecution;
    private double totalTests;
    private double passedTests;
    private double failedTests;
    private double skippedTests;
    private String status;
    private String failureAnalysisURL;
    private String jobName;
    private String additionalEmailIds;
    private String excludePrpcDetails;
    private String host;
    private String fromEmailAddress;
    private String fromPassword;
    private String port;
    private boolean isDevJenkins;
    private boolean isProdJenkins;
    private boolean isMIExcluded;
    private String customMessage;
    private String ccList;
    private String customSignature;
    private String analyzeLinkURL;
    private boolean isFAIgnored;
    private String color;

    public PostBuildTasks() {
        this.COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
        this.color = "white";
    }

    public static void main(final String[] args) throws IOException, URISyntaxException, ParserConfigurationException, SAXException {
        final PostBuildTasks tasks = new PostBuildTasks();
        Reporter.log("Perfomring Post Build Tasks...", true);
        tasks.initVars();
        if (tasks.jenkinsURL == null) {
            Reporter.log("This call is not made from jenkins...", true);
            return;
        }
        tasks.sendRequests();
        tasks.sendEmail();
        tasks.cleanup();
    }

    private void cleanup() {
        final File buildDir = new File(System.getProperty("user.dir") + File.separator + System.getenv("JOB_NAME") + "_" + System.getenv("BUILD_NUMBER"));
        Reporter.log("Build Dir path: " + buildDir.getAbsolutePath(), true);
        if (buildDir.exists()) {
            System.out.println("Deleting build directory: " + buildDir);
            try {
                FileUtils.deleteDirectory(buildDir);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Unable to delete build directory: " + buildDir);
            }
        }
        final File logFile = new File(System.getProperty("user.dir") + File.separator + "log-" + System.getenv("BUILD_NUMBER") + ".txt");
        Reporter.log("Log file path: " + logFile.getAbsolutePath(), true);
        if (logFile.exists()) {
            System.out.println("Deleting log file: " + logFile);
            logFile.delete();
        }
    }

    private void initVars() throws IOException, ParserConfigurationException, SAXException {
        Reporter.log("Initializing variables...", true);
        this.jenkinsURL = System.getenv("JENKINS_URL");
        this.jenkinsURL = (this.jenkinsURL.endsWith("/") ? this.jenkinsURL.substring(0, this.jenkinsURL.length() - 1) : this.jenkinsURL);
        this.teamName = System.getenv("team.name");
        if (this.teamName == null) {
            Reporter.log("team.name property is not set...");
            this.teamName = "";
        }
        this.jobName = System.getenv("JOB_NAME");
        this.buildId = System.getenv("BUILD_NUMBER");
        this.buildURL = System.getenv("BUILD_URL");
        if (this.buildId == null) {
            this.buildId = "-";
        }
        this.instanceURL = System.getenv("instance.url");
        this.executionProperties = new Properties();
        final File f = new File("execution.properties");
        if (f.exists()) {
            this.executionProperties.load(new FileInputStream("execution.properties"));
            this.prpcBuild = this.executionProperties.getProperty("build.name");
        }
        if (this.instanceURL == null) {
            this.instanceURL = this.executionProperties.getProperty("prpc.url");
        }
        this.browser = System.getenv("browser.name");
        this.tags = System.getenv("tags");
        final String teamJobRun = System.getProperty("team.job.run");
        if (teamJobRun == null) {
            Reporter.log("team.job.run property is not set... assuming that it is not a team job...", true);
            this.isTeamJobExecution = false;
        } else this.isTeamJobExecution = teamJobRun.trim().equalsIgnoreCase("true");
        this.tags = System.getenv("tags");
        this.getTestStatistics();
        if (this.totalTests < 0.0 || this.passedTests < 0.0 || this.failedTests < 0.0 || (this.totalTests == 0.0 && this.passedTests == 0.0 && this.failedTests == 0.0)) {
            this.status = Status.FAILED_CONFIGURATION.getValue();
        } else if (this.failedTests > 0.0) {
            this.status = Status.UNSTABLE.getValue();
        } else {
            this.status = Status.PASSED.getValue();
        }
        final String exclFailureAnalysis = System.getProperty("exclude.fa");
        if (exclFailureAnalysis != null && exclFailureAnalysis.trim().equalsIgnoreCase("true")) {
            Reporter.log("Failure Analysis tool integration is ignored on request...", true);
            this.isFAIgnored = true;
        } else {
            this.isFAIgnored = false;
        }
        this.failureAnalysisURL = System.getProperty("failure.analysis.url");
        this.additionalEmailIds = System.getProperty("mail.ids");
        this.excludePrpcDetails = System.getProperty("exclude.prpc.details");
        this.fromEmailAddress = System.getProperty("from.email.id");
        this.fromPassword = System.getProperty("from.password");
        this.host = System.getProperty("host");
        this.port = System.getProperty("port");
        final String excludeMIFromMailList = System.getProperty("excl.mi.from.mail.list");
        this.customMessage = System.getProperty("custom.message");
        this.ccList = System.getProperty("cc.mail.ids");
        this.customSignature = System.getProperty("custom.signature");
        if (this.host == null) {
            this.host = "10.61.5.218";
            Reporter.log("Host is set to default value...", true);
        }
        if (this.fromEmailAddress == null) {
            Reporter.log("Email will be sent from default email id...", true);
            this.fromEmailAddress = "noreply@missionimpossible.com";
        } else if (this.fromPassword == null) {
            Reporter.log("WARN: From password is not set. Email might not be sent if the given from email requirees password...", true);
        }
        if (excludeMIFromMailList != null && excludeMIFromMailList.trim().equalsIgnoreCase("true")) {
            this.isMIExcluded = true;
        }
        this.isProdJenkins = this.jenkinsURL.toLowerCase().contains("quality");
        this.isDevJenkins = (this.jenkinsURL.toLowerCase().contains("wprpcw7hyd") && this.jenkinsURL.toLowerCase().contains("8080"));
        Reporter.log("Total Tests: " + this.totalTests, true);
        Reporter.log("Passed Tests: " + this.passedTests, true);
        Reporter.log("Failed Tests: " + this.failedTests, true);
        Reporter.log("Skipped Tests: " + this.skippedTests, true);
        Reporter.log("Status: " + this.status, true);
    }

    private void getTestStatistics() throws ParserConfigurationException, SAXException, IOException {
        try {
            final String url = this.buildURL + "testReport/api/xml?pretty=true";
            System.out.println("Reports URL: " + url);
            final DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            final Document dom = db.parse(url);
            final Element docEle = dom.getDocumentElement();
            final Element failCount = (Element) docEle.getElementsByTagName("failCount").item(0);
            final Element passCount = (Element) docEle.getElementsByTagName("passCount").item(0);
            final Element skipCount = (Element) docEle.getElementsByTagName("skipCount").item(0);
            this.failedTests = Integer.parseInt(failCount.getTextContent());
            this.passedTests = Integer.parseInt(passCount.getTextContent());
            this.skippedTests = Integer.parseInt(skipCount.getTextContent());
            this.totalTests = this.failedTests + this.passedTests + this.skippedTests;
        } catch (Exception e) {
            Reporter.log("Unable to fetch Test results for this build", true);
            e.printStackTrace();
        }
    }

    private void sendRequests() throws IOException, URISyntaxException {
        if (this.isFAIgnored) {
            final File f = new File("Link.txt");
            if (f.exists()) {
                f.delete();
            }
        } else {
            Reporter.log("Sending request to failure analysis tool...", true);
            String jenkinURLParam = "";
            if (this.isDevJenkins) {
                this.failureAnalysisURL = "http://wvellsw7hyd:7070/722";
            } else if (this.failureAnalysisURL == null) {
                this.failureAnalysisURL = "http://quality:7070/722";
                if (!this.isProdJenkins) {
                    final URI uri = new URI(this.jenkinsURL);
                    jenkinURLParam = "&jenkinURL=" + uri.getHost() + ":" + uri.getPort();
                }
            }
            Reporter.log(jenkinURLParam);
            this.failureAnalysisURL = (this.failureAnalysisURL.endsWith("/") ? this.failureAnalysisURL : (this.failureAnalysisURL + "/"));
            this.analyzeLinkURL = this.failureAnalysisURL + "postval?job=" + this.jobName.replace(" ", "%20") + "&testRun=" + this.buildId;
            String getAllTestsURL = this.failureAnalysisURL + "getAll?job=" + this.jobName.replace(" ", "%20") + "&testRun=" + this.buildId;
            final String backUpURL = this.failureAnalysisURL + "takeBackUp";
            if (this.isTeamJobExecution) {
                String scenarioNameParam = "";
                String scenarioName = System.getenv("scenario");
                if (scenarioName != null) {
                    if (scenarioName.contains(",")) {
                        scenarioName = scenarioName.substring(0, scenarioName.indexOf(44));
                    }
                    scenarioNameParam = "&scenarioName=" + scenarioName.replace(" ", "%20");
                }
                this.analyzeLinkURL = this.analyzeLinkURL + "&teamName=" + this.teamName + scenarioNameParam;
                getAllTestsURL = getAllTestsURL + "&teamName=" + this.teamName + scenarioNameParam;
            }
            final String[] urls = {this.analyzeLinkURL, getAllTestsURL, backUpURL};
            Reporter.log("Link to analyze the failed tests in this build: " + this.analyzeLinkURL, true);
            try {
                for (int i = 0; i < urls.length; ++i) {
                    final URL url = new URL(urls[i]);
                    final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(120000);
                    connection.setReadTimeout(120000);
                    connection.setRequestMethod("GET");
                    final int responseCode = connection.getResponseCode();
                    if (responseCode != 200) {
                        Reporter.log("Given URL: " + url + " is not available. It returned HTTP Code: " + responseCode, true);
                    } else {
                        Reporter.log("Request sent to URL: " + url, true);
                    }
                }
            } catch (Exception e) {
                Reporter.log("Error sending requests to Failure Analysis tool", 40000, true);
                e.printStackTrace();
            }
            PrintWriter out = null;
            try {
                final File f2 = new File("Link.txt");
                if (f2.exists()) {
                    f2.delete();
                }
                out = new PrintWriter(new FileOutputStream(f2));
                out.println("Failure Analysis_OLD\t" + this.analyzeLinkURL);
            } catch (Exception e2) {
                Reporter.log("Error writing to Link.txt file", 40000, true);
                e2.printStackTrace();
                return;
            } finally {
                out.close();
            }
            out.close();
        }
    }

    private void sendEmail() {
        Reporter.log("Sending Email...", true);
        final Set<String> recepientsList = this.fetchRecepientsList();
        final Properties properties = System.getProperties();
        properties.setProperty("java.net.preferIPv4Stack", "true");
        properties.setProperty("mail.smtp.host", this.host);
        if (this.port != null) {
            properties.setProperty("mail.smtp.port", this.port);
        }
        Session session;
        if (this.fromPassword != null) {
            properties.setProperty("mail.smtp.auth", "true");
            properties.setProperty("mail.smtp.starttls.enable", "true");
            session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(PostBuildTasks.this.fromEmailAddress, PostBuildTasks.this.fromPassword);
                }
            });
        } else {
            session = Session.getDefaultInstance(properties);
        }
        Reporter.log("From Email Address::" + this.fromEmailAddress, true);
        Reporter.log("From Email Password::" + this.fromPassword, true);
        try {
            final MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(this.fromEmailAddress));
            if (!recepientsList.contains("MissionImpossible@pega.com") && (this.isTeamJobExecution || (!this.isMIExcluded && !this.isDevJenkins))) {
                message.addRecipient(Message.RecipientType.CC, new InternetAddress("MissionImpossible@pega.com"));
            }
            if (this.ccList != null) {
                final String[] ids = this.ccList.split(",");
                for (int i = 0; i < ids.length; ++i) {
                    message.addRecipient(Message.RecipientType.CC, new InternetAddress(ids[i]));
                }
            }
            final Iterator<String> iter = recepientsList.iterator();
            while (iter.hasNext()) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(iter.next()));
            }
            if (this.customMessage != null) {
                message.setSubject(this.customMessage + "- Tests Execution Results");
            } else if (this.isProdJenkins || this.isDevJenkins) {
                message.setSubject("7.3, Job: " + this.jobName + ", Build: " + this.buildId + "- Tests Execution Results");
            } else {
                message.setSubject("Tests Execution Results");
            }
            message.setContent(this.getMailBody(), "text/html; charset=UTF-8");
            Reporter.log("Triggering Email....", true);
            Transport.send(message);
            Reporter.log("Mail sent successfully....", true);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    private Set<String> fetchRecepientsList() {
        final Set<String> recepientsList = new LinkedHashSet<String>();
        final String teamName = this.teamName.trim();
        if (teamName.equalsIgnoreCase("sachin")) {
            recepientsList.add("sachin.vellanki@in.pega.com");
        } else if (teamName.equalsIgnoreCase("viswa")) {
            recepientsList.add("sreekanta.viswa@in.pega.com");
        } else if (teamName.equalsIgnoreCase("pallavi")) {
            recepientsList.add("pallavi.ganesh@in.pega.com");
        } else if (teamName.equalsIgnoreCase("chanukya")) {
            recepientsList.add("Chanukya.Vempati@in.pega.com");
        } else if (teamName.equalsIgnoreCase("bala")) {
            recepientsList.add("BalaNaveenReddy.Kappeta@in.pega.com");
        } else if (teamName.equalsIgnoreCase("pavan")) {
            recepientsList.add("Pavan.Beri@in.pega.com");
        } else if (teamName.equalsIgnoreCase("saketh")) {
            recepientsList.add("SakethKumar.Shakkari@in.pega.com");
        } else if (teamName.equalsIgnoreCase("anil")) {
            recepientsList.add("Anil.Battinapati@in.pega.com");
        } else if (teamName.equalsIgnoreCase("srikar")) {
            recepientsList.add("VenkataSrikar.Vadlamudi@in.pega.com");
        } else if (teamName.equalsIgnoreCase("prashant")) {
            recepientsList.add("prashant.sammeta@in.pega.com");
        }
        if (recepientsList.size() == 0) {
            final Properties properties = new Properties();
            String filePath = "Data\\MailingList.properties";
            filePath = filePath.replace("\\", System.getProperty("file.separator"));
            final File f = new File(filePath);
            if (!f.exists()) {
                Reporter.log("Mailing list properties file is not found...", true);
            } else {
                try {
                    properties.load(new FileInputStream(f));
                } catch (Exception e) {
                    throw new PegaWebDriverException("Unable to load mailing list properties file");
                }
                String recepients;
                if (this.isTeamJobExecution) {
                    recepients = properties.getProperty(teamName.replace(" ", ""));
                } else {
                    recepients = properties.getProperty(this.jobName.replace(" ", ""));
                }
                if (recepients != null) {
                    if (recepients.contains(",")) {
                        final String[] mailIds = recepients.split(",");
                        for (int i = 0; i < mailIds.length; ++i) {
                            recepientsList.add(mailIds[i]);
                        }
                    } else {
                        recepientsList.add(recepients);
                    }
                }
            }
        }
        if (this.additionalEmailIds != null) {
            Reporter.log("The following email IDs will also be included: " + this.additionalEmailIds, true);
            final String[] mailIds2 = this.additionalEmailIds.split(",");
            for (int j = 0; j < mailIds2.length; ++j) {
                recepientsList.add(mailIds2[j]);
            }
        }
        Reporter.log("Email will be sent to: " + recepientsList, true);
        return recepientsList;
    }

    private String getMailBody() {
        String statusBGColor = "#04B404";
        if (this.status == Status.FAILED.getValue() || this.status == Status.FAILED_CONFIGURATION.getValue()) {
            statusBGColor = "#FA5858";
        } else if (this.status == Status.UNSTABLE.getValue()) {
            statusBGColor = "#f9cc06";
            this.color = "black";
        }
        final String header = "<!DOCTYPE html><html><head><style>table{border-collapse: collapse; float:left;} table,th,td{border: 1px solid black; width:50%; text-align:center;} th{font-style:italic;}th.header{background-color: #A4A4A4; color: white; font-style:normal;}</style></head>";
        final String jobDetails = "<table><tr><th colspan='2' class='header'>Job Details</th></tr><tr><th>Job Name</th><td>" + this.jobName + "</td></tr>" + "<tr><th>Build Number</th><td>" + "#" + this.buildId + "</td></tr>" + "<tr><th>Build Status</th><td style='background-color: " + statusBGColor + "; color: " + this.color + "; font-weight:bold;'>" + this.status + "</td></tr>" + "<tr><th>No. of Tests Passed</th><td style='color:#04B404;'>" + (int) this.passedTests + "</td></tr>" + "<tr><th>No. of Tests Failed</th><td style='color:#FA5858;'>" + (int) this.failedTests + "</td></tr>" + "<tr><th>No. of Tests Skipped</th><td style='color:#cccc00;'>" + (int) this.skippedTests + "</td></tr>" + "<tr><th>Total Tests Executed</th><td>" + (int) this.totalTests + "</td></tr>" + "</table>";
        if (this.instanceURL == null) {
            this.instanceURL = "-";
        }
        String envDetails = "<br/><table><tr><th colspan='2' class='header'>Environment Details</th></tr><tr><th>Instance URL</th><td>" + this.instanceURL + "</td></tr>";
        if (this.excludePrpcDetails == null) {
            if (this.prpcBuild == null) {
                this.prpcBuild = "-";
            }
            envDetails = envDetails + "<tr><th>PRPC Build</th><td>" + this.prpcBuild + "</td></tr>" + "<tr><th>PRPC Version</th><td>" + "Universal SMA v7.4" + "</td></tr>";
        }
        envDetails = envDetails + "<tr><th>Tags</th><td>" + this.tags + "</td></tr>" + "<tr><th>Browser</th><td>" + this.browser + "</td></tr>" + "</table>";
        double passPer;
        double failPer;
        double skipPer;
        if (this.totalTests != 0.0) {
            passPer = this.passedTests / this.totalTests * 100.0;
            failPer = this.failedTests / this.totalTests * 100.0;
            skipPer = this.skippedTests / this.totalTests * 100.0;
        } else {
            passPer = 0.0;
            failPer = 100.0;
            skipPer = 0.0;
        }
        passPer = (double) Math.round(passPer);
        failPer = (double) Math.round(failPer);
        skipPer = (double) Math.round(skipPer);
        final String chart = "<a href='" + this.buildURL + "'><img align='right' vspace='60%' hspace='30%' src='https://chart.googleapis.com/chart?cht=p&chs=350x200&chd=t:" + (int) passPer + "," + (int) failPer + "," + (int) skipPer + "&chl=Pass (" + passPer + "%)|Fail (" + failPer + "%)|Skip(" + skipPer + "%)&chco=33CC33,FF0000,CCCC00'/></a>";
        String text = header + "<body><div><p>";
        text = text + chart;
        text = text + "Hi, <br><br>Please find your execution details below:<br><br>" + jobDetails + envDetails + "<b>For Cucumber Reports:</b>" + "<a href=\"" + this.buildURL + "cucumber-html-reports/feature-overview.html\">Click here</a>" + "<br/><br/><b>For Build log:</b><a href=\"" + this.buildURL + "console\">Click Here</a>";
        if (!this.isFAIgnored) {
            text = text + "<br/><br/><b>For Failure Analysis:</b><a href=\"" + this.analyzeLinkURL + "\">Click Here</a>";
        }
        text = text + "<br/><br/><br>Thanks,<br>";
        if (this.customSignature == null) {
            text = text + "Mission Impossible";
        } else {
            text = text + this.customSignature;
        }
        text = text + "</p></div></body></html>";
        return text;
    }

    public enum Status {
        PASSED("PASSED", 0, "Passed"),
        FAILED("FAILED", 1, "Failed"),
        FAILED_CONFIGURATION("FAILED_CONFIGURATION", 2, "Configuration Failure"),
        UNSTABLE("UNSTABLE", 3, "Unstable");

        private String value;

        Status(final String name, final int ordinal, final String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }
}
