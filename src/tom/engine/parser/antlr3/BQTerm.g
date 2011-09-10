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
package tom.engine.parser.antlr3;

import org.antlr.runtime.CommonToken;
import org.antlr.runtime.tree.Tree;

import static tom.engine.parser.antlr3.TreeFactory.*;
import static tom.engine.parser.antlr3.miniTomParser.*;
}

@parser::members {

  public static CommonTree extractOptions(CommonToken t) {
    String newline = System.getProperty("line.separator");
    String lines[] = t.getText().split(newline);
    
    int firstCharLine = t.getLine();
    int firstCharColumn = t.getCharPositionInLine();
    int lastCharLine = firstCharColumn+lines.length-1;
    int lastCharColumn;
    if(lines.length==1) {
      lastCharColumn = firstCharColumn + lines[0].length();
    } else {
      lastCharColumn = lines[lines.length-1].length();
    }
//FIXME
    return
      makeOptions(t.getInputStream()!=null?t.getInputStream().getSourceName():"unknown",
      firstCharLine, firstCharColumn, lastCharLine, lastCharColumn);  
  }

  public static CommonTree extractOptions(CommonToken start, CommonToken end) {
    String newline = System.getProperty("line.separator");
    String lines[] = end.getText().split(newline);

    int lastCharLine = end.getLine()+lines.length;
    int lastCharColumn;
    if(lines.length==1) {
      lastCharColumn = end.getCharPositionInLine() + lines[0].length();
    } else {
      lastCharColumn = lines[lines.length-1].length();
    }

//FIXME
    return
      makeOptions(start.getInputStream()!=null?start.getInputStream().getSourceName():"unknown",
      start.getLine(), start.getCharPositionInLine(), lastCharLine,
      lastCharColumn);
  }
}
//beginBackQuote

csMainBQTerm [ boolean compositeAllowed] :
  UNDERSCORE -> ^(Cst_BQDefault {extractOptions((CommonToken)$UNDERSCORE)})
  | IDSTAR -> ^(Cst_BQVarStar {extractOptions((CommonToken)$IDSTAR)} IDSTAR ^(Cst_TypeUnknown ))
  | ID {$compositeAllowed}?=> c=csCompositePart*
    -> {c==null}? ^(Cst_BQVar {extractOptions((CommonToken)$ID)} 
        ID ^(Cst_TypeUnknown ))
    -> ^(Cst_CompositeTerm
         {extractOptions((CommonToken)$ID)}
         ^(Cst_concCstBQTerm
           ^(Cst_BQVar {extractOptions((CommonToken)$ID)}
             ID ^(Cst_TypeUnknown )) csCompositePart*
          )
        )
  | LPAR ID RPAR {$compositeAllowed}?=> c=csCompositePart*
    -> {c==null}? ^(Cst_BQVar {extractOptions((CommonToken)$ID)}
        ID ^(Cst_TypeUnknown ))
    -> ^(Cst_CompositeTerm
         {extractOptions((CommonToken)$LPAR, (CommonToken)$RPAR)}
         ^(Cst_concCstBQTerm
           ^(Cst_ITL {extractOptions((CommonToken)$LPAR)} LPAR)
           ^(Cst_BQVar {extractOptions((CommonToken)$ID)}
             ID ^(Cst_TypeUnknown ))
           ^(Cst_ITL {extractOptions((CommonToken)$RPAR)} RPAR)
           csCompositePart*
          )
        )

  | IDPAR (csMainBQTerm[true] (COMMA csMainBQTerm[true])*)? RPAR
    -> ^(Cst_BQAppl {extractOptions((CommonToken)$IDPAR, (CommonToken)$RPAR)}
        IDPAR ^(Cst_concCstBQTerm csMainBQTerm*))
 
  | IDBR (csPairSlotBQTerm (COMMA csPairSlotBQTerm)*)? RBR
    -> ^(Cst_BQRecordAppl {extractOptions((CommonToken)$IDBR, (CommonToken)$RBR)}
        IDBR ^(Cst_concCstPairSlotBQTerm csPairSlotBQTerm*))
  | csTL -> ^(Cst_ITL {extractOptions((CommonToken)$csTL.start, (CommonToken)$csTL.stop)} csTL)
  ;

csBQTerm
returns [int marker] :
 BQID
  {$marker = ((CustomToken)$BQID).getPayload(Integer.class);}
  -> ^(Cst_BQVar {extractOptions((CommonToken)$BQID)} BQID ^(Cst_TypeUnknown ))

 |BQPAR csCompositePart* RPAR
  {$marker = ((CustomToken)$RPAR).getPayload(Integer.class);}

  -> ^(Cst_CompositeTerm 
       {extractOptions((CommonToken)$BQPAR, (CommonToken)$RPAR)}
       ^(Cst_concCstBQTerm
         ^(Cst_ITL {extractOptions((CommonToken)$BQPAR)} BQPAR)
         ^(Cst_CompositeTerm
           {extractOptions((CommonToken)$csCompositePart.stop)}
           ^(Cst_concCstBQTerm csCompositePart*))
         ^(Cst_ITL {extractOptions((CommonToken)$RPAR)} RPAR)
        )
      )

 |BQIDSTAR
  {$marker = ((CustomToken)$BQIDSTAR).getPayload(Integer.class);}

  -> ^(Cst_BQVarStar {extractOptions((CommonToken)$BQIDSTAR)} BQIDSTAR ^(Cst_TypeUnknown ))

 |BQIDPAR (csMainBQTerm[true] (COMMA csMainBQTerm[true])*)? RPAR
  {$marker = ((CustomToken)$RPAR).getPayload(Integer.class);}

  -> ^(Cst_BQAppl {extractOptions((CommonToken)$BQIDPAR, (CommonToken)$RPAR)}
      BQIDPAR ^(Cst_concCstBQTerm csMainBQTerm*))
 
 |BQIDBR (csPairSlotBQTerm (COMMA csPairSlotBQTerm)*)? RBR
  {$marker = ((CustomToken)$RBR).getPayload(Integer.class);}
  
  -> ^(Cst_BQRecordAppl {extractOptions((CommonToken)$BQIDBR, (CommonToken)$RBR)}
      BQIDBR ^(Cst_concCstPairSlotBQTerm csPairSlotBQTerm*)) 
 ; 

csPairSlotBQTerm :
  ID EQUAL csMainBQTerm[false]
  -> ^(Cst_PairSlotBQTerm {extractOptions((CommonToken)$ID)} ^(Cst_Name ID) csMainBQTerm)
;

csCompositePart :
   ANY -> ^(Cst_ITL {extractOptions((CommonToken)$ANY)} ANY)
  | EQUAL -> ^(Cst_ITL {extractOptions((CommonToken)$EQUAL)} EQUAL)
  | NUM -> ^(Cst_ITL {extractOptions((CommonToken)$NUM)} NUM)
  | BQDOT -> ^(Cst_ITL {extractOptions((CommonToken)$BQDOT)} BQDOT)
  | BQSTRING -> ^(Cst_ITL {extractOptions((CommonToken)$BQSTRING)} BQSTRING)
  | BQCHAR -> ^(Cst_ITL {extractOptions((CommonToken)$BQCHAR)} BQCHAR)
  | csMainBQTerm[true] -> csMainBQTerm
  ;

csTL :
  BQSTRING | NUM | BQDOT | BQCHAR |ANY
  ;
