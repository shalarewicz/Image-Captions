package memely;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Rescale implements Expression {
	
	/**
	 * Rescale represents an expression using the "@" operator to resize an expression. The expression cannot be 
	 * rescaled to width <= 0 or height <= 0;
	 */
	private final int width, height;
	private final Expression expression;
	
	// Abstraction Function
	// 		AF(expression, with, height) ::= Resizes expression to width x height
	// Rep Invariant
	// 		width, height > 0
	// Safety from Rep Exposure
	//	  References aren't returned. getExpression return a reference to this expression
	
	private void checkRep() {
		assert this.width > 0 && this.height > 0;
	}
	/**
	 * Make a new Rescale expression
	 * @param expression
	 * @param width - new width of the expression
	 * @param height - new height of the expression
	 */
	public Rescale(Expression expression, int width, int height) {
		this.width = width;
		this.height = height;
		this.expression = expression;
		this.checkRep();
	}
	
	public Rescale(Expression expression) {
		this.expression = expression;
		// TODO When I add width and height ops use them get the width and height of the base
		this.width = expression.getWidth();
		this.height = expression.getHeight();
		this.checkRep();
	}
	
	@Override
	public String toString() {
		return "(" + expression.toString() + ")@" + width + "x" + height;
	}
	
	@Override
	public boolean equals(Object that) {
		return that instanceof Rescale && this.sameExpression( (Rescale) that);
	}
	
	private boolean sameExpression(Rescale that) {
//		int index = that.toString().lastIndexOf("@");
//		String resize = that.toString().substring(index);
//		int x = resize.indexOf("x");
//		int width = Integer.parseInt(resize.substring(0, x));
//		int height = Integer.parseInt(resize.substring(x));
		return this.height == that.height && this.width == that.width && this.expression.equals(that.expression);
	}
	
	@Override
	public int hashCode() {
		//TODO This must be recursive???
		return this.toString().hashCode();
	}
	
	public List<Expression> getExpression() {
		List<Expression> result = new ArrayList<Expression>();
		result.add(this.expression);
		return result;
	}
	
	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public int getWidth() {
		return this.width;
	}


	@Override
	public Expression layout() {
		if (this.height == this.expression.getHeight() && this.width == this.expression.getWidth()) {
			System.out.println("used this");
			return this.expression.layout();
		}
		else {return new Rescale(this.expression.layout(), this.width, this.height);}
//		// TODO Auto-generated method stub
//		return null;
	}
//
//	@Override
//	public BufferedImage generate() {
//		// TODO Auto-generated method stub
//		return null;
//	}
	

}
