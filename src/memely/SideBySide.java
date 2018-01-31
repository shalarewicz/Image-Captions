package memely;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SideBySide implements Expression {
	
	/**
	 * Represents a new expression consisting of two expressions side by side. Each expression represents an image. In order to 
	 * create the new image the smaller iage is shrunk while preserving the aspect ratio. The width is rounded to the nearest integer.
	 */
	private final Expression left, right;
	private final int height, width;
	
	// Abstraction function
	// 		AF(left, right) ::= left | right glued side by side
	// RepInvariant
	//   None. left, right != null
	// Safety from rep exposure
	// 	 getEpression() returns references to immutable objects. 
	
	private final boolean TRUE = true;
	
	// TODO - checkRep()
		private void checkRep() {
			assert TRUE;
		}
	
	/**
	 * Makes a new SideBySide Expression left | right
	 * @param left - expression will appear on the left
	 * @param right - expression will appear on the right
	 */
	public SideBySide(Expression left, Expression right) {
//		this.left = left;
//		this.right = right;
		
		final int leftHeight = left.getHeight();
		final int rightHeight = right.getHeight();
		
		if (leftHeight > rightHeight) {
			this.left = left;
			this.height = leftHeight;
			
			final double aspectRatio = right.getWidth() / (double) rightHeight;
			final double newWidth = aspectRatio * leftHeight;
			this.width = (int) newWidth + left.getWidth();
			
			//TODO This prevents layout from ever accessing the original size of right
			this.right = new Rescale(right, (int) newWidth, leftHeight);
			
		} else {
			this.right = right;
			this.height = rightHeight;
			
			final double aspectRatio = left.getWidth() / (double) leftHeight;
			final double newWidth = aspectRatio * rightHeight;
			this.width = (int) newWidth + right.getWidth();
			
			//TODO This prevents layout from ever accessing the original size of right
			this.left = new Rescale(left, (int) newWidth, rightHeight);
		}
		
		this.checkRep();
	}

	@Override
	public String toString() {
//		System.out.println("Using SxS toString");
//		System.out.println("Left is " + this.left.toString());
//		System.out.println("Right is " + this.right.toString());
		return this.left.toString() + "|" + this.right.toString();
	}
	
	@Override 
	public boolean equals(Object that) {
		return that instanceof SideBySide && sameParts((SideBySide) that);
	}
	
	private boolean sameParts(SideBySide that) {
//		List<Expression> parts = that.getExpression();
//		Expression left = parts.get(0);
//		Expression right = parts.get(1);
//		return this.left.equals(left) && this.right.equals(right);
		return this.left.equals(that.left) && this.right.equals(that.right);
		
	}
	
	@Override
	public int hashCode() {
		//TODO This must be recursive???
		return this.toString().hashCode();
	}
	
	public List<Expression> getExpression(){
		List<Expression> result = new ArrayList<Expression>();
		result.add(this.left);
		result.add(this.right);
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
		// layout left, layout right. adjust size of entire
		Expression left = this.left.layout();
		Expression right = this.right.layout();
		System.out.println("Creating rescale in SxS layout");
		return new Rescale(this, this.getWidth(), this.getHeight());
	}
//
//	@Override
//	public BufferedImage generate() {
//		// TODO Auto-generated method stub
//		return null;
//	}
	

}
