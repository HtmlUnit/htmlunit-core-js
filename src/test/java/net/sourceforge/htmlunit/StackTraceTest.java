package net.sourceforge.htmlunit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.JavaScriptException;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * In certain cases stacktrace if lost in case of exception
 * https://bugzilla.mozilla.org/show_bug.cgi?id=376717
 * @author Marc Guillemot
 */
public class StackTraceTest {

    /**
     * As of CVS head on May, 11. 2009, stacktrace information is lost when a call to some
     * native function has been made.
     */
    @Test
    public void testFailureStackTrace() {
        final String lineSeparator = System.getProperty("line.separator");
        final String source1 = "function f2() { throw 'hello'; }; f2();";
        final String source2 = "function f2() { 'H'.toLowerCase(); throw 'hello'; }; f2();";

        runWithExpectedStackTrace(source1,
                "\tat test.js (f2)" + lineSeparator + "\tat test.js" + lineSeparator); // works
        runWithExpectedStackTrace(source2,
                "\tat test.js (f2)" + lineSeparator + "\tat test.js" + lineSeparator); // fails
    }

    private static void runWithExpectedStackTrace(final String _source, final String _expectedStackTrace) {
        final ContextAction action = new ContextAction() {
            @Override
            public Object run(final Context cx) {
                final Scriptable scope = cx.initSafeStandardObjects();
                try {
                    cx.evaluateString(scope, _source, "test.js", 0, null);
                }
                catch (final JavaScriptException e) {
                    assertEquals(_expectedStackTrace, e.getScriptStackTrace());
                    return null;
                }
                throw new RuntimeException("Exception expected!");
            }
        };
        Utils.runWithOptimizationLevel(action, -1);
    }
 }
