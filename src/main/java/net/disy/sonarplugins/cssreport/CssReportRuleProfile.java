package net.disy.sonarplugins.cssreport;

import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.utils.ValidationMessages;

public class CssReportRuleProfile extends ProfileDefinition {
    private RuleProfileRegistry ruleProfileRegistry;

    public CssReportRuleProfile(RuleProfileRegistry ruleProfileRegistry) {
        this.ruleProfileRegistry = ruleProfileRegistry;
    }

    @Override
    public RulesProfile createProfile(ValidationMessages validationMessages) {
        RulesProfile rulesProfile = RulesProfile.create("css-reports", CssLanguage.LANGUAGE_KEY);
        for (Rule rule : ruleProfileRegistry.getRules()) {
            rulesProfile.activateRule(rule, null);
        }


        return rulesProfile;
    }
}
