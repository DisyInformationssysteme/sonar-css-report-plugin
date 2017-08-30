package net.disy.sonarplugins.cssreport.cssstats;

import org.sonar.api.ce.measure.Component;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class UniqueAggregator implements MeasureComputer {
    private final static Logger log = Loggers.get(UniqueAggregator.class);

    @Override
    public MeasureComputerDefinition define(MeasureComputerDefinitionContext measureComputerDefinitionContext) {
        return measureComputerDefinitionContext.newDefinitionBuilder()
                .setInputMetrics(
                        CssStatsMetrics.DECL_PROPERTIES.getSonarMetric().getKey(),
                        CssStatsMetrics.MEDIA_QUERIES_VALUES.getSonarMetric().getKey()
                )
                .setOutputMetrics(
                        CssStatsMetrics.DECL_UNIQUE.getSonarMetric().getKey(),
                        CssStatsMetrics.MEDIA_QUERIES_UNIQUE.getSonarMetric().getKey()
                )
                .build();
    }

    @Override
    public void compute(MeasureComputerContext measureComputerContext) {
        if (measureComputerContext.getComponent().getType() != Component.Type.FILE) {
            Measure serializedProperties =
                    measureComputerContext.getMeasure(CssStatsMetrics.DECL_PROPERTIES.getSonarMetric().getKey());
            Measure serializedQueries =
                    measureComputerContext.getMeasure(CssStatsMetrics.MEDIA_QUERIES_VALUES.getSonarMetric().getKey());
            try {
                if (serializedProperties != null) {
                    Map<String, List<String>> properties = Mapper.mapper.readValue(serializedProperties.getStringValue(), Mapper.propertiesType);
                    int uniqueProperties = properties.entrySet().stream()
                            .map(Map.Entry::getValue)
                            .map(list -> list.stream().distinct().count())
                            .mapToInt(Long::intValue)
                            .sum();
                    measureComputerContext.addMeasure(CssStatsMetrics.DECL_UNIQUE.getSonarMetric().getKey(), uniqueProperties);
                }
                if (serializedQueries != null) {
                    List<String> queries = Mapper.mapper.readValue(serializedQueries.getStringValue(), Mapper.stringList);
                    int uniqeQueries = (int)queries.stream()
                            .distinct()
                            .count();
                    measureComputerContext.addMeasure(CssStatsMetrics.MEDIA_QUERIES_UNIQUE.getSonarMetric().getKey(), uniqeQueries);
                }
            } catch (IOException e) {
                log.error("error while computing unique aggregates: {}", e);
            }
        }
    }
}
