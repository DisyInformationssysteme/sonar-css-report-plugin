package net.disy.sonarplugins.cssreport;

import net.disy.sonarplugins.cssreport.doiuse.DoiuseReportSensor;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.config.Settings;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class CssIssueSensor {
    private static final Logger log = Loggers.get(DoiuseReportSensor.class);
    private final Settings settings;
    private final CssReportConstants constants;

    public CssIssueSensor(
            Settings settings,
            CssReportConstants constants
            ) {
        this.settings = settings;
        this.constants = constants;
    }

    public void describe(SensorDescriptor sensorDescriptor) {
        sensorDescriptor.name("Sensor for " + constants.getToolName() + " reports.")
                .onlyOnLanguage(CssLanguage.LANGUAGE_KEY);
    }

    public void execute(SensorContext sensorContext) {
        if (!settings.getBoolean(constants.getSettingKeyEnabled())) {
            return;
        }
        String reportPath = settings.getString(constants.getSettingKeyReportPath());
        if (reportPath == null || reportPath.isEmpty()) {
            log.error(constants.getToolName() + " reports are enabled but the report path is not set");
            return;
        }
        Set<String> activeRules = sensorContext
                .activeRules()
                .findByRepository(constants.getRepositoryName())
                .stream()
                .map(rule -> rule.ruleKey().rule())
                .collect(Collectors.toSet());
        FileSystem fileSystem = sensorContext.fileSystem();
        IssueReport report;
        try {
            report = Mapper.mapper.readValue(new File(fileSystem.baseDir() + reportPath), IssueReport.class);
        } catch (IOException e) {
            log.error("error while parsing report", e);
            return;
        }
        for (FileIssueReport fileReport : report.getFiles()) {
            InputFile inputFile = fileSystem.inputFile(fileSystem.predicates().hasAbsolutePath(fileReport.getPath()));
            saveIssues(inputFile, fileReport.getIssues(), sensorContext, activeRules);
        }
    }

    private void saveIssues(InputFile inputFile, List<Issue> issues, SensorContext sensorContext, Set<String> activeRules) {
        for (Issue issue : issues) {
            NewIssue newIssue = sensorContext
                    .newIssue()
                    .forRule(getRuleKey(issue.getRulekey(), activeRules));
            NewIssueLocation location = newIssue
                    .newLocation()
                    .on(inputFile)
                    .message(issue.getMessage())
                    .at(inputFile.selectLine(issue.getLine()));
            newIssue.at(location);
            newIssue.save();
        }
    }

    private RuleKey getRuleKey(String rule, Set<String> activeRules) {
        String validRule = activeRules.contains(rule)
                ? rule
                : constants.getUnknownRuleKey();
        return RuleKey.of(constants.getRepositoryName(), validRule);
    }
}
