parser grammar PythonParser;

@header {package gen.ANTLR;}

options { tokenVocab = PythonLexer; }

// ------------------- Program -------------------
program
    : statementBlock NEWLINE* EOF
    ;

statementBlock
    : statement (NEWLINE+ statement)*
    ;
// ------------------- Statements -------------------
statement
    : simpleStatement (SEMI simpleStatement)* #SimpleSeqStmt
    | compoundStatement #CompoundStmt
    | css #CssStmt
    | html #HtmlStmt
    | jinjaBody #JinjaStmt
    ;

simpleStatement
    : pythonImport #ImportStmt
    | assignment #AssignStmt
    | printStatement #PrintStmt
    | returnStatement #ReturnStmt
    | value #ValueStmt
    | globalStatement #GlobalStmt
    | passStatement #PassStmt
    ;

passStatement
    : PASS
    ;

compoundStatement
    : ifStatement #IfCompound
    | forLoop #ForCompound
    | whileLoop #WhileCompound
    | function #FuncDef
    | classDef #ClassDefStmt
    | expressions #ExprCompound
    ;

// ------------------- Assignments & Values -------------------
assignment
    : IDENTIFIER ASSIGN value
    ;

value
    : atom #AtomVal
    | expressions #ExprVal
    | list #ListVal
    | tuple #TupleVal
    | json #JsonVal
    | listComprehension #ListCompVal
    ;

list
    : LBRACK NEWLINE* elements NEWLINE* RBRACK
    ;

listComprehension
    : LBRACK atom FOR IDENTIFIER IN value (IF expressions)? RBRACK
    ;

tuple
    : LPAREN NEWLINE* elements NEWLINE* RPAREN
    ;

elements
    : value? (COMMA NEWLINE* value)*
    ;

json
    : LCBRACK NEWLINE* (jsonData (COMMA NEWLINE* jsonData)*)? NEWLINE* RCBRACK
    ;

jsonData
    :  STRING COLON value
    ;

// ------------------- Expressions -------------------
expressions
    : logicalExpressions #LogicalExp
    | comparisonExpressions #ComparisonExp
    | mathematicalExpressions #MathExp
    ;

comparisonExpressions
    : left=mathematicalExpressions LT right=mathematicalExpressions            #LtExp
    | left=mathematicalExpressions GT right=mathematicalExpressions            #GtExp
    | left=mathematicalExpressions LTE right=mathematicalExpressions           #LteExp
    | left=mathematicalExpressions GTE right=mathematicalExpressions           #GteExp
    | left=mathematicalExpressions EQUAL right=mathematicalExpressions         #EqExp
    | left=mathematicalExpressions STRICT_EQ right=mathematicalExpressions     #StrictEqExp
    | left=mathematicalExpressions NEQ right=mathematicalExpressions           #NeExp
    | left=mathematicalExpressions STRICT_NEQ right=mathematicalExpressions    #StrictNeqExp
    | left=mathematicalExpressions IS NOT? right=mathematicalExpressions       #IdComparison
    | left=mathematicalExpressions IN right=mathematicalExpressions            #MembershipTest
    ;

mathematicalExpressions
    : left=mathematicalExpressions PLUS right=mathematicalExpressions          #AddExp
    | left=mathematicalExpressions MINUS right=mathematicalExpressions         #SubExp
    | left=mathematicalExpressions MULTIPLY right=mathematicalExpressions      #MulExp
    | left=mathematicalExpressions SLASH right=mathematicalExpressions         #DivExp
    | left=mathematicalExpressions MOD right=mathematicalExpressions           #ModExp
    | valuesExp                                                                #ValueExp
    ;

logicalExpressions
    : left=logicalExpressions (AND | S_AND) right=logicalExpressions           #AndExp
    | left=logicalExpressions (OR  | S_OR) right=logicalExpressions            #OrExp
    | NOT item=logicalExpressions                                              #NotExp
    | comparisonExpressions                                                    #CompAsLogical
    | valuesExp                                                                #ValuesAsLogical
    ;

valuesExp
    : atom
    ;

atom
    : primaryAtom postfix*   #AtomWithAccess
    ;

primaryAtom
    : IDENTIFIER                 #Id
    | NUMBER                     #Number
    | STRING                     #String
//    | LPAREN expressions RPAREN  #Paren
    | TRUE                       #True
    | FALSE                      #False
    ;

postfix
    : DOT (IDENTIFIER | functionCall)     #DotAccess
    | LBRACK (expressions | atom) RBRACK  #IndexAccess
    | LPAREN argument* RPAREN             #FuncCallPostfix
    ;

globalStatement
    : GLOBAL IDENTIFIER (COMMA IDENTIFIER)*
    ;
// ------------------- Import Statement -------------------

pythonImport
    : importSyntax #ImportSyntaxStmt
    | fromImport #FromImportStmt
    ;

importSyntax
    : IMPORT IDENTIFIER (AS IDENTIFIER)? #SimpleImport
    | IMPORT STRING (AS IDENTIFIER)?     #StringImport
    ;

fromImport
    : FROM IDENTIFIER IMPORT importedNames #IdFromImportStmt
    | FROM STRING IMPORT importedNames     #StrFromImportStmt
    ;

importedNames
    : importsAliases (COMMA importsAliases)*
    ;

importsAliases
    : IDENTIFIER (AS IDENTIFIER)?
    ;


// ------------------- Print Statement -------------------
printStatement
    : PRINT LPAREN printArgs? RPAREN
    ;

printArgs
    : value (COMMA value)* #ValuePrintArgs
    | expressions (COMMA? expressions)* #ExprPrintArgs
    ;

// ------------------- Block -------------------
block
    : LCBRACK NEWLINE* statementBlock? NEWLINE* RCBRACK
    ;

// ------------------- If / Conditional Statements -------------------
ifStatement
    : ifBlock (elifBlock)* (elseBlock)?
    ;

ifBlock
    : IF LPAREN? condition=logicalExpressions RPAREN? block
    ;

elifBlock
    : ELIF LPAREN? condition=logicalExpressions RPAREN? block
    ;

elseBlock
    : ELSE block
    ;

// ------------------- Loops -------------------
forLoop
    : FOR IDENTIFIER IN iterable=value block;

whileLoop
    : WHILE LPAREN expressions RPAREN block;

// ------------------- Function -------------------
function
    : decorator_rule* DEF IDENTIFIER LPAREN parameters? RPAREN block;

parameters
    : parameter (COMMA parameter)*;

parameter
    : IDENTIFIER (ASSIGN value)?;

returnStatement
    : RETURN value?
    ;

functionCall
    : IDENTIFIER LPAREN argument* RPAREN
    ;

argument
    : (IDENTIFIER ASSIGN)? value COMMA?
    ;

decorator_rule
    : AT atom NEWLINE+  #Decorator
    ;

// ------------------- CLASS -------------------
classDef
    : decorator_rule* CLASS IDENTIFIER baseClass? block
    ;

baseClass: (LPAREN IDENTIFIER RPAREN);

// ------------------- JINJA2 -------------------
jinjaBody
    : jinjaExpression #JinjaExprBody
    | jinjaStatement #JinjaStmtBody
    | jinjaComment #JinjaCommentBody
    ;

jinjaExpression
    : EXPR_START (atom | expressions | value) jinjaFilter* EXPR_END
    ;

jinjaFilter
    : PIPE (IDENTIFIER | functionCall)
    ;

jinjaComment
    : COMMENT_START COMMENT_END #EmptyComment
    | COMMENT_START commentBody COMMENT_END #CommentWithBody
    ;

commentBody
    : COMMENT_CONTENT #CcContent
    | COMMENT_HASH #CcHash
    ;

jinjaStatement
    : STMT_START jinjaStatementContent
    ;

jinjaStatementContent
    : jinjaIfStatements #JinjaIfContent
    | jinjaFor #JinjaForContent
    | jinjaSet #JinjaSetContent
    | jiniaExtends #JinjaExtendsContent
    | jinjaInclude #JinjaIncludeContent
    | jinjaBlock #JinjaBlockContent
    | jinjaLocalVariable #JinjaLocalVarContent
    | importSyntax STMT_END #JinjaImportContent
    | fromImport STMT_END #JinjaFromImportContent
    | PRINT expressions #JinjaPrintContent
    ;

jinjaIfStatements
    : jinjaIf (jinjaElif)* (jinjaElse)? STMT_START ENDIF STMT_END;

jinjaIf
    : IF condition=expressions STMT_END templateBody
    ;

jinjaElif
    : STMT_START ELIF condition=expressions STMT_END templateBody
    ;

jinjaElse
    : STMT_START ELSE STMT_END templateBody
    ;

jinjaFor
    : FOR IDENTIFIER IN value STMT_END templateBody STMT_START ENDFOR STMT_END
    ;

jinjaSet
    : SET IDENTIFIER ASSIGN expressions STMT_END
    ;

jiniaExtends
    : EXTENDS atom STMT_END;

jinjaInclude
    : INCLUDE atom
      (IGNORE MISSING)?
      ((WITH | WITHOUT) CONTEXT)?
      STMT_END
    ;


jinjaBlock
    : BLOCK IDENTIFIER STMT_END templateBody STMT_START ENDBLOCK (IDENTIFIER)? STMT_END
    ;

jinjaLocalVariable
    : WITH IDENTIFIER ASSIGN expressions STMT_END templateBody STMT_START ENDWITH STMT_END
    ;

// --- Template Body ---
templateBody
    : (html | jinjaBody | statement)*
    ;

// ------------------- FLASK -------------------
//flask
//    : pythonImport
//    ;

// ------------------- HTML -------------------
html
    : htmlDoctype
    | htmlElement+
    ;

htmlElement
    : htmlTag #HtmlTagElem
    | selfClosingTag #SelfClosingElem
    | htmlText #HtmlTextElem
    ;

htmlDoctype
    : HTML_DOCTYPE
    ;

htmlTag
    : styleTag #StyleHtmlTag
    | genericHtml #GenericHtmlTag
    ;

styleTag
    : LT STYLE htmlAttributes* GT css* LT SLASH STYLE GT
    ;

genericHtml
    : LT IDENTIFIER htmlAttributes* GT htmlBody? LT SLASH IDENTIFIER GT
    ;

selfClosingTag
    : LT IDENTIFIER htmlAttributes* SLASH GT
    ;

htmlAttributes
    : attributeName ASSIGN attributeValue
    ;

attributeName
    : IDENTIFIER #AttrNameId
    | CLASS #AttrNameClass
    ;

attributeValue
    : STRING
    ;

htmlBody
    : (htmlElement
    | htmlText
    | jinjaBody
    )+
    ;

htmlText
    : (IDENTIFIER
    | STRING
    | jinjaExpression
    | BANG
    | AMPERSAND
    | DOLLAR
    | HASHTAG_VALUE
    | HASHTAG
    | SEMI
    )+
    ;

// ------------------- CSS -------------------
css
    : cssSelector (COMMA cssSelector)* LCBRACK (cssKeyValue)* RCBRACK #CssBlock
    | cssComment #CssAnnotation
    ;

cssSelector
    : (DOT | HASHTAG)? cssKey (COLON cssKey)*
    ;

cssKeyValue
    : cssKey COLON cssValue SEMI?
    ;

cssKey
    : IDENTIFIER (MINUS IDENTIFIER?)*
    ;

cssValue
    : NUMBER (TYPE)?                    #cssVNumber
    | IDENTIFIER                        #cssVId
    | HASHTAG_VALUE                     #cssVColor
    | STRING                            #cssVStr
    | jinjaExpression                   #cssVJinja
    ;

cssComment
    : CSS_COM_S (.)? CSS_COM_E
    ;