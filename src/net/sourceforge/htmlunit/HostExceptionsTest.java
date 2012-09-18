package net.sourceforge.htmlunit;

import org.junit.Assert;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * It can be useful to avoid that JavaScript catch block can catch internal exceptions in host objects.
 * Indeed it hides these exception and makes more difficult to find the root problems.
 * @see <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=519935">Rhino bug 519935</a>
 * @author Marc Guillemot
 */
public class HostExceptionsTest {

	/**
	 * JS catch block should always catch JS exceptions
	 */
	@Test
	public void testCatchJSException() throws Exception {
		doTest("foo.willThrowJSException", true);
		doTest("foo.willThrowJSException", false);
	}

	/**
	 * Per default JS catch block catch Java exception 
	 */
	@Test
	public void testCatchJavaException() throws Exception {
		doTest("foo.willThrowJavaException", true);
		doTest("foo.throwJavaException()", true);
	}
	
	/**
	 * If configured, JS catch block should not catch Java exception 
	 */
	@Test
	public void testDontCatchJavaException_inGetter() throws Exception {
		try {
			doTest("foo.willThrowJavaException", false);
			Assert.fail("Should have throw!");
		}
		catch(final Exception e)
		{
			Assert.assertSame(MyScriptable.javaException, e.getCause());
		}
	}

	/**
	 * If configured, JS catch block should not catch Java exception 
	 */
	@Test
	public void testDontCatchJavaException_inFunction() throws Exception {
		try {
			doTest("foo.throwJavaException()", false);
			Assert.fail("Should have throw!");
		}
		catch(final Exception e)
		{
			Assert.assertSame(MyScriptable.javaException, e.getCause());
		}
	}

	private void doTest(final String jsExpression, final boolean contextFeatureJSCatchOn) throws Exception {
		final String script = "var foo = new MyScriptable(); try { " + jsExpression + "} catch(e) {}";

		final ContextFactory myContextFactory = new ContextFactory() {
			protected boolean hasFeature(final Context cx, final int featureIndex) {
				if (Context.FEATURE_HTMLUNIT_JS_CATCH_JAVA_EXCEPTION == featureIndex) {
					return contextFeatureJSCatchOn;
				}
				return super.hasFeature(cx, featureIndex);
			};
		};
		
		final ContextAction action = new ContextAction() {
			public Object run(final Context cx) {
				final Scriptable scope = cx.initStandardObjects();
				try {
					ScriptableObject.defineClass(scope, MyScriptable.class);
				}
				catch (final Exception e) {
					throw new RuntimeException();
				}
				
				cx.evaluateString(scope, script, "test.js", 0, null);

				return null;
			}
		};
		Utils.runWithAllOptimizationLevels(myContextFactory, action);
	}

	public static class MyScriptable extends ScriptableObject {
		static RuntimeException javaException = new NullPointerException();
		@Override
		public String getClassName() {
			return "MyScriptable";
		}
		
		public String jsGet_willThrowJavaException() {
			throw javaException;
		}

		public String jsGet_willThrowJSException() {
			throw Context.reportRuntimeError("this is a JS exception");
		}
		
		public void jsFunction_throwJavaException() {
			throw javaException;
		}
	}
}
