package net.disy.sonarplugins.cssreport.cssstats.report;

import java.util.ArrayList;
import java.util.List;

public class SpecificityStats {
    private Object graph;
    private List<SelectorSpecificityPair> values;

    public List<Integer> getGraph() {
        if (graph instanceof Boolean) {
            return new ArrayList<>();
        }
        return (List<Integer>)graph;
    }

    public void setGraph(Object graph) {
        this.graph = graph;
    }

    public List<SelectorSpecificityPair> getValues() {
        return values;
    }

    public void setValues(List<SelectorSpecificityPair> values) {
        this.values = values;
    }
}
