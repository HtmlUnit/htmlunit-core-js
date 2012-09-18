package net.sourceforge.htmlunit;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * Test setting ReadOnly property behavior.
 *
 * The same web browser (e.g. FF) may allow setting read-only property, 
 * ignores setting the read-only property, or even throw an exception.
 *
 * So, it is the ScriptableObject itself to decide which action according to the property name
 *
 * @author Ahmed Ashour
 */
public class SetReadOnlyPropertyTest extends TestCase {

    @Test
	public void testOnlyGetterError() {
        testOnlyGetterError(Boolean.TRUE);
        testOnlyGetterError(null);
        testOnlyGetterError(Boolean.FALSE);
    }

    private void testOnlyGetterError(final Boolean isSetterAllowed) {
        MyHostObject.isReadOnlySettable = isSetterAllowed;
		final ContextFactory cf = new ContextFactory() {
			@Override
			protected boolean hasFeature(Context cx, int featureIndex) {
			    if (Context.FEATURE_HTMLUNIT_ASK_OBJECT_TO_WRITE_READONLY == featureIndex) {
			        return true;
			    }
				return super.hasFeature(cx, featureIndex);
			}
		};
		
		final String script = "o.readonlyProp = 123;o.readonlyProp";
		
		final ContextAction action = new ContextAction() {
			public Object run(final Context cx) {
				try {
					Scriptable scope = cx.initStandardObjects();
					final MyHostObject prototype = new MyHostObject();
					ScriptableObject.defineClass(scope, MyHostObject.class);
					final Method readMethod = MyHostObject.class.getMethod("jsxGet_x");
					prototype.defineProperty("readonlyProp", null, readMethod , null, ScriptableObject.EMPTY);

                    ScriptableObject.defineProperty(scope, "o", prototype, ScriptableObject.DONTENUM);

					Number number = (Number) cx.evaluateString(scope, script, "test_script", 1, null);
					if (isSetterAllowed == null) {
					    throw new RuntimeException("Should have failed!");
					}

                    if (isSetterAllowed.equals(Boolean.TRUE)) {
                        assertEquals(123, number.intValue());
                    }
                    else {
                        assertEquals(0, number.intValue());
                    }
					return null;
				}
				catch (final EcmaError e) {
				    if (MyHostObject.isReadOnlySettable == null) {
                        assertEquals("TypeError: Cannot set property [MyHostObject].readonlyProp that has only a getter to 123. (test_script#1)", e.getMessage());
                        return null;
				    }
                    throw new RuntimeException("Should not throw EcmaError!");
				}
				catch (final Exception e) {
					throw new RuntimeException(e);
				}
			}
		};

		Utils.runWithAllOptimizationLevels(cf, action);
	}

	public static class MyHostObject extends ScriptableObject {
	    static Boolean isReadOnlySettable = Boolean.TRUE;
		private int x;

		@Override
		public String getClassName() {
			return getClass().getSimpleName();
		}

		public int jsxGet_x() {
			return x;
		}

        @Override
        protected boolean isReadOnlySettable(String name, Object value) {
            if (isReadOnlySettable == null) {
                throw ScriptRuntime.typeError3("msg.set.prop.no.setter", name,
                        this.getClassName(), Context.toString(value));
            }
            if (isReadOnlySettable.equals(Boolean.TRUE)) {
                return true;
            }
            return false;
        }
	}
}
