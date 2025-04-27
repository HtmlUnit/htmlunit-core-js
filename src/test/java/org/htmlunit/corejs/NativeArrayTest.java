package org.htmlunit.corejs;

import org.htmlunit.corejs.javascript.testutils.Utils;
import org.junit.jupiter.api.Test;

/**
 * Tests for NativeArray object.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class NativeArrayTest {

    static {
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
    }

    /**
     * Test for "Comparison method violates its general contract!".
     * @throws Exception if the test fails
     */
    @Test
    public void java7Sort() {
        final String script =
            "var results = [1, 1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 1, 1, 1, 1, 1, 1, 1, -1, -1, 1, -1, -1, 1,"
            + " -1, -1, 1, -1, 1, -1, 1, 1, 1, 1, -1, -1, 1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, 1, -1,"
            + " 1, -1, -1, 1, 1, -1, -1, 1, 1, 1, 1, 1, 1, -1, -1, 0, -1, -1, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0,"
            + " 1, 1, 1, 1, 1, 1, 1, 1, 1, -1, -1, -1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1, 1, 1, 1, 1,"
            + " 1, 1, 1, -1, -1, -1, -1, -1, -1, -1, -1, 1, 1, -1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1];\n"

            + "var index = 0;"
            + "var arr = new Array(37);\n"
            + "for (var x = 0; x < arr.length; x++) {\n"
            + "  arr[x] = new Object();\n"
            + "}\n"
            + "arr.sort(function (a, b) {\n"
            + "  return results[index++];\n"
            + "});\n"
            + "'sorted'";

        Utils.assertWithAllModes("sorted", script);
    }
}
