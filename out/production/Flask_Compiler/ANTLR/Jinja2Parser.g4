parser grammar Jinja2Parser;

@header {package gen.ANTLR;}

options { tokenVocab = Jinja2Lexer; }

// Entry point
template
    : ( textChunk | expression | statement | comment )* EOF
    ;

textChunk
    : TEXT // lone '{' is already converted to TEXT by the lexer
    ;

// {{ expr }}
expression
    : EXPR_START expr CLOSE
    ;

// {% stmt %}
statement
    : STMT_START stmtContent
    ;

stmtContent
    : ifStmt #IfContent
    | forStmt #ForContent
    | setStmt #SetContent
    | blockStmt #BlockContent
    | extendsStmt #ExtendsContent
    | includeStmt #IncludeContent
    | withStmt #WithContent
    ;

// {# ... #}
comment
    : COMMENT_START COMMENT_END #NoBodyComment
    | COMMENT_START commentBody COMMENT_END #BodyComment
    ;

commentBody
    : COMMENT_CONTENT #CommentText
    | COMMENT_HASH #CommentHash
    ;

// --- Statements ---

ifStmt
    : IF_KW expr CLOSE bodyContent* elifClause* elseClause? STMT_START ENDIF_KW CLOSE
    ;

elifClause
    : STMT_START ELIF_KW expr CLOSE bodyContent*
    ;

elseClause
    : STMT_START ELSE_KW CLOSE bodyContent*
    ;

forStmt
    : FOR_KW forInit CLOSE bodyContent* STMT_START ENDFOR_KW CLOSE
    ;

forInit
    : expr ( IN_KW expr )?
    ;

idList
    : ID ( COMMA ID )*
    ;

setStmt
    : SET_KW expr CLOSE
    ;

blockStmt
    : BLOCK_KW expr CLOSE bodyContent* STMT_START ENDBLOCK_KW CLOSE
    ;

extendsStmt
    : EXTENDS_KW expr CLOSE
    ;

includeStmt
    : INCLUDE_KW expr CLOSE
    ;

withStmt
    : WITH_KW expr CLOSE bodyContent*
    ;

bodyContent
    : textChunk #TextContent
    | expression #ExprContent
    | comment #CommentContent
    | STMT_START ifStmt #IfEmbedded
    | STMT_START forStmt #ForEmbedded
    | STMT_START setStmt #SetEmbedded
    | STMT_START blockStmt #BlockEmbedded
    | STMT_START extendsStmt #ExtendsEmbedded
    | STMT_START includeStmt #IncludeEmbedded
    | STMT_START withStmt #WithEmbedded
    ;

// --- Expressions (simplified, precedence-correct) ---

expr
    : orExpr
    ;

orExpr
    : andExpr ( OR_KW andExpr )*
    ;

andExpr
    : notExpr ( AND_KW notExpr )*
    ;

notExpr
    : NOT_KW notExpr #NotExpression
    | comparison #ComparisonExpr
    ;

comparison
    : concat ( compOp concat )?
    ;

compOp
    : LT #LessThan
    | GT #GreaterThan
    | LTE #LessEqual
    | GTE #GreaterEqual
    | EQUAL #Equal
    | NEQ #NotEqual
    | IN_KW (NOT_KW)? #InOp
    | IS_KW (NOT_KW)? #IsOp
    ;

concat
    : pipeExpr
    ;

pipeExpr
    : term ( PIPE ID ( LPAREN argList? RPAREN )? )*
    ;

term
    : factor ( ( PLUS | MINUS ) factor )*
    ;

factor
    : unary ( ( MULT | DIV | MOD ) unary )*
    ;

unary
    : ( PLUS | MINUS | NOT_KW ) unary #UnaryOp
    | atom #AtomUnary
    ;

atom
    : literal #LiteralAtom
    | ID #IdAtom
    | LPAREN expr RPAREN #ParenAtom
    | LBRACK argList? RBRACK #ListAtom
    | atom DOT ID #FieldAccess
    | atom LPAREN argList? RPAREN #CallAtom
    | atom LBRACK expr RBRACK #IndexAtom
    ;

literal
    : NUMBER #NumberLit
    | STRING #StringLit
    | TRUE_KW #TrueLit
    | FALSE_KW #FalseLit
    | NONE_KW #NoneLit
    ;

argList
    : expr ( COMMA expr )*
    ;
