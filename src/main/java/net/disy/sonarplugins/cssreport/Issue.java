package net.disy.sonarplugins.cssreport;

public class Issue {
    private int line;
    private String message;
    private String rulekey;

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRulekey() {
        return rulekey;
    }

    public void setRulekey(String rulekey) {
        this.rulekey = rulekey;
    }
}
