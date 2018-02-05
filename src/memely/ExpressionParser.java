package memely;

import java.io.File;
import java.io.IOException;

import edu.mit.eecs.parserlib.ParseTree;
import edu.mit.eecs.parserlib.Parser;
import edu.mit.eecs.parserlib.UnableToParseException;
import edu.mit.eecs.parserlib.Visualizer;
import java.util.List;

public class ExpressionParser {
    /**
     * Main method. Parses and then reprints an example expression.
     * 
     * @param args command line arguments, not used
     * @throws UnableToParseException if example expression can't be parsed
     */
    public static void main(final String[] args) throws UnableToParseException {
        final String input = "foobar|baz";
        System.out.println(input);
        final Expression expression = ExpressionParser.parse(input);
        System.out.println(expression);
    }
    
    // the nonterminals of the grammar
    private enum ExpressionGrammar {
        EXPRESSION, RESIZE, PRIMITIVE, TOPTOBOTTOMOPERATOR, FILENAME, NUMBER, WHITESPACE, CAPTION, SIDEBYSIDEOPERATOR, TOPOVERLAYOPERATOR, BOTTOMOVERLAYOPERATOR
    }

    private static Parser<ExpressionGrammar> parser = makeParser();
    
    /**
     * Compile the grammar into a parser.
     * 
     * @param grammarFilename <b>Must be in this class's Java package.</b>
     * @return parser for the grammar
     * @throws RuntimeException if grammar file can't be read or has syntax errors
     */
    private static Parser<ExpressionGrammar> makeParser() {
        try {
            // read the grammar as a file, relative to the project root.
            final File grammarFile = new File("src/memely/Expression.g");
            return Parser.compile(grammarFile, ExpressionGrammar.EXPRESSION);

            // A better way would read the grammar as a "classpath resource", which would allow this code 
            // to be packed up in a jar and still be able to find its grammar file:
            //
            // final InputStream grammarStream = IntegerExpression.class.openResourceAsStream("IntegerExpression.g");
            // return Parser.compile(grammarStream, IntegerGrammar.EXPRESSION);
            //
            // See http://www.javaworld.com/article/2077352/java-se/smartly-load-your-properties.html
            // for a discussion of classpath resources.
            

        // Parser.compile() throws two checked exceptions.
        // Translate these checked exceptions into unchecked RuntimeExceptions,
        // because these failures indicate internal bugs rather than client errors
        } catch (IOException e) {
            throw new RuntimeException("can't read the grammar file", e);
        } catch (UnableToParseException e) {
            throw new RuntimeException("the grammar has a syntax error", e);
        }
    }

    /**
     * Parse a string into an expression.
     * @param string string to parse
     * @return IntegerExpression parsed from the string
     * @throws UnableToParseException if the string doesn't match the IntegerExpression grammar
     */
    public static Expression parse(final String string) throws UnableToParseException {
        // parse the example into a parse tree
    	System.out.println("input is " + string);
        final ParseTree<ExpressionGrammar> parseTree = parser.parse(string);
        System.out.println("Parse Tree: " + parseTree);
        // display the parse tree in a web browser, for debugging only
        //Visualizer.showInBrowser(parseTree);

        // make an AST from the parse tree
        final Expression expression = makeAbstractSyntaxTree(parseTree);
        
        System.out.println("AST: " + expression);
        return expression;
    }
    
    private static int operatorPriority(ExpressionGrammar op) {
    	switch (op) {
    	case TOPOVERLAYOPERATOR:
    		return TOPOVERLAY_PRIORITY;
    	case BOTTOMOVERLAYOPERATOR:
    		return BOTTOMOVERLAY_PRIORITY;
    	case SIDEBYSIDEOPERATOR:
    		return SIDEBYSSIDE_PRIORITY;
    	case TOPTOBOTTOMOPERATOR:
    		return TOPTOBOTTOM_PRIORITY;
    	default: throw new AssertionError("Should never get here");
    	}
    }
 
    private static final int TOPOVERLAY_PRIORITY = 4;
    private static final int BOTTOMOVERLAY_PRIORITY = 3;
    private static final int SIDEBYSSIDE_PRIORITY = 2;
    private static final int TOPTOBOTTOM_PRIORITY = 1;
    /**
     * Convert a parse tree into an abstract syntax tree.
     * 
     * @param parseTree constructed according to the grammar in IntegerExpression.g
     * @return abstract syntax tree corresponding to parseTree
     */
    private static Expression makeAbstractSyntaxTree(final ParseTree<ExpressionGrammar> parseTree) {
    	System.out.println("Trying to match " + parseTree.name());
    	
        switch (parseTree.name()) {
        case EXPRESSION: //  expression ::= sideBySide | topToBottom
            {
            	final List<ParseTree<ExpressionGrammar>> children = parseTree.children();
            	Expression current = makeAbstractSyntaxTree(children.get(0));
            	for (int i = 1; i < children.size() - 1; i=i+2) {
            		System.out.println(i);
	            	switch (parseTree.children().get(i).name()) {
		            	case TOPOVERLAYOPERATOR: {
		            		System.out.println("found ^ making AST for " + children.get(i + 1));
		            		final Expression next = makeAbstractSyntaxTree(children.get(i + 1));
		            		current =  new TopOverlay(current, next);
		            		continue;
		            	}
						case BOTTOMOVERLAYOPERATOR:{
							//TODO check next operator
							
							if (i + 2 < children.size() - 1 && BOTTOMOVERLAY_PRIORITY >= operatorPriority(children.get(i + 2).name())) {
								System.out.println("found _ making AST for " + children.get(i + 1));
								final Expression next = makeAbstractSyntaxTree(children.get(i + 1));
								current =  new BottomOverlay(next, current);
								continue;						
							}
							else {
								// TODO Put in stack? Look up the calculate algorithm???? this seems wrwong?
							}
						}
						case SIDEBYSIDEOPERATOR:{
							final Expression next = makeAbstractSyntaxTree(children.get(i + 1));
		        			current =  new SideBySide(current, next);
		        			continue;
						}
						case TOPTOBOTTOMOPERATOR: {
							final Expression next = makeAbstractSyntaxTree(children.get(i + 1));
		        			current =  new TopToBottom(current, next);
		        			continue;
						}
						default: {
							System.out.println("Couldn't match " + parseTree.children().get(i).name());
							throw new AssertionError("should never get here in expression");
						}
	            	}
	            //	System.out.println(current);
            	}
            	System.out.println("Current");
            	return current;
//            	// Get the expression for the first resize
//            	// then glue it side by side to any subsequent expressions
//            	
//            	// TODO What about parentheses??? Looks like they're handled automatically because data type is recursive
//            	// primitive can be composed of expressions enclosed in parentheses
//            	final List<ParseTree<ExpressionGrammar>> children = parseTree.children();
//            	Expression left = makeAbstractSyntaxTree(children.get(0));
//            	System.out.println("Current child is" + children);
//            	System.out.println("Size = " + children.size());
//            	for (int i = 1; i < children.size(); i++) {
//            		final Expression right = makeAbstractSyntaxTree(children.get(i));
//            		left = new SideBySide(left, right);
//            		System.out.println("made a side by side");
//            	}
//                return left;
            }
//        case TOPTOBOTTOMOPERATOR: //  topToBottom ::= resize ('---' expression)*
//           
//        {
//        	final List<ParseTree<ExpressionGrammar>> children = parseTree.children();
//        	Expression top = makeAbstractSyntaxTree(children.get(0));
//        	for (int i = 1; i < children.size(); i++) {
//        		final Expression bottom = makeAbstractSyntaxTree(children.get(i));
//        		top = new TopToBottom(top, bottom);
//        	}
//            return top;
//        }
//        
//        case SIDEBYSIDEOPERATOR: // sideBySide ::= resize ('|' expression)*;
//        {
//        	final List<ParseTree<ExpressionGrammar>> children = parseTree.children();
//        	Expression left = makeAbstractSyntaxTree(children.get(0));
//        	for (int i = 1; i < children.size(); i++) {
//        		final Expression right = makeAbstractSyntaxTree(children.get(i));
//        		left = new SideBySide(left, right);
//        	}
//            return left;
//        }
//        
//        case TOPOVERLAYOPERATOR: // topOverlay ::= resize ('^' expression)*;
//        {
//        	final List<ParseTree<ExpressionGrammar>> children = parseTree.children();
//        	System.out.println("Children of ^ are" + children);
//        	Expression bottom = makeAbstractSyntaxTree(children.get(0));
//        	for (int i = 1; i < children.size(); i++) {
//        		final Expression top = makeAbstractSyntaxTree(children.get(i));
//        		bottom = new TopOverlay(bottom, top);
//        	}
//        	return bottom;
//        }
//
//        case BOTTOMOVERLAYOPERATOR: //  bottomOverlay ::= resize ('_' expression)*;
//        {
//        	final List<ParseTree<ExpressionGrammar>> children = parseTree.children();
//        	Expression top = makeAbstractSyntaxTree(children.get(0));
//        	for (int i = 1; i < children.size(); i++) {
//        		final Expression bottom = makeAbstractSyntaxTree(children.get(i));
//        		top = new BottomOverlay(bottom, top);
//        	}
//        	return top;
//        }
        case RESIZE: // resize ::= primitive ('@' number 'x' number)?;
            {
                // TODO This makes me realize we need a number class in order to create the rescale object. I don't think so. 
            	// Numbers are listed as children in the ASt
            	final List<ParseTree<ExpressionGrammar>> children = parseTree.children();
            	// TODO Remove print statement. Using to see if a Number class needs to be added
            	final Expression primitive = makeAbstractSyntaxTree(children.get(0));
            	if (children.size() == 1) {
            		return primitive;
            	}
            	else {
            		final int width = Integer.parseInt((children.get(1)).text());
            		final int height = Integer.parseInt(children.get(2).text());
            		return new Rescale(primitive, width, height);
            	}
            	//System.out.println(parseTree.text());
            	//TODO Return statement
            	//return primitive;
//            	final Expression width = makeAbstractSyntaxTree(children.get(1));
//            	final Expression height = makeAbstractSyntaxTree(children.get(2));
//            	return new Rescale(primitive, width.getExpression(), height);
            }
            
        case PRIMITIVE: // primitive ::= filename | '(' expression ')';
            {
            	final ParseTree<ExpressionGrammar> child = parseTree.children().get(0);
            	switch (child.name()) {
            	
            	case FILENAME:
            		return makeAbstractSyntaxTree(child);
            		
            	case EXPRESSION:
            		return makeAbstractSyntaxTree(child);
            	case CAPTION:
            		return makeAbstractSyntaxTree(child);
            		
            	default:
                     throw new AssertionError("should never get here");
            	}
            		
            }
            
        case FILENAME: // filename ::= [A-Za-z0-9./]+;
            {
            	return new BaseImage(parseTree.text());
            }
            
        case CAPTION: // caption ::= '"' .* '"'*;
        {
        	return new Caption(parseTree.text());
        }
        
        
        // ...
        // TODO more rules
        //
        
        default:
            throw new AssertionError("should never get here after caption");
        }

    }

}
