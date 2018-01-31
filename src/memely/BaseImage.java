package memely;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import javax.imageio.ImageIO;

public class BaseImage implements Expression {
/**
 * BaseImage represents an expression consisting of just an image or a a caption. Newline characters are not allowed in caption. 
 */
	private final String base;
//	private final BufferedImage image;
	
	//TODO fix AF, Rep Invariant to account for image;
	
	// Abstraction Function
	//	 AF(base, height width) := an image of size height x width. If base is not a valid image file a image containing the caption name
	// Rep Invariant
	//   base != null
	//   base has a file extension suported by Image I/O package
	// Safety from Rep Exposure
	// 	getExpression returns a list containing the current expression
	
	private void checkRep() {
		assert !this.base.equals("");
		//TODO Check Rep needs to check image
		// assert this.image != null;
	}
	/**
	 * Creates an image from name. If name is a valid image File supported by BufferedImage then the file is rendered as a Buffered image.
	 * If the image does not exist in the current directory then a new file is created. 
	 * If name is not a valid Image file then a new image is created from the text.
	 * @param name - File location of existing image or new image or caption text
	 */
	public BaseImage(String filename) {
		this.base = filename;
		// this.image = ImageIO.read(new File(filename));
		this.checkRep();
		
		// TODO assert image has valid extension to prevent getWidth, getHeight from failing. 
	}
	
	@Override
	public String toString() {
		return this.base;
	}
	
	@Override 
	public boolean equals(Object that) {
		return that instanceof BaseImage && this.sameBase( (BaseImage) that);
		
		//throw new RuntimeException("not implemented");
	}
	
	private boolean sameBase(BaseImage that) {
		return this.base.equals(that.base);
	}
	
	@Override 
	public int hashCode() {
		//TODO This must be recursive???
		return this.base.hashCode();
	}
	
	public List<Expression> getExpression(){
		List<Expression> result = new ArrayList<Expression>();
		result.add(this);
		return result;
	}
	
	@Override
	public int getHeight() {
		try {
			BufferedImage image = ImageIO.read(new File(this.base));
			return image.getHeight();
		}
		catch (Exception e) {
			return -1;
//			throw new RuntimeException("Couldn't read image height");
		}
	}
	
	@Override
	public int getWidth() {
		try {
			BufferedImage image = ImageIO.read(new File(this.base));
			return image.getWidth();
		}
		catch (Exception e) {
			return -1;
			//throw new RuntimeException("Couldn't read image width");
		}
	}
	
	@Override
	public Expression layout() {
//		return this;
		// next line puts you in an infinite loop
		return new Rescale(this, this.getWidth(), this.getHeight());
	}
//	
//	public BufferedImage generate() {
//		throw new RuntimeException("not implemented");
//		BufferedImage image = ImageIO.read(new File(this.base));
//		// TODO What about when the base is text?
//		BufferedImage text = Examples.con
//		return image;
//	}
}