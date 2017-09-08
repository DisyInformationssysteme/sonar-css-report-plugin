package net.disy.sonarplugins.cssreport.doiuse;

import net.disy.sonarplugins.cssreport.CssRuleDefinition;
import net.disy.sonarplugins.cssreport.RuleProfileRegistry;
import org.sonar.api.batch.BatchSide;
import org.sonar.api.batch.ScannerSide;
import org.sonar.api.config.Settings;
import org.sonar.api.server.rule.RulesDefinition;

@ScannerSide
public class DoiuseRulesDefinition implements RulesDefinition {
    private final CssRuleDefinition rules;

    public DoiuseRulesDefinition(Settings settings, RuleProfileRegistry ruleProfileRegistry) {
        rules = new CssRuleDefinition(
                settings,
                new DoiuseConstants(),
                ruleProfileRegistry
        );
    }

    @Override
    public void define(Context context) {
        rules.define(context);
    }
}
