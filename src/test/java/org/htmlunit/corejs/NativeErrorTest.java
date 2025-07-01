package org.htmlunit.corejs;

import org.htmlunit.corejs.javascript.ContextFactory;
import org.junit.jupiter.api.Test;

/**
 * Tests for NativeError object.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
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
                + "      output += e.stack.indexOf('\tat test.js:2 (test)') != -1;\n"
                + "    else\n"
                + "      output += 'undefined';\n"
                + "  }\n"
                + "}\n"
                + "var output = '';\n"
                + "test();\n"
                + "output";

        Utils.assertWithAllModes_ES6("true", script);
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

        Utils.assertWithAllModes_ES6("string", script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void stackNewErrorWithoutThrow() throws Exception {
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

        Utils.assertWithAllModes_ES6("string", script);
    }

}
