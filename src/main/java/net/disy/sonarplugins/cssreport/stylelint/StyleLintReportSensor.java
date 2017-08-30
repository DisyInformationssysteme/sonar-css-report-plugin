package net.disy.sonarplugins.cssreport.stylelint;

import net.disy.sonarplugins.cssreport.CssIssueSensor;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.Metric;

import java.io.Serializable;
import java.util.HashMap;

public class StyleLintReportSensor implements Sensor {
    private final CssIssueSensor sensor;
    public StyleLintReportSensor(Settings settings) {
        sensor = new CssIssueSensor(
                settings,
                new StyleLintConstants()
        );
    }

    @Override
    public void describe(SensorDescriptor sensorDescriptor) {
        sensor.describe(sensorDescriptor);
    }

    @Override
    public void execute(SensorContext sensorContext) {
        sensor.execute(sensorContext);
    }
}
