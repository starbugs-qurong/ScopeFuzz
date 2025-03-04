parser grammar CParser;
options { tokenVocab=CLexer; }

Body
    : memberContent  mainMethodDeclaration
    ;

memberContent
    : GlobalVariable  fieldDeclaration* fieldDeclaration* fieldDeclaration* memberDeclaration templateStructSample templateStruct* templateFuction* templateClass* methodDeclaration methodDeclaration* 
    ;

templateStructSample
	:|'template <int> void f1() {}'
	;
	
templateStruct
	:templateStructDeclaration structDeclaration
	;

templateFuction
	:templateFunDeclaration templateMethodDeclaration
	;
	
templateMethodDeclaration
	:methodDeclaration
	;

templateClass
	:templateClassDeclaration classDeclaration
	;
	
templateStructDeclaration
	: TEMPLATE  '<' templateStructParameters '>' 
	;
	
templateFunDeclaration
	: TEMPLATE  '<' templateFunParameters '>' 
	;
	
templateClassDeclaration
	: TEMPLATE  '<' templateClassParameter '>' 
	;
	
templateFunParameters
	: TYPENAME 'T' 
	;
	
templateStructParameters
	:templateClassParameter 
	;
	
templateClassParameter
	:TYPENAME 'T'
	;

/*templateAutoParameter
	:AUTO identifier
	;*/
	
structDeclaration
	: STRUCT identifier structBody
	;
	
structBody
	:'{' structMemberContent* '};'
	;
	
structMemberContent
	: fieldDeclaration* memberDeclaration* methodDeclaration*
	;
	
classDeclaration
	:CLASS identifier classBody
	;
	
classBody
    : '{' classMemberContent '};'
    ;

classMemberContent
    : fieldModifier ':' classMemberDeclaration+
    ;

classMemberDeclaration
    : fieldDeclaration* memberDeclaration* methodDeclaration*
    ;

fieldModifier
    : PUBLIC
    ;

memberDeclaration
	: arrayDeclaration 
    ;

methodDeclaration
    : typeTypeOrVoid identifier formalParameters methodBody
    ;

mainMethodDeclaration
    : INT MAIN  mainMethodBody
    ;

methodBody
    : block
    ;

mainMethodBody
    : mainMethodBlock
    ;

typeTypeOrVoid
    : typeType
    | VOID
    ;

fieldDeclaration
    : primitiveType identifier  ('=' expression) ';'
    ;

arrayDeclaration
	: INT identifier '[' arrayLength ']' ('=' '{' arrayExpression '}') ';'
    ;


formalParameters
    : '(' ( formalParameterList?) ')'
    ;

formalParameterList
    : formalParameter (',' formalParameter)* (',' lastFormalParameter)? 
    | lastFormalParameter
    ;

formalParameter
    :  primitiveType methodVarname
	|  INT identifier '[' ']'
	|  INT '*' identifier
    ;

lastFormalParameter
    :  primitiveType  methodVarname
	|  INT identifier '[' ']'
	|  INT '*' identifier
    ;

methodVarname
    : identifier
    ;

integerLiteral
    : integer
    ;

shortLiteral
    : shortInteger
    ;

longLiteral
    : longInteger
    ;


// STATEMENTS / BLOCKS

block
    : '{' ( localVariableDeclaration ';' )* ( localVariableDeclaration ';' )*  statement blockStatement '}'
    ;

loopBlock
    : '{' ( localVariableDeclaration ';' )* ( localVariableDeclaration ';' )*  statement blockStatement '}'
    ;

mainMethodBlock
    : '{'  mainContent '}'
    ;
mainContent
    : ' '
    ;

blockStatement
    : localVariableDeclaration ';'
    | statement
	| (updateExpression ';')*
    ;

localVariableDeclaration
    : variableDeclaration
	| INT identifier '[' arrayLength ']' ('=' '{' arrayExpression '}')
    ;
variableDeclaration	
    : primitiveType identifier ('=' expression)
	;

identifier
    : IDENTIFIER
    ;

statement
    : ifStatement
    | forStatement
    | whileStatement
    | (updateExpression ';')*
	| (updateExpression ';')*
    ;

whileStatement
    : WHILE '(' boolParExpression ')' loopBlock
    | DO loopBlock WHILE '(' boolParExpression ')' ';'
    ;

ifStatement
    : IF '(' boolExpression ')' block elseStatement?
    ;
	
elseStatement
	:ELSE block
	;

forStatement
    : FOR '(' forControl ')' loopBlock
    ;

updateExpression
    :   boolVarName  ('=' | '&=' | '|=' ) '(' boolExpression ')'
    //| floatVarName ('/=' ) floatDivisor 
    //| floatVarName ('=' | '+=' | '-=' | '*=' ) floatExpression 
    | integerVarName ('=' | '+=' | '-=' | '*=') integerExpression 
    | integerVarName '=' integerDivisor ('/' | '%') integerDivisor
    | integerVarName ( '^='  ) integerExpression 
//	    | integerVarName ( '^=' | '>>=' | '<<=' ) integerExpression 
    | longVarName '=' longDivisor ('/' | '%') longDivisor 
    | longVarName ('=' | '+=' | '-=' | '*=') longExpression 
    | longVarName ( '^=' ) longExpression 
//	| longVarName ( '^=' | '>>=' | '<<=' ) longExpression 
    | shortVarName '=' shortDivisor ('/' | '%') shortDivisor 
    | shortVarName ('=' | '+=' | '-=' | '*=' ) shortExpression 
    | shortVarName ( '^=' ) shortExpression 
//	| shortVarName ( '^=' | '>>=' | '<<=' ) shortExpression 
    ;

/** Matches cases then statements, both of which are mandatory.
 *  To handle empty cases at the end, we add switchLabel* to statement.
 */

forControl
    : forInit? ';' boolParExpression ';'  updateExpression?
    ;

forInit
    : variableDeclaration
    ;

// EXPRESSIONS


boolParExpression
    :  '(' integerVarName ')' ('<=' | '>=' | '>' | '<') '(' integerExpression ')' 
    |  '(' integerVarName ')' ('==' | '!=') '(' integerExpression ')' 
	//| '(' floatVarName ')' ('<=' | '>=' | '>' | '<') '(' floatExpression ')'  
    //|  '(' floatVarName ')' ('==' | '!=') '(' floatExpression ')' 
    |  '(' shortVarName ')' ('<=' | '>=' | '>' | '<') '(' shortExpression ')' 
    |  '(' shortVarName ')' ('==' | '!=') '(' shortExpression ')' 
    |  '(' longVarName ')' ('<=' | '>=' | '>' | '<') '(' longExpression ')' 
    |  '(' longVarName ')' ('==' | '!=') '(' longExpression ')' 
    |  '!'  '(' boolVarName ')'
    | '('  boolVarName ')' '&' '(' boolExpression ')'
    | '('  boolVarName ')' '|' '(' boolExpression ')'
    | '('  boolVarName ')' '&&' '(' boolExpression ')'
    | '('  boolVarName ')' OR  '(' boolExpression ')'
    |   boolVarName
    ;

boolExpression
    :   boolExpression 
    //|  '(' floatExpression ')' ('<=' | '>=' | '>' | '<') '(' floatExpression ')'  
    //|  '(' floatExpression ')' ('==' | '!=') '(' floatExpression ')' 
    |  '(' integerExpression ')' ('<=' | '>=' | '>' | '<') '(' integerExpression ')' 
    |  '(' integerExpression ')' ('==' | '!=') '(' integerExpression ')' 
    |  '(' shortExpression ')' ('<=' | '>=' | '>' | '<') '(' shortExpression ')' 
    |  '(' shortExpression ')' ('==' | '!=') '(' shortExpression ')' 
    |  '(' longExpression ')' ('<=' | '>=' | '>' | '<') '(' longExpression ')' 
    |  '(' longExpression ')' ('==' | '!=') '(' longExpression ')' 
    |  '!'  '(' boolExpression ')'
    | '('  boolExpression ')' '&' '(' boolExpression ')'
    | '('  boolExpression ')' '|' '(' boolExpression ')'
    | '('  boolExpression ')' '&&' '(' boolExpression ')'
    | '('  boolExpression ')' OR  '(' boolExpression ')'
    |   boolVarName 
    |   BOOL_LITERAL  
	| ( integerVarName | arrayElement) ( '==' | '<' | '>' | '>=' | '<=' ) ( integerExpression | arrayElement)
    ;

expression
    : integerExpression
	//floatExpression 
    | shortExpression
    | longExpression
    ;


/*floatExpression
    : '(' floatExpression ')'
    | floatVarName
    | floatLiteral
    //| classVarName '.' ( fieldVarname | methodCall)
    //| methodCall
    //| NEW creator
    //| floatVarName ('++' | '--')
	//| ('++'|'--') floatVarName
    | '(' ('+'|'-') floatVarName ')'
    | '(' floatVarName '/' '(' floatDivisor ')' ')'
    | '(' floatVarName ('*'|'+'|'-') '(' floatExpression ')' ')'
    | '(' '(' floatExpression ')'  '/' '(' floatDivisor ')' ')'
    | '(' '(' floatExpression ')'  ('*'|'+'|'-') '(' floatExpression ')' ')'
    //| varName '=' NEW creator 
    ;
	*/

integerExpression
    : '(' integerExpression ')'
    | integerVarName
    | integerLiteral
    //| integerVarName ('++' | '--')
	//| ('++'|'--') integerVarName
    | '(' ('+'|'-') integerVarName ')'
    //| '(' integerDivisor ('/'|'%')  '(' integerDivisor ')' ')'
    | '(' integerVarName ('*'|'+'|'-') '(' integerExpression ')' ')'
    //| '('  '(' integerDivisor ')'  ('/'|'%')  '(' integerDivisor ')' ')'
    | '('  '(' integerExpression ')'  ('*'|'+'|'-') '(' integerExpression ')' ')'
    //| '('  '(' shortDivisor ')'  ('/'|'%')  '(' shortDivisor ')' ')'
    | '('  '(' shortExpression ')'  ('*'|'+'|'-') '(' shortExpression ')' ')'
    //| '(' shortDivisor ('/'|'%')  '(' shortDivisor ')' ')'
    | '(' shortVarName ('*'|'+'|'-') '(' shortExpression ')' ')'
    | '(' ('+'|'-') shortVarName ')'
	| arrayElement
    ;


shortExpression
    : '(' shortExpression ')'
    | shortVarName
    | shortLiteral
    ;

	
arrayExpression
    : '(' arrayExpression ')'
    | arrayVarName
    ;

longExpression
    : '(' longExpression ')'
    | longVarName
    | longLiteral
    //| longVarName ('++' | '--')
	//| ('++'|'--') longVarName
    | '(' ('+'|'-') longVarName ')'
    | '(' longDivisor ('/'|'%')  '(' longDivisor ')' ')'
    | '(' longVarName ('*'|'+'|'-') '(' longExpression ')' ')'
    | '('  '(' longDivisor ')'  ('/'|'%')  '(' longDivisor ')' ')'
    | '('  '(' longExpression ')'  ('*'|'+'|'-') '(' longExpression ')' ')'
    ;

longDivisor
    : longVarName
    ;
shortDivisor
    : shortVarName
    ;

integerDivisor
    : integerVarName 
    ;
/*floatDivisor
    : floatVarName 
    ;
	*/
	
/*arrayDivisor
    : arrayVarName 
    ;
	*/

boolVarName
    : identifier
    ;

integerVarName
    : identifier
    ;

shortVarName
    : identifier
    ;

longVarName
    : identifier
    ;

arrayVarName
    : identifier
    ;


typeType
    : primitiveType
	| otherType
    ;

primitiveType
    : BOOL
    | SHORT
    | INT
    | LONG
    //| DOUBLE
    ;
	
otherType
	:IntArray
	;

IntArray:'int[]';

arrayLength:[1-9][0-9];

//integer: [1-9][0-9][0-9][0-9];
integer: [0-9];


//shortInteger: [1-9][0-9][0-9];
shortInteger: [0-9];


//longInteger: [1-9][0-9][0-9][0-9][0-9][0-9][0-9];
longInteger: [0-9];

/*double: integer '.' [0-9]([0-9])?
  |'0.' [0-9]([0-9])?;
  */
  
GlobalVariable: 'GlobalVariable';

