package net.sourceforge.htmlunit;

import org.junit.Test;

/**
 * Unit tests for <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=538172">Bug 538172 - 
 * Number.toString fails for 2.274341322658976E-309</a>  
 * @author Marc Guillemot
 */
public class NumberTest {

    /**
	 * @throws Exception if the test fails
	 */
	@Test
	public void test() throws Exception {
		final String script = "var n = 2.274341322658976E-309;\n"
			+ "var s = '' + n;\n"
			+ "if (s != '2.274341322658976e-309') throw 'got ' + s";
		
		Utilities.executeScript(script);
		Utilities.executeScript("'' + 1");
	}
}
