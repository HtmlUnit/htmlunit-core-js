package net.sourceforge.htmlunit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

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
        final ContextAction action = new ContextAction() {
            @Override
            public Object run(final Context cx) {
                final Scriptable scope = cx.initStandardObjects();
                Object result = cx.evaluateString(scope, src, "test.js", 0, null);
                assertEquals(expected, result);
                return null;
            }
        };
        Utils.runWithAllOptimizationLevels(action);
    }

}
