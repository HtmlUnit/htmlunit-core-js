package net.sourceforge.htmlunit;

import junit.framework.Assert;

import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.Scriptable;

/**
 * Test for a basic implementation of caller property on functions.
 * Implementation is not yet good enough to propose a patch to Rhino.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class FunctionCaller {

	/**
	 * JS catch block should always catch JS exceptions
	 */
	@Test
	public void callee() throws Exception {
		final String script = "function f() {\n"
			+ "  var c = arguments.callee.caller;\n"
			+ "  if (c) throw 'got: ' + c;\n"
			+ "  if (g.caller) throw 'got: ' + c;\n"
			+ "  g();\n"
			+ "  if (g.caller) throw 'after g() got: ' + g.caller;\n"
			+ "}\n"
			+ "function g() {\n"
			+ "  var c = arguments.callee.caller;\n"
			+ "  if (c != f) throw 'got: ' + c;\n"
			+ "}\n"
			+ "f();\n";
		
		final ContextAction action = new ContextAction() {
			public Object run(final Context cx) {
				final Scriptable scope = cx.initStandardObjects();
				
				cx.evaluateString(scope, script, "test.js", 1, null);

				return null;
			}
		};
		Utils.runWithOptimizationLevel(action, -1);
	}

    /**
     * Tests the caller arguments
     */
    @Test
    public void callerArguments() throws Exception {
        final String script =
              "function f() {\n"
            + "  g('hello', 'world');\n"
            + "}\n"
            + "function g(hi, now) {\n"
            + "  h('hi', 'there', 'yet')\n"
            + "}\n"
            + "function h(hi, hehe, there) {\n"
            + "  output += g.arguments.length + '-';\n"
            + "  output += arguments.callee.caller.arguments.length;\n"
            + "}\n"
            + "var output = '';\n"
            + "f();\n"
            + "output";
        
        final ContextAction action = new ContextAction() {
            public Object run(final Context cx) {
                final Scriptable scope = cx.initStandardObjects();
                
                final Object result = cx.evaluateString(scope, script, "test.js", 1, null);
                Assert.assertEquals("2-2", result);
                return null;
            }
        };
        Utils.runWithOptimizationLevel(action, -1);
    }

    /**
     * Tests the caller arguments
     */
    @Test
    public void arguments() throws Exception {
        final String script =
              "function f() {\n"
            + "  g('hello', 'world');\n"
            + "}\n"
            + "function g() {\n"
            + "  output += g.arguments.length + '-';\n"
            + "  output += g.arguments[0] + '-';\n"
            + "  h('i', 'you');\n"
            + "}\n"
            + "function h() {\n"
            + "  output += g.arguments.length;\n"
            + "}\n"
            + "var output = '';\n"
            + "f();\n"
            + "output";

        final ContextAction action = new ContextAction() {
            public Object run(final Context cx) {
                final Scriptable scope = cx.initStandardObjects();
                
                final Object result = cx.evaluateString(scope, script, "test.js", 1, null);
                Assert.assertEquals("2-hello-2", result);
                return null;
            }
        };
        Utils.runWithAllOptimizationLevels(action);
    }
}
