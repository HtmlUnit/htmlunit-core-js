package net.sourceforge.htmlunit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.regexp.SubString;

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
        assertTrue(SubString.emptySubString.toString().isEmpty());
        test(script, Boolean.TRUE);
        assertTrue(SubString.emptySubString.toString().isEmpty());
    }

    private static void test(final String script, final Object expected) {
        final ContextAction action = new ContextAction() {
            @Override
            public Object run(final Context cx) {
                try {
                    Scriptable scope = cx.initStandardObjects();
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
