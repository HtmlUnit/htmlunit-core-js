package org.htmlunit.corejs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.htmlunit.corejs.javascript.regexp.RegExpImpl;
import org.htmlunit.corejs.javascript.regexp.SubString;
import org.junit.jupiter.api.Test;

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
        assertEquals("testInput", myTestRegExpImplSubclass.getInput());

        myTestRegExpImplSubclass.setMultiline(true);
        assertTrue(myTestRegExpImplSubclass.isMultiline());

        myTestRegExpImplSubclass.setParens(new SubString[] {new SubString("test") });
        assertEquals("test", myTestRegExpImplSubclass.getParens()[0].toString());

        myTestRegExpImplSubclass.setLastMatch(new SubString());
        assertEquals("", myTestRegExpImplSubclass.getLastMatch().toString());

        myTestRegExpImplSubclass.setLastParen(new SubString("testLastParen"));
        assertEquals("testLastParen", myTestRegExpImplSubclass.getLastParen().toString());

        myTestRegExpImplSubclass.setLeftContext(new SubString("testLeftContext"));
        assertEquals("testLeftContext", myTestRegExpImplSubclass.getLeftContext().toString());

        myTestRegExpImplSubclass.setRightContext(null);
        assertEquals(null, myTestRegExpImplSubclass.getRightContext());
    }
}
