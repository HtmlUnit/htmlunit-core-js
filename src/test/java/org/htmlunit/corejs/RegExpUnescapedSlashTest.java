package org.htmlunit.corejs;

import org.htmlunit.corejs.javascript.Undefined;
import org.junit.jupiter.api.Test;

/**
 * Test that regexp like "[/]" are parsed correctly.
 * https://bugzilla.mozilla.org/show_bug.cgi?id=368019
 * (now fixed in Rhino itself)
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class RegExpUnescapedSlashTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testUnescapedSlashInSquareBrackets() throws Exception {
        final String script = "var o = /[/]/;";

        Utils.assertWithAllModes_ES6(Undefined.instance, script);
    }
}
