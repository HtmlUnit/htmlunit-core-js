package org.htmlunit.corejs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.ContextAction;
import org.htmlunit.corejs.javascript.ContextFactory;
import org.htmlunit.corejs.javascript.Scriptable;
import org.junit.jupiter.api.Test;

/**
 * Tests for Arguments object.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class ArgumentsTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void notEnumerated() throws Exception {
        final String script = "function f() {\n"
                + "  var s = '';\n"
                + "  for (i in f) s += i + ',';\n"
                + "  return s;\n"
                + "}\n"
                + "f();\n";

        Utils.assertWithAllModes_ES6("", script);
    }


    /**
     * @throws Exception if the test fails
     */
    @Test
    public void argumentsShouldBeNullOutsideOfAFunction() throws Exception {
        final String script = "function f() {\n"
                + "}\n"
                + "f(1, 2);\n"
                + "f.arguments";

        Utils.assertWithAllModes_ES6(null, script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void _toString() throws Exception {
        final String script = "function f() {\n"
                + "  output = arguments.toString();\n"
                + "}\n"
                + "var output = '';\n"
                + "f();\n"
                + "output";

        Utils.assertWithAllModes_ES6("[object Arguments]", script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void writableWithinFunction() throws Exception {
        final String script = "function test1() {\n"
                + "  arguments[1] = 'hi';\n"
                + "  arguments[3] = 'you';\n"
                + "  output += arguments.length + '-';\n"
                + "  output += arguments[1] + '-';\n"
                + "  output += arguments[2] + '-';\n"
                + "  output += arguments[3];\n"
                + "}\n"
                + "var output = '';\n"
                + "test1('hello', 'world');\n"
                + "output";

        // we do a hard coded replacement
        // runScript(script, "2-hi-undefined-you", Context.FEATURE_HTMLUNIT_FN_ARGUMENTS_IS_RO_VIEW, true);
        // runScript(script, "2-hi-undefined-you", Context.FEATURE_HTMLUNIT_FN_ARGUMENTS_IS_RO_VIEW, false);
        Utils.assertWithAllModes_ES6("2-hi-undefined-you", script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void readonlyWhenAccessThroughFunction() throws Exception {
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

        // we do a hard coded replacement
        // runScript(script, "2-world-undefined-undefined", Context.FEATURE_HTMLUNIT_FN_ARGUMENTS_IS_RO_VIEW, true);
        // runScript(script, "2-hi-undefined-you", Context.FEATURE_HTMLUNIT_FN_ARGUMENTS_IS_RO_VIEW, false);
        Utils.assertWithAllModes_ES6("2-world-undefined-undefined", script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void equalsWithFnArguments() throws Exception {
        final String script = "function test1() {\n"
                + "  return arguments == test1.arguments;\n"
                + "}\n"
                + "test1('hello', 'world')";

        // we do a hard coded replacement
        // runScript(script, false, Context.FEATURE_HTMLUNIT_FN_ARGUMENTS_IS_RO_VIEW, true);
        // runScript(script, true, Context.FEATURE_HTMLUNIT_FN_ARGUMENTS_IS_RO_VIEW, false);
        Utils.assertWithAllModes_ES6(false, script);
    }

    private static void runScript(final String script, final Object expectedResult,
            final int featureToSet, final boolean featureValue) {
        final ContextFactory cf = new ContextFactory() {
            @Override
            protected boolean hasFeature(final Context cx, final int featureIndex) {
                if (featureToSet == featureIndex) {
                    return featureValue;
                }
                return super.hasFeature(cx, featureIndex);
            }
        };

        final ContextAction<Object> action = new ContextAction<Object>() {
            @Override
            public Object run(final Context cx) {
                final Scriptable scope = cx.initSafeStandardObjects();
                final Object result = cx.evaluateString(scope, script, "test.js", 1, null);
                assertEquals(expectedResult, result);
                return null;
            }
        };

        Utils.runWithAllModes(cf, action);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void override() throws Exception {
        final String script = "function test1(arguments) {\n"
                + "  return arguments;\n"
                + "}\n"
                + "test1('hello')";

        Utils.assertWithAllModes_ES6("hello", script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void normal() throws Exception {
        final String script = "function test1(hi) {\n"
                + "  return typeof arguments;\n"
                + "}\n"
                + "test1('hello')";

        Utils.assertWithAllModes_ES6("object", script);
    }
}
