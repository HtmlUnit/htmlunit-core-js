package net.sourceforge.htmlunit;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import org.junit.Test;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

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
        doTest("foo.willThrowJSException");
    }

    /**
     * If configured, JS catch block should not catch Java exception
     */
    @Test
    public void testDontCatchJavaException_inGetter() throws Exception {
        try {
            doTest("foo.willThrowJavaException");
        }
        catch(final Exception e)
        {
            assertSame(MyScriptable.javaException, e.getCause());
        }
    }

    /**
     * If configured, JS catch block should not catch Java exception
     */
    @Test
    public void testDontCatchJavaException_inFunction() throws Exception {
        try {
            doTest("foo.throwJavaException()");
            fail("Should have throw!");
        }
        catch(final Exception e)
        {
            assertSame(MyScriptable.javaException, e.getCause());
        }
    }

    private static void doTest(final String jsExpression) throws Exception {
        final String script = "var foo = new MyScriptable(); try { " + jsExpression + "} catch(e) {}";

        final ContextAction<Object> action = new ContextAction<Object>() {
            @Override
            public Object run(final Context cx) {
                final Scriptable scope = cx.initSafeStandardObjects();
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
        Utils.runWithAllOptimizationLevels(action);
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
