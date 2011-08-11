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

  -> ^(Cst_BQVar ^(Cst_Name BQID))

 |BQIDSTAR
  {$marker = ((CustomToken)$BQIDSTAR).getPayload(Integer.class);}

  -> ^(Cst_BQVarStar ^(Cst_Name BQIDSTAR))

 |BQIDPAR (csMainBQTerm (COMMA csMainBQTerm)*)? RPAR
  {$marker = ((CustomToken)$RPAR).getPayload(Integer.class);}

  -> ^(Cst_BQAppl ^(Cst_Name BQIDPAR)
                ^(Cst_concCstBQTerm csMainBQTerm*))
 
 |BQIDBR (csPairSlotBQTerm (COMMA csPairSlotBQTerm)*)? RBR
  {$marker = ((CustomToken)$RBR).getPayload(Integer.class);}
  
  -> ^(Cst_BQRecordAppl
     	^(Cst_Name BQIDBR)
        ^(Cst_concCstPairSlotBQTerm csPairSlotBQTerm*)) 
 ; 

csMainBQTerm :
  UNDERSCORE
  -> ^(Cst_BQDefault)

 |IDSTAR
  -> ^(Cst_BQVarStar ^(Cst_Name IDSTAR))

 |ID
  -> ^(Cst_BQVar ^(Cst_Name ID))

 |IDPAR (csMainBQTerm (COMMA csMainBQTerm)*)? RPAR
  -> ^(Cst_BQAppl ^(Cst_Name IDPAR)
                ^(Cst_concCstBQTerm csMainBQTerm*))
 
 |IDBR (csPairSlotBQTerm (COMMA csPairSlotBQTerm)*)? RBR
  -> ^(Cst_BQRecordAppl
     	^(Cst_Name IDBR)
        ^(Cst_concCstPairSlotBQTerm csPairSlotBQTerm*)) 
;

csPairSlotBQTerm :
  ID EQUAL (csMainBQTerm)
  -> ^(Cst_PairSlotBQTerm ^(Cst_Name ID) csMainBQTerm)
;
