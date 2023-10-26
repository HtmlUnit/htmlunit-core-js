package org.htmlunit.corejs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.ContextAction;
import org.htmlunit.corejs.javascript.Scriptable;
import org.junit.jupiter.api.Test;

/**
 * Tests for NativeRegExp object.
 *
 * @author Ahmed Ashour
 */
public class NativeRegExpTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void empty() throws Exception {
        final String script = "new RegExp().test('AA')";
        test(script, Boolean.TRUE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void undefined() throws Exception {
        final String script = "new RegExp(undefined).test('AA')";
        test(script, Boolean.TRUE);
    }

    /**
     * Test for bug #1706.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void changed() throws Exception {
        final String script = "'alpha'.replace(/alpha/, '');/beta/.test('abc beta def');";
        test(script, Boolean.TRUE);
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
