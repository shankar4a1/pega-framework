

package com.pega.framework;

public interface Mouse {
    String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    String VERSION = "$Id: Mouse.java 125139 2015-02-22 15:23:22Z SachinVellanki $";

    void click();

    void doubleClick();

    void rightClick();

    void pressLeftButton();

    void releaseLeftButton();

    void moveTo(final int p0, final int p1);

    void setSpeed(final Speed p0);

    Speed getSpeed();

    void moveRelative(final int p0, final int p1);

    void mouseWheel(final int p0);

    enum Speed {
        FAST("FAST", 0, 1),
        MEDIUM("MEDIUM", 1, 2),
        SLOW("SLOW", 2, 6),
        VERYSLOW("VERYSLOW", 3, 15);

        private final int delay;

        Speed(final String name, final int ordinal, final int delay) {
            this.delay = delay;
        }

        public int getDelay() {
            return this.delay;
        }
    }
}
