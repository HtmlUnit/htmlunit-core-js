package net.sourceforge.htmlunit;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import org.junit.Test;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 * Test scope of 'eval'.
 *
 * @author Ahmed Ashour
 */
public class EvalScopeTest {

    @Test
    public void eval() {
        eval(createFunction("output=host1.eval('f')"), "exception");
        eval(createFunction("output=eval('f')"), "3");
        eval(createFunction("arr=['f'];output=eval(arr.join(''))"), "3");
    }

    private static String createFunction(final String outputSetter) {
        return "function test(){\n"
                + "  test1(3)\n"
                + "};\n"
                + "function test1(f){\n"
                + "  try {\n"
                + "    " + outputSetter + "\n"
                + "  } catch (e) {\n"
                + "    output='exception';\n"
                + "  }\n"
                + "};\n"
                + "var output;\n"
                + "test();\n"
                + "output";
    }

    private static void eval(final String script, final String expected) {
        final ContextFactory cf = new ContextFactory() {
            @Override
            protected boolean hasFeature(Context cx, int featureIndex) {
                if (Context.FEATURE_DYNAMIC_SCOPE == featureIndex) {
                    return true;
                }
                return super.hasFeature(cx, featureIndex);
            }
        };
        final ContextAction action = new ContextAction() {
            @Override
            public Object run(final Context cx) {
                try {
                    final MyHostObject prototype = new MyHostObject();
                    final Method readMethod = MyHostObject.class.getMethod("jsxGet_id");
                    prototype.defineProperty("id", null, readMethod , null, ScriptableObject.EMPTY);

                    final MyHostObject host1 = new MyHostObject(1);
                    host1.setPrototype(prototype);
                    cx.initStandardObjects(host1);

                    final MyHostObject host2 = new MyHostObject(2);
                    host2.setPrototype(prototype);
                    cx.initStandardObjects(host2);

                    ScriptableObject.defineProperty(host1, "host1", host1, ScriptableObject.EMPTY);
                    ScriptableObject.defineProperty(host1, "host2", host2, ScriptableObject.EMPTY);

                    Object result = cx.evaluateString(host1, script, "test_script", 1, null);
                    if (result instanceof Number) {
                        result = ((Number) result).intValue();
                    }
                    assertEquals(expected, result.toString());
                    return null;
                }
                catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Utils.runWithAllOptimizationLevels(cf, action);
    }

    public static class MyHostObject extends ScriptableObject {
        private int id_;

        private MyHostObject() {
        }

        private MyHostObject(int id) {
            id_ = id;
        }

        @Override
        public String getClassName() {
            return getClass().getSimpleName();
        }

        public int jsxGet_id() {
            return id_;
        }
    }
}
