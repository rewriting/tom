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

//beginBackQuote

csMainBQTerm [ boolean compositeAllowed] :
  UNDERSCORE -> ^(Cst_BQDefault)
  | IDSTAR -> ^(Cst_BQVarStar IDSTAR ^(Cst_TypeUnknown ))
  | ID {$compositeAllowed}?=> c=csCompositePart*
    -> {c==null}? ^(Cst_BQVar ID ^(Cst_TypeUnknown ))
    -> ^(Cst_CompositeTerm
         ^(Cst_concCstBQTerm
           ^(Cst_BQVar ID ^(Cst_TypeUnknown )) csCompositePart*
          )
        )
  | LPAR ID RPAR {$compositeAllowed}?=> c=csCompositePart*
    -> {c==null}? ^(Cst_BQVar ID ^(Cst_TypeUnknown ))
    -> ^(Cst_CompositeTerm
         ^(Cst_concCstBQTerm
           ^(Cst_ITL LPAR)
           ^(Cst_BQVar ID ^(Cst_TypeUnknown ))
           ^(Cst_ITL RPAR)
           csCompositePart*
          )
        )

  | IDPAR (csMainBQTerm[true] (COMMA csMainBQTerm[true])*)? RPAR
    -> ^(Cst_BQAppl IDPAR ^(Cst_concCstBQTerm csMainBQTerm*))
 
  | IDBR (csPairSlotBQTerm (COMMA csPairSlotBQTerm)*)? RBR
    -> ^(Cst_BQRecordAppl IDBR ^(Cst_concCstPairSlotBQTerm csPairSlotBQTerm*)
        )
  | csTL -> ^(Cst_ITL csTL)
  ;
/*   
  | lp='(' csCompositePart* rp=')'
    -> ^(Cst_CompositeTerm 
         ^(Cst_concCstBQTerm
           ^(Cst_ITL $lp) ^(Cst_CompositeTerm ^(Cst_concCstBQTerm csCompositePart*)) ^(Cst_ITL $rp)
        )
      )
  | lp='(' csTL rp=')' -> ^(Cst_CompositeTerm 
      ^(Cst_concCstBQTerm ^(Cst_ITL $lp) ^(Cst_ITL csTL) ^(Cst_ITL $rp))
      )


 */

csBQTerm
returns [int marker] :
 BQID
  {$marker = ((CustomToken)$BQID).getPayload(Integer.class);}

  -> ^(Cst_BQVar BQID ^(Cst_TypeUnknown ))

 |BQPAR csCompositePart* RPAR
  {$marker = ((CustomToken)$RPAR).getPayload(Integer.class);}

  -> ^(Cst_CompositeTerm 
       ^(Cst_concCstBQTerm
         ^(Cst_ITL BQPAR) ^(Cst_CompositeTerm ^(Cst_concCstBQTerm csCompositePart*)) ^(Cst_ITL RPAR)
        )
      )

 |BQIDSTAR
  {$marker = ((CustomToken)$BQIDSTAR).getPayload(Integer.class);}

  -> ^(Cst_BQVarStar BQIDSTAR ^(Cst_TypeUnknown ))

 |BQIDPAR (csMainBQTerm[true] (COMMA csMainBQTerm[true])*)? RPAR
  {$marker = ((CustomToken)$RPAR).getPayload(Integer.class);}

  -> ^(Cst_BQAppl BQIDPAR ^(Cst_concCstBQTerm csMainBQTerm*))
 
 |BQIDBR (csPairSlotBQTerm (COMMA csPairSlotBQTerm)*)? RBR
  {$marker = ((CustomToken)$RBR).getPayload(Integer.class);}
  
  -> ^(Cst_BQRecordAppl BQIDBR ^(Cst_concCstPairSlotBQTerm csPairSlotBQTerm*)
      ) 
 ; 

csPairSlotBQTerm :
  ID EQUAL csMainBQTerm[false]
  -> ^(Cst_PairSlotBQTerm ^(Cst_Name ID) csMainBQTerm)
;

/*csComposite :
  csCompositePart* -> ^(Cst_CompositeTerm ^(Cst_concCstBQTerm csCompositePart*))
  | lp='(' csCompositePart* rp=')' -> ^(Cst_CompositeTerm ^(Cst_concCstBQTerm ^(Cst_ITL $lp) csCompositePart* ^(Cst_ITL $rp)))
  ;*/

csCompositePart :
   ANY -> ^(Cst_ITL ANY)
  | EQUAL -> ^(Cst_ITL EQUAL)
  | NUM -> ^(Cst_ITL NUM)
//  | MINUS -> ^(Cst_ITL MINUS)
  | BQDOT -> ^(Cst_ITL BQDOT)
  | BQSTRING -> ^(Cst_ITL BQSTRING)
  | BQCHAR -> ^(Cst_ITL BQCHAR)
  | csMainBQTerm[true] -> csMainBQTerm
  ;

csTL :
  BQSTRING | NUM | BQDOT | BQCHAR |/* MINUS |*/ ANY
  ;
