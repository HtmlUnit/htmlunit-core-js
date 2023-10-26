package org.htmlunit.corejs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.ContextAction;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;

/**
 * Tests for {@link org.mozilla.javascript.MemberBox}
 *
 * @author Ahmed Ashour
 */
public class MemberBoxTest {

    @Test
    public void descriptionOfIllegalArgumentException() {
        final String script = "var o = new MyHostObject(); o.someFunction(new Object())";

        final ContextAction<Object> action = new ContextAction<Object>() {
            @Override
            public Object run(final Context cx) {
                try {
                    Scriptable scope = cx.initSafeStandardObjects();
                    ScriptableObject.defineClass(scope, MyHostObject.class);

                    cx.evaluateString(scope, script, "test_script", 1, null);
                    fail("An exception should be thrown");
                }
                catch (final Exception e) {
                    assertEquals("Exception invoking MyHostObject.jsFunction_someFunction() "
                            + "with arguments [NativeObject]",
                            e.getMessage());
                }
                return null;
            }
        };

        Utils.runWithAllOptimizationLevels(action);
    }

    public static class MyHostObject extends ScriptableObject {
        @Override
        public String getClassName() {
            return getClass().getSimpleName();
        }

        public void jsFunction_someFunction(Function a) {
        }

    }
}
