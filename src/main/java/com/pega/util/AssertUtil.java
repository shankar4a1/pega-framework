

package com.pega.util;

import org.jsoup.*;
import org.jsoup.nodes.*;

import java.util.regex.*;

public class AssertUtil {


    public AssertUtil() {

    }

    public static boolean validateRegex(final String regex, final String content) {
        final Pattern p = Pattern.compile(regex);
        final Matcher m = p.matcher(content);
        return m.find();
    }

    public static boolean validateRegex(final String regex, final String content, final int patternFlags) {
        final Pattern p = Pattern.compile(regex, patternFlags);
        final Matcher m = p.matcher(content);
        return m.find();
    }

    public static boolean validateCss(final String cssQuery, final String content) {
        final Document doc = Jsoup.parse(content, "UTF-8");
        return doc.select(cssQuery).first() != null;
    }

    public static String[] getMatchedGroups(final String regex, final String content) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        final int openBraces = regex.length() - regex.replace("(", "").length();
        final int closedBraces = regex.length() - regex.replace(")", "").length();
        final int groupCount = (openBraces > closedBraces) ? closedBraces : openBraces;
        final String[] matches = new String[groupCount + 1];
        p = Pattern.compile(regex);
        m = p.matcher(content);
        m.find();
        for (int counter = 0; counter <= groupCount; ++counter) {
            matches[counter] = m.group(counter);
        }
        return matches;
    }
}
