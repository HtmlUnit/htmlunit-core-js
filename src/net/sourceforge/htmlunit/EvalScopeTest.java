package net.sourceforge.htmlunit;

import java.lang.reflect.Method;

import junit.framework.Assert;

import org.junit.Test;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.ScriptableObject;

/**
 * Test scope of 'eval'.
 *
 * @author Ahmed Ashour
 */
public class EvalScopeTest {

    @Test
	public void eval() {
        eval("host2.eval('id')", true, "2");
        eval("host2['eval']('id')", true, "2");
        eval(createFunction("output=host1.eval('f')"), true, "3");
        eval(createFunction("output=host1['eval']('f')"), true, "3");
        eval(createFunction("output=host1.eval('f')"), false, "exception");
        eval(createFunction("output=eval('f')"), false, "3");
        eval(createFunction("arr=['f'];output=eval(arr.join(''))"), false, "3");
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

    private void eval(final String script, final boolean hasFeature, final String expected) {
        final ContextFactory cf = new ContextFactory() {
            @Override
            protected boolean hasFeature(Context cx, int featureIndex) {
                if (Context.FEATURE_HTMLUNIT_EVAL_LOCAL_SCOPE == featureIndex) {
                    return hasFeature;
                }
                if (Context.FEATURE_DYNAMIC_SCOPE == featureIndex) {
                    return true;
                }
                return super.hasFeature(cx, featureIndex);
            }
        };
		final ContextAction action = new ContextAction() {
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
                    Assert.assertEquals(expected, result.toString());
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
