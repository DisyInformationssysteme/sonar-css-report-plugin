package net.disy.sonarplugins.cssreport.cssstats.report;

public class RuleStats {
    private int total;
    private SizeStats size;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public SizeStats getSize() {
        return size;
    }

    public void setSize(SizeStats size) {
        this.size = size;
    }
}
