package org.htmlunit.corejs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;
import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.ContextAction;
import org.htmlunit.corejs.javascript.ContextFactory;
import org.htmlunit.corejs.javascript.EcmaError;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;

/**
 * Test that exception messages contain rich information.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class ExceptionMessageTest {

    @BeforeAll
    public static void setUp() throws Exception {
        Locale.setDefault(Locale.ENGLISH); // to be sure that error messages are in English
    }

    /**
     * Unit test for bug 608235
     * https://bugzilla.mozilla.org/show_bug.cgi?id=608235
     */
    @Test
    public void undefinedFromUndefined() {
        exceptionMessage("undefined[undefined]", "TypeError: Cannot read property \"undefined\" from undefined");
        exceptionMessage("undefined[undefined] = 1", "TypeError: Cannot set property \"undefined\" of undefined to \"1\"");
        exceptionMessage("undefined.undefined()", "TypeError: Cannot call method \"undefined\" of undefined");
    }

    private static void exceptionMessage(final String script, final String expectedMesage) {
        final ContextAction<Object> action = new ContextAction<Object>() {
            @Override
            public Object run(final Context cx) {
                try {
                    Scriptable scope = cx.initSafeStandardObjects();

                    cx.evaluateString(scope, script, "test_script", 1, null);
                    throw new RuntimeException("Should have failed!");
                }
                catch (final EcmaError e) {
                    assertEquals(expectedMesage + " (test_script#1)", e.getMessage());
                    return null;
                }
                catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Utils.runWithAllOptimizationLevels(action);
    }
}
