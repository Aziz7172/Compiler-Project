lexer grammar PythonLexer;

@header {package gen.ANTLR;}

@lexer::members {
    private void processIndent() {
        setType(NEWLINE);
    }
}

// --- Template Delimiters ---
EXPR_START : '{{' ;
EXPR_END   : '}}' ;
STMT_START : '{%' ;
STMT_END   : '%}' ;
COMMENT_START : '{#' ;
COMMENT_END   : '#}' ;

// ------------------- Keywords -------------------
IMPORT     : 'import';
FROM       : 'from';
TRUE       : 'True';
FALSE      : 'False';
NONE       : 'None';
IF         : 'if';
ELIF       : 'elif';
ELSE       : 'else';
FOR        : 'for';
WHILE      : 'while';
DEF        : 'def';
RETURN     : 'return';
PRINT      : 'print';
CLASS      : 'class';
SELF       : 'self';
BREAK      : 'break';
CONTINUE   : 'continue';
PASS       : 'pass';
TRY        : 'try';
EXCEPT     : 'except';
FINALLY    : 'finally';
S_AND      : 'and';
S_OR       : 'or';
NOT        : 'not';
IN         : 'in';
IS         : 'is';
AS         : 'as';
SET        : 'set';
PIPE       : '|';
GLOBAL     : 'global';
// ------------------- JINJA2 -------------------
INCLUDE    : 'include';
EXTENDS    : 'extends';
ENDIF      : 'endif';
ENDFOR     : 'endfor';
IGNORE     : 'ignore';
MISSING    : 'missing';
WITH       : 'with';
ENDWITH    : 'endwith';
WITHOUT    : 'without';
CONTEXT    : 'context';
BLOCK      : 'block';
ENDBLOCK   : 'endblock';

// ------------------- Operators -------------------
PLUS       : '+';
MINUS      : '-';
MULTIPLY   : '*';
SLASH      : '/';
MOD        : '%';
LT         : '<';
GT         : '>';
LTE        : '<=';
GTE        : '>=';
EQUAL      : '==';
NEQ        : '!=';
STRICT_EQ  : '===';
STRICT_NEQ : '!==';
ASSIGN     : '=';
AND        : '&&';
OR         : '||';



// ------------------- Symbols -------------------
LPAREN     : '(';
RPAREN     : ')';
LBRACK     : '[';
RBRACK     : ']';
LCBRACK    : '{';
RCBRACK    : '}';
COLON      : ':';
SEMI       : ';';
COMMA      : ',';
DOT        : '.';
HASHTAG_VALUE : '#' [a-fA-F0-9]+ ;
HASHTAG    : '#';
AT         : '@';
BANG      : '!';
AMPERSAND : '&';
DOLLAR    : '$';

HTML_DOCTYPE : '<!DOCTYPE' [ \t]+ [a-zA-Z_] [a-zA-Z_0-9]* [ \t]* '>' ;
CSS_COM_S  : '/*';
CSS_COM_E  : '*/';

// ----------------- HTML & CSS -------------------
STYLE : 'style';
TYPE
    : ('px' | 'em' | 'rem' | '%' | 'vh' | 'vw' | 'deg' | 's' | 'ms')
    ;

// ------------------- Literals -------------------
NUMBER
    : ('+'|'-')? [0-9]+ ('.' [0-9]+)?
    ;

STRING
    : '"' (~["\\] | '\\' .)* '"'
    | '\'' (~['\\] | '\\' .)* '\''
    ;

// ------------------- Identifiers -------------------

IDENTIFIER
    : [a-zA-Z_][a-zA-Z_0-9]*
    ;

// ------------------- Indentation Tokens (Stack-based, @lexer::members) -------------------
NEWLINE
    :   '\r'? '\n' [ \t]*
        {
            processIndent();
        }
    ;

WS : [ \t]+ -> skip ;

COMMENT
    : '#' ~[\r\n]* -> skip
    ;
