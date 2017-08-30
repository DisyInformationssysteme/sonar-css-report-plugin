package net.disy.sonarplugins.cssreport.cssstats.report;

public class Stats {
    private int size;
    private int gzipSize;
    private RuleStats rules;
    private SelectorStats selectors;
    private DeclarationsStats declarations;
    private MediaQueriesStats mediaQueries;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getGzipSize() {
        return gzipSize;
    }

    public void setGzipSize(int gzipSize) {
        this.gzipSize = gzipSize;
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

    public MediaQueriesStats getMediaQueries() {
        return mediaQueries;
    }

    public void setMediaQueries(MediaQueriesStats mediaQueries) {
        this.mediaQueries = mediaQueries;
    }
}
