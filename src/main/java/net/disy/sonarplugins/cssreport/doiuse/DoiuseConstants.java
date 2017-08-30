package net.disy.sonarplugins.cssreport.doiuse;

import net.disy.sonarplugins.cssreport.CssReportConstants;

public class DoiuseConstants implements CssReportConstants{

    @Override
    public String getSettingKeyEnabled() {
        return "sonar.css.report.doiuse.enabled";
    }

    @Override
    public String getSettingKeyReportPath() {
        return "sonar.css.report.doiuse.report.path";
    }

    @Override
    public String getSettingKeyRulePath() {
        return "sonar.css.report.doiuse.rule.path";
    }

    @Override
    public String getToolName() {
        return "Doiuse";
    }

    @Override
    public String getRepositoryName() {
        return "css.reports.doiuse";
    }

    @Override
    public String getUnknownRuleKey() {
        return "css.report.doiuse.unknown.rule";
    }

    @Override
    public String getUnknownRuleSeverity() {
        return "MINOR";
    }

    @Override
    public String getRuleResourcePath() {
        return "/net/disy/sonarplugins/cssreport/doiuse/rules.json";
    }

    @Override
    public Class getRuleClass() {
        return DoiuseRule.class;
    }
}
