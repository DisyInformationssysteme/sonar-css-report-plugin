package net.disy.sonarplugins.cssreport;

import org.sonar.api.batch.sensor.measure.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.Serializable;

public class CssReportRuleProfile extends ProfileDefinition {
    private final static Logger log = Loggers.get(ProfileDefinition.class);

    private RuleProfileRegistry ruleProfileRegistry;

    public CssReportRuleProfile(RuleProfileRegistry ruleProfileRegistry) {
        this.ruleProfileRegistry = ruleProfileRegistry;
    }

    @Override
    public RulesProfile createProfile(ValidationMessages validationMessages) {
        RulesProfile rulesProfile = RulesProfile.create("css-reports", CssLanguage.LANGUAGE_KEY);
        for (Rule rule : ruleProfileRegistry.getRules()) {
            rulesProfile.activateRule(rule, null);
            // TODO find out why rules don't get activated in profile
        }


        return rulesProfile;
    }
}
