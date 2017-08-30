package net.disy.sonarplugins.cssreport.cssstats.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeclarationsStats {
    private int total;
    private int unique;
    private ArrayList<CssProperty> important;
    private Map<String, List<String>> properties;
    private Map<String, Integer> resets;

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

    public ArrayList<CssProperty> getImportant() {
        return important;
    }

    public void setImportant(ArrayList<CssProperty> important) {
        this.important = important;
    }

    public Map<String, List<String>> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, List<String>> properties) {
        this.properties = properties;
    }

    public Map<String, Integer> getResets() {
        return resets;
    }

    public void setResets(Map<String, Integer> resets) {
        this.resets = resets;
    }
}
