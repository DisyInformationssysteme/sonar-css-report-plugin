package net.disy.sonarplugins.cssreport.cssstats;

import net.disy.sonarplugins.cssreport.cssstats.report.Stats;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.Metric;

import java.io.Serializable;
import java.util.function.Function;

public class CssStatsPrimitiveMetric<T extends Serializable> implements CssStatsMetric<T> {
    private final Metric<T> sonarMetric;
    private final Function<Stats, T> valueGetter;

    public CssStatsPrimitiveMetric(Metric<T> sonarMetric, Function<Stats, T> valueGetter) {
        this.sonarMetric = sonarMetric;
        this.valueGetter = valueGetter;
    }

    public Metric<T> getSonarMetric() {
        return sonarMetric;
    }

    public void save(InputFile inputFile, SensorContext context, Stats stats) {
        context.<T>newMeasure()
                .forMetric(getSonarMetric())
                .on(inputFile)
                .withValue(valueGetter.apply(stats))
                .save();
    }
}
