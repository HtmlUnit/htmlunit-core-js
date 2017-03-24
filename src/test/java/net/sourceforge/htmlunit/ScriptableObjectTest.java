package net.sourceforge.htmlunit;

import org.junit.Assert;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * Unit tests for {@link ScriptableObject}
 * 
 * @author Ahmed Ashour
 */
public class ScriptableObjectTest {

	public static class MyHostObject extends ScriptableObject {
		@Override
		public String getClassName() {
			return getClass().getSimpleName();
		}
	}

    @Test
    public void constructor() {
        test(true, "var o = new MyHostObject(); typeof o.constructor", "function");
        test(false, "var o = new MyHostObject(); typeof o.constructor", "undefined");
    }

    @Test
    public void objectConstructor() {
        test(true, "typeof new Object().constructor", "function");
        test(false, "typeof new Object().constructor", "function");
    }

    @Test
    public void arrayConstructor() {
        test(true, "typeof [].constructor", "function");
        test(false, "typeof [].constructor", "function");
    }

	private static void test(final boolean isConstructor, final String script, final Object expected) {
        final ContextFactory cf = new ContextFactory() {
            @Override
            protected boolean hasFeature(Context cx, int featureIndex) {
                if (Context.FEATURE_HTMLUNIT_CONSTRUCTOR == featureIndex) {
                    return isConstructor;
                }
                return super.hasFeature(cx, featureIndex);
            }
        };
		final ContextAction action = new ContextAction() {
            @Override
			public Object run(final Context cx) {
				try {
					Scriptable scope = cx.initStandardObjects();
					ScriptableObject.defineClass(scope, MyHostObject.class);
					final Object o = cx.evaluateString(scope, script, "test_script", 1, null);
					Assert.assertEquals(expected, o);
					return o;
				} catch (final Exception e) {
					throw new RuntimeException(e);
				}
			}
		};

		Utils.runWithAllOptimizationLevels(cf, action);
	}
}
