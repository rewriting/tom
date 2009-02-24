/* This grammar parses simple logo */
grammar Langage;
options {
  output=AST;
  ASTLabelType=Tree;
}

tokens {
  %include { ast/AstTokenList.txt }
}

@header {
  package logo;
}
@lexer::header {
  package logo;
}

LeveCrayon : 'LC' | 'leve crayon';
PoseCrayon : 'BC' | 'baisse crayon';
TourneGauche : 'TG' | 'gauche' | 'tourne gauche';
TourneDroite : 'TD' | 'droite' |'tourne droite';
Avance : 'AV' | 'avance';
Recule : 'RE' | 'recule';
Repete : 'repete';


PLUS : '+';

program :
  instructionlist EOF -> instructionlist
  ;
instructionlist :
  (instruction)* -> ^(InstructionList (instruction)*)
  ;

instruction :
    LeveCrayon -> ^(LC)
  | PoseCrayon -> ^(BC)
  | Avance e=expression -> ^(AV $e)
  | Recule e=expression -> ^(RE $e)
  | TourneGauche e=expression -> ^(TG $e)
  | TourneDroite e=expression -> ^(TD $e)
  | Repete n=INT '[' il=instructionlist ']' -> ^(REP $n $il)
  ;

expression :
    e1=elementaryexpression (PLUS e2=expression)?
    -> {e2!=null}? ^(Plus $e1 $e2)
    -> $e1
;

elementaryexpression :
    INT -> ^(Cst INT)
;


INT : ('0'..'9')+ ;
ESC : '\\' ( 'n'| 'r'| 't'| 'b'| 'f'| '"'| '\''| '\\') ;
STRING : '"' (ESC|~('"'|'\\'|'\n'|'\r'))* '"' ;
ID : ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')* ('*')?;
WS : (' '|'\t'|'\n')+ { $channel=HIDDEN; } ;

SLCOMMENT : '//' (~('\n'|'\r'))* ('\n'|'\r'('\n')?)? { $channel=HIDDEN; } ;
