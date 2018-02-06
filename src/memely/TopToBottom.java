package memely;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.List;

public class TopToBottom implements Expression {

	
	private final Expression top, bottom;
	
	
	private final boolean TRUE = true;
	
	// TODO - checkRep()
		private void checkRep() {
			assert TRUE;
		}
		
	public TopToBottom(Expression top, Expression bottom) {
		this.top = top;
		this.bottom = bottom;
		this.checkRep();
	}
	
	@Override
	public String toString() {
		return this.top.toString() + "\n---------\n" + this.bottom.toString();
	}
	
	@Override 
	public boolean equals(Object that) {
		return that instanceof TopToBottom && sameParts((TopToBottom) that);
	}
	
	private boolean sameParts(TopToBottom that) {
		return this.top.equals(that.top) && this.bottom.equals(that.bottom);
		
	}
	
	@Override
	public int hashCode() {
		//TODO This must be recursive???
		return this.toString().hashCode();
	}
	
	@Override
	public List<Expression> getExpression() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHeight() {
		int fullHeight;
		
		final int topWidth = this.top.getWidth();
		final int bottomWidth = this.bottom.getWidth();
		
		if (topWidth < bottomWidth) {
			final double aspectRatio = bottom.getHeight() / (double) bottomWidth;
			double newHeight = aspectRatio * topWidth;
			fullHeight = (int) newHeight + top.getHeight();
		} else {
			final double aspectRatio = top.getHeight() / (double) topWidth;
			final double newHeight = aspectRatio * bottomWidth;
			fullHeight = (int) newHeight + bottom.getHeight();
		}
		return fullHeight;
	}

	private int rescaledHeight() {
		double newHeight;
		
		final int topWidth = this.top.getWidth();
		final int bottomWidth = this.bottom.getWidth();
		
		if (topWidth < bottomWidth) {
			final double aspectRatio = bottom.getHeight() / (double) bottomWidth;
			newHeight = aspectRatio * topWidth;
		} else {
			final double aspectRatio = top.getHeight() / (double) topWidth;
			newHeight = aspectRatio * bottomWidth;
		}
		return (int) newHeight;
	}
	
	@Override
	public int getWidth() {
		final int topWidth = this.top.getWidth();
		final int bottomWidth = this.bottom.getWidth();
		return Math.min(topWidth, bottomWidth);
	}

	@Override
	public Expression layout() {
		final int topWidth = this.top.getWidth();
		final int bottomWidth = this.bottom.getWidth();
		
		Expression top = this.top;
		Expression bottom = this.bottom;
		
		int newWidth;
		double fullHeight;
		
		if (topWidth < bottomWidth) {
			top = this.top;
			newWidth = topWidth;
			
			final double aspectRatio = bottom.getHeight() / (double) bottomWidth;
			double newHeight = aspectRatio * topWidth;
			fullHeight = (int) newHeight + top.getHeight();
			
			//TODO This prevents layout from ever accessing the original size of right
			bottom = new Rescale(bottom, topWidth, (int) newHeight);
			
		} else {
			bottom = this.bottom;
			newWidth = bottomWidth;
			
			final double aspectRatio = top.getHeight() / (double) topWidth;
			final double newHeight = aspectRatio * bottomWidth;
			fullHeight = (int) newHeight + bottom.getHeight();
			
			//TODO This prevents layout from ever accessing the original size of right
			top = new Rescale(top, bottomWidth, (int) newHeight);
		}
		return new Rescale(new TopToBottom(top.layout(), bottom.layout()), newWidth, (int) fullHeight);
	}
	
	@Override
	public BufferedImage generate() throws IOException {
		final int upperLeftX = 0;
		final int upperLeftY = 0;
		
		final int outputWidth = this.getWidth();
		final int outputHeight = this.getHeight();
		
		final BufferedImage topImage = this.top.generate();
		final BufferedImage bottomImage = this.bottom.generate();
		
		final BufferedImage output = new BufferedImage(outputWidth, outputHeight, BufferedImage.TYPE_4BYTE_ABGR);
        final Graphics graphics = output.getGraphics();
        
        final ImageObserver NO_OBSERVER_NEEDED = null;
        
        final int topWidth = this.top.getWidth();
		final int bottomWidth = this.bottom.getWidth();
		final int topHeight = this.top.getHeight();
		final int bottomHeight = this.bottom.getHeight();
        
		if (topWidth == bottomWidth) {
			graphics.drawImage(topImage, 
	                upperLeftX, upperLeftY,
	                outputWidth, topHeight, 
	                NO_OBSERVER_NEEDED);
	        graphics.drawImage(bottomImage, 
	                upperLeftX, topHeight,
	                outputWidth, bottomHeight, 
	                NO_OBSERVER_NEEDED);
		} else if (topWidth < bottomWidth) {
			graphics.drawImage(topImage, 
	                upperLeftX, upperLeftY,
	                outputWidth, topHeight, 
	                NO_OBSERVER_NEEDED);
	        graphics.drawImage(bottomImage, 
	                upperLeftX, topHeight,
	                outputWidth, this.rescaledHeight(), 
	                NO_OBSERVER_NEEDED);
		} else {
			final int yOffset = outputHeight - bottomHeight;
			
			graphics.drawImage(topImage, 
					upperLeftX, upperLeftY, 
					outputWidth, this.rescaledHeight(), 
					NO_OBSERVER_NEEDED);
			
			graphics.drawImage(bottomImage,
					upperLeftX, yOffset,
					outputWidth, bottomHeight,
					NO_OBSERVER_NEEDED);
		}
			
        return output;
	}

}
