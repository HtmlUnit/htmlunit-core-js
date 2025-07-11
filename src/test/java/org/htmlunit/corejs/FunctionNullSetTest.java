package org.htmlunit.corejs;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.reflect.Method;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.ContextAction;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.junit.jupiter.api.Test;

/**
 * Checks if setting "functionName = null" is propagated to thisObject.
 *
 * @author Ahmed Ashour
 */
public class FunctionNullSetTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setFunctionToNull() throws Exception {
        final String script = "function onclick() {onclick=null}";
        final ContextAction<Object> action = new ContextAction<Object>() {
            @Override
            public Object run(final Context cx) {
                try {
                    final Scriptable scope = cx.initSafeStandardObjects();
                    final MyHostObject prototype = new MyHostObject();
                    ScriptableObject.defineClass(scope, MyHostObject.class);
                    final Method getterMethod = MyHostObject.class.getMethod("jsxGet_onclick");
                    final Method setterMethod = MyHostObject.class.getMethod("jsxSet_onclick", Object.class);
                    prototype.defineProperty("onclick", null, getterMethod , setterMethod, ScriptableObject.EMPTY);

                    ScriptableObject.defineProperty(scope, "o", prototype, ScriptableObject.DONTENUM);

                    final MyHostObject jsObj = new MyHostObject();
                    jsObj.setPrototype(prototype);
                    jsObj.setParentScope(scope);

                    final Function realFunction_ = cx.compileFunction(jsObj, script, "myevent", 0, null);

                    realFunction_.call(cx, jsObj, jsObj, new Object[0]);

                    assertNull(jsObj.onclick_);
                }
                catch (final Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        };

        Utils.runWithAllModes(action);
    }

    public static class MyHostObject extends ScriptableObject {
        private Object onclick_ = new Object();

        @Override
        public String getClassName() {
            return getClass().getSimpleName();
        }

        public Object jsxGet_onclick() {
            return onclick_;
        }
        public void jsxSet_onclick(final Object o) {
            onclick_ = o;
        }
    }
}
