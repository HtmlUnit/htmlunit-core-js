package org.htmlunit.corejs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.htmlunit.corejs.javascript.RhinoException;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=549604">bug 549604</a>.
 * This tests verify the properties of a JS exception and ensures that they don't change
 * with different optimization levels.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
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
        testScriptStackTrace(script, expectedStackTrace, true);
        testScriptStackTrace(script, expectedStackTrace, false);
    }

    private static void testScriptStackTrace(final String script, final String expectedStackTrace,
            boolean interpreted) {
        try {
            Utils.executeScript(script, interpreted);
        }
        catch (final RhinoException e) {
            assertEquals(expectedStackTrace, e.getScriptStackTrace());
        }
    }

    @Test
    public void fileName() {
        Utils.assertWithAllModes_ES6("test.js", "try { null.method() } catch (e) { e.fileName }");
        Utils.assertWithAllModes_ES6("test.js", "try { null.property } catch (e) { e.fileName }");
    }

    @Test
    public void lineNumber() {
        Utils.assertWithAllModes_ES6(0, "try { null.method() } catch (e) { e.lineNumber }");
        Utils.assertWithAllModes_ES6(1, "try {\n null.method() \n} catch (e) { e.lineNumber }");
        Utils.assertWithAllModes_ES6(3, "\ntry \n{\n null.method() \n} catch (e) { e.lineNumber }");

        Utils.assertWithAllModes_ES6(1, "function f() {\n null.method(); \n}\n try { f() } catch (e) { e.lineNumber }");
    }

    @Test
    public void stack() {
        Utils.assertWithAllModes_ES6(
                "\tat test.js:0" + LS,
                "try { null.method() } catch (e) { e.stack }");

        final String expectedStack = "\tat test.js:1 (f)" + LS + "\tat test.js:3" + LS;
        final String script = "function f() {\n null.method(); \n}\n try { f() } catch (e) { e.stack }";
        Utils.assertWithAllModes_ES6(expectedStack, script);
    }
}
