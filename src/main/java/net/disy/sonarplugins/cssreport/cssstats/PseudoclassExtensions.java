package net.disy.sonarplugins.cssreport.cssstats;

import jodd.csselly.selector.PseudoClass;
import jodd.csselly.selector.PseudoClassSelector;
import jodd.lagarto.dom.Node;

public class PseudoclassExtensions {
    public static void register() {
        PseudoClassSelector.registerPseudoClass(Link.class);
        PseudoClassSelector.registerPseudoClass(Active.class);
        PseudoClassSelector.registerPseudoClass(Before.class);
        PseudoClassSelector.registerPseudoClass(Disabled.class);
        PseudoClassSelector.registerPseudoClass(Hover.class);
        PseudoClassSelector.registerPseudoClass(Visited.class);
        PseudoClassSelector.registerPseudoClass(_Ms_clear.class);
        PseudoClassSelector.registerPseudoClass(After.class);
        PseudoClassSelector.registerPseudoClass(Enabled.class);
        PseudoClassSelector.registerPseudoClass(Focus.class);
        PseudoClassSelector.registerPseudoClass(_Moz_Focus_Inner.class);
    }
    public static class _Moz_Focus_Inner extends PseudoClass {
        @Override
        public boolean match(Node node) {
            return false;
        }
    }
    public static class Focus extends PseudoClass {
        @Override
        public boolean match(Node node) {
            return false;
        }
    }
    public static class Enabled extends PseudoClass {
        @Override
        public boolean match(Node node) {
            return false;
        }
    }
    public static class After extends PseudoClass {
        @Override
        public boolean match(Node node) {
            return false;
        }
    }
    public static class _Ms_clear extends PseudoClass {
        @Override
        public boolean match(Node node) {
            return false;
        }
    }
    public static class Link extends PseudoClass {
        @Override
        public boolean match(Node node) {
            return false;
        }
    }
    public static class Active extends PseudoClass{
        @Override
        public boolean match(Node node) {
            return false;
        }
    }
    public static class Before extends PseudoClass{
        @Override
        public boolean match(Node node) {
            return false;
        }
    }
    public static class Disabled extends PseudoClass {

        @Override
        public boolean match(Node node) {
            return node.hasAttribute("disabled");
        }
    }
    public static class Hover extends PseudoClass{
        @Override
        public boolean match(Node node) {
            return false;
        }
    }
    public static class Visited extends PseudoClass{
        @Override
        public boolean match(Node node) {
            return false;
        }
    }
}
