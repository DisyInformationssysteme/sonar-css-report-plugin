package net.disy.sonarplugins.cssreport.cssstats;

import net.disy.sonarplugins.cssreport.cssstats.report.CssMediaQuery;
import net.disy.sonarplugins.cssreport.cssstats.report.CssProperty;
import net.disy.sonarplugins.cssreport.cssstats.report.SelectorSpecificityPair;
import net.disy.sonarplugins.cssreport.cssstats.report.Stats;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class CssStatsMetrics implements Metrics {
    public static final CssStatsJsonMetric<List<String>> SELECTOR_VALUES =
            new CssStatsJsonMetric<>(
                    new Metric.Builder("selector_values", "Selector Values", Metric.ValueType.DATA)
                            .setDescription("All used selectors as json.")
                            .setQualitative(false)
                            .setDirection(Metric.DIRECTION_NONE)
                            .setDomain(CoreMetrics.DOMAIN_COMPLEXITY)
                            .create(), stats -> stats.getSelectors().getValues());

    public static final CssStatsPrimitiveMetric<Integer> TOTAL_RULES =
            new CssStatsPrimitiveMetric<>(
                    new Metric.Builder("total_rules", "Total Rules", Metric.ValueType.INT)
                            .setDescription("Number of total css-rules used.")
                            .setQualitative(true)
                            .setDirection(Metric.DIRECTION_NONE)
                            .setDomain(CoreMetrics.DOMAIN_MAINTAINABILITY)
                            .create(), stats -> stats.getRules().getTotal());

    public static final CssStatsPrimitiveMetric<Integer> FILE_SIZE =
            new CssStatsPrimitiveMetric<>(
                    new Metric.Builder("file_size", "File Size", Metric.ValueType.INT)
                            .setDescription("The filesize in Bytes.")
                            .setQualitative(true)
                            .setDirection(Metric.DIRECTION_WORST)
                            .setDomain(CoreMetrics.DOMAIN_SIZE)
                            .create(), Stats::getSize);

    public static final CssStatsPrimitiveMetric<Integer> GZIP_SIZE =
            new CssStatsPrimitiveMetric<>(
                    new Metric.Builder("gzip_size", "Gzip Size", Metric.ValueType.INT)
                            .setDescription("The filesize after gzip in Bytes.")
                            .setQualitative(true)
                            .setDirection(Metric.DIRECTION_WORST)
                            .setDomain(CoreMetrics.DOMAIN_SIZE)
                            .create(), Stats::getGzipSize);

    public static final CssStatsJsonMetric<List<Integer>> RULE_SIZE_GRAPH =
            new CssStatsJsonMetric<>(
                    new Metric.Builder("rule_size_graph", "Rule Size Graph", Metric.ValueType.DATA)
                            .setDescription("Graph of rule sizes as json")
                            .setQualitative(false)
                            .setDirection(Metric.DIRECTION_NONE)
                            .setDomain(CoreMetrics.DOMAIN_COMPLEXITY)
                            .create(), stats -> stats.getRules().getSize().getGraph());

    public static final CssStatsPrimitiveMetric<Integer> SELECTOR_TOTAL =
            new CssStatsPrimitiveMetric<>(
                    new Metric.Builder("selector_total", "Selector Total", Metric.ValueType.INT)
                            .setDescription("Number of total selectors used.")
                            .setQualitative(true)
                            .setDirection(Metric.DIRECTION_WORST)
                            .setDomain(CoreMetrics.DOMAIN_MAINTAINABILITY)
                            .create(), stats -> stats.getSelectors().getTotal());

    public static final CssStatsPrimitiveMetric<Integer> SELECTOR_ID =
            new CssStatsPrimitiveMetric<>(
                    new Metric.Builder("selector_id", "Selector Ids", Metric.ValueType.INT)
                            .setDescription("Number of id-selectors used.")
                            .setQualitative(true)
                            .setDirection(Metric.DIRECTION_WORST)
                            .setDomain(CoreMetrics.DOMAIN_MAINTAINABILITY)
                            .create(), stats -> stats.getSelectors().getId());

    public static final CssStatsJsonMetric<List<Integer>> SPECIFICITY_GRAPH =
            new CssStatsJsonMetric<>(
                    new Metric.Builder("specificity_graph", "Specificity Graph", Metric.ValueType.DATA)
                            .setDescription("Specificity Graph data as json.")
                            .setQualitative(false)
                            .setDirection(Metric.DIRECTION_NONE)
                            .setDomain(CoreMetrics.DOMAIN_DESIGN)
                            .create(), stats -> stats.getSelectors().getSpecificity().getGraph());

    public static final CssStatsJsonMetric<List<SelectorSpecificityPair>> SPECIFICITY_VALUES =
            new CssStatsJsonMetric<>(
                    new Metric.Builder("specificity_values", "Specificity Values", Metric.ValueType.DATA)
                            .setDescription("List of selectors and specificity as json.")
                            .setQualitative(false)
                            .setDirection(Metric.DIRECTION_NONE)
                            .setDomain(CoreMetrics.DOMAIN_DESIGN)
                            .create(), stats -> stats.getSelectors().getSpecificity().getValues());

    public static final CssStatsPrimitiveMetric<Integer> DECL_TOTAL =
            new CssStatsPrimitiveMetric<>(
                    new Metric.Builder("declarations_total", "Declarations Total", Metric.ValueType.INT)
                            .setDescription("Total number of declarations.")
                            .setQualitative(true)
                            .setDirection(Metric.DIRECTION_WORST)
                            .setDomain(CoreMetrics.DOMAIN_MAINTAINABILITY)
                            .create(), stats -> stats.getDeclarations().getTotal());

    public static final CssStatsPrimitiveMetric<Integer> DECL_UNIQUE =
            new CssStatsPrimitiveMetric<>(
                    new Metric.Builder("declarations_unique", "Declarations Unique", Metric.ValueType.INT)
                            .setDescription("Total number of unique declarations.")
                            .setQualitative(true)
                            .setDirection(Metric.DIRECTION_NONE)
                            .setDomain(CoreMetrics.DOMAIN_MAINTAINABILITY)
                            .create(), stats -> stats.getDeclarations().getUnique());

    public static final CssStatsJsonMetric<List<CssProperty>> DECL_IMPORTANT =
            new CssStatsJsonMetric<>(
                    new Metric.Builder("declarations_important", "Declarations Important", Metric.ValueType.DATA)
                            .setDescription("All declarations marked with !important as json.")
                            .setQualitative(false)
                            .setDirection(Metric.DIRECTION_NONE)
                            .setDomain(CoreMetrics.DOMAIN_COMPLEXITY)
                            .create(), stats -> stats.getDeclarations().getImportant());

    public static final CssStatsJsonMetric<Map<String, List<String>>> DECL_PROPERTIES =
            new CssStatsJsonMetric<>(
                    new Metric.Builder("declarations_properties", "Declarations Properties", Metric.ValueType.DATA)
                            .setDescription("All used properties and their values as json.")
                            .setQualitative(false)
                            .setDirection(Metric.DIRECTION_NONE)
                            .setDomain(CoreMetrics.DOMAIN_COMPLEXITY)
                            .create(), stats -> stats.getDeclarations().getProperties());

    public static final CssStatsJsonMetric<Map<String, Integer>> DECL_RESETS =
            new CssStatsJsonMetric<>(
                    new Metric.Builder("declarations_resets", "Declarations Resets", Metric.ValueType.DATA)
                            .setDescription("How often is a property reset to its default value as json.")
                            .setQualitative(false)
                            .setDirection(Metric.DIRECTION_NONE)
                            .setDomain(CoreMetrics.DOMAIN_COMPLEXITY)
                            .create(), stats -> stats.getDeclarations().getResets());

    public static final CssStatsPrimitiveMetric<Integer> MEDIA_QUERIES_TOTAL =
            new CssStatsPrimitiveMetric<>(
                    new Metric.Builder("media_queries_total", "Media Queries Total", Metric.ValueType.INT)
                            .setDescription("Total number of media queries.")
                            .setQualitative(true)
                            .setDirection(Metric.DIRECTION_WORST)
                            .setDomain(CoreMetrics.DOMAIN_MAINTAINABILITY)
                            .create(), stats -> stats.getMediaQueries().getTotal());

    public static final CssStatsPrimitiveMetric<Integer> MEDIA_QUERIES_UNIQUE =
            new CssStatsPrimitiveMetric<>(
                    new Metric.Builder("media_queries_unique", "Media Queries Unique", Metric.ValueType.INT)
                            .setDescription("Total number of unique media queries.")
                            .setQualitative(false)
                            .setDirection(Metric.DIRECTION_NONE)
                            .setDomain(CoreMetrics.DOMAIN_MAINTAINABILITY)
                            .create(), stats -> stats.getMediaQueries().getUnique());

    public static final CssStatsJsonMetric<List<String>> MEDIA_QUERIES_VALUES =
            new CssStatsJsonMetric<>(
                    new Metric.Builder("media_queries_values", "Media Queries Values", Metric.ValueType.DATA)
                            .setDescription("List of all used values as json.")
                            .setQualitative(false)
                            .setDirection(Metric.DIRECTION_NONE)
                            .setDomain(CoreMetrics.DOMAIN_MAINTAINABILITY)
                            .create(), stats -> stats.getMediaQueries().getValues());

    public static final CssStatsJsonMetric<List<CssMediaQuery>> MEDIA_QUERIES_STATS =
            new CssStatsJsonMetric<>(
                    new Metric.Builder("media_queries_stats", "Media Queries Stats", Metric.ValueType.DATA)
                            .setDescription("Detailed stats for media queries")
                            .setQualitative(false)
                            .setDirection(Metric.DIRECTION_NONE)
                            .setDomain(CoreMetrics.DOMAIN_MAINTAINABILITY)
                            .create(), stats -> stats.getMediaQueries().getContents());

    public static final Metric<HashMap<String, Integer>> SELECTORS_KEY_SELECTORS =
            new Metric.Builder("selectors_key_selectors", "Key Selectors", Metric.ValueType.DATA)
                    .setDescription("Map of key-selectors to total as json.")
                    .setQualitative(false)
                    .setDirection(Metric.DIRECTION_NONE)
                    .setDomain(CoreMetrics.DOMAIN_MAINTAINABILITY)
                    .create();

    public static final Metric<ArrayList<Integer>> SELECTORS_IDENTIFIERS_PER =
            new Metric.Builder("selectors_identifiers_per", "Identifiers Per Selector", Metric.ValueType.DATA)
                    .setDescription("List of number of identifiers per selector as json.")
                    .setQualitative(false)
                    .setDirection(Metric.DIRECTION_NONE)
                    .setDomain(CoreMetrics.DOMAIN_MAINTAINABILITY)
                    .create();

    public static final Metric<Double> MEDIA_QUERIES_UNIQUE_PERCENTAGE =
            new Metric.Builder("media_queries_unique_percentage", "Percentage of unique media queries", Metric.ValueType.PERCENT)
                    .setDescription("Percentage of unique media queries")
                    .setQualitative(true)
                    .setDirection(Metric.DIRECTION_BETTER)
                    .setDomain(CoreMetrics.DOMAIN_DESIGN)
                    .create();

    public static final Metric<Double> DECL_UNIQUE_PERCENTAGE =
            new Metric.Builder("declarations_unique_percentage", "Percentage of unique Declarations", Metric.ValueType.PERCENT)
                    .setDescription("Percentage of unique declarations.")
                    .setQualitative(true)
                    .setDirection(Metric.DIRECTION_BETTER)
                    .setDomain(CoreMetrics.DOMAIN_DESIGN)
                    .create();

    public static final Metric<Double> RULE_SIZE_AVG =
            new Metric.Builder("rule_size_avg", "Average Rule Size", Metric.ValueType.FLOAT)
                    .setDescription("Average rulesize.")
                    .setQualitative(true)
                    .setDirection(Metric.DIRECTION_WORST)
                    .setDomain(CoreMetrics.DOMAIN_COMPLEXITY)
                    .create();

    public static final Metric<Double> RULE_SIZE_STDDEV =
            new Metric.Builder("rule_size_stddev", "Standard Deviation of Rule Size", Metric.ValueType.FLOAT)
                    .setDescription("Standard deviation of rulesize.")
                    .setQualitative(true)
                    .setDirection(Metric.DIRECTION_NONE)
                    .setDomain(CoreMetrics.DOMAIN_COMPLEXITY)
                    .create();

    public static final Metric<Double> SPECIFICITY_AVG =
            new Metric.Builder("specificity_avg", "Average Specificity", Metric.ValueType.FLOAT)
                    .setDescription("Average specificity.")
                    .setQualitative(true)
                    .setDirection(Metric.DIRECTION_WORST)
                    .setDomain(CoreMetrics.DOMAIN_COMPLEXITY)
                    .create();

    public static final Metric<Double> SPECIFICITY_STDDEV =
            new Metric.Builder("specificity_stddev", "Standard Deviation of Specificity", Metric.ValueType.FLOAT)
                    .setDescription("Standard deviation of specificity.")
                    .setQualitative(true)
                    .setDirection(Metric.DIRECTION_NONE)
                    .setDomain(CoreMetrics.DOMAIN_COMPLEXITY)
                    .create();

    public static final Metric<Double> SELECTORS_IDENTIFIERS_PER_AVG =
            new Metric.Builder("selectors_identifiers_per_avg", "Average Number of Identifiers per Selector", Metric.ValueType.FLOAT)
                    .setDescription("Average number of Identifiers per Selector as json.")
                    .setQualitative(true)
                    .setDirection(Metric.DIRECTION_WORST)
                    .setDomain(CoreMetrics.DOMAIN_COMPLEXITY)
                    .create();

    public static final Metric<Double> SELECTORS_IDENTIFIERS_PER_STDDEV =
            new Metric.Builder("selectors_identifiers_per_stddev", "Standard Deviation of Identifiers per Selector", Metric.ValueType.FLOAT)
                    .setDescription("Standard Deviation of Identifiers per Selector as json.")
                    .setQualitative(true)
                    .setDirection(Metric.DIRECTION_NONE)
                    .setDomain(CoreMetrics.DOMAIN_COMPLEXITY)
                    .create();

    public List<CssStatsJsonMetric> getJsonMetrics() {
        return asList(
                SELECTOR_VALUES,
                RULE_SIZE_GRAPH,
                SPECIFICITY_GRAPH,
                SPECIFICITY_VALUES,
                DECL_IMPORTANT,
                DECL_PROPERTIES,
                DECL_RESETS,
                MEDIA_QUERIES_VALUES,
                MEDIA_QUERIES_STATS);
    }

    public List<CssStatsPrimitiveMetric> getPrimitiveMetrics() {
        return asList(
                TOTAL_RULES,
                FILE_SIZE,
                GZIP_SIZE,
                SELECTOR_TOTAL,
                SELECTOR_ID,
                DECL_TOTAL,
                DECL_UNIQUE,
                MEDIA_QUERIES_TOTAL,
                MEDIA_QUERIES_UNIQUE);
    }

    public List<Metric> getComputedFileMetrics() {
        return asList(
                MEDIA_QUERIES_UNIQUE_PERCENTAGE,
                DECL_UNIQUE_PERCENTAGE,
                SELECTORS_KEY_SELECTORS,
                SELECTORS_IDENTIFIERS_PER,
                SELECTORS_IDENTIFIERS_PER_AVG,
                SELECTORS_IDENTIFIERS_PER_STDDEV,
                RULE_SIZE_AVG,
                RULE_SIZE_STDDEV,
                SPECIFICITY_AVG,
                SPECIFICITY_STDDEV
        );
    }

    public List<Metric> getMetrics() {
        return Stream.of(
                getJsonMetrics().stream().map(CssStatsJsonMetric::getSonarMetric),
                getPrimitiveMetrics().stream().map(CssStatsPrimitiveMetric::getSonarMetric),
                getComputedFileMetrics().stream())
            .flatMap(Function.identity())
            .collect(Collectors.toList());
    }
}
