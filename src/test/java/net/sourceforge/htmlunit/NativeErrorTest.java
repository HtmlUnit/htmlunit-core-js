package net.sourceforge.htmlunit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

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
                assertEquals("true", result);
                return null;
            }
        };

        Utils.runWithAllOptimizationLevels(action);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void stackNewError() throws Exception {
        final ContextFactory cf = new ContextFactory();
        final String script = "function test() {\n"
                + "  try {\n"
                + "    throw new Error();\n"
                + "  } catch (e) {\n"
                + "    if (e.stack)\n"
                + "      output += typeof e.stack;\n"
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
                assertEquals("string", result);
                return null;
            }
        };

        Utils.runWithAllOptimizationLevels(cf, action);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void stackNewErrorWithoutThrow() throws Exception {
        stackNewErrorWithoutThrow(true, "string");
        stackNewErrorWithoutThrow(false, "undefined");
    }

    private static void stackNewErrorWithoutThrow(final boolean hasFeature, final String expected) throws Exception {
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
                + "  var e = new Error();\n"
                + "  if (e.stack)\n"
                + "    output += typeof e.stack;\n"
                + "  else\n"
                + "    output += 'undefined';\n"
                + "}\n"
                + "var output = '';\n"
                + "test();\n"
                + "output";
        
        final ContextAction action = new ContextAction() {
            @Override
            public Object run(final Context cx) {
                final Scriptable scope = cx.initStandardObjects();
                final Object result = cx.evaluateString(scope, script, "test.js", 1, null);
                assertEquals(expected, result);
                return null;
            }
        };

        Utils.runWithAllOptimizationLevels(cf, action);
    }

}
