grammar Tile;

/*  + Remove method for lists
 *  + Boolean Algebra
 *  + Catch size(arr) when arr does not exist
 *  Move Structs to functions via blocks
 */

parse
 : block EOF
 ;

block
 : importStatmemt* (statement | functionDecl | structDecl)* (Return expression? ';')? functionDecl*
 ;

statement
 : assignment ';'
 | functionCall ';'
 | ifStatement
 | forStatement
 | whileStatement
 | functionStatment
 | runStatment
 | breakStatement
 | continueStatement
 ;

functionStatment
 : expression '.' listFunctions ';'        #listFunctionStatment
 ;

importStatmemt
 : Import expression ';'
 ;

runStatment
 : Run '(' expression ')' ';'
 ;

assignment
 : Identifier indexes? '=' expression        #regularAssignment
 | Identifier indexes? SingularOp            #singularAssignment
 | Identifier indexes? MultiOp expression    #fullAssignment
 | Identifier '.' Identifier '=' expression  #structAssignment
 ;

functionCall
 : Identifier '(' exprList? ')' #identifierFunctionCall
 //| expression Identifier expression #sequentialFunctionCall
 //| Println '(' expression? ')'  #printlnFunctionCall
 | Assert '(' expression ')'    #assertFunctionCall
 ;

ifStatement
 : ifStat elseIfStat* elseStat? End
 ;

ifStat
 : If '(' expression ')' Start block
 ;

elseIfStat
 : End Else If '(' expression ')' Start block
 ;

elseStat
 : End Else Start block
 ;

functionDecl
 : Def Identifier '(' idList? ')' Start block End
 ;

structDecl
 : Struct Identifier Start idList? End
 ;

/*forStatement
 : For Identifier '=' expression To expression Do block End
 ;*/

forStatement
 : For '(' Identifier '=' ( (expression To expression) | (expression To expression To expression) ) ')' Start block End   #forStepStatment
 | For '(' Identifier '=' expression SColon expression SColon expression ')' Start block End                              #forSTDStatment
 | For '(' Identifier To expression ')' Start block End                                                                   #forEachStatment
 ;

/*whileStatement
 : While expression Do block End
 ;*/

whileStatement
 : While '(' expression ')' Start block End
 ;

breakStatement
 : Break ';'
 ;

continueStatement
 : Continue ';'
 ;
idList
 : type? Identifier (',' type? Identifier)*
 ;

exprList
 : expression (',' expression)*
 ;

expression
 : '(' expression ')' indexes?              #expressionExpression
 | '!' expression                           #notExpression
 | '-' expression                           #unaryMinusExpression
 | expression '^' expression                #powerExpression
 | expression mulDivMod expression          #multiplyDivideModExpression
 | expression addSub expression             #addSubExpression
 | expression '>=' expression               #gtEqExpression
 | expression '<=' expression               #ltEqExpression
 | expression '>' expression                #gtExpression
 | expression '<' expression                #ltExpression
 | expression '==' expression               #eqExpression
 | expression '!=' expression               #notEqExpression
 | expression '&&' expression               #andExpression
 | expression '||' expression               #orExpression

 | Identifier '.' Identifier                #accessStructExpression
 //| expression '?' expression ':' expression #ternaryExpression
 | assignment                               #assignmentExpression
 | expression In expression                 #inExpression
 | Number                                   #numberExpression
 | Bool                                     #boolExpression
 | NULL                                     #nullExpression
 | functionCall indexes?                    #functionCallExpression
 | list indexes?                            #listExpression
 | Identifier indexes?                      #identifierExpression
 | String indexes?                          #stringExpression
 | '(' expression ')' indexes?              #expressionExpression
 //| expression '.' stringFunctions           #stringFunctionExpression
 | '@' Identifier                           #functionRefExpression
 | expression ISA type                      #isaTypeExpression
 ;


/*stringFunctions
 : Substring '(' expression (',' expression)? ')'
 | StrLength '(' expression? ')'
 | IndexOf '(' expression (',' expression)? ')'
 | ToNum '(' ')'
 ;*/

listFunctions
 : Remove '(' expression ')'
 | Clear '(' ')'
 ;

list
 : '[' exprList? ']'
 ;

indexes
 : ('[' expression ']')+
 ;
//STRING, NUMBER, LIST, NULL, STRUCT, ANY, STRING_NUMBER, BOOL, FUNCTION, VOID
type
 : STRING
 | NUMBER
 | LIST
 | STRUCT
 | BOOL
 | FUNCTION
 | NULL
 | VOID
 ;

STRING   : 'string';
NUMBER   : 'number';
LIST     : 'list';
STRUCT   : 'struct';
BOOL     : 'bool';
FUNCTION : 'funct';
NULL : 'null';
VOID : 'void';

ISA      : 'isa';

//Sub      : '-';

addSub   : ('+' | '-');
mulDivMod: ('*' | '/' | '%');

/*/Sub      : '-';*/
//Println  : 'println';
Assert   : 'assert';
Def      : 'function';
Struct   : 'struct';
If       : 'if';
Else     : 'else';
Return   : 'return';
Run      : 'run';
Import   : 'import';
Break    : 'break';
Continue : 'continue';
For      : 'for';
While    : 'while';
To       : 'to' | ':';
Do       : 'do';
Start    : OBrace;
End      : CBrace;
In       : 'in';

//Substring: 'substring';
//StrLength: 'length';
//IndexOf  : 'indexOf';
//ToNum    : 'toNumber';

Remove   : 'remove';
Clear    : 'clear';

SingularOp: '++' | '--';
MultiOp  : '+=' | '-=' | '*=' | '/=';

Or       : '||';
And      : '&&';
Equals   : '==';
NEquals  : '!=';
GTEquals : '>=';
LTEquals : '<=';
Pow      : '^';
Excl     : '!';
GT       : '>';
LT       : '<';
Add      : '+';
Subtract : '-';
Multiply : '*';
Divide   : '/';
Modulus  : '%';
OBrace   : '{';
CBrace   : '}';
OBracket : '[';
CBracket : ']';
OParen   : '(';
CParen   : ')';
SColon   : ';';
Assign   : '=';
Comma    : ',';
QMark    : '?';
Colon    : ':';

Bool
 : 'true'
 | 'false'
 ;

Number
 //: Int ('.' Digit*)? (('-')? ('E'|'e') Int)?
 : ((Digit+('.'Digit+)?)|(('.'Digit+)))(('e'|'E')'-'?(Digit+))?
 ;

Identifier
 : [a-zA-Z_] [a-zA-Z_0-9]*
 ;

String
 : ["] (~["\r\n] | '\\\\' | '\\"')* ["]
 | ['] (~['\r\n] | '\\\\' | '\\\'')* [']
 ;

Comment
 : ('//' ~[\r\n]* | '/*' .*? '*/') -> skip
 ;

Space
 : [ \t\r\n\u000C] -> skip
 ;

fragment Int
 : [1-9] Digit*
 | '0'
 ;

fragment Digit
 : [0-9]
 ;
