

package com.pega.framework;

import com.pega.*;
import org.slf4j.*;

import java.awt.*;
import java.util.List;
import java.util.*;

public class MouseImpl implements Mouse {
    private static final String VERSION = "$Id: MouseImpl.java 121818 2015-01-26 07:18:23Z SachinVellanki $";
    private static final Logger LOGGER;
    private int lastX;
    private int lastY;
    private Robot robot;
    private static long lastClickTime;
    private Speed speed;
    TestEnvironment testEnv;

    static {
        LOGGER = LoggerFactory.getLogger(MouseImpl.class.getName());
        MouseImpl.lastClickTime = 0L;
    }

    public MouseImpl(final TestEnvironment testEnv) {
        this.lastX = 0;
        this.lastY = 0;
        this.testEnv = null;
        this.testEnv = testEnv;
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            MouseImpl.LOGGER.error(e.getMessage(), true);
        }
    }

    @Override
    public void click() {
        final long delta = System.currentTimeMillis() - MouseImpl.lastClickTime;
        if (delta < 1000L) {
            try {
                Thread.sleep(1000L - delta);
            } catch (InterruptedException ex) {
            }
        }
        this.robot.mousePress(16);
        this.robot.mouseRelease(16);
        MouseImpl.lastClickTime = System.currentTimeMillis();
    }

    @Override
    public void doubleClick() {
        this.robot.mousePress(16);
        this.robot.mouseRelease(16);
        this.robot.mousePress(16);
        this.robot.mouseRelease(16);
    }

    @Override
    public void rightClick() {
        this.robot.mousePress(4);
        this.robot.mouseRelease(4);
    }

    @Override
    public void pressLeftButton() {
        this.robot.mousePress(16);
    }

    @Override
    public void releaseLeftButton() {
        this.robot.mouseRelease(16);
    }

    private List<Point> generateMousePath(final int xStart, final int yStart, final int xEnd, final int yEnd) {
        final List<Point> path = new ArrayList<Point>();
        final Point start = new Point(xStart, yStart);
        final Point end = new Point(xEnd, yEnd);
        path.add(start);
        final int stepsOfOne = 5;
        final int stepsOfFive = 3;
        final int stepsRest = 5;
        if (start.distance(end) > stepsOfOne * 2) {
            Point subEnd = this.getPointFromDistance(start, end, stepsOfOne);
            List<Point> subPath = this.generateMousePath(start, subEnd, stepsOfOne);
            path.addAll(subPath);
            if (start.distance(end) > stepsOfOne * 2 + stepsOfFive * 5 * 2) {
                Point subStart = path.get(path.size() - 1);
                subEnd = this.getPointFromDistance(subStart, end, stepsOfOne + stepsOfFive * 5);
                subPath = this.generateMousePath(subStart, subEnd, stepsOfFive);
                path.addAll(subPath);
                subStart = path.get(path.size() - 1);
                subEnd = this.getPointFromDistance(start, end, start.distance(end) - (stepsOfOne + stepsOfFive * 5));
                subPath = this.generateMousePath(subStart, subEnd, stepsRest);
                path.addAll(subPath);
                subStart = path.get(path.size() - 1);
                subEnd = this.getPointFromDistance(start, end, start.distance(end) - stepsOfOne);
                subPath = this.generateMousePath(subStart, subEnd, stepsOfFive);
                path.addAll(subPath);
            } else {
                final Point subStart = path.get(path.size() - 1);
                subEnd = this.getPointFromDistance(start, end, start.distance(end) - stepsOfOne);
                subPath = this.generateMousePath(subStart, subEnd, (int) Math.floor(subStart.distance(subEnd) / 5.0));
                path.addAll(subPath);
            }
            Point subStart = path.get(path.size() - 1);
            subPath = this.generateMousePath(subStart, end, stepsOfOne);
            path.addAll(subPath);
        } else {
            final List<Point> subPath = this.generateMousePath(start, end, (int) Math.ceil(start.distance(end)));
            path.addAll(subPath);
        }
        return path;
    }

    private Point getPointFromDistance(final Point start, final Point end, final double distance) {
        final double x = (end.x - start.x) * (distance / start.distance(end)) + start.x;
        final double y = (end.y - start.y) * (distance / start.distance(end)) + start.y;
        return new Point((int) x, (int) y);
    }

    private List<Point> generateMousePath(final Point start, final Point end, final int steps) {
        final List<Point> path = new ArrayList<Point>();
        final double xStep = (start.x - end.x) / (double) steps;
        final double yStep = (start.y - end.y) / (double) steps;
        for (int i = 0; i <= steps; ++i) {
            final Point p = new Point((int) (start.x - i * xStep), (int) (start.y - i * yStep));
            if (start.distance(p) > start.distance(end)) {
                break;
            }
            if (p.equals(end)) {
                break;
            }
            path.add(p);
        }
        path.add(end);
        return path;
    }

    private void mouseMove(final int xStart, final int yStart, final int xEnd, final int yEnd, final int delay) {
        MouseImpl.LOGGER.debug("Moving mouse to " + xStart + "," + yStart + "," + xEnd + "," + yEnd);
        this.robot.mouseMove(xStart, yStart);
        final List<Point> path = this.generateMousePath(xStart, yStart, xEnd, yEnd);
        for (final Point point : path) {
            this.robot.delay(delay);
            this.robot.mouseMove(point.x, point.y);
        }
        this.lastX = xEnd;
        this.lastY = yEnd;
    }

    @Override
    public void moveTo(final int xEnd, final int yEnd) {
        if (!this.testEnv.getConfiguration().getMobileConfig().isMobileExecution()) {
            final int xStart = this.lastX;
            final int yStart = this.lastY;
            final int delay = this.getSpeed().getDelay();
            this.mouseMove(xStart, yStart, xEnd, yEnd, delay);
        }
    }

    @Override
    public void setSpeed(final Speed speed) {
        this.speed = speed;
    }

    @Override
    public Speed getSpeed() {
        if (this.speed == null) {
            this.setSpeed(Speed.MEDIUM);
        }
        return this.speed;
    }

    @Override
    public void moveRelative(final int x, final int y) {
        if (!this.testEnv.getConfiguration().getMobileConfig().isMobileExecution()) {
            final int xStart = this.lastX;
            final int yStart = this.lastY;
            final int delay = this.getSpeed().getDelay();
            this.mouseMove(xStart, yStart, xStart + x, yStart + y, delay);
        }
    }

    @Override
    public void mouseWheel(final int wheelAmt) {
        this.robot.mouseWheel(wheelAmt);
    }
}
