package net.disy.sonarplugins.cssreport.cssstats.report;

import java.util.List;

public class MediaQueriesStats {
    private int total;
    private int unique;
    private List<String> values;
    private List<CssMediaQuery> contents;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getUnique() {
        return unique;
    }

    public void setUnique(int unique) {
        this.unique = unique;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public List<CssMediaQuery> getContents() {
        return contents;
    }

    public void setContents(List<CssMediaQuery> contents) {
        this.contents = contents;
    }
}
