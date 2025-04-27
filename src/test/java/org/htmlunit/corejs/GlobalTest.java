package org.htmlunit.corejs;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.ContextFactory;
import org.htmlunit.corejs.javascript.tools.shell.Global;
import org.junit.jupiter.api.Test;

/**
 * @author Ronald Brill
 */
public class GlobalTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void test() throws Exception {
        try (Context ctx = ContextFactory.getGlobal().enterContext()) {
            new Global(ctx);
        }
    }
}
