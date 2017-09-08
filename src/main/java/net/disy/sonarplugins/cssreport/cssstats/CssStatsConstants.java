package net.disy.sonarplugins.cssreport.cssstats;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;

public class CssStatsConstants {
    public static final String SETTINGS_ENABLED_KEY = "sonar.css.report.cssstats.enabled";
    public static final String SETTINGS_REPORT_PATH_KEY = "sonar.css.report.cssstats.report.path";

    public static final PropertyDefinition ENABLED = PropertyDefinition.builder(SETTINGS_ENABLED_KEY)
            .name("Enable CssStats report parsing.")
            .type(PropertyType.BOOLEAN)
            .defaultValue("false")
            .onQualifiers(Qualifiers.PROJECT, Qualifiers.MODULE)
            .build();

    public static final PropertyDefinition REPORT_PATH = PropertyDefinition.builder(SETTINGS_REPORT_PATH_KEY)
            .name("Path to the CssStats report file.")
            .type(PropertyType.STRING)
            .defaultValue("/target/css-reports/cssstats.json")
            .onQualifiers(Qualifiers.PROJECT, Qualifiers.MODULE)
            .build();
}
