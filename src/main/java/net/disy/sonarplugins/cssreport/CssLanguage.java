package net.disy.sonarplugins.cssreport;

import org.sonar.api.resources.AbstractLanguage;

public class CssLanguage extends AbstractLanguage{
    private static final String[] LANGUAGE_EXT = {"css"};
    public static final String LANGUAGE_KEY = "css";
    private static final String LANGUAGE_NAME = "CSS";

    public CssLanguage() {
        super(LANGUAGE_KEY, LANGUAGE_NAME);
    }

    public String[] getFileSuffixes() {
        return LANGUAGE_EXT;
    }
}
