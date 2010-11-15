/*
* Gom
*
* Copyright (c) 2006-2010, INPL, INRIA
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
* Antoine Reilles  e-mail: Antoine.Reilles@loria.fr
*
**/

package tom.gom.backend;

import java.io.Writer;
import java.io.StringWriter;
import java.util.logging.Logger;
import tom.gom.GomMessage;
import tom.gom.tools.error.GomRuntimeException;
import tom.gom.adt.code.types.*;
import tom.gom.adt.objects.types.*;

public class CodeGen {



  private static   tom.gom.adt.code.types.Code  tom_append_list_CodeList( tom.gom.adt.code.types.Code  l1,  tom.gom.adt.code.types.Code  l2) {
    if( l1.isEmptyCodeList() ) {
      return l2;
    } else if( l2.isEmptyCodeList() ) {
      return l1;
    } else if( ((l1 instanceof tom.gom.adt.code.types.code.ConsCodeList) || (l1 instanceof tom.gom.adt.code.types.code.EmptyCodeList)) ) {
      if(  l1.getTailCodeList() .isEmptyCodeList() ) {
        return  tom.gom.adt.code.types.code.ConsCodeList.make( l1.getHeadCodeList() ,l2) ;
      } else {
        return  tom.gom.adt.code.types.code.ConsCodeList.make( l1.getHeadCodeList() ,tom_append_list_CodeList( l1.getTailCodeList() ,l2)) ;
      }
    } else {
      return  tom.gom.adt.code.types.code.ConsCodeList.make(l1,l2) ;
    }
  }
  private static   tom.gom.adt.code.types.Code  tom_get_slice_CodeList( tom.gom.adt.code.types.Code  begin,  tom.gom.adt.code.types.Code  end, tom.gom.adt.code.types.Code  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyCodeList()  ||  (end== tom.gom.adt.code.types.code.EmptyCodeList.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.gom.adt.code.types.code.ConsCodeList.make((( ((begin instanceof tom.gom.adt.code.types.code.ConsCodeList) || (begin instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )? begin.getHeadCodeList() :begin),( tom.gom.adt.code.types.Code )tom_get_slice_CodeList((( ((begin instanceof tom.gom.adt.code.types.code.ConsCodeList) || (begin instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )? begin.getTailCodeList() : tom.gom.adt.code.types.code.EmptyCodeList.make() ),end,tail)) ;
  }
  

private static Logger logger = Logger.getLogger("CodeGen");

private CodeGen() {
/* Prevent instantiation */
}

/**
* Generate code in a String.
*
* @params code the Code to generate
* @return the generated code
*/
public static String generateCode(Code code) {
StringWriter writer = new StringWriter();
try {
generateCode(code,writer);
} catch (java.io.IOException e) {
GomMessage.error(logger, null, 0, 
GomMessage.codeGenerationFailure , code);
}
return writer.toString();
}

/**
* Generate code in a writer.
*
* @params code the Code to generate
* @params writer where to generate
*/
public static void generateCode(Code code, Writer writer)
throws java.io.IOException {

{
{
if ( (code instanceof tom.gom.adt.code.types.Code) ) {
if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.Code) ) {

writer.write(
 (( tom.gom.adt.code.types.Code )code).getprog() );
return;


}
}

}
{
if ( (code instanceof tom.gom.adt.code.types.Code) ) {
boolean tomMatch460_6= false ;
 tom.gom.adt.gom.types.OperatorDecl  tomMatch460_4= null ;
if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.Empty) ) {
{
tomMatch460_6= true ;
tomMatch460_4= (( tom.gom.adt.code.types.Code )code).getOperator() ;

}
} else {
if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.Cons) ) {
{
tomMatch460_6= true ;
tomMatch460_4= (( tom.gom.adt.code.types.Code )code).getOperator() ;

}
}
}
if (tomMatch460_6) {
 tom.gom.adt.gom.types.OperatorDecl  tom_opdecl=tomMatch460_4;
{
{
if ( (tom_opdecl instanceof tom.gom.adt.gom.types.OperatorDecl) ) {
if ( ((( tom.gom.adt.gom.types.OperatorDecl )tom_opdecl) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {
 tom.gom.adt.gom.types.SortDecl  tomMatch461_2= (( tom.gom.adt.gom.types.OperatorDecl )tom_opdecl).getSort() ;
 String  tom_opName= (( tom.gom.adt.gom.types.OperatorDecl )tom_opdecl).getName() ;
if ( (tomMatch461_2 instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {
if ( ( (( tom.gom.adt.gom.types.OperatorDecl )tom_opdecl).getProd()  instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) {

String tName = 
tom_opName;

{
{
if ( (code instanceof tom.gom.adt.code.types.Code) ) {
if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.Empty) ) {

tName = "Empty" + 
tom_opName;


}
}

}
{
if ( (code instanceof tom.gom.adt.code.types.Code) ) {
if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.Cons) ) {

tName = "Cons" + 
tom_opName;


}
}

}


}

String sortNamePackage = 
 tomMatch461_2.getName() .toLowerCase();
ClassName className = 
 tom.gom.adt.objects.types.classname.ClassName.make(tom.gom.compiler.Compiler.packagePrefix( tomMatch461_2.getModuleDecl() )+".types."+sortNamePackage, tName) ;
writer.write(tom.gom.backend.TemplateClass.fullClassName(className));
return;


}
}
}
}

}

}

GomMessage.error(logger, null, 0, 
GomMessage.expectingVariadicButGot, 
(tom_opdecl));
return;


}

}

}
{
if ( (code instanceof tom.gom.adt.code.types.Code) ) {
boolean tomMatch460_11= false ;
 String  tomMatch460_8= "" ;
 tom.gom.adt.gom.types.OperatorDecl  tomMatch460_9= null ;
if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.IsEmpty) ) {
{
tomMatch460_11= true ;
tomMatch460_8= (( tom.gom.adt.code.types.Code )code).getVar() ;
tomMatch460_9= (( tom.gom.adt.code.types.Code )code).getOperator() ;

}
} else {
if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.IsCons) ) {
{
tomMatch460_11= true ;
tomMatch460_8= (( tom.gom.adt.code.types.Code )code).getVar() ;
tomMatch460_9= (( tom.gom.adt.code.types.Code )code).getOperator() ;

}
}
}
if (tomMatch460_11) {
 tom.gom.adt.gom.types.OperatorDecl  tom_opdecl=tomMatch460_9;
{
{
if ( (tom_opdecl instanceof tom.gom.adt.gom.types.OperatorDecl) ) {
if ( ((( tom.gom.adt.gom.types.OperatorDecl )tom_opdecl) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {
if ( ( (( tom.gom.adt.gom.types.OperatorDecl )tom_opdecl).getProd()  instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) {

writer.write(
tomMatch460_8);

{
{
if ( (code instanceof tom.gom.adt.code.types.Code) ) {
if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.IsEmpty) ) {

writer.write(".isEmpty");

}
}

}
{
if ( (code instanceof tom.gom.adt.code.types.Code) ) {
if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.IsCons) ) {

writer.write(".isCons");

}
}

}


}

writer.write(
 (( tom.gom.adt.gom.types.OperatorDecl )tom_opdecl).getName() );
writer.write("()");
return;


}
}
}

}

}

GomMessage.error(logger, null, 0, 
GomMessage.expectingVariadicButGot, 
(tom_opdecl));
return;


}

}

}
{
if ( (code instanceof tom.gom.adt.code.types.Code) ) {
if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.FullOperatorClass) ) {
 tom.gom.adt.gom.types.OperatorDecl  tomMatch460_13= (( tom.gom.adt.code.types.Code )code).getOperator() ;
if ( (tomMatch460_13 instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {
 tom.gom.adt.gom.types.SortDecl  tomMatch460_16= tomMatch460_13.getSort() ;
if ( (tomMatch460_16 instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {

String sortNamePackage = 
 tomMatch460_16.getName() .toLowerCase();
ClassName className = 
 tom.gom.adt.objects.types.classname.ClassName.make(tom.gom.compiler.Compiler.packagePrefix( tomMatch460_16.getModuleDecl() )+".types."+sortNamePackage,  tomMatch460_13.getName() ) ;
writer.write(tom.gom.backend.TemplateClass.fullClassName(className));
return;


}
}
}
}

}
{
if ( (code instanceof tom.gom.adt.code.types.Code) ) {
if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.FullSortClass) ) {
 tom.gom.adt.gom.types.SortDecl  tomMatch460_22= (( tom.gom.adt.code.types.Code )code).getSort() ;
if ( (tomMatch460_22 instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {

ClassName sortClassName = 
 tom.gom.adt.objects.types.classname.ClassName.make(tom.gom.compiler.Compiler.packagePrefix( tomMatch460_22.getModuleDecl() )+".types",  tomMatch460_22.getName() ) ;
writer.write(tom.gom.backend.TemplateClass.fullClassName(sortClassName));
return;


}
}
}

}
{
if ( (code instanceof tom.gom.adt.code.types.Code) ) {
if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.ShortSortClass) ) {
 tom.gom.adt.gom.types.SortDecl  tomMatch460_28= (( tom.gom.adt.code.types.Code )code).getSort() ;
if ( (tomMatch460_28 instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {

writer.write(
 tomMatch460_28.getName() );
return;


}
}
}

}
{
if ( (code instanceof tom.gom.adt.code.types.Code) ) {
boolean tomMatch460_37= false ;
 tom.gom.adt.gom.types.SortDecl  tomMatch460_33= null ;
if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.FullSortClass) ) {
{
tomMatch460_37= true ;
tomMatch460_33= (( tom.gom.adt.code.types.Code )code).getSort() ;

}
} else {
if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.ShortSortClass) ) {
{
tomMatch460_37= true ;
tomMatch460_33= (( tom.gom.adt.code.types.Code )code).getSort() ;

}
}
}
if (tomMatch460_37) {
if ( (tomMatch460_33 instanceof tom.gom.adt.gom.types.sortdecl.BuiltinSortDecl) ) {

writer.write(
 tomMatch460_33.getName() );
return;


}
}

}

}
{
if ( (code instanceof tom.gom.adt.code.types.Code) ) {
if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.Compare) ) {

generateCode(
 (( tom.gom.adt.code.types.Code )code).getLCode() , writer);
writer.write(".compareTo(");
generateCode(
 (( tom.gom.adt.code.types.Code )code).getRCode() , writer);
writer.write(")");
return;


}
}

}
{
if ( (code instanceof tom.gom.adt.code.types.Code) ) {
if ( (((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.ConsCodeList) || ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.EmptyCodeList)) ) {
if ( (  (( tom.gom.adt.code.types.Code )code).isEmptyCodeList()  ||  ((( tom.gom.adt.code.types.Code )code)== tom.gom.adt.code.types.code.EmptyCodeList.make() )  ) ) {
return ; 
}
}
}

}
{
if ( (code instanceof tom.gom.adt.code.types.Code) ) {
if ( (((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.ConsCodeList) || ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.EmptyCodeList)) ) {
if (!( (  (( tom.gom.adt.code.types.Code )code).isEmptyCodeList()  ||  ((( tom.gom.adt.code.types.Code )code)== tom.gom.adt.code.types.code.EmptyCodeList.make() )  ) )) {

generateCode(
(( (((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.ConsCodeList) || ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )?( (( tom.gom.adt.code.types.Code )code).getHeadCodeList() ):((( tom.gom.adt.code.types.Code )code))),writer);
generateCode(
(( (((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.ConsCodeList) || ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )?( (( tom.gom.adt.code.types.Code )code).getTailCodeList() ):( tom.gom.adt.code.types.code.EmptyCodeList.make() )),writer);
return;


}
}
}

}


}

throw new GomRuntimeException("Can't generate code for " + code);
}
}
