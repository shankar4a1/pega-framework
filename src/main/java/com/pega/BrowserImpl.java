

package com.pega;

import com.pega.framework.*;
import com.pega.framework.elmt.*;
import com.pega.page.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;
import java.util.function.*;

public class BrowserImpl implements Browser {
    private static final String VERSION = "$Id: BrowserImpl.java 209311 2016-09-30 12:55:53Z AnilBattinapati $";
    private static final Logger LOGGER;
    protected PegaWebDriver pegaDriver;
    protected WebDriver driver;
    protected TestEnvironment testEnv;
    protected Boolean isIE9;
    protected Boolean isIE10;
    private Boolean isIE11;
    private String currentLoggedInUser;
    private String currentUserUncloned;

    static {
        LOGGER = LoggerFactory.getLogger(Browser.class.getName());
    }

    public BrowserImpl(final TestEnvironment testEnv) {
        this.pegaDriver = null;
        this.driver = null;
        this.testEnv = null;
        this.testEnv = testEnv;
        this.pegaDriver = testEnv.getPegaDriver();
        this.driver = testEnv.getPegaDriver().getDriver();
    }

    @Override
    public void setLoggedInUser(final String user) {
        if (user.matches(".*\\d*")) {
            this.currentLoggedInUser = user;
        }
    }

    @Override
    public String getLoggedInUser() {
        return this.currentLoggedInUser;
    }

    @Override
    public void setNonClonedUser(final String unClonedUser) {
        this.currentUserUncloned = unClonedUser;
    }

    @Override
    public String getUnClonedLoggedInUser() {
        return this.currentUserUncloned;
    }

    @Override
    public void login() {
        this.login(this.testEnv.getConfiguration().getSUTConfig().getUser(), this.testEnv.getConfiguration().getSUTConfig().getPwd());
    }

    @Override
    public void login(final String usr, final String pwd) {
        BrowserImpl.LOGGER.info("Logging in the user");
        final PegaWebElement userid = this.pegaDriver.findElement(By.id("txtUserID"));
        final PegaWebElement password = this.pegaDriver.findElement(By.id("txtPassword"));
        if (this.isFirefox() && this.testEnv.getConfiguration().getSUTConfig().isEnableFullScreenMode()) {
            this.enableFullScreenForFirefox();
        } else if ((this.isIE9() || this.isIE10() || this.isIE11()) && this.testEnv.getConfiguration().getSUTConfig().isEnableFullScreenMode()) {
            this.enableFullScreenForIE();
        }
        if (this.isIE10() || this.isIE9()) {
            userid.click(false);
            this.testEnv.getScriptExecutor().sendKeys(userid, usr);
            password.click(false);
            this.testEnv.getScriptExecutor().sendKeys(password, pwd);
        } else {
            userid.click(false);
            userid.sendKeys(usr);
            password.click(false);
            password.sendKeys(pwd);
        }
        this.pegaDriver.findElement(By.name("pyActivity=Code-Security.Login")).click(false);
        this.pegaDriver.handleWaits().waitForPageLoaded();
        this.closeOpenedTabs();
        this.pegaDriver.getDefaultFrameTabCntDiff(true);
        this.pegaDriver.waitForDocStateReady(3);
        this.pegaDriver.loadCustomScripts();
        this.setLoggedInUser(usr);
    }

    private void closeOpenedTabs() {
        if (this.testEnv.getConfiguration().getSUTConfig().isCloud()) {
            this.pegaDriver.findElement(By.cssSelector("a[aria-label='Currently open']")).click(false);
            this.pegaDriver.findElement(By.xpath(BrowserImpl.CLOSE_ALL_XPATH)).click();
        }
    }

    @Override
    public void loginAndChangePassword(final String usr, final String pwd, final String newPassword) {
        this.login(usr, pwd);
        final PegaWebElement curPasswordTxtFiled = this.pegaDriver.findElement(By.xpath("//input[contains(@name,'pyPwdOldText')]"));
        final PegaWebElement newPasswordTxtFiled = this.pegaDriver.findElement(By.xpath("//input[contains(@name,'pyPwdNew')]"));
        final PegaWebElement confirmPasswordTxtFiled = this.pegaDriver.findElement(By.xpath("//input[contains(@name,'pyPwdVerifyText')]"));
        if (this.isIE10() || this.isIE9()) {
            curPasswordTxtFiled.click(false);
            this.testEnv.getScriptExecutor().sendKeys(curPasswordTxtFiled, pwd);
            newPasswordTxtFiled.click(false);
            this.testEnv.getScriptExecutor().sendKeys(newPasswordTxtFiled, newPassword);
            confirmPasswordTxtFiled.click(false);
            this.testEnv.getScriptExecutor().sendKeys(confirmPasswordTxtFiled, newPassword);
        } else {
            curPasswordTxtFiled.click(false);
            curPasswordTxtFiled.sendKeys(pwd);
            newPasswordTxtFiled.click(false);
            newPasswordTxtFiled.sendKeys(newPassword);
            confirmPasswordTxtFiled.click(false);
            confirmPasswordTxtFiled.sendKeys(newPassword);
        }
        this.pegaDriver.findElement(By.xpath(BrowserImpl.CHANGE_PASSWORD_BUTTON_XPATH)).click();
        this.pegaDriver.handleWaits().waitForPageLoaded();
        this.pegaDriver.getDefaultFrameTabCntDiff(true);
        this.pegaDriver.waitForDocStateReady(3);
    }

    @Override
    public void logout() {
        BrowserImpl.LOGGER.info("Performing Log off operation");
        this.pegaDriver.switchTo().defaultContent();
        this.pegaDriver.get(this.testEnv.getConfiguration().getSUTConfig().getURL() + "/!Developer?pyActivity=Code-Security.EndSession");
        this.pegaDriver.handleWaits().waitForPageLoaded();
    }

    @Override
    public void open() {
        BrowserImpl.LOGGER.info("Opening the URL");
        this.open(this.testEnv.getConfiguration().getSUTConfig().getURL());
    }

    @Override
    public void open(final String url) {
        if (this.testEnv.getConfiguration().getPlatformDetails().isLinux()) {
            final Dimension d = new Dimension(1920, 1080);
            this.driver.manage().window().setSize(d);
            if (this.testEnv.getConfiguration().getSUTConfig().isEnableFullScreenMode()) {
                try {
                    Runtime.getRuntime().exec("xdotool key F11");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        final String device = System.getProperty("device", "desktop");
        final String platform = this.testEnv.getConfiguration().getPlatformDetails().getPlatform();
        if ((!"mobileapp".equalsIgnoreCase(device.trim()) && !"responsiveapp".equalsIgnoreCase(device.trim())) || (!"android".equalsIgnoreCase(platform) && !"ios".equalsIgnoreCase(platform))) {
            this.driver.get(url);
            this.setIsIE9();
            this.setIsIE10();
            this.setIsIE11();
        }
    }

    @Override
    public void close() {
        this.driver.close();
    }

    @Override
    public Object executeJavaScript(final String script) {
        return ((JavascriptExecutor) this.driver).executeScript(script);
    }

    @Override
    public void closeCurrentPopUpAndGoBackToDefaultWindow() {
        this.pegaDriver.handleWaits().sleep(2L);
        this.pegaDriver.close();
        this.testEnv.getBrowser().switchToWindow(1);
    }

    @Override
    public String getWindow(final int windowNo) {
        final Set<String> set = this.driver.getWindowHandles();
        BrowserImpl.LOGGER.debug("Current no. of windows are: " + set.size());
        if (windowNo <= set.size()) {
            final ArrayList<String> windows = new ArrayList<String>(set);
            return windows.get(windowNo - 1);
        }
        return null;
    }

    @Override
    public boolean switchToWindow(final int windowNo) {
        return this.switchToWindow(windowNo, true);
    }

    @Override
    public boolean switchToWindow(final int windowNo, final boolean waitForPageLoaded) {
        this.pegaDriver.handleWaits().waitForWindowPresence(windowNo);
        final String windowHandle = this.getWindow(windowNo);
        if (windowHandle != null) {
            BrowserImpl.LOGGER.debug("New window's handle: " + windowHandle);
            this.pegaDriver.switchTo().window(windowHandle);
            BrowserImpl.LOGGER.debug("Window handle after switching: " + this.pegaDriver.getWindowHandle());
            this.pegaDriver.handleWaits().sleep(1L);
            if (waitForPageLoaded) {
                this.pegaDriver.handleWaits().waitForPageLoaded();
            }
            this.pegaDriver.handleWaits().sleep(1L);
            this.pegaDriver.handleWaits().sleep(1L);
            BrowserImpl.LOGGER.info("Switching successful to window no: " + windowNo);
            return true;
        }
        BrowserImpl.LOGGER.error("Switching could not be done to window no: " + windowNo, true);
        return false;
    }

    @Override
    public boolean switchToWindow(final String title) {
        boolean isSwitchingSuccessful = false;
        final Set<String> set = this.pegaDriver.getWindowHandles();
        final Iterator<String> iter = set.iterator();
        while (iter.hasNext()) {
            this.pegaDriver.switchTo().window(iter.next());
            this.pegaDriver.handleWaits().waitForPageLoaded();
            this.pegaDriver.handleWaits().sleep(1L);
            if (this.pegaDriver.getTitle().contains(title)) {
                isSwitchingSuccessful = true;
                break;
            }
        }
        return isSwitchingSuccessful;
    }

    @Override
    public String getTenantIdentifier() {
        return this.pegaDriver.findElement(By.xpath("//footer/p/span[1]")).getText();
    }

    @Override
    public String getPRPCVersion() {
        return this.pegaDriver.findElement(By.xpath("//span[contains(text(),'Pega')]")).getText();
    }

    @Override
    public boolean isIE9() {
        return this.isIE9;
    }

    private void enableFullScreenForFirefox() {
        this.testEnv.getPegaDriver().manage().window().maximize();
        this.testEnv.getMouse().moveTo(100, 5);
        this.testEnv.getMouse().click();
        this.testEnv.getMouse().moveTo(100, 100);
        this.testEnv.getPegaDriver().handleWaits().sleep(2L);
        this.testEnv.getKeyboard().keyPressAndRelease(Keys.F11);
        this.testEnv.getPegaDriver().handleWaits().sleep(2L);
    }

    private void enableFullScreenForIE() {
        try {
            final Wait<WebDriver> wait = new WebDriverWait(this.driver, 30L);
            wait.until((Function) this.checkFullScreenStatus());
        } catch (TimeoutException te) {
            BrowserImpl.LOGGER.error("Could not do a full screen mode after timeout. Continuing with available resolution", true);
        }
    }

    private com.google.common.base.Function<WebDriver, Boolean> checkFullScreenStatus() {
        return new com.google.common.base.Function<WebDriver, Boolean>() {
            public Boolean apply(final WebDriver driver) {
                try {
                    long browserHeight = (long) BrowserImpl.this.testEnv.getScriptExecutor().executeJavaScript("return window.innerHeight");
                    final long screenHeight = (long) BrowserImpl.this.testEnv.getScriptExecutor().executeJavaScript("return screen.height");
                    BrowserImpl.LOGGER.debug("Browser Height: " + browserHeight + "\nScreenHeight: " + screenHeight);
                    if (screenHeight - browserHeight > 30L) {
                        BrowserImpl.this.testEnv.getPegaDriver().manage().window().maximize();
                        BrowserImpl.this.testEnv.getMouse().moveTo(100, 5);
                        BrowserImpl.this.testEnv.getMouse().click();
                        BrowserImpl.this.pegaDriver.handleWaits().sleep(2L);
                        BrowserImpl.this.testEnv.getMouse().moveTo(100, 100);
                        BrowserImpl.this.pegaDriver.findElement(By.tagName("body")).sendKeys(Keys.F11);
                        BrowserImpl.this.pegaDriver.handleWaits().sleep(2L);
                        browserHeight = (long) BrowserImpl.this.testEnv.getScriptExecutor().executeJavaScript("return window.innerHeight");
                    }
                    BrowserImpl.LOGGER.debug("Browser Height: " + browserHeight + "\nScreenHeight: " + screenHeight);
                    return screenHeight - browserHeight <= 30L;
                } catch (Exception e) {
                    BrowserImpl.LOGGER.error("Browser might not be loaded yet. JavaScript execution failed" + e.getMessage());
                    return false;
                }
            }
        };
    }

    private void setIsIE10() {
        this.isIE10 = (Boolean) this.testEnv.getScriptExecutor().executeJavaScript("if (navigator.appVersion.indexOf(\"MSIE 10\") != -1){return true;}else{return false;}");
    }

    private void setIsIE9() {
        this.isIE9 = (Boolean) this.testEnv.getScriptExecutor().executeJavaScript("if(window.requestAnimationFrame){return false;}else{return true;}");
    }

    private void setIsIE11() {
        this.isIE11 = (Boolean) this.testEnv.getScriptExecutor().executeJavaScript("if (navigator.appVersion.indexOf(\"Trident\") != -1){return true;}else{return false;}");
        BrowserImpl.LOGGER.debug("setIsIE11() output:" + this.isIE11);
    }

    @Override
    public boolean isIE10() {
        return this.isIE10;
    }

    @Override
    public boolean isIE11() {
        return this.isIE11;
    }

    @Override
    public boolean isFirefox() {
        return this.testEnv.getConfiguration().getBrowserConfig().getBrowserName().equalsIgnoreCase("firefox");
    }

    @Override
    public void refresh() {
        this.pegaDriver.navigate().refresh();
        this.pegaDriver.loadCustomScripts();
        this.pegaDriver.getDefaultFrameTabCntDiff(true);
    }

    @Override
    public <T extends Portal> T getPortal(final Class<T> type) {
        return null;
    }

    @Override
    public <T extends Portal> T getPortal(final Class<T> type, final Frame frame) {
        return null;
    }

    @Override
    public String getDownloadDir() {
        String downloads_path;
        if (this.testEnv.getConfiguration().getPlatformDetails().isWindows()) {
            downloads_path = System.getProperty("user.home") + "/Downloads/";
        } else {
            downloads_path = System.getenv("HOME") + "/Downloads/";
        }
        return downloads_path;
    }
}
