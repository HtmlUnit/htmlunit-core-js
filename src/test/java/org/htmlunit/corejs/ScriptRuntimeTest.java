package org.htmlunit.corejs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.ContextAction;
import org.htmlunit.corejs.javascript.Scriptable;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link org.mozilla.javascript.ScriptRuntime}
 *
 * @author Ahmed Ashour
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
        test(script, "function foo() {}");
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
        test(script, "undefined");
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
        test(script, "function foo() {}");
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
        test(script, "0,50,100,xxx,zzz,yyy,");
    }

    private static void test(final String script, final Object expected) {
        final ContextAction<Object> action = new ContextAction<Object>() {
            @Override
            public Object run(final Context cx) {
                try {
                    Scriptable scope = cx.initSafeStandardObjects();
                    final Object o = cx.evaluateString(scope, script, "test_script", 1, null);
                    assertEquals(expected, o);
                    return o;
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Utils.runWithAllOptimizationLevels(action);
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

        final String expected = "2, eat, bananas";
        test(script, expected);
    }
}
