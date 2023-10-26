package org.htmlunit.corejs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.ContextAction;
import org.htmlunit.corejs.javascript.EcmaError;
import org.htmlunit.corejs.javascript.Scriptable;

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
        final ContextAction<Object> action = new ContextAction<Object>() {
            @Override
            public Object run(Context cx) {
                final Scriptable scope = cx.initSafeStandardObjects();
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

    @Test
    public void varUsedLater() {
        String script = "var output = '';\n"
                + "output += t;\n"
                + "!function t() {output += 'hi';}();\n"
                + "output";
        assertThrows(EcmaError.class, () -> assertEvaluates("undefined", script));
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

    @Test
    public void functionHasNameOfVarInsideFunction() {
        String script = "var output = '';\n"
                + "function test() {\n"
                + "  output += t;\n"
                + "  var t = 1;\n"
                + "  !function t() {output += 'hi';}();\n"
                + "}\n"
                + "test();\n"
                + "output";
        assertEvaluates("undefinedhi", script);
    }
}
