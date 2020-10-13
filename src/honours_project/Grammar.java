package honours_project;

/*
grammar RC;

DIGIT = "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
LETTER = "a" | ... | "z" | "A" | ... | "Z"
LEFTPAREN = '('
RIGHTPAREN = ')'
LEFTBRACE = '{'
RIGHTBRACE = '}'

EQUAL = "=" 
ASSIGN = ":="
LESSTHAN = "<"
LESSTHANOREQUAL = "<="
GREATERTHAN = ">"
GREATERTHANOREQUAL = ">="
NOTEQUAL = "!="
TRUE = "true"
FALSE = "false"

PIPE = '|'
UNDERSCORE = "_"
WHITESPACE = " " | "\n" | "\r\n" | "\t"

name = (UNDERSCORE | LETTER), {UNDERSCORE, LETTER, DIGIT}
number = DIGIT, {DIGIT}

string = '"' (<any> - '"') '"'
       | "'" (<any> - "'") "'"
       
expression = query 
           | variable
           | formula
           | number
           | string
           | LEFTPAREN expression RIGHTPAREN
           | expression; {expression;}
           | expression; {expression;} expression
           | relation
           | if-then-else

formula = TRUE | FALSE
        	| LEFTPAREN formula RIGHTPAREN
        | variable
        | expression logical-operator expression
        | formula 'and' formula
        | formula 'or' formula
        | 'not' formula
        | variable 'in' relation
        | exists name {, name} LEFTPAREN formula RIGHTPAREN
        | forall name {, name} LEFTPAREN formula RIGHTPAREN
           
variable = name

query = projection PIPE formula
       
tuple-lit = LEFTPAREN {name, ASSIGN, expression} RIGHTPAREN
relation-lit = LEFTBRACE {tuple-lit} RIGHTBRACE
relation = relation-lit
         | LEFTBRACE query RIGHTBRACE
         | variable
  
assignment = variable ASSIGN expression

projection = LEFTPAREN {name} RIGHTPAREN

logical-operator = EQUAL | LESSTHAN | LESSTHANOREQUAL | GREATERTHAN | GREATERTHANOREQUAL | NOTEQUAL

boolean-expr = TRUE | FALSE
        
if-then-else = 'if' boolean-expr 'then' expression
              {'elseif' formula 'then' expression}
              ['else' expression]

*/