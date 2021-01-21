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
	'='
;

LESSTHAN
:
	'<'
;

variable
:
	'?' NAME
;

variableList
:
	variable
	(
		',' variable
	)*
;

constant
:
	NAME
;

predicateName
:
	NAME
;

term
:
	variable
	| constant
;

termList
:
	term
	(
		',' term
	)*
;

formula
:
	predicateName '(' termList ')' # Predicate
	| '(' formula ')' #ParenthesizedFormula
	| term EQUAL term # Equality
	| term LESSTHAN term # LessThan
	| NEGATION formula # Negation
	| formula CONJUNCTION formula # Conjunction
	| formula DISJUNCTION formula # Disjunction
	| formula IMPLICATION formula # Implication
	| UNIVERSAL variableList '(' formula ')' # UniversalQuantifier
	| EXISTENTIAL variableList '(' formula ')' # ExistentialQuantifier
;
