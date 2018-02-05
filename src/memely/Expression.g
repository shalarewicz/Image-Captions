/* Copyright (c) 2017 MIT 6.031 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */

@skip whitespace {
    expression ::= sideBySide | topToBottom;
    sideBySide ::= (topOverlay | bottomOverlay | resize) ('|' expression)*;
    topToBottom ::= (topOverlay | bottomOverlay | resize) ('---' expression)*;
    topOverlay ::= resize ('^' expression)+;
    bottomOverlay ::= resize ('_' expression)+;
    resize ::= primitive ('@' number 'x' number)?;
    primitive ::= filename | '(' expression ')' | caption;
}

caption ::= '"' [A-Za-z0-9 ]* '"'*;
filename ::= [A-Za-z0-9./]+;
number ::= [0-9]+;
whitespace ::= [ \t\r\n]+;
