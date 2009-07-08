/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2009, INRIA
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

import tom.library.sl.*;

public class PILFactory {

        private static   tom.engine.adt.tominstruction.types.InstructionList  tom_append_list_concInstruction( tom.engine.adt.tominstruction.types.InstructionList l1,  tom.engine.adt.tominstruction.types.InstructionList  l2) {     if( l1.isEmptyconcInstruction() ) {       return l2;     } else if( l2.isEmptyconcInstruction() ) {       return l1;     } else if(  l1.getTailconcInstruction() .isEmptyconcInstruction() ) {       return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,l2) ;     } else {       return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,tom_append_list_concInstruction( l1.getTailconcInstruction() ,l2)) ;     }   }   private static   tom.engine.adt.tominstruction.types.InstructionList  tom_get_slice_concInstruction( tom.engine.adt.tominstruction.types.InstructionList  begin,  tom.engine.adt.tominstruction.types.InstructionList  end, tom.engine.adt.tominstruction.types.InstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcInstruction()  ||  (end== tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( begin.getHeadconcInstruction() ,( tom.engine.adt.tominstruction.types.InstructionList )tom_get_slice_concInstruction( begin.getTailconcInstruction() ,end,tail)) ;   }      private static   tom.engine.adt.tomoption.types.OptionList  tom_append_list_concOption( tom.engine.adt.tomoption.types.OptionList l1,  tom.engine.adt.tomoption.types.OptionList  l2) {     if( l1.isEmptyconcOption() ) {       return l2;     } else if( l2.isEmptyconcOption() ) {       return l1;     } else if(  l1.getTailconcOption() .isEmptyconcOption() ) {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,l2) ;     } else {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,tom_append_list_concOption( l1.getTailconcOption() ,l2)) ;     }   }   private static   tom.engine.adt.tomoption.types.OptionList  tom_get_slice_concOption( tom.engine.adt.tomoption.types.OptionList  begin,  tom.engine.adt.tomoption.types.OptionList  end, tom.engine.adt.tomoption.types.OptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcOption()  ||  (end== tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( begin.getHeadconcOption() ,( tom.engine.adt.tomoption.types.OptionList )tom_get_slice_concOption( begin.getTailconcOption() ,end,tail)) ;   }          private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Sequence) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) );       } else {         return ( (tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Sequence(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin):new tom.library.sl.Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)) );   }       private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) ) );}   



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

  public TomTerm remove(TomTerm subject) {
    try {
      return tom_make_TopDown(tom_make_replaceRemove()).visitLight(subject);
    } catch(tom.library.sl.VisitFailure e) {
      System.out.println("strategy failed");
    }
    return subject;
  }

  public static class replaceRemove extends tom.library.sl.AbstractStrategyBasic {public replaceRemove() {super(( new tom.library.sl.Identity() ));}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomoption.types.OptionList  visit_OptionList( tom.engine.adt.tomoption.types.OptionList  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomoption.types.OptionList) ) {


 return  tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ; }}}return _visit_OptionList(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tomoption.types.Option  visit_Option( tom.engine.adt.tomoption.types.Option  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomoption.types.Option) ) {


 return  tom.engine.adt.tomoption.types.option.noOption.make() ; }}}return _visit_Option(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tomsignature.types.TargetLanguage  visit_TargetLanguage( tom.engine.adt.tomsignature.types.TargetLanguage  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomsignature.types.TargetLanguage) ) {



 return  tom.engine.adt.tomsignature.types.targetlanguage.noTL.make() ; }}}return _visit_TargetLanguage(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tomexpression.types.Expression  visit_Expression( tom.engine.adt.tomexpression.types.Expression  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.Cast) ) {



 return tom_make_TopDown(tom_make_replaceRemove()).visitLight( (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getSource() ); }}}{if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.Or) ) {if ( ( (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg2()  instanceof tom.engine.adt.tomexpression.types.expression.FalseTL) ) {
 return tom_make_TopDown(tom_make_replaceRemove()).visitLight( (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getArg1() ); }}}}}return _visit_Expression(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tomoption.types.Option  _visit_Option( tom.engine.adt.tomoption.types.Option  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomoption.types.Option )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public  tom.engine.adt.tomexpression.types.Expression  _visit_Expression( tom.engine.adt.tomexpression.types.Expression  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomexpression.types.Expression )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public  tom.engine.adt.tomoption.types.OptionList  _visit_OptionList( tom.engine.adt.tomoption.types.OptionList  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomoption.types.OptionList )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public  tom.engine.adt.tomsignature.types.TargetLanguage  _visit_TargetLanguage( tom.engine.adt.tomsignature.types.TargetLanguage  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomsignature.types.TargetLanguage )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomoption.types.Option) ) {return ((T)visit_Option((( tom.engine.adt.tomoption.types.Option )v),introspector));}if ( (v instanceof tom.engine.adt.tomexpression.types.Expression) ) {return ((T)visit_Expression((( tom.engine.adt.tomexpression.types.Expression )v),introspector));}if ( (v instanceof tom.engine.adt.tomoption.types.OptionList) ) {return ((T)visit_OptionList((( tom.engine.adt.tomoption.types.OptionList )v),introspector));}if ( (v instanceof tom.engine.adt.tomsignature.types.TargetLanguage) ) {return ((T)visit_TargetLanguage((( tom.engine.adt.tomsignature.types.TargetLanguage )v),introspector));}if (!((environment ==  null ))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_replaceRemove() { return new replaceRemove();}



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

  public String prettyPrint(tom.library.sl.Visitable subject) {
    {{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.CompiledMatch) ) {
 
        return prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getAutomataInst() ); 
      }}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.Let) ) {


        return "let " + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getVariable() ) + " = " + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getSource() ) + " in\n\t" + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getAstInstruction() ).replace("\n","\n\t");
      }}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.LetRef) ) {


        return "letRef " + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getVariable() ) + " = " + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getSource() ) + " in\n\t" + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getAstInstruction() ).replace("\n","\n\t");
      }}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.Assign) ) {


        return prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getVariable() ) + " := " + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getSource() );
      }}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.DoWhile) ) {


        return "do\n\t " + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getDoInst() ).replace("\n","\n\t") +"while "+ prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() );
      }}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.WhileDo) ) {


        return "while "+ prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() )+" do\n\t " + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getDoInst() ).replace("\n","\n\t");
      }}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {if ( ( (( tom.engine.adt.tominstruction.types.Instruction )subject).getFailureInst()  instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {



        return  "if " + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() ) + " then \n\t" + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getSuccesInst() ).replace("\n","\n\t"); 
      }}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {


        return "if " + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getCondition() ) + " then \n\t" + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getSuccesInst() ).replace("\n","\n\t") + "\n\telse " + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getFailureInst() ).replace("\n","\n\t")+"\n";
      }}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) { tom.engine.adt.tominstruction.types.InstructionList  tomMatch228NameNumber_freshVar_38= (( tom.engine.adt.tominstruction.types.Instruction )subject).getInstList() ;if ( ((tomMatch228NameNumber_freshVar_38 instanceof tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction) || (tomMatch228NameNumber_freshVar_38 instanceof tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction)) ) { tom.engine.adt.tominstruction.types.InstructionList  tomMatch228NameNumber_end_43=tomMatch228NameNumber_freshVar_38;do {{if (!( tomMatch228NameNumber_end_43.isEmptyconcInstruction() )) {if ( ( tomMatch228NameNumber_end_43.getHeadconcInstruction()  instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {


        return prettyPrint( tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make(tom_append_list_concInstruction(tom_get_slice_concInstruction(tomMatch228NameNumber_freshVar_38,tomMatch228NameNumber_end_43, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ),tom_append_list_concInstruction( tomMatch228NameNumber_end_43.getTailconcInstruction() , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ))) );
      }}if ( tomMatch228NameNumber_end_43.isEmptyconcInstruction() ) {tomMatch228NameNumber_end_43=tomMatch228NameNumber_freshVar_38;} else {tomMatch228NameNumber_end_43= tomMatch228NameNumber_end_43.getTailconcInstruction() ;}}} while(!( (tomMatch228NameNumber_end_43==tomMatch228NameNumber_freshVar_38) ));}}}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) {


        return prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getInstList() );
      }}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.UnamedBlock) ) {


        return prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getInstList() );
      }}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.NamedBlock) ) {


        return  (( tom.engine.adt.tominstruction.types.Instruction )subject).getBlockName() + " : " + prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getInstList() );
      }}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.TypedAction) ) {



        return "targetLanguageInstructions";
      }}}{if ( (subject instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )subject) instanceof tom.engine.adt.tominstruction.types.instruction.CompiledPattern) ) {

 
        return prettyPrint( (( tom.engine.adt.tominstruction.types.Instruction )subject).getAutomataInst() ); 
      }}}}{{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.TomTermToExpression) ) {





        return prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getAstTerm() );
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.IsSort) ) {


        return "isSort("+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getAstType() )+","+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() )+")";
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.IsFsym) ) {


        return "is_fun_sym(" + prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() ) + "," + prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ) + ")";
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Negation) ) {


        return "not " + prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getArg() );
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Or) ) {


        return "("+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getArg1() )+" || "+ prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getArg2() )+")"; 
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.IsEmptyList) ) {


        return "is_empty(" + prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() ) + ")";
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.EqualTerm) ) {


        return "equal(" + prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getKid1() ) + "," + prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getKid2() ) + ")";
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetSliceList) ) {


        return "getSliceList("+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() )+","+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableBeginAST() )+","+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableEndAST() )+"," + prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getTail() ) + ")";
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetHead) ) {


        return "getHead("+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() )+")";
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetTail) ) {


        return "getTail("+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() )+")";
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetSlot) ) {


        return "get_slot_"+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() )+"_"+ (( tom.engine.adt.tomexpression.types.Expression )subject).getSlotNameString() +"("+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() )+")";
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Conditional) ) {


        return prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getCond() )+"?"+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getThen() )+":"+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getElse() );
      }}}}{{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.ExpressionToTomTerm) ) {





        return prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getAstExpression() );
      }}}{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {


        return prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getAstName() );
      }}}{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {


        return prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getAstName() );
      }}}{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {


        return prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getNameList() ); 
      }}}}{{if ( (subject instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )subject) instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {




        return "t"+ TomBase.tomNumberListToString( (( tom.engine.adt.tomname.types.TomName )subject).getNumberList() );
      }}}{if ( (subject instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )subject) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

        return  (( tom.engine.adt.tomname.types.TomName )subject).getString() ;
      }}}}{{if ( (subject instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )subject) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TomType  tomMatch232NameNumber_freshVar_1= (( tom.engine.adt.tomtype.types.TomType )subject).getTomType() ;if ( (tomMatch232NameNumber_freshVar_1 instanceof tom.engine.adt.tomtype.types.tomtype.ASTTomType) ) {




 return  tomMatch232NameNumber_freshVar_1.getString() ; }}}}}{{if ( (subject instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )subject) instanceof tom.engine.adt.tomname.types.tomnumber.Position) ) {




        return "" +  (( tom.engine.adt.tomname.types.TomNumber )subject).getInteger() ;
      }}}{if ( (subject instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )subject) instanceof tom.engine.adt.tomname.types.tomnumber.NameNumber) ) {


        return prettyPrint( (( tom.engine.adt.tomname.types.TomNumber )subject).getAstName() );
      }}}{if ( (subject instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )subject) instanceof tom.engine.adt.tomname.types.tomnumber.ListNumber) ) {


        return "listNumber"+ (( tom.engine.adt.tomname.types.TomNumber )subject).getInteger() ;
      }}}{if ( (subject instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )subject) instanceof tom.engine.adt.tomname.types.tomnumber.Begin) ) {


        return "begin"+ (( tom.engine.adt.tomname.types.TomNumber )subject).getInteger() ;
      }}}{if ( (subject instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )subject) instanceof tom.engine.adt.tomname.types.tomnumber.End) ) {


        return "end"+ (( tom.engine.adt.tomname.types.TomNumber )subject).getInteger() ;
      }}}}



    if(subject instanceof InstructionList) {
      InstructionList list = (InstructionList)subject;
      if(list.isEmptyconcInstruction()) {
        return "";
      } else {
        return prettyPrint(list.getHeadconcInstruction()) + "\n" + prettyPrint(list.getTailconcInstruction());
      }
    }  else if(subject instanceof TomNumberList) {
      TomNumberList list = (TomNumberList)subject;
      if(list.isEmptyconcTomNumber()) {
        return "";
      } else {
        return prettyPrint(list.getTailconcTomNumber()) + prettyPrint(list.getTailconcTomNumber());
      }
    }
    return subject.toString();
  }

  public static class collectMatch extends tom.library.sl.AbstractStrategyBasic {private  java.util.Collection  c;public collectMatch( java.util.Collection  c) {super(( new tom.library.sl.Identity() ));this.c=c;}public  java.util.Collection  getc() {return c;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.CompiledMatch) ) {


        c.add((( tom.engine.adt.tominstruction.types.Instruction )tom__arg));
      }}}}return _visit_Instruction(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if (!((environment ==  null ))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_collectMatch( java.util.Collection  t0) { return new collectMatch(t0);}

 

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
