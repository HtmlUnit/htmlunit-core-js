package net.sourceforge.htmlunit;

import org.junit.Assert;
import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * Unit tests for {@link org.mozilla.javascript.ScriptRuntime}
 * 
 * @author Ahmed Ashour
 */
public class ScriptRuntimeTest {

	@Test
    public void functionDeclaredForwardInBlock() {
	    final String script =
	              "function test () {\n"
	            + "  if (true) {\n"
	            + "    try {\n"
                + "      output += '' + foo;\n"
                + "    } catch (e) {\n"
                + "      output += 'exception';\n"
                + "    }\n"
                + "    function foo() {}\n"
	            + "  }\n"
	            + "};\n"
	            + "var output = '';\n"
	            + "test();\n"
	            + "output";
	    //Not Yet Implemented
        //functionDeclaredForwardInBlock(script, "exception", false);
        //functionDeclaredForwardInBlock(script, "\nfunction foo() {\n}\n", true);
    }

	private void functionDeclaredForwardInBlock(final String script, final Object expected,
	        final boolean functionDeclaredForwardInBlock) {
        final ContextFactory cf = new ContextFactory() {
//            @Override
//            protected boolean hasFeature(Context cx, int featureIndex) {
//                if (Context.FEATURE_HTMLUNIT_FUNCTION_DECLARED_FORWARD_IN_BLOCK == featureIndex) {
//                    return functionDeclaredForwardInBlock;
//                }
//                return super.hasFeature(cx, featureIndex);
//            }
        };

        final ContextAction action = new ContextAction() {
			public Object run(final Context cx) {
				try {
					Scriptable scope = cx.initStandardObjects();
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
