package net.sourceforge.htmlunit;

import org.junit.Assert;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.Scriptable;

/**
 * Tests for NativeDate.
 * @author Ahmed Ashour
 */
public class NativeDateTest {

	@Test
	public void toLocaleDateString() {
		final String source = "new Date(2000, 0, 1).toLocaleDateString()";
		final String expected = "Saturday, January 01, 2000";
		final ContextAction action = new ContextAction() {
			public Object run(final Context cx) {
                Scriptable scope = cx.initStandardObjects();
                Object result = cx.evaluateString(scope, source, "test", 1, null);
				Assert.assertEquals(expected, result);
				return null;
			}
		};
		Utils.runWithAllOptimizationLevels(action);
	}
}
