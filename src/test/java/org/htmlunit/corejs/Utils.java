/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.htmlunit.corejs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Context.EvaluationMethod;
import org.htmlunit.corejs.javascript.ContextAction;
import org.htmlunit.corejs.javascript.ContextFactory;
import org.htmlunit.corejs.javascript.EcmaError;
import org.htmlunit.corejs.javascript.JavaScriptException;
import org.htmlunit.corejs.javascript.TopLevel;

/**
 * Misc utilities to make test code easier.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class Utils {

    /** The default set of levels to run tests at. */
    public static final int[] DEFAULT_OPT_LEVELS = new int[] {-1, 9};

    /**
     * helper for joining multiple lines into one string, so that you don't need to do {@code
     * "line1\n" + "line2\n" + "line3"} by yourself
     *
     * @param lines the lines to join
     * @return the joined lines
     */
    public static String lines(String... lines) {
        return String.join("\n", lines);
    }

    /** Make the ctor private; this is a utility classe. */
    private Utils() {}

    /**
     * Runs the action successively with interpreted and optimized mode
     *
     * @param action the action to execute
     */
    public static <T> void runWithAllModes(final ContextAction<T> action) {
        runWithAllModes(new ContextFactory(), action);
    }

    /**
     * Runs the action successively with interpreted and optimized mode
     *
     * @param contextFactory the context factory to use
     * @param action the action to execute
     */
    public static <T> void runWithAllModes(final ContextFactory contextFactory, final ContextAction<T> action) {
        // we only use the interpreter at the moment
        //        for (var e : EvaluationMethod.values()) {
        //            runWithMode(contextFactory, action, e);
        //        }
        runWithMode(contextFactory, action, EvaluationMethod.Interpreter);
    }

    /**
     * Runs the provided action at the given interpretation mode
     *
     * @param contextFactory the context factory to use
     * @param action the action to execute
     * @param mode evaluation mode to use
     */
    public static void runWithMode(
            final ContextFactory contextFactory,
            final ContextAction<?> action,
            final Context.EvaluationMethod mode) {

        try (final Context cx = contextFactory.enterContext()) {
            cx.setEvaluationMethod(mode);
            cx.setLanguageVersion(Context.VERSION_ES6);
            action.run(cx);
        }
    }

    /**
     * Execute the provided script and assert the result.
     *
     * @param expected the expected result
     * @param script the javascript script to execute
     */
    public static void assertWithAllModes(final Object expected, final String script) {
        assertWithAllModes(null, expected, script);
    }

    /**
     * Execute the provided script and assert the result.
     *
     * @param message the message to be used if this fails
     * @param expected the expected result
     * @param script the javascript script to execute
     */
    public static void assertWithAllModes(
            final String message,
            final Object expected,
            final String script) {
        assertWithAllModes(new ContextFactory(), message, expected, script);
    }

    /**
     * Execute the provided script and assert the result.
     *
     * @param contextFactory a user defined {@link ContextFactory}
     * @param message the message to be used if this fails
     * @param expected the expected result
     * @param script the javascript script to execute
     */
    public static void assertWithAllModes(
            final ContextFactory contextFactory,
            final String message,
            final Object expected,
            final String script) {
        runWithAllModes(
                contextFactory,
                cx -> {
                    cx.setLanguageVersion(Context.VERSION_ES6);

                    final TopLevel scope = cx.initStandardObjects();
                    final Object res = cx.evaluateString(scope, script, "test.js", 0, null);

                    if (expected instanceof Integer && res instanceof Double) {
                        assertEquals(
                                ((Integer) expected).doubleValue(),
                                ((Double) res).doubleValue(),
                                0.00001,
                                message);
                        return null;
                    }
                    if (expected instanceof Double && res instanceof Integer) {
                        assertEquals(
                                ((Double) expected).doubleValue(),
                                ((Integer) res).doubleValue(),
                                0.00001,
                                message);
                        return null;
                    }

                    assertEquals(expected, res, message);
                    return null;
                });
    }

    /**
     * Execute the provided script and assert an {@link EcmaError}. The error message of the {@link
     * EcmaError} has to start with the provided expectedMessage.
     *
     * @param expectedMessage the expected result
     * @param script the javascript script to execute
     */
    public static void assertEcmaError(final String expectedMessage, final String script) {
        assertException(EcmaError.class, expectedMessage, script);
    }

    /**
     * Execute the provided script and assert an {@link org.htmlunit.corejs.javascript.JavaScriptException}.
     * The error message of the {@link org.htmlunit.corejs.javascript.JavaScriptException} has to start with
     * the provided expectedMessage.
     *
     * @param expectedMessage the expected result
     * @param script the javascript script to execute
     */
    public static void assertJavaScriptException(
            final String expectedMessage, final String script) {
        assertException(JavaScriptException.class, expectedMessage, script);
    }

    /**
     * Execute the provided script and assert an {@link EcmaError}. The error message of the {@link
     * EcmaError} has to start with the provided expectedMessage.
     *
     * @param <T> the type of the expected throwable
     * @param expectedThrowable the class of the expected exception
     * @param expectedMessage the expected exception message
     * @param script the javascript script to execute
     */
    public static <T extends Exception> void assertException(
            final Class<T> expectedThrowable,
            final String expectedMessage,
            String script) {
        assertException(
                new ContextFactory(), expectedThrowable, expectedMessage, script);
    }

    /**
     * Execute the provided script and assert an {@link EcmaError}. The error message of the {@link
     * EcmaError} has to start with the provided expectedMessage.
     *
     * @param contextFactory the context factory to be used
     * @param <T> the type of the expected throwable
     * @param expectedThrowable the class of the expected exception
     * @param expectedMessage the expected exception message
     * @param script the javascript script to execute
     */
    public static <T extends Exception> void assertException(
            final ContextFactory contextFactory,
            final Class<T> expectedThrowable,
            final String expectedMessage,
            String script) {

        // to avoid false positives because we use startsWith()
        assertTrue(
                expectedMessage != null && !expectedMessage.isEmpty(),
                "expectedMessage can't be empty");

        Utils.runWithAllModes(
                contextFactory,
                cx -> {
                    cx.setLanguageVersion(Context.VERSION_ES6);

                    TopLevel scope = cx.initStandardObjects();

                    T e =
                            assertThrows(
                                    expectedThrowable,
                                    () -> cx.evaluateString(scope, script, "test", 1, null));

                    assertTrue(
                            e.getMessage().startsWith(expectedMessage),
                            "'"
                                    + e.getMessage()
                                    + "' does not start with '"
                                    + expectedMessage
                                    + "'");
                    return null;
                });
    }
}
