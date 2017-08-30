package net.disy.sonarplugins.cssreport.cssstats;

import org.sonar.api.web.page.Context;
import org.sonar.api.web.page.Page;
import org.sonar.api.web.page.PageDefinition;

public class CssMetricsIndexPage implements PageDefinition {
    @Override
    public void define(Context context) {
        context.addPage(Page.builder("cssreport/index")
                .setName("Css Metrics")
                .setScope(Page.Scope.COMPONENT)
                .setComponentQualifiers(Page.Qualifier.PROJECT, Page.Qualifier.MODULE)
                .build());
    }
}
