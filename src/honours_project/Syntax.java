/*
grammar RC;

DIGIT = "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
LETTER = "a" | ... | "z" | "A" | ... | "Z"
LEFTPAREN = '('
RIGHTPAREN = ')'
LEFTBRACE = '{'
RIGHTBRACE = '}'

EQUAL = "=" | "=="
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

number = DIGIT, {DIGIT}

string = '"' (<any> - '"') '"'
       | "'" (<any> - "'") "'"
       
name = (UNDERSCORE | LETTER), {UNDERSCORE, LETTER, DIGIT}
scalar = number | string
tuple-lit = LEFTPAREN {name, ASSIGN, expression} RIGHTPAREN
relation-lit = LEFTBRACE {tuple-lit} RIGHTBRACE
relation = relation-lit
         | LEFTBRACE query RIGHTBRACE
         | variable
         
expression = query 
           | variable
           | formula
           | scalar
           | LEFTPAREN expression RIGHTPAREN
           | expression; {expression;}
           | expression; {expression;} expression
           | relation
           | if-then-else
           
variable = name

query = projection PIPE formula

assignment = variable ASSIGN expression

projection = LEFTPAREN {name} RIGHTPAREN

logical-operator = EQUAL | LESSTHAN | LESSTHANOREQUAL | GREATERTHAN | GREATERTHANOREQUAL | NOTEQUAL

boolean-expr = TRUE | FALSE

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
        
if-then-else = 'if' boolean-expr 'then' expression
              {'elseif' formula 'then' expression}
              ['else' expression]

*/