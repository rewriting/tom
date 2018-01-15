























package tom.gom.antlradapter;



import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;



import tom.gom.GomStreamManager;
import tom.gom.tools.GomEnvironment;
import tom.gom.adt.gom.*;
import tom.gom.adt.gom.types.*;
import tom.gom.backend.CodeGen;
import tom.gom.tools.error.GomRuntimeException;



import tom.gom.adt.code.types.*;
import tom.gom.adt.objects.*;
import tom.gom.adt.objects.types.*;
import tom.library.sl.VisitFailure;
import tom.library.sl.Strategy;



public class AdapterGenerator {

  
  private File tomHomePath;
  
  
  
  private GomEnvironment gomEnvironment;
  private String grammarPkg = "";

  AdapterGenerator(File tomHomePath, GomEnvironment gomEnvironment) {
    this.tomHomePath = tomHomePath;
    this.gomEnvironment = gomEnvironment;
    this.grammarPkg = getStreamManager().getDefaultPackagePath();
  }

  private GomStreamManager getStreamManager() {
    return gomEnvironment.getStreamManager();
  }

     private static   tom.gom.adt.code.types.Code  tom_append_list_CodeList( tom.gom.adt.code.types.Code  l1,  tom.gom.adt.code.types.Code  l2) {     if( l1.isEmptyCodeList() ) {       return l2;     } else if( l2.isEmptyCodeList() ) {       return l1;     } else if( ((l1 instanceof tom.gom.adt.code.types.code.ConsCodeList) || (l1 instanceof tom.gom.adt.code.types.code.EmptyCodeList)) ) {       if(  l1.getTailCodeList() .isEmptyCodeList() ) {         return  tom.gom.adt.code.types.code.ConsCodeList.make( l1.getHeadCodeList() ,l2) ;       } else {         return  tom.gom.adt.code.types.code.ConsCodeList.make( l1.getHeadCodeList() ,tom_append_list_CodeList( l1.getTailCodeList() ,l2)) ;       }     } else {       return  tom.gom.adt.code.types.code.ConsCodeList.make(l1,l2) ;     }   }   private static   tom.gom.adt.code.types.Code  tom_get_slice_CodeList( tom.gom.adt.code.types.Code  begin,  tom.gom.adt.code.types.Code  end, tom.gom.adt.code.types.Code  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyCodeList()  ||  (end== tom.gom.adt.code.types.code.EmptyCodeList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.code.types.code.ConsCodeList.make((( ((begin instanceof tom.gom.adt.code.types.code.ConsCodeList) || (begin instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )? begin.getHeadCodeList() :begin),( tom.gom.adt.code.types.Code )tom_get_slice_CodeList((( ((begin instanceof tom.gom.adt.code.types.code.ConsCodeList) || (begin instanceof tom.gom.adt.code.types.code.EmptyCodeList)) )? begin.getTailCodeList() : tom.gom.adt.code.types.code.EmptyCodeList.make() ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ): null ),end,tail)) ;   }   private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) , tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ), null ) ) , null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ), null ) ) , null ) ) ) ));}private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return (  tom.library.sl.Choice.make(s, tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) )  );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.MuVar("_x") ), null ) ) , tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}






  public void generate(ModuleList moduleList, HookDeclList hookDecls) {
    final Map<OperatorDecl,Integer> operatormap = new HashMap<OperatorDecl,Integer>();
    IntRef intref = new IntRef(10);
    try {
      tom_make_TopDown( new CollectOperators(operatormap,intref) ).visitLight(moduleList);
    } catch (VisitFailure f) {
      throw new GomRuntimeException("CollectOperators should not fail");
    }
    writeTokenFile(operatormap);
    writeAdapterFile(operatormap);
  }

  public int writeTokenFile(Map<OperatorDecl,Integer> operatormap) {
    try {
       File output = tokenFileToGenerate();
       
       output.getParentFile().mkdirs();
       Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output)));
       generateTokenFile(operatormap, writer);
       writer.flush();
       writer.close();
    } catch(Exception e) {
      e.printStackTrace();
      return 1;
    }
    return 0;
  }

  public int writeAdapterFile(Map<OperatorDecl,Integer> operatormap) {
    try {
       File output = adaptorFileToGenerate();
       
       output.getParentFile().mkdirs();
       Writer writer =
         new BufferedWriter(
             new OutputStreamWriter(
               new FileOutputStream(output)));
       generateAdapterFile(operatormap, writer);
       writer.flush();
       writer.close();
    } catch(Exception e) {
      e.printStackTrace();
      return 1;
    }
    return 0;
  }

  private String adapterPkg() {
    String packagePrefix = getStreamManager().getDefaultPackagePath();
    return ((packagePrefix=="")?filename():packagePrefix+"."+filename()).toLowerCase();
  }

  public void generateAdapterFile(Map<OperatorDecl,Integer> operatormap, Writer writer)
    throws java.io.IOException {
    writer.write(
    "\npackage "+adapterPkg()+";\n\nimport org.antlr.runtime.Token;\nimport org.antlr.runtime.tree.Tree;\n"




);
    if (!"".equals(grammarPkg)) {
    writer.write("\n"
);
    }
    writer.write("\npublic class "+filename()+"Adaptor {\n  public static tom.library.sl.Visitable getTerm(Tree tree) {\n    tom.library.sl.Visitable res = null;\n    if (tree.isNil()) {\n      throw new RuntimeException(\"nil term\");\n    }\n    if (tree.getType()==Token.INVALID_TOKEN_TYPE) {\n      throw new RuntimeException(\"bad type\");\n    }\n\n    switch (tree.getType()) {\n"











);

    for (Map.Entry<OperatorDecl,Integer> entry : operatormap.entrySet()) {
      OperatorDecl opDecl = entry.getKey();
      String opkey = entry.getValue().toString();
      { /* unamed block */{ /* unamed block */if ( (opDecl instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )opDecl) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.TypedProduction  tomMatch601_1= (( tom.gom.adt.gom.types.OperatorDecl )opDecl).getProd() ;if ( ((( tom.gom.adt.gom.types.TypedProduction )tomMatch601_1) instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) { tom.gom.adt.gom.types.SortDecl  tom___domainSort= tomMatch601_1.getSort() ; tom.gom.adt.gom.types.OperatorDecl  tom___op=(( tom.gom.adt.gom.types.OperatorDecl )opDecl);


          Code cast = genGetTerm(tom___domainSort,"tree.getChild(i)");
          Code code =
             tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("      case ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(opkey) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(":\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("        {\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("          res = ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Empty.make(tom___op) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(".make();\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("          for(int i = 0; i < tree.getChildCount(); i++) {\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("            ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.FullSortClass.make(tom___domainSort) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(" elem = ") ,tom_append_list_CodeList(cast, tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(";\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("            ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.FullOperatorClass.make(tom___op) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(" list = (") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.FullOperatorClass.make(tom___op) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(") res;\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("            ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("res = list.append(elem);\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("          }\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("          break;\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("        }\n") , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) ) ) ) ) ) ) ) ) ) )) ) ) ) ) ) ) ) ) ) ) 
























;
          CodeGen.generateCode(code,writer);
        }}}}{ /* unamed block */if ( (opDecl instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )opDecl) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) { tom.gom.adt.gom.types.TypedProduction  tomMatch601_8= (( tom.gom.adt.gom.types.OperatorDecl )opDecl).getProd() ; tom.gom.adt.gom.types.TypedProduction  tom___prod=tomMatch601_8;boolean tomMatch601_14= false ;if ( ((( tom.gom.adt.gom.types.TypedProduction )tomMatch601_8) instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) {if ( (tom___prod==tomMatch601_8) ) {tomMatch601_14= true ;}}if (!(tomMatch601_14)) {


        Code code =
           tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("      case ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(opkey) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(":\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("        {\n") , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) ) ) 




;
        { /* unamed block */{ /* unamed block */if ( (tom___prod instanceof tom.gom.adt.gom.types.TypedProduction) ) {if ( ((( tom.gom.adt.gom.types.TypedProduction )tom___prod) instanceof tom.gom.adt.gom.types.typedproduction.Slots) ) { tom.gom.adt.gom.types.SlotList  tom___slotList= (( tom.gom.adt.gom.types.TypedProduction )tom___prod).getSlots() ;

            int idx = 0;
            SlotList sList = tom___slotList;
            int length = sList.length();
            String sCode = "\n          if (tree.getChildCount()!="+length+") {\n            throw new RuntimeException(\"Node \" + tree + \": "+length+" child(s) expected, but \" + tree.getChildCount() + \" found\");\n          }\n"



;
            code =  tom.gom.adt.code.types.code.ConsCodeList.make(code, tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(sCode) , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) ;

            while(sList.isConsConcSlot()) { /* unamed block */
              Slot slot = sList.getHeadConcSlot();
              sList = sList.getTailConcSlot();
              Code cast = genGetTerm(slot.getSort(),"tree.getChild("+idx+")");
              code =  tom.gom.adt.code.types.code.ConsCodeList.make(code, tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("          ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.FullSortClass.make(slot.getSort()) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(" field"+idx+" = ") ,tom_append_list_CodeList(cast, tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(";\n") , tom.gom.adt.code.types.code.EmptyCodeList.make() ) )) ) ) ) 





;
              idx++;
            }
            code =  tom.gom.adt.code.types.code.ConsCodeList.make(code, tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("          res = ") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.FullOperatorClass.make((( tom.gom.adt.gom.types.OperatorDecl )opDecl)) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(".make(") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(genArgsList(tom___slotList)) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(");\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("          break;\n") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("        }\n") , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) ) ) ) ) ) ) 







;
              }}}}


          CodeGen.generateCode(code,writer);
        }}}}}


    }

    writer.write("\n    }\n    return res;\n  }\n}\n"




);
  }

  public void generateTokenFile(Map<OperatorDecl,Integer> operatormap, Writer writer)
    throws java.io.IOException {
    for (Map.Entry<OperatorDecl,Integer> entry : operatormap.entrySet()) {
      final OperatorDecl decl = entry.getKey();
      { /* unamed block */{ /* unamed block */if ( (decl instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )decl) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {

          writer.write( (( tom.gom.adt.gom.types.OperatorDecl )decl).getName()  + "="+entry.getValue().intValue()+"\n");
        }}}}

    }
  }

  static class IntRef {
    IntRef(int val) {
      intValue = val;
    }
    public int intValue;
  }
  public static class CollectOperators extends tom.library.sl.AbstractStrategyBasic {private  Map<OperatorDecl,Integer>  bag;private  IntRef  intref;public CollectOperators( Map<OperatorDecl,Integer>  bag,  IntRef  intref) {super(( new tom.library.sl.Identity() ));this.bag=bag;this.intref=intref;}public  Map<OperatorDecl,Integer>  getbag() {return bag;}public  IntRef  getintref() {return intref;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.gom.adt.gom.types.OperatorDecl) ) {return ((T)visit_OperatorDecl((( tom.gom.adt.gom.types.OperatorDecl )v),introspector));}if (!(  null ==environment )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.gom.types.OperatorDecl  _visit_OperatorDecl( tom.gom.adt.gom.types.OperatorDecl  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(  null ==environment )) {return (( tom.gom.adt.gom.types.OperatorDecl )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.gom.types.OperatorDecl  visit_OperatorDecl( tom.gom.adt.gom.types.OperatorDecl  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )tom__arg) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {




        bag.put((( tom.gom.adt.gom.types.OperatorDecl )tom__arg),Integer.valueOf(intref.intValue));
        intref.intValue++;
      }}}}return _visit_OperatorDecl(tom__arg,introspector);}}



  protected Code genGetTerm(SortDecl sort, String tree) {
    Code code =  tom.gom.adt.code.types.code.EmptyCodeList.make() ;
    { /* unamed block */{ /* unamed block */if ( (sort instanceof tom.gom.adt.gom.types.SortDecl) ) {if ( ((( tom.gom.adt.gom.types.SortDecl )sort) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {

        code =  tom.gom.adt.code.types.code.ConsCodeList.make(code, tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("(") , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.FullSortClass.make(sort) , tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(")"+filename()+"Adaptor.getTerm("+tree+")") , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) ) ) 


;
      }}}{ /* unamed block */if ( (sort instanceof tom.gom.adt.gom.types.SortDecl) ) {if ( ((( tom.gom.adt.gom.types.SortDecl )sort) instanceof tom.gom.adt.gom.types.sortdecl.BuiltinSortDecl) ) { String  tom___name= (( tom.gom.adt.gom.types.SortDecl )sort).getName() ;

        if ("int".equals(tom___name)) { /* unamed block */
          code =  tom.gom.adt.code.types.code.ConsCodeList.make(code, tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("Integer.parseInt("+tree+".getText())") , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) 
;
        } else if ("long".equals(tom___name)) { /* unamed block */
          code =  tom.gom.adt.code.types.code.ConsCodeList.make(code, tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("Long.parseLong("+tree+".getText())") , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) 
;
        } else if ("String".equals(tom___name)) { /* unamed block */
          code =  tom.gom.adt.code.types.code.ConsCodeList.make(code, tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make(tree+".getText()") , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) 
;
        } else if ("boolean".equals(tom___name)) { /* unamed block */
          code =  tom.gom.adt.code.types.code.ConsCodeList.make(code, tom.gom.adt.code.types.code.ConsCodeList.make( tom.gom.adt.code.types.code.Code.make("Boolean.valueOf("+tree+".getText())") , tom.gom.adt.code.types.code.EmptyCodeList.make() ) ) 
;
        } else { /* unamed block */
          throw new RuntimeException("Unsupported builtin "+tom___name);
        }}}}}


    return code;
  }

  protected String genArgsList(SlotList slots) {
    String res = "";
    SlotList sList = slots;
    int idx = 0;
    while (sList.isConsConcSlot()) {
      Slot slot = sList.getHeadConcSlot();
      sList = sList.getTailConcSlot();
      res += "field" + idx;
      if (sList.isConsConcSlot()) {
        res += ", ";
      }
      idx++;
    }
    return res;
  }

  protected String fullFileName() {
    return (adapterPkg() + "." + filename()).replace('.',File.separatorChar);
  }

  protected String filename() {
    String filename = (new File(getStreamManager().getOutputFileName())).getName();
    int dotidx = filename.indexOf('.');
    if (-1 != dotidx) {
      filename = filename.substring(0,dotidx);
    }
    return filename;
  }

  protected File tokenFileToGenerate() {
    File output = new File(
        getStreamManager().getDestDir(),
        fullFileName()+"Tokens.tokens");
    return output;
  }

  protected File adaptorFileToGenerate() {
    File output = new File(
        getStreamManager().getDestDir(),
        fullFileName()+"Adaptor.java");
    return output;
  }
}
