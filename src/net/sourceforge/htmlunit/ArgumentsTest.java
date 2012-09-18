package net.sourceforge.htmlunit;

import org.junit.Assert;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;

/**
 * Tests for Arguments object.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class ArgumentsTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _toString() throws Exception {
        _toString(true, "[object Object]");
        _toString(false, "[object Arguments]");
    }

    private void _toString(final boolean hasFeature, final String expected) throws Exception {

        final ContextFactory cf = new ContextFactory() {
            @Override
            protected boolean hasFeature(Context cx, int featureIndex) {
                if (Context.FEATURE_HTMLUNIT_ARGUMENTS_IS_OBJECT == featureIndex) {
                    return hasFeature;
                }
                return super.hasFeature(cx, featureIndex);
            }
        };
        final String script = "function f() {\n"
                + "  output = arguments.toString();\n"
                + "}\n"
                + "var output = '';\n"
                + "f();\n"
                + "output";
        final ContextAction action = new ContextAction() {
            public Object run(final Context cx) {
                final Scriptable scope = cx.initStandardObjects();
                final Object result = cx.evaluateString(scope, script, "test.js", 1, null);
                Assert.assertEquals(expected, result);
                return null;
            }
        };

        Utils.runWithAllOptimizationLevels(cf, action);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void readonly() throws Exception {
        readonly(true, "2-world-undefined-undefined");
        readonly(false, "2-hi-undefined-you");
    }

    private void readonly(final boolean hasFeature, final String expected) throws Exception {

        final ContextFactory cf = new ContextFactory() {
            @Override
            protected boolean hasFeature(Context cx, int featureIndex) {
                if (Context.FEATURE_HTMLUNIT_ARGUMENTS_IS_READ_ONLY == featureIndex) {
                    return hasFeature;
                }
                return super.hasFeature(cx, featureIndex);
            }
        };
        final String script = "function test() {\n"
                + "  test1('hello', 'world');\n"
                + "}\n"
                + "function test1() {\n"
                + "  test1.arguments[1] = 'hi';\n"
                + "  test1.arguments[3] = 'you';\n"
                + "  output += test1.arguments.length + '-';\n"
                + "  output += test1.arguments[1] + '-';\n"
                + "  output += test1.arguments[2] + '-';\n"
                + "  output += test1.arguments[3];\n"
                + "}\n"
                + "var output = '';\n"
                + "test();\n"
                + "output";
        final ContextAction action = new ContextAction() {
            public Object run(final Context cx) {
                final Scriptable scope = cx.initStandardObjects();
                final Object result = cx.evaluateString(scope, script, "test.js", 1, null);
                Assert.assertEquals(expected, result);
                return null;
            }
        };

        Utils.runWithAllOptimizationLevels(cf, action);
    }
}
