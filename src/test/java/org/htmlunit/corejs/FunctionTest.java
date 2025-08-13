package org.htmlunit.corejs;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for Function.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
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
        Utils.assertWithAllModes_ES6(true, "new Function('return true//;').call()");
    }

    @Test
    public void varDefinedLater() {
        String script = "var output = '';\n"
                + "output += t;\n"
                + "var t = 1;\n"
                + "output";

        Utils.assertWithAllModes_ES6("undefined", script);
    }

    @Test
    public void varUsedLater() {
        String script = "var output = '';\n"
                + "output += t;\n"
                + "!function t() {output += 'hi';}();\n"
                + "output";

        Utils.assertEcmaErrorES6("ReferenceError: \"t\" is not defined. (test#2)", script);
    }

    @Test
    public void functionHasNameOfVar() {
        String script = "var output = '';\n"
                + "output += t;\n"
                + "var t = 1;\n"
                + "!function t() {output += 'hi';}();\n"
                + "output";

        Utils.assertWithAllModes_ES6("undefinedhi", script);
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

        Utils.assertWithAllModes_ES6("undefinedhi", script);
    }

    @Test
    public void applyThisFromBoundArgs() throws Exception {
        String script =
                "var f = function(x) { return this.toString(); };\n"
                + "var a = f.apply;\n"
                + "var b = a.bind(f, 'Hello!');\n"
                + "b([1,2]);";

        Utils.assertWithAllModes_ES6("Hello!", script);
    }

    @Test
    public void applyToApplyCallsCorrectFunction() throws Exception {
        String script =
                "function foo(x) {return x;};\n"
                + "Function.prototype.apply.apply(foo, ['b', ['Hello!', 'Goodbye!']]);\n";

        Utils.assertWithAllModes_ES6("Hello!", script);
    }

    @Test
    public void applyToApplySetsCorrectFunctionThis() throws Exception {
        String script =
                "function foo(x) {return this.toString();};\n"
                + "Function.prototype.apply.apply(foo, ['b', ['Hello!', 'Goodbye!']]);\n";

        Utils.assertWithAllModes_ES6("b", script);
    }

    @Test
    public void applyToCallCallsCorrectFunction() throws Exception {
        String script =
                "function foo(x) {return x;};\n"
                + "foo.call.apply(foo, ['b', 'Hello!']);\n";

        Utils.assertWithAllModes_ES6("Hello!", script);
    }

    @Test
    public void applyToCallSetsCorrectFunctionThis() throws Exception {
        String script =
                "function foo(x) {return this.toString();};\n"
                + "foo.call.apply(foo, ['b', 'Hello!']);\n";

        Utils.assertWithAllModes_ES6("b", script);
    }
}
