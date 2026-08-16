lexer grammar Jinja2Lexer;

@header {package gen.ANTLR;}

tokens {
    CLOSE, WS, ID, NUMBER, STRING,
    PLUS, MINUS, MULT, DIV, MOD,
    LPAREN, RPAREN, LBRACK, RBRACK, COMMA, DOT, PIPE,
    ASSIGN, LT, GT, LTE, GTE, EQUAL, NEQ,
    AND_KW, OR_KW, NOT_KW, IN_KW, IS_KW,
    NONE_KW, TRUE_KW, FALSE_KW,
    IF_KW, FOR_KW, ELIF_KW, ELSE_KW, ENDIF_KW, ENDFOR_KW,
    SET_KW, BLOCK_KW, ENDBLOCK_KW,
    EXTENDS_KW, INCLUDE_KW,
    WITH_KW, WITHOUT_KW, CONTEXT_KW, IGNORE_KW, MISSING_KW,
    EXPR_START, STMT_START, COMMENT_START,
    TEXT, NEWLINE,
    COMMENT_HASH, COMMENT_CONTENT, COMMENT_END
}

// ============ DEFAULT MODE (the "Sea" — raw template text) ============
EXPR_START : '{{' -> pushMode(JINJA_MODE) ;
STMT_START : '{%' -> pushMode(JINJA_MODE) ;
COMMENT_START : '{#' -> pushMode(COMMENT_MODE) ;
TEXT : ~[{]+ ;
TEXT_BRACE : '{' -> type(TEXT) ;
NEWLINE : '\r'? '\n' ;
WS : [ \t]+ -> skip ;

// ============ JINJA MODE (the "Islands" — inside Jinja tags) ============
mode JINJA_MODE;

JINJA_EXPR_END : '}}' -> type(CLOSE), popMode ;
JINJA_STMT_END : '%}' -> type(CLOSE), popMode ;
JINJA_WS : [ \t\r\n]+ -> skip ;

JINJA_IF : 'if' -> type(IF_KW) ;
JINJA_FOR : 'for' -> type(FOR_KW) ;
JINJA_ELIF : 'elif' -> type(ELIF_KW) ;
JINJA_ELSE : 'else' -> type(ELSE_KW) ;
JINJA_ENDIF : 'endif' -> type(ENDIF_KW) ;
JINJA_ENDFOR : 'endfor' -> type(ENDFOR_KW) ;
JINJA_SET : 'set' -> type(SET_KW) ;
JINJA_BLOCK : 'block' -> type(BLOCK_KW) ;
JINJA_ENDBLOCK : 'endblock' -> type(ENDBLOCK_KW) ;
JINJA_EXTENDS : 'extends' -> type(EXTENDS_KW) ;
JINJA_INCLUDE : 'include' -> type(INCLUDE_KW) ;
JINJA_WITH : 'with' -> type(WITH_KW) ;
JINJA_WITHOUT : 'without' -> type(WITHOUT_KW) ;
JINJA_CONTEXT : 'context' -> type(CONTEXT_KW) ;
JINJA_IGNORE : 'ignore' -> type(IGNORE_KW) ;
JINJA_MISSING : 'missing' -> type(MISSING_KW) ;
JINJA_AND : 'and' -> type(AND_KW) ;
JINJA_OR : 'or' -> type(OR_KW) ;
JINJA_NOT : 'not' -> type(NOT_KW) ;
JINJA_IN : 'in' -> type(IN_KW) ;
JINJA_IS : 'is' -> type(IS_KW) ;
JINJA_NONE : 'None' -> type(NONE_KW) ;
JINJA_TRUE : 'True' -> type(TRUE_KW) ;
JINJA_FALSE : 'False' -> type(FALSE_KW) ;
JINJA_ID : [a-zA-Z_][a-zA-Z_0-9]* -> type(ID) ;
JINJA_NUMBER : [0-9]+ ('.' [0-9]+)? -> type(NUMBER) ;
JINJA_STRING : '"' (~["\\] | '\\' .)* '"' -> type(STRING) ;
JINJA_STRING2 : '\'' (~['\\] | '\\' .)* '\'' -> type(STRING) ;
JINJA_PLUS : '+' -> type(PLUS) ;
JINJA_MINUS : '-' -> type(MINUS) ;
JINJA_MULT : '*' -> type(MULT) ;
JINJA_DIV : '/' -> type(DIV) ;
JINJA_MOD : '%' -> type(MOD) ;
JINJA_LT : '<' -> type(LT) ;
JINJA_GT : '>' -> type(GT) ;
JINJA_LTE : '<=' -> type(LTE) ;
JINJA_GTE : '>=' -> type(GTE) ;
JINJA_EQUAL : '==' -> type(EQUAL) ;
JINJA_NEQ : '!=' -> type(NEQ) ;
JINJA_ASSIGN : '=' -> type(ASSIGN) ;
JINJA_PIPE : '|' -> type(PIPE) ;
JINJA_LPAREN : '(' -> type(LPAREN) ;
JINJA_RPAREN : ')' -> type(RPAREN) ;
JINJA_LBRACK : '[' -> type(LBRACK) ;
JINJA_RBRACK : ']' -> type(RBRACK) ;
JINJA_COMMA : ',' -> type(COMMA) ;
JINJA_DOT : '.' -> type(DOT) ;

// ============ COMMENT MODE (the "Islands" — inside Jinja comments) ============
mode COMMENT_MODE;

COMMENT_HASH : '#' ;
COMMENT_CONTENT : (~[}#])+ ;
COMMENT_END : '#}' -> popMode ;