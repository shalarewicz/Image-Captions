/* Copyright (c) 2017 MIT 6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package memely;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * String-based commands provided by the meme generation system.
 * 
 * <p>PS3 instructions: this is a required class.
 * You MUST NOT change its name or package or the names or type signatures of existing methods.
 * You MUST NOT add fields, constructors, or instance methods.
 * You may, however, add additional static methods, or strengthen the specs of existing methods.
 */
public class Commands {
    
    /**
     * Lay out an image meme expression, computing the size of each subexpression.
     * @param expression the meme expression to lay out
     * @return a valid expression with the same meaning as the input expression, but where every non-resize subexpression 
     * (all operators plus lowest-level primitives like filenames and captions) is enclosed by a resize operator @WxH 
     * indicating its computed size, according to the language definition in the ps3 handout.
     * @throws IllegalArgumentException if the expression is syntactically invalid
     * @throws IOException if any of the filenames used in the expression can't be read as images
     */
    public static String layout(String expression) throws IOException {
        throw new RuntimeException("unimplemented");
    }
    
    /**
     * Generate the meme of an expression.
     * @param expression the expression to generate
     * @return the image generated by the expression, according to the language definition in the ps3 handout.
     * @throws IllegalArgumentException if the expression is syntactically invalid
     * @throws IOException if any of the filenames used in the expression can't be read as images
     */
    public static BufferedImage generate(String expression) throws IOException {
        throw new RuntimeException("unimplemented");
    }
    
}
