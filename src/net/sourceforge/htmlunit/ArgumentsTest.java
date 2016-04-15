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
    public void notEnumerated() throws Exception {
        final String script = "function f() {\n"
                + "  var s = '';\n"
                + "  for (i in f) s += i + ',';\n"
                + "  return s;\n"
                + "}\n"
                + "f();\n";
        runScript(script, "");
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
        runScript(script, null);
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

        runScript(script, "[object Object]", Context.FEATURE_HTMLUNIT_ARGUMENTS_IS_OBJECT, true);
        runScript(script, "[object Arguments]", Context.FEATURE_HTMLUNIT_ARGUMENTS_IS_OBJECT, false);
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

        runScript(script, "2-hi-undefined-you", Context.FEATURE_HTMLUNIT_FN_ARGUMENTS_IS_RO_VIEW, true);
        runScript(script, "2-hi-undefined-you", Context.FEATURE_HTMLUNIT_FN_ARGUMENTS_IS_RO_VIEW, false);
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

        runScript(script, "2-world-undefined-undefined", Context.FEATURE_HTMLUNIT_FN_ARGUMENTS_IS_RO_VIEW, true);
        runScript(script, "2-hi-undefined-you", Context.FEATURE_HTMLUNIT_FN_ARGUMENTS_IS_RO_VIEW, false);
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

        runScript(script, false, Context.FEATURE_HTMLUNIT_FN_ARGUMENTS_IS_RO_VIEW, true);
        runScript(script, true, Context.FEATURE_HTMLUNIT_FN_ARGUMENTS_IS_RO_VIEW, false);
    }

    private void runScript(final String script, final Object expectedResult) {
    	runScript(script, expectedResult, Integer.MAX_VALUE, false);
	}

    private void runScript(final String script, final Object expectedResult,
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
        final ContextAction action = new ContextAction() {
            @Override
            public Object run(final Context cx) {
                final Scriptable scope = cx.initStandardObjects();
                final Object result = cx.evaluateString(scope, script, "test.js", 1, null);
                Assert.assertEquals(expectedResult, result);
                return null;
            }
        };

        Utils.runWithOptimizationLevel(cf, action, -1);
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

        test(script, "hello");
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

        test(script, "object");
    }

    private void test(final String script, final Object expected) {
        final ContextAction action = new ContextAction() {
            @Override
            public Object run(final Context cx) {
                try {
                    Scriptable scope = cx.initStandardObjects();
                    final Object o = cx.evaluateString(scope, script, "test_script", 1, null);
                    Assert.assertEquals(expected, o);
                    return o;
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Utils.runWithAllOptimizationLevels(action);
    }
}
