
























package tom.engine.parser.antlr4;



import java.util.logging.Logger;
import java.util.*;
import java.io.*;



import java.lang.reflect.InvocationTargetException;



import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;



import tom.engine.adt.cst.types.*;
import tom.engine.adt.code.types.*;



import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.exception.TomRuntimeException;
import tom.engine.exception.TomIncludeException;
import tom.engine.exception.TomException;
import tom.engine.parser.TomParserTool;



import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;
import tom.engine.TomStreamManager;
import tom.platform.OptionManager;



import tom.library.sl.*;



public class CstConverter {
     private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ): null ),end,tail)) ;   }   private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) , tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ), null ) ) , null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ), null ) ) , null ) ) ) ));}private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return (  tom.library.sl.Choice.make(s, tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) )  );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.MuVar("_x") ), null ) ) , tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_BottomUp( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), tom.library.sl.Sequence.make(v, null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}   private static   tom.engine.adt.cst.types.CstSlotList  tom_append_list_ConcCstSlot( tom.engine.adt.cst.types.CstSlotList l1,  tom.engine.adt.cst.types.CstSlotList  l2) {     if( l1.isEmptyConcCstSlot() ) {       return l2;     } else if( l2.isEmptyConcCstSlot() ) {       return l1;     } else if(  l1.getTailConcCstSlot() .isEmptyConcCstSlot() ) {       return  tom.engine.adt.cst.types.cstslotlist.ConsConcCstSlot.make( l1.getHeadConcCstSlot() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstslotlist.ConsConcCstSlot.make( l1.getHeadConcCstSlot() ,tom_append_list_ConcCstSlot( l1.getTailConcCstSlot() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstSlotList  tom_get_slice_ConcCstSlot( tom.engine.adt.cst.types.CstSlotList  begin,  tom.engine.adt.cst.types.CstSlotList  end, tom.engine.adt.cst.types.CstSlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstSlot()  ||  (end== tom.engine.adt.cst.types.cstslotlist.EmptyConcCstSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstslotlist.ConsConcCstSlot.make( begin.getHeadConcCstSlot() ,( tom.engine.adt.cst.types.CstSlotList )tom_get_slice_ConcCstSlot( begin.getTailConcCstSlot() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstBlockList  tom_append_list_ConcCstBlock( tom.engine.adt.cst.types.CstBlockList l1,  tom.engine.adt.cst.types.CstBlockList  l2) {     if( l1.isEmptyConcCstBlock() ) {       return l2;     } else if( l2.isEmptyConcCstBlock() ) {       return l1;     } else if(  l1.getTailConcCstBlock() .isEmptyConcCstBlock() ) {       return  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make( l1.getHeadConcCstBlock() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make( l1.getHeadConcCstBlock() ,tom_append_list_ConcCstBlock( l1.getTailConcCstBlock() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstBlockList  tom_get_slice_ConcCstBlock( tom.engine.adt.cst.types.CstBlockList  begin,  tom.engine.adt.cst.types.CstBlockList  end, tom.engine.adt.cst.types.CstBlockList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstBlock()  ||  (end== tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make( begin.getHeadConcCstBlock() ,( tom.engine.adt.cst.types.CstBlockList )tom_get_slice_ConcCstBlock( begin.getTailConcCstBlock() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstOptionList  tom_append_list_ConcCstOption( tom.engine.adt.cst.types.CstOptionList l1,  tom.engine.adt.cst.types.CstOptionList  l2) {     if( l1.isEmptyConcCstOption() ) {       return l2;     } else if( l2.isEmptyConcCstOption() ) {       return l1;     } else if(  l1.getTailConcCstOption() .isEmptyConcCstOption() ) {       return  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make( l1.getHeadConcCstOption() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make( l1.getHeadConcCstOption() ,tom_append_list_ConcCstOption( l1.getTailConcCstOption() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstOptionList  tom_get_slice_ConcCstOption( tom.engine.adt.cst.types.CstOptionList  begin,  tom.engine.adt.cst.types.CstOptionList  end, tom.engine.adt.cst.types.CstOptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstOption()  ||  (end== tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make( begin.getHeadConcCstOption() ,( tom.engine.adt.cst.types.CstOptionList )tom_get_slice_ConcCstOption( begin.getTailConcCstOption() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstNameList  tom_append_list_ConcCstName( tom.engine.adt.cst.types.CstNameList l1,  tom.engine.adt.cst.types.CstNameList  l2) {     if( l1.isEmptyConcCstName() ) {       return l2;     } else if( l2.isEmptyConcCstName() ) {       return l1;     } else if(  l1.getTailConcCstName() .isEmptyConcCstName() ) {       return  tom.engine.adt.cst.types.cstnamelist.ConsConcCstName.make( l1.getHeadConcCstName() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstnamelist.ConsConcCstName.make( l1.getHeadConcCstName() ,tom_append_list_ConcCstName( l1.getTailConcCstName() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstNameList  tom_get_slice_ConcCstName( tom.engine.adt.cst.types.CstNameList  begin,  tom.engine.adt.cst.types.CstNameList  end, tom.engine.adt.cst.types.CstNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstName()  ||  (end== tom.engine.adt.cst.types.cstnamelist.EmptyConcCstName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstnamelist.ConsConcCstName.make( begin.getHeadConcCstName() ,( tom.engine.adt.cst.types.CstNameList )tom_get_slice_ConcCstName( begin.getTailConcCstName() ,end,tail)) ;   }      private static   tom.engine.adt.cst.types.CstBQTermList  tom_append_list_ConcCstBQTerm( tom.engine.adt.cst.types.CstBQTermList l1,  tom.engine.adt.cst.types.CstBQTermList  l2) {     if( l1.isEmptyConcCstBQTerm() ) {       return l2;     } else if( l2.isEmptyConcCstBQTerm() ) {       return l1;     } else if(  l1.getTailConcCstBQTerm() .isEmptyConcCstBQTerm() ) {       return  tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( l1.getHeadConcCstBQTerm() ,l2) ;     } else {       return  tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( l1.getHeadConcCstBQTerm() ,tom_append_list_ConcCstBQTerm( l1.getTailConcCstBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.cst.types.CstBQTermList  tom_get_slice_ConcCstBQTerm( tom.engine.adt.cst.types.CstBQTermList  begin,  tom.engine.adt.cst.types.CstBQTermList  end, tom.engine.adt.cst.types.CstBQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcCstBQTerm()  ||  (end== tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( begin.getHeadConcCstBQTerm() ,( tom.engine.adt.cst.types.CstBQTermList )tom_get_slice_ConcCstBQTerm( begin.getTailConcCstBQTerm() ,end,tail)) ;   }   



  private static Logger logger = Logger.getLogger("tom.engine.parser.CstConverter");
  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

  private TomParserTool parserTool;
  private static HashMap<String,List<String>> includedFiles = new HashMap<String,List<String>>();

  public CstConverter(TomParserTool parserTool) {
    this.parserTool = parserTool;
  }

  public TomParserTool getParserTool() {
    return this.parserTool;
  }

  public TomStreamManager getStreamManager() {
    return this.getParserTool().getStreamManager();
  }

  public CstProgram convert(CstProgram t) {
    try {
      t = tom_make_BottomUp( new SimplifyCST(this) ).visitLight(t);
    } catch(VisitFailure e) {
      
    }
    return t;
  }

  
  public static class SimplifyCST extends tom.library.sl.AbstractStrategyBasic {private  CstConverter  cc;public SimplifyCST( CstConverter  cc) {super(( new tom.library.sl.Identity() ));this.cc=cc;}public  CstConverter  getcc() {return cc;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.cst.types.CstBQTermList) ) {return ((T)visit_CstBQTermList((( tom.engine.adt.cst.types.CstBQTermList )v),introspector));}if ( (v instanceof tom.engine.adt.cst.types.CstPattern) ) {return ((T)visit_CstPattern((( tom.engine.adt.cst.types.CstPattern )v),introspector));}if ( (v instanceof tom.engine.adt.cst.types.CstBQTerm) ) {return ((T)visit_CstBQTerm((( tom.engine.adt.cst.types.CstBQTerm )v),introspector));}if ( (v instanceof tom.engine.adt.cst.types.CstBlockList) ) {return ((T)visit_CstBlockList((( tom.engine.adt.cst.types.CstBlockList )v),introspector));}if ( (v instanceof tom.engine.adt.cst.types.CstBlock) ) {return ((T)visit_CstBlock((( tom.engine.adt.cst.types.CstBlock )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.cst.types.CstBlock  _visit_CstBlock( tom.engine.adt.cst.types.CstBlock  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.cst.types.CstBlock )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.cst.types.CstBlockList  _visit_CstBlockList( tom.engine.adt.cst.types.CstBlockList  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.cst.types.CstBlockList )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.cst.types.CstBQTerm  _visit_CstBQTerm( tom.engine.adt.cst.types.CstBQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.cst.types.CstBQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.cst.types.CstPattern  _visit_CstPattern( tom.engine.adt.cst.types.CstPattern  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.cst.types.CstPattern )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.cst.types.CstBQTermList  _visit_CstBQTermList( tom.engine.adt.cst.types.CstBQTermList  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.cst.types.CstBQTermList )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.cst.types.CstBQTermList  visit_CstBQTermList( tom.engine.adt.cst.types.CstBQTermList  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.cst.types.CstBQTermList) ) {if ( (((( tom.engine.adt.cst.types.CstBQTermList )tom__arg) instanceof tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm) || ((( tom.engine.adt.cst.types.CstBQTermList )tom__arg) instanceof tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm)) ) {





















































































        
        return simplifyCstBQTermList((( tom.engine.adt.cst.types.CstBQTermList )tom__arg));
      }}}}return _visit_CstBQTermList(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.cst.types.CstBlockList  visit_CstBlockList( tom.engine.adt.cst.types.CstBlockList  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.cst.types.CstBlockList) ) {if ( (((( tom.engine.adt.cst.types.CstBlockList )tom__arg) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )tom__arg) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {                           return simplifyCstBlockList((( tom.engine.adt.cst.types.CstBlockList )tom__arg));       }}}}return _visit_CstBlockList(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.cst.types.CstPattern  visit_CstPattern( tom.engine.adt.cst.types.CstPattern  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.cst.types.CstPattern) ) {if ( ((( tom.engine.adt.cst.types.CstPattern )tom__arg) instanceof tom.engine.adt.cst.types.cstpattern.Cst_Anti) ) { tom.engine.adt.cst.types.CstPattern  tomMatch333_1= (( tom.engine.adt.cst.types.CstPattern )tom__arg).getpattern() ;if ( ((( tom.engine.adt.cst.types.CstPattern )tomMatch333_1) instanceof tom.engine.adt.cst.types.cstpattern.Cst_Anti) ) {          return  tomMatch333_1.getpattern() ;        }}}}}return _visit_CstPattern(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.cst.types.CstBQTerm  visit_CstBQTerm( tom.engine.adt.cst.types.CstBQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.cst.types.CstBQTerm) ) {if ( ((( tom.engine.adt.cst.types.CstBQTerm )tom__arg) instanceof tom.engine.adt.cst.types.cstbqterm.Cst_BQComposite) ) { tom.engine.adt.cst.types.CstBQTermList  tomMatch334_2= (( tom.engine.adt.cst.types.CstBQTerm )tom__arg).getlist() ;if ( (((( tom.engine.adt.cst.types.CstBQTermList )tomMatch334_2) instanceof tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm) || ((( tom.engine.adt.cst.types.CstBQTermList )tomMatch334_2) instanceof tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm)) ) { tom.engine.adt.cst.types.CstBQTermList  tomMatch334_end_8=tomMatch334_2;do {{ /* unamed block */if (!( tomMatch334_end_8.isEmptyConcCstBQTerm() )) { tom.engine.adt.cst.types.CstBQTerm  tomMatch334_13= tomMatch334_end_8.getHeadConcCstBQTerm() ;if ( ((( tom.engine.adt.cst.types.CstBQTerm )tomMatch334_13) instanceof tom.engine.adt.cst.types.cstbqterm.Cst_BQComposite) ) {                   return  tom.engine.adt.cst.types.cstbqterm.Cst_BQComposite.make( (( tom.engine.adt.cst.types.CstBQTerm )tom__arg).getoptionList() , tom_append_list_ConcCstBQTerm(tom_get_slice_ConcCstBQTerm(tomMatch334_2,tomMatch334_end_8, tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() ),tom_append_list_ConcCstBQTerm( tomMatch334_13.getlist() ,tom_append_list_ConcCstBQTerm( tomMatch334_end_8.getTailConcCstBQTerm() , tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() )))) ;       }}if ( tomMatch334_end_8.isEmptyConcCstBQTerm() ) {tomMatch334_end_8=tomMatch334_2;} else {tomMatch334_end_8= tomMatch334_end_8.getTailConcCstBQTerm() ;}}} while(!( (tomMatch334_end_8==tomMatch334_2) ));}}}}}return _visit_CstBQTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.cst.types.CstBlock  visit_CstBlock( tom.engine.adt.cst.types.CstBlock  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )tom__arg) instanceof tom.engine.adt.cst.types.cstblock.Cst_IncludeFile) ) { tom.engine.adt.cst.types.CstOptionList  tomMatch335_1= (( tom.engine.adt.cst.types.CstBlock )tom__arg).getoptionList() ;if ( (((( tom.engine.adt.cst.types.CstOptionList )tomMatch335_1) instanceof tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption) || ((( tom.engine.adt.cst.types.CstOptionList )tomMatch335_1) instanceof tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption)) ) {if (!( tomMatch335_1.isEmptyConcCstOption() )) { tom.engine.adt.cst.types.CstOption  tomMatch335_12= tomMatch335_1.getHeadConcCstOption() ;if ( ((( tom.engine.adt.cst.types.CstOption )tomMatch335_12) instanceof tom.engine.adt.cst.types.cstoption.Cst_OriginTracking) ) {if (  tomMatch335_1.getTailConcCstOption() .isEmptyConcCstOption() ) {         try { /* unamed block */           return cc.includeFile( tomMatch335_12.getfileName() , (( tom.engine.adt.cst.types.CstBlock )tom__arg).getfilename() , tomMatch335_12.getstartLine() );         } catch(TomIncludeException e) { /* unamed block */           e.printStackTrace();         }}}}}}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )tom__arg) instanceof tom.engine.adt.cst.types.cstblock.Cst_GomConstruct) ) { tom.engine.adt.cst.types.CstOptionList  tomMatch335_15= (( tom.engine.adt.cst.types.CstBlock )tom__arg).getoptionList() ;if ( (((( tom.engine.adt.cst.types.CstOptionList )tomMatch335_15) instanceof tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption) || ((( tom.engine.adt.cst.types.CstOptionList )tomMatch335_15) instanceof tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption)) ) {if (!( tomMatch335_15.isEmptyConcCstOption() )) { tom.engine.adt.cst.types.CstOption  tomMatch335_27= tomMatch335_15.getHeadConcCstOption() ;if ( ((( tom.engine.adt.cst.types.CstOption )tomMatch335_27) instanceof tom.engine.adt.cst.types.cstoption.Cst_OriginTracking) ) {if (  tomMatch335_15.getTailConcCstOption() .isEmptyConcCstOption() ) {         try { /* unamed block */                      return cc.gomFile( tomMatch335_27.getfileName() , (( tom.engine.adt.cst.types.CstBlock )tom__arg).getnameList() , (( tom.engine.adt.cst.types.CstBlock )tom__arg).gettext() , tomMatch335_27.getstartLine() );         } catch(TomIncludeException e) { /* unamed block */           e.printStackTrace();         }}}}}}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )tom__arg) instanceof tom.engine.adt.cst.types.cstblock.Cst_Metaquote) ) { tom.engine.adt.cst.types.CstOptionList  tomMatch335_30= (( tom.engine.adt.cst.types.CstBlock )tom__arg).getoptionList() ; tom.engine.adt.cst.types.CstBlockList  tomMatch335_31= (( tom.engine.adt.cst.types.CstBlock )tom__arg).getblocks() ;if ( (((( tom.engine.adt.cst.types.CstOptionList )tomMatch335_30) instanceof tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption) || ((( tom.engine.adt.cst.types.CstOptionList )tomMatch335_30) instanceof tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption)) ) {if (!( tomMatch335_30.isEmptyConcCstOption() )) {if ( ((( tom.engine.adt.cst.types.CstOption ) tomMatch335_30.getHeadConcCstOption() ) instanceof tom.engine.adt.cst.types.cstoption.Cst_OriginTracking) ) {if (  tomMatch335_30.getTailConcCstOption() .isEmptyConcCstOption() ) {if ( (((( tom.engine.adt.cst.types.CstBlockList )tomMatch335_31) instanceof tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock) || ((( tom.engine.adt.cst.types.CstBlockList )tomMatch335_31) instanceof tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock)) ) {if (!( tomMatch335_31.isEmptyConcCstBlock() )) { tom.engine.adt.cst.types.CstBlock  tomMatch335_47= tomMatch335_31.getHeadConcCstBlock() ;if ( ((( tom.engine.adt.cst.types.CstBlock )tomMatch335_47) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) { String  tom___code= tomMatch335_47.getcontent() ;if (  tomMatch335_31.getTailConcCstBlock() .isEmptyConcCstBlock() ) {         String metacode = tom___code.substring(2,tom___code.length()-2);         CstBlockList bl = cc.tomSplitter( tomMatch335_47.getoptionList() , metacode);         return  tom.engine.adt.cst.types.cstblock.Cst_Metaquote.make(tomMatch335_30, bl) ;       }}}}}}}}}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )tom__arg) instanceof tom.engine.adt.cst.types.cstblock.Cst_StrategyConstruct) ) { tom.engine.adt.cst.types.CstOptionList  tomMatch335_50= (( tom.engine.adt.cst.types.CstBlock )tom__arg).getoptionList() ; tom.engine.adt.cst.types.CstName  tomMatch335_51= (( tom.engine.adt.cst.types.CstBlock )tom__arg).getstratName() ;if ( (((( tom.engine.adt.cst.types.CstOptionList )tomMatch335_50) instanceof tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption) || ((( tom.engine.adt.cst.types.CstOptionList )tomMatch335_50) instanceof tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption)) ) {if (!( tomMatch335_50.isEmptyConcCstOption() )) { tom.engine.adt.cst.types.CstOption  tomMatch335_67= tomMatch335_50.getHeadConcCstOption() ;if ( ((( tom.engine.adt.cst.types.CstOption )tomMatch335_67) instanceof tom.engine.adt.cst.types.cstoption.Cst_OriginTracking) ) {if (  tomMatch335_50.getTailConcCstOption() .isEmptyConcCstOption() ) {if ( ((( tom.engine.adt.cst.types.CstName )tomMatch335_51) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom___stratName= tomMatch335_51.getname() ; tom.engine.adt.cst.types.CstSlotList  tom___stratArgs= (( tom.engine.adt.cst.types.CstBlock )tom__arg).getstratArgs() ;         String opargs = "";         String getslots = "";         String makeargs = "";         String newargs = "";         int index = 1;         { /* unamed block */{ /* unamed block */if ( (tom___stratArgs instanceof tom.engine.adt.cst.types.CstSlotList) ) {if ( (((( tom.engine.adt.cst.types.CstSlotList )tom___stratArgs) instanceof tom.engine.adt.cst.types.cstslotlist.ConsConcCstSlot) || ((( tom.engine.adt.cst.types.CstSlotList )tom___stratArgs) instanceof tom.engine.adt.cst.types.cstslotlist.EmptyConcCstSlot)) ) { tom.engine.adt.cst.types.CstSlotList  tomMatch336_end_4=(( tom.engine.adt.cst.types.CstSlotList )tom___stratArgs);do {{ /* unamed block */if (!( tomMatch336_end_4.isEmptyConcCstSlot() )) { tom.engine.adt.cst.types.CstSlot  tomMatch336_9= tomMatch336_end_4.getHeadConcCstSlot() ;if ( ((( tom.engine.adt.cst.types.CstSlot )tomMatch336_9) instanceof tom.engine.adt.cst.types.cstslot.Cst_Slot) ) { tom.engine.adt.cst.types.CstName  tomMatch336_7= tomMatch336_9.getslotName() ; tom.engine.adt.cst.types.CstType  tomMatch336_8= tomMatch336_9.getslotTypeName() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch336_7) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) { String  tom___name= tomMatch336_7.getname() ;if ( ((( tom.engine.adt.cst.types.CstType )tomMatch336_8) instanceof tom.engine.adt.cst.types.csttype.Cst_Type) ) {             opargs += (tom___name + ":" +  tomMatch336_8.gettype()  + ",");             makeargs += ("t" + index + ",");             newargs += ("$t" + index + ",");             index++;             getslots += "get_slot("+tom___name+",t) { (("+tom___stratName+")$t).get"+tom___name+"() }";           }}}}if ( tomMatch336_end_4.isEmptyConcCstSlot() ) {tomMatch336_end_4=(( tom.engine.adt.cst.types.CstSlotList )tom___stratArgs);} else {tomMatch336_end_4= tomMatch336_end_4.getTailConcCstSlot() ;}}} while(!( (tomMatch336_end_4==(( tom.engine.adt.cst.types.CstSlotList )tom___stratArgs)) ));}}}}         index = opargs.lastIndexOf(',');         if(index > 0) { /* unamed block */ opargs = opargs.substring(0,index); }         index = makeargs.lastIndexOf(',');         if(index > 0) { /* unamed block */ makeargs = makeargs.substring(0,index); }         index = newargs.lastIndexOf(',');         if(index > 0) { /* unamed block */ newargs = newargs.substring(0,index); }          String opdecl = "\n          %op Strategy "+tom___stratName+"("+opargs+") {\n            is_fsym(t) { ($t instanceof "+tom___stratName+") }\n            "+getslots+"\n            make("+makeargs+") { new "+tom___stratName+"("+newargs+") }\n          }\n        ";                   ANTLRInputStream tomInput = new ANTLRInputStream(opdecl);         CstBlock block = cc.parseStream(tomInput, tomMatch335_67.getfileName() );         return  tom.engine.adt.cst.types.cstblock.Cst_AbstractBlock.make( tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make((( tom.engine.adt.cst.types.CstBlock )tom__arg), tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make(block, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() ) ) ) ;       }}}}}}}}}return _visit_CstBlock(tom__arg,introspector);}}





  
  private CstBlock includeFile(String currentFileName, String filename, int lineNumber) throws TomIncludeException {
    
    String canonicalPath = getParserTool().searchIncludeFile(currentFileName,filename,lineNumber);

    List<String> listOfIncludedFiles = includedFiles.get(getStreamManager().getInputFileName());
    if(listOfIncludedFiles == null) {
      listOfIncludedFiles = new ArrayList<String>();
      includedFiles.put(getStreamManager().getInputFileName(),listOfIncludedFiles);
    }

    
    
    
    if(listOfIncludedFiles.contains(canonicalPath)) {
      
      if(!getStreamManager().isSilentDiscardImport(filename)) {
        TomMessage.info(logger, currentFileName, lineNumber, TomMessage.includedFileAlreadyParsed,filename);
      }
      return  tom.engine.adt.cst.types.cstblock.Cst_IncludeConstruct.make( tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() ) ;
    } else {
      listOfIncludedFiles.add(canonicalPath);
      
    }
    
    
    try {
      ANTLRInputStream tomInput = new ANTLRFileStream(canonicalPath);
      CstBlock block = parseStream(tomInput,canonicalPath);
      return  tom.engine.adt.cst.types.cstblock.Cst_IncludeConstruct.make( tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make(block, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() ) ) ;
    } catch (IOException e) {
      throw new RuntimeException(e); 
    }
  }

  private CstBlock parseStream(ANTLRInputStream tomInput, String canonicalPath) {
    
    try {
      tom.engine.parser.antlr4.TomParser parser = new tom.engine.parser.antlr4.TomParser(canonicalPath, getParserTool(), getStreamManager().getSymbolTable());
      CstProgram include = parser.parse(tomInput);
      { /* unamed block */{ /* unamed block */if ( (include instanceof tom.engine.adt.cst.types.CstProgram) ) {if ( ((( tom.engine.adt.cst.types.CstProgram )include) instanceof tom.engine.adt.cst.types.cstprogram.Cst_Program) ) {
 
          return  tom.engine.adt.cst.types.cstblock.Cst_AbstractBlock.make( (( tom.engine.adt.cst.types.CstProgram )include).getblocks() ) ;
        }}}}

    } catch (Exception e) {
      throw new RuntimeException(e); 
    }

    return  tom.engine.adt.cst.types.cstblock.Cst_AbstractBlock.make( tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() ) ;
  }


  
  private CstBlock gomFile(String currentFileName, CstNameList nameList, String gomCode, int initialGomLine) throws TomIncludeException {
    
    
    int nbOpts = nameList.length();
    String[] userOpts = new String[nbOpts];
    int i = 0;
    { /* unamed block */{ /* unamed block */if ( (nameList instanceof tom.engine.adt.cst.types.CstNameList) ) {if ( (((( tom.engine.adt.cst.types.CstNameList )nameList) instanceof tom.engine.adt.cst.types.cstnamelist.ConsConcCstName) || ((( tom.engine.adt.cst.types.CstNameList )nameList) instanceof tom.engine.adt.cst.types.cstnamelist.EmptyConcCstName)) ) { tom.engine.adt.cst.types.CstNameList  tomMatch338_end_4=(( tom.engine.adt.cst.types.CstNameList )nameList);do {{ /* unamed block */if (!( tomMatch338_end_4.isEmptyConcCstName() )) { tom.engine.adt.cst.types.CstName  tomMatch338_8= tomMatch338_end_4.getHeadConcCstName() ;if ( ((( tom.engine.adt.cst.types.CstName )tomMatch338_8) instanceof tom.engine.adt.cst.types.cstname.Cst_Name) ) {

        userOpts[i++] =  tomMatch338_8.getname() ;
      }}if ( tomMatch338_end_4.isEmptyConcCstName() ) {tomMatch338_end_4=(( tom.engine.adt.cst.types.CstNameList )nameList);} else {tomMatch338_end_4= tomMatch338_end_4.getTailConcCstName() ;}}} while(!( (tomMatch338_end_4==(( tom.engine.adt.cst.types.CstNameList )nameList)) ));}}}}

    String generatedMapping = getParserTool().parseGomFile(gomCode,initialGomLine, userOpts);
    if(generatedMapping != null && generatedMapping.length() > 0) {
      return includeFile(currentFileName, generatedMapping, initialGomLine);
    }
    return  tom.engine.adt.cst.types.cstblock.Cst_AbstractBlock.make( tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() ) ;
  }

  
  private static CstBlockList simplifyCstBlockList(CstBlockList l) {
    CstBlockList res =  tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() ; 
    CstBlock last = null;
    int size = 0;
    while(!l.isEmptyConcCstBlock()) {
      size++;
      CstBlock head = l.getHeadConcCstBlock();
      if(!head.isHOSTBLOCK()) {
        if(last != null) {
          res =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make(last,tom_append_list_ConcCstBlock(res, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() )) ;
          last = null;
        }
        res =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make(head,tom_append_list_ConcCstBlock(res, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() )) ;
      } else {
        if(last == null) {
          last = head;
        } else {
          { /* unamed block */{ /* unamed block */if ( (last instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )last) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) { tom.engine.adt.cst.types.CstOptionList  tomMatch339_2= (( tom.engine.adt.cst.types.CstBlock )last).getoptionList() ;if ( (((( tom.engine.adt.cst.types.CstOptionList )tomMatch339_2) instanceof tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption) || ((( tom.engine.adt.cst.types.CstOptionList )tomMatch339_2) instanceof tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption)) ) {if (!( tomMatch339_2.isEmptyConcCstOption() )) { tom.engine.adt.cst.types.CstOption  tomMatch339_19= tomMatch339_2.getHeadConcCstOption() ;if ( ((( tom.engine.adt.cst.types.CstOption )tomMatch339_19) instanceof tom.engine.adt.cst.types.cstoption.Cst_OriginTracking) ) { String  tom___name= tomMatch339_19.getfileName() ;if (  tomMatch339_2.getTailConcCstOption() .isEmptyConcCstOption() ) {if ( (head instanceof tom.engine.adt.cst.types.CstBlock) ) {if ( ((( tom.engine.adt.cst.types.CstBlock )head) instanceof tom.engine.adt.cst.types.cstblock.HOSTBLOCK) ) { tom.engine.adt.cst.types.CstOptionList  tomMatch339_6= (( tom.engine.adt.cst.types.CstBlock )head).getoptionList() ;if ( (((( tom.engine.adt.cst.types.CstOptionList )tomMatch339_6) instanceof tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption) || ((( tom.engine.adt.cst.types.CstOptionList )tomMatch339_6) instanceof tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption)) ) {if (!( tomMatch339_6.isEmptyConcCstOption() )) { tom.engine.adt.cst.types.CstOption  tomMatch339_26= tomMatch339_6.getHeadConcCstOption() ;if ( ((( tom.engine.adt.cst.types.CstOption )tomMatch339_26) instanceof tom.engine.adt.cst.types.cstoption.Cst_OriginTracking) ) {if ( tom___name.equals( tomMatch339_26.getfileName() ) ) {if (  tomMatch339_6.getTailConcCstOption() .isEmptyConcCstOption() ) {


              String s = mergeString( (( tom.engine.adt.cst.types.CstBlock )last).getcontent() , (( tom.engine.adt.cst.types.CstBlock )head).getcontent() , tomMatch339_19.getendLine() , tomMatch339_19.getendColumn() , tomMatch339_26.getstartLine() , tomMatch339_26.getstartColumn() );
              last =  tom.engine.adt.cst.types.cstblock.HOSTBLOCK.make( tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make( tom.engine.adt.cst.types.cstoption.Cst_OriginTracking.make(tom___name,  tomMatch339_19.getstartLine() ,  tomMatch339_19.getstartColumn() ,  tomMatch339_26.getendLine() ,  tomMatch339_26.getendColumn() ) , tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) , s) ;
            }}}}}}}}}}}}}}}

        }
      }
      l = l.getTailConcCstBlock();
    }


    if(last != null) {
      res =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make(last,tom_append_list_ConcCstBlock(res, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() )) ;
      last = null;
    }
    return res.reverse();
  }

  
  

  
  private static CstBQTermList simplifyCstBQTermList(CstBQTermList l) {
    { /* unamed block */{ /* unamed block */if ( (l instanceof tom.engine.adt.cst.types.CstBQTermList) ) {if ( (((( tom.engine.adt.cst.types.CstBQTermList )l) instanceof tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm) || ((( tom.engine.adt.cst.types.CstBQTermList )l) instanceof tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm)) ) { tom.engine.adt.cst.types.CstBQTermList  tomMatch340_end_4=(( tom.engine.adt.cst.types.CstBQTermList )l);do {{ /* unamed block */if (!( tomMatch340_end_4.isEmptyConcCstBQTerm() )) { tom.engine.adt.cst.types.CstBQTerm  tomMatch340_10= tomMatch340_end_4.getHeadConcCstBQTerm() ;if ( ((( tom.engine.adt.cst.types.CstBQTerm )tomMatch340_10) instanceof tom.engine.adt.cst.types.cstbqterm.Cst_ITL) ) { tom.engine.adt.cst.types.CstOptionList  tomMatch340_8= tomMatch340_10.getoptionList() ;if ( (((( tom.engine.adt.cst.types.CstOptionList )tomMatch340_8) instanceof tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption) || ((( tom.engine.adt.cst.types.CstOptionList )tomMatch340_8) instanceof tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption)) ) {if (!( tomMatch340_8.isEmptyConcCstOption() )) { tom.engine.adt.cst.types.CstOption  tomMatch340_25= tomMatch340_8.getHeadConcCstOption() ;if ( ((( tom.engine.adt.cst.types.CstOption )tomMatch340_25) instanceof tom.engine.adt.cst.types.cstoption.Cst_OriginTracking) ) { String  tom___name= tomMatch340_25.getfileName() ;if (  tomMatch340_8.getTailConcCstOption() .isEmptyConcCstOption() ) { tom.engine.adt.cst.types.CstBQTermList  tomMatch340_5= tomMatch340_end_4.getTailConcCstBQTerm() ;if (!( tomMatch340_5.isEmptyConcCstBQTerm() )) { tom.engine.adt.cst.types.CstBQTerm  tomMatch340_14= tomMatch340_5.getHeadConcCstBQTerm() ;if ( ((( tom.engine.adt.cst.types.CstBQTerm )tomMatch340_14) instanceof tom.engine.adt.cst.types.cstbqterm.Cst_ITL) ) { tom.engine.adt.cst.types.CstOptionList  tomMatch340_12= tomMatch340_14.getoptionList() ;if ( (((( tom.engine.adt.cst.types.CstOptionList )tomMatch340_12) instanceof tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption) || ((( tom.engine.adt.cst.types.CstOptionList )tomMatch340_12) instanceof tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption)) ) {if (!( tomMatch340_12.isEmptyConcCstOption() )) { tom.engine.adt.cst.types.CstOption  tomMatch340_32= tomMatch340_12.getHeadConcCstOption() ;if ( ((( tom.engine.adt.cst.types.CstOption )tomMatch340_32) instanceof tom.engine.adt.cst.types.cstoption.Cst_OriginTracking) ) {if ( tom___name.equals( tomMatch340_32.getfileName() ) ) {if (  tomMatch340_12.getTailConcCstOption() .isEmptyConcCstOption() ) {




        String s = mergeString( tomMatch340_10.getcode() , tomMatch340_14.getcode() , tomMatch340_25.getendLine() , tomMatch340_25.getendColumn() , tomMatch340_32.getstartLine() , tomMatch340_32.getstartColumn() );
        if(s != null) { /* unamed block */
          return simplifyCstBQTermList(tom_append_list_ConcCstBQTerm(tom_get_slice_ConcCstBQTerm((( tom.engine.adt.cst.types.CstBQTermList )l),tomMatch340_end_4, tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() ), tom.engine.adt.cst.types.cstbqtermlist.ConsConcCstBQTerm.make( tom.engine.adt.cst.types.cstbqterm.Cst_ITL.make( tom.engine.adt.cst.types.cstoptionlist.ConsConcCstOption.make( tom.engine.adt.cst.types.cstoption.Cst_OriginTracking.make(tom___name,  tomMatch340_25.getstartLine() ,  tomMatch340_25.getstartColumn() ,  tomMatch340_32.getendLine() ,  tomMatch340_32.getendColumn() ) , tom.engine.adt.cst.types.cstoptionlist.EmptyConcCstOption.make() ) , s) ,tom_append_list_ConcCstBQTerm( tomMatch340_5.getTailConcCstBQTerm() , tom.engine.adt.cst.types.cstbqtermlist.EmptyConcCstBQTerm.make() )) ));
        }}}}}}}}}}}}}}if ( tomMatch340_end_4.isEmptyConcCstBQTerm() ) {tomMatch340_end_4=(( tom.engine.adt.cst.types.CstBQTermList )l);} else {tomMatch340_end_4= tomMatch340_end_4.getTailConcCstBQTerm() ;}}} while(!( (tomMatch340_end_4==(( tom.engine.adt.cst.types.CstBQTermList )l)) ));}}}}



    return l;
  }

  
  private static String mergeString(String s1, String s2, int lmax1, int cmax1, int lmin2, int cmin2) {
    String newline = System.getProperty("line.separator");
    if(lmax1 < lmin2) {
      
      
    }
    while(lmax1 < lmin2) {
      s1 += newline;
      lmax1++;
      cmax1 = 1;
    }
    if(cmax1 < cmin2) {
      
      
    }
    while(cmax1 < cmin2) {
      s1 += " ";
      cmax1++;
    }
    s1 += s2;
    return s1;
  }


  
  
  private CstBlockList tomSplitter(CstOptionList optionList,String subject) {

    CstBlockList bl =  tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() ;
    String metaChar = "]%";
    String escapeChar = "@";

    
    subject = subject.replace(escapeChar+escapeChar,metaChar);
    
    String split[] = subject.split(escapeChar);
    boolean last = subject.endsWith(escapeChar);
    int numSeparator = split.length + 1 + (last ? 1 : 0);
    if(numSeparator%2==1) {
      
    }
    
    boolean metaMode = true;
    for(int i=0 ; i<split.length ; i++) {
      if(metaMode) {
        
        String code = getParserTool().metaEncodeCode(split[i].replace(metaChar,escapeChar));
        metaMode = false;
        

        bl =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make( tom.engine.adt.cst.types.cstblock.HOSTBLOCK.make(optionList, code) ,tom_append_list_ConcCstBlock(bl, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() )) ;
      } else {
        String code = "+"+split[i]+"+";
        
        metaMode = true;
        
        try {
          ANTLRInputStream tomInput = new ANTLRInputStream(code.toCharArray(), code.length());
          CstBlock block = parseStream(tomInput,"");
          
          bl =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make(block,tom_append_list_ConcCstBlock(bl, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() )) ;
        } catch (Exception e) {
          throw new TomRuntimeException("Exception catched in tomSplitter");
        }
      }
    }
    if(subject.endsWith(escapeChar)) {
      
      bl =  tom.engine.adt.cst.types.cstblocklist.ConsConcCstBlock.make( tom.engine.adt.cst.types.cstblock.HOSTBLOCK.make(optionList, "\"\"") ,tom_append_list_ConcCstBlock(bl, tom.engine.adt.cst.types.cstblocklist.EmptyConcCstBlock.make() )) ;
    }
    return bl.reverse();
  }
 

}
