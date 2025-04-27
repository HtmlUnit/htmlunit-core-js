package org.htmlunit.corejs;

import org.htmlunit.corejs.javascript.testutils.Utils;
import org.junit.jupiter.api.Test;

/**
 * Tests for NativeObject.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class NativeObjectTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void assign() throws Exception {
        final String script = "function test() {\n"
                + "  var obj = { a: 1 };\n"
                + "  var copy = Object.assign({}, obj);\n"
                + "  output += copy.a;\n"
                + "}\n"
                + "var output = '';\n"
                + "test();\n"
                + "output";

        Utils.assertWithAllModes_ES6("1", script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void assignNull() throws Exception {
        final String script = "function test() {\n"
                + "  var copy = Object.assign({}, null);\n"
                + "  output += copy.a;\n"
                + "}\n"
                + "var output = '';\n"
                + "test();\n"
                + "output";

        Utils.assertWithAllModes_ES6("undefined", script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void definePropertyUsingConsString() throws Exception {
        final String script =
            "  'use strict';\n"
            + "  var log = '';\n"
            + "  var f = function () {};\n"
            + "  var a1='proto';\n"
            + "  var p = a1 + 'type';\n"
            + "  log = log + 'before: ' + f.prototype;\n"
            + "  Object.defineProperty(f, p, {});\n"
            + "  log = log + ' / after: ' + f.prototype;\n"
            + "  var p = new f();\n"
            + "  log = log + ' / ' + (p instanceof f);";

        Utils.assertWithAllModes_ES6("before: [object Object] / after: [object Object] / true", script);
    }
}
