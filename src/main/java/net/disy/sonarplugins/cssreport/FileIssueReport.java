package net.disy.sonarplugins.cssreport;

import java.util.List;

public class FileIssueReport {
    private String path;
    private List<Issue> issues;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }
}
