package net.disy.sonarplugins.cssreport.cssstats;

import com.fasterxml.jackson.databind.ObjectMapper;
import jodd.csselly.CSSelly;
import jodd.csselly.CssSelector;
import org.sonar.api.ce.measure.Component;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.IOException;
import java.util.*;

//TODO rename class
public class ComputeFileMetrics implements MeasureComputer {
    private final static ObjectMapper mapper = new ObjectMapper();
    private final static Logger log = Loggers.get(ComputeFileMetrics.class);
    @Override
    public MeasureComputerDefinition define(MeasureComputerDefinitionContext measureComputerDefinitionContext) {
        return measureComputerDefinitionContext.newDefinitionBuilder()
                .setInputMetrics(
                        CssStatsMetrics.DECL_TOTAL.getSonarMetric().getKey(),
                        CssStatsMetrics.DECL_UNIQUE.getSonarMetric().getKey(),
                        CssStatsMetrics.MEDIA_QUERIES_TOTAL.getSonarMetric().getKey(),
                        CssStatsMetrics.MEDIA_QUERIES_UNIQUE.getSonarMetric().getKey(),
                        CssStatsMetrics.SELECTOR_VALUES.getSonarMetric().getKey(),
                        CssStatsMetrics.RULE_SIZE_GRAPH.getSonarMetric().getKey(),
                        CssStatsMetrics.SPECIFICITY_GRAPH.getSonarMetric().getKey()
                )
                .setOutputMetrics(
                        CssStatsMetrics.MEDIA_QUERIES_UNIQUE_PERCENTAGE.getKey(),
                        CssStatsMetrics.DECL_UNIQUE_PERCENTAGE.getKey(),
                        CssStatsMetrics.SELECTORS_KEY_SELECTORS.getKey(),
                        CssStatsMetrics.SELECTORS_IDENTIFIERS_PER.getKey(),
                        CssStatsMetrics.SELECTORS_IDENTIFIERS_PER_AVG.getKey(),
                        CssStatsMetrics.SELECTORS_IDENTIFIERS_PER_STDDEV.getKey(),
                        CssStatsMetrics.RULE_SIZE_AVG.getKey(),
                        CssStatsMetrics.RULE_SIZE_STDDEV.getKey(),
                        CssStatsMetrics.SPECIFICITY_AVG.getKey(),
                        CssStatsMetrics.SPECIFICITY_STDDEV.getKey()
                ).build();
    }

    @Override
    public void compute(MeasureComputerContext measureComputerContext) {
        computeSelectorMetrics(measureComputerContext);
        computeDeclUnique(measureComputerContext);
        computeMediaQueriesUnique(measureComputerContext);
        computeRuleSizeMetrics(measureComputerContext);
        computeSpecificityMetrics(measureComputerContext);
    }

    private void computeMediaQueriesUnique(MeasureComputerContext ctx) {
        computePercentage(
                ctx,
                CssStatsMetrics.MEDIA_QUERIES_TOTAL.getSonarMetric().getKey(),
                CssStatsMetrics.MEDIA_QUERIES_UNIQUE.getSonarMetric().getKey(),
                CssStatsMetrics.MEDIA_QUERIES_UNIQUE_PERCENTAGE.getKey()
        );
    }

    private void computeDeclUnique(MeasureComputerContext ctx) {
        computePercentage(
                ctx,
                CssStatsMetrics.DECL_TOTAL.getSonarMetric().getKey(),
                CssStatsMetrics.DECL_UNIQUE.getSonarMetric().getKey(),
                CssStatsMetrics.DECL_UNIQUE_PERCENTAGE.getKey()
        );
    }

    private void computeSelectorMetrics(MeasureComputerContext ctx) {
        Measure values = ctx.getMeasure(CssStatsMetrics.SELECTOR_VALUES.getSonarMetric().getKey());
        if (values == null) {
            return;
        }
        try {
            String[] deserialized = mapper.readValue(values.getStringValue(), String[].class);
            if (deserialized.length == 0) {
                return;
            }
            Map<String, Integer> keySelectors = new HashMap<>();
            List<Integer> selectorCount = new ArrayList<>(deserialized.length);
            for (String selector : deserialized) {
                List<CssSelector> parsed = new CSSelly(selector).parse();
                String keySelector = parsed.get(parsed.size() - 1).toString();
                Integer value = keySelectors.getOrDefault(keySelector, 0);
                value++;
                keySelectors.put(keySelector, value);
                selectorCount.add(parsed.size());
            }
            if (ctx.getComponent().getType() == Component.Type.FILE) {
                ctx.addMeasure(CssStatsMetrics.SELECTORS_KEY_SELECTORS.getKey(), mapper.writeValueAsString(keySelectors));
                ctx.addMeasure(CssStatsMetrics.SELECTORS_IDENTIFIERS_PER.getKey(), mapper.writeValueAsString(selectorCount));
            } else {
                MergeMapAggregator.mergeStringIntMap(ctx, CssStatsMetrics.SELECTORS_KEY_SELECTORS.getKey());
                ListConcatenationAggregator.concatList(ctx, CssStatsMetrics.SELECTORS_IDENTIFIERS_PER.getKey(), Integer.class);
            }
            computeAvgStddev(ctx,
                    selectorCount,
                    CssStatsMetrics.SELECTORS_IDENTIFIERS_PER_AVG.getKey(),
                    CssStatsMetrics.SELECTORS_IDENTIFIERS_PER_STDDEV.getKey());
        } catch (IOException e) {
            log.error("error calculating key selectors: {}", e);
        }
    }

    private void computeRuleSizeMetrics(MeasureComputerContext ctx) {
        Measure ruleSizes = ctx.getMeasure(CssStatsMetrics.RULE_SIZE_GRAPH.getSonarMetric().getKey());
        if (ruleSizes == null) {
            return;
        }
        try {
            List<Integer> values = Arrays.asList(mapper.readValue(ruleSizes.getStringValue(), Integer[].class));
            computeAvgStddev(ctx,
                    values,
                    CssStatsMetrics.RULE_SIZE_AVG.getKey(),
                    CssStatsMetrics.RULE_SIZE_STDDEV.getKey());
        } catch (IOException e) {
            log.error("error calculating rule size metrics: {}", e);
        }
    }

    private void computeSpecificityMetrics(MeasureComputerContext ctx) {
        Measure specificities = ctx.getMeasure(CssStatsMetrics.SPECIFICITY_GRAPH.getSonarMetric().getKey());
        if (specificities == null) {
            return;
        }
        try {
            List<Integer> values = Arrays.asList(mapper.readValue(specificities.getStringValue(), Integer[].class));
            computeAvgStddev(ctx,
                    values,
                    CssStatsMetrics.SPECIFICITY_AVG.getKey(),
                    CssStatsMetrics.SPECIFICITY_STDDEV.getKey());
        } catch (IOException e) {
            log.error("error calculating rule size metrics: {}", e);
        }
    }

    private void computePercentage(MeasureComputerContext ctx, String totalKey, String uniqueKey, String percentageKey) {
        Measure total = ctx.getMeasure(totalKey);
        Measure unique = ctx.getMeasure(uniqueKey);
        if (total == null || unique == null) {
            return;
        }
        if (total.getIntValue() == 0 || unique.getIntValue() == 0) {
            return;
        }
        double percentage = (double)unique.getIntValue() / total.getIntValue();
        percentage *= 100;
        ctx.addMeasure(percentageKey, percentage);
    }

    private void computeAvgStddev(MeasureComputerContext ctx, List<Integer> values, String avgKey, String stddevKey) {
        double average = values.stream()
                .mapToDouble(a -> a)
                .average().orElse(0);
        double stddev = values.stream()
                .mapToDouble(a -> a)
                .map(a -> Math.pow(a - average, 2))
                .sum() / (values.size() - 1);
        if (Double.isNaN(stddev)) {
            stddev = 0;
        }
        ctx.addMeasure(avgKey, average);
        ctx.addMeasure(stddevKey, stddev);
    }
}
