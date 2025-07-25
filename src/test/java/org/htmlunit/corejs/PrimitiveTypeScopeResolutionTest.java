package org.htmlunit.corejs;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.ContextAction;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=374918">Bug 374918 -
 * String primitive prototype wrongly resolved when used with many top scopes</a>
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class PrimitiveTypeScopeResolutionTest {

    @Test
    public void functionCall() {
        String str2 = "function f() { String.prototype.foo = function() { return 'from 2' }; \n"
            + "var s2 = 's2';\n"
            + "var s2Foo = s2.foo();\n"
            + "if (s2Foo != 'from 2') throw 's2 got: ' + s2Foo;\n" // fails
            + "}";

        String str1 = "String.prototype.foo = function() { return 'from 1'}; scope2.f()";
        testWithTwoScopes(str1, str2);
    }

    @Test
    public void propertyAccess() {
        String str2 = "function f() { String.prototype.foo = 'from 2'; \n"
            + "var s2 = 's2';\n"
            + "var s2Foo = s2.foo;\n"
            + "if (s2Foo != 'from 2') throw 's2 got: ' + s2Foo;\n" // fails
            + "}";

        String str1 = "String.prototype.foo = 'from 1'; scope2.f()";
        testWithTwoScopes(str1, str2);
    }

    private static void testWithTwoScopes(final String scriptScope1, final String scriptScope2) {
        final ContextAction<Object> action = new ContextAction<Object>() {
            @Override
            public Object run(final Context cx){
                final Scriptable scope1 = cx.initSafeStandardObjects(new MySimpleScriptableObject("scope1"));
                final Scriptable scope2 = cx.initSafeStandardObjects(new MySimpleScriptableObject("scope2"));
                cx.evaluateString(scope2, scriptScope2, "source2", 1, null);

                scope1.put("scope2", scope1, scope2);

                return cx.evaluateString(scope1, scriptScope1, "source1", 1, null);
            }
        };
        Utils.runWithAllModes(action);
    }

    /**
     * Simple utility allowing to better see the concerned scope while debugging
     */
    static class MySimpleScriptableObject extends ScriptableObject {
        private String label_;
        MySimpleScriptableObject(String label) {
            label_ = label;
        }
        @Override
        public String getClassName() {
            return "MySimpleScriptableObject";
        }

        @Override
        public String toString() {
            return label_;
        }
    }
}
