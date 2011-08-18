/*
*
* TOM - To One Matching Compiler
* 
* Copyright (c) 2000-2011, INPL, INRIA
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

protected TomType getCodomain(TomSymbol tSymbol) {
return TomBase.getSymbolCodomain(tSymbol);
}

// ------------------------------------------------------------


  private static   tom.engine.adt.code.types.BQTermList  tom_append_list_concBQTerm( tom.engine.adt.code.types.BQTermList l1,  tom.engine.adt.code.types.BQTermList  l2) {
    if( l1.isEmptyconcBQTerm() ) {
      return l2;
    } else if( l2.isEmptyconcBQTerm() ) {
      return l1;
    } else if(  l1.getTailconcBQTerm() .isEmptyconcBQTerm() ) {
      return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,l2) ;
    } else {
      return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,tom_append_list_concBQTerm( l1.getTailconcBQTerm() ,l2)) ;
    }
  }
  private static   tom.engine.adt.code.types.BQTermList  tom_get_slice_concBQTerm( tom.engine.adt.code.types.BQTermList  begin,  tom.engine.adt.code.types.BQTermList  end, tom.engine.adt.code.types.BQTermList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyconcBQTerm()  ||  (end== tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( begin.getHeadconcBQTerm() ,( tom.engine.adt.code.types.BQTermList )tom_get_slice_concBQTerm( begin.getTailconcBQTerm() ,end,tail)) ;
  }
  
  private static   tom.engine.adt.code.types.BQTerm  tom_append_list_Composite( tom.engine.adt.code.types.BQTerm l1,  tom.engine.adt.code.types.BQTerm  l2) {
    if( l1.isEmptyComposite() ) {
      return l2;
    } else if( l2.isEmptyComposite() ) {
      return l1;
    } else if(  l1.getTailComposite() .isEmptyComposite() ) {
      return  tom.engine.adt.code.types.bqterm.ConsComposite.make( l1.getHeadComposite() ,l2) ;
    } else {
      return  tom.engine.adt.code.types.bqterm.ConsComposite.make( l1.getHeadComposite() ,tom_append_list_Composite( l1.getTailComposite() ,l2)) ;
    }
  }
  private static   tom.engine.adt.code.types.BQTerm  tom_get_slice_Composite( tom.engine.adt.code.types.BQTerm  begin,  tom.engine.adt.code.types.BQTerm  end, tom.engine.adt.code.types.BQTerm  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyComposite()  ||  (end== tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.code.types.bqterm.ConsComposite.make( begin.getHeadComposite() ,( tom.engine.adt.code.types.BQTerm )tom_get_slice_Composite( begin.getTailComposite() ,end,tail)) ;
  }
  
// ------------------------------------------------------------

/**
* Generate the goal language
* 
* @param deep 
* 		The distance from the right side (allows the computation of the column number)
*/
protected void generate(int deep, Code subject, String moduleName) throws IOException {

{
{
if ( (subject instanceof tom.engine.adt.code.types.Code) ) {
if ( ((( tom.engine.adt.code.types.Code )subject) instanceof tom.engine.adt.code.types.code.Tom) ) {

generateList(deep,
 (( tom.engine.adt.code.types.Code )subject).getCodeList() , moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.code.types.Code) ) {
if ( ((( tom.engine.adt.code.types.Code )subject) instanceof tom.engine.adt.code.types.code.TomInclude) ) {

generateListInclude(deep,
 (( tom.engine.adt.code.types.Code )subject).getCodeList() , moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.code.types.Code) ) {
if ( ((( tom.engine.adt.code.types.Code )subject) instanceof tom.engine.adt.code.types.code.BQTermToCode) ) {

generateBQTerm(deep,
 (( tom.engine.adt.code.types.Code )subject).getBq() , moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.code.types.Code) ) {
if ( ((( tom.engine.adt.code.types.Code )subject) instanceof tom.engine.adt.code.types.code.TargetLanguageToCode) ) {

generateTargetLanguage(deep,
 (( tom.engine.adt.code.types.Code )subject).getTl() , moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.code.types.Code) ) {
if ( ((( tom.engine.adt.code.types.Code )subject) instanceof tom.engine.adt.code.types.code.InstructionToCode) ) {

generateInstruction(deep,
 (( tom.engine.adt.code.types.Code )subject).getAstInstruction() , moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.code.types.Code) ) {
if ( ((( tom.engine.adt.code.types.Code )subject) instanceof tom.engine.adt.code.types.code.DeclarationToCode) ) {

generateDeclaration(deep,
 (( tom.engine.adt.code.types.Code )subject).getAstDeclaration() , moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.code.types.Code) ) {
 tom.engine.adt.code.types.Code  tom_t=(( tom.engine.adt.code.types.Code )subject);

System.out.println("Cannot generate code for: " + 
tom_t);
throw new TomRuntimeException("Cannot generate code for: " + 
tom_t);


}

}


}

}

/**
* Generate the goal language
* 
* @param deep 
* 		The distance from the right side (allows the computation of the column number)
*/
protected void generateTomTerm(int deep, TomTerm subject, String moduleName) throws IOException {
//TODO: complete with each constructor used in the baclend input term

{
{
if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
boolean tomMatch62_2= false ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
tomMatch62_2= true ;
} else {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
tomMatch62_2= true ;
}
}
if (tomMatch62_2) {

output.write(deep,getVariableName(
(( tom.engine.adt.tomterm.types.TomTerm )subject)));
return;


}

}

}

}

}

/**
* Generate the goal language
* 
* @param deep 
* 		The distance from the right side (allows the computation of the column number)
*/
protected void generateBQTerm(int deep, BQTerm subject, String moduleName) throws IOException {

{
{
if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildConstant) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch63_1= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ;
if ( (tomMatch63_1 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_name= tomMatch63_1.getString() ;

if(
tom_name.charAt(0)=='\'' && 
tom_name.charAt(
tom_name.length()-1)=='\'') {
String substring = 
tom_name.substring(1,
tom_name.length()-1);
//System.out.println("BuildConstant: " + substring);
substring = substring.replace("\\","\\\\"); // replace backslash by backslash-backslash
substring = substring.replace("'","\\'"); // replace quote by backslash-quote
output.write("'" + substring + "'");
return;
}
output.write(
tom_name);
return;


}
}
}

}
{
if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildTerm) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch63_6= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ;
if ( (tomMatch63_6 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
buildTerm(deep, tomMatch63_6.getString() , (( tom.engine.adt.code.types.BQTerm )subject).getArgs() , (( tom.engine.adt.code.types.BQTerm )subject).getModuleName() );
return;


}
}
}

}
{
if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch63_14= false ;
if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyList) ) {
tomMatch63_14= true ;
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyArray) ) {
tomMatch63_14= true ;
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildConsList) ) {
tomMatch63_14= true ;
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildAppendList) ) {
tomMatch63_14= true ;
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildConsArray) ) {
tomMatch63_14= true ;
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildAppendArray) ) {
tomMatch63_14= true ;
}
}
}
}
}
}
if (tomMatch63_14) {

buildListOrArray(deep, 
(( tom.engine.adt.code.types.BQTerm )subject), moduleName);
return;


}

}

}
{
if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.FunctionCall) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch63_16= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ;
if ( (tomMatch63_16 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

buildFunctionCall(deep,
 tomMatch63_16.getString() , 
 (( tom.engine.adt.code.types.BQTerm )subject).getArgs() , moduleName);
return;


}
}
}

}
{
if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch63_23= false ;
if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
tomMatch63_23= true ;
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
tomMatch63_23= true ;
}
}
if (tomMatch63_23) {

output.write(deep,getVariableName(
(( tom.engine.adt.code.types.BQTerm )subject)));
return;


}

}

}
{
if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) {

generateExpression(deep,
 (( tom.engine.adt.code.types.BQTerm )subject).getExp() , moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( (((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.ConsComposite) || ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.EmptyComposite)) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch63__end__31=(( tom.engine.adt.code.types.BQTerm )subject);
do {
{
if (!( tomMatch63__end__31.isEmptyComposite() )) {
 tom.engine.adt.code.types.CompositeMember  tom_t= tomMatch63__end__31.getHeadComposite() ;
{
{
if ( (tom_t instanceof tom.engine.adt.code.types.CompositeMember) ) {
if ( ((( tom.engine.adt.code.types.CompositeMember )tom_t) instanceof tom.engine.adt.code.types.compositemember.CompositeTL) ) {

generateTargetLanguage(deep,
 (( tom.engine.adt.code.types.CompositeMember )tom_t).getTl() , moduleName);


}
}

}
{
if ( (tom_t instanceof tom.engine.adt.code.types.CompositeMember) ) {
if ( ((( tom.engine.adt.code.types.CompositeMember )tom_t) instanceof tom.engine.adt.code.types.compositemember.CompositeBQTerm) ) {

generateBQTerm(deep,
 (( tom.engine.adt.code.types.CompositeMember )tom_t).getterm() , moduleName);


}
}

}


}



}
if ( tomMatch63__end__31.isEmptyComposite() ) {
tomMatch63__end__31=(( tom.engine.adt.code.types.BQTerm )subject);
} else {
tomMatch63__end__31= tomMatch63__end__31.getTailComposite() ;
}

}
} while(!( (tomMatch63__end__31==(( tom.engine.adt.code.types.BQTerm )subject)) ));
}
}

}
{
if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {
 tom.engine.adt.code.types.BQTerm  tom_t=(( tom.engine.adt.code.types.BQTerm )subject);
boolean tomMatch63_38= false ;
if ( (((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.ConsComposite) || ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.EmptyComposite)) ) {
if ( (tom_t==(( tom.engine.adt.code.types.BQTerm )subject)) ) {
tomMatch63_38= true ;
}
}
if (!(tomMatch63_38)) {

throw new TomRuntimeException("Cannot generate code for bqterm "+
tom_t);


}

}

}


}

}

protected String getVariableName(BQTerm var) {

{
{
if ( (var instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )var) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch65_1= (( tom.engine.adt.code.types.BQTerm )var).getAstName() ;
if ( (tomMatch65_1 instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {

return ("tom" + TomBase.tomNumberListToString(
 tomMatch65_1.getNumberList() ));


}
}
}

}
{
if ( (var instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )var) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch65_6= (( tom.engine.adt.code.types.BQTerm )var).getAstName() ;
if ( (tomMatch65_6 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

return 
 tomMatch65_6.getString() ;


}
}
}

}
{
if ( (var instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )var) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch65_11= (( tom.engine.adt.code.types.BQTerm )var).getAstName() ;
if ( (tomMatch65_11 instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {

return ("tom" + TomBase.tomNumberListToString(
 tomMatch65_11.getNumberList() ));


}
}
}

}
{
if ( (var instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )var) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch65_16= (( tom.engine.adt.code.types.BQTerm )var).getAstName() ;
if ( (tomMatch65_16 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

return 
 tomMatch65_16.getString() ;


}
}
}

}


}

throw new RuntimeException("cannot generate the name of the variable "+var);
}

protected String getVariableName(TomTerm var) {

{
{
if ( (var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch66_1= (( tom.engine.adt.tomterm.types.TomTerm )var).getAstName() ;
if ( (tomMatch66_1 instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {

return ("tom" + TomBase.tomNumberListToString(
 tomMatch66_1.getNumberList() ));


}
}
}

}
{
if ( (var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch66_6= (( tom.engine.adt.tomterm.types.TomTerm )var).getAstName() ;
if ( (tomMatch66_6 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

return 
 tomMatch66_6.getString() ;


}
}
}

}
{
if ( (var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch66_11= (( tom.engine.adt.tomterm.types.TomTerm )var).getAstName() ;
if ( (tomMatch66_11 instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {

return ("tom" + TomBase.tomNumberListToString(
 tomMatch66_11.getNumberList() ));


}
}
}

}
{
if ( (var instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )var) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch66_16= (( tom.engine.adt.tomterm.types.TomTerm )var).getAstName() ;
if ( (tomMatch66_16 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

return 
 tomMatch66_16.getString() ;


}
}
}

}


}

throw new RuntimeException("cannot generate the name of the variable "+var);
}

public void generateExpression(int deep, Expression subject, String moduleName) throws IOException {

{
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {

output.write(
 (( tom.engine.adt.tomexpression.types.Expression )subject).getCode() );
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Integer) ) {

output.write(
 (( tom.engine.adt.tomexpression.types.Expression )subject).getvalue() );
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Negation) ) {

buildExpNegation(deep, 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getArg() , moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Conditional) ) {

buildExpConditional(deep, 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getCond() , 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getThen() , 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getElse() , moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.And) ) {

buildExpAnd(deep, 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() , 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() , moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Or) ) {

buildExpOr(deep, 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() , 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() , moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GreaterThan) ) {

buildExpGreaterThan(deep, 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() , 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() , moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GreaterOrEqualThan) ) {

buildExpGreaterOrEqualThan(deep, 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() , 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() , moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.LessThan) ) {

buildExpLessThan(deep, 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() , 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() , moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.LessOrEqualThan) ) {

buildExpLessOrEqualThan(deep, 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() , 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() , moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Bottom) ) {

buildExpBottom(deep,
 (( tom.engine.adt.tomexpression.types.Expression )subject).getTomType() , moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.TrueTL) ) {

buildExpTrue(deep);
return;

}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.FalseTL) ) {

buildExpFalse(deep);
return;

}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.IsEmptyList) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch67_46= (( tom.engine.adt.tomexpression.types.Expression )subject).getOpname() ;
if ( (tomMatch67_46 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 tom.engine.adt.code.types.BQTerm  tom_expList= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;
buildExpIsEmptyList(deep, tomMatch67_46.getString() ,getTermType(tom_expList),tom_expList,moduleName);
return;


}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.IsEmptyArray) ) {
 tom.engine.adt.code.types.BQTerm  tom_expArray= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;

buildExpIsEmptyArray(deep, 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getOpname() , getTermType(
tom_expArray), 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getIndex() , 
tom_expArray, moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.EqualTerm) ) {
buildExpEqualTerm(deep, (( tom.engine.adt.tomexpression.types.Expression )subject).getTomType() , (( tom.engine.adt.tomexpression.types.Expression )subject).getKid1() , (( tom.engine.adt.tomexpression.types.Expression )subject).getKid2() ,moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.EqualBQTerm) ) {
buildExpEqualBQTerm(deep, (( tom.engine.adt.tomexpression.types.Expression )subject).getTomType() , (( tom.engine.adt.tomexpression.types.Expression )subject).gett1() , (( tom.engine.adt.tomexpression.types.Expression )subject).gett2() ,moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.IsSort) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch67_67= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstType() ;
if ( (tomMatch67_67 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
buildExpIsSort(deep, tomMatch67_67.getTomType() , (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ,moduleName);
return;


}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.IsFsym) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch67_73= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() ;
if ( (tomMatch67_73 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

buildExpIsFsym(deep, 
 tomMatch67_73.getString() , 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() , moduleName);
return;


}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Cast) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch67_79= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstType() ;
if ( (tomMatch67_79 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch67_82= tomMatch67_79.getTlType() ;
if ( (tomMatch67_82 instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {

buildExpCast(deep, 
tomMatch67_82, 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getSource() , moduleName);
return;


}
}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetSlot) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch67_87= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() ;
 tom.engine.adt.code.types.BQTerm  tomMatch67_89= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;
if ( (tomMatch67_87 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
boolean tomMatch67_94= false ;
if ( (tomMatch67_89 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
tomMatch67_94= true ;
} else {
if ( (tomMatch67_89 instanceof tom.engine.adt.code.types.bqterm.BuildTerm) ) {
tomMatch67_94= true ;
} else {
if ( (tomMatch67_89 instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) {
tomMatch67_94= true ;
}
}
}
if (tomMatch67_94) {
buildExpGetSlot(deep, tomMatch67_87.getString() , (( tom.engine.adt.tomexpression.types.Expression )subject).getSlotNameString() ,tomMatch67_89,moduleName);
return;


}

}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetSlot) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch67_97= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() ;
 tom.engine.adt.code.types.BQTerm  tomMatch67_99= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;
if ( (tomMatch67_97 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch67_99 instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) {
if ( ( tomMatch67_99.getExp()  instanceof tom.engine.adt.tomexpression.types.expression.GetSlot) ) {
buildExpGetSlot(deep, tomMatch67_97.getString() , (( tom.engine.adt.tomexpression.types.Expression )subject).getSlotNameString() ,tomMatch67_99,moduleName);
return;


}
}
}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetHead) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch67_107= (( tom.engine.adt.tomexpression.types.Expression )subject).getOpname() ;
if ( (tomMatch67_107 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 tom.engine.adt.code.types.BQTerm  tom_exp= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;
buildExpGetHead(deep, tomMatch67_107.getString() ,getTermType(tom_exp), (( tom.engine.adt.tomexpression.types.Expression )subject).getCodomain() ,tom_exp,moduleName);
return;


}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetTail) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch67_114= (( tom.engine.adt.tomexpression.types.Expression )subject).getOpname() ;
if ( (tomMatch67_114 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 tom.engine.adt.code.types.BQTerm  tom_exp= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;
buildExpGetTail(deep, tomMatch67_114.getString() ,getTermType(tom_exp),tom_exp,moduleName);
return;


}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.AddOne) ) {

buildAddOne(deep, 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() , moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.SubstractOne) ) {

buildSubstractOne(deep, 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() , moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Substract) ) {

buildSubstract(deep, 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getTerm1() , 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getTerm2() , moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetSize) ) {
 tom.engine.adt.code.types.BQTerm  tom_exp= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;

buildExpGetSize(deep,
 (( tom.engine.adt.tomexpression.types.Expression )subject).getOpname() ,getTermType(
tom_exp), 
tom_exp, moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetElement) ) {
 tom.engine.adt.code.types.BQTerm  tom_varName= (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ;

buildExpGetElement(deep,
 (( tom.engine.adt.tomexpression.types.Expression )subject).getOpname() ,getTermType(
tom_varName),
tom_varName, 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getIndex() , moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetSliceList) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch67_140= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() ;
if ( (tomMatch67_140 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

buildExpGetSliceList(deep, 
 tomMatch67_140.getString() , 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableBeginAST() , 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableEndAST() , 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getTail() ,moduleName);
return;


}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetSliceArray) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch67_148= (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() ;
if ( (tomMatch67_148 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

buildExpGetSliceArray(deep, 
 tomMatch67_148.getString() , 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getSubjectListName() , 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableBeginAST() , 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableEndAST() , moduleName);
return;


}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.BQTermToExpression) ) {

generateBQTerm(deep,
 (( tom.engine.adt.tomexpression.types.Expression )subject).getAstTerm() , moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.TomInstructionToExpression) ) {

generateInstruction(deep, 
 (( tom.engine.adt.tomexpression.types.Expression )subject).getInstruction() , moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {
 tom.engine.adt.tomexpression.types.Expression  tom_t=(( tom.engine.adt.tomexpression.types.Expression )subject);

System.out.println("Cannot generate code for expression: " + 
tom_t);
throw new TomRuntimeException("Cannot generate code for expression: " + 
tom_t);


}

}


}

}

/**
* generates var[index] 
*/
protected void generateArray(int deep, BQTerm subject, BQTerm index, String moduleName) throws IOException {

{
{
if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch68_1= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ;
if ( (tomMatch68_1 instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {

output.write("tom" + TomBase.tomNumberListToString(
 tomMatch68_1.getNumberList() ));        


}
}
}

}
{
if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch68_6= (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ;
if ( (tomMatch68_6 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

output.write(
 tomMatch68_6.getString() );        


}
}
}

}


}
{
{
if ( (index instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )index) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch69_1= (( tom.engine.adt.code.types.BQTerm )index).getAstName() ;
if ( (tomMatch69_1 instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {

output.write("[");
output.write("tom" + TomBase.tomNumberListToString(
 tomMatch69_1.getNumberList() ));
output.write("]");        


}
}
}

}
{
if ( (index instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )index) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch69_6= (( tom.engine.adt.code.types.BQTerm )index).getAstName() ;
if ( (tomMatch69_6 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

output.write("[");
output.write(
 tomMatch69_6.getString() );
output.write("]");


}
}
}

}
{
if ( (index instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )index) instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) {
 tom.engine.adt.tomexpression.types.Expression  tomMatch69_11= (( tom.engine.adt.code.types.BQTerm )index).getExp() ;
if ( (tomMatch69_11 instanceof tom.engine.adt.tomexpression.types.expression.Integer) ) {

output.write("[");
output.write(
 tomMatch69_11.getvalue() );
output.write("]");  


}
}
}

}


}

} 

public void generateInstruction(int deep, Instruction subject, String moduleName) throws IOException {

{
{
if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.CodeToInstruction) ) {
generate(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getCode() ,moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.BQTermToInstruction) ) {
generateBQTerm(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getTom() ,moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction) ) {
generateExpression(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getExpr() ,moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {

return;

}
}

}
{
if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.Assign) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch70_12= (( tom.engine.adt.tominstruction.types.Instruction )subject).getVariable() ;
boolean tomMatch70_17= false ;
 tom.engine.adt.tomoption.types.OptionList  tomMatch70_15= null ;
if ( (tomMatch70_12 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch70_17= true ;
tomMatch70_15= tomMatch70_12.getOptions() ;

}
} else {
if ( (tomMatch70_12 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch70_17= true ;
tomMatch70_15= tomMatch70_12.getOptions() ;

}
}
}
if (tomMatch70_17) {
buildAssign(deep,tomMatch70_12,tomMatch70_15, (( tom.engine.adt.tominstruction.types.Instruction )subject).getSource() ,moduleName);
return;


}

}
}

}
{
if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.AssignArray) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch70_19= (( tom.engine.adt.tominstruction.types.Instruction )subject).getVariable() ;
if ( (tomMatch70_19 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
buildAssignArray(deep,tomMatch70_19, tomMatch70_19.getOptions() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getIndex() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getSource() ,moduleName);
return;


}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.Let) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch70_26= (( tom.engine.adt.tominstruction.types.Instruction )subject).getVariable() ;
boolean tomMatch70_35= false ;
 tom.engine.adt.tomtype.types.TomType  tomMatch70_31= null ;
 tom.engine.adt.tomoption.types.OptionList  tomMatch70_30= null ;
if ( (tomMatch70_26 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch70_35= true ;
tomMatch70_30= tomMatch70_26.getOptions() ;
tomMatch70_31= tomMatch70_26.getAstType() ;

}
} else {
if ( (tomMatch70_26 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch70_35= true ;
tomMatch70_30= tomMatch70_26.getOptions() ;
tomMatch70_31= tomMatch70_26.getAstType() ;

}
}
}
if (tomMatch70_35) {
if ( (tomMatch70_31 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
buildLet(deep,tomMatch70_26,tomMatch70_30, tomMatch70_31.getTlType() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getSource() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getAstInstruction() ,moduleName);
return;


}
}

}
}

}
{
if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.LetRef) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch70_37= (( tom.engine.adt.tominstruction.types.Instruction )subject).getVariable() ;
boolean tomMatch70_46= false ;
 tom.engine.adt.tomtype.types.TomType  tomMatch70_42= null ;
 tom.engine.adt.tomoption.types.OptionList  tomMatch70_41= null ;
if ( (tomMatch70_37 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch70_46= true ;
tomMatch70_41= tomMatch70_37.getOptions() ;
tomMatch70_42= tomMatch70_37.getAstType() ;

}
} else {
if ( (tomMatch70_37 instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch70_46= true ;
tomMatch70_41= tomMatch70_37.getOptions() ;
tomMatch70_42= tomMatch70_37.getAstType() ;

}
}
}
if (tomMatch70_46) {
if ( (tomMatch70_42 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
buildLetRef(deep,tomMatch70_37,tomMatch70_41, tomMatch70_42.getTlType() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getSource() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getAstInstruction() ,moduleName);
return;


}
}

}
}

}
{
if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) {

//`generateInstructionList(deep, instList);

buildInstructionSequence(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getInstList() ,moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.UnamedBlock) ) {
buildUnamedBlock(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getInstList() ,moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.NamedBlock) ) {
buildNamedBlock(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getBlockName() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getInstList() ,moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {
if ( ( (( tom.engine.adt.tominstruction.types.Instruction )subject).getFailureInst()  instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {
buildIf(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getSuccesInst() ,moduleName);
return;


}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {
if ( ( (( tom.engine.adt.tominstruction.types.Instruction )subject).getSuccesInst()  instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {
buildIf(deep, tom.engine.adt.tomexpression.types.expression.Negation.make( (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() ) , (( tom.engine.adt.tominstruction.types.Instruction )subject).getFailureInst() ,moduleName);
return;


}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {
buildIfWithFailure(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getSuccesInst() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getFailureInst() ,moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.DoWhile) ) {
buildDoWhile(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getDoInst() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() ,moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.WhileDo) ) {
buildWhileDo(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() , (( tom.engine.adt.tominstruction.types.Instruction )subject).getDoInst() ,moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.RawAction) ) {
 tom.engine.adt.tominstruction.types.Instruction  tomMatch70_83= (( tom.engine.adt.tominstruction.types.Instruction )subject).getAstInstruction() ;
if ( (tomMatch70_83 instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) {
generateInstructionList(deep, tomMatch70_83.getInstList() ,moduleName);
return;


}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.RawAction) ) {
generateInstruction(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getAstInstruction() ,moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.Return) ) {
buildReturn(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getKid1() ,moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.CompiledMatch) ) {

//TODO moduleName

generateInstruction(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getAutomataInst() ,moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.CompiledPattern) ) {
generateInstruction(deep, (( tom.engine.adt.tominstruction.types.Instruction )subject).getAutomataInst() ,moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
 tom.engine.adt.tominstruction.types.Instruction  tom_t=(( tom.engine.adt.tominstruction.types.Instruction )subject);

System.out.println("Cannot generate code for instruction: " + 
tom_t);
throw new TomRuntimeException("Cannot generate code for instruction: " + 
tom_t);


}

}


}

}

public void generateTargetLanguage(int deep, TargetLanguage subject, String moduleName) throws IOException {

{
{
if ( (subject instanceof tom.engine.adt.code.types.TargetLanguage) ) {
if ( ((( tom.engine.adt.code.types.TargetLanguage )subject) instanceof tom.engine.adt.code.types.targetlanguage.TL) ) {
 tom.engine.adt.tomsignature.types.TextPosition  tomMatch71_2= (( tom.engine.adt.code.types.TargetLanguage )subject).getStart() ;
 tom.engine.adt.tomsignature.types.TextPosition  tomMatch71_3= (( tom.engine.adt.code.types.TargetLanguage )subject).getEnd() ;
if ( (tomMatch71_2 instanceof tom.engine.adt.tomsignature.types.textposition.TextPosition) ) {
 int  tom_startLine= tomMatch71_2.getLine() ;
if ( (tomMatch71_3 instanceof tom.engine.adt.tomsignature.types.textposition.TextPosition) ) {

output.write(deep, 
 (( tom.engine.adt.code.types.TargetLanguage )subject).getCode() , 
tom_startLine, 
 tomMatch71_3.getLine() - 
tom_startLine);
return;


}
}
}
}

}
{
if ( (subject instanceof tom.engine.adt.code.types.TargetLanguage) ) {
if ( ((( tom.engine.adt.code.types.TargetLanguage )subject) instanceof tom.engine.adt.code.types.targetlanguage.ITL) ) {

output.write(
 (( tom.engine.adt.code.types.TargetLanguage )subject).getCode() );
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.code.types.TargetLanguage) ) {
if ( ((( tom.engine.adt.code.types.TargetLanguage )subject) instanceof tom.engine.adt.code.types.targetlanguage.Comment) ) {
buildComment(deep, (( tom.engine.adt.code.types.TargetLanguage )subject).getCode() );
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.code.types.TargetLanguage) ) {
 tom.engine.adt.code.types.TargetLanguage  tom_t=(( tom.engine.adt.code.types.TargetLanguage )subject);

System.out.println("Cannot generate code for TL: " + 
tom_t);
throw new TomRuntimeException("Cannot generate code for TL: " + 
tom_t);


}

}


}

}

public void generateOption(int deep, Option subject, String moduleName) throws IOException {

{
{
if ( (subject instanceof tom.engine.adt.tomoption.types.Option) ) {
if ( ((( tom.engine.adt.tomoption.types.Option )subject) instanceof tom.engine.adt.tomoption.types.option.DeclarationToOption) ) {
generateDeclaration(deep, (( tom.engine.adt.tomoption.types.Option )subject).getAstDeclaration() ,moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tomoption.types.Option) ) {
if ( ((( tom.engine.adt.tomoption.types.Option )subject) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {
return; 
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomoption.types.Option) ) {
if ( ((( tom.engine.adt.tomoption.types.Option )subject) instanceof tom.engine.adt.tomoption.types.option.ACSymbol) ) {

// TODO RK: here add the declarations for intarray
return; 

}
}

}
{
if ( (subject instanceof tom.engine.adt.tomoption.types.Option) ) {

throw new TomRuntimeException("Cannot generate code for option: " + 
(( tom.engine.adt.tomoption.types.Option )subject));


}

}


}

}

public void generateDeclaration(int deep, Declaration subject, String moduleName) throws IOException {

{
{
if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.EmptyDeclaration) ) {

return;

}
}

}
{
if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.AbstractDecl) ) {

//`generateInstructionList(deep, instList);

generateDeclarationList(deep, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getDeclList() ,moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.FunctionDef) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_6= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;
if ( (tomMatch73_6 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
buildFunctionDef(deep, tomMatch73_6.getString() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getArgumentList() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getCodomain() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getThrowsType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstruction() ,moduleName);
return;


}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.MethodDef) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_15= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;
if ( (tomMatch73_15 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
buildMethodDef(deep, tomMatch73_15.getString() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getArgumentList() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getCodomain() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getThrowsType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstruction() ,moduleName);
return;


}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.Class) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_24= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;
if ( (tomMatch73_24 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
buildClass(deep, tomMatch73_24.getString() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExtendsType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getSuperTerm() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getDeclaration() ,moduleName);
return;


}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.IntrospectorClass) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_32= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;
if ( (tomMatch73_32 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
buildIntrospectorClass(deep, tomMatch73_32.getString() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getDeclaration() ,moduleName);
return;


}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.SymbolDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_38= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;
if ( (tomMatch73_38 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
buildSymbolDecl(deep, tomMatch73_38.getString() ,moduleName);
return ;


}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.ArraySymbolDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_43= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;
if ( (tomMatch73_43 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_tomName= tomMatch73_43.getString() ;

if(getSymbolTable(moduleName).isUsedSymbolConstructor(
tom_tomName) 
||getSymbolTable(moduleName).isUsedSymbolDestructor(
tom_tomName)) {

buildSymbolDecl(deep,tom_tomName,moduleName);

genDeclArray(tom_tomName,moduleName);
}
return ;


}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
boolean tomMatch73_52= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch73_48= null ;
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.ListSymbolDecl) ) {
{
tomMatch73_52= true ;
tomMatch73_48= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;

}
} else {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.ACSymbolDecl) ) {
{
tomMatch73_52= true ;
tomMatch73_48= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;

}
}
}
if (tomMatch73_52) {
if ( (tomMatch73_48 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_tomName= tomMatch73_48.getString() ;

if(getSymbolTable(moduleName).isUsedSymbolConstructor(
tom_tomName) 
||getSymbolTable(moduleName).isUsedSymbolDestructor(
tom_tomName)) {

buildSymbolDecl(deep,tom_tomName,moduleName);

genDeclList(tom_tomName,moduleName);
}
return ;


}
}

}

}
{
if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetImplementationDecl) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch73_54= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;
if ( (tomMatch73_54 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_58= tomMatch73_54.getAstName() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch73_59= tomMatch73_54.getAstType() ;
if ( (tomMatch73_58 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_name= tomMatch73_58.getString() ;
if ( (tomMatch73_59 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch73_64= tomMatch73_59.getTlType() ;
if ( (tomMatch73_64 instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {

if(getSymbolTable(moduleName).isUsedSymbolDestructor(
tom_name)) {

buildGetImplementationDecl(deep, tomMatch73_59.getTomType() ,tom_name,tomMatch73_64, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstr() ,moduleName);
}
return;


}
}
}
}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsFsymDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_68= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_69= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;
if ( (tomMatch73_68 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_tomName= tomMatch73_68.getString() ;
if ( (tomMatch73_69 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_75= tomMatch73_69.getAstName() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch73_76= tomMatch73_69.getAstType() ;
if ( (tomMatch73_75 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch73_76 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch73_80= tomMatch73_76.getTlType() ;
if ( (tomMatch73_80 instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {

if(getSymbolTable(moduleName).isUsedSymbolDestructor(
tom_tomName)) {

buildIsFsymDecl(deep,tom_tomName, tomMatch73_75.getString() ,tomMatch73_80, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName);
}
return;


}
}
}
}
}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetSlotDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_84= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_86= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;
if ( (tomMatch73_84 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_tomName= tomMatch73_84.getString() ;
if ( (tomMatch73_86 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_91= tomMatch73_86.getAstName() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch73_92= tomMatch73_86.getAstType() ;
if ( (tomMatch73_91 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch73_92 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch73_96= tomMatch73_92.getTlType() ;
if ( (tomMatch73_96 instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {

if(getSymbolTable(moduleName).isUsedSymbolDestructor(
tom_tomName)) {

buildGetSlotDecl(deep,tom_tomName, tomMatch73_91.getString() ,tomMatch73_96, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getSlotName() ,moduleName);
}
return;


}
}
}
}
}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetDefaultDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_100= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;
if ( (tomMatch73_100 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_tomName= tomMatch73_100.getString() ;

if(getSymbolTable(moduleName).isUsedSymbolConstructor(
tom_tomName)) {

buildGetDefaultDecl(deep,tom_tomName, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getSlotName() ,moduleName);
}
return;


}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.EqualTermDecl) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch73_107= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getTermArg1() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_108= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getTermArg2() ;
if ( (tomMatch73_107 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_112= tomMatch73_107.getAstName() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch73_113= tomMatch73_107.getAstType() ;
if ( (tomMatch73_112 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch73_113 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 String  tom_type1= tomMatch73_113.getTomType() ;
if ( (tomMatch73_108 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_119= tomMatch73_108.getAstName() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch73_120= tomMatch73_108.getAstType() ;
if ( (tomMatch73_119 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch73_120 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {

if(getSymbolTable(moduleName).isUsedType(
tom_type1)) {

buildEqualTermDecl(deep, tomMatch73_112.getString() , tomMatch73_119.getString() ,tom_type1, tomMatch73_120.getTomType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName);
}
return;


}
}
}
}
}
}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsSortDecl) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch73_127= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getTermArg() ;
if ( (tomMatch73_127 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_131= tomMatch73_127.getAstName() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch73_132= tomMatch73_127.getAstType() ;
if ( (tomMatch73_131 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch73_132 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 String  tom_type= tomMatch73_132.getTomType() ;

if(getSymbolTable(moduleName).isUsedType(
tom_type)) {

buildIsSortDecl(deep, tomMatch73_131.getString() ,tom_type, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName);
}
return;


}
}
}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetHeadDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_139= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getOpname() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch73_140= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getCodomain() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_141= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;
if ( (tomMatch73_139 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_opname= tomMatch73_139.getString() ;
if ( (tomMatch73_140 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
if ( (tomMatch73_141 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_148= tomMatch73_141.getAstName() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch73_149= tomMatch73_141.getAstType() ;
if ( (tomMatch73_148 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch73_149 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch73_154= tomMatch73_149.getTlType() ;
if ( (tomMatch73_154 instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {

if(getSymbolTable(moduleName).isUsedSymbolConstructor(
tom_opname) 
||getSymbolTable(moduleName).isUsedSymbolDestructor(
tom_opname)) {

buildGetHeadDecl(deep,tomMatch73_139, tomMatch73_148.getString() , tomMatch73_149.getTomType() ,tomMatch73_154, tomMatch73_140.getTlType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName);
}
return;


}
}
}
}
}
}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetTailDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_158= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getOpname() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_159= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;
if ( (tomMatch73_158 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_opname= tomMatch73_158.getString() ;
if ( (tomMatch73_159 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_164= tomMatch73_159.getAstName() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch73_165= tomMatch73_159.getAstType() ;
if ( (tomMatch73_164 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch73_165 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch73_170= tomMatch73_165.getTlType() ;
if ( (tomMatch73_170 instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {

if(getSymbolTable(moduleName).isUsedSymbolConstructor(
tom_opname) 
||getSymbolTable(moduleName).isUsedSymbolDestructor(
tom_opname)) {

buildGetTailDecl(deep,tomMatch73_158, tomMatch73_164.getString() , tomMatch73_165.getTomType() ,tomMatch73_170, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName);
}
return;


}
}
}
}
}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsEmptyDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_174= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getOpname() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_175= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;
if ( (tomMatch73_174 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_opname= tomMatch73_174.getString() ;
if ( (tomMatch73_175 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_180= tomMatch73_175.getAstName() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch73_181= tomMatch73_175.getAstType() ;
if ( (tomMatch73_180 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch73_181 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch73_186= tomMatch73_181.getTlType() ;
if ( (tomMatch73_186 instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {

if(getSymbolTable(moduleName).isUsedSymbolConstructor(
tom_opname) 
||getSymbolTable(moduleName).isUsedSymbolDestructor(
tom_opname)) {

buildIsEmptyDecl(deep,tomMatch73_174, tomMatch73_180.getString() , tomMatch73_181.getTomType() ,tomMatch73_186, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName);
}
return;


}
}
}
}
}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyList) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_190= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;
if ( (tomMatch73_190 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_opname= tomMatch73_190.getString() ;

TomType returnType = TomBase.getSymbolCodomain(getSymbolFromName(
tom_opname));
if(getSymbolTable(moduleName).isUsedSymbolConstructor(
tom_opname) 
|| getSymbolTable(moduleName).isUsedSymbolDestructor(
tom_opname)) {

genDeclMake("tom_empty_list_",tom_opname,returnType, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstr() ,moduleName);
}
return;


}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeAddList) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_197= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_198= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVarElt() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_199= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVarList() ;
if ( (tomMatch73_197 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_opname= tomMatch73_197.getString() ;
if ( (tomMatch73_198 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch73_205= tomMatch73_198.getAstType() ;
if ( (tomMatch73_205 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
if ( ( tomMatch73_205.getTlType()  instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {
if ( (tomMatch73_199 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch73_210= tomMatch73_199.getAstType() ;
if ( (tomMatch73_210 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
if ( ( tomMatch73_210.getTlType()  instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {

TomType returnType = 
tomMatch73_210;
if(getSymbolTable(moduleName).isUsedSymbolConstructor(
tom_opname) 
||getSymbolTable(moduleName).isUsedSymbolDestructor(
tom_opname)) {

genDeclMake("tom_cons_list_",tom_opname,returnType, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(tomMatch73_198, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(tomMatch73_199, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstr() ,moduleName);
}
return;


}
}
}
}
}
}
}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetElementDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_216= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getOpname() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_217= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_218= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getIndex() ;
if ( (tomMatch73_216 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch73_217 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_223= tomMatch73_217.getAstName() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch73_224= tomMatch73_217.getAstType() ;
if ( (tomMatch73_223 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch73_224 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
if ( ( tomMatch73_224.getTlType()  instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {
if ( (tomMatch73_218 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_232= tomMatch73_218.getAstName() ;
if ( (tomMatch73_232 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

if(getSymbolTable(moduleName).isUsedSymbolDestructor(
 tomMatch73_216.getString() )) {

buildGetElementDecl(deep,tomMatch73_216, tomMatch73_223.getString() , tomMatch73_232.getString() , tomMatch73_224.getTomType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName);
}
return;


}
}
}
}
}
}
}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetSizeDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_237= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getOpname() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_238= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVariable() ;
if ( (tomMatch73_237 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch73_238 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_243= tomMatch73_238.getAstName() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch73_244= tomMatch73_238.getAstType() ;
if ( (tomMatch73_243 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch73_244 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
if ( ( tomMatch73_244.getTlType()  instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {

if(getSymbolTable(moduleName).isUsedSymbolDestructor(
 tomMatch73_237.getString() )) {

buildGetSizeDecl(deep,tomMatch73_237, tomMatch73_243.getString() , tomMatch73_244.getTomType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getExpr() ,moduleName);
}
return;


}
}
}
}
}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyArray) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_253= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_254= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVarSize() ;
if ( (tomMatch73_253 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_opname= tomMatch73_253.getString() ;
if ( (tomMatch73_254 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {

TomType returnType = TomBase.getSymbolCodomain(getSymbolFromName(
tom_opname));
BQTerm newVar = 
 tom.engine.adt.code.types.bqterm.BQVariable.make( tomMatch73_254.getOptions() ,  tomMatch73_254.getAstName() , getSymbolTable(moduleName).getIntType()) ;
if(getSymbolTable(moduleName).isUsedSymbolConstructor(
tom_opname)) {

genDeclMake("tom_empty_array_",tom_opname,returnType, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(newVar, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstr() ,moduleName);
}
return;


}
}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeAddArray) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_264= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_265= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVarElt() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_266= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getVarList() ;
if ( (tomMatch73_264 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_opname= tomMatch73_264.getString() ;
if ( (tomMatch73_265 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch73_272= tomMatch73_265.getAstType() ;
if ( (tomMatch73_272 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
if ( ( tomMatch73_272.getTlType()  instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {
if ( (tomMatch73_266 instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch73_277= tomMatch73_266.getAstType() ;
if ( (tomMatch73_277 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
if ( ( tomMatch73_277.getTlType()  instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {

TomType returnType = 
tomMatch73_277;
if(getSymbolTable(moduleName).isUsedSymbolConstructor(
tom_opname)) {

genDeclMake("tom_cons_array_",tom_opname,returnType, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(tomMatch73_265, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(tomMatch73_266, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstr() ,moduleName);
}
return;


}
}
}
}
}
}
}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_283= (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstName() ;
if ( (tomMatch73_283 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_opname= tomMatch73_283.getString() ;

if(getSymbolTable(moduleName).isUsedSymbolConstructor(
tom_opname)) {

genDeclMake("tom_make_",tom_opname, (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getAstType() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getArgs() , (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getInstr() ,moduleName);
}
return;


}
}
}

}
{
if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )subject) instanceof tom.engine.adt.tomdeclaration.types.declaration.TypeTermDecl) ) {

generateDeclarationList(deep, 
 (( tom.engine.adt.tomdeclaration.types.Declaration )subject).getDeclarations() , moduleName);
return;


}
}

}
{
if ( (subject instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
 tom.engine.adt.tomdeclaration.types.Declaration  tom_t=(( tom.engine.adt.tomdeclaration.types.Declaration )subject);

System.out.println("Cannot generate code for declaration: " + 
tom_t);
throw new TomRuntimeException("Cannot generate code for declaration: " + 
tom_t);


}

}


}

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
protected abstract void buildGetImplementationDecl(int deep, String type, String name,
TargetLanguageType tlType, Instruction instr, String moduleName) throws IOException;

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

}
