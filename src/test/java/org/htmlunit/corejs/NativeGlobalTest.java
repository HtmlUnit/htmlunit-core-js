package org.htmlunit.corejs;

import org.junit.jupiter.api.Test;

/**
 * Tests for NativeGlobal object.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class NativeGlobalTest {

    /**
     * Test for parseInt.
     */
    @Test
    public void parseInt() throws Exception {
        Utils.assertWithAllModes_ES6(8, "parseInt('08')");

        Utils.assertWithAllModes_ES6(0, "parseInt('08', 8)");

        Utils.assertWithAllModes_ES6(Double.NaN, "parseInt('8', 8)");

        Utils.assertWithAllModes_ES6(8, "parseInt('8')");
    }
}
