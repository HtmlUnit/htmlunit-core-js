package net.sourceforge.htmlunit;

import org.junit.Assert;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.ContextFactory;
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
        test(script, "hello", true);
        test(script, "hello", false);
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
        test(script, "hello", true);
        try {
            test(script, "hello", false);
            Assert.fail("Should have failed");
        }
        catch(final RuntimeException e) {
            if (!(e.getCause() instanceof EvaluatorException)) {
               throw e; 
            }
        }
        
    }

	private void test(final String script, final Object expected, final boolean functionObjectMethod) {
        final ContextFactory cf = new ContextFactory() {
            @Override
            protected boolean hasFeature(Context cx, int featureIndex) {
                if (Context.FEATURE_HTMLUNIT_FUNCTION_OBJECT_METHOD == featureIndex) {
                    return functionObjectMethod;
                }
                return super.hasFeature(cx, featureIndex);
            }
        };

        final ContextAction action = new ContextAction() {
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
