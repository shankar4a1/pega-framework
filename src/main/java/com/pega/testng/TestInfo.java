

package com.pega.testng;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface TestInfo {


    String tcid() default "";

    String dataFileName() default "";

    boolean recordTest() default false;

    String description() default "";
}
