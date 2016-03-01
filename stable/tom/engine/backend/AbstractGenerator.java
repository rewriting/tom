/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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
    //TODO//
    //Using of the moduleName
    ////////
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
    {{if ( (subject instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )subject) instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )(( tom.engine.adt.code.types.Code )subject)) instanceof tom.engine.adt.code.types.code.Tom) ) {


        generateList(deep, (( tom.engine.adt.code.types.Code )subject).getCodeList() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )subject) instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )(( tom.engine.adt.code.types.Code )subject)) instanceof tom.engine.adt.code.types.code.TomInclude) ) {


        generateListInclude(deep, (( tom.engine.adt.code.types.Code )subject).getCodeList() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )subject) instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )(( tom.engine.adt.code.types.Code )subject)) instanceof tom.engine.adt.code.types.code.BQTermToCode) ) {


        generateBQTerm(deep, (( tom.engine.adt.code.types.Code )subject).getBq() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )subject) instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )(( tom.engine.adt.code.types.Code )subject)) instanceof tom.engine.adt.code.types.code.TargetLanguageToCode) ) {


        generateTargetLanguage(deep, (( tom.engine.adt.code.types.Code )subject).getTl() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )subject) instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )(( tom.engine.adt.code.types.Code )subject)) instanceof tom.engine.adt.code.types.code.InstructionToCode) ) {


        generateInstruction(deep, (( tom.engine.adt.code.types.Code )subject).getAstInstruction() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )subject) instanceof tom.engine.adt.code.types.Code) ) {if ( ((( tom.engine.adt.code.types.Code )(( tom.engine.adt.code.types.Code )subject)) instanceof tom.engine.adt.code.types.code.DeclarationToCode) ) {


        generateDeclaration(deep, (( tom.engine.adt.code.types.Code )subject).getAstDeclaration() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.code.types.Code) ) { tom.engine.adt.code.types.Code  tom_t=(( tom.engine.adt.code.types.Code )subject);


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
    {{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch61_4= false ; tom.engine.adt.tomterm.types.TomTerm  tomMatch61_3= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch61_2= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )subject)) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch61_4= true ;tomMatch61_2=(( tom.engine.adt.tomterm.types.TomTerm )subject);}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )subject)) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch61_4= true ;tomMatch61_3=(( tom.engine.adt.tomterm.types.TomTerm )subject);}}}}}if (tomMatch61_4) {

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
    {{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )subject)) instanceof tom.engine.adt.code.types.bqterm.BuildConstant) ) { tom.engine.adt.tomname.types.TomName  tomMatch62_1= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ;if ( (tomMatch62_1 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch62_1) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch62_1.getString() ;

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
      }}}}}}{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )subject)) instanceof tom.engine.adt.code.types.bqterm.BuildTerm) ) { tom.engine.adt.tomname.types.TomName  tomMatch62_8= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ;if ( (tomMatch62_8 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch62_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {buildTerm(deep, tomMatch62_8.getString() , (( tom.engine.adt.code.types.BQTerm )subject).getArgs() , (( tom.engine.adt.code.types.BQTerm )subject).getModuleName() )


;
        return;
      }}}}}}{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch62_24= false ; tom.engine.adt.code.types.BQTerm  tomMatch62_20= null ; tom.engine.adt.code.types.BQTerm  tomMatch62_18= null ; tom.engine.adt.code.types.BQTerm  tomMatch62_23= null ; tom.engine.adt.code.types.BQTerm  tomMatch62_21= null ; tom.engine.adt.code.types.BQTerm  tomMatch62_22= null ; tom.engine.adt.code.types.BQTerm  tomMatch62_19= null ;if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )subject)) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyList) ) {{tomMatch62_24= true ;tomMatch62_18=(( tom.engine.adt.code.types.BQTerm )subject);}} else {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )subject)) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyArray) ) {{tomMatch62_24= true ;tomMatch62_19=(( tom.engine.adt.code.types.BQTerm )subject);}} else {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )subject)) instanceof tom.engine.adt.code.types.bqterm.BuildConsList) ) {{tomMatch62_24= true ;tomMatch62_20=(( tom.engine.adt.code.types.BQTerm )subject);}} else {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )subject)) instanceof tom.engine.adt.code.types.bqterm.BuildAppendList) ) {{tomMatch62_24= true ;tomMatch62_21=(( tom.engine.adt.code.types.BQTerm )subject);}} else {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )subject)) instanceof tom.engine.adt.code.types.bqterm.BuildConsArray) ) {{tomMatch62_24= true ;tomMatch62_22=(( tom.engine.adt.code.types.BQTerm )subject);}} else {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )subject)) instanceof tom.engine.adt.code.types.bqterm.BuildAppendArray) ) {{tomMatch62_24= true ;tomMatch62_23=(( tom.engine.adt.code.types.BQTerm )subject);}}}}}}}}}}}}}if (tomMatch62_24) {


        buildListOrArray(deep, (( tom.engine.adt.code.types.BQTerm )subject), moduleName);
        return;
      }}}{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )subject)) instanceof tom.engine.adt.code.types.bqterm.FunctionCall) ) { tom.engine.adt.tomname.types.TomName  tomMatch62_26= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ;if ( (tomMatch62_26 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch62_26) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        buildFunctionCall(deep, tomMatch62_26.getString() ,  (( tom.engine.adt.code.types.BQTerm )subject).getArgs() , moduleName);
        return;
      }}}}}}{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {boolean tomMatch62_37= false ; tom.engine.adt.code.types.BQTerm  tomMatch62_35= null ; tom.engine.adt.code.types.BQTerm  tomMatch62_36= null ;if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )subject)) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch62_37= true ;tomMatch62_35=(( tom.engine.adt.code.types.BQTerm )subject);}} else {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )subject)) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch62_37= true ;tomMatch62_36=(( tom.engine.adt.code.types.BQTerm )subject);}}}}}if (tomMatch62_37) {


        output.write(deep,getVariableName((( tom.engine.adt.code.types.BQTerm )subject)));
        return;
      }}}{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )subject)) instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) {


        generateExpression(deep, (( tom.engine.adt.code.types.BQTerm )subject).getExp() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( (((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )subject)) instanceof tom.engine.adt.code.types.bqterm.ConsComposite) || ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )subject)) instanceof tom.engine.adt.code.types.bqterm.EmptyComposite)) ) { tom.engine.adt.code.types.BQTerm  tomMatch62_end_46=(( tom.engine.adt.code.types.BQTerm )subject);do {{if (!( tomMatch62_end_46.isEmptyComposite() )) { tom.engine.adt.code.types.CompositeMember  tom_t= tomMatch62_end_46.getHeadComposite() ;{{if ( (tom_t instanceof tom.engine.adt.code.types.CompositeMember) ) {if ( ((( tom.engine.adt.code.types.CompositeMember )tom_t) instanceof tom.engine.adt.code.types.CompositeMember) ) {if ( ((( tom.engine.adt.code.types.CompositeMember )(( tom.engine.adt.code.types.CompositeMember )tom_t)) instanceof tom.engine.adt.code.types.compositemember.CompositeTL) ) {




            generateTargetLanguage(deep, (( tom.engine.adt.code.types.CompositeMember )tom_t).getTl() , moduleName);
          }}}}{if ( (tom_t instanceof tom.engine.adt.code.types.CompositeMember) ) {if ( ((( tom.engine.adt.code.types.CompositeMember )tom_t) instanceof tom.engine.adt.code.types.CompositeMember) ) {if ( ((( tom.engine.adt.code.types.CompositeMember )(( tom.engine.adt.code.types.CompositeMember )tom_t)) instanceof tom.engine.adt.code.types.compositemember.CompositeBQTerm) ) {

            generateBQTerm(deep, (( tom.engine.adt.code.types.CompositeMember )tom_t).getterm() , moduleName);
          }}}}}

      }if ( tomMatch62_end_46.isEmptyComposite() ) {tomMatch62_end_46=(( tom.engine.adt.code.types.BQTerm )subject);} else {tomMatch62_end_46= tomMatch62_end_46.getTailComposite() ;}}} while(!( (tomMatch62_end_46==(( tom.engine.adt.code.types.BQTerm )subject)) ));}}}{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) { tom.engine.adt.code.types.BQTerm  tom_t=(( tom.engine.adt.code.types.BQTerm )subject);boolean tomMatch62_53= false ;if ( (((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )subject)) instanceof tom.engine.adt.code.types.bqterm.ConsComposite) || ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )subject)) instanceof tom.engine.adt.code.types.bqterm.EmptyComposite)) ) {if ( (tom_t==(( tom.engine.adt.code.types.BQTerm )subject)) ) {tomMatch62_53= true ;}}if (!(tomMatch62_53)) {


        throw new TomRuntimeException("Cannot generate code for bqterm "+tom_t);
      }}}}

  }

  protected String getVariableName(BQTerm var) {
    {{if ( (var instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )var) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )var)) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch64_1= (( tom.engine.adt.code.types.BQTerm )var).getAstName() ;if ( (tomMatch64_1 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch64_1) instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {

        return ("tom" + TomBase.tomNumberListToString( tomMatch64_1.getNumberList() ));
      }}}}}}{if ( (var instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )var) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )var)) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch64_8= (( tom.engine.adt.code.types.BQTerm )var).getAstName() ;if ( (tomMatch64_8 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch64_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        return  tomMatch64_8.getString() ;
      }}}}}}{if ( (var instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )var) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )var)) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) { tom.engine.adt.tomname.types.TomName  tomMatch64_15= (( tom.engine.adt.code.types.BQTerm )var).getAstName() ;if ( (tomMatch64_15 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch64_15) instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {


        return ("tom" + TomBase.tomNumberListToString( tomMatch64_15.getNumberList() ));
      }}}}}}{if ( (var instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )var) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )var)) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) { tom.engine.adt.tomname.types.TomName  tomMatch64_22= (( tom.engine.adt.code.types.BQTerm )var).getAstName() ;if ( (tomMatch64_22 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch64_22) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        return  tomMatch64_22.getString() ;
      }}}}}}}

    throw new RuntimeException("cannot generate the name of the variable "+var);
  }

  protected String getVariableName(TomTerm var) {
    {{if ( (var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )var)) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch65_1= (( tom.engine.adt.tomterm.types.TomTerm )var).getAstName() ;if ( (tomMatch65_1 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch65_1) instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {

        return ("tom" + TomBase.tomNumberListToString( tomMatch65_1.getNumberList() ));
      }}}}}}{if ( (var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )var)) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch65_8= (( tom.engine.adt.tomterm.types.TomTerm )var).getAstName() ;if ( (tomMatch65_8 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch65_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        return  tomMatch65_8.getString() ;
      }}}}}}{if ( (var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )var)) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) { tom.engine.adt.tomname.types.TomName  tomMatch65_15= (( tom.engine.adt.tomterm.types.TomTerm )var).getAstName() ;if ( (tomMatch65_15 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch65_15) instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {


        return ("tom" + TomBase.tomNumberListToString( tomMatch65_15.getNumberList() ));
      }}}}}}{if ( (var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )var)) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) { tom.engine.adt.tomname.types.TomName  tomMatch65_22= (( tom.engine.adt.tomterm.types.TomTerm )var).getAstName() ;if ( (tomMatch65_22 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch65_22) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        return  tomMatch65_22.getString() ;
      }}}}}}}

    throw new RuntimeException("cannot generate the name of the variable "+var);
  }

  public void generateExpression(int deep, Expression subject, String moduleName) throws IOException {
    {{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {

        output.write( (( tom.engine.adt.tomexpression.types.Expression )subject).getCode() );
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.Integer) ) {


        output.write( (( tom.engine.adt.tomexpression.types.Expression )subject).getvalue() );
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.Negation) ) {


        buildExpNegation(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.Conditional) ) {


        buildExpConditional(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getCond() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getThen() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getElse() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.And) ) {


        buildExpAnd(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.Or) ) {


        buildExpOr(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.GreaterThan) ) {


        buildExpGreaterThan(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.GreaterOrEqualThan) ) {


        buildExpGreaterOrEqualThan(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.LessThan) ) {


        buildExpLessThan(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.LessOrEqualThan) ) {


        buildExpLessOrEqualThan(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.Bottom) ) {



        buildExpBottom(deep, (( tom.engine.adt.tomexpression.types.Expression )subject).getTomType() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.TrueTL) ) {


        buildExpTrue(deep);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.FalseTL) ) {


        buildExpFalse(deep);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.IsEmptyList) ) { tom.engine.adt.tomname.types.TomName  tomMatch66_59= (( tom.engine.adt.tomexpression.types.Expression )subject).getOpname() ;if ( (tomMatch66_59 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch66_59) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.code.types.BQTerm  tom_expList= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;buildExpIsEmptyList(deep, tomMatch66_59.getString() ,getTermType(tom_expList),tom_expList,moduleName)


;
        return;
      }}}}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.IsEmptyArray) ) { tom.engine.adt.code.types.BQTerm  tom_expArray= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;


        buildExpIsEmptyArray(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getOpname() , getTermType(tom_expArray),  (( tom.engine.adt.tomexpression.types.Expression )subject).getIndex() , tom_expArray, moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.EqualTerm) ) {buildExpEqualTerm(deep, (( tom.engine.adt.tomexpression.types.Expression )subject).getTomType() , (( tom.engine.adt.tomexpression.types.Expression )subject).getKid1() , (( tom.engine.adt.tomexpression.types.Expression )subject).getKid2() ,moduleName)


;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.EqualBQTerm) ) {buildExpEqualBQTerm(deep, (( tom.engine.adt.tomexpression.types.Expression )subject).getTomType() , (( tom.engine.adt.tomexpression.types.Expression )subject).gett1() , (( tom.engine.adt.tomexpression.types.Expression )subject).gett2() ,moduleName)

;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.IsSort) ) { tom.engine.adt.tomtype.types.TomType  tomMatch66_85= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstType() ;if ( (tomMatch66_85 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch66_85) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {buildExpIsSort(deep, tomMatch66_85.getTomType() , (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ,moduleName)

;
        return;
      }}}}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.IsFsym) ) { tom.engine.adt.tomname.types.TomName  tomMatch66_93= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() ;if ( (tomMatch66_93 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch66_93) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        buildExpIsFsym(deep,  tomMatch66_93.getString() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() , moduleName);
        return;
      }}}}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.Cast) ) { tom.engine.adt.tomtype.types.TomType  tomMatch66_101= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstType() ;if ( (tomMatch66_101 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch66_101) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch66_105= tomMatch66_101.getTlType() ;if ( (tomMatch66_105 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch66_105) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {


        buildExpCast(deep, tomMatch66_105,  (( tom.engine.adt.tomexpression.types.Expression )subject).getSource() , moduleName);
        return;
      }}}}}}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.GetSlot) ) { tom.engine.adt.tomname.types.TomName  tomMatch66_112= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() ; tom.engine.adt.code.types.BQTerm  tomMatch66_114= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;if ( (tomMatch66_112 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch66_112) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {boolean tomMatch66_124= false ; tom.engine.adt.code.types.BQTerm  tomMatch66_123= null ; tom.engine.adt.code.types.BQTerm  tomMatch66_121= null ; tom.engine.adt.code.types.BQTerm  tomMatch66_122= null ;if ( (tomMatch66_114 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch66_114) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch66_124= true ;tomMatch66_121=tomMatch66_114;}} else {if ( (tomMatch66_114 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch66_114) instanceof tom.engine.adt.code.types.bqterm.BuildTerm) ) {{tomMatch66_124= true ;tomMatch66_122=tomMatch66_114;}} else {if ( (tomMatch66_114 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch66_114) instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) {{tomMatch66_124= true ;tomMatch66_123=tomMatch66_114;}}}}}}}if (tomMatch66_124) {buildExpGetSlot(deep, tomMatch66_112.getString() , (( tom.engine.adt.tomexpression.types.Expression )subject).getSlotNameString() ,tomMatch66_114,moduleName)


;
        return;
      }}}}}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.GetSlot) ) { tom.engine.adt.tomname.types.TomName  tomMatch66_127= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() ; tom.engine.adt.code.types.BQTerm  tomMatch66_129= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;if ( (tomMatch66_127 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch66_127) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch66_129 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch66_129) instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) { tom.engine.adt.tomexpression.types.Expression  tomMatch66_135= tomMatch66_129.getExp() ;if ( (tomMatch66_135 instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch66_135) instanceof tom.engine.adt.tomexpression.types.expression.GetSlot) ) {buildExpGetSlot(deep, tomMatch66_127.getString() , (( tom.engine.adt.tomexpression.types.Expression )subject).getSlotNameString() ,tomMatch66_129,moduleName)

;
        return;
      }}}}}}}}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.GetHead) ) { tom.engine.adt.tomname.types.TomName  tomMatch66_141= (( tom.engine.adt.tomexpression.types.Expression )subject).getOpname() ;if ( (tomMatch66_141 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch66_141) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.code.types.BQTerm  tom_exp= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;buildExpGetHead(deep, tomMatch66_141.getString() ,getTermType(tom_exp), (( tom.engine.adt.tomexpression.types.Expression )subject).getCodomain() ,tom_exp,moduleName)


;
        return;
      }}}}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.GetTail) ) { tom.engine.adt.tomname.types.TomName  tomMatch66_150= (( tom.engine.adt.tomexpression.types.Expression )subject).getOpname() ;if ( (tomMatch66_150 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch66_150) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.code.types.BQTerm  tom_exp= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;buildExpGetTail(deep, tomMatch66_150.getString() ,getTermType(tom_exp),tom_exp,moduleName)


;
        return;
      }}}}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.AddOne) ) {


        buildAddOne(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.SubstractOne) ) {


        buildSubstractOne(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.Substract) ) {


        buildSubstract(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getTerm1() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getTerm2() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.GetSize) ) { tom.engine.adt.code.types.BQTerm  tom_exp= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;


        buildExpGetSize(deep, (( tom.engine.adt.tomexpression.types.Expression )subject).getOpname() ,getTermType(tom_exp), tom_exp, moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.GetElement) ) { tom.engine.adt.code.types.BQTerm  tom_varName= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;


        buildExpGetElement(deep, (( tom.engine.adt.tomexpression.types.Expression )subject).getOpname() ,getTermType(tom_varName),tom_varName,  (( tom.engine.adt.tomexpression.types.Expression )subject).getIndex() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.GetSliceList) ) { tom.engine.adt.tomname.types.TomName  tomMatch66_183= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() ;if ( (tomMatch66_183 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch66_183) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        buildExpGetSliceList(deep,  tomMatch66_183.getString() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableBeginAST() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableEndAST() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getTail() ,moduleName);
        return;
      }}}}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.GetSliceArray) ) { tom.engine.adt.tomname.types.TomName  tomMatch66_193= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() ;if ( (tomMatch66_193 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch66_193) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        buildExpGetSliceArray(deep,  tomMatch66_193.getString() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getSubjectListName() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableBeginAST() ,  (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableEndAST() , moduleName);
        return;
      }}}}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.BQTermToExpression) ) {


        generateBQTerm(deep, (( tom.engine.adt.tomexpression.types.Expression )subject).getAstTerm() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )subject)) instanceof tom.engine.adt.tomexpression.types.expression.TomInstructionToExpression) ) {


        generateInstruction(deep,  (( tom.engine.adt.tomexpression.types.Expression )subject).getInstruction() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) { tom.engine.adt.tomexpression.types.Expression  tom_t=(( tom.engine.adt.tomexpression.types.Expression )subject);


        System.out.println("Cannot generate code for expression: " + tom_t);
        throw new TomRuntimeException("Cannot generate code for expression: " + tom_t);
      }}}

  }

  /**
   * generates var[index] 
   */
  protected void generateArray(int deep, BQTerm subject, BQTerm index, String moduleName) throws IOException {
    {{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )subject)) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch67_1= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ;if ( (tomMatch67_1 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch67_1) instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {
        
        output.write("tom" + TomBase.tomNumberListToString( tomMatch67_1.getNumberList() ));        
      }}}}}}{if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )subject)) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch67_8= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ;if ( (tomMatch67_8 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch67_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

        output.write( tomMatch67_8.getString() );        
      }}}}}}}{{if ( (index instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )index) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )index)) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch68_1= (( tom.engine.adt.code.types.BQTerm )index).getAstName() ;if ( (tomMatch68_1 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch68_1) instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {



        output.write("[");
        output.write("tom" + TomBase.tomNumberListToString( tomMatch68_1.getNumberList() ));
        output.write("]");        
      }}}}}}{if ( (index instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )index) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )index)) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch68_8= (( tom.engine.adt.code.types.BQTerm )index).getAstName() ;if ( (tomMatch68_8 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch68_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

        output.write("[");
        output.write( tomMatch68_8.getString() );
        output.write("]");
      }}}}}}{if ( (index instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )index) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )index)) instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) { tom.engine.adt.tomexpression.types.Expression  tomMatch68_15= (( tom.engine.adt.code.types.BQTerm )index).getExp() ;if ( (tomMatch68_15 instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch68_15) instanceof tom.engine.adt.tomexpression.types.expression.Integer) ) {

        output.write("[");
        output.write( tomMatch68_15.getvalue() );
        output.write("]");  
      }}}}}}}
    
  } 

  public void generateInstruction(int deep, Instruction subject, String moduleName) throws IOException {
    {{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )subject)) instanceof tom.engine.adt.tominstruction.types.instruction.CodeToInstruction) ) {generate(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getCode() ,moduleName)


;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )subject)) instanceof tom.engine.adt.tominstruction.types.instruction.BQTermToInstruction) ) {generateBQTerm(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getTom() ,moduleName)


;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )subject)) instanceof tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction) ) {generateExpression(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getExpr() ,moduleName)


;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )subject)) instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {


        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )subject)) instanceof tom.engine.adt.tominstruction.types.instruction.Assign) ) { tom.engine.adt.code.types.BQTerm  tomMatch69_16= (( tom.engine.adt.tominstruction.types.Instruction )subject).getVariable() ;boolean tomMatch69_24= false ; tom.engine.adt.tomoption.types.OptionList  tomMatch69_20= null ; tom.engine.adt.code.types.BQTerm  tomMatch69_23= null ; tom.engine.adt.code.types.BQTerm  tomMatch69_22= null ;if ( (tomMatch69_16 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch69_16) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch69_24= true ;tomMatch69_22=tomMatch69_16;tomMatch69_20= tomMatch69_22.getOptions() ;}} else {if ( (tomMatch69_16 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch69_16) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch69_24= true ;tomMatch69_23=tomMatch69_16;tomMatch69_20= tomMatch69_23.getOptions() ;}}}}}if (tomMatch69_24) {buildAssign(deep,tomMatch69_16,tomMatch69_20, (( tom.engine.adt.tominstruction.types.Instruction )subject).getSource() ,moduleName)


;
        return;
      }}}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )subject)) instanceof tom.engine.adt.tominstruction.types.instruction.AssignArray) ) { tom.engine.adt.code.types.BQTerm  tomMatch69_26= (( tom.engine.adt.tominstruction.types.Instruction )subject).getVariable() ;if ( (tomMatch69_26 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch69_26) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {buildAssignArray(deep,tomMatch69_26, tomMatch69_26.getOptions() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getIndex() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getSource() ,moduleName)


;
        return;
      }}}}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )subject)) instanceof tom.engine.adt.tominstruction.types.instruction.Let) ) { tom.engine.adt.code.types.BQTerm  tomMatch69_35= (( tom.engine.adt.tominstruction.types.Instruction )subject).getVariable() ;boolean tomMatch69_48= false ; tom.engine.adt.tomoption.types.OptionList  tomMatch69_40= null ; tom.engine.adt.tomtype.types.TomType  tomMatch69_41= null ; tom.engine.adt.code.types.BQTerm  tomMatch69_44= null ; tom.engine.adt.code.types.BQTerm  tomMatch69_43= null ;if ( (tomMatch69_35 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch69_35) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch69_48= true ;tomMatch69_43=tomMatch69_35;tomMatch69_40= tomMatch69_43.getOptions() ;tomMatch69_41= tomMatch69_43.getAstType() ;}} else {if ( (tomMatch69_35 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch69_35) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch69_48= true ;tomMatch69_44=tomMatch69_35;tomMatch69_40= tomMatch69_44.getOptions() ;tomMatch69_41= tomMatch69_44.getAstType() ;}}}}}if (tomMatch69_48) {if ( (tomMatch69_41 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch69_41) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {buildLet(deep,tomMatch69_35,tomMatch69_40, tomMatch69_41.getTlType() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getSource() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getAstInstruction() ,moduleName)


;
        return;
      }}}}}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )subject)) instanceof tom.engine.adt.tominstruction.types.instruction.LetRef) ) { tom.engine.adt.code.types.BQTerm  tomMatch69_50= (( tom.engine.adt.tominstruction.types.Instruction )subject).getVariable() ;boolean tomMatch69_63= false ; tom.engine.adt.code.types.BQTerm  tomMatch69_58= null ; tom.engine.adt.tomtype.types.TomType  tomMatch69_56= null ; tom.engine.adt.code.types.BQTerm  tomMatch69_59= null ; tom.engine.adt.tomoption.types.OptionList  tomMatch69_55= null ;if ( (tomMatch69_50 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch69_50) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {{tomMatch69_63= true ;tomMatch69_58=tomMatch69_50;tomMatch69_55= tomMatch69_58.getOptions() ;tomMatch69_56= tomMatch69_58.getAstType() ;}} else {if ( (tomMatch69_50 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch69_50) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {{tomMatch69_63= true ;tomMatch69_59=tomMatch69_50;tomMatch69_55= tomMatch69_59.getOptions() ;tomMatch69_56= tomMatch69_59.getAstType() ;}}}}}if (tomMatch69_63) {if ( (tomMatch69_56 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch69_56) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {buildLetRef(deep,tomMatch69_50,tomMatch69_55, tomMatch69_56.getTlType() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getSource() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getAstInstruction() ,moduleName)


;
        return;
      }}}}}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )subject)) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) {


        //`generateInstructionList(deep, instList);
        buildInstructionSequence(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getInstList() ,moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )subject)) instanceof tom.engine.adt.tominstruction.types.instruction.UnamedBlock) ) {buildUnamedBlock(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getInstList() ,moduleName)


;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )subject)) instanceof tom.engine.adt.tominstruction.types.instruction.NamedBlock) ) {buildNamedBlock(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getBlockName() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getInstList() ,moduleName)


;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )subject)) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) { tom.engine.adt.tominstruction.types.Instruction  tomMatch69_80= (( tom.engine.adt.tominstruction.types.Instruction )subject).getFailureInst() ;if ( (tomMatch69_80 instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tomMatch69_80) instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {buildIf(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getSuccesInst() ,moduleName)


;
        return;
      }}}}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )subject)) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) { tom.engine.adt.tominstruction.types.Instruction  tomMatch69_87= (( tom.engine.adt.tominstruction.types.Instruction )subject).getSuccesInst() ;if ( (tomMatch69_87 instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tomMatch69_87) instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {buildIf(deep, tom.engine.adt.tomexpression.types.expression.Negation.make( (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() ) , (( tom.engine.adt.tominstruction.types.Instruction )subject).getFailureInst() ,moduleName)


;
        return;
      }}}}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )subject)) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {buildIfWithFailure(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getSuccesInst() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getFailureInst() ,moduleName)


;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )subject)) instanceof tom.engine.adt.tominstruction.types.instruction.DoWhile) ) {buildDoWhile(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getDoInst() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() ,moduleName)


;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )subject)) instanceof tom.engine.adt.tominstruction.types.instruction.WhileDo) ) {buildWhileDo(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getDoInst() ,moduleName)


;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )subject)) instanceof tom.engine.adt.tominstruction.types.instruction.RawAction) ) { tom.engine.adt.tominstruction.types.Instruction  tomMatch69_110= (( tom.engine.adt.tominstruction.types.Instruction )subject).getAstInstruction() ;if ( (tomMatch69_110 instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tomMatch69_110) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) {generateInstructionList(deep, tomMatch69_110.getInstList() ,moduleName)


;
        return;
      }}}}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )subject)) instanceof tom.engine.adt.tominstruction.types.instruction.RawAction) ) {generateInstruction(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getAstInstruction() ,moduleName)


;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )subject)) instanceof tom.engine.adt.tominstruction.types.instruction.Return) ) {buildReturn(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getKid1() ,moduleName)


;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )subject)) instanceof tom.engine.adt.tominstruction.types.instruction.CompiledMatch) ) {


        //TODO moduleName
        generateInstruction(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getAutomataInst() ,moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )subject)) instanceof tom.engine.adt.tominstruction.types.instruction.CompiledPattern) ) {generateInstruction(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getAutomataInst() ,moduleName)


;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )subject)) instanceof tom.engine.adt.tominstruction.types.instruction.Tracelink) ) { tom.engine.adt.tomname.types.TomName  tomMatch69_135= (( tom.engine.adt.tominstruction.types.Instruction )subject).getType() ; tom.engine.adt.tomname.types.TomName  tomMatch69_136= (( tom.engine.adt.tominstruction.types.Instruction )subject).getName() ;if ( (tomMatch69_135 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch69_135) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch69_136 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch69_136) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {buildTracelink(deep, tomMatch69_135.getString() , tomMatch69_136.getString() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getExpr() ,moduleName)



;
        return;
      }}}}}}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )subject)) instanceof tom.engine.adt.tominstruction.types.instruction.TracelinkPopulateResolve) ) { tom.engine.adt.tomname.types.TomName  tomMatch69_149= (( tom.engine.adt.tominstruction.types.Instruction )subject).getRefClassName() ;if ( (tomMatch69_149 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch69_149) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {buildTracelinkPopulateResolve(deep, tomMatch69_149.getString() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getTracedLinks() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getCurrent() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getLink() ,moduleName)


;
        return;
      }}}}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )subject)) instanceof tom.engine.adt.tominstruction.types.instruction.Resolve) ) {



        //why?
        //buildResolve(deep, `bqterm, moduleName);
        generateBQTerm(deep,  (( tom.engine.adt.tominstruction.types.Instruction )subject).getResolveBQTerm() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) { tom.engine.adt.tominstruction.types.Instruction  tom_t=(( tom.engine.adt.tominstruction.types.Instruction )subject);


        System.out.println("Cannot generate code for instruction: " + tom_t);
        throw new TomRuntimeException("Cannot generate code for instruction: " + tom_t);
      }}}

  }

  public void generateTargetLanguage(int deep, TargetLanguage subject, String moduleName) throws IOException {
    {{if ( (subject instanceof tom.engine.adt.code.types.TargetLanguage) ) {if ( ((( tom.engine.adt.code.types.TargetLanguage )subject) instanceof tom.engine.adt.code.types.TargetLanguage) ) {if ( ((( tom.engine.adt.code.types.TargetLanguage )(( tom.engine.adt.code.types.TargetLanguage )subject)) instanceof tom.engine.adt.code.types.targetlanguage.TL) ) { tom.engine.adt.tomsignature.types.TextPosition  tomMatch70_2= (( tom.engine.adt.code.types.TargetLanguage )subject).getStart() ; tom.engine.adt.tomsignature.types.TextPosition  tomMatch70_3= (( tom.engine.adt.code.types.TargetLanguage )subject).getEnd() ;if ( (tomMatch70_2 instanceof tom.engine.adt.tomsignature.types.TextPosition) ) {if ( ((( tom.engine.adt.tomsignature.types.TextPosition )tomMatch70_2) instanceof tom.engine.adt.tomsignature.types.textposition.TextPosition) ) { int  tom_startLine= tomMatch70_2.getLine() ;if ( (tomMatch70_3 instanceof tom.engine.adt.tomsignature.types.TextPosition) ) {if ( ((( tom.engine.adt.tomsignature.types.TextPosition )tomMatch70_3) instanceof tom.engine.adt.tomsignature.types.textposition.TextPosition) ) {

        output.write(deep,  (( tom.engine.adt.code.types.TargetLanguage )subject).getCode() , tom_startLine,  tomMatch70_3.getLine() - tom_startLine);
        return;
      }}}}}}}}{if ( (subject instanceof tom.engine.adt.code.types.TargetLanguage) ) {if ( ((( tom.engine.adt.code.types.TargetLanguage )subject) instanceof tom.engine.adt.code.types.TargetLanguage) ) {if ( ((( tom.engine.adt.code.types.TargetLanguage )(( tom.engine.adt.code.types.TargetLanguage )subject)) instanceof tom.engine.adt.code.types.targetlanguage.ITL) ) {

        output.write( (( tom.engine.adt.code.types.TargetLanguage )subject).getCode() );
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.code.types.TargetLanguage) ) {if ( ((( tom.engine.adt.code.types.TargetLanguage )subject) instanceof tom.engine.adt.code.types.TargetLanguage) ) {if ( ((( tom.engine.adt.code.types.TargetLanguage )(( tom.engine.adt.code.types.TargetLanguage )subject)) instanceof tom.engine.adt.code.types.targetlanguage.Comment) ) {buildComment(deep, (( tom.engine.adt.code.types.TargetLanguage )subject).getCode() )

;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.code.types.TargetLanguage) ) { tom.engine.adt.code.types.TargetLanguage  tom_t=(( tom.engine.adt.code.types.TargetLanguage )subject);

        System.out.println("Cannot generate code for TL: " + tom_t);
        throw new TomRuntimeException("Cannot generate code for TL: " + tom_t);
      }}}

  }

  public void generateOption(int deep, Option subject, String moduleName) throws IOException {
    {{if ( (subject instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )subject) instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )(( tom.engine.adt.tomoption.types.Option )subject)) instanceof tom.engine.adt.tomoption.types.option.DeclarationToOption) ) {generateDeclaration(deep, (( tom.engine.adt.tomoption.types.Option )subject).getAstDeclaration() ,moduleName)

;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )subject) instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )(( tom.engine.adt.tomoption.types.Option )subject)) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {
 return; }}}}{if ( (subject instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )subject) instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )(( tom.engine.adt.tomoption.types.Option )subject)) instanceof tom.engine.adt.tomoption.types.option.ACSymbol) ) {

        // TODO RK: here add the declarations for intarray
        return; 
      }}}}{if ( (subject instanceof tom.engine.adt.tomoption.types.Option) ) {

        throw new TomRuntimeException("Cannot generate code for option: " + (( tom.engine.adt.tomoption.types.Option )subject));
      }}}

  }

  public void generateDeclaration(int deep, Declaration subject, String moduleName) throws IOException {
    {{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.EmptyDeclaration) ) {

        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.AbstractDecl) ) {


        //`generateInstructionList(deep, instList);
        generateDeclarationList(deep, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getDeclList() ,moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.FunctionDef) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_8= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( (tomMatch72_8 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {buildFunctionDef(deep, tomMatch72_8.getString() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getArgumentList() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getCodomain() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getThrowsType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstruction() ,moduleName)


;
        return;
      }}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.MethodDef) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_19= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( (tomMatch72_19 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_19) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {buildMethodDef(deep, tomMatch72_19.getString() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getArgumentList() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getCodomain() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getThrowsType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstruction() ,moduleName)


;
        return;
      }}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.Class) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_30= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( (tomMatch72_30 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_30) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {buildClass(deep, tomMatch72_30.getString() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExtendsType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getSuperTerm() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getDeclaration() ,moduleName)


;
        return;
      }}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.IntrospectorClass) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_40= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( (tomMatch72_40 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_40) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {buildIntrospectorClass(deep, tomMatch72_40.getString() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getDeclaration() ,moduleName)


;
        return;
      }}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.SymbolDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_48= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( (tomMatch72_48 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_48) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {buildSymbolDecl(deep, tomMatch72_48.getString() ,moduleName)


;
        return ;
      }}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.ArraySymbolDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_55= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( (tomMatch72_55 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_55) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_tomName= tomMatch72_55.getString() ;


        if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom_tomName) 
            ||getSymbolTable(moduleName).isUsedSymbolDestructor(tom_tomName)) {
          buildSymbolDecl(deep,tom_tomName,moduleName);
          genDeclArray(tom_tomName,moduleName);
        }
        return ;
      }}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {boolean tomMatch72_69= false ; tom.engine.adt.tomdeclaration.types.Declaration  tomMatch72_64= null ; tom.engine.adt.tomdeclaration.types.Declaration  tomMatch72_65= null ; tom.engine.adt.tomname.types.TomName  tomMatch72_62= null ;if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.ListSymbolDecl) ) {{tomMatch72_69= true ;tomMatch72_64=(( tom.engine.adt.tomdeclaration.types.Declaration )subject);tomMatch72_62= tomMatch72_64.getAstName() ;}} else {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.ACSymbolDecl) ) {{tomMatch72_69= true ;tomMatch72_65=(( tom.engine.adt.tomdeclaration.types.Declaration )subject);tomMatch72_62= tomMatch72_65.getAstName() ;}}}}}if (tomMatch72_69) {if ( (tomMatch72_62 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_62) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_tomName= tomMatch72_62.getString() ;


        if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom_tomName) 
            ||getSymbolTable(moduleName).isUsedSymbolDestructor(tom_tomName)) {
          buildSymbolDecl(deep,tom_tomName,moduleName);
          genDeclList(tom_tomName,moduleName);
        }
        return ;
      }}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsFsymDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_71= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ; tom.engine.adt.code.types.BQTerm  tomMatch72_72= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;if ( (tomMatch72_71 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_71) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_tomName= tomMatch72_71.getString() ;if ( (tomMatch72_72 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch72_72) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_80= tomMatch72_72.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch72_81= tomMatch72_72.getAstType() ;if ( (tomMatch72_80 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_80) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch72_81 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch72_81) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch72_87= tomMatch72_81.getTlType() ;if ( (tomMatch72_87 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch72_87) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {



        if(getSymbolTable(moduleName).isUsedSymbolDestructor(tom_tomName)) {
          buildIsFsymDecl(deep,tom_tomName, tomMatch72_80.getString() ,tomMatch72_87, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName);
        }
        return;
      }}}}}}}}}}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetSlotDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_93= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ; tom.engine.adt.code.types.BQTerm  tomMatch72_95= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;if ( (tomMatch72_93 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_93) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_tomName= tomMatch72_93.getString() ;if ( (tomMatch72_95 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch72_95) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_102= tomMatch72_95.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch72_103= tomMatch72_95.getAstType() ;if ( (tomMatch72_102 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_102) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch72_103 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch72_103) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch72_109= tomMatch72_103.getTlType() ;if ( (tomMatch72_109 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch72_109) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {





          if(getSymbolTable(moduleName).isUsedSymbolDestructor(tom_tomName)) {
            buildGetSlotDecl(deep,tom_tomName, tomMatch72_102.getString() ,tomMatch72_109, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getSlotName() ,moduleName);
          }
          return;
        }}}}}}}}}}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.ImplementDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_115= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( (tomMatch72_115 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_115) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {



        //nothing for the moment
        return;
      }}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetDefaultDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_123= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( (tomMatch72_123 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_123) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_tomName= tomMatch72_123.getString() ;


          if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom_tomName)) {
            buildGetDefaultDecl(deep,tom_tomName, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getSlotName() ,moduleName);
          }
          return;
        }}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.EqualTermDecl) ) { tom.engine.adt.code.types.BQTerm  tomMatch72_132= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getTermArg1() ; tom.engine.adt.code.types.BQTerm  tomMatch72_133= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getTermArg2() ;if ( (tomMatch72_132 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch72_132) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_138= tomMatch72_132.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch72_139= tomMatch72_132.getAstType() ;if ( (tomMatch72_138 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_138) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch72_139 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch72_139) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tom_type1= tomMatch72_139.getTomType() ;if ( (tomMatch72_133 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch72_133) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_148= tomMatch72_133.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch72_149= tomMatch72_133.getAstType() ;if ( (tomMatch72_148 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_148) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch72_149 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch72_149) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {




        if(getSymbolTable(moduleName).isUsedType(tom_type1)) {
          buildEqualTermDecl(deep, tomMatch72_138.getString() , tomMatch72_148.getString() ,tom_type1, tomMatch72_149.getTomType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName);
        }
        return;
      }}}}}}}}}}}}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsSortDecl) ) { tom.engine.adt.code.types.BQTerm  tomMatch72_159= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getTermArg() ;if ( (tomMatch72_159 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch72_159) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_164= tomMatch72_159.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch72_165= tomMatch72_159.getAstType() ;if ( (tomMatch72_164 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_164) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch72_165 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch72_165) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { String  tom_type= tomMatch72_165.getTomType() ;


        if(getSymbolTable(moduleName).isUsedType(tom_type)) {
          buildIsSortDecl(deep, tomMatch72_164.getString() ,tom_type, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName);
        }
        return;
      }}}}}}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.BQTermToDeclaration) ) {


        generateBQTerm(deep, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getBqTerm() ,moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.ResolveIsFsymDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_179= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ; tom.engine.adt.code.types.BQTerm  tomMatch72_180= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;if ( (tomMatch72_179 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_179) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_tomName= tomMatch72_179.getString() ;if ( (tomMatch72_180 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch72_180) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_187= tomMatch72_180.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch72_188= tomMatch72_180.getAstType() ;if ( (tomMatch72_187 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_187) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch72_188 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch72_188) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch72_194= tomMatch72_188.getTlType() ;if ( (tomMatch72_194 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch72_194) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {




        if(getSymbolTable(moduleName).isUsedSymbolDestructor(tom_tomName)) {
          buildResolveIsFsymDecl(deep,tom_tomName, tomMatch72_187.getString() ,tomMatch72_194,moduleName);
        }
        return;
      }}}}}}}}}}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.ResolveGetSlotDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_200= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ; tom.engine.adt.code.types.BQTerm  tomMatch72_202= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;if ( (tomMatch72_200 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_200) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_tomName= tomMatch72_200.getString() ;if ( (tomMatch72_202 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch72_202) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_208= tomMatch72_202.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch72_209= tomMatch72_202.getAstType() ;if ( (tomMatch72_208 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_208) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch72_209 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch72_209) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch72_215= tomMatch72_209.getTlType() ;if ( (tomMatch72_215 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch72_215) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {




          if(getSymbolTable(moduleName).isUsedSymbolDestructor(tom_tomName)) {
            buildResolveGetSlotDecl(deep,tom_tomName, tomMatch72_208.getString() ,tomMatch72_215, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getSlotName() ,moduleName);
          }
          return;
        }}}}}}}}}}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.ResolveMakeDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_221= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( (tomMatch72_221 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_221) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_opname= tomMatch72_221.getString() ;



        if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom_opname)) {
          genResolveDeclMake("tom_make_",tom_opname, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getArgs() ,moduleName);
        }
        return;
      }}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.ResolveClassDecl) ) {buildResolveClass( (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getWithName() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getToName() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExtends() ,moduleName)


;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.ResolveInverseLinksDecl) ) {buildResolveInverseLinks(deep, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getFileFrom() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getFileTo() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getResolveNameList() ,moduleName)


;
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.ReferenceClass) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_243= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getRefName() ;if ( (tomMatch72_243 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_243) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {




        //no test in order to generate the skeletton even if the class is not
        //populated
        //if(!`refclassTInstructions.isEmptyconcRefClassTracelinkInstruction()) {
        buildReferenceClass(deep, tomMatch72_243.getString() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getFields() ,moduleName);
        //}
        return;
      }}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetHeadDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_251= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getOpname() ; tom.engine.adt.tomtype.types.TomType  tomMatch72_252= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getCodomain() ; tom.engine.adt.code.types.BQTerm  tomMatch72_253= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;if ( (tomMatch72_251 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_251) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_opname= tomMatch72_251.getString() ;if ( (tomMatch72_252 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch72_252) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {if ( (tomMatch72_253 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch72_253) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_263= tomMatch72_253.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch72_264= tomMatch72_253.getAstType() ;if ( (tomMatch72_263 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_263) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch72_264 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch72_264) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch72_271= tomMatch72_264.getTlType() ;if ( (tomMatch72_271 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch72_271) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {








          if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom_opname) 
           ||getSymbolTable(moduleName).isUsedSymbolDestructor(tom_opname)) {
            buildGetHeadDecl(deep,tomMatch72_251, tomMatch72_263.getString() , tomMatch72_264.getTomType() ,tomMatch72_271, tomMatch72_252.getTlType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName);
          }
          return;
        }}}}}}}}}}}}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetTailDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_277= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getOpname() ; tom.engine.adt.code.types.BQTerm  tomMatch72_278= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;if ( (tomMatch72_277 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_277) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_opname= tomMatch72_277.getString() ;if ( (tomMatch72_278 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch72_278) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_285= tomMatch72_278.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch72_286= tomMatch72_278.getAstType() ;if ( (tomMatch72_285 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_285) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch72_286 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch72_286) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch72_293= tomMatch72_286.getTlType() ;if ( (tomMatch72_293 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch72_293) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {





          if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom_opname) 
              ||getSymbolTable(moduleName).isUsedSymbolDestructor(tom_opname)) {
            buildGetTailDecl(deep,tomMatch72_277, tomMatch72_285.getString() , tomMatch72_286.getTomType() ,tomMatch72_293, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName);
          }
          return;
        }}}}}}}}}}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsEmptyDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_299= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getOpname() ; tom.engine.adt.code.types.BQTerm  tomMatch72_300= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;if ( (tomMatch72_299 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_299) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_opname= tomMatch72_299.getString() ;if ( (tomMatch72_300 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch72_300) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_307= tomMatch72_300.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch72_308= tomMatch72_300.getAstType() ;if ( (tomMatch72_307 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_307) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch72_308 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch72_308) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch72_315= tomMatch72_308.getTlType() ;if ( (tomMatch72_315 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch72_315) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {





          if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom_opname) 
              ||getSymbolTable(moduleName).isUsedSymbolDestructor(tom_opname)) {
            buildIsEmptyDecl(deep,tomMatch72_299, tomMatch72_307.getString() , tomMatch72_308.getTomType() ,tomMatch72_315, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName);
          }
          return;
        }}}}}}}}}}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyList) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_321= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( (tomMatch72_321 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_321) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_opname= tomMatch72_321.getString() ;


        TomType returnType = TomBase.getSymbolCodomain(getSymbolFromName(tom_opname));
        if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom_opname) 
            || getSymbolTable(moduleName).isUsedSymbolDestructor(tom_opname)) {
          genDeclMake("tom_empty_list_",tom_opname,returnType, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstr() ,moduleName);
        }
        return;
      }}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeAddList) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_330= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ; tom.engine.adt.code.types.BQTerm  tomMatch72_331= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVarElt() ; tom.engine.adt.code.types.BQTerm  tomMatch72_332= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVarList() ;if ( (tomMatch72_330 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_330) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_opname= tomMatch72_330.getString() ;if ( (tomMatch72_331 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch72_331) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomtype.types.TomType  tomMatch72_340= tomMatch72_331.getAstType() ;if ( (tomMatch72_340 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch72_340) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch72_343= tomMatch72_340.getTlType() ;if ( (tomMatch72_343 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch72_343) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {if ( (tomMatch72_332 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch72_332) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomtype.types.TomType  tomMatch72_348= tomMatch72_332.getAstType() ;if ( (tomMatch72_348 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch72_348) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch72_351= tomMatch72_348.getTlType() ;if ( (tomMatch72_351 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch72_351) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {





        TomType returnType = tomMatch72_348;
        if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom_opname) 
            ||getSymbolTable(moduleName).isUsedSymbolDestructor(tom_opname)) {
          genDeclMake("tom_cons_list_",tom_opname,returnType, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(tomMatch72_331, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(tomMatch72_332, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstr() ,moduleName);
        }
        return;
      }}}}}}}}}}}}}}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetElementDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_357= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getOpname() ; tom.engine.adt.code.types.BQTerm  tomMatch72_358= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ; tom.engine.adt.code.types.BQTerm  tomMatch72_359= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getIndex() ;if ( (tomMatch72_357 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_357) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch72_358 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch72_358) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_366= tomMatch72_358.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch72_367= tomMatch72_358.getAstType() ;if ( (tomMatch72_366 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_366) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch72_367 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch72_367) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch72_374= tomMatch72_367.getTlType() ;if ( (tomMatch72_374 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch72_374) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {if ( (tomMatch72_359 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch72_359) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_379= tomMatch72_359.getAstName() ;if ( (tomMatch72_379 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_379) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {





          if(getSymbolTable(moduleName).isUsedSymbolDestructor( tomMatch72_357.getString() )) {
            buildGetElementDecl(deep,tomMatch72_357, tomMatch72_366.getString() , tomMatch72_379.getString() , tomMatch72_367.getTomType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName);
          }
          return;
        }}}}}}}}}}}}}}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetSizeDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_386= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getOpname() ; tom.engine.adt.code.types.BQTerm  tomMatch72_387= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;if ( (tomMatch72_386 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_386) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch72_387 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch72_387) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_394= tomMatch72_387.getAstName() ; tom.engine.adt.tomtype.types.TomType  tomMatch72_395= tomMatch72_387.getAstType() ;if ( (tomMatch72_394 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_394) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( (tomMatch72_395 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch72_395) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch72_402= tomMatch72_395.getTlType() ;if ( (tomMatch72_402 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch72_402) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {





          if(getSymbolTable(moduleName).isUsedSymbolDestructor( tomMatch72_386.getString() )) {
            buildGetSizeDecl(deep,tomMatch72_386, tomMatch72_394.getString() , tomMatch72_395.getTomType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName);
          }
          return;
        }}}}}}}}}}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyArray) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_408= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ; tom.engine.adt.code.types.BQTerm  tomMatch72_409= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVarSize() ;if ( (tomMatch72_408 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_408) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_opname= tomMatch72_408.getString() ;if ( (tomMatch72_409 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch72_409) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {




        TomType returnType = TomBase.getSymbolCodomain(getSymbolFromName(tom_opname));
        BQTerm newVar =  tom.engine.adt.code.types.bqterm.BQVariable.make( tomMatch72_409.getOptions() ,  tomMatch72_409.getAstName() , getSymbolTable(moduleName).getIntType()) ;
        if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom_opname)) {
          genDeclMake("tom_empty_array_",tom_opname,returnType, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(newVar, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstr() ,moduleName);
        }
        return;
      }}}}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeAddArray) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_422= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ; tom.engine.adt.code.types.BQTerm  tomMatch72_423= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVarElt() ; tom.engine.adt.code.types.BQTerm  tomMatch72_424= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVarList() ;if ( (tomMatch72_422 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_422) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_opname= tomMatch72_422.getString() ;if ( (tomMatch72_423 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch72_423) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomtype.types.TomType  tomMatch72_432= tomMatch72_423.getAstType() ;if ( (tomMatch72_432 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch72_432) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch72_435= tomMatch72_432.getTlType() ;if ( (tomMatch72_435 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch72_435) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {if ( (tomMatch72_424 instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch72_424) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) { tom.engine.adt.tomtype.types.TomType  tomMatch72_440= tomMatch72_424.getAstType() ;if ( (tomMatch72_440 instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch72_440) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch72_443= tomMatch72_440.getTlType() ;if ( (tomMatch72_443 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch72_443) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {





        TomType returnType = tomMatch72_440;
        if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom_opname)) {
          genDeclMake("tom_cons_array_",tom_opname,returnType, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(tomMatch72_423, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(tomMatch72_424, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstr() ,moduleName);
        }
        return;
      }}}}}}}}}}}}}}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeDecl) ) { tom.engine.adt.tomname.types.TomName  tomMatch72_449= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;if ( (tomMatch72_449 instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch72_449) instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_opname= tomMatch72_449.getString() ;


        if(getSymbolTable(moduleName).isUsedSymbolConstructor(tom_opname)) {
          genDeclMake("tom_make_",tom_opname, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getArgs() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstr() ,moduleName);
        }
        return;
      }}}}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )subject)) instanceof tom.engine.adt.tomdeclaration.types.declaration.TypeTermDecl) ) {


        //FIXME: consequences of a %op_implement? 
        generateDeclarationList(deep,  (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getDeclarations() , moduleName);
        return;
      }}}}{if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) { tom.engine.adt.tomdeclaration.types.Declaration  tom_t=(( tom.engine.adt.tomdeclaration.types.Declaration )subject);


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
      /* this is not pretty ! there is a ";" right after the new line
      if(prettyMode) {
        output.writeln();
      }*/
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

  
  //TODO: Resolve*
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
