

package com.pega.config;

import java.util.*;

public class Credential {
    private Properties usersProperties;

    public Credential(final Properties prop) {
        this.usersProperties = prop;
    }

    public String getUserID(final String propertyName) {
        return this.usersProperties.getProperty(propertyName);
    }

    public String getPassword(final String propertyName) {
        return this.usersProperties.getProperty(propertyName);
    }
}
