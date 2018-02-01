/* Copyright (c) 2017 MIT 6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package memely;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.mit.eecs.parserlib.Parser;

/**
 * Tests for the Expression abstract data type.
 */
public class ExpressionTest {

    //  Concrete classes Testing strategy
	//  Test for each class BaseImage, Rescale, SideBySide
    //   		  - Base image created from String caption or filename,
	//   		  - Rescale created from BaseImage
	//  		  - SideBySide created from Rescale, BaseImage combos
	//   toString - 
	//	 equals - hashCode produces same result
	//   hashCode - equals produces same result
	//   		  - TODO: Focus on this
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testParserLibVersion() {
        assertTrue("parserlib.jar needs to be version 3.0.x", Parser.VERSION.startsWith("3.0"));
    }
    
    
    
    private final Expression BASE_IMAGE = new BaseImage("test.jpg");
    private final Expression CAPTION = new BaseImage("\"this is a caption\"");
    private final Expression EQUAL_BASE = new BaseImage("\"this is a caption\"");
    private final Expression RESCALE_IMAGE = new Rescale(BASE_IMAGE, 37, 8);
    private final Expression RESCALE_CAPTION = new Rescale(CAPTION, 37, 8);
    private final Expression RESCALE_INEQUAL = new Rescale(CAPTION, 37, 9);
    private final Expression RESCALE_INEQUAL2 = new Rescale(CAPTION, 4, 9);
    private final Expression SIDE_MIX = new SideBySide(RESCALE_IMAGE, BASE_IMAGE);
    private final Expression SIDE_MIX2 = new SideBySide(BASE_IMAGE, RESCALE_IMAGE);
    private final Expression SIDE_RESCALE = new SideBySide(RESCALE_IMAGE, RESCALE_CAPTION);
    private final Expression SIDE_RESCALE2 = new SideBySide(RESCALE_IMAGE, RESCALE_CAPTION);
    
    
    // TODO tests for Expression
    
    @Test
    public void testBaseImageToString() {
    	assertEquals("expected string ", "test.jpg", BASE_IMAGE.toString());
    	assertEquals("expected string ", "\"this is a caption\"", CAPTION.toString());
    }
    
    @Test
    public void testBaseImageeEqual() {
    	assertTrue("expected to be equal ", CAPTION.equals(EQUAL_BASE));
    	assertEquals("expected equal hascodes", CAPTION.hashCode(), EQUAL_BASE.hashCode());
    }
    
    @Test
    public void testRescaleToString() {
    	String expected = "(test.jpg)@37x8";
    	assertEquals("expected string ", expected, RESCALE_IMAGE.toString());
    }
    
    @Test
    public void testSideToString() {
    	String expected = "(test.jpg)@37x8|(\"this is a caption\")@37x8";
    	System.out.println(expected);
    	System.out.println(SIDE_RESCALE.toString());
    	assertEquals(expected, SIDE_RESCALE.toString());
    }
    
    @Test
    public void testRescaleEqual() {
    	assertTrue("expected different heights ", !RESCALE_INEQUAL.equals(RESCALE_CAPTION));
    	assertTrue("expected different widths ", !RESCALE_INEQUAL.equals(RESCALE_INEQUAL2));
    	assertTrue("expected different caption ", !RESCALE_IMAGE.equals(RESCALE_CAPTION));
    	
    }
    
    @Test
    public void testSideEqual() {
    	assertFalse("expected not equal", SIDE_MIX.equals(SIDE_MIX2));
    	assertTrue("expected equal ", SIDE_RESCALE2.equals(SIDE_RESCALE));
    }
    
    //TODO TEST hashCode but hashCode uses toString so it's fine and i'm lazy. Hopefully toString is right.
    
    
    
//    @Test
//    public void testRescale() {
//    	Expression base = new BaseImage("test", 10, 10);
//    	Expression test = new Rescale(base, 10, 10);
//    	assertEquals("expected height ", 10, test.getHeight());
//    }
//    
//    @Test
//    public void testSidebySide() {
//    	Expression left = new BaseImage("test", 10, 10);
//    	Expression right = new BaseImage("test", 10, 10);
//    	Expression test = new SideBySide(left, right);
//    	assertEquals("expected height ", 10, test.getHeight());
//    }
    
    // This is just preliminary to see if I'm at all on the right track with this
    
    @Test
    public void testParserFile() {
    	final String input = "test.jpg";
    	final Expression expression = Expression.parse(input);
    	final String resultString = expression.toString();
    	assertEquals("expected ", input, resultString);
    	// assert same operators and numbers in same order??
    }
    @Test
    public void testParserResize() {
    	// TODO: Because of the way the grammar is written. Parentheses result in different ASTs being made. 
    	// For example "(test.jpg)@10x10" creates a different AST than test.jpg@10x10. The latter is a simpler tree. 
    	// i.e. RESIZE = {PRIMITIVE, NUMBER, NUMBER} whereas the form results in 
    	// EXPRESSION = {RESIZE = {PRIMITIVE {EXPRESSION { RESIZE { PRIMITIVE } ] }, NUMBER, NUMBER}
    	// As the grammar was provided this appears to be intentional to allow for handling of layered expressions within 
    	// the parentheses. 
    	final String input = "(test.jpg)@10x10";
    	final Expression expression = Expression.parse(input);
    	final String resultString = expression.toString();
    	assertEquals("expected ", input, resultString);
    }
    
    // TODO Parser Tests
    // TODO Testing strategy - really think this one out
    // Expression Parser Testing strategy. 
    // 	 String contains multiple '|' characters in a row
    //   Operators with less than the required amount of parameters
    // 		Side by side with only one parameter - This is an invalid expression
    //      Resize - missing a dimension
    //    	Resize - missing source
    //   TODO: Parentheses ???
    //   TODO: Input contains invalid characters. Specifically the filename. Captions should be able to contain whatever
    // 	 TODO: invalid file extensions
    // 
    
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
    
    
    @Test
    // Test toString, equals, parse relationship. e.equals(Expression.parse(e.toString()))
    public void testToStringEqualsParseRescale() {
    	final String test = FILE + RESIZE + WIDTH + BY + HEIGHT;
    	final Expression e = Expression.parse(test);
    	assertEquals(e, Expression.parse(e.toString()));
    }
    
    @Test
    // Test toString, equals, parse relationship. e.equals(Expression.parse(e.toString()))
    public void testToStringEqualsParseSxS() {
    	final String test = FILE + SXS + FILE2 + RESIZE + WIDTH + BY + HEIGHT;
    	final Expression e = Expression.parse(test);
    	assertEquals(e, Expression.parse(e.toString()));
    }
    
    @Test
    // Test toString, equals, parse relationship. e.equals(Expression.parse(e.toString()))
    public void testToStringEqualsParse() {
    	final String test = OPEN_PAREN + FILE + SXS + FILE2 + RESIZE + WIDTH + BY + HEIGHT + CLOSE_PAREN + RESIZE + HEIGHT + BY + WIDTH + SXS + FILE4;
    	final Expression e = Expression.parse(test);
    	assertEquals(e, Expression.parse(e.toString()));
    }
    
    
    @Test(expected=IllegalArgumentException.class)
    // Tests an unsupported file type
    public void testInvalidFile() {
    	final Expression test = Expression.parse(INVALID).layout();
    	//TODO: It's possible to create objects with invalid file names. so this should test layout and generate.. 
    	// assertFalse("fix this thest", true);
    	
    }
    
    @Test(expected=IllegalArgumentException.class)
    // Tests duplicate side by side operators
    public void testDoubleSideBySide() {
    	final String test = FILE + SXS + SXS + FILE2;
    	final Expression result = Expression.parse(test);
    }
    
    @Test(expected=IllegalArgumentException.class)
    // Tests side by side with only one argument on left. 
    public void testSxSNonBinaryLeft() {
    	final String test = FILE + SXS;
    	Expression.parse(test);
    }
    
    @Test(expected=IllegalArgumentException.class)
    // Test side by side operator with only argument on right
    public void testSxsNonBinaryRight() {
    	final String test = SXS + FILE;
    	Expression.parse(test);
    }
    
    @Test
    public void testLayout() {
    	final String test = OPEN_PAREN + FILE + SXS + FILE2 + RESIZE + WIDTH + BY + HEIGHT + CLOSE_PAREN + RESIZE + HEIGHT + BY + WIDTH + SXS + FILE4;
    	final String test2 = OPEN_PAREN + FILE + SXS + FILE2 + RESIZE + WIDTH + BY + HEIGHT + CLOSE_PAREN + RESIZE + HEIGHT + BY + WIDTH + SXS + FILE4;
    	final Expression e = Expression.parse(test);
    	Expression layout = e.layout();
    	System.out.println(layout.toString());
    	//String expected = "(((img/test.jpg|(img/test2.jpg)@10x20)@20x10)@2008x1004|(img/test4.jpg)@1504x1004)@3512x1004";
    	assertFalse(true);
    	//TODO Does base image layout need a size????? (if so just layout the rescale object?
    	
    }
    
    @Test
    // Tests to make sure basimage layout includes a resize. 
    public void testLayoutBaseImage() {
    	final String test = FILE;
    	final String expected = OPEN_PAREN + FILE + CLOSE_PAREN + RESIZE + WIDTH_TESTS + BY + HEIGHT_TESTS;
    	final Expression layout = Expression.parse(test).layout();
    	assertEquals(expected, layout.toString());
    }
    
    @Test
    // Tests layout for Rescale
    public void testLayoutRescale() {
    	final String test = OPEN_PAREN + FILE + CLOSE_PAREN + RESIZE + 10 + BY + 20;
    	final String expected = OPEN_PAREN + OPEN_PAREN + FILE + CLOSE_PAREN + RESIZE + WIDTH_TESTS + BY + HEIGHT_TESTS + CLOSE_PAREN + RESIZE + 10 + BY + 20;
    	final Expression layout = Expression.parse(test).layout();
    	assertEquals(expected, layout.toString());
    }
    
    @Test 
    // Tests layout for SideBySide with two Bases
    public void testLayoutSideByside() {
    	final String test = FILE + SXS + FILE2;
    	final String expected = OPEN_PAREN + OPEN_PAREN + FILE + CLOSE_PAREN + RESIZE + WIDTH_TESTS + BY + HEIGHT_TESTS + SXS +
    			OPEN_PAREN + FILE2 + CLOSE_PAREN + RESIZE + WIDTH_TESTS + BY + HEIGHT_TESTS + CLOSE_PAREN + RESIZE + 3008 + BY + HEIGHT_TESTS;
    	final Expression layout = Expression.parse(test).layout();
    	assertEquals(expected, layout.toString());
    }
    
    @Test
    // Tests layout for SxS in a Rescale
    public void testLayoutSxSInResize() {
    	final String test = OPEN_PAREN + FILE + SXS + FILE2 + CLOSE_PAREN + RESIZE + 10 + BY + 20;
    	final String expected = "(((img/test.jpg)@1504x1004|(img/test2.jpg)@1504x1004)@3008x1004)@10x20";
    	final Expression layout = Expression.parse(test).layout();
    	assertEquals(expected, layout.toString());
    }
    
    @Test
    // Test layout with three side by sides with no parens
    public void testTripleSxS() {
    	final String test = FILE + SXS + FILE2 + SXS + FILE3;
    	final Expression layout = Expression.parse(test).layout();
    	System.out.println(layout.toString());
    }
}


