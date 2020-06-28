

package com.pega.ri;

import com.pega.*;

public interface Instance {
    String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    String VERSION = "$Id: Instance.java 145627 2015-07-26 14:59:31Z SachinVellanki $";

    TestEnvironment getTestEnvironment();
}
