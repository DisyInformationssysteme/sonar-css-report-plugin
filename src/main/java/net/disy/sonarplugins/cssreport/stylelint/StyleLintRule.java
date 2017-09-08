package net.disy.sonarplugins.cssreport.stylelint;

import net.disy.sonarplugins.cssreport.CssRule;

public class StyleLintRule implements CssRule {
    private String key;
    private String name;
    private String severity;
    private String htmlDescription;

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String getName() {
        return name != null ? name : key;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getSeverity() {
        return severity != null ? severity : "MINOR";
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    @Override
    public String getHtmlDescription() {
        return htmlDescription != null ? htmlDescription : "no html description available";
    }

    public void setHtmlDescription(String htmlDescription) {
        this.htmlDescription = htmlDescription;
    }
}
