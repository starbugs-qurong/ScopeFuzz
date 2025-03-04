lexer grammar CLexer;
BOOL:            'int';
DO:                 'do';
ELSE:               'else';
FOR:                'for';
IF:                 'if';
INT:                'int';
LONG:               'long';
SHORT:              'short';
VOID:               'void';
INT:               'int';
WHILE:              'while';
MAIN:             'main()';

BOOL_LITERAL
	:'1'
	|'0'
	;

TYPENAME
	:'typename'
	;


TEMPLATE
	:'template'
	;
	
STRUCT
	:'struct'
	;
	
CLASS
	:'class'
	;
	
PUBLIC
	:'public'
	;
OR
    : '||'
    ;
	
/*AUTO
	:'auto'
	;*/

// Identifiers

IDENTIFIER:         Letter LetterOrDigit LetterOrDigit LetterOrDigit LetterOrDigit LetterOrDigit LetterOrDigit LetterOrDigit*;
//IDENTIFIER:         Letter Letter Digit;

fragment LetterOrDigit
    : Letter
    | Digit
    ;
	
Digit
	:[0-9]
	;
	

fragment Letter
    : [a-zA-Z] // these are the "java letters" below 0x7F
    ;
