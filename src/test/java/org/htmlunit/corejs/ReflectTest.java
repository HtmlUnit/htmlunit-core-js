package org.htmlunit.corejs;

import org.htmlunit.corejs.javascript.testutils.Utils;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for NativeReflect
 *
 * @author Ronald Brill
 */
public class ReflectTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void constructor() {
        final String script =
                "  var res = '';\n"
                + "function foo(a, b) {\n"
                + "  res += 'foo - ';\n"
                + "  for (let i = 0; i < arguments.length; i++) {\n"
                + "    res += arguments[i] + ' ';\n"
                + "  }\n"
                + "}\n"

                + "Reflect.construct(foo, [1, 2]);\n"
                + "res;";

        Utils.assertWithAllModes_ES6("foo - 1 2 ", script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void constructorWithTarger() {
        final String script =
                "  var res = '';\n"
                + "function foo(a, b) {\n"
                + "  res += 'foo - ';\n"
                + "  for (let i = 0; i < arguments.length; i++) {\n"
                + "    res += arguments[i] + ' ';\n"
                + "  }\n"
                + "}\n"
                + "function bar(a, b) {\n"
                + "  res += 'bar - ';\n"
                + "  for (let i = 0; i < arguments.length; i++) {\n"
                + "    res += arguments[i] + ' ';\n"
                + "  }\n"
                + "}\n"

                + "Reflect.construct(foo, [6, 7, 8], bar);\n"
                + "res;";

        Utils.assertWithAllModes_ES6("foo - 6 7 8 ", script);
    }
}
