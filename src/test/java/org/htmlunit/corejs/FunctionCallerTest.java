package org.htmlunit.corejs;

import org.htmlunit.corejs.javascript.Undefined;
import org.junit.jupiter.api.Test;

/**
 * Test for a basic implementation of caller property on functions.
 * Implementation is not yet good enough to propose a patch to Rhino.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
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

        Utils.assertWithAllModes_ES6(Boolean.TRUE, script);
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

        Utils.assertWithAllModes_ES6("", script);
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

        Utils.assertWithAllModes_ES6(Undefined.instance, script);
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

        Utils.assertWithAllModes_ES6("2-2", script);
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

        Utils.assertWithAllModes_ES6("2-hello-2", script);
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

        Utils.assertWithAllModes_ES6("true, true", script);
    }
}
