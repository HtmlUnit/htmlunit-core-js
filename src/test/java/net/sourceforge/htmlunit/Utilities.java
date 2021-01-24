package net.sourceforge.htmlunit;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * Misc utilities.
 * @author Marc Guillemot
 */
public class Utilities {
    /**
     * Execute the provided script in a fresh context
     * @param script the script
     */
    static void executeScript(final String script) {
        final ContextAction action = new ContextAction() {
            @Override
            public Object run(Context cx) {
                final Scriptable scope = cx.initSafeStandardObjects();
                return cx.evaluateString(scope, script, "myScript.js", 1, null);
            }
        };
        Utils.runWithAllOptimizationLevels(action);
    }

    /**
     * Execute the provided script in a fresh context
     * @param script the script
     */
    static void executeScript(final String script, final int optimizationLevel) {
        final ContextAction action = new ContextAction() {
            @Override
            public Object run(Context cx) {
                final Scriptable scope = cx.initSafeStandardObjects();
                return cx.evaluateString(scope, script, "myScript.js", 1, null);
            }
        };
        Utils.runWithOptimizationLevel(action, optimizationLevel);
    }
}
