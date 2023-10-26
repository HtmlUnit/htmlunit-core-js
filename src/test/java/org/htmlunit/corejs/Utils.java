package org.htmlunit.corejs;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.ContextAction;
import org.htmlunit.corejs.javascript.ContextFactory;

/**
 * Misc utilities to make test code easier.
 * @author Marc Guillemot
 */
public class Utils {
    /**
     * Runs the action successively with all available optimization levels
     */
    public static void runWithAllOptimizationLevels(final ContextAction<Object> action) {
        runWithOptimizationLevel(action, -1);
        runWithOptimizationLevel(action, 0);
        runWithOptimizationLevel(action, 1);
    }

    /**
     * Runs the action successively with all available optimization levels
     */
    public static void runWithAllOptimizationLevels(final ContextFactory contextFactory, final ContextAction<Object> action) {
        runWithOptimizationLevel(contextFactory, action, -1);
        runWithOptimizationLevel(contextFactory, action, 0);
        runWithOptimizationLevel(contextFactory, action, 1);
    }

    /**
     * Runs the provided action at the given optimization level
     */
    public static void runWithOptimizationLevel(final ContextAction<Object> action, final int optimizationLevel) {
        runWithOptimizationLevel(new ContextFactory(), action, optimizationLevel);
    }

    /**
     * Runs the provided action at the given optimization level
     */
    public static void runWithOptimizationLevel(final ContextFactory contextFactory, final ContextAction<Object> action, final int optimizationLevel) {
        final Context cx = contextFactory.enterContext();
        try {
            cx.setOptimizationLevel(optimizationLevel);
            action.run(cx);
        }
        finally {
            Context.exit();
        }
    }
}
