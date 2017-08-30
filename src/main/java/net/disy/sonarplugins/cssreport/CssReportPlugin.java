package net.disy.sonarplugins.cssreport;

import net.disy.sonarplugins.cssreport.cssstats.*;
import net.disy.sonarplugins.cssreport.doiuse.DoiuseConstants;
import net.disy.sonarplugins.cssreport.doiuse.DoiuseReportSensor;
import net.disy.sonarplugins.cssreport.doiuse.DoiuseRulesDefinition;
import net.disy.sonarplugins.cssreport.stylelint.StyleLintConstants;
import net.disy.sonarplugins.cssreport.stylelint.StyleLintReportSensor;
import net.disy.sonarplugins.cssreport.stylelint.StyleLintRulesDefinition;
import org.sonar.api.Plugin;
import org.sonar.api.config.PropertyDefinition;

import java.util.List;

public class CssReportPlugin implements Plugin {

    public void define(Context context) {
        PseudoclassExtensions.register();
        context
            .addExtension(CssLanguage.class)
            .addExtension(DoiuseReportSensor.class)
            .addExtension(DoiuseRulesDefinition.class)
            .addExtension(StyleLintReportSensor.class)
            .addExtension(StyleLintRulesDefinition.class)
            .addExtension(CssReportRuleProfile.class)
            .addExtension(new RuleProfileRegistry())
            .addExtension(CssStatsMetrics.class)
            .addExtension(CssStatsSensor.class)
            .addExtension(ListConcatenationAggregator.class)
            .addExtension(ComputeFileMetrics.class)
            .addExtension(SumAggregator.class)
            .addExtension(UniqueAggregator.class)
            .addExtension(MergeMapAggregator.class)
            .addExtension(CssMetricsIndexPage.class)
            .addExtension(CssStatsConstants.ENABLED)
            .addExtension(CssStatsConstants.REPORT_PATH);
        SharedPropertiesHelper propHelper = new SharedPropertiesHelper();
        List<PropertyDefinition> sharedProperties = propHelper.getSharedProperties(
                new DoiuseConstants(),
                new StyleLintConstants());
        for (PropertyDefinition prop : sharedProperties) {
            context.addExtension(prop);
        }
        //TODO move non general stuff to separate classes
    }

}
