package net.sourceforge.htmlunit;

import junit.framework.Assert;

import org.junit.Test;
import org.mozilla.javascript.regexp.RegExpImpl;
import org.mozilla.javascript.regexp.SubString;

/**
 * Test that some fields in RegExpImpl are protected.
 * We have to update this fields from our subclass.
 * Also tests that the class SubString is public. This
 * is needed to fill the fields with the right values.
 * 
 * https://bugzilla.mozilla.org/show_bug.cgi?id=598706
 * 
 * @author Ronald Brill
 */
public class RegExpImplSubclassingTest {

    protected class MyTestRegExpImplSubclass extends RegExpImpl {

        protected String getInput() {
            return input;
        }
        protected void setInput(String input) {
            this.input = input;
        }
        protected boolean isMultiline() {
            return multiline;
        }
        protected void setMultiline(boolean multiline) {
            this.multiline = multiline;
        }
        protected SubString[] getParens() {
            return parens;
        }
        protected void setParens(SubString[] parens) {
            this.parens = parens;
        }
        protected SubString getLastMatch() {
            return lastMatch;
        }
        protected void setLastMatch(SubString lastMatch) {
            this.lastMatch = lastMatch;
        }
        protected SubString getLastParen() {
            return lastParen;
        }
        protected void setLastParen(SubString lastParen) {
            this.lastParen = lastParen;
        }
        protected SubString getLeftContext() {
            return leftContext;
        }
        protected void setLeftContext(SubString leftContext) {
            this.leftContext = leftContext;
        }
        protected SubString getRightContext() {
            return rightContext;
        }
        protected void setRightContext(SubString rightContext) {
            this.rightContext = rightContext;
        }
    }
    
    
	/**
	 * @throws Exception if the test fails
	 */
	@Test
	public void testChangeAttributesFromSubclass() throws Exception {
		final MyTestRegExpImplSubclass myTestRegExpImplSubclass = new MyTestRegExpImplSubclass();

		myTestRegExpImplSubclass.setInput("testInput");
		Assert.assertEquals("testInput", myTestRegExpImplSubclass.getInput());

        myTestRegExpImplSubclass.setMultiline(true);
        Assert.assertTrue(myTestRegExpImplSubclass.isMultiline());

        myTestRegExpImplSubclass.setParens(new SubString[] {new SubString("test") });
        Assert.assertEquals("test", myTestRegExpImplSubclass.getParens()[0].toString());

        myTestRegExpImplSubclass.setLastMatch(SubString.emptySubString);
        Assert.assertEquals("", myTestRegExpImplSubclass.getLastMatch().toString());

        myTestRegExpImplSubclass.setLastParen(new SubString("testLastParen"));
        Assert.assertEquals("testLastParen", myTestRegExpImplSubclass.getLastParen().toString());

        myTestRegExpImplSubclass.setLeftContext(new SubString("testLeftContext"));
        Assert.assertEquals("testLeftContext", myTestRegExpImplSubclass.getLeftContext().toString());

        myTestRegExpImplSubclass.setRightContext(null);
        Assert.assertEquals(null, myTestRegExpImplSubclass.getRightContext());
	}
}
