package net.disy.sonarplugins.cssreport.cssstats.report;

public class SelectorSpecificityPair {
    private String selector;
    private int specificity;

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public int getSpecificity() {
        return specificity;
    }

    public void setSpecificity(int specificity) {
        this.specificity = specificity;
    }
}
