//header {
//package parsingtests;
//}

class SeqParser extends Parser;
options {
  buildAST = true;  // uses CommonAST by default
  k = 3;
  defaultErrorHandler=false;
}

tokens {
    VOIDLIST;
    RULEALONE;
}

seq: SEQ list_pred  
   | list_pred SEQ list_pred 
   ;

list_pred: pred (LIST^ pred)* ;

pred: andpred (IMPL^ andpred)* ;

andpred: orpred (AND^ orpred)* ;

orpred: quantif (OR^ quantif)* ;

quantif: FORALL^ ID DOT! negpred
       | EXISTS^ ID DOT! negpred
       | negpred
       ;

negpred: NOT^ simplepred
       | simplepred
       ;
       
simplepred: LPAREN! pred RPAREN!
          | atom
          ;
atom: appl
    | ID
    | BOTTOM
    | TOP
    ;

appl : ID LPAREN^ term_list  RPAREN!
     ;

term_list: term (LIST^ term)* 
         | { #term_list = #[VOIDLIST,"VOIDLIST"]; }
         ;

term : minterm (PLUS^ minterm)*
     ;

minterm: mterm (MINUS^ mterm)*
       ;

mterm : divterm (TIMES^ divterm)*
      ;

divterm: fterm (DIV^ fterm)*
       ;
      
fterm : LPAREN! term RPAREN!
      | funappl
      | ID
      | NUMBER
      ;

funappl : ID LPAREN^ term_list RPAREN!;


// points d'entree pour les programmes

command: PROOF^ ID COLUMN! seq DOT!
       | RRULE^ atom ARROW! pred DOT!
       | TRULE^ term ARROW! term DOT!
       | DISPLAY^ ID DOT!
       | QUIT DOT!
       | PROOFCHECK^ ID DOT!
       | PRINT^ ID DOT!
       ;

proofcommand: FOCUS^ ID DOT!
            | RRULE^ NUMBER DOT!
            | RRULE DOT { #proofcommand = #[RULEALONE,"RULEALONE"]; }
            | CUT^ pred DOT!
            | THEOREM^ ID DOT!
            | ASKRULES DOT!
            | DISPLAY DOT!
            | ID DOT!
            ;

start1: pred DOT! ;
start2: term DOT! ;
ident: ID DOT! ;
      
class SeqLexer extends Lexer;
options {
  k=7;
  defaultErrorHandler=false;
}

WS
  : (' '
    | '\t'
    | '\n'
    | '\r')
    { _ttype = Token.SKIP; }
  ;
  
LPAREN : '(' ;

RPAREN : ')' ;

SEQ : "|-";

IMPL : "=>";

OR : "\\/" ;

AND :  "/\\" ;

LIST : ',' ;

NOT : '!'
    | '^'
    | '~' ;

DOT : '.' ;

FORALL  : "\\A";

EXISTS : "\\E";

BOTTOM : "\\B";

TOP: "\\T";


ARROW : "->";

COLUMN: ':';

TIMES: '*';

PLUS: '+';

DIV: '/';

MINUS: '-';

GT: '>';

LT: '<';

PROOF: "proof";
RRULE: "rule";
TRULE: "termrule";
FOCUS: "focus";
ASKRULES: "showrules";
CUT: "cut";
DISPLAY: "display";
QUIT: "quit";
THEOREM: "theorem";
PRINT: "print";
PROOFCHECK: "proofcheck";

//VAR: ('a'..'z')('a'..'z'|'0'..'9')*;
ID : ('A'..'Z'|'a'..'z')('A'..'Z'|'a'..'z'|'0'..'9')*;
NUMBER: ('0'..'9')+;

