package net.sourceforge.htmlunit;

import org.junit.Test;

/**
 * Test that encodeURIComponent uses upper case letters for hex encoding.
 * https://bugzilla.mozilla.org/show_bug.cgi?id=429121
 * (now fixed in Rhino itself)
 * @author Marc Guillemot
 */
public class UpperCaseHexTest {

	/**
	 * @throws Exception if the test fails
	 */
	@Test
	public void testUpperCaseHex() throws Exception {
		final String script = "var a = encodeURIComponent('$ a ;');\n"
			+ "if (a != '%24%20a%20%3B') throw 'got: >' + a + '<'";
		
		Utilities.executeScript(script);
	}
}
