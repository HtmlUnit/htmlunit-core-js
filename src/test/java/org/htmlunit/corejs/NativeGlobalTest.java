package org.htmlunit.corejs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.ContextAction;
import org.htmlunit.corejs.javascript.Scriptable;
import org.junit.jupiter.api.Test;

/**
 * Tests for NativeGlobal object.
 *
 * @author Ahmed Ashour
 */
public class NativeGlobalTest {

    /**
     * Test for parseInt.
     */
    @Test
    public void parseInt() throws Exception {
        doTest(8, "parseInt('08')");

        doTest(0, "parseInt('08', 8)");

        doTest(Double.NaN, "parseInt('8', 8)");

        doTest(8, "parseInt('8')");
    }

    private static void doTest(final double expected, final String src) throws Exception {
        final ContextAction<Object> action = new ContextAction<Object>() {
            @Override
            public Object run(final Context cx) {
                // no need to do that because we use a patched version
                // cx.setLanguageVersion(Context.VERSION_ES6);

                final Scriptable scope = cx.initSafeStandardObjects();
                Object result = cx.evaluateString(scope, src, "test.js", 0, null);
                assertEquals(expected, result);
                return null;
            }
        };
        Utils.runWithAllOptimizationLevels(action);
    }

}
