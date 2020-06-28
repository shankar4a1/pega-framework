

package com.pega.util;

import com.pega.*;
import com.pega.exceptions.*;
import org.apache.commons.exec.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;

public class AppiumHandler {
    private Configuration config;
    private static final Logger LOGGER;

    static {
        LOGGER = LoggerFactory.getLogger(AppiumHandler.class);
    }

    public AppiumHandler(final TestEnvironment testEnv) {
        this.config = testEnv.getConfiguration();
    }

    public void startAppium(final String appiumNodePath, final String appiumJsPath, final String appiumLogsPath, final String appiumServerAdress, final String appiumPort) throws IOException, InterruptedException {
        DefaultExecutor executor = null;
        if (this.config.getPlatformDetails().isWindows()) {
            AppiumHandler.LOGGER.debug("********Starting Appium SERVER ********");
            final CommandLine command = new CommandLine("cmd");
            command.addArgument("cmd");
            command.addArgument("/c");
            command.addArgument(appiumNodePath);
            command.addArgument(appiumJsPath);
            command.addArgument("--address");
            command.addArgument(appiumServerAdress);
            command.addArgument("--log");
            command.addArgument(appiumLogsPath);
            final DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
            executor = new DefaultExecutor();
            final ExecuteWatchdog watchdog = new ExecuteWatchdog(1000000L);
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
            executor.setStreamHandler(streamHandler);
            executor.setWatchdog(watchdog);
            executor.setExitValue(1);
            executor.execute(command, resultHandler);
            do {
                Thread.sleep(5000L);
            } while (!this.isProcessRunning("node.exe") && !this.isProcessRunning("adb.exe"));
            Thread.sleep(5000L);
            int counter = 0;
            final String output = outputStream.toString();
            do {
                Thread.sleep(5000L);
                ++counter;
            } while (!output.contains("listener started on") && !output.contains("File LogLevel: debug") && counter < 12);
            if (counter >= 12) {
                throw new RuntimeException(output);
            }
            AppiumHandler.LOGGER.debug("********APPIUM SERVER STARTED********");
        } else {
            if (!this.config.getPlatformDetails().isMac() || !"ios".equalsIgnoreCase(this.config.getPlatformDetails().getPlatform())) {
                AppiumHandler.LOGGER.info("*************APPIUM SERVER MANUAL STARTUP*********\nPlease start your appium server locally as auto instantiation is not impletemented\n**********APPIUM SERVER MANUAL STARTUP********");
                throw new NotYetImplementedException();
            }
            AppiumHandler.LOGGER.debug("********Starting Appium SERVER ********");
            final CommandLine command = new CommandLine(appiumNodePath);
            command.addArgument(appiumJsPath);
            command.addArgument("--address");
            command.addArgument(appiumServerAdress);
            command.addArgument("--port");
            command.addArgument(appiumPort);
            command.addArgument("--log");
            command.addArgument(appiumLogsPath);
            final DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
            executor = new DefaultExecutor();
            executor.setExitValue(1);
            executor.execute(command, resultHandler);
            do {
                Thread.sleep(2000L);
            } while (!this.isProcessRunning("node"));
            AppiumHandler.LOGGER.info("********APPIUM SERVER STARTED********");
        }
    }

    public void stopAppium() throws IOException {
        DefaultExecutor executor = null;
        if (this.config.getPlatformDetails().isWindows()) {
            if (this.isProcessRunning("node.exe")) {
                final CommandLine command = new CommandLine("cmd");
                command.addArgument("/c");
                command.addArgument("taskkill");
                command.addArgument("/F");
                command.addArgument("/IM");
                command.addArgument("node.exe");
                final DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
                executor = new DefaultExecutor();
                executor.setExitValue(1);
                executor.execute(command, resultHandler);
            }
            if (this.isProcessRunning("adb.exe")) {
                final CommandLine command = new CommandLine("cmd");
                command.addArgument("/c");
                command.addArgument("taskkill");
                command.addArgument("/F");
                command.addArgument("/IM");
                command.addArgument("adb.exe");
                final DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
                executor = new DefaultExecutor();
                executor.setExitValue(1);
                executor.execute(command, resultHandler);
            }
        } else {
            if (!this.config.getPlatformDetails().isMac() || !"ios".equalsIgnoreCase(this.config.getPlatformDetails().getPlatform())) {
                AppiumHandler.LOGGER.info("*************APPIUM SERVER MANUAL STARTUP*********\nPlease start your appium server locally as auto instantiation is not impletemented\n**********APPIUM SERVER MANUAL STARTUP********");
                throw new NotYetImplementedException();
            }
            if (this.isProcessRunning("node")) {
                final ArrayList<String> command2 = new ArrayList<String>();
                command2.add("/bin/sh");
                command2.add("-c");
                command2.add("/usr/bin/killall -9 node");
                final ProcessBuilder pb = new ProcessBuilder(command2);
                pb.start();
                AppiumHandler.LOGGER.debug("Appium node process stopped");
            }
            if (this.isProcessRunning("ios_webkit_debug_proxy")) {
                final ArrayList<String> command2 = new ArrayList<String>();
                command2.add("/bin/sh");
                command2.add("-c");
                command2.add("/usr/bin/killall -9 ios_webkit_debug_proxy");
                final ProcessBuilder pb = new ProcessBuilder(command2);
                pb.start();
                AppiumHandler.LOGGER.debug("IOS proxy process stopped");
            }
        }
    }

    private boolean isProcessRunning(final String process) throws IOException {
        String pidInfo = "";
        if (this.config.getPlatformDetails().isWindows()) {
            final Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
            final BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                pidInfo = pidInfo + line;
            }
            input.close();
            if (pidInfo.contains(process)) {
                AppiumHandler.LOGGER.debug(process + " Server running");
                return true;
            }
            AppiumHandler.LOGGER.debug(process + "Server not running");
            return false;
        } else {
            if (!this.config.getPlatformDetails().isMac() || !"ios".equalsIgnoreCase(this.config.getPlatformDetails().getPlatform())) {
                throw new NotYetImplementedException();
            }
            final ArrayList<String> command = new ArrayList<String>();
            command.add("/bin/sh");
            command.add("-c");
            command.add("ps -ef|grep " + process + "|wc -l");
            final ProcessBuilder pb = new ProcessBuilder(command);
            final Process p2 = pb.start();
            final BufferedReader input2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
            final String xyz = input2.readLine();
            input2.close();
            final int i = Integer.parseInt(xyz.trim());
            if (i > 1) {
                AppiumHandler.LOGGER.debug(i + " " + process + " processes are running");
                return true;
            }
            AppiumHandler.LOGGER.debug(process + " Server not running");
            return false;
        }
    }

    public void startProxy(final String iOSDeviceUDID) throws IOException {
        if (this.config.getPlatformDetails().isMac() && "ios".equalsIgnoreCase(this.config.getPlatformDetails().getPlatform())) {
            final ArrayList<String> command = new ArrayList<String>();
            command.add("/bin/sh");
            command.add("-c");
            command.add("/usr/local/bin/ios_webkit_debug_proxy -c " + iOSDeviceUDID + ":27753 -d");
            final ProcessBuilder pb = new ProcessBuilder(command);
            final Process p = pb.inheritIO().start();
            final BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            AppiumHandler.LOGGER.debug("&&&& Proxy details &&&&");
            String line;
            while ((line = input.readLine()) != null) {
                AppiumHandler.LOGGER.debug("iOS Proxy log : " + line + "&&&&");
            }
            do {
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (!this.isProcessRunning("ios_webkit_debug_proxy"));
            AppiumHandler.LOGGER.info("############# IOS proxy started #############");
        }
    }
}
