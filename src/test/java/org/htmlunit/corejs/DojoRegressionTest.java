package org.htmlunit.corejs;

import org.htmlunit.corejs.javascript.testutils.Utils;
import org.junit.jupiter.api.Test;

/**
 * Regressions tests found with Dojo.
 * @author Ronald Brill
 */
public class DojoRegressionTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void test() throws Exception {
        final String script =
                "var NodeList = function(array) {\n"
                + "  this.length = array.length;\n"
                + "  return this;\n"
                + "}\n"

                + "var nlp = NodeList.prototype = [];\n"

                + "var test = new NodeList(['abc']);\n"
                + "'' + nlp.length + ' # ' + test.length";

        Utils.assertWithAllModes_ES6("0 # 1", script);
    }
}
