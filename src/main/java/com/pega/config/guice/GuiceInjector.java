package com.pega.config.guice;

import com.google.inject.*;
import io.cucumber.guice.*;

public class GuiceInjector implements InjectorSource {
    String COPYRIGHT;
    private static final String VERSION = "$Id: GuiceInjector.java 174698 2016-02-08 08:24:26Z SachinVellanki $";

    public GuiceInjector() {
        this.COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    }

    public Injector getInjector() {
        return Guice.createInjector(Stage.PRODUCTION, CucumberModules.createScenarioModule(), new TestEnvInjector());
    }
}
