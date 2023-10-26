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
}
