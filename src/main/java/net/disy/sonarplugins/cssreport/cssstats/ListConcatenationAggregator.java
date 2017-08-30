package net.disy.sonarplugins.cssreport.cssstats;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.type.CollectionType;
import net.disy.sonarplugins.cssreport.cssstats.report.CssMediaQuery;
import net.disy.sonarplugins.cssreport.cssstats.report.CssProperty;
import org.sonar.api.ce.measure.Component;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListConcatenationAggregator implements MeasureComputer {
    private final static Logger log = Loggers.get(ListConcatenationAggregator.class);

    @Override
    public MeasureComputerDefinition define(MeasureComputerDefinitionContext measureComputerDefinitionContext) {
        return measureComputerDefinitionContext.newDefinitionBuilder()
                .setOutputMetrics(
                        CssStatsMetrics.SELECTOR_VALUES.getSonarMetric().getKey(),
                        CssStatsMetrics.RULE_SIZE_GRAPH.getSonarMetric().getKey(),
                        CssStatsMetrics.DECL_IMPORTANT.getSonarMetric().getKey(),
                        CssStatsMetrics.MEDIA_QUERIES_VALUES.getSonarMetric().getKey(),
                        CssStatsMetrics.MEDIA_QUERIES_STATS.getSonarMetric().getKey()
                ).build();
    }

    @Override
    public void compute(MeasureComputerContext measureComputerContext) {
        concatList(measureComputerContext, CssStatsMetrics.SELECTOR_VALUES.getSonarMetric().getKey(), String.class);
        concatList(measureComputerContext, CssStatsMetrics.RULE_SIZE_GRAPH.getSonarMetric().getKey(), Integer.class);
        concatList(measureComputerContext, CssStatsMetrics.DECL_IMPORTANT.getSonarMetric().getKey(), CssProperty.class);
        concatList(measureComputerContext, CssStatsMetrics.MEDIA_QUERIES_VALUES.getSonarMetric().getKey(), String.class);
        concatList(measureComputerContext, CssStatsMetrics.MEDIA_QUERIES_STATS.getSonarMetric().getKey(), CssMediaQuery.class);
    }

    public static <T>void concatList(MeasureComputerContext ctx, String key, Class<T> clazz) {
        ArrayList<T> accumulator = new ArrayList<>();
        if (ctx.getComponent().getType() != Component.Type.FILE) {
            for (Measure child : ctx.getChildrenMeasures(key)) {
                try {
                    CollectionType type = Mapper.mapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
                    List<T> values = Mapper.mapper.readValue(child.getStringValue(), type);
                    accumulator.addAll(values);
                } catch (IOException e) {
                    log.error("error concatenating list: {}", e);
                }
            }
            try {
                ctx.addMeasure(key, Mapper.mapper.writeValueAsString(accumulator));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
}
