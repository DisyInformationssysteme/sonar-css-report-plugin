package net.disy.sonarplugins.cssreport.cssstats;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.disy.sonarplugins.cssreport.cssstats.report.Stats;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.Metric;

import java.util.function.Function;

public class CssStatsJsonMetric<T> implements CssStatsMetric<String> {
    private final Metric<String> metric;
    private final Function<Stats, T> valueGetter;

    public CssStatsJsonMetric(Metric<String> metric, Function<Stats, T> valueGetter) {
        this.metric = metric;
        this.valueGetter = valueGetter;
    }

    @Override
    public Metric<String> getSonarMetric() {
        return metric;
    }

    public void save(InputFile inputFile, SensorContext context, Stats stats, ObjectMapper mapper) throws JsonProcessingException {

        context.<String>newMeasure()
                .forMetric(getSonarMetric())
                .on(inputFile)
                .withValue(mapper.writeValueAsString(valueGetter.apply(stats)))
                .save();
    }
}
