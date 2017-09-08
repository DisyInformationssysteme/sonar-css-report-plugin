package net.disy.sonarplugins.cssreport;

import java.util.List;

public class IssueReport {
    private List<FileIssueReport> files;

    public List<FileIssueReport> getFiles() {
        return files;
    }

    public void setFiles(List<FileIssueReport> files) {
        this.files = files;
    }
}
