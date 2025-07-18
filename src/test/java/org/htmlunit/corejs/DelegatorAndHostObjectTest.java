package org.htmlunit.corejs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.ContextAction;
import org.htmlunit.corejs.javascript.Delegator;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for <a
 * href="https://bugzilla.mozilla.org/show_bug.cgi?id=539875">Bug 539875 -
 * Delegator incompatible with host objects with functions</a>.
 *
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class DelegatorAndHostObjectTest {

    public static class MyHostObject extends ScriptableObject {
        private int x;

        @Override
        public String getClassName() {
            return getClass().getSimpleName();
        }

        public Delegator jsFunction_createDelegator() {
            return new Delegator(this);
        }

        public int jsFunction_methodWithParam(final MyHostObject param) {
            return 123;
        }

        public int jsFunction_foo() {
            return 42;
        }

        public int jsGet_x() {
            return x;
        }

        public void jsSet_x(int s) {
            this.x = s;
        }
    }

    @Test
    public void delegatorAndHostObjectFunction() {
        test("new MyHostObject().createDelegator().foo()", 42);
    }

    @Test
    public void delegatorAndHostObjectGetterSetter() {
        test("var t = new MyHostObject().createDelegator(); t.x = 12; t.x;", 12);
    }

    @Test
    public void delegatorAsParameter() {
        test("var t = new MyHostObject().createDelegator(); t.methodWithParam(t);", 123);
    }

    @Test
    public void strictEquals() {
        test("var o = new MyHostObject(); o.createDelegator() === o", true);
    }

    @Test
    public void strictEqualsDelegators() {
        test("var o = new MyHostObject(); o.createDelegator() === o.createDelegator()", true);
    }

    private static void test(final String script, final Object expected) {
        final ContextAction<Object> action = new ContextAction<Object>() {
            @Override
            public Object run(final Context cx) {
                try {
                    Scriptable scope = cx.initSafeStandardObjects();
                    ScriptableObject.defineClass(scope, MyHostObject.class);
                    final Object o = cx.evaluateString(scope, script, "test_script", 1, null);
                    assertEquals(expected, o);
                    return o;
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Utils.runWithAllModes(action);
    }
}
