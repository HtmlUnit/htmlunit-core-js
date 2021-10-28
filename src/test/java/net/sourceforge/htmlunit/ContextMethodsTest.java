package net.sourceforge.htmlunit;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.ErrorReporter;
import net.sourceforge.htmlunit.corejs.javascript.Evaluator;
import net.sourceforge.htmlunit.corejs.javascript.Script;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * Unit tests for <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=481152">Bug 481152 -
 * Open Context class for extension</a>
 * @author Marc Guillemot
 */
public class ContextMethodsTest {

    /**
     * When {@link Context#compileString(String, String, int, Object)} is protected and not final,
     * we can capture code passed to eval.
     * @throws Exception if the test fails
     */
    @Test
    public void captureEvalScript() throws Exception {
        final List<String> compiled = new ArrayList<>();
        final ContextFactory cf = new ContextFactory() {
            @Override
            protected Context makeContext() {
                return new Context(this)
                {
                    @Override
                    protected Script compileString(String source, Evaluator compiler,
                            ErrorReporter compilationErrorReporter, String sourceName,
                            int lineno, Object securityDomain) {
                        compiled.add(source);
                        return super.compileString(source, compiler, compilationErrorReporter,
                                sourceName, lineno, securityDomain);
                    }
                };
            }
        };

        final String source = "eval('1 + 2')";

        final ContextAction<Object> action = new ContextAction<Object>() {
            @Override
            public Object run(Context cx) {
                final Scriptable scope = cx.initSafeStandardObjects();
                final Script script = cx.compileString(source, "", 1, (Object) null);
                return script.exec(cx, scope);
            };
        };
        cf.call(action);

        final String[] expected = { "eval('1 + 2')", "1 + 2" };
        assertEquals(Arrays.asList(expected), compiled);
    }
}
