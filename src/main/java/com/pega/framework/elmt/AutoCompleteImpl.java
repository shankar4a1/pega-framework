

package com.pega.framework.elmt;

import com.pega.framework.*;
import com.pega.util.*;
import org.openqa.selenium.*;

public class AutoCompleteImpl extends PegaWebElementImpl implements AutoComplete {


    public AutoCompleteImpl(final WebElement elmt) {
        super(elmt);
    }

    public AutoCompleteImpl(final WebElement elmt, final String elmtId) {
        super(elmt, elmtId);
    }

    @Override
    public void setValue(final String keysToSend) {
        this.setValue(keysToSend, true);
    }

    @Override
    public void setValue(String keysToSend, final boolean clear) {
        if (clear) {
            this.clear();
        }
        this.click(false);
        keysToSend = keysToSend.replace("'", "'");
        this.scriptExecutor.sendKeys(this, keysToSend);
        this.pegaDriver.handleWaits().sleep(2L);
        this.sendKeys(Keys.DOWN);
        this.pegaDriver.handleWaits().sleep(2L);
        this.sendKeys(Keys.DOWN);
        this.pegaDriver.handleWaits().sleep(3L);
        if (this.verifyElement(By.id("ISnsSelect"))) {
            this.findSelectBox(By.id("ISnsSelect")).selectByVisibleText(keysToSend);
        } else if (this.verifyElement(By.xpath(XPathUtil.getMatchHighlightSpanXpath(keysToSend)))) {
            this.testEnv.getPegaDriver().handleWaits().waitForElementPresence(By.xpath(XPathUtil.getMatchHighlightSpanXpath(keysToSend)));
            this.pegaDriver.getDriver().findElement(By.xpath(XPathUtil.getMatchHighlightSpanXpath(keysToSend))).click();
        } else {
            this.testEnv.getPegaDriver().handleWaits().waitForElementPresence(By.xpath("//div[contains(@class,'autocompleteAG')]//span[text()='" + keysToSend + "']"));
            this.pegaDriver.getDriver().findElement(By.xpath("//div[contains(@class,'autocompleteAG')]//span[text()='" + keysToSend + "']")).click();
        }
        this.pegaDriver.waitForDocStateReady();
    }

    @Override
    public void selectValue(final String keysToSend) {
        this.scriptExecutor.sendKeys(this, keysToSend);
        this.pegaDriver.handleWaits().sleep(2L);
        this.sendKeys(Keys.DOWN);
        this.pegaDriver.handleWaits().sleep(4L);
        this.pegaDriver.findElement(By.xpath("//select[@id='ISnsSelect']//option[text()='" + keysToSend + "']")).click();
        this.pegaDriver.waitForDocStateReady();
    }

    @Override
    public void clear() {
        super.clear();
        this.pegaDriver.waitForDocStateReady();
    }
}
