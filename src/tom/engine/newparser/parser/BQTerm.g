grammar BQTerm;
options {
  output=AST;
  ASTLabelType=Tree;
  backtrack=true;
  tokenVocab=BQTermLexer;
}

@parser::header {
package tom.engine.newparser.parser;
}

/* actual backquote already consumed */
csBQTerm
returns [int marker] :
  BQID
  {$marker = ((CustomToken)$BQID).getPayload(Integer.class);}

  -> ^(CsBQVar ^(CsName BQID))

 |BQIDPAR (csNonHeadBQTerm (COMMA csNonHeadBQTerm)*)? RPAR
  {$marker = ((CustomToken)$RPAR).getPayload(Integer.class);}

  -> ^(CsBQAppl ^(CsName BQIDPAR)
                ^(CsBQTermList csNonHeadBQTerm*))
 
 |BQIDBR (csPairSlotBQTerm (COMMA csPairSlotBQTerm)*)? RBR
  {$marker = ((CustomToken)$RBR).getPayload(Integer.class);}
  
  -> ^(CsBQRecordAppl
     	^(CsName BQIDBR)
        ^(CsPairSlotBQTermList csPairSlotBQTerm*)) 
 
 |BQIDSTAR
  {$marker = ((CustomToken)$BQIDSTAR).getPayload(Integer.class);}

  -> ^(CsBQVarStar ^(CsName BQIDSTAR))
 
; 

csNonHeadBQTerm :
  csBQTerm
  -> csBQTerm

 |UNDERSCORE
  -> ^(CsBQDefault)
; 

csPairSlotBQTerm :
  ID EQUAL (csNonHeadBQTerm)
  -> ^(CsPairSlotBQTerm ^(CsName ID) csNonHeadBQTerm)
;
