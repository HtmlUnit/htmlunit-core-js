/**
 * @author Ronald Brill
 * @since 5.0
 */
module org.htmlunit.corejs {
    requires java.desktop;

    exports org.htmlunit.corejs.javascript;
    exports org.htmlunit.corejs.javascript.debug;
    exports org.htmlunit.corejs.javascript.json;
    exports org.htmlunit.corejs.javascript.regexp;
    exports org.htmlunit.corejs.javascript.tools.debugger;
    exports org.htmlunit.corejs.javascript.typedarrays;

    // Service Loader Support
    uses org.htmlunit.corejs.javascript.NullabilityDetector;
    uses org.htmlunit.corejs.javascript.config.RhinoPropertiesLoader;
    uses org.htmlunit.corejs.javascript.xml.XMLLoader;
}