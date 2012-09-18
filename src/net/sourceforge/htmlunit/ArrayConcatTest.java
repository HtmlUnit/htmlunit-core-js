package net.sourceforge.htmlunit;

import org.junit.Test;

/**
 * Unit tests for <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=477604">Bug 477604 - 
 * Array.concat causes ArrayIndexOutOfBoundException with non dense array</a>  
 * @author Marc Guillemot
 */
public class ArrayConcatTest {

    /**
	 * @throws Exception if the test fails
	 */
	@Test
	public void concat() throws Exception {
		final String script = "var a = [1, 2, 3];\n"
			+ "for (var i=10; i<20; ++i) a[i] = 't' + i;\n"
			+ "var b = [1, 2, 3];\n"
			+ "b.concat(a)";
		
		Utilities.executeScript(script);
	}
}
