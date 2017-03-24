package net.sourceforge.htmlunit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.Scriptable;

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
        
        final ContextAction action = new ContextAction() {
            @Override
            public Object run(final Context cx) {
                final Scriptable scope = cx.initStandardObjects();
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
    public void getPrototypeOfString() throws Exception {
        final String script = "Object.getPrototypeOf('')";
        
        final ContextAction action = new ContextAction() {
            @Override
            public Object run(final Context cx) {
                final Scriptable scope = cx.initStandardObjects();
                final Object result = cx.evaluateString(scope, script, "test.js", 1, null);
                assertEquals("", result);
                return null;
            }
        };

       Utils.runWithAllOptimizationLevels(action);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getPrototypeOfNumber() throws Exception {
        final String script = "Object.getPrototypeOf(1)";
        
        final ContextAction action = new ContextAction() {
            @Override
            public Object run(final Context cx) {
                final Scriptable scope = cx.initStandardObjects();
                final Object result = cx.evaluateString(scope, script, "test.js", 1, null);
                assertEquals(0, result);
                return null;
            }
        };

       Utils.runWithAllOptimizationLevels(action);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getPrototypeOfBoolean() throws Exception {
        final String script = "Object.getPrototypeOf(true)";
        
        final ContextAction action = new ContextAction() {
            @Override
            public Object run(final Context cx) {
                final Scriptable scope = cx.initStandardObjects();
                final Object result = cx.evaluateString(scope, script, "test.js", 1, null);
                assertFalse((Boolean) result);
                return null;
            }
        };

       Utils.runWithAllOptimizationLevels(action);
    }
}
