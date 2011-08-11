grammar BQTerm;
/*
 * Lexer rules are defined in BQTermLexer.g
 * Defining it in a different file allows to use the Lexer-only
 * option 'filter'.
 */
options {
  output=AST;
  ASTLabelType=Tree;
  backtrack=true;
  tokenVocab=BQTermLexer;
}

@parser::header {
package tom.engine.newparser.parser;
}

csBQTerm
returns [int marker] :
  BQID
  {$marker = ((CustomToken)$BQID).getPayload(Integer.class);}

  -> ^(CsBQVar ^(CsName BQID))

 |BQIDSTAR
  {$marker = ((CustomToken)$BQIDSTAR).getPayload(Integer.class);}

  -> ^(CsBQVarStar ^(CsName BQIDSTAR))

 |BQIDPAR (csMainBQTerm (COMMA csMainBQTerm)*)? RPAR
  {$marker = ((CustomToken)$RPAR).getPayload(Integer.class);}

  -> ^(CsBQAppl ^(CsName BQIDPAR)
                ^(CsBQTermList csMainBQTerm*))
 
 |BQIDBR (csPairSlotBQTerm (COMMA csMainBQTerm)*)? RBR
  {$marker = ((CustomToken)$RBR).getPayload(Integer.class);}
  
  -> ^(CsBQRecordAppl
     	^(CsName BQIDBR)
        ^(CsPairSlotBQTermList csPairSlotBQTerm*)) 
; 

csMainBQTerm :
  UNDERSCORE
  -> ^(CsBQVar ^(CsName BQID))

 |IDSTAR
  -> ^(CsBQVarStar ^(CsName IDSTAR))

 |IDPAR (csMainBQTerm (COMMA csMainBQTerm)*)? RPAR
  -> ^(CsBQAppl ^(CsName IDPAR)
                ^(CsBQTermList csMainBQTerm*))
 
 |IDBR (csPairSlotBQTerm (COMMA csMainBQTerm)*)? RBR
  -> ^(CsBQRecordAppl
     	^(CsName IDBR)
        ^(CsPairSlotBQTermList csPairSlotBQTerm*)) 
;

csPairSlotBQTerm :
  ID EQUAL (csMainBQTerm)
  -> ^(CsPairSlotBQTerm ^(CsName ID) csMainBQTerm)
;
