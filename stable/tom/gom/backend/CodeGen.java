/* Generated by TOM (version 2.6alpha): Do not edit this file *//*
 * Gom
 *
 * Copyright (C) 2006-2007, INRIA
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
import java.util.logging.Level;
import tom.gom.tools.error.GomRuntimeException;
import tom.gom.adt.code.types.*;
import tom.gom.adt.objects.types.*;

public class CodeGen {

  /* Generated by TOM (version 2.6alpha): Do not edit this file *//* Generated by TOM (version 2.6alpha): Do not edit this file *//* Generated by TOM (version 2.6alpha): Do not edit this file */  /* Generated by TOM (version 2.6alpha): Do not edit this file */    private static   tom.gom.adt.code.types.Code  tom_append_list_CodeList( tom.gom.adt.code.types.Code  l1,  tom.gom.adt.code.types.Code  l2) {     if( l1.isEmptyCodeList() ) {       return l2;     } else if( l2.isEmptyCodeList() ) {       return l1;     } else if( ((l1 instanceof tom.gom.adt.code.types.code.ConsCodeList) || (l1 instanceof tom.gom.adt.code.types.code.EmptyCodeList)) ) {       if( (( ((l1 instanceof tom.gom.adt.code.types.code.ConsCodeList) || (l1 instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )? l1.getTailCodeList() : tom.gom.adt.code.types.code.EmptyCodeList.make() ).isEmptyCodeList() ) {         return  tom.gom.adt.code.types.code.ConsCodeList.make((( ((l1 instanceof tom.gom.adt.code.types.code.ConsCodeList) || (l1 instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )? l1.getHeadCodeList() :l1),l2) ;       } else {         return  tom.gom.adt.code.types.code.ConsCodeList.make((( ((l1 instanceof tom.gom.adt.code.types.code.ConsCodeList) || (l1 instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )? l1.getHeadCodeList() :l1),tom_append_list_CodeList((( ((l1 instanceof tom.gom.adt.code.types.code.ConsCodeList) || (l1 instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )? l1.getTailCodeList() : tom.gom.adt.code.types.code.EmptyCodeList.make() ),l2)) ;       }     } else {       return  tom.gom.adt.code.types.code.ConsCodeList.make(l1,l2) ;     }   }   private static   tom.gom.adt.code.types.Code  tom_get_slice_CodeList( tom.gom.adt.code.types.Code  begin,  tom.gom.adt.code.types.Code  end, tom.gom.adt.code.types.Code  tail) {     if( begin.equals(end) ) {       return tail;     } else {       return  tom.gom.adt.code.types.code.ConsCodeList.make((( ((begin instanceof tom.gom.adt.code.types.code.ConsCodeList) || (begin instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )? begin.getHeadCodeList() :begin),( tom.gom.adt.code.types.Code )tom_get_slice_CodeList((( ((begin instanceof tom.gom.adt.code.types.code.ConsCodeList) || (begin instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )? begin.getTailCodeList() : tom.gom.adt.code.types.code.EmptyCodeList.make() ),end,tail)) ;     }   }    

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
      Logger.getLogger("CodeGen").log(
          Level.SEVERE,"Failed to generate code for " + code);
      
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
    {if ( (code instanceof tom.gom.adt.code.types.Code) ) {{  tom.gom.adt.code.types.Code  tomMatch344NameNumberfreshSubject_1=(( tom.gom.adt.code.types.Code )code);if ( (tomMatch344NameNumberfreshSubject_1 instanceof tom.gom.adt.code.types.code.Code) ) {{  String  tomMatch344NameNumber_freshVar_0= tomMatch344NameNumberfreshSubject_1.getprog() ;if ( true ) {

        writer.write(tomMatch344NameNumber_freshVar_0);
        return;
      }}}}}if ( (code instanceof tom.gom.adt.code.types.Code) ) {{  tom.gom.adt.code.types.Code  tomMatch344NameNumberfreshSubject_1=(( tom.gom.adt.code.types.Code )code);{ boolean tomMatch344NameNumber_freshVar_2= false ;{  tom.gom.adt.gom.types.OperatorDecl  tomMatch344NameNumber_freshVar_1= null ;if ( (tomMatch344NameNumberfreshSubject_1 instanceof tom.gom.adt.code.types.code.Empty) ) {{tomMatch344NameNumber_freshVar_2= true ;tomMatch344NameNumber_freshVar_1= tomMatch344NameNumberfreshSubject_1.getOperator() ;}} else {if ( (tomMatch344NameNumberfreshSubject_1 instanceof tom.gom.adt.code.types.code.Cons) ) {{tomMatch344NameNumber_freshVar_2= true ;tomMatch344NameNumber_freshVar_1= tomMatch344NameNumberfreshSubject_1.getOperator() ;}}}if ((tomMatch344NameNumber_freshVar_2 ==  true )) {{  tom.gom.adt.gom.types.OperatorDecl  tom_opdecl=tomMatch344NameNumber_freshVar_1;if ( true ) {{if ( (tom_opdecl instanceof tom.gom.adt.gom.types.OperatorDecl) ) {{  tom.gom.adt.gom.types.OperatorDecl  tomMatch345NameNumberfreshSubject_1=(( tom.gom.adt.gom.types.OperatorDecl )tom_opdecl);if ( (tomMatch345NameNumberfreshSubject_1 instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {{  String  tomMatch345NameNumber_freshVar_0= tomMatch345NameNumberfreshSubject_1.getName() ;{  tom.gom.adt.gom.types.SortDecl  tomMatch345NameNumber_freshVar_1= tomMatch345NameNumberfreshSubject_1.getSort() ;{  tom.gom.adt.gom.types.TypedProduction  tomMatch345NameNumber_freshVar_2= tomMatch345NameNumberfreshSubject_1.getProd() ;{  String  tom_opName=tomMatch345NameNumber_freshVar_0;if ( (tomMatch345NameNumber_freshVar_1 instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {{  String  tomMatch345NameNumber_freshVar_3= tomMatch345NameNumber_freshVar_1.getName() ;{  tom.gom.adt.gom.types.ModuleDecl  tomMatch345NameNumber_freshVar_4= tomMatch345NameNumber_freshVar_1.getModuleDecl() ;if ( (tomMatch345NameNumber_freshVar_2 instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) {if ( true ) {





            String tName = tom_opName;
            {if ( (code instanceof tom.gom.adt.code.types.Code) ) {{  tom.gom.adt.code.types.Code  tomMatch346NameNumberfreshSubject_1=(( tom.gom.adt.code.types.Code )code);if ( (tomMatch346NameNumberfreshSubject_1 instanceof tom.gom.adt.code.types.code.Empty) ) {if ( true ) {

                tName = "Empty" + tom_opName;
              }}}}if ( (code instanceof tom.gom.adt.code.types.Code) ) {{  tom.gom.adt.code.types.Code  tomMatch346NameNumberfreshSubject_1=(( tom.gom.adt.code.types.Code )code);if ( (tomMatch346NameNumberfreshSubject_1 instanceof tom.gom.adt.code.types.code.Cons) ) {if ( true ) {

                tName = "Cons" + tom_opName;
              }}}}}

            String sortNamePackage = tomMatch345NameNumber_freshVar_3.toLowerCase();
            ClassName className =  tom.gom.adt.objects.types.classname.ClassName.make(tom.gom.compiler.Compiler.packagePrefix(tomMatch345NameNumber_freshVar_4)+".types."+sortNamePackage, tName) 

;
            writer.write(tom.gom.backend.TemplateClass.fullClassName(className));        
            return;
          }}}}}}}}}}}}}

        Logger.getLogger("CodeGen").log(
            Level.SEVERE,"{Empty,Cons}: expecting varidic, but got {0}",
            new Object[] { (tom_opdecl) });
        return;
      }}}}}}}if ( (code instanceof tom.gom.adt.code.types.Code) ) {{  tom.gom.adt.code.types.Code  tomMatch344NameNumberfreshSubject_1=(( tom.gom.adt.code.types.Code )code);{ boolean tomMatch344NameNumber_freshVar_5= false ;{  tom.gom.adt.gom.types.OperatorDecl  tomMatch344NameNumber_freshVar_4= null ;{  String  tomMatch344NameNumber_freshVar_3= "" ;if ( (tomMatch344NameNumberfreshSubject_1 instanceof tom.gom.adt.code.types.code.IsEmpty) ) {{tomMatch344NameNumber_freshVar_5= true ;tomMatch344NameNumber_freshVar_3= tomMatch344NameNumberfreshSubject_1.getVar() ;tomMatch344NameNumber_freshVar_4= tomMatch344NameNumberfreshSubject_1.getOperator() ;}} else {if ( (tomMatch344NameNumberfreshSubject_1 instanceof tom.gom.adt.code.types.code.IsCons) ) {{tomMatch344NameNumber_freshVar_5= true ;tomMatch344NameNumber_freshVar_3= tomMatch344NameNumberfreshSubject_1.getVar() ;tomMatch344NameNumber_freshVar_4= tomMatch344NameNumberfreshSubject_1.getOperator() ;}}}if ((tomMatch344NameNumber_freshVar_5 ==  true )) {{  tom.gom.adt.gom.types.OperatorDecl  tom_opdecl=tomMatch344NameNumber_freshVar_4;if ( true ) {{if ( (tom_opdecl instanceof tom.gom.adt.gom.types.OperatorDecl) ) {{  tom.gom.adt.gom.types.OperatorDecl  tomMatch347NameNumberfreshSubject_1=(( tom.gom.adt.gom.types.OperatorDecl )tom_opdecl);if ( (tomMatch347NameNumberfreshSubject_1 instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {{  String  tomMatch347NameNumber_freshVar_0= tomMatch347NameNumberfreshSubject_1.getName() ;{  tom.gom.adt.gom.types.TypedProduction  tomMatch347NameNumber_freshVar_1= tomMatch347NameNumberfreshSubject_1.getProd() ;if ( (tomMatch347NameNumber_freshVar_1 instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) {if ( true ) {



            writer.write(tomMatch344NameNumber_freshVar_3);
            {if ( (code instanceof tom.gom.adt.code.types.Code) ) {{  tom.gom.adt.code.types.Code  tomMatch348NameNumberfreshSubject_1=(( tom.gom.adt.code.types.Code )code);if ( (tomMatch348NameNumberfreshSubject_1 instanceof tom.gom.adt.code.types.code.IsEmpty) ) {if ( true ) {

                writer.write(".isEmpty");
              }}}}if ( (code instanceof tom.gom.adt.code.types.Code) ) {{  tom.gom.adt.code.types.Code  tomMatch348NameNumberfreshSubject_1=(( tom.gom.adt.code.types.Code )code);if ( (tomMatch348NameNumberfreshSubject_1 instanceof tom.gom.adt.code.types.code.IsCons) ) {if ( true ) {

                writer.write(".isCons");
              }}}}}

            writer.write(tomMatch347NameNumber_freshVar_0);
            writer.write("()");
            return;
          }}}}}}}}

        Logger.getLogger("CodeGen").log(
            Level.SEVERE,"Is{Empty,Cons}: expecting varidic, but got {0}",
            new Object[] { (tom_opdecl) });
        return;
      }}}}}}}}if ( (code instanceof tom.gom.adt.code.types.Code) ) {{  tom.gom.adt.code.types.Code  tomMatch344NameNumberfreshSubject_1=(( tom.gom.adt.code.types.Code )code);if ( (tomMatch344NameNumberfreshSubject_1 instanceof tom.gom.adt.code.types.code.FullOperatorClass) ) {{  tom.gom.adt.gom.types.OperatorDecl  tomMatch344NameNumber_freshVar_6= tomMatch344NameNumberfreshSubject_1.getOperator() ;if ( (tomMatch344NameNumber_freshVar_6 instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {{  String  tomMatch344NameNumber_freshVar_7= tomMatch344NameNumber_freshVar_6.getName() ;{  tom.gom.adt.gom.types.SortDecl  tomMatch344NameNumber_freshVar_8= tomMatch344NameNumber_freshVar_6.getSort() ;if ( (tomMatch344NameNumber_freshVar_8 instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {{  String  tomMatch344NameNumber_freshVar_9= tomMatch344NameNumber_freshVar_8.getName() ;{  tom.gom.adt.gom.types.ModuleDecl  tomMatch344NameNumber_freshVar_10= tomMatch344NameNumber_freshVar_8.getModuleDecl() ;if ( true ) {






        String sortNamePackage = tomMatch344NameNumber_freshVar_9.toLowerCase();
        ClassName className =  tom.gom.adt.objects.types.classname.ClassName.make(tom.gom.compiler.Compiler.packagePrefix(tomMatch344NameNumber_freshVar_10)+".types."+sortNamePackage, tomMatch344NameNumber_freshVar_7) 

;
        writer.write(tom.gom.backend.TemplateClass.fullClassName(className));        
        return;
      }}}}}}}}}}}if ( (code instanceof tom.gom.adt.code.types.Code) ) {{  tom.gom.adt.code.types.Code  tomMatch344NameNumberfreshSubject_1=(( tom.gom.adt.code.types.Code )code);if ( (tomMatch344NameNumberfreshSubject_1 instanceof tom.gom.adt.code.types.code.FullSortClass) ) {{  tom.gom.adt.gom.types.SortDecl  tomMatch344NameNumber_freshVar_11= tomMatch344NameNumberfreshSubject_1.getSort() ;if ( (tomMatch344NameNumber_freshVar_11 instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {{  String  tomMatch344NameNumber_freshVar_12= tomMatch344NameNumber_freshVar_11.getName() ;{  tom.gom.adt.gom.types.ModuleDecl  tomMatch344NameNumber_freshVar_13= tomMatch344NameNumber_freshVar_11.getModuleDecl() ;if ( true ) {

        ClassName sortClassName =  tom.gom.adt.objects.types.classname.ClassName.make(tom.gom.compiler.Compiler.packagePrefix(tomMatch344NameNumber_freshVar_13)+".types", tomMatch344NameNumber_freshVar_12) 
;
        writer.write(tom.gom.backend.TemplateClass.fullClassName(sortClassName));        
        return;
      }}}}}}}}if ( (code instanceof tom.gom.adt.code.types.Code) ) {{  tom.gom.adt.code.types.Code  tomMatch344NameNumberfreshSubject_1=(( tom.gom.adt.code.types.Code )code);if ( (tomMatch344NameNumberfreshSubject_1 instanceof tom.gom.adt.code.types.code.FullSortClass) ) {{  tom.gom.adt.gom.types.SortDecl  tomMatch344NameNumber_freshVar_14= tomMatch344NameNumberfreshSubject_1.getSort() ;if ( (tomMatch344NameNumber_freshVar_14 instanceof tom.gom.adt.gom.types.sortdecl.BuiltinSortDecl) ) {{  String  tomMatch344NameNumber_freshVar_15= tomMatch344NameNumber_freshVar_14.getName() ;if ( true ) {

        writer.write(tomMatch344NameNumber_freshVar_15);        
        return;
      }}}}}}}if ( (code instanceof tom.gom.adt.code.types.Code) ) {{  tom.gom.adt.code.types.Code  tomMatch344NameNumberfreshSubject_1=(( tom.gom.adt.code.types.Code )code);if ( (tomMatch344NameNumberfreshSubject_1 instanceof tom.gom.adt.code.types.code.Compare) ) {{  tom.gom.adt.code.types.Code  tomMatch344NameNumber_freshVar_16= tomMatch344NameNumberfreshSubject_1.getLCode() ;{  tom.gom.adt.code.types.Code  tomMatch344NameNumber_freshVar_17= tomMatch344NameNumberfreshSubject_1.getRCode() ;if ( true ) {

        generateCode(tomMatch344NameNumber_freshVar_16, writer);
        writer.write(".compareTo(");
        generateCode(tomMatch344NameNumber_freshVar_17, writer);
        writer.write(")");
        return;
      }}}}}}if ( (code instanceof tom.gom.adt.code.types.Code) ) {{  tom.gom.adt.code.types.Code  tomMatch344NameNumberfreshSubject_1=(( tom.gom.adt.code.types.Code )code);if ( ((tomMatch344NameNumberfreshSubject_1 instanceof tom.gom.adt.code.types.code.ConsCodeList) || (tomMatch344NameNumberfreshSubject_1 instanceof tom.gom.adt.code.types.code.EmptyCodeList)) ) {{  tom.gom.adt.code.types.Code  tomMatch344NameNumber_freshVar_18=tomMatch344NameNumberfreshSubject_1;if ( (  tomMatch344NameNumber_freshVar_18.isEmptyCodeList()  ||  tomMatch344NameNumber_freshVar_18.equals( tom.gom.adt.code.types.code.EmptyCodeList.make() )  ) ) {if ( true ) {
 return ; }}}}}}if ( (code instanceof tom.gom.adt.code.types.Code) ) {{  tom.gom.adt.code.types.Code  tomMatch344NameNumberfreshSubject_1=(( tom.gom.adt.code.types.Code )code);if ( ((tomMatch344NameNumberfreshSubject_1 instanceof tom.gom.adt.code.types.code.ConsCodeList) || (tomMatch344NameNumberfreshSubject_1 instanceof tom.gom.adt.code.types.code.EmptyCodeList)) ) {{  tom.gom.adt.code.types.Code  tomMatch344NameNumber_freshVar_19=tomMatch344NameNumberfreshSubject_1;if (!( (  tomMatch344NameNumber_freshVar_19.isEmptyCodeList()  ||  tomMatch344NameNumber_freshVar_19.equals( tom.gom.adt.code.types.code.EmptyCodeList.make() )  ) )) {{  tom.gom.adt.code.types.Code  tomMatch344NameNumber_freshVar_20=(( ((tomMatch344NameNumber_freshVar_19 instanceof tom.gom.adt.code.types.code.ConsCodeList) || (tomMatch344NameNumber_freshVar_19 instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )?( tomMatch344NameNumber_freshVar_19.getTailCodeList() ):( tom.gom.adt.code.types.code.EmptyCodeList.make() ));if ( true ) {

        generateCode((( ((tomMatch344NameNumber_freshVar_19 instanceof tom.gom.adt.code.types.code.ConsCodeList) || (tomMatch344NameNumber_freshVar_19 instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )?( tomMatch344NameNumber_freshVar_19.getHeadCodeList() ):(tomMatch344NameNumber_freshVar_19)),writer);
        generateCode(tomMatch344NameNumber_freshVar_20,writer);
        return;
      }}}}}}}}

    throw new GomRuntimeException("Can't generate code for " + code);
  }
}
