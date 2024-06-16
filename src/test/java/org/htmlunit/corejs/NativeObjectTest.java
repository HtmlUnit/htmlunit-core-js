package org.htmlunit.corejs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.ContextAction;
import org.htmlunit.corejs.javascript.Scriptable;
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

        final ContextAction<Object> action = new ContextAction<Object>() {
            @Override
            public Object run(final Context cx) {
                final Scriptable scope = cx.initSafeStandardObjects();
                final Object result = cx.evaluateString(scope, script, "test.js", 1, null);
                assertEquals("1", result);
                return null;
            }
        };

       Utils.runWithAllOptimizationLevels(action);
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

        final ContextAction<Object> action = new ContextAction<Object>() {
            @Override
            public Object run(final Context cx) {
                final Scriptable scope = cx.initSafeStandardObjects();
                final Object result = cx.evaluateString(scope, script, "test.js", 1, null);
                assertEquals("undefined", result);
                return null;
            }
        };

       Utils.runWithAllOptimizationLevels(action);
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

        final ContextAction<Object> action = new ContextAction<Object>() {
            @Override
            public Object run(final Context cx) {
                try {
                    Scriptable scope = cx.initSafeStandardObjects();
                    final Object o = cx.evaluateString(scope, script, "test_script", 1, null);
                    assertEquals("before: [object Object] / after: [object Object] / true", o);
                    return o;
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Utils.runWithAllOptimizationLevels(action);
    }
}
