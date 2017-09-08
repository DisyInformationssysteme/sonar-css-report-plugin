package net.disy.sonarplugins.cssreport.stylelint;

import net.disy.sonarplugins.cssreport.CssRuleDefinition;
import net.disy.sonarplugins.cssreport.RuleProfileRegistry;
import org.sonar.api.config.Settings;
import org.sonar.api.server.rule.RulesDefinition;

public class StyleLintRulesDefinition implements RulesDefinition {
    private final CssRuleDefinition rules;

    public StyleLintRulesDefinition(Settings settings, RuleProfileRegistry ruleProfileRegistry) {
        rules = new CssRuleDefinition(
                settings,
                new StyleLintConstants(),
                ruleProfileRegistry
        );
    }


    @Override
    public void define(Context context) {
        rules.define(context);
    }
}
