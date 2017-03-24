package net.sourceforge.htmlunit;

import org.junit.Test;

/**
 * Test that regexp like "[/]" are parsed correctly.
 * https://bugzilla.mozilla.org/show_bug.cgi?id=368019
 * (now fixed in Rhino itself)
 * @author Marc Guillemot
 */
public class RegExpUnescapedSlashTest {

	/**
	 * @throws Exception if the test fails
	 */
	@Test
	public void testUnescapedSlashInSquareBrackets() throws Exception {
		final String script = "var o = /[/]/;";
		
		Utilities.executeScript(script);
	}
}
