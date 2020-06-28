

package com.pega.config.test;

import com.fasterxml.jackson.databind.deser.*;
import io.cucumber.plugin.event.*;
import org.testng.*;


/*import gherkin.formatter.model.Result;
import gherkin.formatter.model.Match;
import gherkin.formatter.Reporter;*/

public class CustomFormatter extends Reporter {
    public void after(final DataFormatReaders.Match arg0, final Result arg1) {
    }

    public void before(final DataFormatReaders.Match arg0, final Result arg1) {
    }

    public void embedding(final String arg0, final byte[] arg1) {
    }

    public void match(final DataFormatReaders.Match match) {
        // ThreadLocalStepDefinitionMatch.set((StepDefinitionMatch)match);
    }

    public void result(final Result arg0) {
    }

    public void write(final String arg0) {
    }
}
