# sonar-css-report-plugin

This plugin takes advantage of the rich JavaScript eco-system for css-analysis.
It allows you to read reports generated
by
[Stylelint](https://github.com/stylelint/stylelint),
[doiuse](https://github.com/anandthakker/doiuse)
and [cssstats](https://github.com/cssstats/core).
`Stylelint` and `doiuse` reports generate issues while `cssstats` reports
generate metrics.

## Other CSS-Plugins

[sonar-css-plugin](https://github.com/racodond/sonar-css-plugin) utilizes a
CSS-parser and implements its own checks. Since we use `Stylelint` during
development we wanted to leverage it and use the same tool during development
and build.

[sonar-web-frontend-plugin](https://github.com/groupe-sii/sonar-web-frontend-plugin) uses
the same process as this plugin but does not support current Sonar versions.

## Building

```sh
mvn clean package
```

## Report Format

The structure for every kind of report is the same:
```
{
  "files": [
    "path": "<absolute path to source file>",
    "<data>": ...
  ]
}
```

### The Different Objects used for `"<data>"`

`Stylelint` and `doiuse`:
```
"issues": [
  {
    "line": number,
    "message": string,
    "rulekey": string
  },
  ...
]
```

`cssstats`:
```
"stats": {
  "size": number,
  "gzipSize": number,
  "rules": {
    "total": number,
    "size": {
      "graph": [ number ],
      "max": number,
      "average": number
    }
  },
  "selectors": {
    "total": number,
    "id": number,
    "values": [ string ],
    "specificity": {
      "graph": [ number ],
      "values": [ { "selector": string, "specificity": number } ]
    }
  },
  "declarations": {
    "total": number,
    "unique": number,
    "important": [ { "property": string, "value": "string" } ],
    "properties": {
      "<property-name>": [ string ]
    },
    "resets": {
      "<property-name>": number
    }
  },
  "mediaQueries": {
    "total": number,
    "unique": number,
    "values": [ string ],
    "contents": [
      {
        "value": string,
        "rules": {
          "total": number,
          "size": {
            "graph": [ number ],
            "max": number,
            "average": number
          }
        },
        "selectors": {
          "total": number,
          "id": number,
          "values": [ string ],
          "specificity": {
            "graph": [ number ],
            "values": [ { "selector": string, "specificity": number } ]
          }
        },
        "declarations": {
          "total": number,
          "unique": number,
          "important": [ { "property": string, "value": "string" } ],
          "properties": {
            "<property-name>": [ string ]
          },
          "resets": {
            "<property-name>": number
          }
        }
      }
    ]
  }
}
```
## Create Reports

To create the different reports we use a custom `node.js` script.

## Configuration

The following options can be configured through the SonarQube UI:

| Key | Type | Default | Description |
|--|--|--|--|
|`sonar.css.report.doiuse.enabled` | `boolean` | `false` | Are `doiuse` reports processed. |
|`sonar.css.report.doiuse.report.path` | `String` | `/target/css-reports/doiuse.json` | The location of the report |
|`sonar.css.report.doiuse.rule.path` | `String` | | An optional path for custom rule definitions |
|`sonar.css.report.stylelint.enabled` | `boolean` | `false` | Are `Stylelint` reports processed. |
|`sonar.css.report.stylelint.report.path` | `String` | `/target/css-reports/stylelint.json` | The location of the report |
|`sonar.css.report.stylelint.rule.path` | `String` | | An optional path for custom rule definitions |
| `sonar.css.report.cssstats.enabled` | `boolean` |`false` | Are `cssstats` report processed. |
|`sonar.css.report.cssstats.report.path` | `String` | `/target/css-reports/cssstats.json` | The location of the report |

## Metrics

- **Key:** key used for metric in SonarQube
- **Java-Type:** type used to represent metric
- **Aggregation Method:** method used to aggregat file-metrics to dir-metrics
  and so forth
- **Description:** description of metric

| Key | Java-Type | Aggregation Method | Description |
|--|--|--|--|
| `selector_values` | `List<String>` | Concatenation | all used selectors |
| `total_rules` | `Integer` | Sum | Total rules |
| `file_size` | `Integer` | Sum | filesize in bytes |
| `gzip_size` | `Integer` | Sum | filesize gzipped in bytes |
| `rule_size_graph` |`List<Integer>` | Concatenation | list of rulesizes (number of declarations) |
| `selector_total` | `Integer` | Sum | number of selectors |
| `selector_id` | `Integer` | Sum | number of id-selectors |
| `specificity_graph` | `List<Integer>` | - | list of specificity values in source file order |
| `specificity_values` | `List<SelectorSpecificityPair>` | - | list of selectors and corresponding specificity in source file order |
| `declarations_total` | `Integer` | Sum | number of declarations |
| `declarations_unique` | `Integer` | Recalculation | number of unique declarations |
| `declarations_important` | `List<CssProperty>` | Concatenation | list of property-value-pairs marked with `!important` |
| `declarations_properties` | `Map<String, List<String>>` | Merge with Concatenation | properties with a list of all their values |
| `declarations_resets` | `Map<String, Integer`> | Merge with Sum | properties with their reset count |
| `media_queries_total` | `Integer` | Sum | number of media queries |
| `media_queries_unique` | `Integer` | Sum | number of unique media queries |
| `media_queries_values` | `List<String>` | Concatenation | list of all media queries |
| `media_queries_stats` | `List<CssMediaQuery>` | Concatenation | list of all media queries stats |
| `selectors_key_selectors` | `Map<String, Integer>` | Merge with Sum | key selectors (last selector in complex selector) with count |
| `selectors_identifiers_per` | `List<Integer>` | Concatenation | count of selectors per complex selector |
| `media_queries_unique_percentage` | `Double` | Recalculation | ratio of unique to total media queries |
| `declarations_unique_percentage` | `Double` | Recalculation | ratio of unique to total declarations |
| `rule_size_avg` | `Double` | Recalculation | average number of declarations per rule |
| `rule_size_stddev` |`Double` | Recalculation | standard deviation of declarations per rule |
| `specificity_avg` | `Double` | Recalculation | average specificity |
| `specificity_stddev` | `Double` | Recalculation | standard deviation of specificity |
| `selectors_identifiers_per_avg` | `Double` | Recalculation | average number of simple selectors per complex selector |
| `selectors_identifiers_per_stddec` | `Double` | Recalculation | standard deviation of number of simple selectors per complex selector |

## Terminology

Rule:
```css
.tabs li {
  background-color: red;
}
```
- Complex Selector: `.tabs li`
- Key Selector: `li`
- Declaration: `background-color: red;`

## Development

During development we used a Docker container to host SonarQube ([SonarQube
Docker image](https://store.docker.com/images/sonarqube)). 

To deploy the plugin to the container use:
```sh
docker cp ./target/sonar-css-report-plugin-1.0-SNAPSHOT.jar sonarqube:/opt/sonarqube/extensions/plugins/
```

To debug the plugin use `mvnDebug sonar:sonar` to run analysis and attach a
debugger.

When changing the Page build the plugin with `mvn clean package -P dev` und run
`yarn run start` in `src/main/frontend`. This will include `entry-dev.js` as
`index.js`.
`entry-dev.js` loads `localhost:8080/build/bundle.js` (the file served by
webpack-dev-server). This allows changes to take effect without repackaging the
whole plugin.

## TODOs

### TODO manage Todos as issues

### TODO remove `specificity_graph`

`specificity_graph` isn't needed anymore. `specificity_values` contains the same
information. Calculated Metrics, which depend on `specificity_graph` should use
`specificity_values`. After this `specificity_graph` can be removed.

### TODO Differentiate Key-Selectors by type (id, class, ...)
