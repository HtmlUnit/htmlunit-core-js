package org.htmlunit.corejs;

import org.junit.jupiter.api.Test;
import org.htmlunit.corejs.DelegatorAndHostObjectTest.MyHostObject;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.ContextAction;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;

/**
 * Tests for NativeArray object.
 *
 * @author Ahmed Ashour
 */
public class NativeArrayTest {

    static {
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
    }
    /**
     * Test for "Comparison method violates its general contract!".
     * @throws Exception if the test fails
     */
    @Test
    public void java7Sort() {
        test("var results = [1, 1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 1, 1, 1, 1, 1, 1, 1, -1, -1, 1, -1, -1, 1,"
            + " -1, -1, 1, -1, 1, -1, 1, 1, 1, 1, -1, -1, 1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, 1, -1,"
            + " 1, -1, -1, 1, 1, -1, -1, 1, 1, 1, 1, 1, 1, -1, -1, 0, -1, -1, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0,"
            + " 1, 1, 1, 1, 1, 1, 1, 1, 1, -1, -1, -1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1, 1, 1, 1, 1,"
            + " 1, 1, 1, -1, -1, -1, -1, -1, -1, -1, -1, 1, 1, -1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1];\n"
            + "var index = 0;"
            + "    var arr = new Array(37);\n"
            + "    for (var x = 0; x < arr.length; x++) {\n"
            + "        arr[x] = new Object();\n"
            + "    }\n"
            + "    arr.sort(function (a, b) {\n"
            + "        return results[index++];\n"
            + "    });\n"
        );
    }

    private static void test(final String script) {
        final ContextAction<Object> action = new ContextAction<Object>() {
            @Override
            public Object run(final Context cx) {
                try {
                    Scriptable scope = cx.initSafeStandardObjects();
                    ScriptableObject.defineClass(scope, MyHostObject.class);
                    final Object o = cx.evaluateString(scope, script, "test_script", 1, null);
                    return o;
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Utils.runWithAllOptimizationLevels(action);
    }
}
