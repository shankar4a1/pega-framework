package com.pega.config.guice;

import com.google.inject.*;
import io.cucumber.guice.*;

public class GuiceInjector implements InjectorSource {


    public GuiceInjector() {

    }

    public Injector getInjector() {
        return Guice.createInjector(Stage.PRODUCTION, CucumberModules.createScenarioModule(), new TestEnvInjector());
    }
}
