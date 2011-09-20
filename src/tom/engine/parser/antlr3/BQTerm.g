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
    int firstCharColumn = t.getCharPositionInLine()+1;
    int lastCharLine = firstCharLine+lines.length-1;
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
      start.getLine(), start.getCharPositionInLine(), lastCharLine, lastCharColumn);
  }
}
//beginBackQuote

csMainBQTerm [ boolean compositeAllowed] :
  UNDERSCORE -> ^(Cst_BQDefault {extractOptions((CommonToken)$UNDERSCORE)})
  | IDSTAR -> ^(Cst_BQVarStar {extractOptions((CommonToken)$IDSTAR)} IDSTAR ^(Cst_TypeUnknown ))
  | ID {$compositeAllowed}?=> c=csCompositePart*
    -> {c==null}? ^(Cst_BQVar {extractOptions((CommonToken)$ID)} 
        ID ^(Cst_TypeUnknown ))
    -> ^(Cst_BQComposite
         {extractOptions((CommonToken)$ID)}
         ^(ConcCstBQTerm
           ^(Cst_BQVar {extractOptions((CommonToken)$ID)}
             ID ^(Cst_TypeUnknown )) csCompositePart*
          )
        )
  | LPAR ID RPAR {$compositeAllowed}?=> c=csCompositePart*
    -> {c==null}? ^(Cst_BQVar {extractOptions((CommonToken)$ID)}
        ID ^(Cst_TypeUnknown ))
    -> ^(Cst_BQComposite
         {extractOptions((CommonToken)$LPAR, (CommonToken)$RPAR)}
         ^(ConcCstBQTerm
           ^(Cst_ITL {extractOptions((CommonToken)$LPAR)} LPAR)
           ^(Cst_BQVar {extractOptions((CommonToken)$ID)}
             ID ^(Cst_TypeUnknown ))
           ^(Cst_ITL {extractOptions((CommonToken)$RPAR)} RPAR)
           csCompositePart*
          )
        )

  | IDPAR (csMainBQTerm[true] (COMMA csMainBQTerm[true])*)? RPAR
    -> ^(Cst_BQAppl {extractOptions((CommonToken)$IDPAR, (CommonToken)$RPAR)}
        IDPAR ^(ConcCstBQTerm csMainBQTerm*))
 
  | IDBR (csPairSlotBQTerm (COMMA csPairSlotBQTerm)*)? RBR
    -> ^(Cst_BQRecordAppl {extractOptions((CommonToken)$IDBR, (CommonToken)$RBR)}
        IDBR ^(ConcCstPairSlotBQTerm csPairSlotBQTerm*))
  | csTL csMainBQTerm[true]*
    -> ^(Cst_BQComposite
         {extractOptions((CommonToken)$csTL.start,(CommonToken)$csTL.stop)}
         ^(ConcCstBQTerm csTL csMainBQTerm*)
        )
  ;

csBQTerm
returns [int marker] :
 BQID
  {$marker = ((CustomToken)$BQID).getPayload(Integer.class);}
  -> ^(Cst_BQVar {extractOptions((CommonToken)$BQID)} BQID ^(Cst_TypeUnknown ))

 |BQPAR csCompositePart* RPAR
  {$marker = ((CustomToken)$RPAR).getPayload(Integer.class);}

  -> ^(Cst_BQComposite 
       {extractOptions((CommonToken)$BQPAR, (CommonToken)$RPAR)}
       ^(ConcCstBQTerm
         ^(Cst_ITL {extractOptions((CommonToken)$BQPAR)} BQPAR)
         ^(Cst_BQComposite
           {extractOptions((CommonToken)$csCompositePart.stop)}
           ^(ConcCstBQTerm csCompositePart*))
         ^(Cst_ITL {extractOptions((CommonToken)$RPAR)} RPAR)
        )
      )

 |BQIDSTAR
  {$marker = ((CustomToken)$BQIDSTAR).getPayload(Integer.class);}

  -> ^(Cst_BQVarStar {extractOptions((CommonToken)$BQIDSTAR)} BQIDSTAR ^(Cst_TypeUnknown ))

 |BQIDPAR (csMainBQTerm[true] (COMMA csMainBQTerm[true])*)? RPAR
  {$marker = ((CustomToken)$RPAR).getPayload(Integer.class);}

  -> ^(Cst_BQAppl {extractOptions((CommonToken)$BQIDPAR, (CommonToken)$RPAR)}
      BQIDPAR ^(ConcCstBQTerm csMainBQTerm*))
 
 |BQIDBR (csPairSlotBQTerm (COMMA csPairSlotBQTerm)*)? RBR
  {$marker = ((CustomToken)$RBR).getPayload(Integer.class);}
  
  -> ^(Cst_BQRecordAppl {extractOptions((CommonToken)$BQIDBR, (CommonToken)$RBR)}
      BQIDBR ^(ConcCstPairSlotBQTerm csPairSlotBQTerm*)) 
 ; 

csPairSlotBQTerm :
  ID EQUAL csMainBQTerm[false]
  -> ^(Cst_PairSlotBQTerm {extractOptions((CommonToken)$ID)} ^(Cst_Name ID) csMainBQTerm)
;

csCompositePart :
   csTL
  | EQUAL -> ^(Cst_ITL {extractOptions((CommonToken)$EQUAL)} EQUAL)
  | csMainBQTerm[true] -> csMainBQTerm
  ;

csTL :
  BQSTRING -> ^(Cst_ITL {extractOptions((CommonToken)$BQSTRING)} BQSTRING)
  | NUM -> ^(Cst_ITL {extractOptions((CommonToken)$NUM)} NUM)
  | BQDOT -> ^(Cst_ITL {extractOptions((CommonToken)$BQDOT)} BQDOT)
  | BQCHAR -> ^(Cst_ITL {extractOptions((CommonToken)$BQCHAR)} BQCHAR)
  | ANY -> ^(Cst_ITL {extractOptions((CommonToken)$ANY)} ANY)
  ;