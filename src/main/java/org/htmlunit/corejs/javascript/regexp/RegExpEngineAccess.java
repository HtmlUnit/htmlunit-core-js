package org.htmlunit.corejs.javascript.regexp;

import org.htmlunit.corejs.javascript.Context;

/**
 * Simple wrapper to use the RegExp engine without js.
 * This is required for implementing input validation.
 *
 * @author Ronald Brill
 */
public class RegExpEngineAccess {

    public static final class CompiledRegExp {
        private final RECompiled reCompiled_;

        public CompiledRegExp(final RECompiled reCompiled) {
            reCompiled_ = reCompiled;
        }
    }

    public static CompiledRegExp compile(final Context cx, final String regExp, final String regExpArgs) {
        final RECompiled reCompiled = NativeRegExp.compileRE(cx, regExp, regExpArgs, false);
        return new CompiledRegExp(reCompiled);
    }

    public static boolean matches(final Context cx, final String input, final CompiledRegExp compiledRegExp) {
        boolean matches = NativeRegExp.matchRegExp(cx,
                                new REGlobalData(),
                                compiledRegExp.reCompiled_,
                                input, 0, input.length(),
                                false);
        return matches;
    }
}
