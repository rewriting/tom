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
if ( (((Object)subject) instanceof tom.engine.adt.code.types.Code) ) {
if ( ((( tom.engine.adt.code.types.Code )((Object)subject)) instanceof tom.engine.adt.code.types.Code) ) {
if ( ((( tom.engine.adt.code.types.Code )(( tom.engine.adt.code.types.Code )((Object)subject))) instanceof tom.engine.adt.code.types.code.Tom) ) {

generateList(deep,
 (( tom.engine.adt.code.types.Code )((Object)subject)).getCodeList() , moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.code.types.Code) ) {
if ( ((( tom.engine.adt.code.types.Code )((Object)subject)) instanceof tom.engine.adt.code.types.Code) ) {
if ( ((( tom.engine.adt.code.types.Code )(( tom.engine.adt.code.types.Code )((Object)subject))) instanceof tom.engine.adt.code.types.code.TomInclude) ) {

generateListInclude(deep,
 (( tom.engine.adt.code.types.Code )((Object)subject)).getCodeList() , moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.code.types.Code) ) {
if ( ((( tom.engine.adt.code.types.Code )((Object)subject)) instanceof tom.engine.adt.code.types.Code) ) {
if ( ((( tom.engine.adt.code.types.Code )(( tom.engine.adt.code.types.Code )((Object)subject))) instanceof tom.engine.adt.code.types.code.BQTermToCode) ) {

generateBQTerm(deep,
 (( tom.engine.adt.code.types.Code )((Object)subject)).getBq() , moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.code.types.Code) ) {
if ( ((( tom.engine.adt.code.types.Code )((Object)subject)) instanceof tom.engine.adt.code.types.Code) ) {
if ( ((( tom.engine.adt.code.types.Code )(( tom.engine.adt.code.types.Code )((Object)subject))) instanceof tom.engine.adt.code.types.code.TargetLanguageToCode) ) {

generateTargetLanguage(deep,
 (( tom.engine.adt.code.types.Code )((Object)subject)).getTl() , moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.code.types.Code) ) {
if ( ((( tom.engine.adt.code.types.Code )((Object)subject)) instanceof tom.engine.adt.code.types.Code) ) {
if ( ((( tom.engine.adt.code.types.Code )(( tom.engine.adt.code.types.Code )((Object)subject))) instanceof tom.engine.adt.code.types.code.InstructionToCode) ) {

generateInstruction(deep,
 (( tom.engine.adt.code.types.Code )((Object)subject)).getAstInstruction() , moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.code.types.Code) ) {
if ( ((( tom.engine.adt.code.types.Code )((Object)subject)) instanceof tom.engine.adt.code.types.Code) ) {
if ( ((( tom.engine.adt.code.types.Code )(( tom.engine.adt.code.types.Code )((Object)subject))) instanceof tom.engine.adt.code.types.code.DeclarationToCode) ) {

generateDeclaration(deep,
 (( tom.engine.adt.code.types.Code )((Object)subject)).getAstDeclaration() , moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.code.types.Code) ) {
 tom.engine.adt.code.types.Code  tom_t=(( tom.engine.adt.code.types.Code )((Object)subject));

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
if ( (((Object)subject) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
boolean tomMatch62_4= false ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch62_3= null ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch62_2= null ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)subject)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)subject))) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch62_4= true ;
tomMatch62_2=(( tom.engine.adt.tomterm.types.TomTerm )((Object)subject));

}
} else {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)subject)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)subject))) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch62_4= true ;
tomMatch62_3=(( tom.engine.adt.tomterm.types.TomTerm )((Object)subject));

}
}
}
}
}
if (tomMatch62_4) {

output.write(deep,getVariableName(
(( tom.engine.adt.tomterm.types.TomTerm )((Object)subject))));
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
if ( (((Object)subject) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.BuildConstant) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch63_1= (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getAstName() ;
if ( (tomMatch63_1 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch63_1) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
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
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.BuildTerm) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch63_8= (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getAstName() ;
if ( (tomMatch63_8 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch63_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
buildTerm(deep, tomMatch63_8.getString() , (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getArgs() , (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getModuleName() );
return;


}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch63_24= false ;
 tom.engine.adt.code.types.BQTerm  tomMatch63_22= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch63_18= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch63_19= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch63_20= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch63_23= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch63_21= null ;
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyList) ) {
{
tomMatch63_24= true ;
tomMatch63_18=(( tom.engine.adt.code.types.BQTerm )((Object)subject));

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyArray) ) {
{
tomMatch63_24= true ;
tomMatch63_19=(( tom.engine.adt.code.types.BQTerm )((Object)subject));

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.BuildConsList) ) {
{
tomMatch63_24= true ;
tomMatch63_20=(( tom.engine.adt.code.types.BQTerm )((Object)subject));

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.BuildAppendList) ) {
{
tomMatch63_24= true ;
tomMatch63_21=(( tom.engine.adt.code.types.BQTerm )((Object)subject));

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.BuildConsArray) ) {
{
tomMatch63_24= true ;
tomMatch63_22=(( tom.engine.adt.code.types.BQTerm )((Object)subject));

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.BuildAppendArray) ) {
{
tomMatch63_24= true ;
tomMatch63_23=(( tom.engine.adt.code.types.BQTerm )((Object)subject));

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
}
}
}
if (tomMatch63_24) {

buildListOrArray(deep, 
(( tom.engine.adt.code.types.BQTerm )((Object)subject)), moduleName);
return;


}

}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.FunctionCall) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch63_26= (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getAstName() ;
if ( (tomMatch63_26 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch63_26) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

buildFunctionCall(deep,
 tomMatch63_26.getString() , 
 (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getArgs() , moduleName);
return;


}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch63_37= false ;
 tom.engine.adt.code.types.BQTerm  tomMatch63_36= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch63_35= null ;
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch63_37= true ;
tomMatch63_35=(( tom.engine.adt.code.types.BQTerm )((Object)subject));

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch63_37= true ;
tomMatch63_36=(( tom.engine.adt.code.types.BQTerm )((Object)subject));

}
}
}
}
}
if (tomMatch63_37) {

output.write(deep,getVariableName(
(( tom.engine.adt.code.types.BQTerm )((Object)subject))));
return;


}

}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) {

generateExpression(deep,
 (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getExp() , moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( (((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.ConsComposite) || ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.EmptyComposite)) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch63__end__46=(( tom.engine.adt.code.types.BQTerm )((Object)subject));
do {
{
if (!( tomMatch63__end__46.isEmptyComposite() )) {
 tom.engine.adt.code.types.CompositeMember  tom_t= tomMatch63__end__46.getHeadComposite() ;
{
{
if ( (((Object)tom_t) instanceof tom.engine.adt.code.types.CompositeMember) ) {
if ( ((( tom.engine.adt.code.types.CompositeMember )((Object)tom_t)) instanceof tom.engine.adt.code.types.CompositeMember) ) {
if ( ((( tom.engine.adt.code.types.CompositeMember )(( tom.engine.adt.code.types.CompositeMember )((Object)tom_t))) instanceof tom.engine.adt.code.types.compositemember.CompositeTL) ) {

generateTargetLanguage(deep,
 (( tom.engine.adt.code.types.CompositeMember )((Object)tom_t)).getTl() , moduleName);


}
}
}

}
{
if ( (((Object)tom_t) instanceof tom.engine.adt.code.types.CompositeMember) ) {
if ( ((( tom.engine.adt.code.types.CompositeMember )((Object)tom_t)) instanceof tom.engine.adt.code.types.CompositeMember) ) {
if ( ((( tom.engine.adt.code.types.CompositeMember )(( tom.engine.adt.code.types.CompositeMember )((Object)tom_t))) instanceof tom.engine.adt.code.types.compositemember.CompositeBQTerm) ) {

generateBQTerm(deep,
 (( tom.engine.adt.code.types.CompositeMember )((Object)tom_t)).getterm() , moduleName);


}
}
}

}


}



}
if ( tomMatch63__end__46.isEmptyComposite() ) {
tomMatch63__end__46=(( tom.engine.adt.code.types.BQTerm )((Object)subject));
} else {
tomMatch63__end__46= tomMatch63__end__46.getTailComposite() ;
}

}
} while(!( (tomMatch63__end__46==(( tom.engine.adt.code.types.BQTerm )((Object)subject))) ));
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.code.types.BQTerm) ) {
 tom.engine.adt.code.types.BQTerm  tom_t=(( tom.engine.adt.code.types.BQTerm )((Object)subject));
boolean tomMatch63_53= false ;
if ( (((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.ConsComposite) || ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.EmptyComposite)) ) {
if ( (tom_t==(( tom.engine.adt.code.types.BQTerm )((Object)subject))) ) {
tomMatch63_53= true ;
}
}
if (!(tomMatch63_53)) {

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
if ( (((Object)var) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)var)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)var))) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch65_1= (( tom.engine.adt.code.types.BQTerm )((Object)var)).getAstName() ;
if ( (tomMatch65_1 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch65_1) instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {

return ("tom" + TomBase.tomNumberListToString(
 tomMatch65_1.getNumberList() ));


}
}
}
}
}

}
{
if ( (((Object)var) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)var)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)var))) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch65_8= (( tom.engine.adt.code.types.BQTerm )((Object)var)).getAstName() ;
if ( (tomMatch65_8 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch65_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

return 
 tomMatch65_8.getString() ;


}
}
}
}
}

}
{
if ( (((Object)var) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)var)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)var))) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch65_15= (( tom.engine.adt.code.types.BQTerm )((Object)var)).getAstName() ;
if ( (tomMatch65_15 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch65_15) instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {

return ("tom" + TomBase.tomNumberListToString(
 tomMatch65_15.getNumberList() ));


}
}
}
}
}

}
{
if ( (((Object)var) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)var)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)var))) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch65_22= (( tom.engine.adt.code.types.BQTerm )((Object)var)).getAstName() ;
if ( (tomMatch65_22 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch65_22) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

return 
 tomMatch65_22.getString() ;


}
}
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
if ( (((Object)var) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)var)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)var))) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch66_1= (( tom.engine.adt.tomterm.types.TomTerm )((Object)var)).getAstName() ;
if ( (tomMatch66_1 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch66_1) instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {

return ("tom" + TomBase.tomNumberListToString(
 tomMatch66_1.getNumberList() ));


}
}
}
}
}

}
{
if ( (((Object)var) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)var)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)var))) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch66_8= (( tom.engine.adt.tomterm.types.TomTerm )((Object)var)).getAstName() ;
if ( (tomMatch66_8 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch66_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

return 
 tomMatch66_8.getString() ;


}
}
}
}
}

}
{
if ( (((Object)var) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)var)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)var))) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch66_15= (( tom.engine.adt.tomterm.types.TomTerm )((Object)var)).getAstName() ;
if ( (tomMatch66_15 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch66_15) instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {

return ("tom" + TomBase.tomNumberListToString(
 tomMatch66_15.getNumberList() ));


}
}
}
}
}

}
{
if ( (((Object)var) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)var)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)var))) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch66_22= (( tom.engine.adt.tomterm.types.TomTerm )((Object)var)).getAstName() ;
if ( (tomMatch66_22 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch66_22) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

return 
 tomMatch66_22.getString() ;


}
}
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
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.Code) ) {

output.write(
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getCode() );
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.Integer) ) {

output.write(
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getvalue() );
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.Negation) ) {

buildExpNegation(deep, 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getArg() , moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.Conditional) ) {

buildExpConditional(deep, 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getCond() , 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getThen() , 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getElse() , moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.And) ) {

buildExpAnd(deep, 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getArg1() , 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getArg2() , moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.Or) ) {

buildExpOr(deep, 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getArg1() , 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getArg2() , moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.GreaterThan) ) {

buildExpGreaterThan(deep, 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getArg1() , 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getArg2() , moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.GreaterOrEqualThan) ) {

buildExpGreaterOrEqualThan(deep, 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getArg1() , 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getArg2() , moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.LessThan) ) {

buildExpLessThan(deep, 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getArg1() , 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getArg2() , moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.LessOrEqualThan) ) {

buildExpLessOrEqualThan(deep, 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getArg1() , 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getArg2() , moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.Bottom) ) {

buildExpBottom(deep,
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getTomType() , moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.TrueTL) ) {

buildExpTrue(deep);
return;

}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.FalseTL) ) {

buildExpFalse(deep);
return;

}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.IsEmptyList) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch67_59= (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getOpname() ;
if ( (tomMatch67_59 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch67_59) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 tom.engine.adt.code.types.BQTerm  tom_expList= (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getVariable() ;
buildExpIsEmptyList(deep, tomMatch67_59.getString() ,getTermType(tom_expList),tom_expList,moduleName);
return;


}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.IsEmptyArray) ) {
 tom.engine.adt.code.types.BQTerm  tom_expArray= (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getVariable() ;

buildExpIsEmptyArray(deep, 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getOpname() , getTermType(
tom_expArray), 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getIndex() , 
tom_expArray, moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.EqualTerm) ) {
buildExpEqualTerm(deep, (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getTomType() , (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getKid1() , (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getKid2() ,moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.EqualBQTerm) ) {
buildExpEqualBQTerm(deep, (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getTomType() , (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).gett1() , (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).gett2() ,moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.IsSort) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch67_85= (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getAstType() ;
if ( (tomMatch67_85 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch67_85) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
buildExpIsSort(deep, tomMatch67_85.getTomType() , (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getVariable() ,moduleName);
return;


}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.IsFsym) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch67_93= (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getAstName() ;
if ( (tomMatch67_93 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch67_93) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

buildExpIsFsym(deep, 
 tomMatch67_93.getString() , 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getVariable() , moduleName);
return;


}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.Cast) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch67_101= (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getAstType() ;
if ( (tomMatch67_101 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch67_101) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch67_105= tomMatch67_101.getTlType() ;
if ( (tomMatch67_105 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {
if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch67_105) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {

buildExpCast(deep, 
tomMatch67_105, 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getSource() , moduleName);
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
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.GetSlot) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch67_112= (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getAstName() ;
 tom.engine.adt.code.types.BQTerm  tomMatch67_114= (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getVariable() ;
if ( (tomMatch67_112 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch67_112) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
boolean tomMatch67_124= false ;
 tom.engine.adt.code.types.BQTerm  tomMatch67_122= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch67_121= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch67_123= null ;
if ( (tomMatch67_114 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch67_114) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch67_124= true ;
tomMatch67_121=tomMatch67_114;

}
} else {
if ( (tomMatch67_114 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch67_114) instanceof tom.engine.adt.code.types.bqterm.BuildTerm) ) {
{
tomMatch67_124= true ;
tomMatch67_122=tomMatch67_114;

}
} else {
if ( (tomMatch67_114 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch67_114) instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) {
{
tomMatch67_124= true ;
tomMatch67_123=tomMatch67_114;

}
}
}
}
}
}
}
if (tomMatch67_124) {
buildExpGetSlot(deep, tomMatch67_112.getString() , (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getSlotNameString() ,tomMatch67_114,moduleName);
return;


}

}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.GetSlot) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch67_127= (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getAstName() ;
 tom.engine.adt.code.types.BQTerm  tomMatch67_129= (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getVariable() ;
if ( (tomMatch67_127 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch67_127) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch67_129 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch67_129) instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) {
 tom.engine.adt.tomexpression.types.Expression  tomMatch67_135= tomMatch67_129.getExp() ;
if ( (tomMatch67_135 instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch67_135) instanceof tom.engine.adt.tomexpression.types.expression.GetSlot) ) {
buildExpGetSlot(deep, tomMatch67_127.getString() , (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getSlotNameString() ,tomMatch67_129,moduleName);
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
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.GetHead) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch67_141= (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getOpname() ;
if ( (tomMatch67_141 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch67_141) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 tom.engine.adt.code.types.BQTerm  tom_exp= (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getVariable() ;
buildExpGetHead(deep, tomMatch67_141.getString() ,getTermType(tom_exp), (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getCodomain() ,tom_exp,moduleName);
return;


}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.GetTail) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch67_150= (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getOpname() ;
if ( (tomMatch67_150 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch67_150) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 tom.engine.adt.code.types.BQTerm  tom_exp= (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getVariable() ;
buildExpGetTail(deep, tomMatch67_150.getString() ,getTermType(tom_exp),tom_exp,moduleName);
return;


}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.AddOne) ) {

buildAddOne(deep, 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getVariable() , moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.SubstractOne) ) {

buildSubstractOne(deep, 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getVariable() , moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.Substract) ) {

buildSubstract(deep, 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getTerm1() , 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getTerm2() , moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.GetSize) ) {
 tom.engine.adt.code.types.BQTerm  tom_exp= (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getVariable() ;

buildExpGetSize(deep,
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getOpname() ,getTermType(
tom_exp), 
tom_exp, moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.GetElement) ) {
 tom.engine.adt.code.types.BQTerm  tom_varName= (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getVariable() ;

buildExpGetElement(deep,
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getOpname() ,getTermType(
tom_varName),
tom_varName, 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getIndex() , moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.GetSliceList) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch67_183= (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getAstName() ;
if ( (tomMatch67_183 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch67_183) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

buildExpGetSliceList(deep, 
 tomMatch67_183.getString() , 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getVariableBeginAST() , 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getVariableEndAST() , 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getTail() ,moduleName);
return;


}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.GetSliceArray) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch67_193= (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getAstName() ;
if ( (tomMatch67_193 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch67_193) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

buildExpGetSliceArray(deep, 
 tomMatch67_193.getString() , 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getSubjectListName() , 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getVariableBeginAST() , 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getVariableEndAST() , moduleName);
return;


}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.BQTermToExpression) ) {

generateBQTerm(deep,
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getAstTerm() , moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.TomInstructionToExpression) ) {

generateInstruction(deep, 
 (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getInstruction() , moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {
 tom.engine.adt.tomexpression.types.Expression  tom_t=(( tom.engine.adt.tomexpression.types.Expression )((Object)subject));

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
if ( (((Object)subject) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch68_1= (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getAstName() ;
if ( (tomMatch68_1 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch68_1) instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {

output.write("tom" + TomBase.tomNumberListToString(
 tomMatch68_1.getNumberList() ));        


}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch68_8= (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getAstName() ;
if ( (tomMatch68_8 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch68_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

output.write(
 tomMatch68_8.getString() );        


}
}
}
}
}

}


}
{
{
if ( (((Object)index) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)index)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)index))) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch69_1= (( tom.engine.adt.code.types.BQTerm )((Object)index)).getAstName() ;
if ( (tomMatch69_1 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch69_1) instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {

output.write("[");
output.write("tom" + TomBase.tomNumberListToString(
 tomMatch69_1.getNumberList() ));
output.write("]");        


}
}
}
}
}

}
{
if ( (((Object)index) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)index)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)index))) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch69_8= (( tom.engine.adt.code.types.BQTerm )((Object)index)).getAstName() ;
if ( (tomMatch69_8 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch69_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

output.write("[");
output.write(
 tomMatch69_8.getString() );
output.write("]");


}
}
}
}
}

}
{
if ( (((Object)index) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)index)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)index))) instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) {
 tom.engine.adt.tomexpression.types.Expression  tomMatch69_15= (( tom.engine.adt.code.types.BQTerm )((Object)index)).getExp() ;
if ( (tomMatch69_15 instanceof tom.engine.adt.tomexpression.types.Expression) ) {
if ( ((( tom.engine.adt.tomexpression.types.Expression )tomMatch69_15) instanceof tom.engine.adt.tomexpression.types.expression.Integer) ) {

output.write("[");
output.write(
 tomMatch69_15.getvalue() );
output.write("]");  


}
}
}
}
}

}


}

} 

public void generateInstruction(int deep, Instruction subject, String moduleName) throws IOException {

{
{
if ( (((Object)subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )((Object)subject))) instanceof tom.engine.adt.tominstruction.types.instruction.CodeToInstruction) ) {
generate(deep, (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getCode() ,moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )((Object)subject))) instanceof tom.engine.adt.tominstruction.types.instruction.BQTermToInstruction) ) {
generateBQTerm(deep, (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getTom() ,moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )((Object)subject))) instanceof tom.engine.adt.tominstruction.types.instruction.ExpressionToInstruction) ) {
generateExpression(deep, (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getExpr() ,moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )((Object)subject))) instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {

return;

}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )((Object)subject))) instanceof tom.engine.adt.tominstruction.types.instruction.Assign) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch70_16= (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getVariable() ;
boolean tomMatch70_24= false ;
 tom.engine.adt.code.types.BQTerm  tomMatch70_22= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch70_23= null ;
 tom.engine.adt.tomoption.types.OptionList  tomMatch70_20= null ;
if ( (tomMatch70_16 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch70_16) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch70_24= true ;
tomMatch70_22=tomMatch70_16;
tomMatch70_20= tomMatch70_22.getOptions() ;

}
} else {
if ( (tomMatch70_16 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch70_16) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch70_24= true ;
tomMatch70_23=tomMatch70_16;
tomMatch70_20= tomMatch70_23.getOptions() ;

}
}
}
}
}
if (tomMatch70_24) {
buildAssign(deep,tomMatch70_16,tomMatch70_20, (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getSource() ,moduleName);
return;


}

}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )((Object)subject))) instanceof tom.engine.adt.tominstruction.types.instruction.AssignArray) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch70_26= (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getVariable() ;
if ( (tomMatch70_26 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch70_26) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
buildAssignArray(deep,tomMatch70_26, tomMatch70_26.getOptions() , (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getIndex() , (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getSource() ,moduleName);
return;


}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )((Object)subject))) instanceof tom.engine.adt.tominstruction.types.instruction.Let) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch70_35= (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getVariable() ;
boolean tomMatch70_48= false ;
 tom.engine.adt.tomoption.types.OptionList  tomMatch70_40= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch70_44= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch70_41= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch70_43= null ;
if ( (tomMatch70_35 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch70_35) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch70_48= true ;
tomMatch70_43=tomMatch70_35;
tomMatch70_40= tomMatch70_43.getOptions() ;
tomMatch70_41= tomMatch70_43.getAstType() ;

}
} else {
if ( (tomMatch70_35 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch70_35) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch70_48= true ;
tomMatch70_44=tomMatch70_35;
tomMatch70_40= tomMatch70_44.getOptions() ;
tomMatch70_41= tomMatch70_44.getAstType() ;

}
}
}
}
}
if (tomMatch70_48) {
if ( (tomMatch70_41 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch70_41) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
buildLet(deep,tomMatch70_35,tomMatch70_40, tomMatch70_41.getTlType() , (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getSource() , (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getAstInstruction() ,moduleName);
return;


}
}
}

}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )((Object)subject))) instanceof tom.engine.adt.tominstruction.types.instruction.LetRef) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch70_50= (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getVariable() ;
boolean tomMatch70_63= false ;
 tom.engine.adt.code.types.BQTerm  tomMatch70_58= null ;
 tom.engine.adt.tomoption.types.OptionList  tomMatch70_55= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch70_56= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch70_59= null ;
if ( (tomMatch70_50 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch70_50) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch70_63= true ;
tomMatch70_58=tomMatch70_50;
tomMatch70_55= tomMatch70_58.getOptions() ;
tomMatch70_56= tomMatch70_58.getAstType() ;

}
} else {
if ( (tomMatch70_50 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch70_50) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch70_63= true ;
tomMatch70_59=tomMatch70_50;
tomMatch70_55= tomMatch70_59.getOptions() ;
tomMatch70_56= tomMatch70_59.getAstType() ;

}
}
}
}
}
if (tomMatch70_63) {
if ( (tomMatch70_56 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch70_56) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
buildLetRef(deep,tomMatch70_50,tomMatch70_55, tomMatch70_56.getTlType() , (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getSource() , (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getAstInstruction() ,moduleName);
return;


}
}
}

}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )((Object)subject))) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) {

//`generateInstructionList(deep, instList);

buildInstructionSequence(deep, (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getInstList() ,moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )((Object)subject))) instanceof tom.engine.adt.tominstruction.types.instruction.UnamedBlock) ) {
buildUnamedBlock(deep, (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getInstList() ,moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )((Object)subject))) instanceof tom.engine.adt.tominstruction.types.instruction.NamedBlock) ) {
buildNamedBlock(deep, (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getBlockName() , (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getInstList() ,moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )((Object)subject))) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {
 tom.engine.adt.tominstruction.types.Instruction  tomMatch70_80= (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getFailureInst() ;
if ( (tomMatch70_80 instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tomMatch70_80) instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {
buildIf(deep, (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getCondition() , (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getSuccesInst() ,moduleName);
return;


}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )((Object)subject))) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {
 tom.engine.adt.tominstruction.types.Instruction  tomMatch70_87= (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getSuccesInst() ;
if ( (tomMatch70_87 instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tomMatch70_87) instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {
buildIf(deep, tom.engine.adt.tomexpression.types.expression.Negation.make( (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getCondition() ) , (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getFailureInst() ,moduleName);
return;


}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )((Object)subject))) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {
buildIfWithFailure(deep, (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getCondition() , (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getSuccesInst() , (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getFailureInst() ,moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )((Object)subject))) instanceof tom.engine.adt.tominstruction.types.instruction.DoWhile) ) {
buildDoWhile(deep, (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getDoInst() , (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getCondition() ,moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )((Object)subject))) instanceof tom.engine.adt.tominstruction.types.instruction.WhileDo) ) {
buildWhileDo(deep, (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getCondition() , (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getDoInst() ,moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )((Object)subject))) instanceof tom.engine.adt.tominstruction.types.instruction.RawAction) ) {
 tom.engine.adt.tominstruction.types.Instruction  tomMatch70_110= (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getAstInstruction() ;
if ( (tomMatch70_110 instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )tomMatch70_110) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) {
generateInstructionList(deep, tomMatch70_110.getInstList() ,moduleName);
return;


}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )((Object)subject))) instanceof tom.engine.adt.tominstruction.types.instruction.RawAction) ) {
generateInstruction(deep, (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getAstInstruction() ,moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )((Object)subject))) instanceof tom.engine.adt.tominstruction.types.instruction.Return) ) {
buildReturn(deep, (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getKid1() ,moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )((Object)subject))) instanceof tom.engine.adt.tominstruction.types.instruction.CompiledMatch) ) {

//TODO moduleName

generateInstruction(deep, (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getAutomataInst() ,moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )((Object)subject))) instanceof tom.engine.adt.tominstruction.types.instruction.CompiledPattern) ) {
generateInstruction(deep, (( tom.engine.adt.tominstruction.types.Instruction )((Object)subject)).getAutomataInst() ,moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
 tom.engine.adt.tominstruction.types.Instruction  tom_t=(( tom.engine.adt.tominstruction.types.Instruction )((Object)subject));

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
if ( (((Object)subject) instanceof tom.engine.adt.code.types.TargetLanguage) ) {
if ( ((( tom.engine.adt.code.types.TargetLanguage )((Object)subject)) instanceof tom.engine.adt.code.types.TargetLanguage) ) {
if ( ((( tom.engine.adt.code.types.TargetLanguage )(( tom.engine.adt.code.types.TargetLanguage )((Object)subject))) instanceof tom.engine.adt.code.types.targetlanguage.TL) ) {
 tom.engine.adt.tomsignature.types.TextPosition  tomMatch71_2= (( tom.engine.adt.code.types.TargetLanguage )((Object)subject)).getStart() ;
 tom.engine.adt.tomsignature.types.TextPosition  tomMatch71_3= (( tom.engine.adt.code.types.TargetLanguage )((Object)subject)).getEnd() ;
if ( (tomMatch71_2 instanceof tom.engine.adt.tomsignature.types.TextPosition) ) {
if ( ((( tom.engine.adt.tomsignature.types.TextPosition )tomMatch71_2) instanceof tom.engine.adt.tomsignature.types.textposition.TextPosition) ) {
 int  tom_startLine= tomMatch71_2.getLine() ;
if ( (tomMatch71_3 instanceof tom.engine.adt.tomsignature.types.TextPosition) ) {
if ( ((( tom.engine.adt.tomsignature.types.TextPosition )tomMatch71_3) instanceof tom.engine.adt.tomsignature.types.textposition.TextPosition) ) {

output.write(deep, 
 (( tom.engine.adt.code.types.TargetLanguage )((Object)subject)).getCode() , 
tom_startLine, 
 tomMatch71_3.getLine() - 
tom_startLine);
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
if ( (((Object)subject) instanceof tom.engine.adt.code.types.TargetLanguage) ) {
if ( ((( tom.engine.adt.code.types.TargetLanguage )((Object)subject)) instanceof tom.engine.adt.code.types.TargetLanguage) ) {
if ( ((( tom.engine.adt.code.types.TargetLanguage )(( tom.engine.adt.code.types.TargetLanguage )((Object)subject))) instanceof tom.engine.adt.code.types.targetlanguage.ITL) ) {

output.write(
 (( tom.engine.adt.code.types.TargetLanguage )((Object)subject)).getCode() );
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.code.types.TargetLanguage) ) {
if ( ((( tom.engine.adt.code.types.TargetLanguage )((Object)subject)) instanceof tom.engine.adt.code.types.TargetLanguage) ) {
if ( ((( tom.engine.adt.code.types.TargetLanguage )(( tom.engine.adt.code.types.TargetLanguage )((Object)subject))) instanceof tom.engine.adt.code.types.targetlanguage.Comment) ) {
buildComment(deep, (( tom.engine.adt.code.types.TargetLanguage )((Object)subject)).getCode() );
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.code.types.TargetLanguage) ) {
 tom.engine.adt.code.types.TargetLanguage  tom_t=(( tom.engine.adt.code.types.TargetLanguage )((Object)subject));

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
if ( (((Object)subject) instanceof tom.engine.adt.tomoption.types.Option) ) {
if ( ((( tom.engine.adt.tomoption.types.Option )((Object)subject)) instanceof tom.engine.adt.tomoption.types.Option) ) {
if ( ((( tom.engine.adt.tomoption.types.Option )(( tom.engine.adt.tomoption.types.Option )((Object)subject))) instanceof tom.engine.adt.tomoption.types.option.DeclarationToOption) ) {
generateDeclaration(deep, (( tom.engine.adt.tomoption.types.Option )((Object)subject)).getAstDeclaration() ,moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomoption.types.Option) ) {
if ( ((( tom.engine.adt.tomoption.types.Option )((Object)subject)) instanceof tom.engine.adt.tomoption.types.Option) ) {
if ( ((( tom.engine.adt.tomoption.types.Option )(( tom.engine.adt.tomoption.types.Option )((Object)subject))) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {
return; 
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomoption.types.Option) ) {
if ( ((( tom.engine.adt.tomoption.types.Option )((Object)subject)) instanceof tom.engine.adt.tomoption.types.Option) ) {
if ( ((( tom.engine.adt.tomoption.types.Option )(( tom.engine.adt.tomoption.types.Option )((Object)subject))) instanceof tom.engine.adt.tomoption.types.option.ACSymbol) ) {

// TODO RK: here add the declarations for intarray
return; 

}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomoption.types.Option) ) {

throw new TomRuntimeException("Cannot generate code for option: " + 
(( tom.engine.adt.tomoption.types.Option )((Object)subject)));


}

}


}

}

public void generateDeclaration(int deep, Declaration subject, String moduleName) throws IOException {

{
{
if ( (((Object)subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject))) instanceof tom.engine.adt.tomdeclaration.types.declaration.EmptyDeclaration) ) {

return;

}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject))) instanceof tom.engine.adt.tomdeclaration.types.declaration.AbstractDecl) ) {

//`generateInstructionList(deep, instList);

generateDeclarationList(deep, (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getDeclList() ,moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject))) instanceof tom.engine.adt.tomdeclaration.types.declaration.FunctionDef) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_8= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getAstName() ;
if ( (tomMatch73_8 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
buildFunctionDef(deep, tomMatch73_8.getString() , (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getArgumentList() , (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getCodomain() , (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getThrowsType() , (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getInstruction() ,moduleName);
return;


}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject))) instanceof tom.engine.adt.tomdeclaration.types.declaration.MethodDef) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_19= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getAstName() ;
if ( (tomMatch73_19 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_19) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
buildMethodDef(deep, tomMatch73_19.getString() , (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getArgumentList() , (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getCodomain() , (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getThrowsType() , (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getInstruction() ,moduleName);
return;


}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject))) instanceof tom.engine.adt.tomdeclaration.types.declaration.Class) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_30= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getAstName() ;
if ( (tomMatch73_30 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_30) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
buildClass(deep, tomMatch73_30.getString() , (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getExtendsType() , (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getSuperTerm() , (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getDeclaration() ,moduleName);
return;


}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject))) instanceof tom.engine.adt.tomdeclaration.types.declaration.IntrospectorClass) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_40= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getAstName() ;
if ( (tomMatch73_40 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_40) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
buildIntrospectorClass(deep, tomMatch73_40.getString() , (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getDeclaration() ,moduleName);
return;


}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject))) instanceof tom.engine.adt.tomdeclaration.types.declaration.SymbolDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_48= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getAstName() ;
if ( (tomMatch73_48 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_48) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
buildSymbolDecl(deep, tomMatch73_48.getString() ,moduleName);
return ;


}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject))) instanceof tom.engine.adt.tomdeclaration.types.declaration.ArraySymbolDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_55= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getAstName() ;
if ( (tomMatch73_55 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_55) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_tomName= tomMatch73_55.getString() ;

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
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
boolean tomMatch73_69= false ;
 tom.engine.adt.tomdeclaration.types.Declaration  tomMatch73_65= null ;
 tom.engine.adt.tomdeclaration.types.Declaration  tomMatch73_64= null ;
 tom.engine.adt.tomname.types.TomName  tomMatch73_62= null ;
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject))) instanceof tom.engine.adt.tomdeclaration.types.declaration.ListSymbolDecl) ) {
{
tomMatch73_69= true ;
tomMatch73_64=(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject));
tomMatch73_62= tomMatch73_64.getAstName() ;

}
} else {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject))) instanceof tom.engine.adt.tomdeclaration.types.declaration.ACSymbolDecl) ) {
{
tomMatch73_69= true ;
tomMatch73_65=(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject));
tomMatch73_62= tomMatch73_65.getAstName() ;

}
}
}
}
}
if (tomMatch73_69) {
if ( (tomMatch73_62 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_62) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_tomName= tomMatch73_62.getString() ;

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

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject))) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetImplementationDecl) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch73_71= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getVariable() ;
if ( (tomMatch73_71 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch73_71) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_76= tomMatch73_71.getAstName() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch73_77= tomMatch73_71.getAstType() ;
if ( (tomMatch73_76 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_76) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_name= tomMatch73_76.getString() ;
if ( (tomMatch73_77 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch73_77) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch73_84= tomMatch73_77.getTlType() ;
if ( (tomMatch73_84 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {
if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch73_84) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {

if(getSymbolTable(moduleName).isUsedSymbolDestructor(
tom_name)) {

buildGetImplementationDecl(deep, tomMatch73_77.getTomType() ,tom_name,tomMatch73_84, (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getInstr() ,moduleName);
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
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject))) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsFsymDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_90= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getAstName() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_91= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getVariable() ;
if ( (tomMatch73_90 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_90) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_tomName= tomMatch73_90.getString() ;
if ( (tomMatch73_91 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch73_91) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_99= tomMatch73_91.getAstName() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch73_100= tomMatch73_91.getAstType() ;
if ( (tomMatch73_99 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_99) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch73_100 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch73_100) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch73_106= tomMatch73_100.getTlType() ;
if ( (tomMatch73_106 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {
if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch73_106) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {

if(getSymbolTable(moduleName).isUsedSymbolDestructor(
tom_tomName)) {

buildIsFsymDecl(deep,tom_tomName, tomMatch73_99.getString() ,tomMatch73_106, (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getExpr() ,moduleName);
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
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject))) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetSlotDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_112= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getAstName() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_114= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getVariable() ;
if ( (tomMatch73_112 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_112) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_tomName= tomMatch73_112.getString() ;
if ( (tomMatch73_114 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch73_114) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_121= tomMatch73_114.getAstName() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch73_122= tomMatch73_114.getAstType() ;
if ( (tomMatch73_121 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_121) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch73_122 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch73_122) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch73_128= tomMatch73_122.getTlType() ;
if ( (tomMatch73_128 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {
if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch73_128) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {

if(getSymbolTable(moduleName).isUsedSymbolDestructor(
tom_tomName)) {

buildGetSlotDecl(deep,tom_tomName, tomMatch73_121.getString() ,tomMatch73_128, (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getExpr() , (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getSlotName() ,moduleName);
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
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject))) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetDefaultDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_134= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getAstName() ;
if ( (tomMatch73_134 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_134) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_tomName= tomMatch73_134.getString() ;

if(getSymbolTable(moduleName).isUsedSymbolConstructor(
tom_tomName)) {

buildGetDefaultDecl(deep,tom_tomName, (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getExpr() , (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getSlotName() ,moduleName);
}
return;


}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject))) instanceof tom.engine.adt.tomdeclaration.types.declaration.EqualTermDecl) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch73_143= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getTermArg1() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_144= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getTermArg2() ;
if ( (tomMatch73_143 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch73_143) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_149= tomMatch73_143.getAstName() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch73_150= tomMatch73_143.getAstType() ;
if ( (tomMatch73_149 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_149) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch73_150 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch73_150) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 String  tom_type1= tomMatch73_150.getTomType() ;
if ( (tomMatch73_144 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch73_144) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_159= tomMatch73_144.getAstName() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch73_160= tomMatch73_144.getAstType() ;
if ( (tomMatch73_159 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_159) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch73_160 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch73_160) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {

if(getSymbolTable(moduleName).isUsedType(
tom_type1)) {

buildEqualTermDecl(deep, tomMatch73_149.getString() , tomMatch73_159.getString() ,tom_type1, tomMatch73_160.getTomType() , (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getExpr() ,moduleName);
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
}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject))) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsSortDecl) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch73_170= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getTermArg() ;
if ( (tomMatch73_170 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch73_170) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_175= tomMatch73_170.getAstName() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch73_176= tomMatch73_170.getAstType() ;
if ( (tomMatch73_175 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_175) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch73_176 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch73_176) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 String  tom_type= tomMatch73_176.getTomType() ;

if(getSymbolTable(moduleName).isUsedType(
tom_type)) {

buildIsSortDecl(deep, tomMatch73_175.getString() ,tom_type, (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getExpr() ,moduleName);
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
if ( (((Object)subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject))) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetHeadDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_186= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getOpname() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch73_187= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getCodomain() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_188= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getVariable() ;
if ( (tomMatch73_186 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_186) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_opname= tomMatch73_186.getString() ;
if ( (tomMatch73_187 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch73_187) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
if ( (tomMatch73_188 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch73_188) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_198= tomMatch73_188.getAstName() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch73_199= tomMatch73_188.getAstType() ;
if ( (tomMatch73_198 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_198) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch73_199 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch73_199) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch73_206= tomMatch73_199.getTlType() ;
if ( (tomMatch73_206 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {
if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch73_206) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {

if(getSymbolTable(moduleName).isUsedSymbolConstructor(
tom_opname) 
||getSymbolTable(moduleName).isUsedSymbolDestructor(
tom_opname)) {

buildGetHeadDecl(deep,tomMatch73_186, tomMatch73_198.getString() , tomMatch73_199.getTomType() ,tomMatch73_206, tomMatch73_187.getTlType() , (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getExpr() ,moduleName);
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
}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject))) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetTailDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_212= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getOpname() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_213= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getVariable() ;
if ( (tomMatch73_212 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_212) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_opname= tomMatch73_212.getString() ;
if ( (tomMatch73_213 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch73_213) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_220= tomMatch73_213.getAstName() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch73_221= tomMatch73_213.getAstType() ;
if ( (tomMatch73_220 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_220) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch73_221 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch73_221) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch73_228= tomMatch73_221.getTlType() ;
if ( (tomMatch73_228 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {
if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch73_228) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {

if(getSymbolTable(moduleName).isUsedSymbolConstructor(
tom_opname) 
||getSymbolTable(moduleName).isUsedSymbolDestructor(
tom_opname)) {

buildGetTailDecl(deep,tomMatch73_212, tomMatch73_220.getString() , tomMatch73_221.getTomType() ,tomMatch73_228, (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getExpr() ,moduleName);
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
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject))) instanceof tom.engine.adt.tomdeclaration.types.declaration.IsEmptyDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_234= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getOpname() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_235= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getVariable() ;
if ( (tomMatch73_234 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_234) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_opname= tomMatch73_234.getString() ;
if ( (tomMatch73_235 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch73_235) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_242= tomMatch73_235.getAstName() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch73_243= tomMatch73_235.getAstType() ;
if ( (tomMatch73_242 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_242) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch73_243 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch73_243) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch73_250= tomMatch73_243.getTlType() ;
if ( (tomMatch73_250 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {
if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch73_250) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {

if(getSymbolTable(moduleName).isUsedSymbolConstructor(
tom_opname) 
||getSymbolTable(moduleName).isUsedSymbolDestructor(
tom_opname)) {

buildIsEmptyDecl(deep,tomMatch73_234, tomMatch73_242.getString() , tomMatch73_243.getTomType() ,tomMatch73_250, (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getExpr() ,moduleName);
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
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject))) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyList) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_256= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getAstName() ;
if ( (tomMatch73_256 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_256) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_opname= tomMatch73_256.getString() ;

TomType returnType = TomBase.getSymbolCodomain(getSymbolFromName(
tom_opname));
if(getSymbolTable(moduleName).isUsedSymbolConstructor(
tom_opname) 
|| getSymbolTable(moduleName).isUsedSymbolDestructor(
tom_opname)) {

genDeclMake("tom_empty_list_",tom_opname,returnType, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() , (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getInstr() ,moduleName);
}
return;


}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject))) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeAddList) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_265= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getAstName() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_266= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getVarElt() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_267= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getVarList() ;
if ( (tomMatch73_265 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_265) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_opname= tomMatch73_265.getString() ;
if ( (tomMatch73_266 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch73_266) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch73_275= tomMatch73_266.getAstType() ;
if ( (tomMatch73_275 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch73_275) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch73_278= tomMatch73_275.getTlType() ;
if ( (tomMatch73_278 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {
if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch73_278) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {
if ( (tomMatch73_267 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch73_267) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch73_283= tomMatch73_267.getAstType() ;
if ( (tomMatch73_283 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch73_283) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch73_286= tomMatch73_283.getTlType() ;
if ( (tomMatch73_286 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {
if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch73_286) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {

TomType returnType = 
tomMatch73_283;
if(getSymbolTable(moduleName).isUsedSymbolConstructor(
tom_opname) 
||getSymbolTable(moduleName).isUsedSymbolDestructor(
tom_opname)) {

genDeclMake("tom_cons_list_",tom_opname,returnType, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(tomMatch73_266, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(tomMatch73_267, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) , (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getInstr() ,moduleName);
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
}
}
}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject))) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetElementDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_292= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getOpname() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_293= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getVariable() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_294= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getIndex() ;
if ( (tomMatch73_292 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_292) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch73_293 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch73_293) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_301= tomMatch73_293.getAstName() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch73_302= tomMatch73_293.getAstType() ;
if ( (tomMatch73_301 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_301) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch73_302 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch73_302) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch73_309= tomMatch73_302.getTlType() ;
if ( (tomMatch73_309 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {
if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch73_309) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {
if ( (tomMatch73_294 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch73_294) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_314= tomMatch73_294.getAstName() ;
if ( (tomMatch73_314 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_314) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

if(getSymbolTable(moduleName).isUsedSymbolDestructor(
 tomMatch73_292.getString() )) {

buildGetElementDecl(deep,tomMatch73_292, tomMatch73_301.getString() , tomMatch73_314.getString() , tomMatch73_302.getTomType() , (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getExpr() ,moduleName);
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
}
}
}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject))) instanceof tom.engine.adt.tomdeclaration.types.declaration.GetSizeDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_321= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getOpname() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_322= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getVariable() ;
if ( (tomMatch73_321 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_321) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch73_322 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch73_322) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_329= tomMatch73_322.getAstName() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch73_330= tomMatch73_322.getAstType() ;
if ( (tomMatch73_329 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_329) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
if ( (tomMatch73_330 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch73_330) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch73_337= tomMatch73_330.getTlType() ;
if ( (tomMatch73_337 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {
if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch73_337) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {

if(getSymbolTable(moduleName).isUsedSymbolDestructor(
 tomMatch73_321.getString() )) {

buildGetSizeDecl(deep,tomMatch73_321, tomMatch73_329.getString() , tomMatch73_330.getTomType() , (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getExpr() ,moduleName);
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
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject))) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeEmptyArray) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_343= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getAstName() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_344= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getVarSize() ;
if ( (tomMatch73_343 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_343) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_opname= tomMatch73_343.getString() ;
if ( (tomMatch73_344 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch73_344) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {

TomType returnType = TomBase.getSymbolCodomain(getSymbolFromName(
tom_opname));
BQTerm newVar = 
 tom.engine.adt.code.types.bqterm.BQVariable.make( tomMatch73_344.getOptions() ,  tomMatch73_344.getAstName() , getSymbolTable(moduleName).getIntType()) ;
if(getSymbolTable(moduleName).isUsedSymbolConstructor(
tom_opname)) {

genDeclMake("tom_empty_array_",tom_opname,returnType, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(newVar, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) , (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getInstr() ,moduleName);
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
if ( (((Object)subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject))) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeAddArray) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_357= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getAstName() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_358= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getVarElt() ;
 tom.engine.adt.code.types.BQTerm  tomMatch73_359= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getVarList() ;
if ( (tomMatch73_357 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_357) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_opname= tomMatch73_357.getString() ;
if ( (tomMatch73_358 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch73_358) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch73_367= tomMatch73_358.getAstType() ;
if ( (tomMatch73_367 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch73_367) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch73_370= tomMatch73_367.getTlType() ;
if ( (tomMatch73_370 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {
if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch73_370) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {
if ( (tomMatch73_359 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch73_359) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch73_375= tomMatch73_359.getAstType() ;
if ( (tomMatch73_375 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch73_375) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch73_378= tomMatch73_375.getTlType() ;
if ( (tomMatch73_378 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {
if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch73_378) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.TLType) ) {

TomType returnType = 
tomMatch73_375;
if(getSymbolTable(moduleName).isUsedSymbolConstructor(
tom_opname)) {

genDeclMake("tom_cons_array_",tom_opname,returnType, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(tomMatch73_358, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(tomMatch73_359, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) , (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getInstr() ,moduleName);
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
}
}
}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject))) instanceof tom.engine.adt.tomdeclaration.types.declaration.MakeDecl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch73_384= (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getAstName() ;
if ( (tomMatch73_384 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch73_384) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_opname= tomMatch73_384.getString() ;

if(getSymbolTable(moduleName).isUsedSymbolConstructor(
tom_opname)) {

genDeclMake("tom_make_",tom_opname, (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getAstType() , (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getArgs() , (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getInstr() ,moduleName);
}
return;


}
}
}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
if ( ((( tom.engine.adt.tomdeclaration.types.Declaration )(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject))) instanceof tom.engine.adt.tomdeclaration.types.declaration.TypeTermDecl) ) {

generateDeclarationList(deep, 
 (( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject)).getDeclarations() , moduleName);
return;


}
}
}

}
{
if ( (((Object)subject) instanceof tom.engine.adt.tomdeclaration.types.Declaration) ) {
 tom.engine.adt.tomdeclaration.types.Declaration  tom_t=(( tom.engine.adt.tomdeclaration.types.Declaration )((Object)subject));

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
