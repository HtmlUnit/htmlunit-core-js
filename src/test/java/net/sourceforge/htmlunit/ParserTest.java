package net.sourceforge.htmlunit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * Unit tests for {@link org.mozilla.javascript.Parser}
 * 
 * @author Ahmed Ashour
 */
public class ParserTest {

	public static class MyHostObject extends ScriptableObject {
		@Override
		public String getClassName() {
			return getClass().getSimpleName();
		}
	}

	@Test
    public void normalFunctionDefinition() {
	    final String script = "var o = new MyHostObject();\n"
	            + "o.click = function() {\n"
	            + "  output = 'hello';\n"
	            + "};\n"
	            + "var output;\n"
	            + "o.click();\n"
	            + "output";
        test(script, "hello");
    }

	/**
	 * Test <code>function object.property() {}</code> instead of <code>object.property = function() {}</code>.
	 */
    @Test
    public void functionObjectMethod() {
        final String script = "var o = new MyHostObject();\n"
            + "function o.click() {\n"
            + "  output = 'hello';\n"
            + "};\n"
            + "var output;\n"
            + "o.click();\n"
            + "output";
        try {
            test(script, "hello");
            fail("Should have failed");
        }
        catch(final RuntimeException e) {
            if (!(e.getCause() instanceof EvaluatorException)) {
               throw e; 
            }
        }
    }

	private static void test(final String script, final Object expected) {
        final ContextAction action = new ContextAction() {
            @Override
			public Object run(final Context cx) {
				try {
					Scriptable scope = cx.initStandardObjects();
					ScriptableObject.defineClass(scope, MyHostObject.class);
					final Object o = cx.evaluateString(scope, script, "test_script", 1, null);
					assertEquals(expected, o);
					return o;
				} catch (final Exception e) {
					throw new RuntimeException(e);
				}
			}
		};

		Utils.runWithAllOptimizationLevels(action);
	}
}
