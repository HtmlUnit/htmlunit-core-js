package org.htmlunit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.ContextAction;
import org.htmlunit.corejs.javascript.RhinoException;
import org.htmlunit.corejs.javascript.ScriptableObject;

/**
 * Unit tests for <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=549604">bug 549604</a>.
 * This tests verify the properties of a JS exception and ensures that they don't change with different optimization levels.
 * @author Marc Guillemot
 */
public class ExceptionPropertiesTest {
    final static String LS = System.getProperty("line.separator");

    @Test
    public void scriptStackTrace() {
        testScriptStackTrace("null.method()", "\tat myScript.js:1" + LS);
        final String script = "function f() \n"
            + "{\n"
            + "  null.method();\n"
            + "}\n"
            + "f();\n";
        testScriptStackTrace(script, "\tat myScript.js:3 (f)" + LS + "\tat myScript.js:5" + LS);
    }

    private static void testScriptStackTrace(final String script, final String expectedStackTrace) {
        testScriptStackTrace(script, expectedStackTrace, -1);
        testScriptStackTrace(script, expectedStackTrace, 0);
        testScriptStackTrace(script, expectedStackTrace, 1);
    }

    private static void testScriptStackTrace(final String script, final String expectedStackTrace,
            final int optimizationLevel) {
        try {
            Utilities.executeScript(script, optimizationLevel);
        }
        catch (final RhinoException e) {
            assertEquals(expectedStackTrace, e.getScriptStackTrace());
        }
    }

    @Test
    public void fileName() {
        testIt("try { null.method() } catch (e) { e.fileName }", "myScript.js");
        testIt("try { null.property } catch (e) { e.fileName }", "myScript.js");
    }

    @Test
    public void lineNumber() {
        testIt("try { null.method() } catch (e) { e.lineNumber }", 1);
        testIt("try {\n null.method() \n} catch (e) { e.lineNumber }", 2);
        testIt("\ntry \n{\n null.method() \n} catch (e) { e.lineNumber }", 4);

        testIt("function f() {\n null.method(); \n}\n try { f() } catch (e) { e.lineNumber }", 2);
    }

    @Test
    public void stack() {
        testIt("try { null.method() } catch (e) { e.stack }", "\tat myScript.js:1" + LS);
        final String expectedStack = "\tat myScript.js:2 (f)" + LS + "\tat myScript.js:4" + LS;
        testIt("function f() {\n null.method(); \n}\n try { f() } catch (e) { e.stack }", expectedStack);
    }

    private static void testIt(final String script, final Object expected) {
        final ContextAction<Object> action = new ContextAction<Object>() {
            @Override
            public Object run(final Context cx) {
                cx.setLanguageVersion(Context.VERSION_ES6);

                try {
                    final ScriptableObject scope = cx.initSafeStandardObjects();
                    final Object o = cx.evaluateString(scope, script,
                            "myScript.js", 1, null);
                    assertEquals(expected, o);
                    return o;
                }
                catch (final RuntimeException e) {
                    throw e;
                }
                catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Utils.runWithAllOptimizationLevels(action);
    }
}
