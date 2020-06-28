

package com.pega.ri;

import com.pega.*;

public class InstanceImpl implements Instance {
    private static final String VERSION = "$Id: InstanceImpl.java 131616 2015-04-12 08:27:54Z SachinVellanki $";
    protected TestEnvironment testEnv;

    @Override
    public TestEnvironment getTestEnvironment() {
        return this.testEnv;
    }
}
