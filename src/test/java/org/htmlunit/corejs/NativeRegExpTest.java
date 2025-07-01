package org.htmlunit.corejs;

import org.junit.jupiter.api.Test;

/**
 * Tests for NativeRegExp object.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class NativeRegExpTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void empty() throws Exception {
        final String script = "new RegExp().test('AA')";
        Utils.assertWithAllModes_ES6(Boolean.TRUE, script);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void undefined() throws Exception {
        final String script = "new RegExp(undefined).test('AA')";
        Utils.assertWithAllModes_ES6(Boolean.TRUE, script);
    }

    /**
     * Test for bug #1706.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void changed() throws Exception {
        final String script = "'alpha'.replace(/alpha/, '');/beta/.test('abc beta def');";
        Utils.assertWithAllModes_ES6(Boolean.TRUE, script);
    }
}
