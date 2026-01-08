/**
 * @author Ronald Brill
 * @since 5.0
 */
module org.htmlunit.corejs {
    requires java.desktop;

    exports org.htmlunit.corejs.javascript;

    // Service Loader Support
    uses org.htmlunit.corejs.javascript.NullabilityDetector;
    uses org.htmlunit.corejs.javascript.config.RhinoPropertiesLoader;
    uses org.htmlunit.corejs.javascript.xml.XMLLoader;
}