/* This grammar parses simple logo */
grammar Langage;
options {
  output=AST;
  ASTLabelType=Tree;
  tokenVocab=AstTokens;
}

@header {
  package logo;
}
@lexer::header {
  package logo;
}

LeveCrayon : 'LC' | 'leve crayon';
BaisseCrayon : 'BC' | 'baisse crayon' | 'PC';
Avance : 'AV' | 'avance';
PLUS : '+';

instruction :
    LeveCrayon -> ^(LC)
  | BaisseCrayon -> ^(BC)
  | Avance e=expression -> ^(AV $e)
  | TourneGauche e=expression -> ^(TG $e)
  | TourneDroite e=expression -> ^(TD $e)
  | Recule e=expression -> ^(RE $e)
  | Repete n=INT '[' il=instructionlist ']' -> ^(REP $n $il)
  | '[' il=instructionlist ']' -> ^(WORLD $il)
  ;

expression :
    e1=elementaryexpression (PLUS e2=expression)?
    -> {e2!=null}? ^(Plus $e1 $e2)
    -> $e1
;

elementaryexpression :
    INT -> ^(Cst INT)
;

instructionlist :
  (instruction)* -> ^(InstructionList (instruction)*)
  ;

program :
  instructionlist EOF -> instructionlist
  ;



TourneGauche : 'TG' | 'gauche' | 'tourne gauche';
TourneDroite : 'TD' | 'droite' |'tourne droite';
Recule : 'RE' | 'recule';
Repete : 'repete';

INT : ('0'..'9')+ ;
ESC : '\\' ( 'n'| 'r'| 't'| 'b'| 'f'| '"'| '\''| '\\') ;
STRING : '"' (ESC|~('"'|'\\'|'\n'|'\r'))* '"' ;
ID : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ('*')?;
WS : (' '|'\t'|'\n')+ { $channel=HIDDEN; } ;

SLCOMMENT : '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)? { $channel=HIDDEN; } ;
