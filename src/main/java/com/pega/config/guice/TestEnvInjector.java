

package com.pega.config.guice;

import com.google.inject.*;
import com.pega.*;
import com.pega.exceptions.*;
import org.testng.*;

public class TestEnvInjector extends AbstractModule {


    public TestEnvInjector() {

    }

    protected void configure() {
        String className = null;
        try {
            className = System.getProperty("baseClass");
            if (className == null) {
                className = System.getenv("baseClass");
            }
            if (className == null) {
                throw new PegaWebDriverException("baseClass param is empty. Base class which extends com.pega.TestEnvironmentImpl is not provided");
            }
            final Class<TestEnvironment> classType = (Class<TestEnvironment>) Class.forName(className, true, this.getClass().getClassLoader());
            this.bind((Class) TestEnvironment.class).to(classType);
            Reporter.log("Implementation class for TestEnvironment interface is: " + className, true);
        } catch (ClassNotFoundException e) {
            throw new PegaWebDriverException("Implementation class is not found: " + className);
        }
    }
}
