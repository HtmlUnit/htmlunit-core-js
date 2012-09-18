package net.sourceforge.htmlunit;

import org.junit.Test;

/**
 * Test that Object properties are returned in the creation order.
 * (now fixed in Rhino itself)
 * @author Marc Guillemot
 */
public class PropertiesOrderTest {

	/**
	 * Test for properties order as exposed in issues
	 * https://bugzilla.mozilla.org/show_bug.cgi?id=419090
	 * and
	 * https://bugzilla.mozilla.org/show_bug.cgi?id=383592
	 * @throws Exception if the test fails
	 */
	@Test
	public void testPropertiesOrder() throws Exception {
		final String script = "var o = {a: 1, b: 1, c: 1, d: 1};\n"
			+ "var str = '';\n"
			+ "for (var i in o) str += i + ' ';\n"
			+ "if (str != 'a b c d ')\n"
			+ "  throw 'Got: ' + str";
		
		Utilities.executeScript(script);
	}
}
