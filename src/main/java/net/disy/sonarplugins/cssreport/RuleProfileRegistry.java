package net.disy.sonarplugins.cssreport;

import org.sonar.api.rules.Rule;
import org.sonar.api.server.ServerSide;

import java.util.HashSet;
import java.util.Set;

@ServerSide
public class RuleProfileRegistry {
    private final Set<Rule> rules = new HashSet<>();

    public void add(Rule rule) {
        rules.add(rule);
    }

    public Iterable<Rule> getRules() {
        return rules;
    }
}
