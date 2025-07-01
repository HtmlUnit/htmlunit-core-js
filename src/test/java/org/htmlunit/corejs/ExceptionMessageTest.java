package org.htmlunit.corejs;

import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
        Utils.assertEcmaErrorES6(
                "TypeError: Cannot read property \"undefined\" from undefined",
                "undefined[undefined]");
        Utils.assertEcmaErrorES6(
                "TypeError: Cannot set property \"undefined\" of undefined to \"1\"",
                "undefined[undefined] = 1");
        Utils.assertEcmaErrorES6(
                "TypeError: Cannot call method \"undefined\" of undefined",
                "undefined.undefined()");
    }
}
