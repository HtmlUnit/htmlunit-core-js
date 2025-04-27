package org.htmlunit.corejs;

import org.htmlunit.corejs.javascript.testutils.Utils;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=538172">Bug 538172 -
 * Number.toString fails for 2.274341322658976E-309</a>
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class NumberTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void test() throws Exception {
        final String script = "var n = 2.274341322658976E-309;\n"
            + "'' + n;";

        Utils.assertWithAllModes_ES6("2.274341322658976e-309", script);
        Utils.assertWithAllModes_ES6("1", "'' + 1");
    }
}
