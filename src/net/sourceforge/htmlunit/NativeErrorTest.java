package net.sourceforge.htmlunit;

import org.junit.Assert;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;

/**
 * Tests for NativeError object.
 *
 * @author Ahmed Ashour
 */
public class NativeErrorTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void stack() throws Exception {
        stack(true, "true");
        stack(false, "undefined");
    }

    private void stack(final boolean hasFeature, final String expected) throws Exception {
        final ContextFactory cf = new ContextFactory() {
            @Override
            protected boolean hasFeature(Context cx, int featureIndex) {
                if (Context.FEATURE_HTMLUNIT_ERROR_STACK == featureIndex) {
                    return hasFeature;
                }
                return super.hasFeature(cx, featureIndex);
            }
        };
        final String script = "function test() {\n"
                + "  try {\n"
                + "    null.method();\n"
                + "  } catch (e) {\n"
                + "    if (e.stack)\n"
                + "      output += e.stack.indexOf('test()@') != -1;\n"
                + "    else\n"
                + "      output += 'undefined';\n"
                + "  }\n"
                + "}\n"
                + "var output = '';\n"
                + "test();\n"
                + "output";
        
        final ContextAction action = new ContextAction() {
            @Override
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
