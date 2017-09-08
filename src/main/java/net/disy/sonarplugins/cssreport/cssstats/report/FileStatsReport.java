package net.disy.sonarplugins.cssreport.cssstats.report;

public class FileStatsReport {
    private String path;
    private Stats stats;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }
}
