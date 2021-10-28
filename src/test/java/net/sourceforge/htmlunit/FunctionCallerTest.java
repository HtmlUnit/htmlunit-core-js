package net.sourceforge.htmlunit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * Test for a basic implementation of caller property on functions.
 * Implementation is not yet good enough to propose a patch to Rhino.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class FunctionCallerTest {

    /**
     * This was failing probably as consequence of making inner functions available first
     * when the line of their declaration has been reached.
     * @throws Exception if the test fails
     */
    @Test
    public void upperScopeVarShouldntBeSetWithVarFunctionWithSameName() throws Exception {
        final String script = "var g = true; (function() { var g = function g() { return 1; };\n"
                + "return this.g == undefined })();g;\n";
        final ContextAction<Object> action = new ContextAction<Object>() {
            @Override
            public Object run(final Context cx) {
                final Scriptable scope = cx.initSafeStandardObjects();
                final Object result = cx.evaluateString(scope, script, "test.js", 1, null);
                assertEquals(Boolean.TRUE, result);
                return null;
            }
        };

        Utils.runWithOptimizationLevel(action, -1);
    }

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
                + "function g() {\n"
                + "  return f();\n"
                + "}\n"
                + "g();\n";
        final ContextAction<Object> action = new ContextAction<Object>() {
            @Override
            public Object run(final Context cx) {
                final Scriptable scope = cx.initSafeStandardObjects();
                final Object result = cx.evaluateString(scope, script, "test.js", 1, null);
                assertEquals("", result);
                return null;
            }
        };

        Utils.runWithAllOptimizationLevels(action);
    }

    /**
     * Property caller should be defined only during function execution
     */
    @Test
    public void callee() throws Exception {
        final String script = "function f() {\n"
            + "  var c = arguments.callee.caller;\n"
            + "  if (c) throw 'got: ' + c;\n"
            + "  if (g.caller) throw 'got: ' + c;\n"
            + "  g();\n"
            + "  if (g.caller) throw 'after g() got: ' + g.caller;\n"
            + "}\n"
            + "function g() {\n"
            + "  var c = arguments.callee.caller;\n"
            + "  if (c != f) throw 'got: ' + c;\n"
            + "}\n"
            + "f();\n";

        final ContextAction<Object> action = new ContextAction<Object>() {
            @Override
            public Object run(final Context cx) {
                final Scriptable scope = cx.initSafeStandardObjects();

                cx.evaluateString(scope, script, "test.js", 1, null);

                return null;
            }
        };
        Utils.runWithOptimizationLevel(action, -1);
    }

    /**
     * Tests the caller arguments
     */
    @Test
    public void callerArguments() throws Exception {
        final String script =
              "function f() {\n"
            + "  g('hello', 'world');\n"
            + "}\n"
            + "function g(hi, now) {\n"
            + "  h('hi', 'there', 'yet')\n"
            + "}\n"
            + "function h(hi, hehe, there) {\n"
            + "  output += g.arguments.length + '-';\n"
            + "  output += arguments.callee.caller.arguments.length;\n"
            + "}\n"
            + "var output = '';\n"
            + "f();\n"
            + "output";

        final ContextAction<Object> action = new ContextAction<Object>() {
            @Override
            public Object run(final Context cx) {
                final Scriptable scope = cx.initSafeStandardObjects();

                final Object result = cx.evaluateString(scope, script, "test.js", 1, null);
                assertEquals("2-2", result);
                return null;
            }
        };
        Utils.runWithOptimizationLevel(action, -1);
    }

    /**
     * Tests the caller arguments
     */
    @Test
    public void arguments() throws Exception {
        final String script =
              "function f() {\n"
            + "  g('hello', 'world');\n"
            + "}\n"
            + "function g() {\n"
            + "  output += g.arguments.length + '-';\n"
            + "  output += g.arguments[0] + '-';\n"
            + "  h('i', 'you');\n"
            + "}\n"
            + "function h() {\n"
            + "  output += g.arguments.length;\n"
            + "}\n"
            + "var output = '';\n"
            + "f();\n"
            + "output";

        final ContextAction<Object> action = new ContextAction<Object>() {
            @Override
            public Object run(final Context cx) {
                final Scriptable scope = cx.initSafeStandardObjects();

                final Object result = cx.evaluateString(scope, script, "test.js", 1, null);
                assertEquals("2-hello-2", result);
                return null;
            }
        };
        Utils.runWithAllOptimizationLevels(action);
    }

    /**
     * Tests the caller arguments
     */
    @Test
    public void callerArgumentsCallee() throws Exception {
        final String script = "function f() {\n"
            + "  var caller = arguments.callee.caller;\n"
            + "  output += (caller == g) + ', ';\n"
            + "  output += (caller.arguments.callee == g);\n"
            + "}\n"
            + "function g() {\n"
            + "  f(123)\n"
            + "}\n"
            + "var output = '';\n"
            + "g();\n"
            + "output";

        final ContextAction<Object> action = new ContextAction<Object>() {
            @Override
            public Object run(final Context cx) {
                final Scriptable scope = cx.initSafeStandardObjects();

                final Object result = cx.evaluateString(scope, script, "test.js", 1, null);
                assertEquals("true, true", result);
                return null;
            }
        };
        Utils.runWithOptimizationLevel(action, -1);
    }
}
