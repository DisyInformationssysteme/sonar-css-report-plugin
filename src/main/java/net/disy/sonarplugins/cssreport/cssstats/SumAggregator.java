package net.disy.sonarplugins.cssreport.cssstats;

import org.sonar.api.ce.measure.Component;
import org.sonar.api.ce.measure.Measure;
import org.sonar.api.ce.measure.MeasureComputer;

public class SumAggregator implements MeasureComputer{
    private static String[] metrics = {
            CssStatsMetrics.TOTAL_RULES.getSonarMetric().getKey(),
            CssStatsMetrics.FILE_SIZE.getSonarMetric().getKey(),
            CssStatsMetrics.SELECTOR_TOTAL.getSonarMetric().getKey(),
            CssStatsMetrics.SELECTOR_ID.getSonarMetric().getKey(),
            CssStatsMetrics.DECL_TOTAL.getSonarMetric().getKey(),
            CssStatsMetrics.MEDIA_QUERIES_TOTAL.getSonarMetric().getKey()
    };

    @Override
    public MeasureComputerDefinition define(MeasureComputerDefinitionContext measureComputerDefinitionContext) {
        return measureComputerDefinitionContext.newDefinitionBuilder()
                .setOutputMetrics(metrics).build();
    }

    @Override
    public void compute(MeasureComputerContext measureComputerContext) {
        if (measureComputerContext.getComponent().getType() != Component.Type.FILE) {
            for (String metric : metrics) {
                int sum = 0;
                for (Measure child : measureComputerContext.getChildrenMeasures(metric)) {
                    sum += child.getIntValue();
                }
                measureComputerContext.addMeasure(metric, sum);
            }
        }
    }
}
