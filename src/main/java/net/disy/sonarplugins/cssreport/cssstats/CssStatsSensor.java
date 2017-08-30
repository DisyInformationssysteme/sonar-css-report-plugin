package net.disy.sonarplugins.cssreport.cssstats;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.disy.sonarplugins.cssreport.CssLanguage;
import net.disy.sonarplugins.cssreport.cssstats.report.FileStatsReport;
import net.disy.sonarplugins.cssreport.cssstats.report.Stats;
import net.disy.sonarplugins.cssreport.cssstats.report.StatsReport;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.config.Settings;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.File;
import java.io.IOException;

public class CssStatsSensor  implements Sensor {
    private static final Logger log = Loggers.get(CssStatsSensor.class);
    private final Settings settings;
    private final CssStatsMetrics metrics;

    public CssStatsSensor(Settings settings, CssStatsMetrics metrics) {
        this.settings = settings;
        this.metrics = metrics;
    }

    @Override
    public void describe(SensorDescriptor sensorDescriptor) {
        sensorDescriptor
                .onlyOnLanguage(CssLanguage.LANGUAGE_KEY)
                .name("Read cssstats-report from file.");
    }

    @Override
    public void execute(SensorContext sensorContext) {
        if (!settings.getBoolean(CssStatsConstants.SETTINGS_ENABLED_KEY)) {
            return;
        }
        String reportPath = settings.getString(CssStatsConstants.SETTINGS_REPORT_PATH_KEY);
        if (reportPath == null || reportPath.isEmpty()) {
            log.error("CssStats reports are enabled, but the report path is not set");
            return;
        }
        FileSystem fileSystem = sensorContext.fileSystem();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        StatsReport report;
        try {
            report = mapper.readValue(new File(fileSystem.baseDir() + reportPath), StatsReport.class);
        } catch (IOException e) {
            log.error("error while parsing report {}", e);
            return;
        }
        for (FileStatsReport fileReport : report.getFiles()) {
            InputFile inputFile = fileSystem.inputFile(fileSystem.predicates().hasAbsolutePath(fileReport.getPath()));
            saveMetrics(inputFile, fileReport.getStats(), sensorContext, mapper);
        }
    }

    private void saveMetrics(InputFile inputFile, Stats stats, SensorContext sensorContext, ObjectMapper mapper) {
        for (CssStatsPrimitiveMetric metric : metrics.getPrimitiveMetrics()) {
            metric.save(inputFile, sensorContext, stats);
        }
        for (CssStatsJsonMetric metric : metrics.getJsonMetrics()) {
            try {
                metric.save(inputFile, sensorContext, stats, mapper);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
}
