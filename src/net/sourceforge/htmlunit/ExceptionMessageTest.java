package net.sourceforge.htmlunit;

import java.lang.reflect.Method;
import java.util.Locale;

import junit.framework.TestCase;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * Test that exception messages contain rich information.
 * Unit tests for <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=604674">bug 604674</a>.
 * @author Marc Guillemot
 */
public class ExceptionMessageTest extends TestCase {
    
    @Override
    protected void setUp() throws Exception {
    	Locale.setDefault(Locale.ENGLISH); // to be sure that error messages are in English
    }

    /**
     * Unit test for bug 608235
     * https://bugzilla.mozilla.org/show_bug.cgi?id=608235
     */
	public void testUndefinedFromUndefined() {
		testExceptionMessage("undefined[undefined]", "TypeError: Cannot read property \"undefined\" from undefined");
		testExceptionMessage("undefined[undefined] = 1", "TypeError: Cannot set property \"undefined\" of undefined to \"1\"");
		testExceptionMessage("undefined.undefined()", "TypeError: Cannot call method \"undefined\" of undefined");
	}

	private void testExceptionMessage(final String script, final String expectedMesage) {
		final ContextAction action = new ContextAction() {
			public Object run(final Context cx) {
				try {
					Scriptable scope = cx.initStandardObjects();

					cx.evaluateString(scope, script, "test_script", 1, null);
					throw new RuntimeException("Should have failed!");
				}
				catch (final EcmaError e) {
					assertEquals(expectedMesage + " (test_script#1)", e.getMessage());
					return null;
				}
				catch (final Exception e) {
					throw new RuntimeException(e);
				}
			}
		};

		Utils.runWithAllOptimizationLevels(action);
	}

	/**
     * Unit test for bug 604674
     * https://bugzilla.mozilla.org/show_bug.cgi?id=604674
     */
	public void testOnlyGetterError() {
		testOnlyGetterError(Context.FEATURE_STRICT_MODE);
		testOnlyGetterError(Context.FEATURE_HTMLUNIT_ASK_OBJECT_TO_WRITE_READONLY);
	}

	private void testOnlyGetterError(final int feature) {
		final ContextFactory cf = new ContextFactory() {
			@Override
			protected boolean hasFeature(Context cx, int featureIndex) {
            	if (Context.FEATURE_STRICT_MODE == featureIndex) {
            		return true;
            	}
				return super.hasFeature(cx, featureIndex);
			}
		};
		
		final String script = "o.readonlyProp = 123";
		
		final ContextAction action = new ContextAction() {
			public Object run(final Context cx) {
				try {
					Scriptable scope = cx.initStandardObjects();
					final MyHostObject prototype = new MyHostObject();
					ScriptableObject.defineClass(scope, MyHostObject.class);
					final Method readMethod = MyHostObject.class.getMethod("jsxGet_x");
					prototype.defineProperty("readonlyProp", null, readMethod , null, ScriptableObject.EMPTY);

                    ScriptableObject.defineProperty(scope, "o", prototype, ScriptableObject.DONTENUM);

					cx.evaluateString(scope, script, "test_script", 1, null);
					throw new RuntimeException("Should have failed!");
				}
				catch (final EcmaError e) {
					assertEquals("TypeError: Cannot set property [MyHostObject].readonlyProp that has only a getter to 123. (test_script#1)", e.getMessage());
					return null;
				}
				catch (final Exception e) {
					throw new RuntimeException(e);
				}
			}
		};

		Utils.runWithAllOptimizationLevels(cf, action);
	}

	public static class MyHostObject extends ScriptableObject {
		private int x;

		@Override
		public String getClassName() {
			return getClass().getSimpleName();
		}

		public int jsxGet_x() {
			return x;
		}
	}
}
