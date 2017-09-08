package net.disy.sonarplugins.cssreport;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.sonar.api.config.Settings;
import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rules.Rule;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinition.NewRepository;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.*;
import java.util.List;

public class CssRuleDefinition {
    private final Logger log = Loggers.get(CssRuleDefinition.class);
    private final Settings settings;
    private final CssReportConstants constants;
    private RuleProfileRegistry ruleProfileRegistry;

    public CssRuleDefinition(
            Settings settings,
            CssReportConstants constants,
            RuleProfileRegistry ruleProfileRegistry
    ) {
        this.settings = settings;
        this.constants = constants;
        this.ruleProfileRegistry = ruleProfileRegistry;
    }


    public void define(RulesDefinition.Context context) {
        NewRepository newRepository = context.createRepository(constants.getRepositoryName(), CssLanguage.LANGUAGE_KEY)
                .setName("Css IssueReport " + constants.getToolName());
        createDefaultUnknownRule(newRepository);
        createDefaultRules(newRepository, Mapper.mapper);
        createCustomRules(newRepository, Mapper.mapper);
        newRepository.done();
    }

    private void createDefaultRules(NewRepository repository, ObjectMapper mapper) {
        try(InputStream rules = getClass().getResourceAsStream(constants.getRuleResourcePath())){
            createRules(rules, repository, mapper);
        } catch (IOException e) {
            log.error("Error while opening file: {}", e);
        }
    }

    private void createCustomRules(NewRepository repository, ObjectMapper mapper) {
        String path = settings.getString(constants.getSettingKeyRulePath());
        if (path == null || path.isEmpty()) {
            return;
        }
        File file = new File(path);
        try (FileInputStream fileInputStream = new FileInputStream(file)){
            createRules(fileInputStream, repository, mapper);
        } catch (FileNotFoundException e) {
            log.error("File {} not found: {}", path, e);
        } catch (IOException e) {
            log.error("Error while opening file: {}", e);
        }
    }

    private void createRules(InputStream rules, NewRepository repository, ObjectMapper mapper) {
        try{
            List<CssRule> defaultRules =
            mapper.readValue(rules, mapper.getTypeFactory().constructCollectionType(List.class, constants.getRuleClass()));
            for (CssRule rule : defaultRules) {
                repository
                        .createRule(rule.getKey())
                        .setName(rule.getName())
                        .setSeverity(rule.getSeverity())
                        .setHtmlDescription(rule.getHtmlDescription())
                        .setStatus(RuleStatus.READY);
                ruleProfileRegistry.add(Rule.create(constants.getRepositoryName(), rule.getKey()));
            }
        } catch (IOException e) {
            log.error("error while parsing rules {}", e);
        }
    }

    private void createDefaultUnknownRule(NewRepository repository) {
        repository
                .createRule(constants.getUnknownRuleKey())
                .setName("unknown " + constants.getToolName() + " issue")
                .setSeverity(constants.getUnknownRuleSeverity())
                .setHtmlDescription("no html description available")
                .setStatus(RuleStatus.READY);
        ruleProfileRegistry.add(Rule.create(constants.getRepositoryName(), constants.getUnknownRuleKey()));
    }
}
