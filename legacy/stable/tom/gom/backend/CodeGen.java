























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
    
  }

  
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

  
  public static void generateCode(Code code, Writer writer)
    throws java.io.IOException {
    { /* unamed block */{ /* unamed block */if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.Code) ) {

        writer.write( (( tom.gom.adt.code.types.Code )code).getprog() );
        return;
      }}}{ /* unamed block */if ( (code instanceof tom.gom.adt.code.types.Code) ) {boolean tomMatch608_9= false ; tom.gom.adt.gom.types.OperatorDecl  tomMatch608_5= null ; tom.gom.adt.code.types.Code  tomMatch608_7= null ; tom.gom.adt.code.types.Code  tomMatch608_8= null ;if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.Empty) ) {{ /* unamed block */tomMatch608_9= true ;tomMatch608_7=(( tom.gom.adt.code.types.Code )code);tomMatch608_5= tomMatch608_7.getOperator() ;}} else {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.Cons) ) {{ /* unamed block */tomMatch608_9= true ;tomMatch608_8=(( tom.gom.adt.code.types.Code )code);tomMatch608_5= tomMatch608_8.getOperator() ;}}}if (tomMatch608_9) { tom.gom.adt.gom.types.OperatorDecl  tom___opdecl=tomMatch608_5;{ /* unamed block */{ /* unamed block */if ( (tom___opdecl instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )tom___opdecl) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.SortDecl  tomMatch609_2= (( tom.gom.adt.gom.types.OperatorDecl )tom___opdecl).getSort() ; String  tom___opName= (( tom.gom.adt.gom.types.OperatorDecl )tom___opdecl).getName() ;if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch609_2) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {if ( ((( tom.gom.adt.gom.types.TypedProduction ) (( tom.gom.adt.gom.types.OperatorDecl )tom___opdecl).getProd() ) instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) {





            String tName = tom___opName;
            { /* unamed block */{ /* unamed block */if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.Empty) ) {

                tName = "Empty" + tom___opName;
              }}}{ /* unamed block */if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.Cons) ) {

                tName = "Cons" + tom___opName;
              }}}}

            String sortNamePackage =  tomMatch609_2.getName() .toLowerCase();
            ClassName className =  tom.gom.adt.objects.types.classname.ClassName.make(tom.gom.compiler.Compiler.packagePrefix( tomMatch609_2.getModuleDecl() )+".types."+sortNamePackage, tName) 

;
            writer.write(tom.gom.backend.TemplateClass.fullClassName(className));
            return;
          }}}}}}

        GomMessage.error(logger, null, 0, 
            GomMessage.expectingVariadicButGot, (tom___opdecl));
        return;
      }}}{ /* unamed block */if ( (code instanceof tom.gom.adt.code.types.Code) ) {boolean tomMatch608_16= false ; tom.gom.adt.code.types.Code  tomMatch608_15= null ; String  tomMatch608_11= "" ; tom.gom.adt.gom.types.OperatorDecl  tomMatch608_12= null ; tom.gom.adt.code.types.Code  tomMatch608_14= null ;if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.IsEmpty) ) {{ /* unamed block */tomMatch608_16= true ;tomMatch608_14=(( tom.gom.adt.code.types.Code )code);tomMatch608_11= tomMatch608_14.getVar() ;tomMatch608_12= tomMatch608_14.getOperator() ;}} else {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.IsCons) ) {{ /* unamed block */tomMatch608_16= true ;tomMatch608_15=(( tom.gom.adt.code.types.Code )code);tomMatch608_11= tomMatch608_15.getVar() ;tomMatch608_12= tomMatch608_15.getOperator() ;}}}if (tomMatch608_16) { tom.gom.adt.gom.types.OperatorDecl  tom___opdecl=tomMatch608_12;{ /* unamed block */{ /* unamed block */if ( (tom___opdecl instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )tom___opdecl) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.TypedProduction ) (( tom.gom.adt.gom.types.OperatorDecl )tom___opdecl).getProd() ) instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) {



            writer.write(tomMatch608_11);
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
      }}}{ /* unamed block */if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.FullOperatorClass) ) { tom.gom.adt.gom.types.OperatorDecl  tomMatch608_18= (( tom.gom.adt.code.types.Code )code).getOperator() ;if ( ((( tom.gom.adt.gom.types.OperatorDecl )tomMatch608_18) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.SortDecl  tomMatch608_22= tomMatch608_18.getSort() ;if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch608_22) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {






        String sortNamePackage =  tomMatch608_22.getName() .toLowerCase();
        ClassName className =  tom.gom.adt.objects.types.classname.ClassName.make(tom.gom.compiler.Compiler.packagePrefix( tomMatch608_22.getModuleDecl() )+".types."+sortNamePackage,  tomMatch608_18.getName() ) 

;
        writer.write(tom.gom.backend.TemplateClass.fullClassName(className));
        return;
      }}}}}{ /* unamed block */if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.FullSortClass) ) { tom.gom.adt.gom.types.SortDecl  tomMatch608_30= (( tom.gom.adt.code.types.Code )code).getSort() ;if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch608_30) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {

        ClassName sortClassName =  tom.gom.adt.objects.types.classname.ClassName.make(tom.gom.compiler.Compiler.packagePrefix( tomMatch608_30.getModuleDecl() )+".types",  tomMatch608_30.getName() ) 
;
        writer.write(tom.gom.backend.TemplateClass.fullClassName(sortClassName));
        return;
      }}}}{ /* unamed block */if ( (code instanceof tom.gom.adt.code.types.Code) ) {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.ShortSortClass) ) { tom.gom.adt.gom.types.SortDecl  tomMatch608_38= (( tom.gom.adt.code.types.Code )code).getSort() ;if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch608_38) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {

        writer.write( tomMatch608_38.getName() );
        return;
      }}}}{ /* unamed block */if ( (code instanceof tom.gom.adt.code.types.Code) ) {boolean tomMatch608_52= false ; tom.gom.adt.gom.types.SortDecl  tomMatch608_45= null ; tom.gom.adt.code.types.Code  tomMatch608_48= null ; tom.gom.adt.code.types.Code  tomMatch608_47= null ;if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.FullSortClass) ) {{ /* unamed block */tomMatch608_52= true ;tomMatch608_47=(( tom.gom.adt.code.types.Code )code);tomMatch608_45= tomMatch608_47.getSort() ;}} else {if ( ((( tom.gom.adt.code.types.Code )code) instanceof tom.gom.adt.code.types.code.ShortSortClass) ) {{ /* unamed block */tomMatch608_52= true ;tomMatch608_48=(( tom.gom.adt.code.types.Code )code);tomMatch608_45= tomMatch608_48.getSort() ;}}}if (tomMatch608_52) {if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch608_45) instanceof tom.gom.adt.gom.types.sortdecl.BuiltinSortDecl) ) {

        writer.write( tomMatch608_45.getName() );
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
