/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2017, Inria
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
 * **/

package tom.engine.tools;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import tom.engine.TomBase;
import tom.engine.adt.tomsignature.*;
import tom.engine.tools.TomGenericPlugin;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.code.types.*;

import tom.library.sl.*;

public class PILFactory {

        private static   tom.engine.adt.code.types.BQTermList  tom_append_list_concBQTerm( tom.engine.adt.code.types.BQTermList l1,  tom.engine.adt.code.types.BQTermList  l2) {     if( l1.isEmptyconcBQTerm() ) {       return l2;     } else if( l2.isEmptyconcBQTerm() ) {       return l1;     } else if(  l1.getTailconcBQTerm() .isEmptyconcBQTerm() ) {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,tom_append_list_concBQTerm( l1.getTailconcBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTermList  tom_get_slice_concBQTerm( tom.engine.adt.code.types.BQTermList  begin,  tom.engine.adt.code.types.BQTermList  end, tom.engine.adt.code.types.BQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcBQTerm()  ||  (end== tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( begin.getHeadconcBQTerm() ,( tom.engine.adt.code.types.BQTermList )tom_get_slice_concBQTerm( begin.getTailconcBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.tominstruction.types.InstructionList  tom_append_list_concInstruction( tom.engine.adt.tominstruction.types.InstructionList l1,  tom.engine.adt.tominstruction.types.InstructionList  l2) {     if( l1.isEmptyconcInstruction() ) {       return l2;     } else if( l2.isEmptyconcInstruction() ) {       return l1;     } else if(  l1.getTailconcInstruction() .isEmptyconcInstruction() ) {       return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,l2) ;     } else {       return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,tom_append_list_concInstruction( l1.getTailconcInstruction() ,l2)) ;     }   }   private static   tom.engine.adt.tominstruction.types.InstructionList  tom_get_slice_concInstruction( tom.engine.adt.tominstruction.types.InstructionList  begin,  tom.engine.adt.tominstruction.types.InstructionList  end, tom.engine.adt.tominstruction.types.InstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcInstruction()  ||  (end== tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( begin.getHeadconcInstruction() ,( tom.engine.adt.tominstruction.types.InstructionList )tom_get_slice_concInstruction( begin.getTailconcInstruction() ,end,tail)) ;   }      private static   tom.engine.adt.tomoption.types.OptionList  tom_append_list_concOption( tom.engine.adt.tomoption.types.OptionList l1,  tom.engine.adt.tomoption.types.OptionList  l2) {     if( l1.isEmptyconcOption() ) {       return l2;     } else if( l2.isEmptyconcOption() ) {       return l1;     } else if(  l1.getTailconcOption() .isEmptyconcOption() ) {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,l2) ;     } else {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,tom_append_list_concOption( l1.getTailconcOption() ,l2)) ;     }   }   private static   tom.engine.adt.tomoption.types.OptionList  tom_get_slice_concOption( tom.engine.adt.tomoption.types.OptionList  begin,  tom.engine.adt.tomoption.types.OptionList  end, tom.engine.adt.tomoption.types.OptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcOption()  ||  (end== tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( begin.getHeadconcOption() ,( tom.engine.adt.tomoption.types.OptionList )tom_get_slice_concOption( begin.getTailconcOption() ,end,tail)) ;   }          private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ): null ),end,tail)) ;   }      private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) , tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ), null ) ) , null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ), null ) ) , null ) ) ) ));} private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return (  tom.library.sl.Choice.make(s, tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) )  );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.MuVar("_x") ), null ) ) , tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}   



  /**
   * level specifies the level of details of the output
   * 0 is identity
   * 1 removes options
   */
  private int level = 0;

  public PILFactory() {
    super();
    init(1);
  }

  void init (int level) {
    this.level = level;
  }

  public <T extends tom.library.sl.Visitable> T remove(T subject) {
    try {
      return tom_make_TopDown(tom_make_replaceRemove()).visitLight(subject);
    } catch(tom.library.sl.VisitFailure e) {
      System.out.println("strategy failed");
    }
    return subject;
  }

  public static class replaceRemove extends tom.library.sl.AbstractStrategyBasic {public replaceRemove() {super(( new tom.library.sl.Identity() ));}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomexpression.types.Expression) ) {return ((T)visit_Expression((( tom.engine.adt.tomexpression.types.Expression )v),introspector));}if ( (v instanceof tom.engine.adt.tomoption.types.Option) ) {return ((T)visit_Option((( tom.engine.adt.tomoption.types.Option )v),introspector));}if ( (v instanceof tom.engine.adt.code.types.TargetLanguage) ) {return ((T)visit_TargetLanguage((( tom.engine.adt.code.types.TargetLanguage )v),introspector));}if ( (v instanceof tom.engine.adt.tomoption.types.OptionList) ) {return ((T)visit_OptionList((( tom.engine.adt.tomoption.types.OptionList )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomoption.types.OptionList  _visit_OptionList( tom.engine.adt.tomoption.types.OptionList  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomoption.types.OptionList )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.TargetLanguage  _visit_TargetLanguage( tom.engine.adt.code.types.TargetLanguage  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.TargetLanguage )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomoption.types.Option  _visit_Option( tom.engine.adt.tomoption.types.Option  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomoption.types.Option )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomexpression.types.Expression  _visit_Expression( tom.engine.adt.tomexpression.types.Expression  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomexpression.types.Expression )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomexpression.types.Expression  visit_Expression( tom.engine.adt.tomexpression.types.Expression  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.Cast) ) {













 return tom_make_TopDown(tom_make_replaceRemove()).visitLight( (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getSource() ); }}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.Or) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression ) (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg2() ) instanceof tom.engine.adt.tomexpression.types.expression.FalseTL) ) {
 return tom_make_TopDown(tom_make_replaceRemove()).visitLight( (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg1() ); }}}}}return _visit_Expression(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.TargetLanguage  visit_TargetLanguage( tom.engine.adt.code.types.TargetLanguage  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.code.types.TargetLanguage) ) { return  tom.engine.adt.code.types.targetlanguage.noTL.make() ; }}}return _visit_TargetLanguage(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomoption.types.Option  visit_Option( tom.engine.adt.tomoption.types.Option  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomoption.types.Option) ) { return  tom.engine.adt.tomoption.types.option.noOption.make() ; }}}return _visit_Option(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomoption.types.OptionList  visit_OptionList( tom.engine.adt.tomoption.types.OptionList  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomoption.types.OptionList) ) { return  tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ; }}}return _visit_OptionList(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_replaceRemove() { return new replaceRemove();}



  public String prettyPrintCompiledMatch(tom.library.sl.Visitable subject) {
    StringBuilder res = new StringBuilder();
    Collection matches = collectMatch(subject);
    Iterator it = matches.iterator();
    while(it.hasNext()) {
      Instruction cm = (Instruction) it.next();
      res.append(prettyPrint(cm));
      res.append("\n");
    }
    return res.toString();
  }

  public String prettyPrint(Instruction subject) {
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.CompiledMatch) ) {
 
        return prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getAutomataInst() ); 
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.Let) ) {


        return "let " + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getVariable() ) + " = " + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getSource() ) + " in\n\t" + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getAstInstruction() ).replace("\n","\n\t");
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.LetRef) ) {


        return "letRef " + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getVariable() ) + " = " + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getSource() ) + " in\n\t" + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getAstInstruction() ).replace("\n","\n\t");
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.Assign) ) {


        return prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getVariable() ) + " := " + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getSource() ) ;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.AssignArray) ) {


        return "AssignArray " + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getVariable() ) + "["+prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getIndex() )+"] := " + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getSource() ) ;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.Assign) ) {


        return "Assign " + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getVariable() ) + " = " + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getSource() ) ;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.DoWhile) ) {



        return "do\n\t " + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getDoInst() ).replace("\n","\n\t") +"while "+ prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() );
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.WhileDo) ) {


        return "while "+ prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() )+" do\n\t " + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getDoInst() ).replace("\n","\n\t");
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction ) (( tom.engine.adt.tominstruction.types.Instruction )subject).getFailureInst() ) instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {



        return  "if " + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() ) + " then \n\t" + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getSuccesInst() ).replace("\n","\n\t"); 
      }}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {


        return "if " + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() ) + " then \n\t" + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getSuccesInst() ).replace("\n","\n\t") + "\n\telse " + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getFailureInst() ).replace("\n","\n\t")+"\n";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) { tom.engine.adt.tominstruction.types.InstructionList  tomMatch409_58= (( tom.engine.adt.tominstruction.types.Instruction )subject).getInstList() ;if ( (((( tom.engine.adt.tominstruction.types.InstructionList )tomMatch409_58) instanceof tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction) || ((( tom.engine.adt.tominstruction.types.InstructionList )tomMatch409_58) instanceof tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction)) ) { tom.engine.adt.tominstruction.types.InstructionList  tomMatch409_end_64=tomMatch409_58;do {{ /* unamed block */if (!( tomMatch409_end_64.isEmptyconcInstruction() )) {if ( ((( tom.engine.adt.tominstruction.types.Instruction ) tomMatch409_end_64.getHeadconcInstruction() ) instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {


        return prettyPrint( tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make(tom_append_list_concInstruction(tom_get_slice_concInstruction(tomMatch409_58,tomMatch409_end_64, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ),tom_append_list_concInstruction( tomMatch409_end_64.getTailconcInstruction() , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ))) );
      }}if ( tomMatch409_end_64.isEmptyconcInstruction() ) {tomMatch409_end_64=tomMatch409_58;} else {tomMatch409_end_64= tomMatch409_end_64.getTailconcInstruction() ;}}} while(!( (tomMatch409_end_64==tomMatch409_58) ));}}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) {


        return prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getInstList() );
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.UnamedBlock) ) {


        return prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getInstList() );
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.NamedBlock) ) {


        return  (( tom.engine.adt.tominstruction.types.Instruction )subject).getBlockName() + " : " + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getInstList() );
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.RawAction) ) {



        return "targetLanguageInstructions";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.CompiledPattern) ) {

 
        return prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getAutomataInst() ); 
      }}}}

    return subject.toString();
  }

  public String prettyPrint(Expression subject) {
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.BQTermToExpression) ) {

        return prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getAstTerm() );
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.IsSort) ) {


        return "isSort\n\t";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.IsFsym) ) {


        return "is_fun_sym(" + prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() ) + "," + prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ) + ")";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Negation) ) {


        return "not " + prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getArg() );
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.And) ) {


        return prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() ) + " && " + prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() ) ;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Or) ) {


        return prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() ) + " || " + prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() ) ;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.IsEmptyList) ) {


        return "is_empty(" + prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ) + ")";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.EqualTerm) ) {


        return prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getKid1() ) + "==" + prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getKid2() ) + ")";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetSliceList) ) {


        return "getSliceList("+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() )+","+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableBeginAST() )+","+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableEndAST() )+"," + prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getTail() ) + ")";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetHead) ) {


        return "getHead("+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() )+")";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetTail) ) {


        return "getTail("+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() )+")";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetSlot) ) {


        return "get_slot_"+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() )+"_"+ (( tom.engine.adt.tomexpression.types.Expression )subject).getSlotNameString() +"("+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() )+")";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetElement) ) {


        return prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() )+"["+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getIndex() )+"]";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetSize) ) {


        return "size("+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() )+")";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GreaterOrEqualThan) ) {


        return prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() )+" >= "+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() );
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GreaterThan) ) {

        return prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() )+" > "+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() );
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.LessOrEqualThan) ) {

        return prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() )+" <= "+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() );
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.LessThan) ) {

        return prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() )+" < "+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() );
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Integer) ) {

        return ""+ (( tom.engine.adt.tomexpression.types.Expression )subject).getvalue() ;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.AddOne) ) {

        return prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() )+"+1";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.SubstractOne) ) {

        return prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() )+"-1";
      }}}}

    return subject.toString();
  }


  public String prettyPrint(BQTerm subject) {
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) {

        return prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getExp() );
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.FunctionCall) ) { tom.engine.adt.code.types.BQTermList  tom___Args= (( tom.engine.adt.code.types.BQTerm )subject).getArgs() ;

        String s = "";
        int min=0;
        { /* unamed block */{ /* unamed block */if ( (tom___Args instanceof tom.engine.adt.code.types.BQTermList) ) {if ( (((( tom.engine.adt.code.types.BQTermList )tom___Args) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )tom___Args) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) { tom.engine.adt.code.types.BQTermList  tomMatch412_end_4=(( tom.engine.adt.code.types.BQTermList )tom___Args);do {{ /* unamed block */if (!( tomMatch412_end_4.isEmptyconcBQTerm() )) {

            s += ","+prettyPrint( tomMatch412_end_4.getHeadconcBQTerm() );
            min=1;
          }if ( tomMatch412_end_4.isEmptyconcBQTerm() ) {tomMatch412_end_4=(( tom.engine.adt.code.types.BQTermList )tom___Args);} else {tomMatch412_end_4= tomMatch412_end_4.getTailconcBQTerm() ;}}} while(!( (tomMatch412_end_4==(( tom.engine.adt.code.types.BQTermList )tom___Args)) ));}}}}

        return prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getAstName() )+"("+s.substring(min, s.length())+")";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyArray) ) {

        return "new "+prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getAstName() )+"["+prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getSize() )+"]";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {

        return prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getAstName() );
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {

        return prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getAstName() );
      }}}}

    return subject.toString();
  }

  public String prettyPrint(TomTerm subject) {
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {

        return prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getAstName() );
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {

        return prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getAstName() );
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {

        return prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getNameList() ); 
      }}}}

    return subject.toString();
  }

  public String prettyPrint(TomName subject) {
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )subject) instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {

        return "t"+ TomBase.tomNumberListToString( (( tom.engine.adt.tomname.types.TomName )subject).getNumberList() );
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )subject) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

        return  (( tom.engine.adt.tomname.types.TomName )subject).getString() ;
      }}}}

    return subject.toString();
  }

  public String prettyPrint(TomType subject) {
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )subject) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 return  (( tom.engine.adt.tomtype.types.TomType )subject).getTomType() ; }}}}

    return subject.toString();
  }

  public String prettyPrint(TomNumber subject) {
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )subject) instanceof tom.engine.adt.tomname.types.tomnumber.Position) ) {
 return "" +  (( tom.engine.adt.tomname.types.TomNumber )subject).getInteger() ; }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )subject) instanceof tom.engine.adt.tomname.types.tomnumber.NameNumber) ) {
 return prettyPrint( (( tom.engine.adt.tomname.types.TomNumber )subject).getAstName() ); }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )subject) instanceof tom.engine.adt.tomname.types.tomnumber.ListNumber) ) {
 return "listNumber"+ (( tom.engine.adt.tomname.types.TomNumber )subject).getInteger() ; }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )subject) instanceof tom.engine.adt.tomname.types.tomnumber.Begin) ) {
 return "begin"+ (( tom.engine.adt.tomname.types.TomNumber )subject).getInteger() ; }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )subject) instanceof tom.engine.adt.tomname.types.tomnumber.End) ) {
 return "end"+ (( tom.engine.adt.tomname.types.TomNumber )subject).getInteger() ; }}}}

    return subject.toString();
  }

  public String prettyPrint(Visitable subject) {
    if(subject instanceof InstructionList) {
      InstructionList list = (InstructionList)subject;
      if(list.isEmptyconcInstruction()) {
        return "";
      } else {
        return prettyPrint(list.getHeadconcInstruction()) + "\n" + prettyPrint(list.getTailconcInstruction());
      }
    }  else if (subject instanceof TomNumberList) {
      TomNumberList list = (TomNumberList)subject;
      if(list.isEmptyconcTomNumber()) {
        return "";
      } else {
        return prettyPrint(list.getTailconcTomNumber()) + prettyPrint(list.getTailconcTomNumber());
      }
    }
    return subject.toString();
  }

  public static class collectMatch extends tom.library.sl.AbstractStrategyBasic {private  java.util.Collection  c;public collectMatch( java.util.Collection  c) {super(( new tom.library.sl.Identity() ));this.c=c;}public  java.util.Collection  getc() {return c;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.CompiledMatch) ) {


        c.add((( tom.engine.adt.tominstruction.types.Instruction )tom__arg));
      }}}}return _visit_Instruction(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_collectMatch( java.util.Collection  t0) { return new collectMatch(t0);}

 

  public Collection collectMatch(tom.library.sl.Visitable subject) {
    Collection result = new HashSet();
    try {
      tom_make_TopDown(tom_make_collectMatch(result)).visitLight(subject);
    } catch(tom.library.sl.VisitFailure e) {
      System.out.println("strategy failed");
    }
    return result;
  }

}
