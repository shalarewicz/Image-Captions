/* Copyright (c) 2017 MIT 6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package memely;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for the static methods of Commands.
 */
public class CommandsTest {

    // Commands.layout() Testing strategy
    //   TODO
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    private final String SXS = "|";
    private final String RESIZE = "@";
    private final String FILE = "img/test.jpg";
    private final String FILE2 = "img/test2.jpg";
    private final String FILE3 = "img/test3.jpg";
    private final String FILE4 = "img/test4.jpg";
    private final String INVALID = "img/bad.docx";
    private final String OPEN_PAREN = "(";
    private final String CLOSE_PAREN = ")";
    private final int WIDTH = 10;
    private final int HEIGHT = 20;
    private final String BY = "x";
    private final int HEIGHT_TESTS = 1004;
    private final int WIDTH_TESTS = 1504;
    
    // TODO tests for Commands.layout()
    @Test(expected=IllegalArgumentException.class)
    // Tests an unsupported file type
    public void testInvalidFile() {
    	final Expression test = Expression.parse(INVALID).layout();
    }
    
    @Test
    public void testLayout() {
    	final String test = OPEN_PAREN + FILE + SXS + FILE2 + RESIZE + WIDTH + BY + HEIGHT + CLOSE_PAREN + RESIZE + HEIGHT + BY + WIDTH + SXS + FILE4;
    	final String expected = "(((((img/test.jpg)@1504x1004|(((img/test2.jpg)@1504x1004)@10x20)@502x1004)@2006x1004)@20x10)@2008x1004|(img/test4.jpg)@1504x1004)@3512x1004";
    	final Expression e = Expression.parse(test);
    	Expression layout = e.layout();
    	System.out.println(layout.toString());
    	assertEquals(expected,layout.toString());
    }
    
    
    //TODO: Comands.generate() Testing Strategy
    
    // TODO: Commands.generate tests
    
}
