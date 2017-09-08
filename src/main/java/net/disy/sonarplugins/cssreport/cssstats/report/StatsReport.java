package net.disy.sonarplugins.cssreport.cssstats.report;

import java.util.List;

public class StatsReport {
    private List<FileStatsReport> files;

    public List<FileStatsReport> getFiles() {
        return files;
    }

    public void setFiles(List<FileStatsReport> files) {
        this.files = files;
    }
}
