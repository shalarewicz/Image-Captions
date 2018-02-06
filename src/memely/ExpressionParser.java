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
        EXPRESSION, RESIZE, PRIMITIVE, TOPTOBOTTOM, FILENAME, NUMBER, WHITESPACE, CAPTION, SIDEBYSIDE, TOPOVERLAY, BOTTOMOVERLAY
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
        final ParseTree<ExpressionGrammar> parseTree = parser.parse(string);
        // System.out.println("Parse Tree: " + parseTree);
        // display the parse tree in a web browser, for debugging only
        Visualizer.showInBrowser(parseTree);

        // make an AST from the parse tree
        final Expression expression = makeAbstractSyntaxTree(parseTree);
        
       // System.out.println("AST: " + expression);
        return expression;
    }
    
    /**
     * Convert a parse tree into an abstract syntax tree.
     * 
     * @param parseTree constructed according to the grammar in IntegerExpression.g
     * @return abstract syntax tree corresponding to parseTree
     */
    private static Expression makeAbstractSyntaxTree(final ParseTree<ExpressionGrammar> parseTree) {
        switch (parseTree.name()) {
        case EXPRESSION: //  expression ::= sideBySide | topToBottom
            {
            	
            	
            	return makeAbstractSyntaxTree(parseTree.children().get(0));
//            	// Get the expression for the first resize
//            	// then glue it side by side to any subsequent expressions
//            	
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
        case TOPTOBOTTOM: //  topToBottom ::= resize ('---' expression)*
           
        {
        	final List<ParseTree<ExpressionGrammar>> children = parseTree.children();
        	Expression top = makeAbstractSyntaxTree(children.get(0));
        	for (int i = 1; i < children.size(); i++) {
        		final Expression bottom = makeAbstractSyntaxTree(children.get(i));
        		top = new TopToBottom(top, bottom);
        	}
            return top;
        }
        
        case SIDEBYSIDE: // sideBySide ::= resize ('|' expression)*;
        {
        	final List<ParseTree<ExpressionGrammar>> children = parseTree.children();
        	Expression left = makeAbstractSyntaxTree(children.get(0));
        	for (int i = 1; i < children.size(); i++) {
        		final Expression right = makeAbstractSyntaxTree(children.get(i));
        			left = new SideBySide(left, right);
        	}
        	System.out.println("Left is " + left);
            return left;
        }
        
        case TOPOVERLAY: // topOverlay ::= resize ('^' expression)*;
        {
        	final List<ParseTree<ExpressionGrammar>> children = parseTree.children();
        	Expression bottom = makeAbstractSyntaxTree(children.get(0));
        	for (int i = 1; i < children.size(); i++) {
        		System.out.println(children.get(i));
        		final Expression top = makeAbstractSyntaxTree(children.get(i));
        		System.out.println("Top is " + top);
        		bottom = new TopOverlay(bottom, top);
        	}
        	return bottom;
        }

        case BOTTOMOVERLAY: //  bottomOverlay ::= resize ('_' expression)*;
        {
        	final List<ParseTree<ExpressionGrammar>> children = parseTree.children();
        	Expression top = makeAbstractSyntaxTree(children.get(0));
        	for (int i = 1; i < children.size(); i++) {
        		final Expression bottom = makeAbstractSyntaxTree(children.get(i));
        		top = new BottomOverlay(bottom, top);
        	}
        	return top;
        }
        case RESIZE: // resize ::= primitive ('@' number 'x' number)?;
            {
            	final List<ParseTree<ExpressionGrammar>> children = parseTree.children();
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
            throw new AssertionError("should never get here");
        }

    }

}
