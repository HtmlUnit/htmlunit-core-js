package org.htmlunit.corejs;

import org.htmlunit.corejs.javascript.testutils.Utils;
import org.junit.jupiter.api.Test;

/**
 * Test that encodeURIComponent uses upper case letters for hex encoding.
 * https://bugzilla.mozilla.org/show_bug.cgi?id=429121
 * (now fixed in Rhino itself)
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class UpperCaseHexTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testUpperCaseHex() throws Exception {
        Utils.assertWithAllModes_ES6("%24%20a%20%3B", "encodeURIComponent('$ a ;');");
    }
}
