package net.sourceforge.htmlunit;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;

public abstract class ContextActionImpl implements ContextAction {
	public Object run(final Context cx) {
		try {
			return doRun(cx);
		} catch (final Exception e) {
			if (e instanceof RuntimeException)
				throw (RuntimeException) e;
			else
				throw new RuntimeException(e);
		}
	}
	protected abstract Object doRun(final Context cx) throws Exception;
}
