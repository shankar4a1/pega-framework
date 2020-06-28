

package com.pega.config.util;

import java.io.*;

public class RefactorMethodsCalls {
    String COPYRIGHT;
    private static final String VERSION = "$Id: RefactorMethodsCalls.java 184148 2016-03-29 05:25:11Z SachinVellanki $";

    public RefactorMethodsCalls() {
        this.COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    }

    public static void main(final String[] a) {
        final RefactorMethodsCalls obj = new RefactorMethodsCalls();
        obj.makeMethodsInAccessible();
    }

    private void makeMethodsInAccessible() {
        final File f = new File(System.getenv("WORKSPACE") + System.getProperty("file.separator") + "stepdefs" + System.getProperty("file.separator") + "com" + System.getProperty("file.separator") + "pega" + System.getProperty("file.separator") + "MyTestEnvironment.java");
        if (f.exists()) {
            f.delete();
            System.out.println("MyTestEnvironment is deleted...");
        }
    }
}
