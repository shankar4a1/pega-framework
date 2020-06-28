

package com.pega.util;

import java.text.*;
import java.util.*;

public class DateUtil {
    String COPYRIGHT;
    private static final String VERSION = "$Id: DataUtil.java 194486 2016-05-25 11:47:44Z ShakkariSakethkumar $";

    public DateUtil() {
        this.COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    }

    public static String getCurrentMonth() {
        return getCurrentMonth("MM/dd/yyyy");
    }

    public static String getCurrentMonth(final String dateFormat) {
        final DateFormat format = new SimpleDateFormat(dateFormat);
        final Calendar cal = Calendar.getInstance();
        final String date = format.format(cal.getTime());
        final String[] dateArray = date.split("/|\\-");
        return dateArray[0];
    }

    public static String getCurrentDay() {
        return getCurrentDay("MM/dd/yyyy");
    }

    public static String getCurrentDay(final String dateFormat) {
        final DateFormat format = new SimpleDateFormat(dateFormat);
        final Calendar cal = Calendar.getInstance();
        final String date = format.format(cal.getTime());
        final String[] dateArray = date.split("/|\\-");
        return dateArray[1];
    }

    public static String getCurrentYear() {
        return getCurrentYear("MM/dd/yyyy");
    }

    public static String getCurrentYear(final String dateFormat) {
        final DateFormat format = new SimpleDateFormat(dateFormat);
        final Calendar cal = Calendar.getInstance();
        final String date = format.format(cal.getTime());
        final String[] dateArray = date.split("/|\\-");
        return dateArray[2];
    }
}
