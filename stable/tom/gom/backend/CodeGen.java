/*
 * Gom
 *
 * Copyright (c) 2006-2012, INPL, INRIA
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
    {{if ( (((Object)code) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )((Object)code)) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )((Object)code))) instanceof tom.gom.adt.code.types.code.Code) ) {

        writer.write( (( tom.gom.adt.code.types.Code )((Object)code)).getprog() );
        return;
      }}}}{if ( (((Object)code) instanceof tom.gom.adt.code.types.Code) ) {boolean tomMatch499_9= false ; tom.gom.adt.code.types.Code  tomMatch499_7= null ; tom.gom.adt.gom.types.OperatorDecl  tomMatch499_5= null ; tom.gom.adt.code.types.Code  tomMatch499_8= null ;if ( ((( tom.gom.adt.code.types.Code )((Object)code)) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )((Object)code))) instanceof tom.gom.adt.code.types.code.Empty) ) {{tomMatch499_9= true ;tomMatch499_7=(( tom.gom.adt.code.types.Code )((Object)code));tomMatch499_5= tomMatch499_7.getOperator() ;}} else {if ( ((( tom.gom.adt.code.types.Code )((Object)code)) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )((Object)code))) instanceof tom.gom.adt.code.types.code.Cons) ) {{tomMatch499_9= true ;tomMatch499_8=(( tom.gom.adt.code.types.Code )((Object)code));tomMatch499_5= tomMatch499_8.getOperator() ;}}}}}if (tomMatch499_9) { tom.gom.adt.gom.types.OperatorDecl  tom_opdecl=tomMatch499_5;{{if ( (((Object)tom_opdecl) instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )((Object)tom_opdecl)) instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )(( tom.gom.adt.gom.types.OperatorDecl )((Object)tom_opdecl))) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.SortDecl  tomMatch500_2= (( tom.gom.adt.gom.types.OperatorDecl )((Object)tom_opdecl)).getSort() ; tom.gom.adt.gom.types.TypedProduction  tomMatch500_3= (( tom.gom.adt.gom.types.OperatorDecl )((Object)tom_opdecl)).getProd() ; String  tom_opName= (( tom.gom.adt.gom.types.OperatorDecl )((Object)tom_opdecl)).getName() ;if ( (tomMatch500_2 instanceof tom.gom.adt.gom.types.SortDecl) ) {if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch500_2) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {if ( (tomMatch500_3 instanceof tom.gom.adt.gom.types.TypedProduction) ) {if ( ((( tom.gom.adt.gom.types.TypedProduction )tomMatch500_3) instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) {





            String tName = tom_opName;
            {{if ( (((Object)code) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )((Object)code)) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )((Object)code))) instanceof tom.gom.adt.code.types.code.Empty) ) {

                tName = "Empty" + tom_opName;
              }}}}{if ( (((Object)code) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )((Object)code)) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )((Object)code))) instanceof tom.gom.adt.code.types.code.Cons) ) {

                tName = "Cons" + tom_opName;
              }}}}}

            String sortNamePackage =  tomMatch500_2.getName() .toLowerCase();
            ClassName className =  tom.gom.adt.objects.types.classname.ClassName.make(tom.gom.compiler.Compiler.packagePrefix( tomMatch500_2.getModuleDecl() )+".types."+sortNamePackage, tName) 

;
            writer.write(tom.gom.backend.TemplateClass.fullClassName(className));
            return;
          }}}}}}}}}

        GomMessage.error(logger, null, 0, 
            GomMessage.expectingVariadicButGot, (tom_opdecl));
        return;
      }}}{if ( (((Object)code) instanceof tom.gom.adt.code.types.Code) ) {boolean tomMatch499_16= false ; tom.gom.adt.gom.types.OperatorDecl  tomMatch499_12= null ; tom.gom.adt.code.types.Code  tomMatch499_14= null ; tom.gom.adt.code.types.Code  tomMatch499_15= null ; String  tomMatch499_11= "" ;if ( ((( tom.gom.adt.code.types.Code )((Object)code)) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )((Object)code))) instanceof tom.gom.adt.code.types.code.IsEmpty) ) {{tomMatch499_16= true ;tomMatch499_14=(( tom.gom.adt.code.types.Code )((Object)code));tomMatch499_11= tomMatch499_14.getVar() ;tomMatch499_12= tomMatch499_14.getOperator() ;}} else {if ( ((( tom.gom.adt.code.types.Code )((Object)code)) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )((Object)code))) instanceof tom.gom.adt.code.types.code.IsCons) ) {{tomMatch499_16= true ;tomMatch499_15=(( tom.gom.adt.code.types.Code )((Object)code));tomMatch499_11= tomMatch499_15.getVar() ;tomMatch499_12= tomMatch499_15.getOperator() ;}}}}}if (tomMatch499_16) { tom.gom.adt.gom.types.OperatorDecl  tom_opdecl=tomMatch499_12;{{if ( (((Object)tom_opdecl) instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )((Object)tom_opdecl)) instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )(( tom.gom.adt.gom.types.OperatorDecl )((Object)tom_opdecl))) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.TypedProduction  tomMatch502_2= (( tom.gom.adt.gom.types.OperatorDecl )((Object)tom_opdecl)).getProd() ;if ( (tomMatch502_2 instanceof tom.gom.adt.gom.types.TypedProduction) ) {if ( ((( tom.gom.adt.gom.types.TypedProduction )tomMatch502_2) instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) {



            writer.write(tomMatch499_11);
            {{if ( (((Object)code) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )((Object)code)) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )((Object)code))) instanceof tom.gom.adt.code.types.code.IsEmpty) ) {

                writer.write(".isEmpty");
              }}}}{if ( (((Object)code) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )((Object)code)) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )((Object)code))) instanceof tom.gom.adt.code.types.code.IsCons) ) {

                writer.write(".isCons");
              }}}}}

            writer.write( (( tom.gom.adt.gom.types.OperatorDecl )((Object)tom_opdecl)).getName() );
            writer.write("()");
            return;
          }}}}}}}

        GomMessage.error(logger, null, 0, 
            GomMessage.expectingVariadicButGot, (tom_opdecl));
        return;
      }}}{if ( (((Object)code) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )((Object)code)) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )((Object)code))) instanceof tom.gom.adt.code.types.code.FullOperatorClass) ) { tom.gom.adt.gom.types.OperatorDecl  tomMatch499_18= (( tom.gom.adt.code.types.Code )((Object)code)).getOperator() ;if ( (tomMatch499_18 instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )tomMatch499_18) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.SortDecl  tomMatch499_22= tomMatch499_18.getSort() ;if ( (tomMatch499_22 instanceof tom.gom.adt.gom.types.SortDecl) ) {if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch499_22) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {






        String sortNamePackage =  tomMatch499_22.getName() .toLowerCase();
        ClassName className =  tom.gom.adt.objects.types.classname.ClassName.make(tom.gom.compiler.Compiler.packagePrefix( tomMatch499_22.getModuleDecl() )+".types."+sortNamePackage,  tomMatch499_18.getName() ) 

;
        writer.write(tom.gom.backend.TemplateClass.fullClassName(className));
        return;
      }}}}}}}}{if ( (((Object)code) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )((Object)code)) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )((Object)code))) instanceof tom.gom.adt.code.types.code.FullSortClass) ) { tom.gom.adt.gom.types.SortDecl  tomMatch499_30= (( tom.gom.adt.code.types.Code )((Object)code)).getSort() ;if ( (tomMatch499_30 instanceof tom.gom.adt.gom.types.SortDecl) ) {if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch499_30) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {

        ClassName sortClassName =  tom.gom.adt.objects.types.classname.ClassName.make(tom.gom.compiler.Compiler.packagePrefix( tomMatch499_30.getModuleDecl() )+".types",  tomMatch499_30.getName() ) 
;
        writer.write(tom.gom.backend.TemplateClass.fullClassName(sortClassName));
        return;
      }}}}}}{if ( (((Object)code) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )((Object)code)) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )((Object)code))) instanceof tom.gom.adt.code.types.code.ShortSortClass) ) { tom.gom.adt.gom.types.SortDecl  tomMatch499_38= (( tom.gom.adt.code.types.Code )((Object)code)).getSort() ;if ( (tomMatch499_38 instanceof tom.gom.adt.gom.types.SortDecl) ) {if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch499_38) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {

        writer.write( tomMatch499_38.getName() );
        return;
      }}}}}}{if ( (((Object)code) instanceof tom.gom.adt.code.types.Code) ) {boolean tomMatch499_52= false ; tom.gom.adt.code.types.Code  tomMatch499_47= null ; tom.gom.adt.gom.types.SortDecl  tomMatch499_45= null ; tom.gom.adt.code.types.Code  tomMatch499_48= null ;if ( ((( tom.gom.adt.code.types.Code )((Object)code)) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )((Object)code))) instanceof tom.gom.adt.code.types.code.FullSortClass) ) {{tomMatch499_52= true ;tomMatch499_47=(( tom.gom.adt.code.types.Code )((Object)code));tomMatch499_45= tomMatch499_47.getSort() ;}} else {if ( ((( tom.gom.adt.code.types.Code )((Object)code)) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )((Object)code))) instanceof tom.gom.adt.code.types.code.ShortSortClass) ) {{tomMatch499_52= true ;tomMatch499_48=(( tom.gom.adt.code.types.Code )((Object)code));tomMatch499_45= tomMatch499_48.getSort() ;}}}}}if (tomMatch499_52) {if ( (tomMatch499_45 instanceof tom.gom.adt.gom.types.SortDecl) ) {if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch499_45) instanceof tom.gom.adt.gom.types.sortdecl.BuiltinSortDecl) ) {

        writer.write( tomMatch499_45.getName() );
        return;
      }}}}}{if ( (((Object)code) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )((Object)code)) instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )((Object)code))) instanceof tom.gom.adt.code.types.code.Compare) ) {

        generateCode( (( tom.gom.adt.code.types.Code )((Object)code)).getLCode() , writer);
        writer.write(".compareTo(");
        generateCode( (( tom.gom.adt.code.types.Code )((Object)code)).getRCode() , writer);
        writer.write(")");
        return;
      }}}}{if ( (((Object)code) instanceof tom.gom.adt.code.types.Code) ) {if ( (((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )((Object)code))) instanceof tom.gom.adt.code.types.code.ConsCodeList) || ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )((Object)code))) instanceof tom.gom.adt.code.types.code.EmptyCodeList)) ) {if ( (  (( tom.gom.adt.code.types.Code )((Object)code)).isEmptyCodeList()  ||  ((( tom.gom.adt.code.types.Code )((Object)code))== tom.gom.adt.code.types.code.EmptyCodeList.make() )  ) ) {
 return ; }}}}{if ( (((Object)code) instanceof tom.gom.adt.code.types.Code) ) {if ( (((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )((Object)code))) instanceof tom.gom.adt.code.types.code.ConsCodeList) || ((( tom.gom.adt.code.types.Code )(( tom.gom.adt.code.types.Code )((Object)code))) instanceof tom.gom.adt.code.types.code.EmptyCodeList)) ) {if (!( (  (( tom.gom.adt.code.types.Code )((Object)code)).isEmptyCodeList()  ||  ((( tom.gom.adt.code.types.Code )((Object)code))== tom.gom.adt.code.types.code.EmptyCodeList.make() )  ) )) {

        generateCode((( (((( tom.gom.adt.code.types.Code )((Object)code)) instanceof tom.gom.adt.code.types.code.ConsCodeList) || ((( tom.gom.adt.code.types.Code )((Object)code)) instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )?( (( tom.gom.adt.code.types.Code )((Object)code)).getHeadCodeList() ):((( tom.gom.adt.code.types.Code )((Object)code)))),writer);
        generateCode((( (((( tom.gom.adt.code.types.Code )((Object)code)) instanceof tom.gom.adt.code.types.code.ConsCodeList) || ((( tom.gom.adt.code.types.Code )((Object)code)) instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )?( (( tom.gom.adt.code.types.Code )((Object)code)).getTailCodeList() ):( tom.gom.adt.code.types.code.EmptyCodeList.make() )),writer);
        return;
      }}}}}

    throw new GomRuntimeException("Can't generate code for " + code);
  }
}
