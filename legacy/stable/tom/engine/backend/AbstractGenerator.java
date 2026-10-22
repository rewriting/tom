
























package tom.engine.backend;



import java.io.IOException;



import tom.engine.TomBase;
import tom.engine.exception.TomRuntimeException;



import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.code.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;



import tom.engine.tools.OutputCode;
import tom.engine.tools.SymbolTable;
import tom.platform.OptionManager;



import aterm.*;



public abstract class AbstractGenerator {

  protected OutputCode output;
  protected OptionManager optionManager;
  protected SymbolTable symbolTable;
  protected boolean prettyMode;

  public AbstractGenerator(OutputCode output, OptionManager optionManager,
                              SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
    this.optionManager = optionManager;
    this.output = output;
    this.prettyMode = ((Boolean)optionManager.getOptionValue("pretty")).booleanValue();
  }

  protected SymbolTable getSymbolTable(String moduleName) {
    
    
    
    return symbolTable;
  }

  protected TomSymbol getSymbolFromName(String tomName) {
    return TomBase.getSymbolFromName(tomName, symbolTable);
  }

  protected TomType getTermType(TomTerm t) {
    return TomBase.getTermType(t, symbolTable);
  }

  protected TomType getTermType(BQTerm t) {
    return TomBase.getTermType(t, symbolTable);
  }

  protected TomType getUniversalType() {
    return symbolTable.getUniversalType();
  }

  protected TomType getCodomain(TomSymbol tSymbol) {
    return TomBase.getSymbolCodomain(tSymbol);
  }


     private static   tom.engine.adt.code.types.BQTermList  tom_append_list_concBQTerm( tom.engine.adt.code.types.BQTermList l1,  tom.engine.adt.code.types.BQTermList  l2) {     if( l1.isEmptyconcBQTerm() ) {       return l2;     } else if( l2.isEmptyconcBQTerm() ) {       return l1;     } else if(  l1.getTailconcBQTerm() .isEmptyconcBQTerm() ) {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,tom_append_list_concBQTerm( l1.getTailconcBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTermList  tom_get_slice_concBQTerm( tom.engine.adt.code.types.BQTermList  begin,  tom.engine.adt.code.types.BQTermList  end, tom.engine.adt.code.types.BQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcBQTerm()  ||  (end== tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( begin.getHeadconcBQTerm() ,( tom.engine.adt.code.types.BQTermList )tom_get_slice_concBQTerm( begin.getTailconcBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.BQTerm  tom_append_list_Composite( tom.engine.adt.code.types.BQTerm l1,  tom.engine.adt.code.types.BQTerm  l2) {     if( l1.isEmptyComposite() ) {       return l2;     } else if( l2.isEmptyComposite() ) {       return l1;     } else if(  l1.getTailComposite() .isEmptyComposite() ) {       return  tom.engine.adt.code.types.bqterm.ConsComposite.make( l1.getHeadComposite() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqterm.ConsComposite.make( l1.getHeadComposite() ,tom_append_list_Composite( l1.getTailComposite() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTerm  tom_get_slice_Composite( tom.engine.adt.code.types.BQTerm  begin,  tom.engine.adt.code.types.BQTerm  end, tom.engine.adt.code.types.BQTerm  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyComposite()  ||  (end== tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqterm.ConsComposite.make( begin.getHeadComposite() ,( tom.engine.adt.code.types.BQTerm )tom_get_slice_Composite( begin.getTailComposite() ,end,tail)) ;   }   


  
  protected void generate(int deep, Code subject, String moduleName) throws IOException {
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )subject) instanceof tom.engine.adt.code.types.code.Tom) ) {


        generateList(deep, (( tom.engine.adt.code.types.Code )subject).getCodeList() , moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )subject) instanceof tom.engine.adt.code.types.code.TomInclude) ) {


        generateListInclude(deep, (( tom.engine.adt.code.types.Code )subject).getCodeList() , moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )subject) instanceof tom.engine.adt.code.types.code.BQTermToCode) ) {


        generateBQTerm(deep, (( tom.engine.adt.code.types.Code )subject).getBq() , moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )subject) instanceof tom.engine.adt.code.types.code.TargetLanguageToCode) ) {


        generateTargetLanguage(deep, (( tom.engine.adt.code.types.Code )subject).getTl() , moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )subject) instanceof tom.engine.adt.code.types.code.InstructionToCode) ) {


        generateInstruction(deep, (( tom.engine.adt.code.types.Code )subject).getAstInstruction() , moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )subject) instanceof tom.engine.adt.code.types.code.DeclarationToCode) ) {


        generateDeclaration(deep, (( tom.engine.adt.code.types.Code )subject).getAstDeclaration() , moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.Code) ) { tom.engine.adt.code.types.Code  tom___t=(( tom.engine.adt.code.types.Code )subject);


        System.out.println("Cannot generate code for: " + tom___t);
        throw new TomRuntimeException("Cannot generate code for: " + tom___t);
      }}}

  }
  
  
  protected void generateTomTerm(int deep, TomTerm subject, String moduleName) throws IOException {
    
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch59_4= false ; tom.engine.adt.tomterm.types.TomTerm  tomMatch59_2= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch59_3= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{ /* unamed block */tomMatch59_4= true ;tomMatch59_2=(( tom.engine.adt.tomterm.types.TomTerm )subject);}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{ /* unamed block */tomMatch59_4= true ;tomMatch59_3=(( tom.engine.adt.tomterm.types.TomTerm )subject);}}}if (tomMatch59_4) {

        output.write(deep,getVariableName((( tom.engine.adt.tomterm.types.TomTerm )subject)));
        return;
      }}}}
 
  }

  
  protected void generateBQTerm(int deep, BQTerm subject, String moduleName) throws IOException {
    
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildConstant) ) { tom.engine.adt.tomname.types.TomName  tomMatch60_1= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch60_1) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

        output.write( tomMatch60_1.getString() );
        return;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildTerm) ) { tom.engine.adt.tomname.types.TomName  tomMatch60_8= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch60_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {buildTerm(deep, tomMatch60_8.getString() , (( tom.engine.adt.code.types.BQTerm )subject).getArgs() , (( tom.engine.adt.code.types.BQTerm )subject).getModuleName() )


;
        return;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch60_24= false ; tom.engine.adt.code.types.BQTerm  tomMatch60_19= null ; tom.engine.adt.code.types.BQTerm  tomMatch60_18= null ; tom.engine.adt.code.types.BQTerm  tomMatch60_23= null ; tom.engine.adt.code.types.BQTerm  tomMatch60_20= null ; tom.engine.adt.code.types.BQTerm  tomMatch60_21= null ; tom.engine.adt.code.types.BQTerm  tomMatch60_22= null ;if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyList) ) {{ /* unamed block */tomMatch60_24= true ;tomMatch60_18=(( tom.engine.adt.code.types.BQTerm )subject);}} else {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyArray) ) {{ /* unamed block */tomMatch60_24= true ;tomMatch60_19=(( tom.engine.adt.code.types.BQTerm )subject);}} else {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildConsList) ) {{ /* unamed block */tomMatch60_24= true ;tomMatch60_20=(( tom.engine.adt.code.types.BQTerm )subject);}} else {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildAppendList) ) {{ /* unamed block */tomMatch60_24= true ;tomMatch60_21=(( tom.engine.adt.code.types.BQTerm )subject);}} else {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildConsArray) ) {{ /* unamed block */tomMatch60_24= true ;tomMatch60_22=(( tom.engine.adt.code.types.BQTerm )subject);}} else {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildAppendArray) ) {{ /* unamed block */tomMatch60_24= true ;tomMatch60_23=(( tom.engine.adt.code.types.BQTerm )subject);}}}}}}}if (tomMatch60_24) {


        buildListOrArray(deep, (( tom.engine.adt.code.types.BQTerm )subject), moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.FunctionCall) ) { tom.engine.adt.tomname.types.TomName  tomMatch60_26= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch60_26) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        buildFunctionCall(deep, tomMatch60_26.getString() ,  (( tom.engine.adt.code.types.BQTerm )subject).getArgs() , moduleName);
        return;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch60_37= false ; tom.engine.adt.code.types.BQTerm  tomMatch60_36= null ; tom.engine.adt.code.types.BQTerm  tomMatch60_35= null ;if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch60_37= true ;tomMatch60_35=(( tom.engine.adt.code.types.BQTerm )subject);}} else {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch60_37= true ;tomMatch60_36=(( tom.engine.adt.code.types.BQTerm )subject);}}}if (tomMatch60_37) {


        output.write(deep,getVariableName((( tom.engine.adt.code.types.BQTerm )subject)));
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) {


        generateExpression(deep, (( tom.engine.adt.code.types.BQTerm )subject).getExp() , moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( (((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.ConsComposite) || ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.EmptyComposite)) ) { tom.engine.adt.code.types.BQTerm  tomMatch60_end_46=(( tom.engine.adt.code.types.BQTerm )subject);do {{ /* unamed block */if (!( tomMatch60_end_46.isEmptyComposite() )) { tom.engine.adt.code.types.CompositeMember  tom___t= tomMatch60_end_46.getHeadComposite() ;{ /* unamed block */{ /* unamed block */if ( (tom___t instanceof tom.engine.adt.code.types.CompositeMember) ) {if ( ((( tom.engine.adt.code.types.CompositeMember )tom___t) instanceof tom.engine.adt.code.types.compositemember.CompositeTL) ) {




            generateTargetLanguage(deep, (( tom.engine.adt.code.types.CompositeMember )tom___t).getTl() , moduleName);
          }}}{ /* unamed block */if ( (tom___t instanceof tom.engine.adt.code.types.CompositeMember) ) {if ( ((( tom.engine.adt.code.types.CompositeMember )tom___t) instanceof tom.engine.adt.code.types.compositemember.CompositeBQTerm) ) {

            generateBQTerm(deep, (( tom.engine.adt.code.types.CompositeMember )tom___t).getterm() , moduleName);
          }}}}}if ( tomMatch60_end_46.isEmptyComposite() ) {tomMatch60_end_46=(( tom.engine.adt.code.types.BQTerm )subject);} else {tomMatch60_end_46= tomMatch60_end_46.getTailComposite() ;}}} while(!( (tomMatch60_end_46==(( tom.engine.adt.code.types.BQTerm )subject)) ));}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) { tom.engine.adt.code.types.BQTerm  tom___t=(( tom.engine.adt.code.types.BQTerm )subject);boolean tomMatch60_53= false ;if ( (((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.ConsComposite) || ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.EmptyComposite)) ) {if ( (tom___t==(( tom.engine.adt.code.types.BQTerm )subject)) ) {tomMatch60_53= true ;}}if (!(tomMatch60_53)) {




        throw new TomRuntimeException("Cannot generate code for bqterm "+tom___t);
      }}}}

  }

  protected String getVariableName(BQTerm var) {
    { /* unamed block */{ /* unamed block */if ( (var instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )var) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch62_1= (( tom.engine.adt.code.types.BQTerm )var).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch62_1) instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {

        return ("tom" + TomBase.tomNumberListToString( tomMatch62_1.getNumberList() ));
      }}}}{ /* unamed block */if ( (var instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )var) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch62_8= (( tom.engine.adt.code.types.BQTerm )var).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch62_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        return  tomMatch62_8.getString() ;
      }}}}{ /* unamed block */if ( (var instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )var) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) { tom.engine.adt.tomname.types.TomName  tomMatch62_15= (( tom.engine.adt.code.types.BQTerm )var).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch62_15) instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {


        return ("tom" + TomBase.tomNumberListToString( tomMatch62_15.getNumberList() ));
      }}}}{ /* unamed block */if ( (var instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )var) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) { tom.engine.adt.tomname.types.TomName  tomMatch62_22= (( tom.engine.adt.code.types.BQTerm )var).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch62_22) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        return  tomMatch62_22.getString() ;
      }}}}}

    throw new RuntimeException("cannot generate the name of the variable "+var);
  }

  protected String getVariableName(TomTerm var) {
    { /* unamed block */{ /* unamed block */if ( (var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch63_1= (( tom.engine.adt.tomterm.types.TomTerm )var).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch63_1) instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {

        return ("tom" + TomBase.tomNumberListToString( tomMatch63_1.getNumberList() ));
      }}}}{ /* unamed block */if ( (var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch63_8= (( tom.engine.adt.tomterm.types.TomTerm )var).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch63_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        return  tomMatch63_8.getString() ;
      }}}}{ /* unamed block */if ( (var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) { tom.engine.adt.tomname.types.TomName  tomMatch63_15= (( tom.engine.adt.tomterm.types.TomTerm )var).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch63_15) instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {


        return ("tom" + TomBase.tomNumberListToString( tomMatch63_15.getNumberList() ));
      }}}}{ /* unamed block */if ( (var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) { tom.engine.adt.tomname.types.TomName  tomMatch63_22= (( tom.engine.adt.tomterm.types.TomTerm )var).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch63_22) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        return  tomMatch63_22.getString() ;
      }}}}}

    throw new RuntimeException("cannot generate the name of the variable "+var);
  }

  public void generateExpression(int deep, Expression subject, String moduleName) throws IOException {
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {

        output.write( (( tom.engine.adt.tomexpression.types.Expression )subject).getCode() );
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Integer) ) {


        output.write( (( tom.engine.adt.tomexpression.types.Expression )subject).getvalue() );
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Negation) ) {


        buildExpNegation(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg() , moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Conditional) ) {


        buildExpConditional(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getCond() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getThen() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getElse() , moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.And) ) {


        buildExpAnd(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() , moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Or) ) {


        buildExpOr(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() , moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GreaterThan) ) {


        buildExpGreaterThan(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() , moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GreaterOrEqualThan) ) {


        buildExpGreaterOrEqualThan(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() , moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.LessThan) ) {


        buildExpLessThan(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() , moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.LessOrEqualThan) ) {


        buildExpLessOrEqualThan(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() , moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Bottom) ) {



        buildExpBottom(deep, (( tom.engine.adt.tomexpression.types.Expression )subject).getTomType() , moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.TrueTL) ) {


        buildExpTrue(deep);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.FalseTL) ) {


        buildExpFalse(deep);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.IsEmptyList) ) { tom.engine.adt.tomname.types.TomName  tomMatch64_59= (( tom.engine.adt.tomexpression.types.Expression )subject).getOpname() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch64_59) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.code.types.BQTerm  tom___expList= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;buildExpIsEmptyList(deep, tomMatch64_59.getString() ,getTermType(tom___expList),tom___expList,moduleName)


;
        return;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.IsEmptyArray) ) { tom.engine.adt.code.types.BQTerm  tom___expArray= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;


        buildExpIsEmptyArray(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getOpname() , getTermType(tom___expArray),  (( tom.engine.adt.tomexpression.types.Expression )subject).getIndex() , tom___expArray, moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.EqualTerm) ) {buildExpEqualTerm(deep, (( tom.engine.adt.tomexpression.types.Expression )subject).getTomType() , (( tom.engine.adt.tomexpression.types.Expression )subject).getKid1() , (( tom.engine.adt.tomexpression.types.Expression )subject).getKid2() ,moduleName)


;
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.EqualBQTerm) ) {buildExpEqualBQTerm(deep, (( tom.engine.adt.tomexpression.types.Expression )subject).getTomType() , (( tom.engine.adt.tomexpression.types.Expression )subject).gett1() , (( tom.engine.adt.tomexpression.types.Expression )subject).gett2() ,moduleName)

;
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.IsSort) ) { tom.engine.adt.tomtype.types.TomType  tomMatch64_85= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstType() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch64_85) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {buildExpIsSort(deep, tomMatch64_85.getTomType() , (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ,moduleName)

;
        return;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.IsFsym) ) { tom.engine.adt.tomname.types.TomName  tomMatch64_93= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch64_93) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        buildExpIsFsym(deep,  tomMatch64_93.getString() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() , moduleName);
        return;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Cast) ) { tom.engine.adt.tomtype.types.TomType  tomMatch64_101= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstType() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch64_101) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch64_105= tomMatch64_101.getTlType() ;if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch64_105) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {


        buildExpCast(deep, tomMatch64_105,  (( tom.engine.adt.tomexpression.types.Expression )subject).getSource() , moduleName);
        return;
      }}}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetSlot) ) { tom.engine.adt.tomname.types.TomName  tomMatch64_112= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() ; tom.engine.adt.code.types.BQTerm  tomMatch64_114= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch64_112) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {boolean tomMatch64_124= false ; tom.engine.adt.code.types.BQTerm  tomMatch64_122= null ; tom.engine.adt.code.types.BQTerm  tomMatch64_123= null ; tom.engine.adt.code.types.BQTerm  tomMatch64_121= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch64_114) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch64_124= true ;tomMatch64_121=tomMatch64_114;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch64_114) instanceof tom.engine.adt.code.types.bqterm.BuildTerm) ) {{ /* unamed block */tomMatch64_124= true ;tomMatch64_122=tomMatch64_114;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch64_114) instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) {{ /* unamed block */tomMatch64_124= true ;tomMatch64_123=tomMatch64_114;}}}}if (tomMatch64_124) {buildExpGetSlot(deep, tomMatch64_112.getString() , (( tom.engine.adt.tomexpression.types.Expression )subject).getSlotNameString() ,tomMatch64_114,moduleName)


;
        return;
      }}}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetSlot) ) { tom.engine.adt.tomname.types.TomName  tomMatch64_127= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() ; tom.engine.adt.code.types.BQTerm  tomMatch64_129= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch64_127) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch64_129) instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression ) tomMatch64_129.getExp() ) instanceof tom.engine.adt.tomexpression.types.expression.GetSlot) ) {buildExpGetSlot(deep, tomMatch64_127.getString() , (( tom.engine.adt.tomexpression.types.Expression )subject).getSlotNameString() ,tomMatch64_129,moduleName)

;
        return;
      }}}}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetHead) ) { tom.engine.adt.tomname.types.TomName  tomMatch64_141= (( tom.engine.adt.tomexpression.types.Expression )subject).getOpname() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch64_141) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.code.types.BQTerm  tom___exp= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;buildExpGetHead(deep, tomMatch64_141.getString() ,getTermType(tom___exp), (( tom.engine.adt.tomexpression.types.Expression )subject).getCodomain() ,tom___exp,moduleName)


;
        return;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetTail) ) { tom.engine.adt.tomname.types.TomName  tomMatch64_150= (( tom.engine.adt.tomexpression.types.Expression )subject).getOpname() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch64_150) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.code.types.BQTerm  tom___exp= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;buildExpGetTail(deep, tomMatch64_150.getString() ,getTermType(tom___exp),tom___exp,moduleName)


;
        return;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.AddOne) ) {


        buildAddOne(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() , moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.SubstractOne) ) {


        buildSubstractOne(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() , moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Substract) ) {


        buildSubstract(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getTerm1() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getTerm2() , moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetSize) ) { tom.engine.adt.code.types.BQTerm  tom___exp= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;


        buildExpGetSize(deep, (( tom.engine.adt.tomexpression.types.Expression )subject).getOpname() ,getTermType(tom___exp), tom___exp, moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetElement) ) { tom.engine.adt.code.types.BQTerm  tom___varName= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;


        buildExpGetElement(deep, (( tom.engine.adt.tomexpression.types.Expression )subject).getOpname() ,getTermType(tom___varName),tom___varName,  (( tom.engine.adt.tomexpression.types.Expression )subject).getIndex() , moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetSliceList) ) { tom.engine.adt.tomname.types.TomName  tomMatch64_183= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch64_183) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        buildExpGetSliceList(deep,  tomMatch64_183.getString() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableBeginAST() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableEndAST() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getTail() ,moduleName);
        return;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetSliceArray) ) { tom.engine.adt.tomname.types.TomName  tomMatch64_193= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch64_193) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        buildExpGetSliceArray(deep,  tomMatch64_193.getString() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getSubjectListName() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableBeginAST() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableEndAST() , moduleName);
        return;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.BQTermToExpression) ) {


        generateBQTerm(deep, (( tom.engine.adt.tomexpression.types.Expression )subject).getAstTerm() , moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.TomInstructionToExpression) ) {


        generateInstruction(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getInstruction() , moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) { tom.engine.adt.tomexpression.types.Expression  tom___t=(( tom.engine.adt.tomexpression.types.Expression )subject);


        System.out.println("Cannot generate code for expression: " + tom___t);
        throw new TomRuntimeException("Cannot generate code for expression: " + tom___t);
      }}}

  }

  
  protected void generateArray(int deep, BQTerm subject, BQTerm index, String moduleName) throws IOException {
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch65_1= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch65_1) instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {
        
        output.write("tom" + TomBase.tomNumberListToString( tomMatch65_1.getNumberList() ));        
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch65_8= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch65_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

        output.write( tomMatch65_8.getString() );        
      }}}}}{ /* unamed block */{ /* unamed block */if ( (index instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )index) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch66_1= (( tom.engine.adt.code.types.BQTerm )index).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch66_1) instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {



        output.write("[");
        output.write("tom" + TomBase.tomNumberListToString( tomMatch66_1.getNumberList() ));
        output.write("]");        
      }}}}{ /* unamed block */if ( (index instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )index) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch66_8= (( tom.engine.adt.code.types.BQTerm )index).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch66_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

        output.write("[");
        output.write( tomMatch66_8.getString() );
        output.write("]");
      }}}}{ /* unamed block */if ( (index instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )index) instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) { tom.engine.adt.tomexpression.types.Expression  tomMatch66_15= (( tom.engine.adt.code.types.BQTerm )index).getExp() ;if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch66_15) instanceof tom.engine.adt.tomexpression.types.expression.Integer) ) {

        output.write("[");
        output.write( tomMatch66_15.getvalue() );
        output.write("]");  
      }}}}}
    
  } 

  public void generateInstruction(int deep, Instruction subject, String moduleName) throws IOException {
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.CodeToInstruction) ) {generate(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getCode() ,moduleName)


;
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.BQTermToInstruction) ) {generateBQTerm(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getTom() ,moduleName)


;
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction) ) {generateExpression(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getExpr() ,moduleName)


;
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {


        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.Assign) ) { tom.engine.adt.code.types.BQTerm  tomMatch67_16= (( tom.engine.adt.tominstruction.types.Instruction )subject).getVariable() ;boolean tomMatch67_24= false ; tom.engine.adt.tomoption.types.OptionList  tomMatch67_20= null ; tom.engine.adt.code.types.BQTerm  tomMatch67_23= null ; tom.engine.adt.code.types.BQTerm  tomMatch67_22= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch67_16) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch67_24= true ;tomMatch67_22=tomMatch67_16;tomMatch67_20= tomMatch67_22.getOptions() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch67_16) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch67_24= true ;tomMatch67_23=tomMatch67_16;tomMatch67_20= tomMatch67_23.getOptions() ;}}}if (tomMatch67_24) {buildAssign(deep,tomMatch67_16,tomMatch67_20, (( tom.engine.adt.tominstruction.types.Instruction )subject).getSource() ,moduleName)


;
        return;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.AssignArray) ) { tom.engine.adt.code.types.BQTerm  tomMatch67_26= (( tom.engine.adt.tominstruction.types.Instruction )subject).getVariable() ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch67_26) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {buildAssignArray(deep,tomMatch67_26, tomMatch67_26.getOptions() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getIndex() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getSource() ,moduleName)


;
        return;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.Let) ) { tom.engine.adt.code.types.BQTerm  tomMatch67_35= (( tom.engine.adt.tominstruction.types.Instruction )subject).getVariable() ;boolean tomMatch67_48= false ; tom.engine.adt.tomoption.types.OptionList  tomMatch67_40= null ; tom.engine.adt.tomtype.types.TomType  tomMatch67_41= null ; tom.engine.adt.code.types.BQTerm  tomMatch67_43= null ; tom.engine.adt.code.types.BQTerm  tomMatch67_44= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch67_35) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch67_48= true ;tomMatch67_43=tomMatch67_35;tomMatch67_40= tomMatch67_43.getOptions() ;tomMatch67_41= tomMatch67_43.getAstType() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch67_35) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch67_48= true ;tomMatch67_44=tomMatch67_35;tomMatch67_40= tomMatch67_44.getOptions() ;tomMatch67_41= tomMatch67_44.getAstType() ;}}}if (tomMatch67_48) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch67_41) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {buildLet(deep,tomMatch67_35,tomMatch67_40, tomMatch67_41.getTlType() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getSource() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getAstInstruction() ,moduleName)


;
        return;
      }}}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.LetRef) ) { tom.engine.adt.code.types.BQTerm  tomMatch67_50= (( tom.engine.adt.tominstruction.types.Instruction )subject).getVariable() ;boolean tomMatch67_63= false ; tom.engine.adt.code.types.BQTerm  tomMatch67_58= null ; tom.engine.adt.tomoption.types.OptionList  tomMatch67_55= null ; tom.engine.adt.tomtype.types.TomType  tomMatch67_56= null ; tom.engine.adt.code.types.BQTerm  tomMatch67_59= null ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch67_50) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{ /* unamed block */tomMatch67_63= true ;tomMatch67_58=tomMatch67_50;tomMatch67_55= tomMatch67_58.getOptions() ;tomMatch67_56= tomMatch67_58.getAstType() ;}} else {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch67_50) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{ /* unamed block */tomMatch67_63= true ;tomMatch67_59=tomMatch67_50;tomMatch67_55= tomMatch67_59.getOptions() ;tomMatch67_56= tomMatch67_59.getAstType() ;}}}if (tomMatch67_63) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch67_56) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {buildLetRef(deep,tomMatch67_50,tomMatch67_55, tomMatch67_56.getTlType() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getSource() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getAstInstruction() ,moduleName)


;
        return;
      }}}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) {buildInstructionSequence(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getInstList() ,moduleName)



;
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.UnamedBlock) ) {buildUnamedBlock(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getInstList() ,moduleName)


;
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.NamedBlock) ) {buildNamedBlock(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getBlockName() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getInstList() ,moduleName)


;
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction ) (( tom.engine.adt.tominstruction.types.Instruction )subject).getFailureInst() ) instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {buildIf(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getSuccesInst() ,moduleName)


;
        return;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction ) (( tom.engine.adt.tominstruction.types.Instruction )subject).getSuccesInst() ) instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {buildIf(deep, tom.engine.adt.tomexpression.types.expression.Negation.make( (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() ) , (( tom.engine.adt.tominstruction.types.Instruction )subject).getFailureInst() ,moduleName)


;
        return;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {buildIfWithFailure(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getSuccesInst() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getFailureInst() ,moduleName)


;
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.DoWhile) ) {buildDoWhile(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getDoInst() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() ,moduleName)


;
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.WhileDo) ) {buildWhileDo(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getDoInst() ,moduleName)


;
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.RawAction) ) { tom.engine.adt.tominstruction.types.Instruction  tomMatch67_110= (( tom.engine.adt.tominstruction.types.Instruction )subject).getAstInstruction() ;if ( ((( tom.engine.adt.tominstruction.types.Instruction )tomMatch67_110) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) {generateInstructionList(deep, tomMatch67_110.getInstList() ,moduleName)


;
        return;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.RawAction) ) {generateInstruction(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getAstInstruction() ,moduleName)


;
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.Return) ) {buildReturn(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getKid1() ,moduleName)


;
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.CompiledMatch) ) {generateInstruction(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getAutomataInst() ,moduleName)



;
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.CompiledPattern) ) {generateInstruction(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getAutomataInst() ,moduleName)


;
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.Tracelink) ) { tom.engine.adt.tomname.types.TomName  tomMatch67_135= (( tom.engine.adt.tominstruction.types.Instruction )subject).getType() ; tom.engine.adt.tomname.types.TomName  tomMatch67_136= (( tom.engine.adt.tominstruction.types.Instruction )subject).getName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch67_135) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch67_136) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {buildTracelink(deep, tomMatch67_135.getString() , tomMatch67_136.getString() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getExpr() ,moduleName)



;
        return;
      }}}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.TracelinkPopulateResolve) ) { tom.engine.adt.tomname.types.TomName  tomMatch67_149= (( tom.engine.adt.tominstruction.types.Instruction )subject).getRefClassName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch67_149) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {buildTracelinkPopulateResolve(deep, tomMatch67_149.getString() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getTracedLinks() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getCurrent() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getLink() ,moduleName)


;
        return;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.Resolve) ) {



        
        
        generateBQTerm(deep,  (( tom.engine.adt.tominstruction.types.Instruction )subject).getResolveBQTerm() , moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) { tom.engine.adt.tominstruction.types.Instruction  tom___t=(( tom.engine.adt.tominstruction.types.Instruction )subject);


        System.out.println("Cannot generate code for instruction: " + tom___t);
        throw new TomRuntimeException("Cannot generate code for instruction: " + tom___t);
      }}}

  }

  public void generateTargetLanguage(int deep, TargetLanguage subject, String moduleName) throws IOException {
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.TargetLanguage) ) {if ( ((( tom.engine.adt.code.types.TargetLanguage )subject) instanceof tom.engine.adt.code.types.targetlanguage.TL) ) { tom.engine.adt.tomsignature.types.TextPosition  tomMatch68_2= (( tom.engine.adt.code.types.TargetLanguage )subject).getStart() ; tom.engine.adt.tomsignature.types.TextPosition  tomMatch68_3= (( tom.engine.adt.code.types.TargetLanguage )subject).getEnd() ;if ( ((( tom.engine.adt.tomsignature.types.TextPosition )tomMatch68_2) instanceof tom.engine.adt.tomsignature.types.textposition.TextPosition) ) { int  tom___startLine= tomMatch68_2.getLine() ;if ( ((( tom.engine.adt.tomsignature.types.TextPosition )tomMatch68_3) instanceof tom.engine.adt.tomsignature.types.textposition.TextPosition) ) {

        int length =   tomMatch68_3.getLine()  - tom___startLine;
        output.write(deep,  (( tom.engine.adt.code.types.TargetLanguage )subject).getCode() , tom___startLine, length);
        return;
      }}}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.TargetLanguage) ) {if ( ((( tom.engine.adt.code.types.TargetLanguage )subject) instanceof tom.engine.adt.code.types.targetlanguage.ITL) ) {

        output.write( (( tom.engine.adt.code.types.TargetLanguage )subject).getCode() );
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.TargetLanguage) ) {if ( ((( tom.engine.adt.code.types.TargetLanguage )subject) instanceof tom.engine.adt.code.types.targetlanguage.Comment) ) {buildComment(deep, (( tom.engine.adt.code.types.TargetLanguage )subject).getCode() )

;
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.TargetLanguage) ) { tom.engine.adt.code.types.TargetLanguage  tom___t=(( tom.engine.adt.code.types.TargetLanguage )subject);

        System.out.println("Cannot generate code for TL: " + tom___t);
        throw new TomRuntimeException("Cannot generate code for TL: " + tom___t);
      }}}

  }

  public void generateOption(int deep, Option subject, String moduleName) throws IOException {
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )subject) instanceof tom.engine.adt.tomoption.types.option.DeclarationToOption) ) {generateDeclaration(deep, (( tom.engine.adt.tomoption.types.Option )subject).getAstDeclaration() ,moduleName)

;
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )subject) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {
 return; }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )subject) instanceof tom.engine.adt.tomoption.types.option.ACSymbol) ) {

        
        return; 
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomoption.types.Option) ) {

        throw new TomRuntimeException("Cannot generate code for option: " + (( tom.engine.adt.tomoption.types.Option )subject));
      }}}

  }

  public void generateDeclaration(int deep, Declaration subject, String moduleName) throws IOException {
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.EmptyDeclaration) ) {

        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.AbstractDecl) ) {generateDeclarationList(deep, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getDeclList() ,moduleName)



;
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.FunctionDef) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_8= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {buildFunctionDef(deep, tomMatch70_8.getString() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getArgumentList() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getCodomain() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getThrowsType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstruction() ,moduleName)


;
        return;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.MethodDef) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_19= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_19) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {buildMethodDef(deep, tomMatch70_19.getString() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getArgumentList() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getCodomain() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getThrowsType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstruction() ,moduleName)


;
        return;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.Class) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_30= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_30) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {buildClass(deep, tomMatch70_30.getString() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExtendsType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getSuperTerm() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getDeclaration() ,moduleName)


;
        return;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.IntrospectorClass) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_40= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_40) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {buildIntrospectorClass(deep, tomMatch70_40.getString() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getDeclaration() ,moduleName)


;
        return;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.SymbolDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_48= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_48) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {buildSymbolDecl(deep, tomMatch70_48.getString() ,moduleName)


;
        return ;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.ArraySymbolDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_55= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_55) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom___tomName= tomMatch70_55.getString() ;


        if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom___tomName) 
            ||getSymbolTable(moduleName).isUsedSymbolDestructor(tom___tomName)) { /* unamed block */buildSymbolDecl(deep,tom___tomName,moduleName)
;
          genDeclArray(tom___tomName,moduleName);
        }
        return ;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {boolean tomMatch70_69= false ; tom.engine.adt.tomdeclaration.types.Declaration  tomMatch70_65= null ; tom.engine.adt.tomname.types.TomName  tomMatch70_62= null ; tom.engine.adt.tomdeclaration.types.Declaration  tomMatch70_64= null ;if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.ListSymbolDecl) ) {{ /* unamed block */tomMatch70_69= true ;tomMatch70_64=(( tom.engine.adt.tomdeclaration.types.Declaration )subject);tomMatch70_62= tomMatch70_64.getAstName() ;}} else {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.ACSymbolDecl) ) {{ /* unamed block */tomMatch70_69= true ;tomMatch70_65=(( tom.engine.adt.tomdeclaration.types.Declaration )subject);tomMatch70_62= tomMatch70_65.getAstName() ;}}}if (tomMatch70_69) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_62) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom___tomName= tomMatch70_62.getString() ;


        if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom___tomName) 
            ||getSymbolTable(moduleName).isUsedSymbolDestructor(tom___tomName)) { /* unamed block */buildSymbolDecl(deep,tom___tomName,moduleName)
;
          genDeclList(tom___tomName,moduleName);
        }
        return ;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsFsymDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_71= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ; tom.engine.adt.code.types.BQTerm  tomMatch70_72= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_71) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom___tomName= tomMatch70_71.getString() ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch70_72) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_80= tomMatch70_72.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch70_81= tomMatch70_72.getAstType() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_80) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch70_81) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch70_87= tomMatch70_81.getTlType() ;if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch70_87) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {



        if(getSymbolTable(moduleName).isUsedSymbolDestructor(tom___tomName)) { /* unamed block */buildIsFsymDecl(deep,tom___tomName, tomMatch70_80.getString() ,tomMatch70_87, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName)
;
        }
        return;
      }}}}}}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetSlotDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_93= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ; tom.engine.adt.code.types.BQTerm  tomMatch70_95= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_93) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom___tomName= tomMatch70_93.getString() ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch70_95) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_102= tomMatch70_95.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch70_103= tomMatch70_95.getAstType() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_102) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch70_103) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch70_109= tomMatch70_103.getTlType() ;if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch70_109) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {





          if(getSymbolTable(moduleName).isUsedSymbolDestructor(tom___tomName)) { /* unamed block */buildGetSlotDecl(deep,tom___tomName, tomMatch70_102.getString() ,tomMatch70_109, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getSlotName() ,moduleName)
;
          }
          return;
        }}}}}}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.ImplementDecl) ) {if ( ((( tom.engine.adt.tomname.types.TomName ) (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {



        
        return;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetDefaultDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_123= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_123) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom___tomName= tomMatch70_123.getString() ;


        if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom___tomName)) { /* unamed block */buildGetDefaultDecl(deep,tom___tomName, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getSlotName() ,moduleName)
;
        }
        return;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.EqualTermDecl) ) { tom.engine.adt.code.types.BQTerm  tomMatch70_132= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getTermArg1() ; tom.engine.adt.code.types.BQTerm  tomMatch70_133= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getTermArg2() ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch70_132) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_138= tomMatch70_132.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch70_139= tomMatch70_132.getAstType() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_138) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch70_139) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tom___type1= tomMatch70_139.getTomType() ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch70_133) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_148= tomMatch70_133.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch70_149= tomMatch70_133.getAstType() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_148) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch70_149) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {




        if(getSymbolTable(moduleName).isUsedType(tom___type1)) { /* unamed block */buildEqualTermDecl(deep, tomMatch70_138.getString() , tomMatch70_148.getString() ,tom___type1, tomMatch70_149.getTomType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName)
;
        }
        return;
      }}}}}}}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsSortDecl) ) { tom.engine.adt.code.types.BQTerm  tomMatch70_159= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getTermArg() ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch70_159) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_164= tomMatch70_159.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch70_165= tomMatch70_159.getAstType() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_164) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch70_165) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tom___type= tomMatch70_165.getTomType() ;


        if(getSymbolTable(moduleName).isUsedType(tom___type)) { /* unamed block */buildIsSortDecl(deep, tomMatch70_164.getString() ,tom___type, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName)
;
        }
        return;
      }}}}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.BQTermToDeclaration) ) {


        generateBQTerm(deep, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getBqTerm() ,moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.ResolveIsFsymDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_179= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ; tom.engine.adt.code.types.BQTerm  tomMatch70_180= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_179) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom___tomName= tomMatch70_179.getString() ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch70_180) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_187= tomMatch70_180.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch70_188= tomMatch70_180.getAstType() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_187) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch70_188) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch70_194= tomMatch70_188.getTlType() ;if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch70_194) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {




        if(getSymbolTable(moduleName).isUsedSymbolDestructor(tom___tomName)) { /* unamed block */buildResolveIsFsymDecl(deep,tom___tomName, tomMatch70_187.getString() ,tomMatch70_194,moduleName)
;
        }
        return;
      }}}}}}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.ResolveGetSlotDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_200= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ; tom.engine.adt.code.types.BQTerm  tomMatch70_202= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_200) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom___tomName= tomMatch70_200.getString() ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch70_202) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_208= tomMatch70_202.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch70_209= tomMatch70_202.getAstType() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_208) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch70_209) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch70_215= tomMatch70_209.getTlType() ;if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch70_215) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {




          if(getSymbolTable(moduleName).isUsedSymbolDestructor(tom___tomName)) { /* unamed block */buildResolveGetSlotDecl(deep,tom___tomName, tomMatch70_208.getString() ,tomMatch70_215, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getSlotName() ,moduleName)
;
          }
          return;
        }}}}}}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.ResolveMakeDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_221= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_221) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom___opname= tomMatch70_221.getString() ;



        if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom___opname)) { /* unamed block */genResolveDeclMake("tom_make_",tom___opname, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getArgs() ,moduleName)
;
        }
        return;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.ResolveClassDecl) ) {buildResolveClass( (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getWithName() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getToName() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExtends() ,moduleName)


;
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.ResolveInverseLinksDecl) ) {buildResolveInverseLinks(deep, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getFileFrom() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getFileTo() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getResolveNameList() ,moduleName)


;
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.ReferenceClass) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_243= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getRefName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_243) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {buildReferenceClass(deep, tomMatch70_243.getString() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getFields() ,moduleName)







;
        
        return;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetHeadDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_251= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getOpname() ; tom.engine.adt.tomtype.types.TomType  tomMatch70_252= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getCodomain() ; tom.engine.adt.code.types.BQTerm  tomMatch70_253= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_251) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom___opname= tomMatch70_251.getString() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch70_252) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch70_253) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_263= tomMatch70_253.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch70_264= tomMatch70_253.getAstType() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_263) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch70_264) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch70_271= tomMatch70_264.getTlType() ;if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch70_271) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {








          if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom___opname) 
           ||getSymbolTable(moduleName).isUsedSymbolDestructor(tom___opname)) { /* unamed block */buildGetHeadDecl(deep,tomMatch70_251, tomMatch70_263.getString() , tomMatch70_264.getTomType() ,tomMatch70_271, tomMatch70_252.getTlType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName)
;
          }
          return;
        }}}}}}}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetTailDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_277= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getOpname() ; tom.engine.adt.code.types.BQTerm  tomMatch70_278= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_277) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom___opname= tomMatch70_277.getString() ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch70_278) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_285= tomMatch70_278.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch70_286= tomMatch70_278.getAstType() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_285) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch70_286) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch70_293= tomMatch70_286.getTlType() ;if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch70_293) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {





          if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom___opname) 
              ||getSymbolTable(moduleName).isUsedSymbolDestructor(tom___opname)) { /* unamed block */buildGetTailDecl(deep,tomMatch70_277, tomMatch70_285.getString() , tomMatch70_286.getTomType() ,tomMatch70_293, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName)
;
          }
          return;
        }}}}}}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsEmptyDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_299= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getOpname() ; tom.engine.adt.code.types.BQTerm  tomMatch70_300= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_299) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom___opname= tomMatch70_299.getString() ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch70_300) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_307= tomMatch70_300.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch70_308= tomMatch70_300.getAstType() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_307) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch70_308) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch70_315= tomMatch70_308.getTlType() ;if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch70_315) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {





          if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom___opname) 
              ||getSymbolTable(moduleName).isUsedSymbolDestructor(tom___opname)) { /* unamed block */buildIsEmptyDecl(deep,tomMatch70_299, tomMatch70_307.getString() , tomMatch70_308.getTomType() ,tomMatch70_315, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName)
;
          }
          return;
        }}}}}}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyList) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_321= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_321) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom___opname= tomMatch70_321.getString() ;


        TomType returnType = TomBase.getSymbolCodomain(getSymbolFromName(tom___opname));
        if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom___opname) 
            || getSymbolTable(moduleName).isUsedSymbolDestructor(tom___opname)) { /* unamed block */genDeclMake("tom_empty_list_",tom___opname,returnType, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstr() ,moduleName)
;
        }
        return;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeAddList) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_330= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ; tom.engine.adt.code.types.BQTerm  tomMatch70_331= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVarElt() ; tom.engine.adt.code.types.BQTerm  tomMatch70_332= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVarList() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_330) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom___opname= tomMatch70_330.getString() ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch70_331) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomtype.types.TomType  tomMatch70_340= tomMatch70_331.getAstType() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch70_340) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType ) tomMatch70_340.getTlType() ) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch70_332) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomtype.types.TomType  tomMatch70_348= tomMatch70_332.getAstType() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch70_348) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType ) tomMatch70_348.getTlType() ) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {





        TomType returnType = tomMatch70_348;
        if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom___opname) 
            ||getSymbolTable(moduleName).isUsedSymbolDestructor(tom___opname)) { /* unamed block */genDeclMake("tom_cons_list_",tom___opname,returnType, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(tomMatch70_331, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(tomMatch70_332, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstr() ,moduleName)
;
        }
        return;
      }}}}}}}}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetElementDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_357= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getOpname() ; tom.engine.adt.code.types.BQTerm  tomMatch70_358= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ; tom.engine.adt.code.types.BQTerm  tomMatch70_359= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getIndex() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_357) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch70_358) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_366= tomMatch70_358.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch70_367= tomMatch70_358.getAstType() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_366) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch70_367) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType ) tomMatch70_367.getTlType() ) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch70_359) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_379= tomMatch70_359.getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_379) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {





          if(getSymbolTable(moduleName).isUsedSymbolDestructor( tomMatch70_357.getString() )) { /* unamed block */buildGetElementDecl(deep,tomMatch70_357, tomMatch70_366.getString() , tomMatch70_379.getString() , tomMatch70_367.getTomType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName)
;
          }
          return;
        }}}}}}}}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetSizeDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_386= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getOpname() ; tom.engine.adt.code.types.BQTerm  tomMatch70_387= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_386) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch70_387) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_394= tomMatch70_387.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch70_395= tomMatch70_387.getAstType() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_394) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch70_395) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType ) tomMatch70_395.getTlType() ) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {





          if(getSymbolTable(moduleName).isUsedSymbolDestructor( tomMatch70_386.getString() )) { /* unamed block */buildGetSizeDecl(deep,tomMatch70_386, tomMatch70_394.getString() , tomMatch70_395.getTomType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName)
;
          }
          return;
        }}}}}}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyArray) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_408= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ; tom.engine.adt.code.types.BQTerm  tomMatch70_409= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVarSize() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_408) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom___opname= tomMatch70_408.getString() ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch70_409) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {




        TomType returnType = TomBase.getSymbolCodomain(getSymbolFromName(tom___opname));
        BQTerm newVar =  tom.engine.adt.code.types.bqterm.BQVariable.make( tomMatch70_409.getOptions() ,  tomMatch70_409.getAstName() , getSymbolTable(moduleName).getIntType()) ;
        if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom___opname)) { /* unamed block */genDeclMake("tom_empty_array_",tom___opname,returnType, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(newVar, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstr() ,moduleName)
;
        }
        return;
      }}}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeAddArray) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_422= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ; tom.engine.adt.code.types.BQTerm  tomMatch70_423= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVarElt() ; tom.engine.adt.code.types.BQTerm  tomMatch70_424= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVarList() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_422) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom___opname= tomMatch70_422.getString() ;if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch70_423) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomtype.types.TomType  tomMatch70_432= tomMatch70_423.getAstType() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch70_432) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType ) tomMatch70_432.getTlType() ) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch70_424) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomtype.types.TomType  tomMatch70_440= tomMatch70_424.getAstType() ;if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch70_440) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType ) tomMatch70_440.getTlType() ) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {





        TomType returnType = tomMatch70_440;
        if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom___opname)) { /* unamed block */genDeclMake("tom_cons_array_",tom___opname,returnType, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(tomMatch70_423, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(tomMatch70_424, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstr() ,moduleName)
;
        }
        return;
      }}}}}}}}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch70_449= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch70_449) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom___opname= tomMatch70_449.getString() ;


        if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom___opname)) { /* unamed block */genDeclMake("tom_make_",tom___opname, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getArgs() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstr() ,moduleName)
;
        }
        return;
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.TypeTermDecl) ) {


        
        generateDeclarationList(deep,  (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getDeclarations() , moduleName);
        return;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) { tom.engine.adt.tomdeclaration.types.Declaration  tom___t=(( tom.engine.adt.tomdeclaration.types.Declaration )subject);


        System.out.println("Cannot generate code for declaration: " + tom___t);
        throw new TomRuntimeException("Cannot generate code for declaration: " + tom___t);
      }}}

  }

  public void generateListInclude(int deep, CodeList subject, String moduleName) throws IOException {
    output.setSingleLine();
    generateList(deep, subject, moduleName);
    output.unsetSingleLine();
  }

  public void generateList(int deep, CodeList subject, String moduleName)
    throws IOException {
      while(!subject.isEmptyconcCode()) {
        generate(deep, subject.getHeadconcCode(), moduleName);
        subject = subject.getTailconcCode();
      }
    }

  public void generateOptionList(int deep, OptionList subject, String moduleName)
    throws IOException {
      while(!subject.isEmptyconcOption()) {
        generateOption(deep,subject.getHeadconcOption(), moduleName);
        subject = subject.getTailconcOption();
      }
    }

  public void generateInstructionList(int deep, InstructionList subject, String moduleName)
    throws IOException {
      while(!subject.isEmptyconcInstruction()) {
        generateInstruction(deep,subject.getHeadconcInstruction(), moduleName);
        subject = subject.getTailconcInstruction();
      }
      
    }

  public void generateDeclarationList(int deep, DeclarationList subject, String moduleName)
    throws IOException {
      while(!subject.isEmptyconcDeclaration()) {
        generateDeclaration(deep,subject.getHeadconcDeclaration(), moduleName);
        subject = subject.getTailconcDeclaration();
      }
    }

  public void generatePairNameDeclList(int deep, PairNameDeclList pairNameDeclList, String moduleName)
    throws IOException {
      while ( !pairNameDeclList.isEmptyconcPairNameDecl() ) {
        generateDeclaration(deep, pairNameDeclList.getHeadconcPairNameDecl().getSlotDecl(), moduleName);
        pairNameDeclList = pairNameDeclList.getTailconcPairNameDecl();
      }
    }

  

  protected abstract void genDecl(String returnType,
      String declName,
      String suffix,
      String args[],
      TargetLanguage tlCode,
      String moduleName) throws IOException;

  protected abstract void genDeclInstr(String returnType,
      String declName,
      String suffix,
      String args[],
      Instruction instr,
      int deep,
      String moduleName) throws IOException;

  protected abstract void genDeclMake(String prefix, String funName, TomType returnType,
      BQTermList argList, Instruction instr, String moduleName) throws IOException;

  protected abstract void genDeclList(String name, String moduleName) throws IOException;

  protected abstract void genDeclArray(String name, String moduleName) throws IOException;

  

  protected abstract void buildInstructionSequence(int deep, InstructionList instructionList, String moduleName) throws IOException;
  protected abstract void buildComment(int deep, String text) throws IOException;
  protected abstract void buildTerm(int deep, String name, BQTermList argList, String moduleName) throws IOException;
  protected abstract void buildListOrArray(int deep, BQTerm list, String moduleName) throws IOException;

  protected abstract void buildFunctionCall(int deep, String name, BQTermList argList, String moduleName)  throws IOException;
  protected abstract void buildFunctionDef(int deep, String tomName, BQTermList argList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName) throws IOException;
  protected void buildMethodDef(int deep, String tomName, BQTermList argList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName) throws IOException {
    throw new TomRuntimeException("Backend "+getClass()+" does not support Methods");
  }

  
  protected void buildClass(int deep, String tomName, TomType extendsType, BQTerm superTerm, Declaration declaration, String moduleName) throws IOException {
    throw new TomRuntimeException("Backend does not support Class");
  }

  
  protected void buildIntrospectorClass(int deep, String tomName, Declaration declaration, String moduleName) throws IOException {
    throw new TomRuntimeException("Backend does not support Class");
  }

  protected abstract void buildExpNegation(int deep, Expression exp, String moduleName) throws IOException;

  protected abstract void buildExpConditional(int deep, Expression cond,Expression exp1, Expression exp2, String moduleName) throws IOException;
  protected abstract void buildExpAnd(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException;
  protected abstract void buildExpOr(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException;
  protected abstract void buildExpGreaterThan(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException;
  protected abstract void buildExpGreaterOrEqualThan(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException;
  protected abstract void buildExpLessThan(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException;
  protected abstract void buildExpLessOrEqualThan(int deep, Expression exp1, Expression exp2, String moduleName) throws IOException;
  protected abstract void buildExpBottom(int deep, TomType type, String moduleName) throws IOException;
  protected abstract void buildExpTrue(int deep) throws IOException;
  protected abstract void buildExpFalse(int deep) throws IOException;
  protected abstract void buildExpIsEmptyList(int deep, String opName, TomType type, BQTerm expList, String moduleName) throws IOException;
  protected abstract void buildExpIsEmptyArray(int deep, TomName opName, TomType type, BQTerm expIndex, BQTerm expArray, String moduleName) throws IOException;
  protected abstract void buildExpEqualTerm(int deep, TomType type, BQTerm exp1,TomTerm exp2, String moduleName) throws IOException;
  protected abstract void buildExpEqualBQTerm(int deep, TomType type, BQTerm exp1,BQTerm exp2, String moduleName) throws IOException;
  protected abstract void buildExpIsSort(int deep, String type, BQTerm exp, String moduleName) throws IOException;
  protected abstract void buildExpIsFsym(int deep, String opname, BQTerm var, String moduleName) throws IOException;
  protected abstract void buildExpCast(int deep, TargetLanguageType tlType, Expression exp, String moduleName) throws IOException;
  protected abstract void buildExpGetSlot(int deep, String opname, String slotName, BQTerm exp, String moduleName) throws IOException;
  protected abstract void buildExpGetHead(int deep, String opName, TomType domain, TomType codomain, BQTerm var, String moduleName) throws IOException;
  protected abstract void buildExpGetTail(int deep, String opName, TomType type1, BQTerm var, String moduleName) throws IOException;
  protected abstract void buildExpGetSize(int deep, TomName opNameAST, TomType type1, BQTerm var, String moduleName) throws IOException;
  protected abstract void buildExpGetElement(int deep, TomName opNameAST, TomType domain, BQTerm varName, BQTerm varIndex, String moduleName) throws IOException;
  protected abstract void buildExpGetSliceList(int deep, String name, BQTerm varBegin, BQTerm varEnd, BQTerm tailSlice, String moduleName) throws IOException;
  protected abstract void buildExpGetSliceArray(int deep, String name, BQTerm varArray, BQTerm varBegin, BQTerm expEnd, String moduleName) throws IOException;
  protected abstract void buildAssign(int deep, BQTerm var, OptionList optionList, Expression exp, String moduleName) throws IOException ;
  protected abstract void buildAssignArray(int deep, BQTerm var, OptionList
      optionList, BQTerm index, Expression exp, String moduleName) throws IOException ;
  protected abstract void buildLet(int deep, BQTerm var, OptionList optionList, TargetLanguageType tlType, Expression exp, Instruction body, String moduleName) throws IOException ;
  protected abstract void buildLetRef(int deep, BQTerm var, OptionList optionList, TargetLanguageType tlType, Expression exp, Instruction body, String moduleName) throws IOException ;
  protected abstract void buildNamedBlock(int deep, String blockName, InstructionList instList, String modulename) throws IOException ;
  protected abstract void buildUnamedBlock(int deep, InstructionList instList, String moduleName) throws IOException ;
  protected abstract void buildIf(int deep, Expression exp, Instruction succes, String moduleName) throws IOException ;
  protected abstract void buildIfWithFailure(int deep, Expression exp, Instruction succes, Instruction failure, String moduleName) throws IOException ;
  protected abstract void buildDoWhile(int deep, Instruction succes, Expression exp, String moduleName) throws IOException;
  protected abstract void buildWhileDo(int deep, Expression exp, Instruction succes, String moduleName) throws IOException;
  protected abstract void buildAddOne(int deep, BQTerm var, String moduleName) throws IOException;
  protected abstract void buildSubstractOne(int deep, BQTerm var, String moduleName) throws IOException;
  protected abstract void buildSubstract(int deep, BQTerm var1, BQTerm var2, String moduleName) throws IOException;
  protected abstract void buildReturn(int deep, BQTerm exp, String moduleName) throws IOException ;
  protected abstract void buildSymbolDecl(int deep, String tomName, String moduleName) throws IOException ;

  protected abstract void buildIsFsymDecl(int deep, String tomName, String name1,
      TargetLanguageType tlType, Expression code, String moduleName) throws IOException;
  protected abstract void buildGetSlotDecl(int deep, String tomName, String name1,
      TargetLanguageType tlType, Expression code, TomName slotName, String moduleName) throws IOException;
  protected abstract void buildGetDefaultDecl(int deep, String tomName, 
       Expression code, TomName slotName, String moduleName) throws IOException;
  protected abstract void buildEqualTermDecl(int deep, String name1, String name2, String type1, String type2, Expression code, String moduleName) throws IOException;
  protected abstract void buildIsSortDecl(int deep, String name1, 
      String type1, Expression expr, String moduleName) throws IOException;
  protected abstract void buildGetHeadDecl(int deep, TomName opNameAST, String varName, String suffix, TargetLanguageType domain, TargetLanguageType codomain, Expression code, String moduleName) throws IOException;
  protected abstract void buildGetTailDecl(int deep, TomName opNameAST, String varName, String type, TargetLanguageType tlType, Expression code, String moduleName) throws IOException;
  protected abstract void buildIsEmptyDecl(int deep, TomName opNameAST, String varName, String type,
      TargetLanguageType tlType, Expression code, String moduleName) throws IOException;
  protected abstract void buildGetElementDecl(int deep, TomName opNameAST, String name1, String name2, String type1, Expression code, String moduleName) throws IOException;
  protected abstract void buildGetSizeDecl(int deep, TomName opNameAST, String name1, String type, Expression code, String moduleName) throws IOException;

  
  
  protected abstract void buildResolveIsFsymDecl(int deep, String tomName, String name1, TargetLanguageType tlType, String moduleName) throws IOException;
  protected abstract void buildResolveGetSlotDecl(int deep, String tomName, String name1, TargetLanguageType tlType, TomName slotName, String moduleName) throws IOException;
  protected abstract void buildResolveClass(String wName, String tName, String extendsName, String moduleName) throws IOException;
  protected abstract void buildResolveInverseLinks(int deep, String fileFrom, String fileTo, TomNameList resolveNameList, String moduleName) throws IOException;
  protected abstract void buildReferenceClass(int deep, String refname, RefClassTracelinkInstructionList refclassTInstructions, String moduleName) throws IOException;
  protected abstract void buildTracelink(int deep, String type, String name, Expression expr, String moduleName) throws IOException;
  protected abstract void buildResolve(int deep, BQTerm bqterm, String moduleName) throws IOException;
  protected abstract void buildTracelinkPopulateResolve(int deep, String refClassName, TomNameList tracedLinks, BQTerm current, BQTerm link, String moduleName) throws IOException;
  protected abstract void genResolveDeclMake(String prefix, String funName, TomType returnType, BQTermList argList, String moduleName) throws IOException;
  protected abstract String genResolveMakeCode(String funName, BQTermList argList) throws IOException;
  protected abstract String genResolveIsFsymCode(String tomName, String varname) throws IOException;
  protected abstract String genResolveGetSlotCode(String tomName, String varname, String slotName) throws IOException;



}
