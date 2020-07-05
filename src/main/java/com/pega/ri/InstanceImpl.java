

package com.pega.ri;

import com.pega.*;

public class InstanceImpl implements Instance {

    protected TestEnvironment testEnv;

    @Override
    public TestEnvironment getTestEnvironment() {
        return this.testEnv;
    }
}
