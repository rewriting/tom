grammar Generic;


@header {
  import generics.types.*;
}

@members {
  %include { generics/generics.tom }
}

/*
@parser::header {
  //package lemu;
}

@lexer::header {
  //package lemu;
}
*/

text: ( ~('{' | '}' | TYPETERM | OP | OPLIST) )*;

typeterm returns [Typeterm t]:	
{ InfoList l = `infolist(); }
TYPETERM n=ID '<' ty=ID '>' '{'
  ( left=text '{' right=text '}' { l = `infolist(l*,info($left.text,$right.text)); }  )+
'}' 
{ t = `typeterm(genId($n.text,$ty.text),l); }
;

arglist returns [IdList l]:
{ l = `idlist(); }
(i1=ID ':' t1=ID)? { l = `idlist(l*,genId($i1.text,$t1.text));  } (',' i2=ID ':' t2=ID { l = `idlist(l*,genId($i2.text,$t2.text));  }  )* 
;

op returns [Operator o] :
{ InfoList il = `infolist(); }
OP n1=ID '<' ty1=ID '>' n2=ID '<' ty2=ID '>' '(' al=arglist  ')'  '{' 
   ( left=text '{' right=text '}' { il = `infolist(il*,info($left.text,$right.text)); }  )+ 
'}' 
{ o = `op(genId($n1.text,$ty1.text),genId($n2.text,$ty2.text),al,il); }
;

oplist returns [Operator o]:
{ InfoList il = `infolist(); }
OPLIST n1=ID '<' ty1=ID '>' n2=ID '<' ty2=ID '>' '(' param=ID '*' ')'  '{' 
   ( left=text  '{' right=text '}' { il = `infolist(il*,info($left.text,$right.text)); } )+ 
'}' 
{ o = `oplist(genId($n1.text,$ty1.text),genId($n2.text,$ty2.text),$param.text,il); }
;

tom_file returns [Sig s]:
{ TypetermList tl = `ttlist();
  OperatorList ol = `olist();
}
(t=typeterm { tl = `ttlist(tl*,t); }  | (o=op | o=oplist) { ol = `olist(ol*,o); }  )* 
{ s = `sig(tl,ol); }
;

WS : (' '|'\r'|'\t'|'\u000C'|'\n') {$channel=HIDDEN;} ;

COMMENT :   '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;} ;

LINE_COMMENT : '//' ~('\n'|'\r')* '\r'? '\n' { $channel=HIDDEN;} ;

TYPETERM :	'\u0025typeterm' ;
OP :		'\u0025op' ;
OPLIST :	'\u0025oplist' ;
ID :		('_'|'A'..'Z'|'a'..'z')('_'|'A'..'Z'|'a'..'z'|'0'..'9')*;

ANY: . ; 
