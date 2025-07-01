package org.htmlunit.corejs;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=477604">Bug 477604 -
 * Array.concat causes ArrayIndexOutOfBoundException with non dense array</a>
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class ArrayConcatTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void concat() throws Exception {
        final String script = "var a = [1, 2, 3];\n"
            + "for (var i=10; i<20; ++i) a[i] = 't' + i;\n"
            + "var b = [1, 2, 3];\n"
            + "'' + (b.concat(a));\n";

        Utils.assertWithAllModes_ES6("1,2,3,1,2,3,,,,,,,,t10,t11,t12,t13,t14,t15,t16,t17,t18,t19", script);
    }
}
