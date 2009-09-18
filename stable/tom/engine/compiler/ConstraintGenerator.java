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
 * Radu Kopetz e-mail: Radu.Kopetz@loria.fr
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/
package tom.engine.compiler;

import java.util.*;

import java.lang.reflect.*;

import tom.engine.TomBase;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomterm.types.tomterm.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomconstraint.types.constraint.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.tools.SymbolTable;
import tom.engine.compiler.*;
import tom.engine.compiler.generator.*;
import tom.engine.exception.TomRuntimeException;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.code.types.*;
import tom.library.sl.*;

/**
 * This class is in charge with launching all the generators,
 * until no more generations can be made 
 */
public class ConstraintGenerator {

//------------------------------------------------------------	
        private static   tom.engine.adt.tomexpression.types.Expression  tom_append_list_OrExpressionDisjunction( tom.engine.adt.tomexpression.types.Expression  l1,  tom.engine.adt.tomexpression.types.Expression  l2) {     if( l1.isEmptyOrExpressionDisjunction() ) {       return l2;     } else if( l2.isEmptyOrExpressionDisjunction() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction) || (l1 instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction)) ) {       if(  l1.getTailOrExpressionDisjunction() .isEmptyOrExpressionDisjunction() ) {         return  tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction.make( l1.getHeadOrExpressionDisjunction() ,l2) ;       } else {         return  tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction.make( l1.getHeadOrExpressionDisjunction() ,tom_append_list_OrExpressionDisjunction( l1.getTailOrExpressionDisjunction() ,l2)) ;       }     } else {       return  tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction.make(l1,l2) ;     }   }   private static   tom.engine.adt.tomexpression.types.Expression  tom_get_slice_OrExpressionDisjunction( tom.engine.adt.tomexpression.types.Expression  begin,  tom.engine.adt.tomexpression.types.Expression  end, tom.engine.adt.tomexpression.types.Expression  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyOrExpressionDisjunction()  ||  (end== tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction.make((( ((begin instanceof tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction) || (begin instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction)) )? begin.getHeadOrExpressionDisjunction() :begin),( tom.engine.adt.tomexpression.types.Expression )tom_get_slice_OrExpressionDisjunction((( ((begin instanceof tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction) || (begin instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction)) )? begin.getTailOrExpressionDisjunction() : tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction.make() ),end,tail)) ;   }      private static   tom.engine.adt.tomexpression.types.Expression  tom_append_list_OrConnector( tom.engine.adt.tomexpression.types.Expression  l1,  tom.engine.adt.tomexpression.types.Expression  l2) {     if( l1.isEmptyOrConnector() ) {       return l2;     } else if( l2.isEmptyOrConnector() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.tomexpression.types.expression.ConsOrConnector) || (l1 instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrConnector)) ) {       if(  l1.getTailOrConnector() .isEmptyOrConnector() ) {         return  tom.engine.adt.tomexpression.types.expression.ConsOrConnector.make( l1.getHeadOrConnector() ,l2) ;       } else {         return  tom.engine.adt.tomexpression.types.expression.ConsOrConnector.make( l1.getHeadOrConnector() ,tom_append_list_OrConnector( l1.getTailOrConnector() ,l2)) ;       }     } else {       return  tom.engine.adt.tomexpression.types.expression.ConsOrConnector.make(l1,l2) ;     }   }   private static   tom.engine.adt.tomexpression.types.Expression  tom_get_slice_OrConnector( tom.engine.adt.tomexpression.types.Expression  begin,  tom.engine.adt.tomexpression.types.Expression  end, tom.engine.adt.tomexpression.types.Expression  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyOrConnector()  ||  (end== tom.engine.adt.tomexpression.types.expression.EmptyOrConnector.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomexpression.types.expression.ConsOrConnector.make((( ((begin instanceof tom.engine.adt.tomexpression.types.expression.ConsOrConnector) || (begin instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrConnector)) )? begin.getHeadOrConnector() :begin),( tom.engine.adt.tomexpression.types.Expression )tom_get_slice_OrConnector((( ((begin instanceof tom.engine.adt.tomexpression.types.expression.ConsOrConnector) || (begin instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrConnector)) )? begin.getTailOrConnector() : tom.engine.adt.tomexpression.types.expression.EmptyOrConnector.make() ),end,tail)) ;   }      private static   tom.engine.adt.code.types.BQTermList  tom_append_list_concBQTerm( tom.engine.adt.code.types.BQTermList l1,  tom.engine.adt.code.types.BQTermList  l2) {     if( l1.isEmptyconcBQTerm() ) {       return l2;     } else if( l2.isEmptyconcBQTerm() ) {       return l1;     } else if(  l1.getTailconcBQTerm() .isEmptyconcBQTerm() ) {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,tom_append_list_concBQTerm( l1.getTailconcBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTermList  tom_get_slice_concBQTerm( tom.engine.adt.code.types.BQTermList  begin,  tom.engine.adt.code.types.BQTermList  end, tom.engine.adt.code.types.BQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcBQTerm()  ||  (end== tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( begin.getHeadconcBQTerm() ,( tom.engine.adt.code.types.BQTermList )tom_get_slice_concBQTerm( begin.getTailconcBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.tominstruction.types.InstructionList  tom_append_list_concInstruction( tom.engine.adt.tominstruction.types.InstructionList l1,  tom.engine.adt.tominstruction.types.InstructionList  l2) {     if( l1.isEmptyconcInstruction() ) {       return l2;     } else if( l2.isEmptyconcInstruction() ) {       return l1;     } else if(  l1.getTailconcInstruction() .isEmptyconcInstruction() ) {       return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,l2) ;     } else {       return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,tom_append_list_concInstruction( l1.getTailconcInstruction() ,l2)) ;     }   }   private static   tom.engine.adt.tominstruction.types.InstructionList  tom_get_slice_concInstruction( tom.engine.adt.tominstruction.types.InstructionList  begin,  tom.engine.adt.tominstruction.types.InstructionList  end, tom.engine.adt.tominstruction.types.InstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcInstruction()  ||  (end== tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( begin.getHeadconcInstruction() ,( tom.engine.adt.tominstruction.types.InstructionList )tom_get_slice_concInstruction( begin.getTailconcInstruction() ,end,tail)) ;   }      private static   tom.engine.adt.tomname.types.TomNameList  tom_append_list_concTomName( tom.engine.adt.tomname.types.TomNameList l1,  tom.engine.adt.tomname.types.TomNameList  l2) {     if( l1.isEmptyconcTomName() ) {       return l2;     } else if( l2.isEmptyconcTomName() ) {       return l1;     } else if(  l1.getTailconcTomName() .isEmptyconcTomName() ) {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,l2) ;     } else {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,tom_append_list_concTomName( l1.getTailconcTomName() ,l2)) ;     }   }   private static   tom.engine.adt.tomname.types.TomNameList  tom_get_slice_concTomName( tom.engine.adt.tomname.types.TomNameList  begin,  tom.engine.adt.tomname.types.TomNameList  end, tom.engine.adt.tomname.types.TomNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomName()  ||  (end== tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( begin.getHeadconcTomName() ,( tom.engine.adt.tomname.types.TomNameList )tom_get_slice_concTomName( begin.getTailconcTomName() ,end,tail)) ;   }      private static   tom.engine.adt.tomslot.types.SlotList  tom_append_list_concSlot( tom.engine.adt.tomslot.types.SlotList l1,  tom.engine.adt.tomslot.types.SlotList  l2) {     if( l1.isEmptyconcSlot() ) {       return l2;     } else if( l2.isEmptyconcSlot() ) {       return l1;     } else if(  l1.getTailconcSlot() .isEmptyconcSlot() ) {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,l2) ;     } else {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,tom_append_list_concSlot( l1.getTailconcSlot() ,l2)) ;     }   }   private static   tom.engine.adt.tomslot.types.SlotList  tom_get_slice_concSlot( tom.engine.adt.tomslot.types.SlotList  begin,  tom.engine.adt.tomslot.types.SlotList  end, tom.engine.adt.tomslot.types.SlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcSlot()  ||  (end== tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( begin.getHeadconcSlot() ,( tom.engine.adt.tomslot.types.SlotList )tom_get_slice_concSlot( begin.getTailconcSlot() ,end,tail)) ;   }          private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Sequence) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) );       } else {         return ( (tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Sequence(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin):new tom.library.sl.Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)) );   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Choice) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ):new tom.library.sl.Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) );       } else {         return ( (tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ):new tom.library.sl.Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Choice(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin):new tom.library.sl.Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):( null )),end,tail)) );   }       private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  v) { return ( ( (( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )==null)?v:new tom.library.sl.Choice(v,( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) ) );}private static  tom.library.sl.Strategy  tom_make_TopDownCollect( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),tom_make_Try(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) ))) ) );}   


//------------------------------------------------------------	

  









  private Compiler compiler;

  public ConstraintGenerator(Compiler myCompiler) {
    this.compiler = myCompiler; 
  } 

  public Compiler getCompiler() {
    return this.compiler;
  }
 
  private static final String generatorsPackage = "tom.engine.compiler.generator.";
  // the list of all generators
  private static final String[] generatorsNames = {"ACGenerator", "SyntacticGenerator","VariadicGenerator","ArrayGenerator"};
  // constants
  public static final String computeLengthFuncName = "__computeLength";
  public static final String multiplicityFuncName = "__getMultiplicities";
  public static final String getTermForMultiplicityFuncName = "__getTermForMult";  

  public Instruction performGenerations(Expression expression, Instruction action) 
       throws ClassNotFoundException,InstantiationException,IllegalAccessException,VisitFailure,InvocationTargetException,NoSuchMethodException{
    // counts the generators that didn't change the instruction
    int genCounter = 0;
    int genNb = generatorsNames.length;
    
    Expression result = null;
    // cache the generators
    IBaseGenerator[] gen = new IBaseGenerator[genNb];
    Class[] classTab = new Class[]{Class.forName("tom.engine.compiler.Compiler"), Class.forName("tom.engine.compiler.ConstraintGenerator")};
    for(int i=0 ; i < genNb ; i++) {
      Class myClass = Class.forName(generatorsPackage + generatorsNames[i]);
      java.lang.reflect.Constructor constructor = myClass.getConstructor(classTab);
      gen[i] = (IBaseGenerator)constructor.newInstance(this.getCompiler(),this);
    }
    
    // iterate until all generators are applied and nothing was changed 
    mainLoop: while(true) {
      for(int i=0 ; i < genNb ; i++) {
        result = gen[i].generate(expression);
        // if nothing was done, start counting 
        genCounter = (result == expression) ? (genCounter + 1) : 0;        				
        // if we applied all the generators and nothing changed,
        // it's time to stop
        if (genCounter == genNb) { break mainLoop; }
        // reinitialize
        expression = result; 
      }
    } // end while
    return buildInstructionFromExpression(result,action);
  }
  
  /**
   * Converts the resulted expression (after generation) into instructions
   */
  private Instruction buildInstructionFromExpression(Expression expression, Instruction action)
      throws VisitFailure {		
    // it is done innermost because the expression is also simplified		
    expression = tom_make_TopDown(tom_make_ReplaceSubterms(this)).visitLight(expression);
    // generate automata
    Instruction automata = generateAutomata(expression,action);    
    return automata;
  }

  /**
   * Generates the automata from the expression
   */
  private Instruction generateAutomata(Expression expression, Instruction action) throws VisitFailure {
    {{if ( (expression instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.And) ) {

        Instruction subInstruction = generateAutomata( (( tom.engine.adt.tomexpression.types.Expression )expression).getArg2() ,action);
        return generateAutomata( (( tom.engine.adt.tomexpression.types.Expression )expression).getArg1() ,subInstruction);
      }}}{if ( (expression instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( (((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.ConsOrConnector) || ((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrConnector)) ) {
        
        return buildConstraintDisjunction((( tom.engine.adt.tomexpression.types.Expression )expression),action);
      }}}{if ( (expression instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.ConstraintToExpression) ) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch110_8= (( tom.engine.adt.tomexpression.types.Expression )expression).getcons() ;if ( (tomMatch110_8 instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch110_10= tomMatch110_8.getPattern() ;boolean tomMatch110_14= false ;if ( (tomMatch110_10 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {tomMatch110_14= true ;} else {if ( (tomMatch110_10 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {tomMatch110_14= true ;}}if (tomMatch110_14) {


        return  tom.engine.adt.tominstruction.types.instruction.LetRef.make(TomBase.convertFromVarToBQVar(tomMatch110_10),  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make( tomMatch110_8.getSubject() ) , action) ;
      }}}}}{if ( (expression instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.ConstraintToExpression) ) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch110_16= (( tom.engine.adt.tomexpression.types.Expression )expression).getcons() ;if ( (tomMatch110_16 instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch110_18= tomMatch110_16.getPattern() ;boolean tomMatch110_22= false ;if ( (tomMatch110_18 instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariableStar) ) {tomMatch110_22= true ;} else {if ( (tomMatch110_18 instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariable) ) {tomMatch110_22= true ;}}if (tomMatch110_22) {

       
        return action;      
      }}}}}{if ( (expression instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.ConstraintToExpression) ) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch110_24= (( tom.engine.adt.tomexpression.types.Expression )expression).getcons() ;if ( (tomMatch110_24 instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) {


        return buildNumericCondition(tomMatch110_24,action);
      }}}}{if ( (expression instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.DoWhileExpression) ) {


        Instruction subInstruction = generateAutomata( (( tom.engine.adt.tomexpression.types.Expression )expression).getEndExpression() , tom.engine.adt.tominstruction.types.instruction.Nop.make() );
        return  tom.engine.adt.tominstruction.types.instruction.DoWhile.make( tom.engine.adt.tominstruction.types.instruction.UnamedBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(action, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(subInstruction, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ,  (( tom.engine.adt.tomexpression.types.Expression )expression).getLoopCondition() ) ;
      }}}{if ( (expression instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.TomInstructionToExpression) ) {


        return  (( tom.engine.adt.tomexpression.types.Expression )expression).getInstruction() ;
      }}}{if ( (expression instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( (((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction) || ((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction)) ) {


        return buildExpressionDisjunction((( tom.engine.adt.tomexpression.types.Expression )expression),action);
      }}}{if ( (expression instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.AntiMatchExpression) ) {


        return buildAntiMatchInstruction( (( tom.engine.adt.tomexpression.types.Expression )expression).getExpression() ,action);
      }}}{if ( (expression instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.ACMatchLoop) ) {


        return buildACMatchLoop( (( tom.engine.adt.tomexpression.types.Expression )expression).getPattern() , (( tom.engine.adt.tomexpression.types.Expression )expression).getSubject() ,action);
      }}}{if ( (expression instanceof tom.engine.adt.tomexpression.types.Expression) ) {


        return  tom.engine.adt.tominstruction.types.instruction.If.make((( tom.engine.adt.tomexpression.types.Expression )expression), action,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ;
      }}}

    throw new TomRuntimeException("ConstraintGenerator.generateAutomata - strange expression:" + expression);
  }

  /**
   * Converts 'Subterm' to 'GetSlot'
   */
  public static class ReplaceSubterms extends tom.library.sl.AbstractStrategyBasic {private  ConstraintGenerator  cg;public ReplaceSubterms( ConstraintGenerator  cg) {super(( new tom.library.sl.Identity() ));this.cg=cg;}public  ConstraintGenerator  getcg() {return cg;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )tom__arg) instanceof tom.engine.adt.code.types.bqterm.Subterm) ) { tom.engine.adt.tomname.types.TomName  tomMatch111_1= (( tom.engine.adt.code.types.BQTerm )tom__arg).getAstName() ;if ( (tomMatch111_1 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.tomname.types.TomName  tom_slotName= (( tom.engine.adt.code.types.BQTerm )tom__arg).getSlotName() ;


        TomSymbol tomSymbol = cg.getCompiler().getSymbolTable().getSymbolFromName( tomMatch111_1.getString() );
        TomType subtermType = TomBase.getSlotType(tomSymbol, tom_slotName);	        	
        return  tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.GetSlot.make(subtermType, tomMatch111_1, tom_slotName.getString(),  (( tom.engine.adt.code.types.BQTerm )tom__arg).getGroundTerm() ) ) ;
      }}}}}return _visit_BQTerm(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_ReplaceSubterms( ConstraintGenerator  t0) { return new ReplaceSubterms(t0);}




  /**
   * compile a disjunction of pattern by duplication the action part
   * we are forced to duplication the action to have the same semantics as
   * without disjunctions
   */
  private Instruction buildConstraintDisjunction(Expression orConnector, Instruction action) throws VisitFailure {    
    {{if ( (orConnector instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( (((( tom.engine.adt.tomexpression.types.Expression )orConnector) instanceof tom.engine.adt.tomexpression.types.expression.ConsOrConnector) || ((( tom.engine.adt.tomexpression.types.Expression )orConnector) instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrConnector)) ) {if (!( (  (( tom.engine.adt.tomexpression.types.Expression )orConnector).isEmptyOrConnector()  ||  ((( tom.engine.adt.tomexpression.types.Expression )orConnector)== tom.engine.adt.tomexpression.types.expression.EmptyOrConnector.make() )  ) )) {
        
        return  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(generateAutomata((( (((( tom.engine.adt.tomexpression.types.Expression )orConnector) instanceof tom.engine.adt.tomexpression.types.expression.ConsOrConnector) || ((( tom.engine.adt.tomexpression.types.Expression )orConnector) instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrConnector)) )?( (( tom.engine.adt.tomexpression.types.Expression )orConnector).getHeadOrConnector() ):((( tom.engine.adt.tomexpression.types.Expression )orConnector))),action), tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(buildConstraintDisjunction((( (((( tom.engine.adt.tomexpression.types.Expression )orConnector) instanceof tom.engine.adt.tomexpression.types.expression.ConsOrConnector) || ((( tom.engine.adt.tomexpression.types.Expression )orConnector) instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrConnector)) )?( (( tom.engine.adt.tomexpression.types.Expression )orConnector).getTailOrConnector() ):( tom.engine.adt.tomexpression.types.expression.EmptyOrConnector.make() )),action)
              , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) 


;
      }}}}}

    return  tom.engine.adt.tominstruction.types.instruction.Nop.make() ;
  }

  /*
   * Takes the OrConstraintDisjunction and generates the tests
   * (this is for the disjunction of symbols)
   * 
   * boolean flag = false;
   * var1 = null;
   * var2 = null;
   * ....
   * if (is_fsym(f1)) {
   *    flag = true;
   *    var1 = subterm1_f1();
   *    var2 = subterm2_f1();
   *    .....    
   * } else if (is_fsym(f2)) {
   *    flag = true;
   *    var1 = subterm1_f2();
   *    var2 = subterm2_f2();
   *    ..... 
   * } ....
   * if (flag == true) ...
   *  
   */
  private Instruction buildExpressionDisjunction(Expression orDisjunction,Instruction action)
         throws VisitFailure {     
    BQTerm flag = getCompiler().getFreshVariable(getCompiler().getSymbolTable().getBooleanType());
    Instruction assignFlagTrue =  tom.engine.adt.tominstruction.types.instruction.Assign.make(flag,  tom.engine.adt.tomexpression.types.expression.TrueTL.make() ) ;
    Collection<BQTerm> freshVarList = new HashSet<BQTerm>();
    // collect variables    
    tom_make_TopDown(tom_make_CollectVar(freshVarList)).visitLight(orDisjunction);    
    Instruction instruction = buildDisjunctionIfElse(orDisjunction,assignFlagTrue);
    // add the final test
    instruction =  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(instruction, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(flag) , action,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ;    
    // add fresh variables' declarations
    for(BQTerm var:freshVarList) {
      instruction =  tom.engine.adt.tominstruction.types.instruction.LetRef.make(var,  tom.engine.adt.tomexpression.types.expression.Bottom.make(var.getAstType()) , instruction) ;
    }
    // stick the flag declaration also
    return  tom.engine.adt.tominstruction.types.instruction.LetRef.make(flag,  tom.engine.adt.tomexpression.types.expression.FalseTL.make() , instruction) ;
  }

  private Instruction buildDisjunctionIfElse(Expression orDisjunction,Instruction assignFlagTrue)
      throws VisitFailure {    
    {{if ( (orDisjunction instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( (((( tom.engine.adt.tomexpression.types.Expression )orDisjunction) instanceof tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction) || ((( tom.engine.adt.tomexpression.types.Expression )orDisjunction) instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction)) ) {if ( (  (( tom.engine.adt.tomexpression.types.Expression )orDisjunction).isEmptyOrExpressionDisjunction()  ||  ((( tom.engine.adt.tomexpression.types.Expression )orDisjunction)== tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction.make() )  ) ) {

        return  tom.engine.adt.tominstruction.types.instruction.Nop.make() ;
      }}}}{if ( (orDisjunction instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( (((( tom.engine.adt.tomexpression.types.Expression )orDisjunction) instanceof tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction) || ((( tom.engine.adt.tomexpression.types.Expression )orDisjunction) instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction)) ) {if (!( (  (( tom.engine.adt.tomexpression.types.Expression )orDisjunction).isEmptyOrExpressionDisjunction()  ||  ((( tom.engine.adt.tomexpression.types.Expression )orDisjunction)== tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction.make() )  ) )) { tom.engine.adt.tomexpression.types.Expression  tomMatch113_8=(( (((( tom.engine.adt.tomexpression.types.Expression )orDisjunction) instanceof tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction) || ((( tom.engine.adt.tomexpression.types.Expression )orDisjunction) instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction)) )?( (( tom.engine.adt.tomexpression.types.Expression )orDisjunction).getHeadOrExpressionDisjunction() ):((( tom.engine.adt.tomexpression.types.Expression )orDisjunction)));if ( (tomMatch113_8 instanceof tom.engine.adt.tomexpression.types.expression.And) ) {
        
        Instruction subtest = buildDisjunctionIfElse(tom_append_list_OrExpressionDisjunction((( (((( tom.engine.adt.tomexpression.types.Expression )orDisjunction) instanceof tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction) || ((( tom.engine.adt.tomexpression.types.Expression )orDisjunction) instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction)) )?( (( tom.engine.adt.tomexpression.types.Expression )orDisjunction).getTailOrExpressionDisjunction() ):( tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction.make() )), tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction.make() ),assignFlagTrue);
        return  tom.engine.adt.tominstruction.types.instruction.If.make( tomMatch113_8.getArg1() ,  tom.engine.adt.tominstruction.types.instruction.UnamedBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(assignFlagTrue, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(generateAutomata( tomMatch113_8.getArg2() , tom.engine.adt.tominstruction.types.instruction.Nop.make() ), tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) , subtest) ;
      }}}}}{if ( (orDisjunction instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( (((( tom.engine.adt.tomexpression.types.Expression )orDisjunction) instanceof tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction) || ((( tom.engine.adt.tomexpression.types.Expression )orDisjunction) instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction)) ) {if (!( (  (( tom.engine.adt.tomexpression.types.Expression )orDisjunction).isEmptyOrExpressionDisjunction()  ||  ((( tom.engine.adt.tomexpression.types.Expression )orDisjunction)== tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction.make() )  ) )) {
        
        Instruction subtest = buildDisjunctionIfElse(tom_append_list_OrExpressionDisjunction((( (((( tom.engine.adt.tomexpression.types.Expression )orDisjunction) instanceof tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction) || ((( tom.engine.adt.tomexpression.types.Expression )orDisjunction) instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction)) )?( (( tom.engine.adt.tomexpression.types.Expression )orDisjunction).getTailOrExpressionDisjunction() ):( tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction.make() )), tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction.make() ),assignFlagTrue);
        return  tom.engine.adt.tominstruction.types.instruction.If.make((( (((( tom.engine.adt.tomexpression.types.Expression )orDisjunction) instanceof tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction) || ((( tom.engine.adt.tomexpression.types.Expression )orDisjunction) instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction)) )?( (( tom.engine.adt.tomexpression.types.Expression )orDisjunction).getHeadOrExpressionDisjunction() ):((( tom.engine.adt.tomexpression.types.Expression )orDisjunction))), assignFlagTrue, subtest) ;
      }}}}}

    throw new TomRuntimeException("ConstraintGenerator.buildDisjunctionIfElse - strange expression:" + orDisjunction);
  }

  /**
   * generates:
   * 
   * bool matchSuccessful = false;
   * if (expression) {
   *    matchSuccessful = true;
   * }
   * if (matchSuccessful == false) {
   *    action;
   * }
   */
  private Instruction buildAntiMatchInstruction(Expression expression, Instruction action)
      throws VisitFailure {
    BQTerm flag = getCompiler().getFreshVariable(getCompiler().getSymbolTable().getBooleanType());
    Instruction assignFlagTrue =  tom.engine.adt.tominstruction.types.instruction.Assign.make(flag,  tom.engine.adt.tomexpression.types.expression.TrueTL.make() ) ;
    Instruction automata = generateAutomata(expression, assignFlagTrue);    
    // add the final test
    Instruction result =  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(automata, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.Negation.make( tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(flag) ) , action,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) 
;
    return  tom.engine.adt.tominstruction.types.instruction.LetRef.make(flag,  tom.engine.adt.tomexpression.types.expression.FalseTL.make() , result) ;
  }

  /**
   * Collect the variables in a match constraint
   */
  public static class CollectVar extends tom.library.sl.AbstractStrategyBasic {private  java.util.Collection<BQTerm>  varList;public CollectVar( java.util.Collection<BQTerm>  varList) {super(( new tom.library.sl.Identity() ));this.varList=varList;}public  java.util.Collection<BQTerm>  getvarList() {return varList;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch114_1= (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getPattern() ;if ( (tomMatch114_1 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomterm.types.TomTerm  tom_v=tomMatch114_1;


        if(!varList.contains(tom_v)) { varList.add(TomBase.convertFromVarToBQVar(tom_v)); }        
      }}}}}return _visit_Constraint(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  _visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomconstraint.types.Constraint )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {return ((T)visit_Constraint((( tom.engine.adt.tomconstraint.types.Constraint )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_CollectVar( java.util.Collection<BQTerm>  t0) { return new CollectVar(t0);}

// end strategy   
  
  /**
   * Collect the free variables in an expression (do not inspect under a anti)  
   */
  public static class CollectFreeVar extends tom.library.sl.AbstractStrategyBasic {private  java.util.Collection<BQTerm>  varList;public CollectFreeVar( java.util.Collection<BQTerm>  varList) {super(( new tom.library.sl.Identity() ));this.varList=varList;}public  java.util.Collection<BQTerm>  getvarList() {return varList;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomexpression.types.Expression  visit_Expression( tom.engine.adt.tomexpression.types.Expression  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.ConstraintToExpression) ) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch115_1= (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getcons() ;if ( (tomMatch115_1 instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch115_3= tomMatch115_1.getPattern() ;if ( (tomMatch115_3 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomterm.types.TomTerm  tom_v=tomMatch115_3;


        if(!varList.contains(tom_v)) { varList.add(TomBase.convertFromVarToBQVar(tom_v)); }     
        throw new VisitFailure();
      }}}}}{if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.AntiMatchExpression) ) {
        
        throw new VisitFailure();
      }}}}return _visit_Expression(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.engine.adt.tomexpression.types.Expression  _visit_Expression( tom.engine.adt.tomexpression.types.Expression  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomexpression.types.Expression )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomexpression.types.Expression) ) {return ((T)visit_Expression((( tom.engine.adt.tomexpression.types.Expression )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_CollectFreeVar( java.util.Collection<BQTerm>  t0) { return new CollectFreeVar(t0);}


  
  /**
   * check that the list is empty
   * when domain=codomain, the test is extended to:
   *   is_empty(l) || l==make_empty()
   *   this is needed because get_tail() may return the neutral element 
   */ 
  public Expression genIsEmptyList(TomName opName, BQTerm var) {
    TomSymbol tomSymbol = getCompiler().getSymbolTable().getSymbolFromName(opName.getString());
    TomType domain = TomBase.getSymbolDomain(tomSymbol).getHeadconcTomType();
    TomType codomain = TomBase.getSymbolCodomain(tomSymbol);
    if(domain==codomain) {
      //TODO: we should check if the BuildEmptyList exists for the mapping (can be null)
      return  tom.engine.adt.tomexpression.types.expression.Or.make( tom.engine.adt.tomexpression.types.expression.IsEmptyList.make(opName, var) ,  tom.engine.adt.tomexpression.types.expression.EqualBQTerm.make(codomain, var,  tom.engine.adt.code.types.bqterm.BuildEmptyList.make(opName) ) ) ;
    }
    return  tom.engine.adt.tomexpression.types.expression.IsEmptyList.make(opName, var) ;
  }
  
  private Instruction buildNumericCondition(Constraint c, Instruction action) {
    {{if ( (c instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )c) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) { tom.engine.adt.code.types.BQTerm  tom_left= (( tom.engine.adt.tomconstraint.types.Constraint )c).getLeft() ; tom.engine.adt.code.types.BQTerm  tom_right= (( tom.engine.adt.tomconstraint.types.Constraint )c).getRight() ; tom.engine.adt.tomconstraint.types.NumericConstraintType  tom_type= (( tom.engine.adt.tomconstraint.types.Constraint )c).getType() ;
        
        Expression leftExpr =  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(tom_left) ;
        Expression rightExpr =  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(tom_right) ;
        {{if ( (tom_type instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )tom_type) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumLessThan) ) {
 return  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.LessThan.make(leftExpr, rightExpr) , action,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ;}}}{if ( (tom_type instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )tom_type) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumLessOrEqualThan) ) {
 return  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.LessOrEqualThan.make(leftExpr, rightExpr) , action,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ;}}}{if ( (tom_type instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )tom_type) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumGreaterThan) ) {
 return  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.GreaterThan.make(leftExpr, rightExpr) , action,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ;}}}{if ( (tom_type instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )tom_type) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumGreaterOrEqualThan) ) {
 return  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.GreaterOrEqualThan.make(leftExpr, rightExpr) , action,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ;}}}{if ( (tom_type instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )tom_type) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumEqual) ) {
 TomType tomType = getCompiler().getTermTypeFromTerm(tom_left);
                                         return  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.EqualBQTerm.make(tomType, tom_right, tom_left) , action,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ; }}}{if ( (tom_type instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )tom_type) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumDifferent) ) {
 TomType tomType = getCompiler().getTermTypeFromTerm(tom_left);
                                         return  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.Negation.make( tom.engine.adt.tomexpression.types.expression.EqualBQTerm.make(tomType, tom_right, tom_left) ) , action,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ; }}}}

      }}}}

    // should never reach here
    throw new TomRuntimeException("Untreated numeric constraint: " + c);
  }


  /*
   * NO LONGER USEFUL: DEAD CODE
   *
   * Takes the OrConnector and generates the tests
   * (this is for the OrConstraint - the disjunction of constraints)
   *
   * var1 = null; // all the free variables
   * var2 = null;
   * ....
   * varm = null;
   * int counter = 0;
   * do { // n is the number of 'or'
   *    boolean flag = false;
   *    if (counter >= 0 && counter <=0 ) { // generated like this because if we use "counter == 0", we have to include "int.tom" 
   *        if ( code_for_first_constraint_in_disjunction ) {
   *            flag = true;
   *        }
   *    } else if (counter >= 1 && counter <= 1 ) {
   *    ....
   *    else if (counter >= n-1 && counter <= n-1) {
   *        if ( code_for_n_constraint_in_disjunction ) {
   *            flag = true;
   *        }
   *    }    
   *    if (flag == true) ...
   *    counter++;
   * } while (counter < n)
   *  
   */
  private Instruction buildConstraintDisjunctionWithoutCopy(Expression orConnector, Instruction action) throws VisitFailure {    
    BQTerm flag = getCompiler().getFreshVariable(getCompiler().getSymbolTable().getBooleanType());
    Instruction assignFlagTrue =  tom.engine.adt.tominstruction.types.instruction.Assign.make(flag,  tom.engine.adt.tomexpression.types.expression.TrueTL.make() ) ;
    TomType intType = getCompiler().getSymbolTable().getIntType();
    BQTerm counter = getCompiler().getFreshVariable(intType);    
    // build the ifs
    Instruction instruction = buildTestsInConstraintDisjuction(0,assignFlagTrue,counter,intType,orConnector);    
    // add the final test
    instruction =  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(instruction, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.EqualTerm.make(getCompiler().getSymbolTable().getBooleanType(), flag,  tom.engine.adt.tomterm.types.tomterm.TruePattern.make() ) , action,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) 
;
    // counter++ : expression at the end of the loop 
    Instruction counterIncrement =  tom.engine.adt.tominstruction.types.instruction.Assign.make(counter,  tom.engine.adt.tomexpression.types.expression.AddOne.make(counter) ) ;
    //  stick the flag declaration and the counterIncrement   
    instruction =  tom.engine.adt.tominstruction.types.instruction.LetRef.make(flag,  tom.engine.adt.tomexpression.types.expression.FalseTL.make() ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(instruction, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(counterIncrement, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) ;      
    instruction =  tom.engine.adt.tominstruction.types.instruction.DoWhile.make(instruction,  tom.engine.adt.tomexpression.types.expression.LessThan.make( tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(counter) ,  tom.engine.adt.tomexpression.types.expression.Integer.make(orConnector.length()) ) ) ;    
    // add fresh variables' declarations
    Collection<BQTerm> freshVarList = new HashSet<BQTerm>();
    // collect free variables    
    tom_make_TopDownCollect(tom_make_CollectFreeVar(freshVarList)).visitLight(orConnector);
    for(BQTerm var:freshVarList) {
      instruction =  tom.engine.adt.tominstruction.types.instruction.LetRef.make(var,  tom.engine.adt.tomexpression.types.expression.Bottom.make(var.getAstType()) , instruction) ;
    }
    // stick the counter declaration
    return  tom.engine.adt.tominstruction.types.instruction.LetRef.make(counter,  tom.engine.adt.tomexpression.types.expression.Integer.make(0) , instruction) ;
  }

  /**
   * NO LONGER USEFUL: DEAD CODE
   *
   * builds the ifs in a constraint disjunction (see buildConstraintDisjunction above for details)
   */
  private Instruction buildTestsInConstraintDisjuction(int cnt, Instruction assignFlagTrue, 
      BQTerm counter, TomType intType, Expression orConnector) throws VisitFailure {
    {{if ( (orConnector instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( (((( tom.engine.adt.tomexpression.types.Expression )orConnector) instanceof tom.engine.adt.tomexpression.types.expression.ConsOrConnector) || ((( tom.engine.adt.tomexpression.types.Expression )orConnector) instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrConnector)) ) {if (!( (  (( tom.engine.adt.tomexpression.types.Expression )orConnector).isEmptyOrConnector()  ||  ((( tom.engine.adt.tomexpression.types.Expression )orConnector)== tom.engine.adt.tomexpression.types.expression.EmptyOrConnector.make() )  ) )) {
        
        return  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.And.make( tom.engine.adt.tomexpression.types.expression.GreaterOrEqualThan.make( tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(counter) ,  tom.engine.adt.tomexpression.types.expression.Integer.make(cnt) ) ,  tom.engine.adt.tomexpression.types.expression.LessOrEqualThan.make( tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(counter) ,  tom.engine.adt.tomexpression.types.expression.Integer.make(cnt) ) ) , generateAutomata((( (((( tom.engine.adt.tomexpression.types.Expression )orConnector) instanceof tom.engine.adt.tomexpression.types.expression.ConsOrConnector) || ((( tom.engine.adt.tomexpression.types.Expression )orConnector) instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrConnector)) )?( (( tom.engine.adt.tomexpression.types.Expression )orConnector).getHeadOrConnector() ):((( tom.engine.adt.tomexpression.types.Expression )orConnector))),assignFlagTrue), buildTestsInConstraintDisjuction(cnt,assignFlagTrue,counter,intType,tom_append_list_OrConnector((( (((( tom.engine.adt.tomexpression.types.Expression )orConnector) instanceof tom.engine.adt.tomexpression.types.expression.ConsOrConnector) || ((( tom.engine.adt.tomexpression.types.Expression )orConnector) instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrConnector)) )?( (( tom.engine.adt.tomexpression.types.Expression )orConnector).getTailOrConnector() ):( tom.engine.adt.tomexpression.types.expression.EmptyOrConnector.make() )), tom.engine.adt.tomexpression.types.expression.EmptyOrConnector.make() ))) 


;        
      }}}}}

    return  tom.engine.adt.tominstruction.types.instruction.Nop.make() ;
  }

  private Instruction buildACMatchLoop(TomTerm pattern, BQTerm subject, Instruction action) {    
    SymbolTable symbolTable = getCompiler().getSymbolTable();
    TomType intType = symbolTable.getIntType();
    TomType intArrayType = symbolTable.getIntArrayType();
    // a 0
    ///Expression zero = `Integer(0);
    // the name of the int[] operator
    TomName intArrayName =  tom.engine.adt.tomname.types.tomname.Name.make(symbolTable.getIntArrayOp()) ;

    BQTerm alpha = getCompiler().getFreshVariable("alpha",intArrayType);
    BQTerm tempSol = getCompiler().getFreshVariable("tempSol",intArrayType);
    BQTerm position = getCompiler().getFreshVariable("position",intType);
    BQTerm length = getCompiler().getFreshVariable("length",intType);                

    ///String tomName = null;
    String symbolName = null;
    TomTerm var_x = null;
    TomTerm var_y = null;
    {{if ( (pattern instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )pattern) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch119_1= (( tom.engine.adt.tomterm.types.TomTerm )pattern).getNameList() ; tom.engine.adt.tomslot.types.SlotList  tomMatch119_2= (( tom.engine.adt.tomterm.types.TomTerm )pattern).getSlots() ;if ( ((tomMatch119_1 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch119_1 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch119_1.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch119_10= tomMatch119_1.getHeadconcTomName() ;if ( (tomMatch119_10 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch119_1.getTailconcTomName() .isEmptyconcTomName() ) {if ( ((tomMatch119_2 instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || (tomMatch119_2 instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {if (!( tomMatch119_2.isEmptyconcSlot() )) { tom.engine.adt.tomslot.types.Slot  tomMatch119_12= tomMatch119_2.getHeadconcSlot() ;if ( (tomMatch119_12 instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) { tom.engine.adt.tomslot.types.SlotList  tomMatch119_7= tomMatch119_2.getTailconcSlot() ;if (!( tomMatch119_7.isEmptyconcSlot() )) { tom.engine.adt.tomslot.types.Slot  tomMatch119_14= tomMatch119_7.getHeadconcSlot() ;if ( (tomMatch119_14 instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) {if (  tomMatch119_7.getTailconcSlot() .isEmptyconcSlot() ) {

        ///tomName = `tomName;
        symbolName =  tomMatch119_10.getString() ;
        var_x =  tomMatch119_12.getAppl() ;
        var_y =  tomMatch119_14.getAppl() ;
      }}}}}}}}}}}}}}

    BQTermList getTermArgs =  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(tempSol, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(alpha, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(subject, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) ) ;        
    TomType subtermType = getCompiler().getTermTypeFromTerm(var_x);
    TomType booleanType = symbolTable.getBooleanType();
/*
    Expression reinitializationLoopCond = `And(
        GreaterThan(
          BQTermToExpression(position),
          zero),
        GreaterThan(
          GetElement(intArrayName,intType,tempSol,position),
          GetElement(intArrayName,intType,alpha,position))
        );

    Instruction reinitializationLoop = `DoWhile(
        AbstractBlock(concInstruction(
        AssignArray(tempSol,position,zero),
          LetRef(position,SubstractOne(position),
            AssignArray(tempSol, position, AddOne(ExpressionToBQTerm(GetElement(intArrayName,intType,tempSol,position))))
            )
          )), reinitializationLoopCond);

    Expression testCond = `LessThan(
        GetElement(intArrayName,intType,tempSol,position),
        GetElement(intArrayName,intType,alpha,position)
        );

    Instruction test = `If(testCond,
        AssignArray(tempSol, position, AddOne(ExpressionToBQTerm(GetElement(intArrayName,intType,tempSol,position)))),
        reinitializationLoop
        );

    Expression whileCond = `LessOrEqualThan(
        GetElement(intArrayName,intType,tempSol,ExpressionToBQTerm(zero)),
        GetElement(intArrayName,intType,alpha,ExpressionToBQTerm(zero)));

    Instruction instruction = `DoWhile(
        LetRef(position,SubstractOne(length),
          LetRef(TomBase.convertFromVarToBQVar(x),
            BQTermToExpression(FunctionCall( Name(ConstraintGenerator.getTermForMultiplicityFuncName + "_" + tomName), subtermType,concBQTerm(getTermArgs*,ExpressionToBQTerm(FalseTL())))),
            LetRef(TomBase.convertFromVarToBQVar(y),
              BQTermToExpression(FunctionCall( Name(ConstraintGenerator.getTermForMultiplicityFuncName + "_" + tomName), subtermType,concBQTerm(getTermArgs*,ExpressionToBQTerm(TrueTL())))),
              UnamedBlock(
                concInstruction(
                  action,
                  test
                  )
                )
              )
            )
          ), whileCond);


    instruction = `LetRef(tempSol,BQTermToExpression(BuildEmptyArray(intArrayName,length)),instruction);
    instruction = `LetRef(length,GetSize(intArrayName,alpha),instruction);
    instruction = `LetRef(alpha,BQTermToExpression(FunctionCall(
            Name(ConstraintGenerator.multiplicityFuncName + "_" + tomName),
            intArrayType,concBQTerm(subject))),instruction);

    // make sure the additional functions are generated
    symbolTable.setUsedSymbolConstructor(symbolTable.getSymbolFromName(tomName));
    symbolTable.setUsedSymbolConstructor(symbolTable.getSymbolFromName(symbolTable.getIntArrayOp()));
    symbolTable.setUsedSymbolDestructor(symbolTable.getSymbolFromName(symbolTable.getIntArrayOp()));
*/
    
    Expression whileCond =  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make( tom.engine.adt.code.types.bqterm.FunctionCall.make( tom.engine.adt.tomname.types.tomname.Name.make("next_minimal_extract") , booleanType,  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(length, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(alpha, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(tempSol, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) ) ) ) ;

    Instruction instruction =  tom.engine.adt.tominstruction.types.instruction.DoWhile.make( tom.engine.adt.tominstruction.types.instruction.LetRef.make(TomBase.convertFromVarToBQVar(var_x),  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make( tom.engine.adt.code.types.bqterm.FunctionCall.make( tom.engine.adt.tomname.types.tomname.Name.make(ConstraintGenerator.getTermForMultiplicityFuncName+ "_" + symbolName) , subtermType, tom_append_list_concBQTerm(getTermArgs, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.FalseTL.make() ) , tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) )) ) ,  tom.engine.adt.tominstruction.types.instruction.LetRef.make(TomBase.convertFromVarToBQVar(var_y),  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make( tom.engine.adt.code.types.bqterm.FunctionCall.make( tom.engine.adt.tomname.types.tomname.Name.make(ConstraintGenerator.getTermForMultiplicityFuncName+ "_" + symbolName) , subtermType, tom_append_list_concBQTerm(getTermArgs, tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.TrueTL.make() ) , tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) )) ) , action) 
          ) , whileCond) 






;

    instruction =  tom.engine.adt.tominstruction.types.instruction.LetRef.make(position,  tom.engine.adt.tomexpression.types.expression.SubstractOne.make(length) , instruction) ;
    instruction =  tom.engine.adt.tominstruction.types.instruction.LetRef.make(tempSol,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make( tom.engine.adt.code.types.bqterm.BuildEmptyArray.make(intArrayName, length) ) , instruction) ;
    instruction =  tom.engine.adt.tominstruction.types.instruction.LetRef.make(length,  tom.engine.adt.tomexpression.types.expression.GetSize.make(intArrayName, alpha) , instruction) ;
    instruction =  tom.engine.adt.tominstruction.types.instruction.LetRef.make(alpha,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make( tom.engine.adt.code.types.bqterm.FunctionCall.make( tom.engine.adt.tomname.types.tomname.Name.make(ConstraintGenerator.multiplicityFuncName+ "_" + symbolName) , intArrayType,  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(subject, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) ) ) , instruction) 

;

    return instruction;
  }

}
