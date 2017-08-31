package net.disy.sonarplugins.cssreport.cssstats;

import net.disy.sonarplugins.cssreport.Mapper;
import org.sonar.api.ce.measure.Component;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MergeMapAggregator implements MeasureComputer {
    private final static Logger log = Loggers.get(MergeMapAggregator.class);
    private final static String propertiesKey = CssStatsMetrics.DECL_PROPERTIES.getSonarMetric().getKey();
    private final static String resetsKey = CssStatsMetrics.DECL_RESETS.getSonarMetric().getKey();

    @Override
    public MeasureComputerDefinition define(MeasureComputerDefinitionContext measureComputerDefinitionContext) {
        return measureComputerDefinitionContext.newDefinitionBuilder()
                .setOutputMetrics(
                        propertiesKey,
                        resetsKey
                ).build();
    }

    @Override
    public void compute(MeasureComputerContext measureComputerContext) {
        if (measureComputerContext.getComponent().getType() != Component.Type.FILE) {
            try {
                Map<String, List<String>> propAccu = new HashMap<>();
                for (Measure measure : measureComputerContext.getChildrenMeasures(propertiesKey)) {
                    Map<String, List<String>> properties = Mapper.mapper.readValue(measure.getStringValue(), Mapper.propertiesType);
                    for (String key : properties.keySet()) {
                        List<String> values = propAccu.getOrDefault(key, new ArrayList<>());
                        values.addAll(properties.get(key));
                        propAccu.put(key, values);
                    }
                }
                measureComputerContext.addMeasure(propertiesKey, Mapper.mapper.writeValueAsString(propAccu));
            } catch (IOException e) {
                log.error("error while merging map: {}", e);
            }
            mergeStringIntMap(measureComputerContext, resetsKey);
        }
    }

    public static void mergeStringIntMap(MeasureComputerContext ctx, String measureKey) {
        try {
            Map<String, Integer> accu = new HashMap<>();
            for (Measure measure : ctx.getChildrenMeasures(measureKey)) {
                Map<String, Integer> resets = Mapper.mapper.readValue(measure.getStringValue(), Mapper.stringIntMap);
                for (String key : resets.keySet()) {
                    Integer value = accu.getOrDefault(key, 0);
                    value += resets.get(key);
                    accu.put(key, value);
                }
            }
            ctx.addMeasure(measureKey, Mapper.mapper.writeValueAsString(accu));
        } catch (IOException e) {
            log.error("error while merging map of string to int: {}",e);
        }
    }
}
