package org.htmlunit.corejs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.ContextAction;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.testutils.Utils;
import org.junit.jupiter.api.Test;

/**
 * Tests for getter/setter lookup.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class LookupSetterTest {
    private final String defineSetterAndGetterX =
        "Foo.__defineSetter__('x', function() {}); \n"
        + "Foo.__defineGetter__('x', function() {return 'hello' });\n";

    @Test
    public void typeof() throws Exception {
        test("function", "typeof Foo.__lookupSetter__('x');");
        test("function", "typeof Foo.__lookupGetter__('x');");
        test("function", "typeof (new Foo()).__lookupSetter__('s')");
        test("function", "typeof (new Foo()).__lookupGetter__('s')");
    }

    @Test
    public void callLookedUpGetter() throws Exception {
        test("hello", "new Foo().s;");
        test("hello", "var f = new Foo(); f.__lookupGetter__('s').call(f);");
    }

    @Test
    public void lookedUpGetter_toString() throws Exception {
        test("function s() {\n    [native code]\n}", "new Foo().__lookupGetter__('s').toString()");
    }

    @Test
    public void lookedUpGetter_equals() throws Exception {
        test("true", "new Foo().__lookupGetter__('s') == new Foo().__lookupGetter__('s')");
    }

    private void test(final String expected, final String src) throws Exception {
        final ContextAction<Object> action = new ContextAction<Object>() {
            @Override
            public Object run(final Context cx) {
                try {
                    final Scriptable scope = cx.initSafeStandardObjects(new TopScope());
                    ScriptableObject.defineClass(scope, Foo.class);
                    cx.evaluateString(scope, defineSetterAndGetterX, "initX", 1, null);
                    Object result = String.valueOf(cx.evaluateString(scope, src, "test", 1, null));
                    assertEquals(expected, result);
                    return null;
                } catch (final Exception e) {
                    if (e instanceof RuntimeException)
                        throw (RuntimeException) e;
                    throw new RuntimeException(e);
                }
            }
        };

        Utils.runWithAllModes(action);
    }

    public static class Foo extends ScriptableObject {
        private String s = "hello";

        public Foo() { /* Empty. */
        }

        public String jsGet_s() {
            return this.s;
        }

        public void jsSet_s(String string) {
            this.s = string;
        }

        @Override
        public String getClassName() {
            return "Foo";
        }
    }

    public static class TopScope extends ScriptableObject {
        @Override
        public String getClassName() {
            return "TopScope";
        }
    }
}
