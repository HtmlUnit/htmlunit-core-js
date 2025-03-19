package org.htmlunit.corejs.javascript.regexp;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.regexp.NativeRegExp;
import org.htmlunit.corejs.javascript.regexp.RECompiled;
import org.htmlunit.corejs.javascript.regexp.REGlobalData;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * Tests to use the RegExp engine without js. This is required
 * for implementing input validation.
 *
 * @author Ronald Brill
 */
public class RegExpImplStandaloneTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testChangeAttributesFromSubclass() throws Exception {
        Context cx = new Context() {};

        String regExp = "^(?:HtmlU[i-t]*)$";
        String regExpArgs = ""; // v


        RegExpEngineAccess.CompiledRegExp compiled = RegExpEngineAccess.compile(cx, regExp, regExpArgs);

        String experiment = "HtmlUniiiiiot";
        final boolean matches = RegExpEngineAccess.matches(cx, experiment, compiled);
        Assert.assertTrue(matches);
    }
}
