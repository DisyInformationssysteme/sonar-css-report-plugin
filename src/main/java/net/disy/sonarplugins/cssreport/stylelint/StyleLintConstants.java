package net.disy.sonarplugins.cssreport.stylelint;

import net.disy.sonarplugins.cssreport.CssReportConstants;

public class StyleLintConstants implements CssReportConstants {
    @Override
    public String getSettingKeyEnabled() {
        return "sonar.css.report.stylelint.enabled";
    }

    @Override
    public String getSettingKeyReportPath() {
        return "sonar.css.report.stylelint.report.path";
    }

    @Override
    public String getSettingKeyRulePath() {
        return "sonar.css.report.stylelint.rule.path";
    }

    @Override
    public String getToolName() {
        return "StyleLint";
    }

    @Override
    public String getRepositoryName() {
        return "css.reports.stylelint";
    }

    @Override
    public String getUnknownRuleKey() {
        return "css.report.stylelint.unknown.rule";
    }

    @Override
    public String getUnknownRuleSeverity() {
        return "MINOR";
    }

    @Override
    public String getRuleResourcePath() {
        return "/net/disy/sonarplugins/cssreport/stylelint/rules.json";
    }

    @Override
    public Class getRuleClass() {
        return StyleLintRule.class;
    }
}
