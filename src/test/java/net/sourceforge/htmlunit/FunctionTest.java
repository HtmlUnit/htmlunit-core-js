package net.sourceforge.htmlunit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.Scriptable;

/**
 * Unit tests for Function.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class FunctionTest {
    
	/**
	 * Test for bug #600479
	 * https://bugzilla.mozilla.org/show_bug.cgi?id=600479
	 * Syntax of function built from Function's constructor string parameter was not correct
	 * when this string contained "//".  
	 */
	@Test
    public void testFunctionWithSlashSlash() {
        assertEvaluates(true, "new Function('return true//;').call()");
    }

    private static void assertEvaluates(final Object expected, final String source) {
        final ContextAction action = new ContextAction() {
            @Override
            public Object run(Context cx) {
                final Scriptable scope = cx.initStandardObjects();
                final Object rep = cx.evaluateString(scope, source, "test.js", 0, null);
                assertEquals(expected, rep);
                return null;
            }
        };
        Utils.runWithAllOptimizationLevels(action);
    }

    @Test
    public void varDefinedLater() {
        String script = "var output = '';\n"
                + "output += t;\n"
                + "var t = 1;\n"
                + "output";
        assertEvaluates("undefined", script);
    }

    @Test(expected = EcmaError.class)
    public void varUsedLater() {
        String script = "var output = '';\n"
                + "output += t;\n"
                + "!function t() {output += 'hi';}();\n"
                + "output";
        assertEvaluates("undefined", script);
    }

    @Test
    public void functionHasNameOfVar() {
        String script = "var output = '';\n"
                + "output += t;\n"
                + "var t = 1;\n"
                + "!function t() {output += 'hi';}();\n"
                + "output";
        assertEvaluates("undefinedhi", script);
    }

}
