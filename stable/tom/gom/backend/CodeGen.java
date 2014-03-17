/*
 * Gom
 *
 * Copyright (c) 2006-2014, Universite de Lorraine, Inria
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
    {{if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )code)) instanceof tom.gom.adt.code.types.code.Code) ) {

        writer.write( (( tom.gom.adt.code.types.Code )code).getprog() );
        return;
      }}}}{if ( (code instanceof tom.gom.adt.code.types.Code) ) {boolean tomMatch527_9= false ; tom.gom.adt.gom.types.OperatorDecl  tomMatch527_5= null ; tom.gom.adt.code.types.Code  tomMatch527_8= null ; tom.gom.adt.code.types.Code  tomMatch527_7= null ;if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )code)) instanceof tom.gom.adt.code.types.code.Empty) ) {{tomMatch527_9= true ;tomMatch527_7=(( tom.gom.adt.code.types.Code )code);tomMatch527_5= tomMatch527_7.getOperator() ;}} else {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )code)) instanceof tom.gom.adt.code.types.code.Cons) ) {{tomMatch527_9= true ;tomMatch527_8=(( tom.gom.adt.code.types.Code )code);tomMatch527_5= tomMatch527_8.getOperator() ;}}}}}if (tomMatch527_9) { tom.gom.adt.gom.types.OperatorDecl  tom_opdecl=tomMatch527_5;{{if ( (tom_opdecl instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )tom_opdecl) instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )(( tom.gom.adt.gom.types.OperatorDecl )tom_opdecl)) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.SortDecl  tomMatch528_2= (( tom.gom.adt.gom.types.OperatorDecl )tom_opdecl).getSort() ; tom.gom.adt.gom.types.TypedProduction  tomMatch528_3= (( tom.gom.adt.gom.types.OperatorDecl )tom_opdecl).getProd() ; String  tom_opName= (( tom.gom.adt.gom.types.OperatorDecl )tom_opdecl).getName() ;if ( (tomMatch528_2 instanceof tom.gom.adt.gom.types.SortDecl) ) {if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch528_2) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {if ( (tomMatch528_3 instanceof tom.gom.adt.gom.types.TypedProduction) ) {if ( ((( tom.gom.adt.gom.types.TypedProduction )tomMatch528_3) instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) {





            String tName = tom_opName;
            {{if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )code)) instanceof tom.gom.adt.code.types.code.Empty) ) {

                tName = "Empty" + tom_opName;
              }}}}{if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )code)) instanceof tom.gom.adt.code.types.code.Cons) ) {

                tName = "Cons" + tom_opName;
              }}}}}

            String sortNamePackage =  tomMatch528_2.getName() .toLowerCase();
            ClassName className =  tom.gom.adt.objects.types.classname.ClassName.make(tom.gom.compiler.Compiler.packagePrefix( tomMatch528_2.getModuleDecl() )+".types."+sortNamePackage, tName) 

;
            writer.write(tom.gom.backend.TemplateClass.fullClassName(className));
            return;
          }}}}}}}}}

        GomMessage.error(logger, null, 0, 
            GomMessage.expectingVariadicButGot, (tom_opdecl));
        return;
      }}}{if ( (code instanceof tom.gom.adt.code.types.Code) ) {boolean tomMatch527_16= false ; tom.gom.adt.code.types.Code  tomMatch527_15= null ; tom.gom.adt.code.types.Code  tomMatch527_14= null ; String  tomMatch527_11= "" ; tom.gom.adt.gom.types.OperatorDecl  tomMatch527_12= null ;if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )code)) instanceof tom.gom.adt.code.types.code.IsEmpty) ) {{tomMatch527_16= true ;tomMatch527_14=(( tom.gom.adt.code.types.Code )code);tomMatch527_11= tomMatch527_14.getVar() ;tomMatch527_12= tomMatch527_14.getOperator() ;}} else {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )code)) instanceof tom.gom.adt.code.types.code.IsCons) ) {{tomMatch527_16= true ;tomMatch527_15=(( tom.gom.adt.code.types.Code )code);tomMatch527_11= tomMatch527_15.getVar() ;tomMatch527_12= tomMatch527_15.getOperator() ;}}}}}if (tomMatch527_16) { tom.gom.adt.gom.types.OperatorDecl  tom_opdecl=tomMatch527_12;{{if ( (tom_opdecl instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )tom_opdecl) instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )(( tom.gom.adt.gom.types.OperatorDecl )tom_opdecl)) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.TypedProduction  tomMatch530_2= (( tom.gom.adt.gom.types.OperatorDecl )tom_opdecl).getProd() ;if ( (tomMatch530_2 instanceof tom.gom.adt.gom.types.TypedProduction) ) {if ( ((( tom.gom.adt.gom.types.TypedProduction )tomMatch530_2) instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) {



            writer.write(tomMatch527_11);
            {{if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )code)) instanceof tom.gom.adt.code.types.code.IsEmpty) ) {

                writer.write(".isEmpty");
              }}}}{if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )code)) instanceof tom.gom.adt.code.types.code.IsCons) ) {

                writer.write(".isCons");
              }}}}}

            writer.write( (( tom.gom.adt.gom.types.OperatorDecl )tom_opdecl).getName() );
            writer.write("()");
            return;
          }}}}}}}

        GomMessage.error(logger, null, 0, 
            GomMessage.expectingVariadicButGot, (tom_opdecl));
        return;
      }}}{if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )code)) instanceof tom.gom.adt.code.types.code.FullOperatorClass) ) { tom.gom.adt.gom.types.OperatorDecl  tomMatch527_18= (( tom.gom.adt.code.types.Code )code).getOperator() ;if ( (tomMatch527_18 instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )tomMatch527_18) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.SortDecl  tomMatch527_22= tomMatch527_18.getSort() ;if ( (tomMatch527_22 instanceof tom.gom.adt.gom.types.SortDecl) ) {if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch527_22) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {






        String sortNamePackage =  tomMatch527_22.getName() .toLowerCase();
        ClassName className =  tom.gom.adt.objects.types.classname.ClassName.make(tom.gom.compiler.Compiler.packagePrefix( tomMatch527_22.getModuleDecl() )+".types."+sortNamePackage,  tomMatch527_18.getName() ) 

;
        writer.write(tom.gom.backend.TemplateClass.fullClassName(className));
        return;
      }}}}}}}}{if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )code)) instanceof tom.gom.adt.code.types.code.FullSortClass) ) { tom.gom.adt.gom.types.SortDecl  tomMatch527_30= (( tom.gom.adt.code.types.Code )code).getSort() ;if ( (tomMatch527_30 instanceof tom.gom.adt.gom.types.SortDecl) ) {if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch527_30) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {

        ClassName sortClassName =  tom.gom.adt.objects.types.classname.ClassName.make(tom.gom.compiler.Compiler.packagePrefix( tomMatch527_30.getModuleDecl() )+".types",  tomMatch527_30.getName() ) 
;
        writer.write(tom.gom.backend.TemplateClass.fullClassName(sortClassName));
        return;
      }}}}}}{if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )code)) instanceof tom.gom.adt.code.types.code.ShortSortClass) ) { tom.gom.adt.gom.types.SortDecl  tomMatch527_38= (( tom.gom.adt.code.types.Code )code).getSort() ;if ( (tomMatch527_38 instanceof tom.gom.adt.gom.types.SortDecl) ) {if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch527_38) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {

        writer.write( tomMatch527_38.getName() );
        return;
      }}}}}}{if ( (code instanceof tom.gom.adt.code.types.Code) ) {boolean tomMatch527_52= false ; tom.gom.adt.code.types.Code  tomMatch527_48= null ; tom.gom.adt.code.types.Code  tomMatch527_47= null ; tom.gom.adt.gom.types.SortDecl  tomMatch527_45= null ;if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )code)) instanceof tom.gom.adt.code.types.code.FullSortClass) ) {{tomMatch527_52= true ;tomMatch527_47=(( tom.gom.adt.code.types.Code )code);tomMatch527_45= tomMatch527_47.getSort() ;}} else {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )code)) instanceof tom.gom.adt.code.types.code.ShortSortClass) ) {{tomMatch527_52= true ;tomMatch527_48=(( tom.gom.adt.code.types.Code )code);tomMatch527_45= tomMatch527_48.getSort() ;}}}}}if (tomMatch527_52) {if ( (tomMatch527_45 instanceof tom.gom.adt.gom.types.SortDecl) ) {if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch527_45) instanceof tom.gom.adt.gom.types.sortdecl.BuiltinSortDecl) ) {

        writer.write( tomMatch527_45.getName() );
        return;
      }}}}}{if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )code)) instanceof tom.gom.adt.code.types.code.Compare) ) {

        generateCode( (( tom.gom.adt.code.types.Code )code).getLCode() , writer);
        writer.write(".compareTo(");
        generateCode( (( tom.gom.adt.code.types.Code )code).getRCode() , writer);
        writer.write(")");
        return;
      }}}}{if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( (((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )code)) instanceof tom.gom.adt.code.types.code.ConsCodeList) || ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )code)) instanceof tom.gom.adt.code.types.code.EmptyCodeList)) ) {if ( (  (( tom.gom.adt.code.types.Code )code).isEmptyCodeList()  ||  ((( tom.gom.adt.code.types.Code )code)== tom.gom.adt.code.types.code.EmptyCodeList.make() )  ) ) {
 return ; }}}}{if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( (((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )code)) instanceof tom.gom.adt.code.types.code.ConsCodeList) || ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )code)) instanceof tom.gom.adt.code.types.code.EmptyCodeList)) ) {if (!( (  (( tom.gom.adt.code.types.Code )code).isEmptyCodeList()  ||  ((( tom.gom.adt.code.types.Code )code)== tom.gom.adt.code.types.code.EmptyCodeList.make() )  ) )) {

        generateCode((( (((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.ConsCodeList) || ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )?( (( tom.gom.adt.code.types.Code )code).getHeadCodeList() ):((( tom.gom.adt.code.types.Code )code))),writer);
        generateCode((( (((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.ConsCodeList) || ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )?( (( tom.gom.adt.code.types.Code )code).getTailCodeList() ):( tom.gom.adt.code.types.code.EmptyCodeList.make() )),writer);
        return;
      }}}}}

    throw new GomRuntimeException("Can't generate code for " + code);
  }
}
