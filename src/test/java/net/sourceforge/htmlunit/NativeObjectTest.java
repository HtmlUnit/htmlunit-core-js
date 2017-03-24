package net.sourceforge.htmlunit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.EcmaError;
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
        getPrototypeOfString("", "''", true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test(expected = EcmaError.class)
    public void getPrototypeOfStringFalse() throws Exception {
        getPrototypeOfString("", "''", false);
    }

    public void getPrototypeOfString(final Object expected, final String value, final boolean contextFeature) throws Exception {
        final ContextFactory myContextFactory = new ContextFactory() {
            @Override
            protected boolean hasFeature(final Context cx, final int featureIndex) {
                if (Context.FEATURE_HTMLUNIT_GET_PROTOTYPE_OF_STRING == featureIndex) {
                    return contextFeature;
                }
                return super.hasFeature(cx, featureIndex);
            };
        };

        final String script = "Object.getPrototypeOf(" + value + ")";
        
        final ContextAction action = new ContextAction() {
            @Override
            public Object run(final Context cx) {
                final Scriptable scope = cx.initStandardObjects();
                final Object result = cx.evaluateString(scope, script, "test.js", 1, null);
                assertEquals(expected, result);
                return null;
            }
        };

       Utils.runWithAllOptimizationLevels(myContextFactory, action);
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
        getPrototypeOfString(false, "true", true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test(expected = EcmaError.class)
    public void getPrototypeOfBooleanFalse() throws Exception {
        getPrototypeOfString("", "true", false);
    }
}
