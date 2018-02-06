package memely;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SideBySide implements Expression {
	
	/**
	 * Represents a new expression consisting of two expressions side by side. Each expression represents an image. In order to 
	 * create the new image the smaller iage is shrunk while preserving the aspect ratio. The width is rounded to the nearest integer.
	 */
	private final Expression left, right;
	// private final int height, width;
	
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
		this.left = left;
		this.right = right;
		
		this.checkRep();
	}

	@Override
	public String toString() {
		return "(" + this.left.toString() + "|" + this.right.toString() + ")";
	}
	
	@Override 
	public boolean equals(Object that) {
		return that instanceof SideBySide && sameParts((SideBySide) that);
	}
	
	private boolean sameParts(SideBySide that) {
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
		final int leftHeight = this.left.getHeight();
		final int rightHeight = this.right.getHeight();
		return Math.min(leftHeight, rightHeight);
	}

	
	private int rescaledWidth() {
		double newWidth;
		
		final int leftHeight = this.left.getHeight();
		final int rightHeight = this.right.getHeight();
		
		if (rightHeight > leftHeight) {
			final double aspectRatio = right.getWidth() / (double) rightHeight;
			newWidth = aspectRatio * leftHeight;
		} else {
			final double aspectRatio = left.getWidth() / (double) leftHeight;
			newWidth = aspectRatio * rightHeight;
		}
		return (int) newWidth;
	}
	@Override
	public int getWidth() {
		int fullWidth;
		
		final int leftHeight = this.left.getHeight();
		final int rightHeight = this.right.getHeight();
		
		if (rightHeight > leftHeight) {
			final double aspectRatio = right.getWidth() / (double) rightHeight;
			double newWidth = aspectRatio * leftHeight;
			fullWidth = (int) newWidth + left.getWidth();
		} else {
			final double aspectRatio = left.getWidth() / (double) leftHeight;
			final double newWidth = aspectRatio * rightHeight;
			fullWidth = (int) newWidth + right.getWidth();
		}
		return fullWidth;
	}

	@Override
	public Expression layout() {
		final int leftHeight = this.left.getHeight();
		final int rightHeight = this.right.getHeight();
		
		Expression left = this.left;
		Expression right = this.right;
		
		int newHeight;
		double fullWidth;
		
		if (rightHeight > leftHeight) {
			left = this.left;
			newHeight = leftHeight;
			
			final double aspectRatio = right.getWidth() / (double) rightHeight;
			double newWidth = aspectRatio * leftHeight;
			fullWidth = (int) newWidth + left.getWidth();
			
			//TODO This prevents layout from ever accessing the original size of right
			right = new Rescale(right, (int) newWidth, leftHeight);
			
		} else {
			right = this.right;
			newHeight = rightHeight;
			
			final double aspectRatio = left.getWidth() / (double) leftHeight;
			final double newWidth = aspectRatio * rightHeight;
			fullWidth = (int) newWidth + right.getWidth();
			
			//TODO This prevents layout from ever accessing the original size of right
			left = new Rescale(left, (int) newWidth, rightHeight);
		}
		
			return new Rescale(new SideBySide(left.layout(), right.layout()), (int) fullWidth, newHeight);
	}

	@Override
	public BufferedImage generate() throws IOException{
		final int upperLeftX = 0;
		final int upperLeftY = 0;
		
		final int outputWidth = this.getWidth();
		final int outputHeight = this.getHeight();
		
		final BufferedImage leftImage = this.left.generate();
		final BufferedImage rightImage = this.right.generate();
		
		final BufferedImage output = new BufferedImage(outputWidth, outputHeight, BufferedImage.TYPE_4BYTE_ABGR);
		final Graphics graphics = output.getGraphics();

		final ImageObserver NO_OBSERVER_NEEDED = null;

		final int leftHeight = this.left.getHeight();
		final int rightHeight = this.right.getHeight();
		
		if (leftHeight == rightHeight) {
			 graphics.drawImage(leftImage, 
		                upperLeftX, upperLeftY,
		                this.left.getWidth(), outputHeight, 
		                NO_OBSERVER_NEEDED);
			 graphics.drawImage(rightImage, 
		                this.left.getWidth(), upperLeftY,
		                this.right.getWidth(), outputHeight, //TODO: Need to draw at resized right Width
		                NO_OBSERVER_NEEDED);
		} else if (leftHeight < rightHeight) {
			
			graphics.drawImage(leftImage, 
	                upperLeftX, upperLeftY,
	                this.left.getWidth(), outputHeight, 
	                NO_OBSERVER_NEEDED);
			
			graphics.drawImage(rightImage, 
					this.left.getWidth(), upperLeftY, 
					this.rescaledWidth(), outputHeight, 
					NO_OBSERVER_NEEDED);
		} else {
			final int rightWidth = this.right.getWidth();
			final int xOffset = outputWidth - rightWidth;
			
			graphics.drawImage(leftImage, 
					upperLeftX, upperLeftY, 
					this.rescaledWidth(), outputHeight, 
					NO_OBSERVER_NEEDED);
			
			graphics.drawImage(rightImage,
					xOffset, upperLeftY,
					rightWidth, outputHeight, 
					NO_OBSERVER_NEEDED);
		}
			
        return output;
	}
	

}
