

package com.pega.framework;

import com.google.common.collect.*;
import io.appium.java_client.*;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;
import org.slf4j.*;

public class TouchActionsImpl extends TouchAction implements TouchActions {
    private static final Logger LOGGER;
    private static final String VERSION = "$Id: MouseImpl.java 121818 2015-01-26 07:18:23Z BalaKappeta $";
    private AppiumDriver driver;
    ImmutableList.Builder parameterBuilder;

    static {
        LOGGER = LoggerFactory.getLogger(TouchActions.class.getName());
    }

    public TouchActionsImpl(final MobileDriver driver) {
        super(driver);
        this.driver = (AppiumDriver) driver;
        this.parameterBuilder = ImmutableList.builder();
    }

    public TouchAction press(final WebElement el) {
        final ActionParameter action = new ActionParameter("press", (RemoteWebElement) el);
        this.parameterBuilder.add(action);
        return this;
    }

    public TouchAction press(final int x, final int y) {
        final ActionParameter action = new ActionParameter("press");
        action.addParameter("x", x);
        action.addParameter("y", y);
        this.parameterBuilder.add(action);
        return this;
    }

    public TouchAction press(final WebElement el, final int x, final int y) {
        final ActionParameter action = new ActionParameter("press", (RemoteWebElement) el);
        action.addParameter("x", x);
        action.addParameter("y", y);
        this.parameterBuilder.add(action);
        return this;
    }

    public TouchAction release() {
        final ActionParameter action = new ActionParameter("release");
        this.parameterBuilder.add(action);
        return this;
    }

    public TouchAction moveTo(final WebElement el) {
        final ActionParameter action = new ActionParameter("moveTo", (RemoteWebElement) el);
        this.parameterBuilder.add(action);
        return this;
    }

    public TouchAction moveTo(final int x, final int y) {
        final ActionParameter action = new ActionParameter("moveTo");
        action.addParameter("x", x);
        action.addParameter("y", y);
        this.parameterBuilder.add(action);
        return this;
    }

    public TouchAction moveTo(final WebElement el, final int x, final int y) {
        final ActionParameter action = new ActionParameter("moveTo", (RemoteWebElement) el);
        action.addParameter("x", x);
        action.addParameter("y", y);
        this.parameterBuilder.add(action);
        return this;
    }

    public TouchAction tap(final WebElement el) {
        final ActionParameter action = new ActionParameter("tap", (RemoteWebElement) el);
        this.parameterBuilder.add(action);
        return this;
    }

    public TouchAction tap(final int x, final int y) {
        final ActionParameter action = new ActionParameter("tap");
        action.addParameter("x", x);
        action.addParameter("y", y);
        this.parameterBuilder.add(action);
        return this;
    }

    public TouchAction tap(final WebElement el, final int x, final int y) {
        final ActionParameter action = new ActionParameter("tap", (RemoteWebElement) el);
        action.addParameter("x", x);
        action.addParameter("y", y);
        this.parameterBuilder.add(action);
        return this;
    }

    public TouchAction waitAction() {
        final ActionParameter action = new ActionParameter("wait");
        this.parameterBuilder.add(action);
        return this;
    }

    public TouchAction waitAction(final int ms) {
        final ActionParameter action = new ActionParameter("wait");
        action.addParameter("ms", ms);
        this.parameterBuilder.add(action);
        return this;
    }

    public TouchAction longPress(final WebElement el) {
        final ActionParameter action = new ActionParameter("longPress", (RemoteWebElement) el);
        this.parameterBuilder.add(action);
        return this;
    }

    public TouchAction longPress(final int x, final int y) {
        final ActionParameter action = new ActionParameter("longPress");
        action.addParameter("x", x);
        action.addParameter("y", y);
        this.parameterBuilder.add(action);
        return this;
    }

    public TouchAction longPress(final WebElement el, final int x, final int y) {
        final ActionParameter action = new ActionParameter("longPress", (RemoteWebElement) el);
        action.addParameter("x", x);
        action.addParameter("y", y);
        this.parameterBuilder.add(action);
        return this;
    }

    public void cancel() {
        final ActionParameter action = new ActionParameter("wait");
        this.parameterBuilder.add(action);
        this.perform();
    }

    public TouchAction perform() {
        this.driver.performTouchAction(this);
        return this;
    }

    public void swipe(final int startx, final int starty, final int endx, final int endy) {
        this.longPress(startx, starty).moveTo(endx, endy).release().perform();
    }

    public void swipe(final PegaWebElement element1, final PegaWebElement element2) {
        final int height1 = element1.getWebElement().getSize().getHeight();
        final int width1 = element1.getWebElement().getSize().getWidth();
        final int height2 = element2.getWebElement().getSize().getHeight();
        final int width2 = element2.getWebElement().getSize().getWidth();
        this.longPress(element1.getWebElement(), width1 / 2, height1 / 2).moveTo(element2.getWebElement(), width2 / 2, height2 / 2).release().perform();
    }

    public void swipe(final PegaWebElement element1, final int xposition, final int yposition) {
        final int height = element1.getWebElement().getSize().getHeight();
        final int width = element1.getWebElement().getSize().getWidth();
        this.longPress(element1.getWebElement(), width / 2, height / 2).moveTo(xposition, yposition).release().perform();
    }

    public void swipe(final PegaWebElement element1, final SwipeDirection sd) {
        final int height = element1.getWebElement().getSize().getHeight();
        final int width = element1.getWebElement().getSize().getWidth();
        int startx = element1.getWebElement().getLocation().getX();
        int starty = element1.getWebElement().getLocation().getY();
        int endx = startx;
        int endy = starty;
        switch (sd) {
            case RIGHT: {
                starty += height / 2;
                endx = startx + width;
                endy = starty;
                this.longPress(startx, starty).moveTo(endx, endy).release().perform();
                break;
            }
            case LEFT: {
                endx = startx;
                endy = starty + height / 2;
                startx += width;
                starty += height / 2;
                this.longPress(startx, starty).moveTo(endx, endy).release().perform();
                break;
            }
            case UP: {
                endx = startx + width / 2;
                endy = starty;
                startx += width / 2;
                starty += height;
                this.longPress(startx, starty).moveTo(endx, endy).release().perform();
                break;
            }
            case DOWN: {
                endx = startx + width / 2;
                endy = starty + height;
                startx += width / 2;
                this.longPress(startx, starty).moveTo(endx, endy).release().perform();
                break;
            }
            default: {
                TouchActionsImpl.LOGGER.info("Wrong Option: Unable to scroll from " + startx + "," + starty + " to " + endx + "," + endy + "!!!");
                break;
            }
        }
    }

    public void scroll(final PegaWebElement ele, final SwipeDirection sd, final int maxScrollCount) {
        for (int i = 0; i < maxScrollCount && !ele.getWebElement().isDisplayed(); ++i) {
            this.swipe(sd);
            TouchActionsImpl.LOGGER.info("Scroll count : " + i + 1);
            System.out.println("Scroll count : " + i + 1 + "Element Status" + ele.getWebElement().isDisplayed());
        }
    }

    public void swipe(final SwipeDirection sd) {
        final int OFFSET = 5;
        final Dimension dim = this.driver.manage().window().getSize();
        final int height = dim.getHeight();
        final int width = dim.getWidth();
        switch (sd) {
            case RIGHT: {
                this.longPress(0 + OFFSET, height / 2).moveTo(width / 2, height / 2).release().perform();
                break;
            }
            case LEFT: {
                this.longPress(width - OFFSET, height / 2).moveTo(width / 2, height / 2).release().perform();
                break;
            }
            case UP: {
                this.longPress(width / 2, height - OFFSET).moveTo(width / 2, height / 2).release().perform();
                break;
            }
            case DOWN: {
                this.longPress(width / 2, 0 + OFFSET).moveTo(width / 2, height / 2).release().perform();
                break;
            }
            default: {
                TouchActionsImpl.LOGGER.error("Unable to Scroll !!!");
                break;
            }
        }
    }

    private class ActionParameter {
        private String actionName;
        private ImmutableMap.Builder optionsBuilder;

        public ActionParameter(final String actionName) {
            this.actionName = actionName;
            this.optionsBuilder = ImmutableMap.builder();
        }

        public ActionParameter(final String actionName, final RemoteWebElement el) {
            this.actionName = actionName;
            this.optionsBuilder = ImmutableMap.builder();
            this.addParameter("element", el.getId());
        }

        public ImmutableMap<String, Object> getParameterMap() {
            final ImmutableMap.Builder builder = ImmutableMap.builder();
            builder.put("action", this.actionName).put("options", this.optionsBuilder.build());
            return (ImmutableMap<String, Object>) builder.build();
        }

        public void addParameter(final String name, final Object value) {
            this.optionsBuilder.put(name, value);
        }
    }
}
