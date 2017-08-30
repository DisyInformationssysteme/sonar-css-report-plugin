package net.disy.sonarplugins.cssreport;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;

import java.util.ArrayList;
import java.util.List;

public class SharedPropertiesHelper {

    public List<PropertyDefinition> getSharedProperties(CssReportConstants... constants) {
        List<PropertyDefinition> result = new ArrayList<>(constants.length * 3);
        for (CssReportConstants c : constants) {
            result.add(PropertyDefinition.builder(c.getSettingKeyEnabled())
                    .name("enable " + c.getToolName() + " report parsing")
                    .type(PropertyType.BOOLEAN)
                    .defaultValue("false")
                    .build());
            result.add(PropertyDefinition.builder(c.getSettingKeyReportPath())
                    .name(c.getToolName() + " report path")
                    .type(PropertyType.STRING)
                    .defaultValue("/target/css-reports/" + c.getToolName().toLowerCase() + ".json")
                    .build());
            result.add(PropertyDefinition.builder(c.getSettingKeyRulePath())
                    .name(c.getToolName() + " additional rules path")
                    .type(PropertyType.STRING)
                    .defaultValue("")
                    .build());
        }
        return result;
    }
}
