

package com.pega.config;

import com.pega.util.*;

import java.util.*;

public class L10NConfig {
    private String language;
    private String langBundlePath;

    public L10NConfig(final Properties prop) {
        if (System.getenv("l10n.language") == null) {
            this.language = prop.getProperty("l10n.language", HTTPUtil.TransaltionLang.ENGLISH.getLang()).trim();
            if ("".equals(this.language)) {
                this.language = HTTPUtil.TransaltionLang.ENGLISH.getLang();
            }
        } else {
            this.language = System.getenv("l10n.language").trim();
        }
        this.langBundlePath = prop.getProperty("l10n.bundle.path", "").trim();
    }

    public String getL10NLanguage() {
        return this.language;
    }

    public String getL10NLangBundlePath() {
        return this.langBundlePath;
    }
}
