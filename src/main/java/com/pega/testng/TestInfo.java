

package com.pega.testng;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface TestInfo {
    String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    String VERSION = "$Id: TestInfo.java 121818 2015-01-26 07:18:23Z SachinVellanki $";

    String tcid() default "";

    String dataFileName() default "";

    boolean recordTest() default false;

    String description() default "";
}
