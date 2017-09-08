package net.disy.sonarplugins.cssreport.doiuse;

import net.disy.sonarplugins.cssreport.CssIssueSensor;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.config.Settings;


public class DoiuseReportSensor implements Sensor {
    private final CssIssueSensor sensor;
    public DoiuseReportSensor(Settings settings)  {
        sensor = new CssIssueSensor(
                settings,
                new DoiuseConstants());
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
