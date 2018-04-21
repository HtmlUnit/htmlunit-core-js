package net.sourceforge.htmlunit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

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
        test("var o = new MyHostObject(); typeof o.constructor");
    }

    @Test
    public void objectConstructor() {
        test("typeof new Object().constructor");
    }

    @Test
    public void arrayConstructor() {
        test("typeof [].constructor");
    }

    private static void test(final String script) {
        final ContextAction action = new ContextAction() {
            @Override
            public Object run(final Context cx) {
                try {
                    Scriptable scope = cx.initStandardObjects();
                    ScriptableObject.defineClass(scope, MyHostObject.class);
                    final Object o = cx.evaluateString(scope, script, "test_script", 1, null);
                    assertEquals("function", o);
                    return o;
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Utils.runWithAllOptimizationLevels(action);
    }
}
