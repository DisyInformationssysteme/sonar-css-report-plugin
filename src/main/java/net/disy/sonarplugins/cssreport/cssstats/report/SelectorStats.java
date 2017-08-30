package net.disy.sonarplugins.cssreport.cssstats.report;

import java.util.List;

public class SelectorStats {
    private int total;
    private int id;
    private List<String> values;
    private SpecificityStats specificity;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // have to be Strings, CssSelector from CSSelly does not handle serialization well
    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public SpecificityStats getSpecificity() {
        return specificity;
    }

    public void setSpecificity(SpecificityStats specificity) {
        this.specificity = specificity;
    }
}
