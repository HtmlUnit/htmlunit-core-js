package net.sourceforge.htmlunit;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.Locale;

import org.junit.BeforeClass;
import org.junit.Test;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.EcmaError;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

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
public class SetReadOnlyPropertyTest {
	@BeforeClass
	public static void initLocale() {
		Locale.setDefault(Locale.US); // to be sure to have English error messages
	}

    @Test
	public void onlyGetterError() {
		final String script = "o.readonlyProp = 123;o.readonlyProp";
		
		final ContextAction action = new ContextAction() {
            @Override
			public Object run(final Context cx) {
				try {
					Scriptable scope = cx.initStandardObjects();
					final MyHostObject prototype = new MyHostObject();
					ScriptableObject.defineClass(scope, MyHostObject.class);
					final Method readMethod = MyHostObject.class.getMethod("jsxGet_x");
					prototype.defineProperty("readonlyProp", null, readMethod , null, ScriptableObject.EMPTY);

                    ScriptableObject.defineProperty(scope, "o", prototype, ScriptableObject.DONTENUM);

					Number number = (Number) cx.evaluateString(scope, script, "test_script", 1, null);

					assertEquals(123, number.intValue());
					return null;
				}
				catch (final EcmaError e) {
                    throw new RuntimeException("Should not throw EcmaError!");
				}
				catch (final Exception e) {
					throw new RuntimeException(e);
				}
			}
		};

		Utils.runWithAllOptimizationLevels(action);
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

        @Override
        protected boolean isReadOnlySettable(String name, Object value) {
            return true;
        }
	}
}
