

package com.pega.framework;

public interface RobotKeyboard {
    String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    String VERSION = "$Id: RobotKeyboard.java 170459 2016-01-08 12:18:51Z ChanukyaVempati $";

    void type(final String p0);

    void tab();

    void enter();

    void f11();
}
