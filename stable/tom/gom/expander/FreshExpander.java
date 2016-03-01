/*
 * Gom
 *
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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
 * Antoine Reilles    e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.gom.expander;

import java.util.logging.Level;
import tom.gom.GomStreamManager;
import tom.gom.SymbolTable;
import tom.gom.adt.gom.types.*;
import tom.library.sl.*;
import java.util.ArrayList;
import java.util.Set;
import tom.gom.SymbolTable.*;
import tom.gom.exception.*;
import tom.gom.tools.GomEnvironment;

public class FreshExpander {

         private static   tom.gom.adt.gom.types.ArgList  tom_append_list_ConcArg( tom.gom.adt.gom.types.ArgList l1,  tom.gom.adt.gom.types.ArgList  l2) {     if( l1.isEmptyConcArg() ) {       return l2;     } else if( l2.isEmptyConcArg() ) {       return l1;     } else if(  l1.getTailConcArg() .isEmptyConcArg() ) {       return  tom.gom.adt.gom.types.arglist.ConsConcArg.make( l1.getHeadConcArg() ,l2) ;     } else {       return  tom.gom.adt.gom.types.arglist.ConsConcArg.make( l1.getHeadConcArg() ,tom_append_list_ConcArg( l1.getTailConcArg() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ArgList  tom_get_slice_ConcArg( tom.gom.adt.gom.types.ArgList  begin,  tom.gom.adt.gom.types.ArgList  end, tom.gom.adt.gom.types.ArgList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcArg()  ||  (end== tom.gom.adt.gom.types.arglist.EmptyConcArg.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.arglist.ConsConcArg.make( begin.getHeadConcArg() ,( tom.gom.adt.gom.types.ArgList )tom_get_slice_ConcArg( begin.getTailConcArg() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.AlternativeList  tom_append_list_ConcAlternative( tom.gom.adt.gom.types.AlternativeList l1,  tom.gom.adt.gom.types.AlternativeList  l2) {     if( l1.isEmptyConcAlternative() ) {       return l2;     } else if( l2.isEmptyConcAlternative() ) {       return l1;     } else if(  l1.getTailConcAlternative() .isEmptyConcAlternative() ) {       return  tom.gom.adt.gom.types.alternativelist.ConsConcAlternative.make( l1.getHeadConcAlternative() ,l2) ;     } else {       return  tom.gom.adt.gom.types.alternativelist.ConsConcAlternative.make( l1.getHeadConcAlternative() ,tom_append_list_ConcAlternative( l1.getTailConcAlternative() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.AlternativeList  tom_get_slice_ConcAlternative( tom.gom.adt.gom.types.AlternativeList  begin,  tom.gom.adt.gom.types.AlternativeList  end, tom.gom.adt.gom.types.AlternativeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcAlternative()  ||  (end== tom.gom.adt.gom.types.alternativelist.EmptyConcAlternative.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.alternativelist.ConsConcAlternative.make( begin.getHeadConcAlternative() ,( tom.gom.adt.gom.types.AlternativeList )tom_get_slice_ConcAlternative( begin.getTailConcAlternative() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.ProductionList  tom_append_list_ConcProduction( tom.gom.adt.gom.types.ProductionList l1,  tom.gom.adt.gom.types.ProductionList  l2) {     if( l1.isEmptyConcProduction() ) {       return l2;     } else if( l2.isEmptyConcProduction() ) {       return l1;     } else if(  l1.getTailConcProduction() .isEmptyConcProduction() ) {       return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( l1.getHeadConcProduction() ,l2) ;     } else {       return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( l1.getHeadConcProduction() ,tom_append_list_ConcProduction( l1.getTailConcProduction() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ProductionList  tom_get_slice_ConcProduction( tom.gom.adt.gom.types.ProductionList  begin,  tom.gom.adt.gom.types.ProductionList  end, tom.gom.adt.gom.types.ProductionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcProduction()  ||  (end== tom.gom.adt.gom.types.productionlist.EmptyConcProduction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( begin.getHeadConcProduction() ,( tom.gom.adt.gom.types.ProductionList )tom_get_slice_ConcProduction( begin.getTailConcProduction() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.Option  tom_append_list_OptionList( tom.gom.adt.gom.types.Option  l1,  tom.gom.adt.gom.types.Option  l2) {     if( l1.isEmptyOptionList() ) {       return l2;     } else if( l2.isEmptyOptionList() ) {       return l1;     } else if( ((l1 instanceof tom.gom.adt.gom.types.option.ConsOptionList) || (l1 instanceof tom.gom.adt.gom.types.option.EmptyOptionList)) ) {       if(  l1.getTailOptionList() .isEmptyOptionList() ) {         return  tom.gom.adt.gom.types.option.ConsOptionList.make( l1.getHeadOptionList() ,l2) ;       } else {         return  tom.gom.adt.gom.types.option.ConsOptionList.make( l1.getHeadOptionList() ,tom_append_list_OptionList( l1.getTailOptionList() ,l2)) ;       }     } else {       return  tom.gom.adt.gom.types.option.ConsOptionList.make(l1,l2) ;     }   }   private static   tom.gom.adt.gom.types.Option  tom_get_slice_OptionList( tom.gom.adt.gom.types.Option  begin,  tom.gom.adt.gom.types.Option  end, tom.gom.adt.gom.types.Option  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyOptionList()  ||  (end== tom.gom.adt.gom.types.option.EmptyOptionList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.option.ConsOptionList.make((( ((begin instanceof tom.gom.adt.gom.types.option.ConsOptionList) || (begin instanceof tom.gom.adt.gom.types.option.EmptyOptionList)) )? begin.getHeadOptionList() :begin),( tom.gom.adt.gom.types.Option )tom_get_slice_OptionList((( ((begin instanceof tom.gom.adt.gom.types.option.ConsOptionList) || (begin instanceof tom.gom.adt.gom.types.option.EmptyOptionList)) )? begin.getTailOptionList() : tom.gom.adt.gom.types.option.EmptyOptionList.make() ),end,tail)) ;   }      private static   tom.gom.adt.gom.types.FieldList  tom_append_list_ConcField( tom.gom.adt.gom.types.FieldList l1,  tom.gom.adt.gom.types.FieldList  l2) {     if( l1.isEmptyConcField() ) {       return l2;     } else if( l2.isEmptyConcField() ) {       return l1;     } else if(  l1.getTailConcField() .isEmptyConcField() ) {       return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( l1.getHeadConcField() ,l2) ;     } else {       return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( l1.getHeadConcField() ,tom_append_list_ConcField( l1.getTailConcField() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.FieldList  tom_get_slice_ConcField( tom.gom.adt.gom.types.FieldList  begin,  tom.gom.adt.gom.types.FieldList  end, tom.gom.adt.gom.types.FieldList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcField()  ||  (end== tom.gom.adt.gom.types.fieldlist.EmptyConcField.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( begin.getHeadConcField() ,( tom.gom.adt.gom.types.FieldList )tom_get_slice_ConcField( begin.getTailConcField() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.AtomList  tom_append_list_ConcAtom( tom.gom.adt.gom.types.AtomList l1,  tom.gom.adt.gom.types.AtomList  l2) {     if( l1.isEmptyConcAtom() ) {       return l2;     } else if( l2.isEmptyConcAtom() ) {       return l1;     } else if(  l1.getTailConcAtom() .isEmptyConcAtom() ) {       return  tom.gom.adt.gom.types.atomlist.ConsConcAtom.make( l1.getHeadConcAtom() ,l2) ;     } else {       return  tom.gom.adt.gom.types.atomlist.ConsConcAtom.make( l1.getHeadConcAtom() ,tom_append_list_ConcAtom( l1.getTailConcAtom() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.AtomList  tom_get_slice_ConcAtom( tom.gom.adt.gom.types.AtomList  begin,  tom.gom.adt.gom.types.AtomList  end, tom.gom.adt.gom.types.AtomList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcAtom()  ||  (end== tom.gom.adt.gom.types.atomlist.EmptyConcAtom.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.atomlist.ConsConcAtom.make( begin.getHeadConcAtom() ,( tom.gom.adt.gom.types.AtomList )tom_get_slice_ConcAtom( begin.getTailConcAtom() ,end,tail)) ;   }        @SuppressWarnings("unchecked") private static java.util.ArrayList concArrayListAppend(Object o, java.util.ArrayList l) {   java.util.ArrayList res = (java.util.ArrayList)l.clone();   res.add(o);   return res; }      private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ): null ),end,tail)) ;   }      private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) , tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ), null ) ) , null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ), null ) ) , null ) ) ) ));} private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return (  tom.library.sl.Choice.make(s, tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) )  );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.MuVar("_x") ), null ) ) , tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}   






  private SymbolTable st;
  private GomEnvironment gomEnvironment;

  public final static String SHOULD_NEVER_HAPPEN_MSG = "Should never happen";

  public FreshExpander(GomEnvironment gomEnvironment) {
    this.gomEnvironment = gomEnvironment;
    this.st = gomEnvironment.getSymbolTable();
  }

  public GomEnvironment getGomEnvironment() {
    return this.gomEnvironment;
  }

  public void setGomEnvironment(GomEnvironment gomEnvironment) {
    this.gomEnvironment = gomEnvironment;
  }

  public GomModuleList expand(GomModuleList m, String pack) {
    try {
      ArrayList list = new ArrayList();
      GomModuleList res =
         tom.library.sl.Sequence.make(tom_make_TopDown(tom_make_ExpandAtoms(list)), tom.library.sl.Sequence.make(tom_make_TopDown(tom_make_UpdateSpecialization(list)), null ) ) 

.visitLight(m);
      st.fill(res);
      res = addRawSortsAndConstructors(res);
      res = addAtomHooks(res);
      res = addSortHooks(res);
      res = addRawSortHooks(res);
      res = addConstructorHooks(res);
      res = addRawConstructorHooks(res);
      res = addMappingHooks(res);
      st.clear();
      st.fill(res);
      return res;
    } catch (VisitFailure e) {
      throw new GomRuntimeException(FreshExpander.SHOULD_NEVER_HAPPEN_MSG);
    } catch (SortException e) {
      e.printStackTrace();
      throw new GomRuntimeException(FreshExpander.SHOULD_NEVER_HAPPEN_MSG);
    } catch (ConstructorException e) {
      e.printStackTrace();
      throw new GomRuntimeException(FreshExpander.SHOULD_NEVER_HAPPEN_MSG);
    }
  }

  /* -- export, convert and alpha maps argument lists -- */

  private String alphamapArgList(String sort) {
    StringBuffer buf = new StringBuffer();
    for(String a: st.getAccessibleAtoms(sort)) {
      String aid = st.getFullSortClassName(a);
      if(st.isPatternType(sort)) {
        if (st.getBoundAtoms(sort).contains(a)) {
          buf.append(",tom.library.freshgom.AlphaMap<"+aid+"> "+a+"OuterMap");
          buf.append(",tom.library.freshgom.AlphaMap<"+aid+"> "+a+"InnerMap");
        } else {
          buf.append(",tom.library.freshgom.AlphaMap<"+aid+"> "+a+"Map");
        }
      } else {
        buf.append(",tom.library.freshgom.AlphaMap<"+aid+"> "+a+"Map");
      }
    }
    return buf.toString();
  }

  private String newAlphamapList(String sort) {
    StringBuffer buf = new StringBuffer();
    for(String a: st.getAccessibleAtoms(sort)) {
      String aid = st.getFullSortClassName(a);
      buf.append(",new tom.library.freshgom.AlphaMap<"+aid+">()");
      if(st.isPatternType(sort) && st.getBoundAtoms(sort).contains(a)) {
        buf.append(",new tom.library.freshgom.AlphaMap<"+aid+">()");
      }
    }
    return buf.toString();
  }

  private String exportmapArgList(String sort) {
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for(String a: st.getAccessibleAtoms(sort)) {
      if(!first) {
        buf.append(", ");
      } else {
        first = false;
      }
      String aid = st.getFullSortClassName(a);
      if(st.isPatternType(sort)) {
        if (st.getBoundAtoms(sort).contains(a)) {
          buf.append("tom.library.freshgom.ExportMap<"+aid+"> "+a+"OuterMap");
          buf.append(",tom.library.freshgom.ExportMap<"+aid+"> "+a+"InnerMap");
        } else {
          buf.append("tom.library.freshgom.ExportMap<"+aid+"> "+a+"Map");
        }
      } else {
        buf.append("tom.library.freshgom.ExportMap<"+aid+"> "+a+"Map");
      }
    }
    return buf.toString();
  }

  private String newExportmapList(String sort) {
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for(String a: st.getAccessibleAtoms(sort)) {
      if(!first) {
        buf.append(", ");
      } else {
        first = false;
      }
      String aid = st.getFullSortClassName(a);
      buf.append("new tom.library.freshgom.ExportMap<"+aid+">()");
      if(st.isPatternType(sort) && st.getBoundAtoms(sort).contains(a)) {
        buf.append(",new tom.library.freshgom.ExportMap<"+aid+">()");
      }
    }
    return buf.toString();
  }

  private String convertmapArgList(String sort) {
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for(String a: st.getAccessibleAtoms(sort)) {
      if(!first) {
        buf.append(", ");
      } else {
        first = false;
      }
      String aid = st.getFullSortClassName(a);
      if(st.isPatternType(sort)) {
        if (st.getBoundAtoms(sort).contains(a)) {
          buf.append("tom.library.freshgom.ConvertMap<"+aid+"> "+a+"OuterMap");
          buf.append(",tom.library.freshgom.ConvertMap<"+aid+"> "+a+"InnerMap");
        } else {
          buf.append("tom.library.freshgom.ConvertMap<"+aid+"> "+a+"Map");
        }
      } else {
        buf.append("tom.library.freshgom.ConvertMap<"+aid+"> "+a+"Map");
      }
    }
    return buf.toString();
  }

  private String newConvertmapList(String sort) {
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for(String a: st.getAccessibleAtoms(sort)) {
      if(!first) {
        buf.append(", ");
      } else {
        first = false;
      }
      String aid = st.getFullSortClassName(a);
      buf.append("new tom.library.freshgom.ConvertMap<"+aid+">()");
      if(st.isPatternType(sort) && st.getBoundAtoms(sort).contains(a)) {
        buf.append(",new tom.library.freshgom.ConvertMap<"+aid+">()");
      }
    }
    return buf.toString();
  }

  /* call to expression field inside expression type */
  private String alphaRecCall1(String sort) {
    StringBuffer buf = new StringBuffer();
    for(String a: st.getAccessibleAtoms(sort)) {
      buf.append(","+a+"Map");
    }
    return buf.toString();
  }

  /* call to pattern field inside expression type */
  private String alphaRecCall2(String sort) {
    StringBuffer buf = new StringBuffer();
    for(String a: st.getAccessibleAtoms(sort)) {
      buf.append(","+a+"Map");
      if(st.getBoundAtoms(sort).contains(a)) {
        buf.append(","+a+"InnerMap");
      }
    }
    return buf.toString();
  }

  /* call to inner field inside pattern type */
  private String alphaRecCall3(String tosort, String fromsort) {
    StringBuffer buf = new StringBuffer();
    for(String a: st.getAccessibleAtoms(tosort)) {
      if(st.getBoundAtoms(fromsort).contains(a)) {
        buf.append(","+a+"InnerMap");
      } else {
        buf.append(","+a+"Map");
      }
    }
    return buf.toString();
  }

  /* call to outer field inside pattern type */
  private String alphaRecCall4(String tosort, String fromsort) {
    StringBuffer buf = new StringBuffer();
    for(String a: st.getAccessibleAtoms(tosort)) {
      if(st.getBoundAtoms(fromsort).contains(a)) {
        buf.append(","+a+"OuterMap");
      } else {
        buf.append(","+a+"Map");
      }
    }
    return buf.toString();
  }

  /* call to neutral field inside pattern type */
  private String alphaRecCall5(String sort) {
    StringBuffer buf = new StringBuffer();
    for(String a: st.getAccessibleAtoms(sort)) {
      buf.append(","+a+"Map");
    }
    return buf.toString();
  }

  /* call to pattern field inside pattern type */
  private String alphaRecCall6(String tosort) {
    StringBuffer buf = new StringBuffer();
    for(String a: st.getAccessibleAtoms(tosort)) {
      // bound atoms have to be the same
      if(st.getBoundAtoms(tosort).contains(a)) {
        buf.append(","+a+"OuterMap");
        buf.append(","+a+"InnerMap");
      } else {
        buf.append(","+a+"Map");
      }
    }
    return buf.toString();
  }


  /* call to expression field inside expression type */
  private String recCall1(String sort) {
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for(String a: st.getAccessibleAtoms(sort)) {
      if(!first) {
        buf.append(",");
      } else {
        first = false;
      }
      buf.append(""+a+"Map");
    }
    return buf.toString();
  }

  /* call to pattern field inside expression type */
  private String recCall2(String sort) {
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for(String a: st.getAccessibleAtoms(sort)) {
      if(!first) {
        buf.append(",");
      } else {
        first = false;
      }
      buf.append(""+a+"Map");
      if(st.getBoundAtoms(sort).contains(a)) {
        buf.append(","+a+"InnerMap");
      }
    }
    return buf.toString();
  }

  /* call to inner field inside pattern type */
  private String recCall3(String tosort, String fromsort) {
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for(String a: st.getAccessibleAtoms(tosort)) {
      if(!first) {
        buf.append(",");
      } else {
        first = false;
      }
      if(st.getBoundAtoms(fromsort).contains(a)) {
        buf.append(""+a+"InnerMap");
      } else {
        buf.append(""+a+"Map");
      }
    }
    return buf.toString();
  }

  /* call to outer field inside pattern type */
  private String recCall4(String tosort, String fromsort) {
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for(String a: st.getAccessibleAtoms(tosort)) {
      if(!first) {
        buf.append(",");
      } else {
        first = false;
      }
      if(st.getBoundAtoms(fromsort).contains(a)) {
        buf.append(""+a+"OuterMap");
      } else {
        buf.append(""+a+"Map");
      }
    }
    return buf.toString();
  }

  /* call to neutral field inside pattern type */
  private String recCall5(String sort) {
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for(String a: st.getAccessibleAtoms(sort)) {
      if(!first) {
        buf.append(",");
      } else {
        first = false;
      }
      buf.append(""+a+"Map");
    }
    return buf.toString();
  }

  /* call to pattern field inside pattern type */
  private String recCall6(String tosort) {
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for(String a: st.getAccessibleAtoms(tosort)) {
      if(!first) {
        buf.append(",");
      } else {
        first = false;
      }
      // bound atoms have to be the same
      if(st.getBoundAtoms(tosort).contains(a)) {
        buf.append(""+a+"OuterMap");
        buf.append(","+a+"InnerMap");
      } else buf.append(""+a+"Map");
    }
    return buf.toString();
  }

  /* hashTables for 'refresh' methods" */
  private String hashtableArgList(String sort) {
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for(String a: st.getBoundAtoms(sort)) {
      if(!first) {
        buf.append(", ");
      } else {
        first = false;
      }
      String aid = st.getFullSortClassName(a);
      buf.append( "java.util.Hashtable<"+aid+","+aid+"> "+a+"Map" );
    }
    return buf.toString();
  }

  /* hashTables for 'refresh' methods" */
  private String newHashtableList(String sort) {
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for(String a: st.getBoundAtoms(sort)) {
      if(!first) {
        buf.append(", ");
      } else {
        first = false;
      }
      String aid = st.getFullSortClassName(a);
      buf.append("new java.util.Hashtable<"+aid+","+aid+">()");
    }
    return buf.toString();
  }

  /* generates "A1Map,A2Map,...,AnMap" */
  private String hashtableRecursiveCall(String sort) {
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for(String a: st.getBoundAtoms(sort)) {
      if(!first) {
        buf.append(", ");
      } else {
        first = false;
      }
      buf.append(""+a+"Map");
    }
    return buf.toString();
  }

  /* generates "raw_f1,raw_f2,...,raw_fn" for fields of cons
   except for builtins where no new is added */
  private String rawArgList(String cons) {
    String sort = st.getSort(cons);
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for(String f: st.getFields(cons)) {
      if(!first) {
        buf.append(",");
      } else {
        first = false;
      }
      buf.append(gomEnvironment.isBuiltin(st.getSort(cons,f))? "get"+f+"()":"raw_"+f+"");
    }
    return buf.toString();
  }

  /* generates "f1,f2,...,fn" for fields of cons
   except for builtins where no new is added */
  private String convertArgList(String cons) {
    String sort = st.getSort(cons);
    StringBuffer buf = new StringBuffer();
    boolean first = true;
    for(String f: st.getFields(cons)) {
      if(!first) {
        buf.append(",");
      } else {
        first = false;
      }
      buf.append(gomEnvironment.isBuiltin(st.getSort(cons,f))? st.rawGetter(cons,f) :f);
    }
    return buf.toString();
  }


  /* -- hooks utilities -- */

  public static class AddHook extends tom.library.sl.AbstractStrategyBasic {private  String  sort;private  tom.gom.adt.gom.types.Production  hook;public AddHook( String  sort,  tom.gom.adt.gom.types.Production  hook) {super(( new tom.library.sl.Fail() ));this.sort=sort;this.hook=hook;}public  String  getsort() {return sort;}public  tom.gom.adt.gom.types.Production  gethook() {return hook;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.gom.adt.gom.types.ProductionList) ) {return ((T)visit_ProductionList((( tom.gom.adt.gom.types.ProductionList )v),introspector));}if (!(  null ==environment )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.gom.types.ProductionList  _visit_ProductionList( tom.gom.adt.gom.types.ProductionList  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(  null ==environment )) {return (( tom.gom.adt.gom.types.ProductionList )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.gom.types.ProductionList  visit_ProductionList( tom.gom.adt.gom.types.ProductionList  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.gom.adt.gom.types.ProductionList) ) {if ( (((( tom.gom.adt.gom.types.ProductionList )(( tom.gom.adt.gom.types.ProductionList )tom__arg)) instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) || ((( tom.gom.adt.gom.types.ProductionList )(( tom.gom.adt.gom.types.ProductionList )tom__arg)) instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction)) ) { tom.gom.adt.gom.types.ProductionList  tomMatch633_end_4=(( tom.gom.adt.gom.types.ProductionList )tom__arg);do {{if (!( tomMatch633_end_4.isEmptyConcProduction() )) { tom.gom.adt.gom.types.Production  tomMatch633_8= tomMatch633_end_4.getHeadConcProduction() ;if ( (tomMatch633_8 instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )tomMatch633_8) instanceof tom.gom.adt.gom.types.production.SortType) ) { tom.gom.adt.gom.types.GomType  tomMatch633_7= tomMatch633_8.getType() ;if ( (tomMatch633_7 instanceof tom.gom.adt.gom.types.GomType) ) {if ( ((( tom.gom.adt.gom.types.GomType )tomMatch633_7) instanceof tom.gom.adt.gom.types.gomtype.GomType) ) {


        if( tomMatch633_7.getName() .equals(sort)) {
          return tom_append_list_ConcProduction(tom_get_slice_ConcProduction((( tom.gom.adt.gom.types.ProductionList )tom__arg),tomMatch633_end_4, tom.gom.adt.gom.types.productionlist.EmptyConcProduction.make() ), tom.gom.adt.gom.types.productionlist.ConsConcProduction.make( tomMatch633_end_4.getHeadConcProduction() , tom.gom.adt.gom.types.productionlist.ConsConcProduction.make(hook,tom_append_list_ConcProduction( tomMatch633_end_4.getTailConcProduction() , tom.gom.adt.gom.types.productionlist.EmptyConcProduction.make() )) ) );
        }
      }}}}}if ( tomMatch633_end_4.isEmptyConcProduction() ) {tomMatch633_end_4=(( tom.gom.adt.gom.types.ProductionList )tom__arg);} else {tomMatch633_end_4= tomMatch633_end_4.getTailConcProduction() ;}}} while(!( (tomMatch633_end_4==(( tom.gom.adt.gom.types.ProductionList )tom__arg)) ));}}}}return _visit_ProductionList(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_AddHook( String  t0,  tom.gom.adt.gom.types.Production  t1) { return new AddHook(t0,t1);}



  /* add the hook next to sort declaration */
  private GomModuleList addSortBlockHook
    (GomModuleList ml, String sort, String code) {
      Production hook =
         tom.gom.adt.gom.types.production.Hook.make( tom.gom.adt.gom.types.idkind.KindSort.make() , sort,  tom.gom.adt.gom.types.hookkind.HookKind.make("javablock") ,  tom.gom.adt.gom.types.arglist.EmptyConcArg.make() , code,  tom.gom.adt.gom.types.option.EmptyOptionList.make() ) 
;
      try {
        return tom_make_OnceTopDown(tom_make_AddHook(sort,hook)).visitLight(ml);
      }
      catch (VisitFailure e) {
        throw new GomRuntimeException(FreshExpander.SHOULD_NEVER_HAPPEN_MSG);
      }
    }

  /* add the hook next to the declaration of the constructor's sort */
  private GomModuleList addConstructorBlockHook
    (GomModuleList ml, String cons, String code) {
      String sort = st.getSort(cons);
      IdKind kind =  tom.gom.adt.gom.types.idkind.KindOperator.make() ;
      // if the constructor is a generated cons or nil, add a 'future' hook
      if(st.isGeneratedCons(cons)) {
        kind =  tom.gom.adt.gom.types.idkind.KindFutureOperator.make( tom.gom.adt.gom.types.future.FutureCons.make() ) ;
        cons = st.getBaseName(cons);
      } else if (st.isGeneratedNil(cons)) {
        kind =  tom.gom.adt.gom.types.idkind.KindFutureOperator.make( tom.gom.adt.gom.types.future.FutureNil.make() ) ;
        cons = st.getBaseName(cons);
      }
      Production hook =  tom.gom.adt.gom.types.production.Hook.make(kind, cons,  tom.gom.adt.gom.types.hookkind.HookKind.make("javablock") ,  tom.gom.adt.gom.types.arglist.EmptyConcArg.make() , code,  tom.gom.adt.gom.types.option.EmptyOptionList.make() ) 
;
      try {
        return tom_make_OnceTopDown(tom_make_AddHook(sort,hook)).visitLight(ml);
      } catch (VisitFailure e) {
        throw new GomRuntimeException(FreshExpander.SHOULD_NEVER_HAPPEN_MSG);
      }
    }

  /* add the hook next to the declaration of the constructor's sort */
  private GomModuleList addRawConstructorBlockHook
    (GomModuleList ml, String cons, String code) {
      String sort = st.getSort(cons);
      IdKind kind =  tom.gom.adt.gom.types.idkind.KindOperator.make() ;
      // if the constructor is a generated cons or nil, add a 'future' hook
      if(st.isGeneratedCons(cons)) {
        kind =  tom.gom.adt.gom.types.idkind.KindFutureOperator.make( tom.gom.adt.gom.types.future.FutureCons.make() ) ;
        cons = st.getBaseName(cons);
      } else if (st.isGeneratedNil(cons)) {
        kind =  tom.gom.adt.gom.types.idkind.KindFutureOperator.make( tom.gom.adt.gom.types.future.FutureNil.make() ) ;
        cons = st.getBaseName(cons);
      }
      Production hook =
         tom.gom.adt.gom.types.production.Hook.make(kind, st.rawCons(cons),  tom.gom.adt.gom.types.hookkind.HookKind.make("javablock") ,  tom.gom.adt.gom.types.arglist.EmptyConcArg.make() , code,  tom.gom.adt.gom.types.option.EmptyOptionList.make() ) 
;
      try {
        return tom_make_OnceTopDown(tom_make_AddHook(st.rawCons(sort),hook)).visitLight(ml);
      }
      catch (VisitFailure e) {
        throw new GomRuntimeException(FreshExpander.SHOULD_NEVER_HAPPEN_MSG);
      }
    }

  /* add the hook next to the sort declaration */
  private GomModuleList addSortInterfaceHook
    (GomModuleList ml, String sort, String code) {
      Production hook =
         tom.gom.adt.gom.types.production.Hook.make( tom.gom.adt.gom.types.idkind.KindSort.make() , sort,  tom.gom.adt.gom.types.hookkind.HookKind.make("interface") ,  tom.gom.adt.gom.types.arglist.EmptyConcArg.make() , code,  tom.gom.adt.gom.types.option.EmptyOptionList.make() ) 
;
      try {
        return tom_make_OnceTopDown(tom_make_AddHook(sort,hook)).visitLight(ml);
      }
      catch (VisitFailure e) {
        throw new GomRuntimeException(FreshExpander.SHOULD_NEVER_HAPPEN_MSG);
      }
    }

  /* add the hook next to the declaration of the constructor's sort */
  private GomModuleList addConstructorMappingHook
    (GomModuleList ml, String cons, String code) {
      String sort = st.getSort(cons);
      IdKind kind =  tom.gom.adt.gom.types.idkind.KindOperator.make() ;
      // if the constructor is a generated cons or nil, add a 'future' hook
      if(st.isGeneratedCons(cons)) {
        kind =  tom.gom.adt.gom.types.idkind.KindFutureOperator.make( tom.gom.adt.gom.types.future.FutureCons.make() ) ;
        cons = st.getBaseName(cons);
      } else if (st.isGeneratedNil(cons)) {
        kind =  tom.gom.adt.gom.types.idkind.KindFutureOperator.make( tom.gom.adt.gom.types.future.FutureNil.make() ) ;
        cons = st.getBaseName(cons);
      }
      Production hook =  tom.gom.adt.gom.types.production.Hook.make(kind, cons,  tom.gom.adt.gom.types.hookkind.HookKind.make("mapping") ,  tom.gom.adt.gom.types.arglist.EmptyConcArg.make() , code,  tom.gom.adt.gom.types.option.EmptyOptionList.make() ) 
;
      try {
        return tom_make_OnceTopDown(tom_make_AddHook(sort,hook)).visitLight(ml);
      } catch (VisitFailure e) {
        throw new GomRuntimeException(FreshExpander.SHOULD_NEVER_HAPPEN_MSG);
      }
    }

  /* -- sort hooks -- */

  private GomModuleList addSortHooks(GomModuleList ml) {
    for(String s: st.getSorts()) {
      if(st.isExpressionType(s) || st.isPatternType(s)) {
        ml = addSortBlockHook(ml,s,sortBlockHookString(s));
      }
    }
    return ml;
  }


  private String sortBlockHookString(String sort) {
    String alphamapargs = alphamapArgList(sort);
    String newalphamaps = newAlphamapList(sort);
    String exportmapargs = exportmapArgList(sort);
    String newexportmaps = newExportmapList(sort);
    String rawsortid = st.qualifiedRawSortId(sort);
    String sortid = st.getFullSortClassName(sort);
    String res = "{";
    for(String a: st.getAccessibleAtoms(sort)) {
      String aid = st.getFullSortClassName(a);
      res += "\n        public abstract "+sortid+"\n          rename"+a+"(java.util.Hashtable<"+aid+","+aid+"> map);\n      "


;
    }

    res += "\n     /**\n      * alpha equivalence\n      */\n      public boolean equals("+sortid+" o) {\n        try {\n          alpha(o "+newalphamaps+");\n          return true;\n        } catch (tom.library.freshgom.AlphaMap.AlphaException e) {\n          return false;\n        }\n      }\n\n      public abstract void alpha ("+sortid+" o "+alphamapargs+")\n        throws tom.library.freshgom.AlphaMap.AlphaException;\n\n     /**\n      * exportation (term -> raw term)\n      */\n      public "+rawsortid+" export() {\n        return _export("+newexportmaps+");\n      }\n\n      public abstract "+rawsortid+" _export("+exportmapargs+");\n    "























;

    if(st.isPatternType(sort)) {
      String newhashtables = newHashtableList(sort);
      String hashtableargs = hashtableArgList(sort);
      res += "\n        public "+sortid+" refresh() {\n          return _refresh("+newhashtables+");\n        }\n\n        public abstract "+sortid+" _refresh("+hashtableargs+");\n      "





;

      for(String a: st.getBoundAtoms(sort)) {
        String aid = st.getFullSortClassName(a);
        res += "\n          public java.util.Set<"+aid+"> getBound"+a+"() {\n            java.util.HashSet<"+aid+"> res = new java.util.HashSet<"+aid+">();\n            getBound"+a+"(res);\n            return res;\n          }\n\n          public abstract void getBound"+a+"(java.util.Set<"+aid+"> atoms);\n\n          public abstract void getBound"+a+"2(\n              "+sortid+" o, tom.library.freshgom.AlphaMap<"+aid+"> m)\n              throws tom.library.freshgom.AlphaMap.AlphaException;\n\n        "












;
      }
    }

    return res + "}";
  }

  /* -- raw sort hooks -- */

  private GomModuleList addRawSortHooks(GomModuleList ml) {
    for(String s: st.getSorts()) {
      if(st.isExpressionType(s) || st.isPatternType(s)) {
        ml = addSortBlockHook(ml,st.rawSort(s),rawSortBlockHookString(s));
      }
    }
    return ml;
  }


  private String rawSortBlockHookString(String sort) {
    String convertmapargs = convertmapArgList(sort);
    String newconvertmaps = newConvertmapList(sort);
    String sortid = st.getFullSortClassName(sort);

    String res = "{\n     /**\n      * importation (raw term -> term)\n      */\n      public "+sortid+" convert() {\n        return _convert("+newconvertmaps+");\n      }\n\n      public abstract "+sortid+" _convert("+convertmapargs+");\n    "








;

    if(st.isPatternType(sort)) {
      for(String a: st.getBoundAtoms(sort)) {
        String aid = st.getFullSortClassName(a);
        res += "\n          public tom.library.freshgom.ConvertMap<"+aid+"> getBound"+a+"Map() {\n            tom.library.freshgom.ConvertMap<"+aid+"> res =\n              new tom.library.freshgom.ConvertMap<"+aid+">();\n            getBound"+a+"Map(res);\n            return res;\n          }\n\n          public abstract void\n            getBound"+a+"Map(tom.library.freshgom.ConvertMap<"+aid+"> m);\n        "









;
      }
    }
    return res + "}";
  }

  /* -- non variadic constructor hooks -- */

  private GomModuleList addConstructorHooks(GomModuleList ml) {
    for(String s: st.getSorts()) {
      if(st.isExpressionType(s) || st.isPatternType(s)) {
        for(String c: st.getConstructors(s)) {
          if(! st.isVariadic(c)) {
            ml = addConstructorBlockHook(ml,c,constructorBlockHookString(c));
          }
        }
      }
    }
    return ml;
  }

  private String renameRecursiveCalls(String c, String atomSort) {
    String sortid = st.getFullSortClassName(st.getSort(c));
    String res = ""+sortid+" res = this;";
    String atomSortId = st.getFullSortClassName(atomSort);
    for(String f: st.getFields(c)) {
      String fsort = st.getSort(c,f);
      if (gomEnvironment.isBuiltin(fsort)) {
        continue;
      }
      if (st.isAtomType(fsort)) {
        if (fsort.equals(atomSort)) {
          res += "\n            "+atomSortId+" n_"+f+" = map.get(get"+f+"());\n        if (n_"+f+" != null) res = res.set"+f+"(n_"+f+");\n        "


;
        }
      } else {
        if (!st.getAccessibleAtoms(fsort).contains(atomSort)) {continue;}
        res += " res = res.set"+f+"(get"+f+"().rename"+atomSort+"(map)); ";
      }
    }
    return res + "return res;";
  }

  private String refreshRecursiveCalls(String c) {
    String sort = st.getSort(c);
    String sortid = st.getFullSortClassName(sort);
    String res = ""+sortid+" res = this;";
    for(String f: st.getPatternFields(c)) {
      String fsort = st.getSort(c,f);
      if (gomEnvironment.isBuiltin(fsort)) {continue;}
      if (st.isAtomType(fsort)) {
        String fsortid = st.getFullSortClassName(fsort);
        res += "\n          "+fsortid+" "+f+" = get"+f+"();\n          if ("+fsort+"Map.containsKey("+f+"))\n            res = res.set"+f+"("+fsort+"Map.get("+f+"));\n          else {\n            "+fsortid+" fresh_"+f+" = "+fsortid+".fresh"+fsort+"("+f+");\n            "+fsort+"Map.put("+f+",fresh_"+f+");\n            res = res.set"+f+"(fresh_"+f+");\n          }\n        "








;
      } else {
        String arglist = hashtableRecursiveCall(sort);
        res += "res = res.set"+f+"(get"+f+"()._refresh("+arglist+"));";
      }
    }
    for(String f: st.getInnerFields(c)) {
      String fsort = st.getSort(c,f);
      if (gomEnvironment.isBuiltin(fsort)) {continue;}
      for(String a: st.getBoundAtoms(sort)) {
        if (st.getAccessibleAtoms(fsort).contains(a)) {
          res += "res = res.set"+f+"(res.get"+f+"().rename"+a+"("+a+"Map));";
        }
      }
    }
    return res + "return res;";
  }

  private String alphaRecursiveCalls(String c) {
    String sort = st.getSort(c);
    String sortid = st.getFullSortClassName(sort);
    String res = "";
    /* if c is a constructor in expression position
       the args are of the form atomsort1Map, atomsort2Map .. */
    if(st.isExpressionType(sort)) {
      for(String f: st.getFields(c)) {
        String fsort = st.getSort(c,f);
        if (gomEnvironment.isBuiltin(fsort)) {
          res += "\n            if (get"+f+"() != o.get"+f+"())\n              throw new tom.library.freshgom.AlphaMap.AlphaException();\n           "


;
        } else {
          String fsortid = st.getFullSortClassName(fsort);
          String rawfsortid = st.qualifiedRawSortId(fsort);
          if (st.isAtomType(fsort)) {
            res += "\n              if (!"+fsort+"Map.equal(get"+f+"(),o.get"+f+"()))\n                throw new tom.library.freshgom.AlphaMap.AlphaException();\n            "


;
          } else if (st.isExpressionType(fsort)) {
            res += "get"+f+"().alpha(o.get"+f+"() "+alphaRecCall1(fsort)+");";
          } else if (st.isPatternType(fsort)) {
            res += "\n              { /* to limit declared variables scope */\n              "+fsortid+" "+f+" = get"+f+"();;\n              "+fsortid+" o_"+f+" = o.get"+f+"();\n            "



;
            for(String a: st.getBoundAtoms(fsort)) {
              String aid = st.getFullSortClassName(a);
              res += "\n                tom.library.freshgom.AlphaMap<"+aid+"> "+f+"_bound"+a+"\n                  = new tom.library.freshgom.AlphaMap<"+aid+">();\n                "+f+".getBound"+a+"2(o_"+f+", "+f+"_bound"+a+");\n                tom.library.freshgom.AlphaMap<"+aid+"> "+a+"InnerMap\n                  = "+a+"Map.combine("+f+"_bound"+a+");\n              "





;
            }
            res += ""+f+".alpha(o.get"+f+"() "+alphaRecCall2(fsort)+");";
            res += "}";
          }
        }
      }
    /* if c is a constructor in pattern position --
       the args are of the form
        as1OuterMap, as1InnerMap, as2Map, as3OuterMap, as3InnerMap .. */
    } else if(st.isPatternType(sort)) {
      for(String f: st.getFields(c)) {
        String fsort = st.getSort(c,f);
        if (gomEnvironment.isBuiltin(fsort)) {
          res += "\n            if (get"+f+"() != o.get"+f+"())\n              throw new tom.library.freshgom.AlphaMap.AlphaException();\n          "


;
        } else {
          String fsortid = st.getFullSortClassName(fsort);
          String rawfsortid = st.qualifiedRawSortId(fsort);
          if (st.isAtomType(fsort)) {
            if (st.isBound(c,f)) {
              res += "\n                if (!"+fsort+"InnerMap.equal(get"+f+"(),o.get"+f+"()))\n                  throw new tom.library.freshgom.AlphaMap.AlphaException();\n            "


;
            } else {
                  res += "\n                    if (!"+fsort+"Map.equal(get"+f+"(),o.get"+f+"()))\n                      throw new tom.library.freshgom.AlphaMap.AlphaException();\n            "


;
            }
          } else if (st.isInner(c,f)) /* must be expression type */ {
            res += "get"+f+"().alpha(o.get"+f+"() "+alphaRecCall3(fsort,sort)+");";
          } else if (st.isOuter(c,f)) /* must be expression type */ {
            res += "get"+f+"().alpha(o.get"+f+"() "+alphaRecCall4(fsort,sort)+");";
          } else if (st.isNeutral(c,f)) /* must be expression type */ {
            res += "get"+f+"().alpha(o.get"+f+"() "+alphaRecCall5(fsort)+");";
          } else /* must be pattern type */ {
            res += "get"+f+"().alpha(o.get"+f+"() "+alphaRecCall6(fsort)+");";
          }
        }
      }
    }
    return res;
  }

  private String exportRecursiveCalls(String c) {
    String sort = st.getSort(c);
    String sortid = st.getFullSortClassName(sort);
    String res = "";
    /* if c is a constructor in expression position
       the args are of the form atomsort1Map, atomsort2Map .. */
    if(st.isExpressionType(sort)) {
      for(String f: st.getFields(c)) {
        String fsort = st.getSort(c,f);
        if (gomEnvironment.isBuiltin(fsort)) {continue;}
        String fsortid = st.getFullSortClassName(fsort);
        String rawfsortid = st.qualifiedRawSortId(fsort);
        if (st.isAtomType(fsort)) {
          res += "String raw_"+f+" = "+fsort+"Map.get(get"+f+"());";
        } else if (st.isExpressionType(fsort)) {
          res += ""+rawfsortid+" raw_"+f+"\n            = get"+f+"()._export("+recCall1(fsort)+");"
;
        } else if (st.isPatternType(fsort)) {
          res += "\n            /* declaration before scoping */\n            "+rawfsortid+" raw_"+f+" = null; "

;
          res += "{ /* to limit declared variables scope */";
          res += ""+fsortid+" "+f+" = get"+f+"();";
          for(String a: st.getBoundAtoms(fsort)) {
            String aid = st.getFullSortClassName(a);
            res += "\n              java.util.Set<"+aid+"> "+f+"_bound"+a+" = "+f+".getBound"+a+"();\n              tom.library.freshgom.ExportMap<"+aid+"> "+a+"InnerMap\n                = "+a+"Map.addSet("+f+"_bound"+a+");\n            "



;
          }
          res += "raw_"+f+" = "+f+"._export("+recCall2(fsort)+");";
          res += "}";
        }
      }
    /* if c is a constructor in pattern position --
       the args are of the form
        as1OuterMap, as1InnerMap, as2Map, as3OuterMap, as3InnerMap .. */
    } else if(st.isPatternType(sort)) {
      for(String f: st.getFields(c)) {
        String fsort = st.getSort(c,f);
        if (gomEnvironment.isBuiltin(fsort)) {continue;}
        String fsortid = st.getFullSortClassName(fsort);
        String rawfsortid = st.qualifiedRawSortId(fsort);
        if (st.isAtomType(fsort)) {
          if (st.isBound(c,f)) {
            res += "String raw_"+f+" = "+fsort+"InnerMap.get(get"+f+"());";
          } else {
            res += "String raw_"+f+" = "+fsort+"Map.get(get"+f+"());";
          }
        } else if (st.isInner(c,f)) /* must be expression type */ {
          res += ""+rawfsortid+" raw_"+f+"\n            = get"+f+"()._export("+recCall3(fsort,sort)+");"
;
        } else if (st.isOuter(c,f)) /* must be expression type */ {
          res += ""+rawfsortid+" raw_"+f+"\n            = get"+f+"()._export("+recCall4(fsort,sort)+");"
;
        } else if (st.isNeutral(c,f)) /* must be expression type */ {
          res += ""+rawfsortid+" raw_"+f+"\n            = get"+f+"()._export("+recCall5(fsort)+");"
;
        } else /* must be pattern type */ {
          res += ""+rawfsortid+" raw_"+f+"\n            = get"+f+"()._export("+recCall6(fsort)+");"
;
        }
      }
    }
    return res + "return "+st.qualifiedRawConstructorId(c)+".make("+rawArgList(c)+");";
  }

  private String getBoundRecursiveCalls(String c, String atomSort) {
    String sort = st.getSort(c);
    String sortid = st.getFullSortClassName(sort);
    String res = "";
    for(String f: st.getPatternFields(c)) {
      String fsort = st.getSort(c,f);
      if (gomEnvironment.isBuiltin(fsort)) {continue;}
      if (st.isAtomType(fsort)) {
        if (fsort.equals(atomSort)) {
          res += "atoms.add(get"+f+"());";
        }
      } else if (st.getBoundAtoms(fsort).contains(atomSort)) {
        res += "get"+f+"().getBound"+atomSort+"(atoms);";
      }
    }
    return res;
  }

  private String getBound2RecursiveCalls(String c, String a) {
    String sort = st.getSort(c);
    String sortid = st.getFullSortClassName(sort);
    String aid = st.getFullSortClassName(a);
    String res = "";
    for(String f: st.getPatternFields(c)) {
      String fsort = st.getSort(c,f);
      if (gomEnvironment.isBuiltin(fsort)) {continue;}
      if (st.isAtomType(fsort)) {
        if (fsort.equals(a)) {
          res += "\n            "+aid+" "+f+" = get"+f+"();\n            m.put("+f+",o.get"+f+"(),"+aid+".fresh"+a+"("+f+"));\n          "


;
        }
      } else if (st.getBoundAtoms(fsort).contains(a)) {
        res += "get"+f+"().getBound"+a+"2(o.get"+f+"(),m);";
      }
    }
    return res;
  }

  private String constructorBlockHookString(String c) {
    String cid = st.getFullConstructorClassName(c);
    String sort = st.getSort(c);
    String alphamapargs = alphamapArgList(sort);
    String exportmapargs = exportmapArgList(sort);
    String rawsortid = st.qualifiedRawSortId(sort);
    String sortid = st.getFullSortClassName(sort);
    String res = "{";
    for(String a: st.getAccessibleAtoms(sort)) {
      String aid = st.getFullSortClassName(a);
      res += "\n        public "+sortid+" rename"+a+"(java.util.Hashtable<"+aid+","+aid+"> map) {\n          "+renameRecursiveCalls(c,a)+"\n        }"


;
    }

    res += "\n      /**\n       * alpha equivalence\n       */\n      public void alpha ("+sortid+" o "+alphamapargs+")\n        throws tom.library.freshgom.AlphaMap.AlphaException {\n          if (! (o instanceof "+cid+"))\n            throw new tom.library.freshgom.AlphaMap.AlphaException();\n          "+alphaRecursiveCalls(c)+"\n        };\n\n    /**\n     * exportation (term -> raw term)\n     */\n    public "+rawsortid+" _export("+exportmapargs+") {\n      "+exportRecursiveCalls(c)+"\n    }\n    "
















;

    if(st.isPatternType(sort)) {
      res += "\n        public "+sortid+" _refresh("+hashtableArgList(sort)+") {\n          "+refreshRecursiveCalls(c)+"\n        }\n      "



;

      for(String a: st.getBoundAtoms(sort)) {
        String aid = st.getFullSortClassName(a);
        res += "\n          public void getBound"+a+"(java.util.Set<"+aid+"> atoms) {\n            "+getBoundRecursiveCalls(c,a)+"\n          }\n\n          public void getBound"+a+"2(\n              "+sortid+" o, tom.library.freshgom.AlphaMap<"+aid+"> m)\n              throws tom.library.freshgom.AlphaMap.AlphaException {\n                if (! (o instanceof "+cid+"))\n                  throw new tom.library.freshgom.AlphaMap.AlphaException();\n                "+getBound2RecursiveCalls(c,a)+"\n              }\n\n        "












;
      }
    }

    return res + "}";
  }

  /* -- non variadic raw constructor hooks -- */

  private GomModuleList addRawConstructorHooks(GomModuleList ml) {
    for(String s: st.getSorts()) {
      if(st.isExpressionType(s) || st.isPatternType(s)) {
        for(String c: st.getConstructors(s)) {
          if(! st.isVariadic(c)) {
            ml = addRawConstructorBlockHook(
                ml,c,rawConstructorBlockHookString(c));
          }
        }
      }
    }
    return ml;
  }

  private String getBoundMapRecursiveCalls(String c, String atomSort) {
    String sort = st.getSort(c);
    String sortid = st.getFullSortClassName(sort);
    String aid = st.getFullSortClassName(atomSort);
    String res = "";
    for(String f: st.getPatternFields(c)) {
      String fsort = st.getSort(c,f);
      if (gomEnvironment.isBuiltin(fsort)) {continue;}
      if (st.isAtomType(fsort)) {
        if (fsort.equals(atomSort)) {
          res += "\n            String "+f+" = "+st.rawGetter(c,f)+";\n            m.put("+f+","+aid+".fresh"+atomSort+"("+f+"));\n          "


;
        }
      } else if (st.getBoundAtoms(fsort).contains(atomSort)) {
        res += ""+st.rawGetter(c,f)+".getBound"+atomSort+"Map(m);";
      }
    }
    return res;
  }

  private String convertRecursiveCalls(String c) {
    String sort = st.getSort(c);
    String sortid = st.getFullSortClassName(sort);
    String opid = st.getFullConstructorClassName(c);
    String res = "";
    /* if c is a constructor in expression position
       the args are of the form atomsort1Map, atomsort2Map .. */
    if(st.isExpressionType(sort)) {
      for(String f: st.getFields(c)) {
        String fsort = st.getSort(c,f);
        if (gomEnvironment.isBuiltin(fsort)) {continue;}
        String fsortid = st.getFullSortClassName(fsort);
        String rawfsortid = st.qualifiedRawSortId(fsort);
        if (st.isAtomType(fsort)) {
          res += ""+fsortid+" "+f+" = "+fsort+"Map.get("+st.rawGetter(c,f)+");";
        } else if (st.isExpressionType(fsort)) {
          res += ""+fsortid+" "+f+"\n            = "+st.rawGetter(c,f)+"._convert("+recCall1(fsort)+");"
;
        } else if (st.isPatternType(fsort)) {
          res += "\n            /* declaration before scoping */\n            "+fsortid+" "+f+" = null;\n          "


;
          res += "{ /* to limit declared variables scope */";
          res += ""+rawfsortid+" raw_"+f+" = "+st.rawGetter(c,f)+";";
          for(String a: st.getBoundAtoms(fsort)) {
            String aid = st.getFullSortClassName(a);
            res += "\n              tom.library.freshgom.ConvertMap<"+aid+"> raw_"+f+"_bound"+a+"\n                = raw_"+f+".getBound"+a+"Map();\n              tom.library.freshgom.ConvertMap<"+aid+"> "+a+"InnerMap\n                = "+a+"Map.combine(raw_"+f+"_bound"+a+");\n            "




;
          }
          res += ""+f+" = raw_"+f+"._convert("+recCall2(fsort)+");";
          res += "}";
        }
      }
    /* if c is a constructor in pattern position --
       the args are of the form
        as1OuterMap, as1InnerMap, as2Map, as3OuterMap, as3InnerMap .. */
    } else if(st.isPatternType(sort)) {
      for(String f: st.getFields(c)) {
        String fsort = st.getSort(c,f);
        if (gomEnvironment.isBuiltin(fsort)) {continue;}
        String fsortid = st.getFullSortClassName(fsort);
        if (st.isAtomType(fsort)) {
          if (st.isBound(c,f)) {
            res += ""+fsortid+" "+f+" = "+fsort+"InnerMap.get("+st.rawGetter(c,f)+");";
          } else {
            res += ""+fsortid+" "+f+" = "+fsort+"Map.get("+st.rawGetter(c,f)+");";
          }
        } else if (st.isInner(c,f)) /* must be expression type */ {
          res += ""+fsortid+" "+f+"\n            = "+st.rawGetter(c,f)+"._convert("+recCall3(fsort,sort)+");"
;
        } else if (st.isOuter(c,f)) /* must be expression type */ {
          res += ""+fsortid+" "+f+"\n            = "+st.rawGetter(c,f)+"._convert("+recCall4(fsort,sort)+");"
;
        } else if (st.isNeutral(c,f)) /* must be expression type */ {
          res += ""+fsortid+" "+f+"\n            = "+st.rawGetter(c,f)+"._convert("+recCall5(fsort)+");"
;
        } else /* must be pattern type */ {
          res += ""+fsortid+" "+f+"\n            = "+st.rawGetter(c,f)+"._convert("+recCall6(fsort)+");"
;
        }
      }
    }
    return res + "return "+opid+".make("+convertArgList(c)+");";
  }

  private String rawConstructorBlockHookString(String c) {
    String sort = st.getSort(c);
    String convertmapargs = convertmapArgList(sort);
    String sortid = st.getFullSortClassName(sort);

    String res = "{\n      /**\n       * importation (raw term -> term)\n       */\n      public "+sortid+" _convert("+convertmapargs+") {\n        "+convertRecursiveCalls(c)+"\n      }\n    "






;

    if(st.isPatternType(sort)) {
      for(String a: st.getBoundAtoms(sort)) {
        String aid = st.getFullSortClassName(a);
        res += "\n          public void\n            getBound"+a+"Map(tom.library.freshgom.ConvertMap<"+aid+"> m) {\n              "+getBoundMapRecursiveCalls(c,a)+";\n            }\n        "




;
      }
    }

    return res + "}";
  }

  /* -- atom hooks -- */

  private GomModuleList addAtomHooks(GomModuleList ml) {
    for(String s: st.getAtoms()) {
      ml = addSortBlockHook(ml,s,atomBlockHookString(s));
      ml = addSortInterfaceHook(ml,s,atomInterfaceHookString);
    }
    return ml;
  }

  private String atomBlockHookString(String sort) {
    String sortid = st.getFullSortClassName(sort);
    return "{\n      private static int counter = 0;\n\n      public static "+sortid+"\n        fresh"+sort+"("+sortid+" hint) {\n          return fresh"+sort+"(hint.gethint());\n        }\n\n      public static "+sortid+"\n        fresh"+sort+"(String hint) {\n          return "+st.getFullConstructorClassName(sort)+".make(++counter,hint.split(\"[0-9]\")[0]);\n        }\n\n      public boolean equals("+sort+" o) {\n        return this.getn() == o.getn();\n      }\n\n      public String getRepresentation(int n) {\n        return gethint() + (n==0 ? \"\" : n);\n      }\n    }"



















;
  }

  private static final String atomInterfaceHookString
    = "{ tom.library.freshgom.Atom }";

  /* -- rawification -- */

  private GomModuleList addRawSortsAndConstructors(GomModuleList res) {
    try { return tom_make_TopDown(tom_make_AddRaw(st,this)).visitLight(res); }
    catch(VisitFailure e) {
      throw new GomRuntimeException(FreshExpander.SHOULD_NEVER_HAPPEN_MSG);
    }
  }

  public static class AddRaw extends tom.library.sl.AbstractStrategyBasic {private  tom.gom.SymbolTable  st;private  tom.gom.expander.FreshExpander  fe;public AddRaw( tom.gom.SymbolTable  st,  tom.gom.expander.FreshExpander  fe) {super(( new tom.library.sl.Identity() ));this.st=st;this.fe=fe;}public  tom.gom.SymbolTable  getst() {return st;}public  tom.gom.expander.FreshExpander  getfe() {return fe;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.gom.adt.gom.types.Section) ) {return ((T)visit_Section((( tom.gom.adt.gom.types.Section )v),introspector));}if (!(  null ==environment )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.gom.types.Section  _visit_Section( tom.gom.adt.gom.types.Section  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(  null ==environment )) {return (( tom.gom.adt.gom.types.Section )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.gom.types.Section  visit_Section( tom.gom.adt.gom.types.Section  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.gom.adt.gom.types.Section) ) {if ( ((( tom.gom.adt.gom.types.Section )tom__arg) instanceof tom.gom.adt.gom.types.Section) ) {if ( ((( tom.gom.adt.gom.types.Section )(( tom.gom.adt.gom.types.Section )tom__arg)) instanceof tom.gom.adt.gom.types.section.Public) ) {


        return (( tom.gom.adt.gom.types.Section )tom__arg).setProductionList(fe.addRaw(st, (( tom.gom.adt.gom.types.Section )tom__arg).getProductionList() ));
      }}}}}return _visit_Section(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_AddRaw( tom.gom.SymbolTable  t0,  tom.gom.expander.FreshExpander  t1) { return new AddRaw(t0,t1);}



  /**
   * match pl with
   *  | p::ps -> if fresh p && (not atom p)
   *             then p::(rawify p)::(addraw pl)
   *             else p::(addraw pl)
   *  | [] -> []
   **/
  private ProductionList addRaw(SymbolTable st, ProductionList pl) {
    {{if ( (pl instanceof tom.gom.adt.gom.types.ProductionList) ) {if ( ((( tom.gom.adt.gom.types.ProductionList )pl) instanceof tom.gom.adt.gom.types.ProductionList) ) {if ( ((( tom.gom.adt.gom.types.ProductionList )(( tom.gom.adt.gom.types.ProductionList )pl)) instanceof tom.gom.adt.gom.types.productionlist.ConsConcProduction) ) { tom.gom.adt.gom.types.Production  tom_p= (( tom.gom.adt.gom.types.ProductionList )pl).getHeadConcProduction() ;

        ProductionList nps = addRaw(st, (( tom.gom.adt.gom.types.ProductionList )pl).getTailConcProduction() );
        {{if ( (tom_p instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )tom_p) instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )(( tom.gom.adt.gom.types.Production )tom_p)) instanceof tom.gom.adt.gom.types.production.SortType) ) { tom.gom.adt.gom.types.GomType  tomMatch636_1= (( tom.gom.adt.gom.types.Production )tom_p).getType() ;if ( (tomMatch636_1 instanceof tom.gom.adt.gom.types.GomType) ) {if ( ((( tom.gom.adt.gom.types.GomType )tomMatch636_1) instanceof tom.gom.adt.gom.types.gomtype.GomType) ) { String  tom_n= tomMatch636_1.getName() ;

            if (st.isFreshType(tom_n) && !st.isAtomType(tom_n)) {
              return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make(tom_p, tom.gom.adt.gom.types.productionlist.ConsConcProduction.make(rawify(tom_p,st),tom_append_list_ConcProduction(nps, tom.gom.adt.gom.types.productionlist.EmptyConcProduction.make() )) ) ;
            }
          }}}}}}}

        return  tom.gom.adt.gom.types.productionlist.ConsConcProduction.make(tom_p,tom_append_list_ConcProduction(nps, tom.gom.adt.gom.types.productionlist.EmptyConcProduction.make() )) ;
      }}}}{if ( (pl instanceof tom.gom.adt.gom.types.ProductionList) ) {if ( ((( tom.gom.adt.gom.types.ProductionList )pl) instanceof tom.gom.adt.gom.types.ProductionList) ) {if ( ((( tom.gom.adt.gom.types.ProductionList )(( tom.gom.adt.gom.types.ProductionList )pl)) instanceof tom.gom.adt.gom.types.productionlist.EmptyConcProduction) ) {
 return (( tom.gom.adt.gom.types.ProductionList )pl); }}}}}

    throw new GomRuntimeException("Non exhaustive patterns");
  }

  public static class Rawify extends tom.library.sl.AbstractStrategyBasic {private  tom.gom.SymbolTable  st;private  tom.gom.expander.FreshExpander  fe;public Rawify( tom.gom.SymbolTable  st,  tom.gom.expander.FreshExpander  fe) {super(( new tom.library.sl.Identity() ));this.st=st;this.fe=fe;}public  tom.gom.SymbolTable  getst() {return st;}public  tom.gom.expander.FreshExpander  getfe() {return fe;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.gom.adt.gom.types.Alternative) ) {return ((T)visit_Alternative((( tom.gom.adt.gom.types.Alternative )v),introspector));}if ( (v instanceof tom.gom.adt.gom.types.GomType) ) {return ((T)visit_GomType((( tom.gom.adt.gom.types.GomType )v),introspector));}if (!(  null ==environment )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.gom.types.GomType  _visit_GomType( tom.gom.adt.gom.types.GomType  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(  null ==environment )) {return (( tom.gom.adt.gom.types.GomType )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.gom.types.Alternative  _visit_Alternative( tom.gom.adt.gom.types.Alternative  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(  null ==environment )) {return (( tom.gom.adt.gom.types.Alternative )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.gom.types.Alternative  visit_Alternative( tom.gom.adt.gom.types.Alternative  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.gom.adt.gom.types.Alternative) ) {if ( ((( tom.gom.adt.gom.types.Alternative )tom__arg) instanceof tom.gom.adt.gom.types.Alternative) ) {if ( ((( tom.gom.adt.gom.types.Alternative )(( tom.gom.adt.gom.types.Alternative )tom__arg)) instanceof tom.gom.adt.gom.types.alternative.Alternative) ) {







 return (( tom.gom.adt.gom.types.Alternative )tom__arg).setName(st.rawCons( (( tom.gom.adt.gom.types.Alternative )tom__arg).getName() )); }}}}}return _visit_Alternative(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.gom.adt.gom.types.GomType  visit_GomType( tom.gom.adt.gom.types.GomType  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.gom.adt.gom.types.GomType) ) {if ( ((( tom.gom.adt.gom.types.GomType )tom__arg) instanceof tom.gom.adt.gom.types.GomType) ) {if ( ((( tom.gom.adt.gom.types.GomType )(( tom.gom.adt.gom.types.GomType )tom__arg)) instanceof tom.gom.adt.gom.types.gomtype.GomType) ) { String  tom_n= (( tom.gom.adt.gom.types.GomType )tom__arg).getName() ; tom.gom.adt.gom.types.GomType  tom_gt=(( tom.gom.adt.gom.types.GomType )tom__arg);         if (st.isAtomType(tom_n)) { return tom_gt.setName("String"); }         else if (!fe.getGomEnvironment().isBuiltin(tom_n)) { return tom_gt.setName(st.rawSort(tom_n)); }       }}}}}return _visit_GomType(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_Rawify( tom.gom.SymbolTable  t0,  tom.gom.expander.FreshExpander  t1) { return new Rawify(t0,t1);}



  private Production rawify(Production p, SymbolTable st) {
    try { return tom_make_TopDown(tom_make_Rawify(st,this)).visitLight(p); }
    catch(VisitFailure f) {
      throw new GomRuntimeException(FreshExpander.SHOULD_NEVER_HAPPEN_MSG);
    }
  }

  /* -- tweaked mappings generation */

  private GomModuleList addMappingHooks(GomModuleList ml) {
    for(String c: st.getFreshConstructors()) {
      if(st.containsRefreshPoint(c)) {
        ml = addConstructorMappingHook(ml,c,mappingString(c));
      }
    }
    return ml;
  }


  /**
  * generates "x:A,y:B" for f(x:A,y:B)
  **/
  private String typedConsArgList(String c) {
    StringBuffer buf = new StringBuffer();
    boolean first=true;
    for(String arg: st.getFields(c)) {
      if (!first) {
        buf.append(",");
      } else {
        first = false;
      }
      buf.append(arg+":"+st.getSort(c,arg));
    }
    return buf.toString();
  }

  /**
  * generates "x,y" for f(x:A,y:B)
  **/
  private String consArgList(String c) {
    StringBuffer buf = new StringBuffer();
    boolean first=true;
    for(String arg: st.getFields(c)) {
      if (!first) {
        buf.append(",");
      } else {
        first = false;
      }
      buf.append(arg);
    }
    return buf.toString();
  }

  /**
  * generates "$x,$y" for f(x:A,y:B)
  **/
  private String dollarConsArgList(String c) {
    StringBuffer buf = new StringBuffer();
    boolean first=true;
    for(String arg: st.getFields(c)) {
      if (!first) {
        buf.append(",");
      } else {
        first = false;
      }
      buf.append("$" + arg);
    }
    return buf.toString();
  }

  /**
  * generates "get_slot(x,t) { $t.getx() }
  *            get_slot(y,t) { $t.gety() }"
  * for f(x:A,y:B)
  **/
  private String slotDescriptions(String c) {
    StringBuffer buf = new StringBuffer();
    boolean first=true;
    for(String arg: st.getFields(c)) {
      if(st.isRefreshPoint(c,arg)) {
        buf.append("get_slot("+arg+",t) { $t.get"+arg+"().refresh() }" + "\n");
      } else {
        buf.append("get_slot("+arg+",t) { $t.get"+arg+"() }" + "\n");
      }
    }
    return buf.toString();
  }

  private String mappingString(String c) {
    String s = st.getSort(c);
    if (st.isVariadic(c)) {
      String codomain = st.getCoDomain(c);
      String domain = st.getDomain(c);
      String consid = st.getFullConstructorClassName("Cons" + c);
      String nilid = st.getFullConstructorClassName("Empty" + c);
      String gethead = null;
      if(st.isRefreshPoint(c)) {
        gethead = "getHead"+c+"().refresh()";
      } else {
        gethead = "getHead"+c+"()";
      }
      return "{\n        %oplist "+codomain+" "+c+"("+domain+"*) {\n          is_fsym(t) { (($t instanceof "+consid+") || ($t instanceof "+nilid+")) }\n          make_empty() { "+nilid+".make() }\n          make_insert(e,l) { "+consid+".make($e,$l) }\n          get_head(l) { $l."+gethead+" }\n          get_tail(l) { $l.getTail"+c+"() }\n          is_empty(l) { $l.isEmpty"+c+"() }\n        }\n      }"








;
    } else {
      String cid = st.getFullConstructorClassName(c);
      return "{\n        %op "+s+" "+c+"("+typedConsArgList(c)+") {\n          is_fsym(t) { ($t instanceof "+cid+") }\n          "+slotDescriptions(c)+"\n          make("+consArgList(c)+") { "+cid+".make("+dollarConsArgList(c)+") }\n       }\n      }"





;
    }
  }

  /* -- atom expansion --**/

  public static class ExpandAtoms extends tom.library.sl.AbstractStrategyBasic {private  java.util.ArrayList  list;public ExpandAtoms( java.util.ArrayList  list) {super(( new tom.library.sl.Identity() ));this.list=list;}public  java.util.ArrayList  getlist() {return list;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.gom.adt.gom.types.Production) ) {return ((T)visit_Production((( tom.gom.adt.gom.types.Production )v),introspector));}if (!(  null ==environment )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.gom.types.Production  _visit_Production( tom.gom.adt.gom.types.Production  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(  null ==environment )) {return (( tom.gom.adt.gom.types.Production )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.gom.types.Production  visit_Production( tom.gom.adt.gom.types.Production  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )tom__arg) instanceof tom.gom.adt.gom.types.Production) ) {if ( ((( tom.gom.adt.gom.types.Production )(( tom.gom.adt.gom.types.Production )tom__arg)) instanceof tom.gom.adt.gom.types.production.AtomDecl) ) { String  tom_name= (( tom.gom.adt.gom.types.Production )tom__arg).getName() ;


        list.add(tom_name);
        return  tom.gom.adt.gom.types.production.SortType.make( tom.gom.adt.gom.types.gomtype.GomType.make( tom.gom.adt.gom.types.typespec.AtomType.make() , tom_name) ,  tom.gom.adt.gom.types.atomlist.EmptyConcAtom.make() ,  tom.gom.adt.gom.types.alternativelist.ConsConcAlternative.make( tom.gom.adt.gom.types.alternative.Alternative.make(tom_name,  tom.gom.adt.gom.types.fieldlist.ConsConcField.make( tom.gom.adt.gom.types.field.NamedField.make("n",  tom.gom.adt.gom.types.gomtype.GomType.make( tom.gom.adt.gom.types.typespec.ExpressionType.make() , "int") ,  tom.gom.adt.gom.types.scopespecifier.None.make() ) , tom.gom.adt.gom.types.fieldlist.ConsConcField.make( tom.gom.adt.gom.types.field.NamedField.make("hint",  tom.gom.adt.gom.types.gomtype.GomType.make( tom.gom.adt.gom.types.typespec.ExpressionType.make() , "String") ,  tom.gom.adt.gom.types.scopespecifier.None.make() ) , tom.gom.adt.gom.types.fieldlist.EmptyConcField.make() ) ) ,  tom.gom.adt.gom.types.gomtype.GomType.make( tom.gom.adt.gom.types.typespec.AtomType.make() , tom_name) ,  tom.gom.adt.gom.types.option.Origin.make(-1) ) , tom.gom.adt.gom.types.alternativelist.EmptyConcAlternative.make() ) ) 







;
      }}}}}return _visit_Production(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_ExpandAtoms( java.util.ArrayList  t0) { return new ExpandAtoms(t0);}



  /* -- update expressiontype -> atomtype for atom sorts --**/

  public static class UpdateSpecialization extends tom.library.sl.AbstractStrategyBasic {private  java.util.ArrayList  list;public UpdateSpecialization( java.util.ArrayList  list) {super(( new tom.library.sl.Identity() ));this.list=list;}public  java.util.ArrayList  getlist() {return list;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.gom.adt.gom.types.GomType) ) {return ((T)visit_GomType((( tom.gom.adt.gom.types.GomType )v),introspector));}if (!(  null ==environment )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.gom.types.GomType  _visit_GomType( tom.gom.adt.gom.types.GomType  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(  null ==environment )) {return (( tom.gom.adt.gom.types.GomType )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.gom.types.GomType  visit_GomType( tom.gom.adt.gom.types.GomType  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.gom.adt.gom.types.GomType) ) {if ( ((( tom.gom.adt.gom.types.GomType )tom__arg) instanceof tom.gom.adt.gom.types.GomType) ) {if ( ((( tom.gom.adt.gom.types.GomType )(( tom.gom.adt.gom.types.GomType )tom__arg)) instanceof tom.gom.adt.gom.types.gomtype.GomType) ) {


        if (list.contains( (( tom.gom.adt.gom.types.GomType )tom__arg).getName() )) {
          return (( tom.gom.adt.gom.types.GomType )tom__arg).setSpecialization( tom.gom.adt.gom.types.typespec.AtomType.make() );
        }
      }}}}}return _visit_GomType(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_UpdateSpecialization( java.util.ArrayList  t0) { return new UpdateSpecialization(t0);}


}
