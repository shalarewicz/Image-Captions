package memely;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
//import java.io.File;
import java.io.IOException;
import java.util.List;

//import javax.imageio.ImageIO;

public class Caption implements Expression {

	// AF(caption) ::= A caption
	// Rep Invariang ::= caption is not null, width, height > 0
	// Safety from rep exposure. caption is an immutable String. 
	
	private final String caption;
	private final int width, height;
	BufferedImage image;
	
    public Caption(String text) {
    	this.caption = text;
    	
        // make a tiny 1x1 image at first so that we can get a Graphics object, 
        // which we need to compute the width and height of the text
        BufferedImage output = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = (Graphics2D) output.getGraphics();
        
        // use the system font, but make it 96-point bold
        final Font defaultFont = graphics.getFont();
        final int fontSizeInPoints = 96;
        final Font font = new Font(defaultFont.getFontName(), Font.BOLD, fontSizeInPoints);
        graphics.setFont(font);
        
        // get the bounding box of the string, rounding up the coordinates
        Rectangle2D rectangle = font.getStringBounds(this.caption.substring(1, text.length()-1), graphics.getFontRenderContext());
        //System.out.println(caption + " has rectangle " + rectangle);
        this.width =   (int) Math.ceil(rectangle.getWidth());
        this.height =  (int) Math.ceil(rectangle.getHeight());
        final int xOffset = (int) Math.ceil(rectangle.getX());
        final int yOffset = (int) Math.ceil(rectangle.getY());
        
        // now that we know the text's width and height,
        // recreate the image big enough to fit
        output = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        graphics = (Graphics2D) output.getGraphics();
        
        // render the text in the font we chose, and white, and antialiased
        graphics.setFont(font);
        graphics.setColor(Color.WHITE);
        graphics.setRenderingHints(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
        
        // finally draw the string. drawString's x,y expects the left end of the text *baseline* (instead
        // of the upper left corner of its bounding box), but xOffset and yOffset give us that.
        graphics.drawString(this.caption.substring(1, text.length()-1), -xOffset, -yOffset);
        this.image = output;
    }
	

	@Override
	public String toString() {
		return this.caption;
	}
	
	@Override 
	public boolean equals(Object that) {
		return that instanceof BaseImage && this.sameCaption( (Caption) that);
		
		//throw new RuntimeException("not implemented");
	}
	
	private boolean sameCaption(Caption that) {
		return this.caption.equals(that.caption);
	}
	
	@Override 
	public int hashCode() {
		//TODO This must be recursive???
		return this.caption.hashCode();
	}
	
	@Override
	public List<Expression> getExpression() {
		// TODO Deprecate
		return null;
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
		return new Rescale(this, this.width, this.height);
	}

	@Override
	public BufferedImage generate() throws IOException {
		return image;
	}

}
