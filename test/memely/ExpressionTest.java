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

    // Testing strategy
    //   TODO
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testParserLibVersion() {
        assertTrue("parserlib.jar needs to be version 3.0.x", Parser.VERSION.startsWith("3.0"));
    }
    
    
    // TODO tests for Expression
    
}
