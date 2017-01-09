/*
 * Gom
 *
 * Copyright (c) 2006-2017, Universite de Lorraine, Inria
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

         private static   tom.gom.adt.code.types.Code  tom_append_list_CodeList( tom.gom.adt.code.types.Code  l1,  tom.gom.adt.code.types.Code  l2) {     if( l1.isEmptyCodeList() ) {       return l2;     } else if( l2.isEmptyCodeList() ) {       return l1;     } else if( ((l1 instanceof tom.gom.adt.code.types.code.ConsCodeList) || (l1 instanceof tom.gom.adt.code.types.code.EmptyCodeList)) ) {       if(  l1.getTailCodeList() .isEmptyCodeList() ) {         return  tom.gom.adt.code.types.code.ConsCodeList.make( l1.getHeadCodeList() ,l2) ;       } else {         return  tom.gom.adt.code.types.code.ConsCodeList.make( l1.getHeadCodeList() ,tom_append_list_CodeList( l1.getTailCodeList() ,l2)) ;       }     } else {       return  tom.gom.adt.code.types.code.ConsCodeList.make(l1,l2) ;     }   }   private static   tom.gom.adt.code.types.Code  tom_get_slice_CodeList( tom.gom.adt.code.types.Code  begin,  tom.gom.adt.code.types.Code  end, tom.gom.adt.code.types.Code  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyCodeList()  ||  (end== tom.gom.adt.code.types.code.EmptyCodeList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.code.types.code.ConsCodeList.make((( ((begin instanceof tom.gom.adt.code.types.code.ConsCodeList) || (begin instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )? begin.getHeadCodeList() :begin),( tom.gom.adt.code.types.Code )tom_get_slice_CodeList((( ((begin instanceof tom.gom.adt.code.types.code.ConsCodeList) || (begin instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )? begin.getTailCodeList() : tom.gom.adt.code.types.code.EmptyCodeList.make() ),end,tail)) ;   }    

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
    { /* unamed block */{ /* unamed block */if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.Code) ) {

        writer.write( (( tom.gom.adt.code.types.Code )code).getprog() );
        return;
      }}}{ /* unamed block */if ( (code instanceof tom.gom.adt.code.types.Code) ) {boolean tomMatch548_9= false ; tom.gom.adt.gom.types.OperatorDecl  tomMatch548_5= null ; tom.gom.adt.code.types.Code  tomMatch548_7= null ; tom.gom.adt.code.types.Code  tomMatch548_8= null ;if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.Empty) ) {{ /* unamed block */tomMatch548_9= true ;tomMatch548_7=(( tom.gom.adt.code.types.Code )code);tomMatch548_5= tomMatch548_7.getOperator() ;}} else {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.Cons) ) {{ /* unamed block */tomMatch548_9= true ;tomMatch548_8=(( tom.gom.adt.code.types.Code )code);tomMatch548_5= tomMatch548_8.getOperator() ;}}}if (tomMatch548_9) { tom.gom.adt.gom.types.OperatorDecl  tom___opdecl=tomMatch548_5;{ /* unamed block */{ /* unamed block */if ( (tom___opdecl instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )tom___opdecl) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.SortDecl  tomMatch549_2= (( tom.gom.adt.gom.types.OperatorDecl )tom___opdecl).getSort() ; String  tom___opName= (( tom.gom.adt.gom.types.OperatorDecl )tom___opdecl).getName() ;if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch549_2) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {if ( ((( tom.gom.adt.gom.types.TypedProduction ) (( tom.gom.adt.gom.types.OperatorDecl )tom___opdecl).getProd() ) instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) {





            String tName = tom___opName;
            { /* unamed block */{ /* unamed block */if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.Empty) ) {

                tName = "Empty" + tom___opName;
              }}}{ /* unamed block */if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.Cons) ) {

                tName = "Cons" + tom___opName;
              }}}}

            String sortNamePackage =  tomMatch549_2.getName() .toLowerCase();
            ClassName className =  tom.gom.adt.objects.types.classname.ClassName.make(tom.gom.compiler.Compiler.packagePrefix( tomMatch549_2.getModuleDecl() )+".types."+sortNamePackage, tName) 

;
            writer.write(tom.gom.backend.TemplateClass.fullClassName(className));
            return;
          }}}}}}

        GomMessage.error(logger, null, 0, 
            GomMessage.expectingVariadicButGot, (tom___opdecl));
        return;
      }}}{ /* unamed block */if ( (code instanceof tom.gom.adt.code.types.Code) ) {boolean tomMatch548_16= false ; tom.gom.adt.code.types.Code  tomMatch548_15= null ; tom.gom.adt.gom.types.OperatorDecl  tomMatch548_12= null ; tom.gom.adt.code.types.Code  tomMatch548_14= null ; String  tomMatch548_11= "" ;if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.IsEmpty) ) {{ /* unamed block */tomMatch548_16= true ;tomMatch548_14=(( tom.gom.adt.code.types.Code )code);tomMatch548_11= tomMatch548_14.getVar() ;tomMatch548_12= tomMatch548_14.getOperator() ;}} else {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.IsCons) ) {{ /* unamed block */tomMatch548_16= true ;tomMatch548_15=(( tom.gom.adt.code.types.Code )code);tomMatch548_11= tomMatch548_15.getVar() ;tomMatch548_12= tomMatch548_15.getOperator() ;}}}if (tomMatch548_16) { tom.gom.adt.gom.types.OperatorDecl  tom___opdecl=tomMatch548_12;{ /* unamed block */{ /* unamed block */if ( (tom___opdecl instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )tom___opdecl) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.TypedProduction ) (( tom.gom.adt.gom.types.OperatorDecl )tom___opdecl).getProd() ) instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) {



            writer.write(tomMatch548_11);
            { /* unamed block */{ /* unamed block */if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.IsEmpty) ) {

                writer.write(".isEmpty");
              }}}{ /* unamed block */if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.IsCons) ) {

                writer.write(".isCons");
              }}}}

            writer.write( (( tom.gom.adt.gom.types.OperatorDecl )tom___opdecl).getName() );
            writer.write("()");
            return;
          }}}}}

        GomMessage.error(logger, null, 0, 
            GomMessage.expectingVariadicButGot, (tom___opdecl));
        return;
      }}}{ /* unamed block */if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.FullOperatorClass) ) { tom.gom.adt.gom.types.OperatorDecl  tomMatch548_18= (( tom.gom.adt.code.types.Code )code).getOperator() ;if ( ((( tom.gom.adt.gom.types.OperatorDecl )tomMatch548_18) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.SortDecl  tomMatch548_22= tomMatch548_18.getSort() ;if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch548_22) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {






        String sortNamePackage =  tomMatch548_22.getName() .toLowerCase();
        ClassName className =  tom.gom.adt.objects.types.classname.ClassName.make(tom.gom.compiler.Compiler.packagePrefix( tomMatch548_22.getModuleDecl() )+".types."+sortNamePackage,  tomMatch548_18.getName() ) 

;
        writer.write(tom.gom.backend.TemplateClass.fullClassName(className));
        return;
      }}}}}{ /* unamed block */if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.FullSortClass) ) { tom.gom.adt.gom.types.SortDecl  tomMatch548_30= (( tom.gom.adt.code.types.Code )code).getSort() ;if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch548_30) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {

        ClassName sortClassName =  tom.gom.adt.objects.types.classname.ClassName.make(tom.gom.compiler.Compiler.packagePrefix( tomMatch548_30.getModuleDecl() )+".types",  tomMatch548_30.getName() ) 
;
        writer.write(tom.gom.backend.TemplateClass.fullClassName(sortClassName));
        return;
      }}}}{ /* unamed block */if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.ShortSortClass) ) { tom.gom.adt.gom.types.SortDecl  tomMatch548_38= (( tom.gom.adt.code.types.Code )code).getSort() ;if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch548_38) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {

        writer.write( tomMatch548_38.getName() );
        return;
      }}}}{ /* unamed block */if ( (code instanceof tom.gom.adt.code.types.Code) ) {boolean tomMatch548_52= false ; tom.gom.adt.code.types.Code  tomMatch548_47= null ; tom.gom.adt.code.types.Code  tomMatch548_48= null ; tom.gom.adt.gom.types.SortDecl  tomMatch548_45= null ;if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.FullSortClass) ) {{ /* unamed block */tomMatch548_52= true ;tomMatch548_47=(( tom.gom.adt.code.types.Code )code);tomMatch548_45= tomMatch548_47.getSort() ;}} else {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.ShortSortClass) ) {{ /* unamed block */tomMatch548_52= true ;tomMatch548_48=(( tom.gom.adt.code.types.Code )code);tomMatch548_45= tomMatch548_48.getSort() ;}}}if (tomMatch548_52) {if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch548_45) instanceof tom.gom.adt.gom.types.sortdecl.BuiltinSortDecl) ) {

        writer.write( tomMatch548_45.getName() );
        return;
      }}}}{ /* unamed block */if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.Compare) ) {

        generateCode( (( tom.gom.adt.code.types.Code )code).getLCode() , writer);
        writer.write(".compareTo(");
        generateCode( (( tom.gom.adt.code.types.Code )code).getRCode() , writer);
        writer.write(")");
        return;
      }}}{ /* unamed block */if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( (((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.ConsCodeList) || ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.EmptyCodeList)) ) {if ( (  (( tom.gom.adt.code.types.Code )code).isEmptyCodeList()  ||  ((( tom.gom.adt.code.types.Code )code)== tom.gom.adt.code.types.code.EmptyCodeList.make() )  ) ) {
 return ; }}}}{ /* unamed block */if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( (((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.ConsCodeList) || ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.EmptyCodeList)) ) {if (!( (  (( tom.gom.adt.code.types.Code )code).isEmptyCodeList()  ||  ((( tom.gom.adt.code.types.Code )code)== tom.gom.adt.code.types.code.EmptyCodeList.make() )  ) )) {

        generateCode((( (((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.ConsCodeList) || ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )?( (( tom.gom.adt.code.types.Code )code).getHeadCodeList() ):((( tom.gom.adt.code.types.Code )code))),writer);
        generateCode((( (((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.ConsCodeList) || ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )?( (( tom.gom.adt.code.types.Code )code).getTailCodeList() ):( tom.gom.adt.code.types.code.EmptyCodeList.make() )),writer);
        return;
      }}}}}

    throw new GomRuntimeException("Can't generate code for " + code);
  }
}
