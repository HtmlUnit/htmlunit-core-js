package org.htmlunit.corejs;

import org.htmlunit.corejs.javascript.testutils.Utils;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link org.mozilla.javascript.ScriptRuntime}
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class ScriptRuntimeTest {

    @Test
    public void functionDeclaredForwardInBlock() {
        final String script =
                  "function test () {\n"
                + "  if (true) {\n"
                + "    try {\n"
                + "      output += '' + foo;\n"
                + "    } catch (e) {\n"
                + "      output += 'exception';\n"
                + "    }\n"
                + "    function foo() {}\n"
                + "  }\n"
                + "};\n"
                + "var output = '';\n"
                + "test();\n"
                + "output";

        Utils.assertWithAllModes_ES6("function foo() {}", script);
    }

    @Test
    public void functionDeclaredForwardInBlockAsVar() {
        final String script =
                  "function test () {\n"
                + "  if (true) {\n"
                + "    try {\n"
                + "      output += '' + foo;\n"
                + "    } catch (e) {\n"
                + "      output += 'exception';\n"
                + "    }\n"
                + "    var foo = function() {}\n"
                + "  }\n"
                + "};\n"
                + "var output = '';\n"
                + "test();\n"
                + "output";

        Utils.assertWithAllModes_ES6("undefined", script);
    }

    @Test
    public void functionDeclaredForwardInBlock2() {
        final String script =
                  "var output = '';\n"
                + "if (true) {\n"
                + "  try {\n"
                + "    output += '' + foo;\n"
                + "  } catch (e) {\n"
                + "    output += 'exception';\n"
                + "  }\n"
                + "  function foo() {}\n"
                + "}\n"
                + "output";

        Utils.assertWithAllModes_ES6("function foo() {}", script);
    }

    @Test
    public void enumChangeObject() {
        final String script =
                "var value = {\n"
                + "'xxx': 'testxxx',\n"
                + "'50': 'test50',\n"
                + "'zzz': 'testzzz',\n"
                + "'100': 'test100',\n"
                + "'0': 'test0',\n"
                + "'yyy': 'testyyy'\n"
                + "};\n"
                + " var output = '';\n"
                + "for (var x in value) {\n"
                + "   output += x + ',';\n"
                + "};"
                + "output";

        Utils.assertWithAllModes_ES6("0,50,100,xxx,zzz,yyy,", script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void apply() throws Exception {
        final String script =
                  "  var myObject = {'length': 2, '0': 'eat', '1': 'bananas'};\n"
                + "  function test() {\n"
                + "    test2.apply(null, myObject);\n"
                + "  }\n"
                + "\n"
                + "  function test2() {\n"
                + "    output += arguments.length;\n"
                + "    for (var i in arguments) {\n"
                + "      output += ', ' + arguments[i];\n"
                + "    }\n"
                + "  }\n"
                + "var output = '';\n"
                + "test();"
                + "output";

        Utils.assertWithAllModes_ES6("2, eat, bananas", script);
    }
}
