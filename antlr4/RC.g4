grammar RC;

fragment
DIGIT
:
	[0-9]
;

fragment
LETTER
:
	[A-Za-z]
;

fragment
LOWER_CASE_LETTER
:
	[a-z]
;

NAME
:
	LETTER+
	(
		LETTER
		| DIGIT
		| '_'
	)*
;

CONSTANT
:
	'\'' .+? '\''
;

WHITESPACE
:
	[ \t\r\n]+ -> skip
;

PREDICATE
:
	NAME
;

NEGATION
:
	'~'
;

CONJUNCTION
:
	'&'
;

DISJUNCTION
:
	'|'
;

IMPLICATION
:
	'->'
;

UNIVERSAL
:
	'[A]'
;

EXISTENTIAL
:
	'[E]'
;

EQUAL
:
	'=='
;

LESSTHAN
:
	'<'
;

variable
:
	LOWER_CASE_LETTER
;

variable_list
:
	variable
	(
		',' variable
	)*
;


constant
:
	CONSTANT
;

term
:
	variable
	| constant
;


comparison
:
	| term EQUAL term # Equality
	| term LESSTHAN term # LessThan
;

formula
:
	| PREDICATE '(' variable_list ')' # Predicate
	| comparison # Comparison
	| NEGATION formula # Negation
	| formula CONJUNCTION formula # Conjunction
	| formula DISJUNCTION formula # Disjunction
	| formula IMPLICATION formula # Implication
	| UNIVERSAL '[' variable_list ']' '(' formula ')' # UniversalQuantifier
	| EXISTENTIAL '[' variable_list ']' '(' formula ')' # ExistentialQuantifier
;
