/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2009, INRIA
 * Nancy, France.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 * 
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

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

public abstract class TomAbstractGenerator {

  protected OutputCode output;
  protected OptionManager optionManager;
  protected SymbolTable symbolTable;
  protected boolean prettyMode;

  public TomAbstractGenerator(OutputCode output, OptionManager optionManager,
                              SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
    this.optionManager = optionManager;
    this.output = output;
    this.prettyMode = ((Boolean)optionManager.getOptionValue("pretty")).booleanValue();
  }

  protected SymbolTable getSymbolTable(String moduleName) {
    //TODO//
    //Using of the moduleName
    ////////
    return symbolTable;
  }

  protected TomSymbol getSymbolFromName(String tomName) {
    return TomBase.getSymbolFromName(tomName, symbolTable);
  }

  protected TomSymbol getSymbolFromType(TomType tomType) {
    return TomBase.getSymbolFromType(tomType, symbolTable);
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
// ------------------------------------------------------------
        private static   tom.engine.adt.code.types.BQTermList  tom_append_list_concBQTerm( tom.engine.adt.code.types.BQTermList l1,  tom.engine.adt.code.types.BQTermList  l2) {     if( l1.isEmptyconcBQTerm() ) {       return l2;     } else if( l2.isEmptyconcBQTerm() ) {       return l1;     } else if(  l1.getTailconcBQTerm() .isEmptyconcBQTerm() ) {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,tom_append_list_concBQTerm( l1.getTailconcBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTermList  tom_get_slice_concBQTerm( tom.engine.adt.code.types.BQTermList  begin,  tom.engine.adt.code.types.BQTermList  end, tom.engine.adt.code.types.BQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcBQTerm()  ||  (end== tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( begin.getHeadconcBQTerm() ,( tom.engine.adt.code.types.BQTermList )tom_get_slice_concBQTerm( begin.getTailconcBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.code.types.BQTerm  tom_append_list_Composite( tom.engine.adt.code.types.BQTerm l1,  tom.engine.adt.code.types.BQTerm  l2) {     if( l1.isEmptyComposite() ) {       return l2;     } else if( l2.isEmptyComposite() ) {       return l1;     } else if(  l1.getTailComposite() .isEmptyComposite() ) {       return  tom.engine.adt.code.types.bqterm.ConsComposite.make( l1.getHeadComposite() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqterm.ConsComposite.make( l1.getHeadComposite() ,tom_append_list_Composite( l1.getTailComposite() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTerm  tom_get_slice_Composite( tom.engine.adt.code.types.BQTerm  begin,  tom.engine.adt.code.types.BQTerm  end, tom.engine.adt.code.types.BQTerm  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyComposite()  ||  (end== tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqterm.ConsComposite.make( begin.getHeadComposite() ,( tom.engine.adt.code.types.BQTerm )tom_get_slice_Composite( begin.getTailComposite() ,end,tail)) ;   }    
// ------------------------------------------------------------

  /**
   * Generate the goal language
   * 
   * @param deep 
   * 		The distance from the right side (allows the computation of the column number)
   */
  protected void generate(int deep, Code subject, String moduleName) throws IOException {
    {{if ( (subject instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )subject) instanceof tom.engine.adt.code.types.code.Tom) ) {


        generateList(deep, (( tom.engine.adt.code.types.Code )subject).getCodeList() , moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )subject) instanceof tom.engine.adt.code.types.code.TomInclude) ) {


        generateListInclude(deep, (( tom.engine.adt.code.types.Code )subject).getCodeList() , moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )subject) instanceof tom.engine.adt.code.types.code.BQTermToCode) ) {


        generateBQTerm(deep, (( tom.engine.adt.code.types.Code )subject).getBq() , moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )subject) instanceof tom.engine.adt.code.types.code.TargetLanguageToCode) ) {


        generateTargetLanguage(deep, (( tom.engine.adt.code.types.Code )subject).getTl() , moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )subject) instanceof tom.engine.adt.code.types.code.InstructionToCode) ) {


        generateInstruction(deep, (( tom.engine.adt.code.types.Code )subject).getAstInstruction() , moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )subject) instanceof tom.engine.adt.code.types.code.DeclarationToCode) ) {


        generateDeclaration(deep, (( tom.engine.adt.code.types.Code )subject).getAstDeclaration() , moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.code.types.Code) ) { tom.engine.adt.code.types.Code  tom_t=(( tom.engine.adt.code.types.Code )subject);


        System.out.println("Cannot generate code for: " + tom_t);
        throw new TomRuntimeException("Cannot generate code for: " + tom_t);
      }}}

  }
  
  /**
   * Generate the goal language
   * 
   * @param deep 
   * 		The distance from the right side (allows the computation of the column number)
   */
  protected void generateTomTerm(int deep, TomTerm subject, String moduleName) throws IOException {
    //TODO: complete with each constructor used in the baclend input term
    {{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch44_2= false ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {tomMatch44_2= true ;} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {tomMatch44_2= true ;}}if (tomMatch44_2) {

        output.write(deep,getVariableName((( tom.engine.adt.tomterm.types.TomTerm )subject)));
        return;
      }}}}
 
  }

  /**
   * Generate the goal language
   * 
   * @param deep 
   * 		The distance from the right side (allows the computation of the column number)
   */
  protected void generateBQTerm(int deep, BQTerm subject, String moduleName) throws IOException {
    {{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildConstant) ) { tom.engine.adt.tomname.types.TomName  tomMatch45_1= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ;if ( (tomMatch45_1 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch45_1.getString() ;

        if(tom_name.charAt(0)=='\'' && tom_name.charAt(tom_name.length()-1)=='\'') {
          String substring = tom_name.substring(1,tom_name.length()-1);
          //System.out.println("BuildConstant: " + substring);
          substring = substring.replace("\\","\\\\"); // replace backslash by backslash-backslash
          substring = substring.replace("'","\\'"); // replace quote by backslash-quote
          output.write("'" + substring + "'");
          return;
        }
        output.write(tom_name);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildTerm) ) { tom.engine.adt.tomname.types.TomName  tomMatch45_6= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ;if ( (tomMatch45_6 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {buildTerm(deep, tomMatch45_6.getString() , (( tom.engine.adt.code.types.BQTerm )subject).getArgs() , (( tom.engine.adt.code.types.BQTerm )subject).getModuleName() )


;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch45_14= false ;if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyList) ) {tomMatch45_14= true ;} else {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyArray) ) {tomMatch45_14= true ;} else {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildConsList) ) {tomMatch45_14= true ;} else {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildAppendList) ) {tomMatch45_14= true ;} else {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildConsArray) ) {tomMatch45_14= true ;} else {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildAppendArray) ) {tomMatch45_14= true ;}}}}}}if (tomMatch45_14) {


        buildListOrArray(deep, (( tom.engine.adt.code.types.BQTerm )subject), moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.FunctionCall) ) { tom.engine.adt.tomname.types.TomName  tomMatch45_16= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ;if ( (tomMatch45_16 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        buildFunctionCall(deep, tomMatch45_16.getString() ,  (( tom.engine.adt.code.types.BQTerm )subject).getArgs() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch45_23= false ;if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {tomMatch45_23= true ;} else {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {tomMatch45_23= true ;}}if (tomMatch45_23) {


        output.write(deep,getVariableName((( tom.engine.adt.code.types.BQTerm )subject)));
        return;
      }}}{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) {


        generateExpression(deep, (( tom.engine.adt.code.types.BQTerm )subject).getExp() , moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( (((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.ConsComposite) || ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.EmptyComposite)) ) { tom.engine.adt.code.types.BQTerm  tomMatch45__end__31=(( tom.engine.adt.code.types.BQTerm )subject);do {{if (!( tomMatch45__end__31.isEmptyComposite() )) { tom.engine.adt.code.types.CompositeMember  tom_t= tomMatch45__end__31.getHeadComposite() ;{{if ( (tom_t instanceof tom.engine.adt.code.types.CompositeMember) ) {if ( ((( tom.engine.adt.code.types.CompositeMember )tom_t) instanceof tom.engine.adt.code.types.compositemember.CompositeTL) ) {




            generateTargetLanguage(deep, (( tom.engine.adt.code.types.CompositeMember )tom_t).getTl() , moduleName);
          }}}{if ( (tom_t instanceof tom.engine.adt.code.types.CompositeMember) ) {if ( ((( tom.engine.adt.code.types.CompositeMember )tom_t) instanceof tom.engine.adt.code.types.compositemember.CompositeBQTerm) ) {

            generateBQTerm(deep, (( tom.engine.adt.code.types.CompositeMember )tom_t).getterm() , moduleName);
          }}}}

      }if ( tomMatch45__end__31.isEmptyComposite() ) {tomMatch45__end__31=(( tom.engine.adt.code.types.BQTerm )subject);} else {tomMatch45__end__31= tomMatch45__end__31.getTailComposite() ;}}} while(!( (tomMatch45__end__31==(( tom.engine.adt.code.types.BQTerm )subject)) ));}}}{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) { tom.engine.adt.code.types.BQTerm  tom_t=(( tom.engine.adt.code.types.BQTerm )subject);boolean tomMatch45_38= false ;if ( (((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.ConsComposite) || ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.EmptyComposite)) ) {if ( (tom_t==(( tom.engine.adt.code.types.BQTerm )subject)) ) {tomMatch45_38= true ;}}if (!(tomMatch45_38)) {

        throw new TomRuntimeException("Cannot generate code for bqterm "+tom_t);
      }}}}

  }

  protected String getVariableName(BQTerm var) {
    {{if ( (var instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )var) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch47_1= (( tom.engine.adt.code.types.BQTerm )var).getAstName() ;if ( (tomMatch47_1 instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {

        return ("tom" + TomBase.tomNumberListToString( tomMatch47_1.getNumberList() ));
      }}}}{if ( (var instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )var) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch47_6= (( tom.engine.adt.code.types.BQTerm )var).getAstName() ;if ( (tomMatch47_6 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        return  tomMatch47_6.getString() ;
      }}}}{if ( (var instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )var) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) { tom.engine.adt.tomname.types.TomName  tomMatch47_11= (( tom.engine.adt.code.types.BQTerm )var).getAstName() ;if ( (tomMatch47_11 instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {


        return ("tom" + TomBase.tomNumberListToString( tomMatch47_11.getNumberList() ));
      }}}}{if ( (var instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )var) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) { tom.engine.adt.tomname.types.TomName  tomMatch47_16= (( tom.engine.adt.code.types.BQTerm )var).getAstName() ;if ( (tomMatch47_16 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        return  tomMatch47_16.getString() ;
      }}}}}

    throw new RuntimeException("cannot generate the name of the variable "+var);
  }

  protected String getVariableName(TomTerm var) {
    {{if ( (var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch48_1= (( tom.engine.adt.tomterm.types.TomTerm )var).getAstName() ;if ( (tomMatch48_1 instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {

        return ("tom" + TomBase.tomNumberListToString( tomMatch48_1.getNumberList() ));
      }}}}{if ( (var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch48_6= (( tom.engine.adt.tomterm.types.TomTerm )var).getAstName() ;if ( (tomMatch48_6 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        return  tomMatch48_6.getString() ;
      }}}}{if ( (var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) { tom.engine.adt.tomname.types.TomName  tomMatch48_11= (( tom.engine.adt.tomterm.types.TomTerm )var).getAstName() ;if ( (tomMatch48_11 instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {


        return ("tom" + TomBase.tomNumberListToString( tomMatch48_11.getNumberList() ));
      }}}}{if ( (var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) { tom.engine.adt.tomname.types.TomName  tomMatch48_16= (( tom.engine.adt.tomterm.types.TomTerm )var).getAstName() ;if ( (tomMatch48_16 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        return  tomMatch48_16.getString() ;
      }}}}}

    throw new RuntimeException("cannot generate the name of the variable "+var);
  }

  public void generateExpression(int deep, Expression subject, String moduleName) throws IOException {
    {{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {

        output.write( (( tom.engine.adt.tomexpression.types.Expression )subject).getCode() );
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Integer) ) {


        output.write( (( tom.engine.adt.tomexpression.types.Expression )subject).getvalue() );
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Negation) ) {


        buildExpNegation(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg() , moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Conditional) ) {


        buildExpConditional(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getCond() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getThen() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getElse() , moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.And) ) {


        buildExpAnd(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() , moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Or) ) {


        buildExpOr(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() , moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GreaterThan) ) {


        buildExpGreaterThan(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() , moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GreaterOrEqualThan) ) {


        buildExpGreaterOrEqualThan(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() , moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.LessThan) ) {


        buildExpLessThan(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() , moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.LessOrEqualThan) ) {


        buildExpLessOrEqualThan(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() , moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Bottom) ) {



        buildExpBottom(deep, (( tom.engine.adt.tomexpression.types.Expression )subject).getTomType() , moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.TrueTL) ) {


        buildExpTrue(deep);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.FalseTL) ) {


        buildExpFalse(deep);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.IsEmptyList) ) { tom.engine.adt.tomname.types.TomName  tomMatch49_46= (( tom.engine.adt.tomexpression.types.Expression )subject).getOpname() ;if ( (tomMatch49_46 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.code.types.BQTerm  tom_expList= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;buildExpIsEmptyList(deep, tomMatch49_46.getString() ,getTermType(tom_expList),tom_expList,moduleName)


;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.IsEmptyArray) ) { tom.engine.adt.code.types.BQTerm  tom_expArray= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;


        buildExpIsEmptyArray(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getOpname() , getTermType(tom_expArray),  (( tom.engine.adt.tomexpression.types.Expression )subject).getIndex() , tom_expArray, moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.EqualTerm) ) {buildExpEqualTerm(deep, (( tom.engine.adt.tomexpression.types.Expression )subject).getTomType() , (( tom.engine.adt.tomexpression.types.Expression )subject).getKid1() , (( tom.engine.adt.tomexpression.types.Expression )subject).getKid2() ,moduleName)


;
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.EqualBQTerm) ) {buildExpEqualBQTerm(deep, (( tom.engine.adt.tomexpression.types.Expression )subject).getTomType() , (( tom.engine.adt.tomexpression.types.Expression )subject).gett1() , (( tom.engine.adt.tomexpression.types.Expression )subject).gett2() ,moduleName)

;
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.IsSort) ) { tom.engine.adt.tomtype.types.TomType  tomMatch49_67= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstType() ;boolean tomMatch49_72= false ; String  tomMatch49_70= "" ;if ( (tomMatch49_67 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {{tomMatch49_72= true ;tomMatch49_70= tomMatch49_67.getTomType() ;}} else {if ( (tomMatch49_67 instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {{tomMatch49_72= true ;tomMatch49_70= tomMatch49_67.getTomType() ;}}}if (tomMatch49_72) {buildExpIsSort(deep,tomMatch49_70, (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ,moduleName)

;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.IsFsym) ) { tom.engine.adt.tomname.types.TomName  tomMatch49_74= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() ;if ( (tomMatch49_74 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        buildExpIsFsym(deep,  tomMatch49_74.getString() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Cast) ) { tom.engine.adt.tomtype.types.TomType  tomMatch49_80= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstType() ;boolean tomMatch49_86= false ; tom.engine.adt.tomtype.types.TomType  tomMatch49_83= null ;if ( (tomMatch49_80 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {{tomMatch49_86= true ;tomMatch49_83= tomMatch49_80.getTlType() ;}} else {if ( (tomMatch49_80 instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {{tomMatch49_86= true ;tomMatch49_83= tomMatch49_80.getTlType() ;}}}if (tomMatch49_86) {if ( (tomMatch49_83 instanceof tom.engine.adt.tomtype.types.tomtype.TLType) ) {


        buildExpCast(deep, tomMatch49_83,  (( tom.engine.adt.tomexpression.types.Expression )subject).getSource() , moduleName);
        return;
      }}}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Cast) ) { tom.engine.adt.tomtype.types.TomType  tomMatch49_88= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstType() ;if ( (tomMatch49_88 instanceof tom.engine.adt.tomtype.types.tomtype.TLType) ) {


        buildExpCast(deep, tomMatch49_88,  (( tom.engine.adt.tomexpression.types.Expression )subject).getSource() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetSlot) ) { tom.engine.adt.tomname.types.TomName  tomMatch49_94= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() ; tom.engine.adt.code.types.BQTerm  tomMatch49_96= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;if ( (tomMatch49_94 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {boolean tomMatch49_101= false ;if ( (tomMatch49_96 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {tomMatch49_101= true ;} else {if ( (tomMatch49_96 instanceof tom.engine.adt.code.types.bqterm.BuildTerm) ) {tomMatch49_101= true ;} else {if ( (tomMatch49_96 instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) {tomMatch49_101= true ;}}}if (tomMatch49_101) {buildExpGetSlot(deep, tomMatch49_94.getString() , (( tom.engine.adt.tomexpression.types.Expression )subject).getSlotNameString() ,tomMatch49_96,moduleName)


;
        return;
      }}}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetSlot) ) { tom.engine.adt.tomname.types.TomName  tomMatch49_104= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() ; tom.engine.adt.code.types.BQTerm  tomMatch49_106= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;if ( (tomMatch49_104 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch49_106 instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) {if ( ( tomMatch49_106.getExp()  instanceof tom.engine.adt.tomexpression.types.expression.GetSlot) ) {buildExpGetSlot(deep, tomMatch49_104.getString() , (( tom.engine.adt.tomexpression.types.Expression )subject).getSlotNameString() ,tomMatch49_106,moduleName)

;
        return;
      }}}}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetHead) ) { tom.engine.adt.tomname.types.TomName  tomMatch49_114= (( tom.engine.adt.tomexpression.types.Expression )subject).getOpname() ;if ( (tomMatch49_114 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.code.types.BQTerm  tom_exp= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;buildExpGetHead(deep, tomMatch49_114.getString() ,getTermType(tom_exp), (( tom.engine.adt.tomexpression.types.Expression )subject).getCodomain() ,tom_exp,moduleName)


;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetTail) ) { tom.engine.adt.tomname.types.TomName  tomMatch49_121= (( tom.engine.adt.tomexpression.types.Expression )subject).getOpname() ;if ( (tomMatch49_121 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.code.types.BQTerm  tom_exp= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;buildExpGetTail(deep, tomMatch49_121.getString() ,getTermType(tom_exp),tom_exp,moduleName)


;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.AddOne) ) {


        buildAddOne(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() , moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.SubstractOne) ) {


        buildSubstractOne(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() , moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Substract) ) {


        buildSubstract(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getTerm1() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getTerm2() , moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetSize) ) { tom.engine.adt.code.types.BQTerm  tom_exp= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;


        buildExpGetSize(deep, (( tom.engine.adt.tomexpression.types.Expression )subject).getOpname() ,getTermType(tom_exp), tom_exp, moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetElement) ) { tom.engine.adt.code.types.BQTerm  tom_varName= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;


        buildExpGetElement(deep, (( tom.engine.adt.tomexpression.types.Expression )subject).getOpname() ,getTermType(tom_varName), (( tom.engine.adt.tomexpression.types.Expression )subject).getCodomain() , tom_varName,  (( tom.engine.adt.tomexpression.types.Expression )subject).getIndex() , moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetSliceList) ) { tom.engine.adt.tomname.types.TomName  tomMatch49_147= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() ;if ( (tomMatch49_147 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        buildExpGetSliceList(deep,  tomMatch49_147.getString() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableBeginAST() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableEndAST() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getTail() ,moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetSliceArray) ) { tom.engine.adt.tomname.types.TomName  tomMatch49_155= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() ;if ( (tomMatch49_155 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        buildExpGetSliceArray(deep,  tomMatch49_155.getString() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getSubjectListName() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableBeginAST() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableEndAST() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.BQTermToExpression) ) {


        generateBQTerm(deep, (( tom.engine.adt.tomexpression.types.Expression )subject).getAstTerm() , moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.TomInstructionToExpression) ) {


        generateInstruction(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getInstruction() , moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) { tom.engine.adt.tomexpression.types.Expression  tom_t=(( tom.engine.adt.tomexpression.types.Expression )subject);


        System.out.println("Cannot generate code for expression: " + tom_t);
        throw new TomRuntimeException("Cannot generate code for expression: " + tom_t);
      }}}

  }

  /**
   * generates var[index] 
   */
  protected void generateArray(int deep, BQTerm subject, BQTerm index, String moduleName) throws IOException {
    {{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch50_1= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ;if ( (tomMatch50_1 instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {
        
        output.write("tom" + TomBase.tomNumberListToString( tomMatch50_1.getNumberList() ));        
      }}}}{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch50_6= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ;if ( (tomMatch50_6 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

        output.write( tomMatch50_6.getString() );        
      }}}}}{{if ( (index instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )index) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch51_1= (( tom.engine.adt.code.types.BQTerm )index).getAstName() ;if ( (tomMatch51_1 instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {



        output.write("[");
        output.write("tom" + TomBase.tomNumberListToString( tomMatch51_1.getNumberList() ));
        output.write("]");        
      }}}}{if ( (index instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )index) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch51_6= (( tom.engine.adt.code.types.BQTerm )index).getAstName() ;if ( (tomMatch51_6 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

        output.write("[");
        output.write( tomMatch51_6.getString() );
        output.write("]");
      }}}}{if ( (index instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )index) instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) { tom.engine.adt.tomexpression.types.Expression  tomMatch51_11= (( tom.engine.adt.code.types.BQTerm )index).getExp() ;if ( (tomMatch51_11 instanceof tom.engine.adt.tomexpression.types.expression.Integer) ) {

        output.write("[");
        output.write( tomMatch51_11.getvalue() );
        output.write("]");  
      }}}}}
    
  } 

  public void generateInstruction(int deep, Instruction subject, String moduleName) throws IOException {
    {{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.CodeToInstruction) ) {generate(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getCode() ,moduleName)


;
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.BQTermToInstruction) ) {generateBQTerm(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getTom() ,moduleName)


;
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction) ) {generateExpression(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getExpr() ,moduleName)


;
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {


        return;
      }}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.Assign) ) { tom.engine.adt.code.types.BQTerm  tomMatch52_12= (( tom.engine.adt.tominstruction.types.Instruction )subject).getVariable() ;boolean tomMatch52_17= false ; tom.engine.adt.tomoption.types.OptionList  tomMatch52_15= null ;if ( (tomMatch52_12 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch52_17= true ;tomMatch52_15= tomMatch52_12.getOption() ;}} else {if ( (tomMatch52_12 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch52_17= true ;tomMatch52_15= tomMatch52_12.getOption() ;}}}if (tomMatch52_17) {buildAssign(deep,tomMatch52_12,tomMatch52_15, (( tom.engine.adt.tominstruction.types.Instruction )subject).getSource() ,moduleName)


;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.AssignArray) ) { tom.engine.adt.code.types.BQTerm  tomMatch52_19= (( tom.engine.adt.tominstruction.types.Instruction )subject).getVariable() ;if ( (tomMatch52_19 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {buildAssignArray(deep,tomMatch52_19, tomMatch52_19.getOption() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getIndex() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getSource() ,moduleName)









;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.Let) ) { tom.engine.adt.code.types.BQTerm  tomMatch52_26= (( tom.engine.adt.tominstruction.types.Instruction )subject).getVariable() ;boolean tomMatch52_36= false ; tom.engine.adt.tomoption.types.OptionList  tomMatch52_30= null ; tom.engine.adt.tomtype.types.TomType  tomMatch52_31= null ;if ( (tomMatch52_26 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch52_36= true ;tomMatch52_30= tomMatch52_26.getOption() ;tomMatch52_31= tomMatch52_26.getAstType() ;}} else {if ( (tomMatch52_26 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch52_36= true ;tomMatch52_30= tomMatch52_26.getOption() ;tomMatch52_31= tomMatch52_26.getAstType() ;}}}if (tomMatch52_36) {boolean tomMatch52_35= false ; tom.engine.adt.tomtype.types.TomType  tomMatch52_33= null ;if ( (tomMatch52_31 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {{tomMatch52_35= true ;tomMatch52_33= tomMatch52_31.getTlType() ;}} else {if ( (tomMatch52_31 instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {{tomMatch52_35= true ;tomMatch52_33= tomMatch52_31.getTlType() ;}}}if (tomMatch52_35) {buildLet(deep,tomMatch52_26,tomMatch52_30,tomMatch52_33, (( tom.engine.adt.tominstruction.types.Instruction )subject).getSource() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getAstInstruction() ,moduleName)










;
        return;
      }}}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.LetRef) ) { tom.engine.adt.code.types.BQTerm  tomMatch52_38= (( tom.engine.adt.tominstruction.types.Instruction )subject).getVariable() ;boolean tomMatch52_48= false ; tom.engine.adt.tomtype.types.TomType  tomMatch52_43= null ; tom.engine.adt.tomoption.types.OptionList  tomMatch52_42= null ;if ( (tomMatch52_38 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch52_48= true ;tomMatch52_42= tomMatch52_38.getOption() ;tomMatch52_43= tomMatch52_38.getAstType() ;}} else {if ( (tomMatch52_38 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch52_48= true ;tomMatch52_42= tomMatch52_38.getOption() ;tomMatch52_43= tomMatch52_38.getAstType() ;}}}if (tomMatch52_48) {boolean tomMatch52_47= false ; tom.engine.adt.tomtype.types.TomType  tomMatch52_45= null ;if ( (tomMatch52_43 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {{tomMatch52_47= true ;tomMatch52_45= tomMatch52_43.getTlType() ;}} else {if ( (tomMatch52_43 instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {{tomMatch52_47= true ;tomMatch52_45= tomMatch52_43.getTlType() ;}}}if (tomMatch52_47) {buildLetRef(deep,tomMatch52_38,tomMatch52_42,tomMatch52_45, (( tom.engine.adt.tominstruction.types.Instruction )subject).getSource() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getAstInstruction() ,moduleName)


;
        return;
      }}}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) {


        //`generateInstructionList(deep, instList);
        buildInstructionSequence(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getInstList() ,moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.UnamedBlock) ) {buildUnamedBlock(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getInstList() ,moduleName)


;
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.NamedBlock) ) {buildNamedBlock(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getBlockName() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getInstList() ,moduleName)


;
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {if ( ( (( tom.engine.adt.tominstruction.types.Instruction )subject).getFailureInst()  instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {buildIf(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getSuccesInst() ,moduleName)


;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {if ( ( (( tom.engine.adt.tominstruction.types.Instruction )subject).getSuccesInst()  instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {buildIf(deep, tom.engine.adt.tomexpression.types.expression.Negation.make( (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() ) , (( tom.engine.adt.tominstruction.types.Instruction )subject).getFailureInst() ,moduleName)


;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {buildIfWithFailure(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getSuccesInst() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getFailureInst() ,moduleName)


;
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.DoWhile) ) {buildDoWhile(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getDoInst() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() ,moduleName)


;
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.WhileDo) ) {buildWhileDo(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getDoInst() ,moduleName)


;
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.TypedAction) ) { tom.engine.adt.tominstruction.types.Instruction  tomMatch52_85= (( tom.engine.adt.tominstruction.types.Instruction )subject).getAstInstruction() ;if ( (tomMatch52_85 instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) {generateInstructionList(deep, tomMatch52_85.getInstList() ,moduleName)


;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.TypedAction) ) {generateInstruction(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getAstInstruction() ,moduleName)


;
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.Return) ) {buildReturn(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getKid1() ,moduleName)


;
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.CompiledMatch) ) {


        //TODO moduleName
        generateInstruction(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getAutomataInst() ,moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.CompiledPattern) ) {generateInstruction(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getAutomataInst() ,moduleName)


;
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) { tom.engine.adt.tominstruction.types.Instruction  tom_t=(( tom.engine.adt.tominstruction.types.Instruction )subject);


        System.out.println("Cannot generate code for instruction: " + tom_t);
        throw new TomRuntimeException("Cannot generate code for instruction: " + tom_t);
      }}}

  }

  public void generateTargetLanguage(int deep, TargetLanguage subject, String moduleName) throws IOException {
    {{if ( (subject instanceof tom.engine.adt.tomsignature.types.TargetLanguage) ) {if ( ((( tom.engine.adt.tomsignature.types.TargetLanguage )subject) instanceof tom.engine.adt.tomsignature.types.targetlanguage.TL) ) { tom.engine.adt.tomsignature.types.TextPosition  tomMatch53_2= (( tom.engine.adt.tomsignature.types.TargetLanguage )subject).getStart() ; tom.engine.adt.tomsignature.types.TextPosition  tomMatch53_3= (( tom.engine.adt.tomsignature.types.TargetLanguage )subject).getEnd() ;if ( (tomMatch53_2 instanceof tom.engine.adt.tomsignature.types.textposition.TextPosition) ) { int  tom_startLine= tomMatch53_2.getLine() ;if ( (tomMatch53_3 instanceof tom.engine.adt.tomsignature.types.textposition.TextPosition) ) {

        output.write(deep,  (( tom.engine.adt.tomsignature.types.TargetLanguage )subject).getCode() , tom_startLine,  tomMatch53_3.getLine() - tom_startLine);
        return;
      }}}}}{if ( (subject instanceof tom.engine.adt.tomsignature.types.TargetLanguage) ) {if ( ((( tom.engine.adt.tomsignature.types.TargetLanguage )subject) instanceof tom.engine.adt.tomsignature.types.targetlanguage.ITL) ) {

        output.write( (( tom.engine.adt.tomsignature.types.TargetLanguage )subject).getCode() );
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomsignature.types.TargetLanguage) ) {if ( ((( tom.engine.adt.tomsignature.types.TargetLanguage )subject) instanceof tom.engine.adt.tomsignature.types.targetlanguage.Comment) ) {buildComment(deep, (( tom.engine.adt.tomsignature.types.TargetLanguage )subject).getCode() )

;
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomsignature.types.TargetLanguage) ) { tom.engine.adt.tomsignature.types.TargetLanguage  tom_t=(( tom.engine.adt.tomsignature.types.TargetLanguage )subject);

        System.out.println("Cannot generate code for TL: " + tom_t);
        throw new TomRuntimeException("Cannot generate code for TL: " + tom_t);
      }}}

  }

  public void generateOption(int deep, Option subject, String moduleName) throws IOException {
    {{if ( (subject instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )subject) instanceof tom.engine.adt.tomoption.types.option.DeclarationToOption) ) {generateDeclaration(deep, (( tom.engine.adt.tomoption.types.Option )subject).getAstDeclaration() ,moduleName)

;
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )subject) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {
 return; }}}{if ( (subject instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )subject) instanceof tom.engine.adt.tomoption.types.option.ACSymbol) ) {

        // TODO RK: here add the declarations for intarray
        return; 
      }}}{if ( (subject instanceof tom.engine.adt.tomoption.types.Option) ) {

        throw new TomRuntimeException("Cannot generate code for option: " + (( tom.engine.adt.tomoption.types.Option )subject));
      }}}

  }

  public void generateDeclaration(int deep, Declaration subject, String moduleName) throws IOException {
    {{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.EmptyDeclaration) ) {

        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.AbstractDecl) ) {


        //`generateInstructionList(deep, instList);
        generateDeclarationList(deep, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getDeclList() ,moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.FunctionDef) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_6= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( (tomMatch55_6 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {buildFunctionDef(deep, tomMatch55_6.getString() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getArgumentList() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getCodomain() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getThrowsType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstruction() ,moduleName)


;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.MethodDef) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_15= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( (tomMatch55_15 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {buildMethodDef(deep, tomMatch55_15.getString() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getArgumentList() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getCodomain() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getThrowsType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstruction() ,moduleName)


;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.Class) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_24= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( (tomMatch55_24 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {buildClass(deep, tomMatch55_24.getString() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExtendsType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getSuperTerm() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getDeclaration() ,moduleName)


;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.IntrospectorClass) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_32= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( (tomMatch55_32 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {buildIntrospectorClass(deep, tomMatch55_32.getString() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getDeclaration() ,moduleName)



;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.SymbolDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_38= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( (tomMatch55_38 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {buildSymbolDecl(deep, tomMatch55_38.getString() ,moduleName)


;
        return ;
      }}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.ArraySymbolDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_43= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( (tomMatch55_43 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_tomName= tomMatch55_43.getString() ;


        if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom_tomName) 
            ||getSymbolTable(moduleName).isUsedSymbolDestructor(tom_tomName)) {
          buildSymbolDecl(deep,tom_tomName,moduleName);
          genDeclArray(tom_tomName,moduleName);
        }
        return ;
      }}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {boolean tomMatch55_52= false ; tom.engine.adt.tomname.types.TomName  tomMatch55_48= null ;if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.ListSymbolDecl) ) {{tomMatch55_52= true ;tomMatch55_48= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;}} else {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.ACSymbolDecl) ) {{tomMatch55_52= true ;tomMatch55_48= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;}}}if (tomMatch55_52) {if ( (tomMatch55_48 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_tomName= tomMatch55_48.getString() ;


        if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom_tomName) 
            ||getSymbolTable(moduleName).isUsedSymbolDestructor(tom_tomName)) {
          buildSymbolDecl(deep,tom_tomName,moduleName);
          genDeclList(tom_tomName,moduleName);
        }
        return ;
      }}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetImplementationDecl) ) { tom.engine.adt.code.types.BQTerm  tomMatch55_54= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;if ( (tomMatch55_54 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_58= tomMatch55_54.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch55_59= tomMatch55_54.getAstType() ;if ( (tomMatch55_58 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch55_58.getString() ;if ( (tomMatch55_59 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TomType  tomMatch55_64= tomMatch55_59.getTlType() ;if ( (tomMatch55_64 instanceof tom.engine.adt.tomtype.types.tomtype.TLType) ) {




        if(getSymbolTable(moduleName).isUsedSymbolDestructor(tom_name)) {
          buildGetImplementationDecl(deep, tomMatch55_59.getTomType() ,tom_name,tomMatch55_64, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstr() ,moduleName);
        }
        return;
      }}}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsFsymDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_68= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ; tom.engine.adt.code.types.BQTerm  tomMatch55_69= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;if ( (tomMatch55_68 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_tomName= tomMatch55_68.getString() ;if ( (tomMatch55_69 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_75= tomMatch55_69.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch55_76= tomMatch55_69.getAstType() ;if ( (tomMatch55_75 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch55_76 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TomType  tomMatch55_80= tomMatch55_76.getTlType() ;if ( (tomMatch55_80 instanceof tom.engine.adt.tomtype.types.tomtype.TLType) ) {



        if(getSymbolTable(moduleName).isUsedSymbolDestructor(tom_tomName)) {
          buildIsFsymDecl(deep,tom_tomName, tomMatch55_75.getString() ,tomMatch55_80, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName);
        }
        return;
      }}}}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetSlotDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_84= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ; tom.engine.adt.code.types.BQTerm  tomMatch55_86= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;if ( (tomMatch55_84 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_tomName= tomMatch55_84.getString() ;if ( (tomMatch55_86 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_91= tomMatch55_86.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch55_92= tomMatch55_86.getAstType() ;if ( (tomMatch55_91 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch55_92 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TomType  tomMatch55_96= tomMatch55_92.getTlType() ;if ( (tomMatch55_96 instanceof tom.engine.adt.tomtype.types.tomtype.TLType) ) {





          if(getSymbolTable(moduleName).isUsedSymbolDestructor(tom_tomName)) {
            buildGetSlotDecl(deep,tom_tomName, tomMatch55_91.getString() ,tomMatch55_96, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getSlotName() ,moduleName);
          }
          return;
        }}}}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.EqualTermDecl) ) { tom.engine.adt.code.types.BQTerm  tomMatch55_100= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getTermArg1() ; tom.engine.adt.code.types.BQTerm  tomMatch55_101= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getTermArg2() ;if ( (tomMatch55_100 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_105= tomMatch55_100.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch55_106= tomMatch55_100.getAstType() ;if ( (tomMatch55_105 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch55_106 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tom_type1= tomMatch55_106.getTomType() ;if ( (tomMatch55_101 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_113= tomMatch55_101.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch55_114= tomMatch55_101.getAstType() ;if ( (tomMatch55_113 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch55_114 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {




        if(getSymbolTable(moduleName).isUsedType(tom_type1)) {
          buildEqualTermDecl(deep, tomMatch55_105.getString() , tomMatch55_113.getString() ,tom_type1, tomMatch55_114.getTomType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName);
        }
        return;
      }}}}}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsSortDecl) ) { tom.engine.adt.code.types.BQTerm  tomMatch55_122= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getTermArg() ;if ( (tomMatch55_122 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_126= tomMatch55_122.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch55_127= tomMatch55_122.getAstType() ;if ( (tomMatch55_126 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch55_127 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tom_type= tomMatch55_127.getTomType() ;


        if(getSymbolTable(moduleName).isUsedType(tom_type)) {
          buildIsSortDecl(deep, tomMatch55_126.getString() ,tom_type, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName);
        }
        return;
      }}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetHeadDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_135= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getOpname() ; tom.engine.adt.tomtype.types.TomType  tomMatch55_136= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getCodomain() ; tom.engine.adt.code.types.BQTerm  tomMatch55_137= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;if ( (tomMatch55_135 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_opname= tomMatch55_135.getString() ;if ( (tomMatch55_136 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( (tomMatch55_137 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_144= tomMatch55_137.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch55_145= tomMatch55_137.getAstType() ;if ( (tomMatch55_144 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch55_145 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TomType  tomMatch55_150= tomMatch55_145.getTlType() ;if ( (tomMatch55_150 instanceof tom.engine.adt.tomtype.types.tomtype.TLType) ) {





          if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom_opname) 
              ||getSymbolTable(moduleName).isUsedSymbolDestructor(tom_opname)) {
            buildGetHeadDecl(deep,tomMatch55_135, tomMatch55_144.getString() , tomMatch55_145.getTomType() ,tomMatch55_150, tomMatch55_136.getTlType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName);
          }
          return;
        }}}}}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetTailDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_154= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getOpname() ; tom.engine.adt.code.types.BQTerm  tomMatch55_155= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;if ( (tomMatch55_154 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_opname= tomMatch55_154.getString() ;if ( (tomMatch55_155 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_160= tomMatch55_155.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch55_161= tomMatch55_155.getAstType() ;if ( (tomMatch55_160 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch55_161 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TomType  tomMatch55_166= tomMatch55_161.getTlType() ;if ( (tomMatch55_166 instanceof tom.engine.adt.tomtype.types.tomtype.TLType) ) {




          if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom_opname) 
              ||getSymbolTable(moduleName).isUsedSymbolDestructor(tom_opname)) {
            buildGetTailDecl(deep,tomMatch55_154, tomMatch55_160.getString() , tomMatch55_161.getTomType() ,tomMatch55_166, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName);
          }
          return;
        }}}}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsEmptyDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_170= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getOpname() ; tom.engine.adt.code.types.BQTerm  tomMatch55_171= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;if ( (tomMatch55_170 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_opname= tomMatch55_170.getString() ;if ( (tomMatch55_171 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_176= tomMatch55_171.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch55_177= tomMatch55_171.getAstType() ;if ( (tomMatch55_176 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch55_177 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TomType  tomMatch55_182= tomMatch55_177.getTlType() ;if ( (tomMatch55_182 instanceof tom.engine.adt.tomtype.types.tomtype.TLType) ) {




          if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom_opname) 
              ||getSymbolTable(moduleName).isUsedSymbolDestructor(tom_opname)) {
            buildIsEmptyDecl(deep,tomMatch55_170, tomMatch55_176.getString() , tomMatch55_177.getTomType() ,tomMatch55_182, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName);
          }
          return;
        }}}}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyList) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_186= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( (tomMatch55_186 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_opname= tomMatch55_186.getString() ;


        TomType returnType = TomBase.getSymbolCodomain(getSymbolFromName(tom_opname));
        if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom_opname) 
            || getSymbolTable(moduleName).isUsedSymbolDestructor(tom_opname)) {
          genDeclMake("tom_empty_list_",tom_opname,returnType, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstr() ,moduleName);
        }
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeAddList) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_193= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ; tom.engine.adt.code.types.BQTerm  tomMatch55_194= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVarElt() ; tom.engine.adt.code.types.BQTerm  tomMatch55_195= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVarList() ;if ( (tomMatch55_193 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_opname= tomMatch55_193.getString() ;if ( (tomMatch55_194 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomtype.types.TomType  tomMatch55_201= tomMatch55_194.getAstType() ;if ( (tomMatch55_201 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( ( tomMatch55_201.getTlType()  instanceof tom.engine.adt.tomtype.types.tomtype.TLType) ) {if ( (tomMatch55_195 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomtype.types.TomType  tomMatch55_206= tomMatch55_195.getAstType() ;if ( (tomMatch55_206 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( ( tomMatch55_206.getTlType()  instanceof tom.engine.adt.tomtype.types.tomtype.TLType) ) {





        TomType returnType = tomMatch55_206;
        if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom_opname) 
            ||getSymbolTable(moduleName).isUsedSymbolDestructor(tom_opname)) {
          genDeclMake("tom_cons_list_",tom_opname,returnType, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(tomMatch55_194, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(tomMatch55_195, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstr() ,moduleName);
        }
        return;
      }}}}}}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetElementDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_212= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getOpname() ; tom.engine.adt.code.types.BQTerm  tomMatch55_213= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ; tom.engine.adt.code.types.BQTerm  tomMatch55_214= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getIndex() ;if ( (tomMatch55_212 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch55_213 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_219= tomMatch55_213.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch55_220= tomMatch55_213.getAstType() ;if ( (tomMatch55_219 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch55_220 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TomType  tomMatch55_225= tomMatch55_220.getTlType() ;if ( (tomMatch55_225 instanceof tom.engine.adt.tomtype.types.tomtype.TLType) ) {if ( (tomMatch55_214 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_228= tomMatch55_214.getAstName() ;if ( (tomMatch55_228 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {





          if(getSymbolTable(moduleName).isUsedSymbolDestructor( tomMatch55_212.getString() )) {
            buildGetElementDecl(deep,tomMatch55_212, tomMatch55_219.getString() , tomMatch55_228.getString() , tomMatch55_220.getTomType() ,tomMatch55_225, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName);
          }
          return;
        }}}}}}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetSizeDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_233= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getOpname() ; tom.engine.adt.code.types.BQTerm  tomMatch55_234= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;if ( (tomMatch55_233 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch55_234 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_239= tomMatch55_234.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch55_240= tomMatch55_234.getAstType() ;if ( (tomMatch55_239 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch55_240 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TomType  tomMatch55_245= tomMatch55_240.getTlType() ;if ( (tomMatch55_245 instanceof tom.engine.adt.tomtype.types.tomtype.TLType) ) {





          if(getSymbolTable(moduleName).isUsedSymbolDestructor( tomMatch55_233.getString() )) {
            buildGetSizeDecl(deep,tomMatch55_233, tomMatch55_239.getString() , tomMatch55_240.getTomType() ,tomMatch55_245, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName);
          }
          return;
        }}}}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyArray) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_249= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ; tom.engine.adt.code.types.BQTerm  tomMatch55_250= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVarSize() ;if ( (tomMatch55_249 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_opname= tomMatch55_249.getString() ;if ( (tomMatch55_250 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {




        TomType returnType = TomBase.getSymbolCodomain(getSymbolFromName(tom_opname));
        BQTerm newVar =  tom.engine.adt.code.types.bqterm.BQVariable.make( tomMatch55_250.getOption() ,  tomMatch55_250.getAstName() , getSymbolTable(moduleName).getIntType()) ;
        if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom_opname)) {
          genDeclMake("tom_empty_array_",tom_opname,returnType, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(newVar, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstr() ,moduleName);
        }
        return;
      }}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeAddArray) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_260= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ; tom.engine.adt.code.types.BQTerm  tomMatch55_261= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVarElt() ; tom.engine.adt.code.types.BQTerm  tomMatch55_262= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVarList() ;if ( (tomMatch55_260 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_opname= tomMatch55_260.getString() ;if ( (tomMatch55_261 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomtype.types.TomType  tomMatch55_268= tomMatch55_261.getAstType() ;if ( (tomMatch55_268 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( ( tomMatch55_268.getTlType()  instanceof tom.engine.adt.tomtype.types.tomtype.TLType) ) {if ( (tomMatch55_262 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomtype.types.TomType  tomMatch55_273= tomMatch55_262.getAstType() ;if ( (tomMatch55_273 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( ( tomMatch55_273.getTlType()  instanceof tom.engine.adt.tomtype.types.tomtype.TLType) ) {





        TomType returnType = tomMatch55_273;
        if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom_opname)) {
          genDeclMake("tom_cons_array_",tom_opname,returnType, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(tomMatch55_261, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(tomMatch55_262, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstr() ,moduleName);
        }
        return;
      }}}}}}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch55_279= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( (tomMatch55_279 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_opname= tomMatch55_279.getString() ;


        if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom_opname)) {
          genDeclMake("tom_make_",tom_opname, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getArgs() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstr() ,moduleName);
        }
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.TypeTermDecl) ) {


        generateDeclarationList(deep,  (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getDeclarations() , moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) { tom.engine.adt.tomdeclaration.types.Declaration  tom_t=(( tom.engine.adt.tomdeclaration.types.Declaration )subject);


        System.out.println("Cannot generate code for declaration: " + tom_t);
        throw new TomRuntimeException("Cannot generate code for declaration: " + tom_t);
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
      if(prettyMode) {
        output.writeln();
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

  // ------------------------------------------------------------

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

  // ------------------------------------------------------------

  protected abstract void buildInstructionSequence(int deep, InstructionList instructionList, String moduleName) throws IOException;
  protected abstract void buildComment(int deep, String text) throws IOException;
  protected abstract void buildTerm(int deep, String name, BQTermList argList, String moduleName) throws IOException;
  protected abstract void buildListOrArray(int deep, BQTerm list, String moduleName) throws IOException;

  protected abstract void buildFunctionCall(int deep, String name, BQTermList argList, String moduleName)  throws IOException;
  protected abstract void buildFunctionDef(int deep, String tomName, BQTermList argList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName) throws IOException;
  protected void buildMethodDef(int deep, String tomName, BQTermList argList, TomType codomain, TomType throwsType, Instruction instruction, String moduleName) throws IOException {
    throw new TomRuntimeException("Backend "+getClass()+" does not support Methods");
  }

  /*buildClass is not abstract since only Java backend supports class
    only backends that supports Class should overload buildClass
   */
  protected void buildClass(int deep, String tomName, TomType extendsType, BQTerm superTerm, Declaration declaration, String moduleName) throws IOException {
    throw new TomRuntimeException("Backend does not support Class");
  }

  /*buildIntrospectorClass is not abstract since only Java backend supports class
    only backends that supports Class should overload buildIntrospectorClass
   */
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
  protected abstract void buildExpCast(int deep, TomType type, Expression exp, String moduleName) throws IOException;
  protected abstract void buildExpGetSlot(int deep, String opname, String slotName, BQTerm exp, String moduleName) throws IOException;
  protected abstract void buildExpGetHead(int deep, String opName, TomType domain, TomType codomain, BQTerm var, String moduleName) throws IOException;
  protected abstract void buildExpGetTail(int deep, String opName, TomType type1, BQTerm var, String moduleName) throws IOException;
  protected abstract void buildExpGetSize(int deep, TomName opNameAST, TomType type1, BQTerm var, String moduleName) throws IOException;
  protected abstract void buildExpGetElement(int deep, TomName opNameAST, TomType domain, TomType codomain, BQTerm varName, BQTerm varIndex, String moduleName) throws IOException;
  protected abstract void buildExpGetSliceList(int deep, String name, BQTerm varBegin, BQTerm varEnd, BQTerm tailSlice, String moduleName) throws IOException;
  protected abstract void buildExpGetSliceArray(int deep, String name, BQTerm varArray, BQTerm varBegin, BQTerm expEnd, String moduleName) throws IOException;
  protected abstract void buildAssign(int deep, BQTerm var, OptionList list, Expression exp, String moduleName) throws IOException ;
  protected abstract void buildAssignArray(int deep, BQTerm var, OptionList list, BQTerm index, Expression exp, String moduleName) throws IOException ;
  protected abstract void buildLet(int deep, BQTerm var, OptionList list, TomType tlType, Expression exp, Instruction body, String moduleName) throws IOException ;
  protected abstract void buildLetRef(int deep, BQTerm var, OptionList list, TomType tlType, Expression exp, Instruction body, String moduleName) throws IOException ;
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
  protected abstract void buildGetImplementationDecl(int deep, String type, String name,
      TomType tlType, Instruction instr, String moduleName) throws IOException;

  protected abstract void buildIsFsymDecl(int deep, String tomName, String name1,
      TomType tlType, Expression code, String moduleName) throws IOException;
  protected abstract void buildGetSlotDecl(int deep, String tomName, String name1,
      TomType tlType, Expression code, TomName slotName, String moduleName) throws IOException;
  protected abstract void buildEqualTermDecl(int deep, String name1, String name2, String type1, String type2, Expression code, String moduleName) throws IOException;
  protected abstract void buildIsSortDecl(int deep, String name1, 
      String type1, Expression expr, String moduleName) throws IOException;
  protected abstract void buildGetHeadDecl(int deep, TomName opNameAST, String varName, String suffix, TomType domain, TomType codomain, Expression code, String moduleName) throws IOException;
  protected abstract void buildGetTailDecl(int deep, TomName opNameAST, String varName, String type, TomType tlType, Expression code, String moduleName) throws IOException;
  protected abstract void buildIsEmptyDecl(int deep, TomName opNameAST, String varName, String type,
      TomType tlType, Expression code, String moduleName) throws IOException;
  protected abstract void buildGetElementDecl(int deep, TomName opNameAST, String name1, String name2, String type1, TomType tlType1, Expression code, String moduleName) throws IOException;
  protected abstract void buildGetSizeDecl(int deep, TomName opNameAST, String name1, String type, TomType tlType, Expression code, String moduleName) throws IOException;

} // class TomAbstractGenerator
