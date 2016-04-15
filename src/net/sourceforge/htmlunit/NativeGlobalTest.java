package net.sourceforge.htmlunit;

import org.junit.Assert;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;

/**
 * Tests for NativeGlobal object.
 *
 * @author Ahmed Ashour
 */
public class NativeGlobalTest {

	/**
	 * Test for parseInt.
	 */
	@Test
	public void parseInt() throws Exception {
	    doTest(0, "parseInt('08')", false);
        doTest(8, "parseInt('08')", true);

        doTest(0, "parseInt('08', 8)", false);
        doTest(0, "parseInt('08', 8)", true);

        doTest(Double.NaN, "parseInt('8', 8)", false);
        doTest(Double.NaN, "parseInt('8', 8)", true);

        doTest(8, "parseInt('8')", false);
        doTest(8, "parseInt('8')", true);
	}

    private void doTest(final double expected, final String src, final boolean contextFeature) throws Exception {
		final ContextFactory myContextFactory = new ContextFactory() {
            @Override
			protected boolean hasFeature(final Context cx, final int featureIndex) {
				if (Context.FEATURE_HTMLUNIT_PARSE_INT_RADIX_10 == featureIndex) {
					return contextFeature;
				}
				return super.hasFeature(cx, featureIndex);
			};
		};
		
		final ContextAction action = new ContextAction() {
            @Override
			public Object run(final Context cx) {
				final Scriptable scope = cx.initStandardObjects();
				Object result = cx.evaluateString(scope, src, "test.js", 0, null);
                Assert.assertEquals(expected, result);
				return null;
			}
		};
		Utils.runWithAllOptimizationLevels(myContextFactory, action);
	}

}
