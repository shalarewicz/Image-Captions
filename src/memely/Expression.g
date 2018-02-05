/* Copyright (c) 2017 MIT 6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */

@skip whitespace {
    expression ::= resize ((sideBySideOperator resize) | (topToBottomOperator resize) | (topOverlayOperator resize) | (bottomOverlayOperator resize))*;
    resize ::= primitive ('@' number 'x' number)?;
    primitive ::= filename | '(' expression ')' | caption;
}
topToBottomOperator ::= '---' '-'*;
sideBySideOperator ::= '|';
topOverlayOperator ::= '^';
bottomOverlayOperator ::= '_';
filename ::= [A-Za-z0-9./]+;
caption ::= '"' [A-Za-z0-9 ]* '"'*;
number ::= [0-9]+;
whitespace ::= [ \t\r\n]+;
