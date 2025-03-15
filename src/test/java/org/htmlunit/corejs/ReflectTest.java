package org.htmlunit.corejs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.ContextAction;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
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

        test(script, "foo - 1 2 ");
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

        test(script, "foo - 6 7 8 ");
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
}
