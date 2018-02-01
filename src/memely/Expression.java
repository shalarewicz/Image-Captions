/* Copyright (c) 2017 MIT 6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package memely;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * An immutable data type representing an image expression, as defined
 * in the PS3 handout.
 * 
 * <p>PS3 instructions: this is a required ADT interface.
 * You MUST NOT change its name or package or the names or type signatures of existing methods.
 * You may, however, add additional methods, or strengthen the specs of existing methods.
 * Declare concrete variants of Expression in their own Java source files.
 */
public interface Expression {
    
    // Datatype definition
    //   Expression = BaseImage(s : String, height: int, width: int) 
	//				+ BaseImage(file : File);
	//				+ Caption(S: String);
	//  			+ Rescale(expression: Expression, height : int, width : int)
	// 				+ SideBySide(left : Expression, right Expression)
	// TODO: Top to bottom, upper caption, lower caption
    
    /**
     * Parse an expression.
     * @param input expression to parse, as defined in the PS3 handout.
     * @return expression AST for the input
     * @throws IllegalArgumentException if the expression is syntactically invalid.
     */
    public static Expression parse(String input) {
    	try {
    		return ExpressionParser.parse(input);
    		
    	} catch (Exception e) {
    		System.out.println(e.getMessage() + e.getClass());
    		throw new IllegalArgumentException("Can't parse input " + input);
    	}
    }
    
    /**
     * @return a parsable representation of this expression, such that
     * for all e:Expression, e.equals(Expression.parse(e.toString())).
     */
    @Override 
    public String toString();

    /**
     * @param that any object
     * @return true if and only if this and that are structurally-equal
     * Expressions, as defined in the PS3 handout. 
     */
    @Override
    public boolean equals(Object that);
    
    /**
     * @return hash code value consistent with the equals() definition of structural
     * equality, such that for all e1,e2:Expression,
     *     e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode();
    
    //TODO implement value and a Number class. Fairly confident this is necessary but not 100%. I no longer believe that it is as
    // numbers are read by the grammar and listed as children in the AST for a rescale operator which is the only placy they're used
    // Check the api and print children to see what children returns
    // Actuall don't need it since we can just parse the text in the switch statement and pull the numbers out
    // Doesn't make sense since the class NUmber wouldn't follow the specs in Expression (ie generate, layout..)
    /**
     * 
     * @return integer value of the expression. throws exception if expression doesn't represent a number
     */
//    public int value();
    
    /**
     * Returns the base expression(s) of the current expression
     * For example the call getExpression on an expression resizing an object would return the 
     * expression to be resized. 
     * 
     * Calling getExpression on an expression gluing two objects together would return [left, right]
     * @return a list of base expressions ordered left, right or top, bottom
     */
    //TODO: Deprecate
    public List<Expression> getExpression();
    
    /**
     * Obtain the height of the image represented by the current expression
     * @return the height of the image
     * @throws IOException - If file type is invalid
     */
    public int getHeight();
    
    /**
     * Obtain the width of the image represented by the current expression
     * @return the width of the image
     */
    public int getWidth();
        
    /**
     * 
     * @return Returns a parsable Expression
     */
    public Expression layout();
    
    /**
     * Creates an image from the given expression.
     * 
     * @return A buffered image representation of the current expression
     * @throws IOException 
     */
    public BufferedImage generate() throws IOException;
    
}
