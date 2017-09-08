package net.disy.sonarplugins.cssreport.cssstats;

import org.sonar.api.measures.Metric;

import java.io.Serializable;

public interface CssStatsMetric<T extends Serializable> {
    Metric<T> getSonarMetric();
}
