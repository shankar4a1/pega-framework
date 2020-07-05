

package com.pega.util;

import java.text.*;
import java.util.*;

public class DateUtil {


    public DateUtil() {

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
