package net.disy.sonarplugins.cssreport.cssstats.report;

public class CssMediaQuery {
    private String value;
    private RuleStats rules;
    private SelectorStats selectors;
    private DeclarationsStats declarations;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public RuleStats getRules() {
        return rules;
    }

    public void setRules(RuleStats rules) {
        this.rules = rules;
    }

    public SelectorStats getSelectors() {
        return selectors;
    }

    public void setSelectors(SelectorStats selectors) {
        this.selectors = selectors;
    }

    public DeclarationsStats getDeclarations() {
        return declarations;
    }

    public void setDeclarations(DeclarationsStats declarations) {
        this.declarations = declarations;
    }
}
