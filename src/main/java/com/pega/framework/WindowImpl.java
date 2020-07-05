

package com.pega.framework;

import com.pega.*;

public class WindowImpl extends PegaWebDriverImpl implements Window {


    public WindowImpl(final TestEnvironment testEnv) {
        super(testEnv);
    }

    @Override
    public String getWindowID() {
        return null;
    }
}
