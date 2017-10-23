package memely;

import java.io.File;
import java.io.IOException;

import edu.mit.eecs.parserlib.ParseTree;
import edu.mit.eecs.parserlib.Parser;
import edu.mit.eecs.parserlib.UnableToParseException;
import edu.mit.eecs.parserlib.Visualizer;

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
        EXPRESSION, RESIZE, PRIMITIVE, TOPTOBOTTOMOPERATOR, FILENAME, NUMBER, WHITESPACE,
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
        System.out.println("parse tree " + parseTree);

        // display the parse tree in a web browser, for debugging only
        Visualizer.showInBrowser(parseTree);

        // make an AST from the parse tree
        final Expression expression = makeAbstractSyntaxTree(parseTree);
        System.out.println("AST " + expression);
        
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
        case EXPRESSION: // expression ::= resize ('|' resize)*;
            {
                // TODO
                throw new RuntimeException("not implemented");
            }

        case RESIZE: // resize ::= primitive ('@' number 'x' number)?;
            {
                // TODO
                throw new RuntimeException("not implemented");
            }
            
        case PRIMITIVE: // primitive ::= filename | '(' expression ')';
            {
                // TODO
                throw new RuntimeException("not implemented");
            }
            
        case FILENAME: // filename ::= [A-Za-z0-9./]+;
            {
                // TODO
                throw new RuntimeException("not implemented");
            }
            
        // ...
        // TODO more rules
        //
        
        default:
            throw new AssertionError("should never get here");
        }

    }

}
