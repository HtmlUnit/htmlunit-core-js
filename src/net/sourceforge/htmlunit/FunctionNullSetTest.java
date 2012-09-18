package net.sourceforge.htmlunit;

import java.lang.reflect.Method;

import junit.framework.Assert;

import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

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
        setFunctionToNull(true, true);
        setFunctionToNull(false, false);
    }

    /**
     * @param expectedNull is the object setter is called (which sets the object value to null)
     */
    private void setFunctionToNull(final boolean hasFeature, final boolean expectedNull) throws Exception {

        final ContextFactory cf = new ContextFactory() {
            @Override
            protected boolean hasFeature(Context cx, int featureIndex) {
                if (Context.FEATURE_HTMLUNIT_FUNCTION_NULL_SETTER == featureIndex) {
                    return hasFeature;
                }
                return super.hasFeature(cx, featureIndex);
            }
        };
        final String script = "function onclick() {onclick=null}";
        final ContextAction action = new ContextAction() {
            public Object run(final Context cx) {
                try {
                    final Scriptable scope = cx.initStandardObjects();
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

                    Assert.assertEquals(expectedNull, jsObj.onclick_ == null);
                }
                catch (final Exception e) {
                    throw new RuntimeException(e);
                }
                return null;
            }
        };

        Utils.runWithOptimizationLevel(cf, action, -1);
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
