

package com.pega.config;

import com.pega.util.*;

import java.util.*;

public class PegaObjectBean implements ObjectBean {


    private static Map<String, String> objectNames;

    static {
        PegaObjectBean.objectNames = new LinkedHashMap<String, String>();
    }

    public PegaObjectBean() {

    }

    @Override
    public String putTimeStampedValue(final String name) {
        final String existingValue = PegaObjectBean.objectNames.get(name);
        if (existingValue == null) {
            final String value = DataUtil.getRandomNumberString(name);
            PegaObjectBean.objectNames.put(name, value);
            return value;
        }
        return existingValue;
    }

    @Override
    public String getTimestampedValue(final String name) {
        final String value = PegaObjectBean.objectNames.get(name);
        if (value != null) {
            return value;
        }
        return name;
    }
}
