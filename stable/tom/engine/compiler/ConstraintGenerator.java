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
import tom.library.sl.*;

/**
 * This class is in charge with launching all the generators,
 * until no more generations can be made 
 */
public class ConstraintGenerator {

//------------------------------------------------------------	
        private static   tom.engine.adt.tominstruction.types.InstructionList  tom_append_list_concInstruction( tom.engine.adt.tominstruction.types.InstructionList l1,  tom.engine.adt.tominstruction.types.InstructionList  l2) {     if( l1.isEmptyconcInstruction() ) {       return l2;     } else if( l2.isEmptyconcInstruction() ) {       return l1;     } else if(  l1.getTailconcInstruction() .isEmptyconcInstruction() ) {       return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,l2) ;     } else {       return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,tom_append_list_concInstruction( l1.getTailconcInstruction() ,l2)) ;     }   }   private static   tom.engine.adt.tominstruction.types.InstructionList  tom_get_slice_concInstruction( tom.engine.adt.tominstruction.types.InstructionList  begin,  tom.engine.adt.tominstruction.types.InstructionList  end, tom.engine.adt.tominstruction.types.InstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcInstruction()  ||  (end== tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( begin.getHeadconcInstruction() ,( tom.engine.adt.tominstruction.types.InstructionList )tom_get_slice_concInstruction( begin.getTailconcInstruction() ,end,tail)) ;   }      private static   tom.engine.adt.tomname.types.TomNameList  tom_append_list_concTomName( tom.engine.adt.tomname.types.TomNameList l1,  tom.engine.adt.tomname.types.TomNameList  l2) {     if( l1.isEmptyconcTomName() ) {       return l2;     } else if( l2.isEmptyconcTomName() ) {       return l1;     } else if(  l1.getTailconcTomName() .isEmptyconcTomName() ) {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,l2) ;     } else {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,tom_append_list_concTomName( l1.getTailconcTomName() ,l2)) ;     }   }   private static   tom.engine.adt.tomname.types.TomNameList  tom_get_slice_concTomName( tom.engine.adt.tomname.types.TomNameList  begin,  tom.engine.adt.tomname.types.TomNameList  end, tom.engine.adt.tomname.types.TomNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomName()  ||  (end== tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( begin.getHeadconcTomName() ,( tom.engine.adt.tomname.types.TomNameList )tom_get_slice_concTomName( begin.getTailconcTomName() ,end,tail)) ;   }      private static   tom.engine.adt.tomexpression.types.Expression  tom_append_list_OrExpressionDisjunction( tom.engine.adt.tomexpression.types.Expression  l1,  tom.engine.adt.tomexpression.types.Expression  l2) {     if( l1.isEmptyOrExpressionDisjunction() ) {       return l2;     } else if( l2.isEmptyOrExpressionDisjunction() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction) || (l1 instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction)) ) {       if(  l1.getTailOrExpressionDisjunction() .isEmptyOrExpressionDisjunction() ) {         return  tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction.make( l1.getHeadOrExpressionDisjunction() ,l2) ;       } else {         return  tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction.make( l1.getHeadOrExpressionDisjunction() ,tom_append_list_OrExpressionDisjunction( l1.getTailOrExpressionDisjunction() ,l2)) ;       }     } else {       return  tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction.make(l1,l2) ;     }   }   private static   tom.engine.adt.tomexpression.types.Expression  tom_get_slice_OrExpressionDisjunction( tom.engine.adt.tomexpression.types.Expression  begin,  tom.engine.adt.tomexpression.types.Expression  end, tom.engine.adt.tomexpression.types.Expression  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyOrExpressionDisjunction()  ||  (end== tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction.make((( ((begin instanceof tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction) || (begin instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction)) )? begin.getHeadOrExpressionDisjunction() :begin),( tom.engine.adt.tomexpression.types.Expression )tom_get_slice_OrExpressionDisjunction((( ((begin instanceof tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction) || (begin instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction)) )? begin.getTailOrExpressionDisjunction() : tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction.make() ),end,tail)) ;   }      private static   tom.engine.adt.tomexpression.types.Expression  tom_append_list_OrConnector( tom.engine.adt.tomexpression.types.Expression  l1,  tom.engine.adt.tomexpression.types.Expression  l2) {     if( l1.isEmptyOrConnector() ) {       return l2;     } else if( l2.isEmptyOrConnector() ) {       return l1;     } else if( ((l1 instanceof tom.engine.adt.tomexpression.types.expression.ConsOrConnector) || (l1 instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrConnector)) ) {       if(  l1.getTailOrConnector() .isEmptyOrConnector() ) {         return  tom.engine.adt.tomexpression.types.expression.ConsOrConnector.make( l1.getHeadOrConnector() ,l2) ;       } else {         return  tom.engine.adt.tomexpression.types.expression.ConsOrConnector.make( l1.getHeadOrConnector() ,tom_append_list_OrConnector( l1.getTailOrConnector() ,l2)) ;       }     } else {       return  tom.engine.adt.tomexpression.types.expression.ConsOrConnector.make(l1,l2) ;     }   }   private static   tom.engine.adt.tomexpression.types.Expression  tom_get_slice_OrConnector( tom.engine.adt.tomexpression.types.Expression  begin,  tom.engine.adt.tomexpression.types.Expression  end, tom.engine.adt.tomexpression.types.Expression  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyOrConnector()  ||  (end== tom.engine.adt.tomexpression.types.expression.EmptyOrConnector.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomexpression.types.expression.ConsOrConnector.make((( ((begin instanceof tom.engine.adt.tomexpression.types.expression.ConsOrConnector) || (begin instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrConnector)) )? begin.getHeadOrConnector() :begin),( tom.engine.adt.tomexpression.types.Expression )tom_get_slice_OrConnector((( ((begin instanceof tom.engine.adt.tomexpression.types.expression.ConsOrConnector) || (begin instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrConnector)) )? begin.getTailOrConnector() : tom.engine.adt.tomexpression.types.expression.EmptyOrConnector.make() ),end,tail)) ;   }      private static   tom.engine.adt.tomoption.types.OptionList  tom_append_list_concOption( tom.engine.adt.tomoption.types.OptionList l1,  tom.engine.adt.tomoption.types.OptionList  l2) {     if( l1.isEmptyconcOption() ) {       return l2;     } else if( l2.isEmptyconcOption() ) {       return l1;     } else if(  l1.getTailconcOption() .isEmptyconcOption() ) {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,l2) ;     } else {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,tom_append_list_concOption( l1.getTailconcOption() ,l2)) ;     }   }   private static   tom.engine.adt.tomoption.types.OptionList  tom_get_slice_concOption( tom.engine.adt.tomoption.types.OptionList  begin,  tom.engine.adt.tomoption.types.OptionList  end, tom.engine.adt.tomoption.types.OptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcOption()  ||  (end== tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( begin.getHeadconcOption() ,( tom.engine.adt.tomoption.types.OptionList )tom_get_slice_concOption( begin.getTailconcOption() ,end,tail)) ;   }          private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Sequence) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) );       } else {         return ( (tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Sequence(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin):new tom.library.sl.Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)) );   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Choice) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ):new tom.library.sl.Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) );       } else {         return ( (tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ):new tom.library.sl.Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Choice(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin):new tom.library.sl.Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):( null )),end,tail)) );   }     private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  v) { return ( ( (( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )==null)?v:new tom.library.sl.Choice(v,( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) ) );}private static  tom.library.sl.Strategy  tom_make_TopDownCollect( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),tom_make_Try(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) ))) ) );}   


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
  private static final String[] generatorsNames = {"SyntacticGenerator","VariadicGenerator","ArrayGenerator"};

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
    //System.out.println("result: " + result);
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
      }}}{if ( (expression instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.ConstraintToExpression) ) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch143NameNumber_freshVar_8= (( tom.engine.adt.tomexpression.types.Expression )expression).getcons() ;if ( (tomMatch143NameNumber_freshVar_8 instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch143NameNumber_freshVar_10= tomMatch143NameNumber_freshVar_8.getPattern() ;boolean tomMatch143NameNumber_freshVar_14= false ;if ( (tomMatch143NameNumber_freshVar_10 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {tomMatch143NameNumber_freshVar_14= true ;} else {if ( (tomMatch143NameNumber_freshVar_10 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {tomMatch143NameNumber_freshVar_14= true ;}}if ((tomMatch143NameNumber_freshVar_14 ==  true )) {


        return  tom.engine.adt.tominstruction.types.instruction.LetRef.make(tomMatch143NameNumber_freshVar_10,  tom.engine.adt.tomexpression.types.expression.TomTermToExpression.make( tomMatch143NameNumber_freshVar_8.getSubject() ) , action) ;
      }}}}}{if ( (expression instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.ConstraintToExpression) ) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch143NameNumber_freshVar_16= (( tom.engine.adt.tomexpression.types.Expression )expression).getcons() ;if ( (tomMatch143NameNumber_freshVar_16 instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch143NameNumber_freshVar_18= tomMatch143NameNumber_freshVar_16.getPattern() ;boolean tomMatch143NameNumber_freshVar_22= false ;if ( (tomMatch143NameNumber_freshVar_18 instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariableStar) ) {tomMatch143NameNumber_freshVar_22= true ;} else {if ( (tomMatch143NameNumber_freshVar_18 instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariable) ) {tomMatch143NameNumber_freshVar_22= true ;}}if ((tomMatch143NameNumber_freshVar_22 ==  true )) {

       
        return action;      
      }}}}}{if ( (expression instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.ConstraintToExpression) ) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch143NameNumber_freshVar_24= (( tom.engine.adt.tomexpression.types.Expression )expression).getcons() ;if ( (tomMatch143NameNumber_freshVar_24 instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) {


        return buildNumericCondition(tomMatch143NameNumber_freshVar_24,action);
      }}}}{if ( (expression instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.DoWhileExpression) ) {


        Instruction subInstruction = generateAutomata( (( tom.engine.adt.tomexpression.types.Expression )expression).getEndExpression() , tom.engine.adt.tominstruction.types.instruction.Nop.make() );
        return  tom.engine.adt.tominstruction.types.instruction.DoWhile.make( tom.engine.adt.tominstruction.types.instruction.UnamedBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(action, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(subInstruction, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ,  (( tom.engine.adt.tomexpression.types.Expression )expression).getLoopCondition() ) ;
      }}}{if ( (expression instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.IfExpression) ) { tom.engine.adt.tomexpression.types.Expression  tomMatch143NameNumber_freshVar_33= (( tom.engine.adt.tomexpression.types.Expression )expression).getThenExpression() ; tom.engine.adt.tomexpression.types.Expression  tomMatch143NameNumber_freshVar_34= (( tom.engine.adt.tomexpression.types.Expression )expression).getElseExpression() ;if ( (tomMatch143NameNumber_freshVar_33 instanceof tom.engine.adt.tomexpression.types.expression.EqualTerm) ) {if ( (tomMatch143NameNumber_freshVar_34 instanceof tom.engine.adt.tomexpression.types.expression.EqualTerm) ) {


        return  tom.engine.adt.tominstruction.types.instruction.If.make( (( tom.engine.adt.tomexpression.types.Expression )expression).getCondition() ,  tom.engine.adt.tominstruction.types.instruction.Assign.make( tomMatch143NameNumber_freshVar_33.getKid1() ,  tom.engine.adt.tomexpression.types.expression.TomTermToExpression.make( tomMatch143NameNumber_freshVar_33.getKid2() ) ) ,  tom.engine.adt.tominstruction.types.instruction.Assign.make( tomMatch143NameNumber_freshVar_34.getKid1() ,  tom.engine.adt.tomexpression.types.expression.TomTermToExpression.make( tomMatch143NameNumber_freshVar_34.getKid2() ) ) ) ;
      }}}}}{if ( (expression instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( (((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction) || ((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction)) ) {


        return buildExpressionDisjunction((( tom.engine.adt.tomexpression.types.Expression )expression),action);
      }}}{if ( (expression instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.AntiMatchExpression) ) {


        return buildAntiMatchInstruction( (( tom.engine.adt.tomexpression.types.Expression )expression).getExpression() ,action);
      }}}{if ( (expression instanceof tom.engine.adt.tomexpression.types.Expression) ) {


        return  tom.engine.adt.tominstruction.types.instruction.If.make((( tom.engine.adt.tomexpression.types.Expression )expression), action,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ;
      }}}

    throw new TomRuntimeException("ConstraintGenerator.generateAutomata - strange expression:" + expression);
  }
 
  /**
   * Converts 'Subterm' to 'GetSlot'
   */
  public static class ReplaceSubterms extends tom.library.sl.AbstractBasicStrategy {private  ConstraintGenerator  cg;public ReplaceSubterms( ConstraintGenerator  cg) {super(( new tom.library.sl.Identity() ));this.cg=cg;}public  ConstraintGenerator  getcg() {return cg;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.Subterm) ) { tom.engine.adt.tomname.types.TomName  tomMatch144NameNumber_freshVar_1= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstName() ;if ( (tomMatch144NameNumber_freshVar_1 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.tomname.types.TomName  tom_slotName= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getSlotName() ;


        TomSymbol tomSymbol = cg.getCompiler().getSymbolTable().getSymbolFromName( tomMatch144NameNumber_freshVar_1.getString() );
        TomType subtermType = TomBase.getSlotType(tomSymbol, tom_slotName);	        	
        return  tom.engine.adt.tomterm.types.tomterm.ExpressionToTomTerm.make( tom.engine.adt.tomexpression.types.expression.GetSlot.make(subtermType, tomMatch144NameNumber_freshVar_1, tom_slotName.getString(),  (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getGroundTerm() ) ) ;
      }}}}}return _visit_TomTerm(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!((environment ==  null ))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_ReplaceSubterms( ConstraintGenerator  t0) { return new ReplaceSubterms(t0);}


  

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
    TomTerm flag = getCompiler().getFreshVariable(getCompiler().getSymbolTable().getBooleanType());
    Instruction assignFlagTrue =  tom.engine.adt.tominstruction.types.instruction.Assign.make(flag,  tom.engine.adt.tomexpression.types.expression.TrueTL.make() ) ;
    Collection<TomTerm> freshVarList = new HashSet<TomTerm>();
    // collect variables    
    tom_make_TopDown(tom_make_CollectVar(freshVarList)).visitLight(orDisjunction);    
    Instruction instruction = buildDisjunctionIfElse(orDisjunction,assignFlagTrue);
    // add the final test
    instruction =  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(instruction, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.EqualTerm.make(getCompiler().getSymbolTable().getBooleanType(), flag,  tom.engine.adt.tomterm.types.tomterm.ExpressionToTomTerm.make( tom.engine.adt.tomexpression.types.expression.TrueTL.make() ) ) , action,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) 
;    
    // add fresh variables' declarations
    for(TomTerm var:freshVarList) {
      instruction =  tom.engine.adt.tominstruction.types.instruction.LetRef.make(var,  tom.engine.adt.tomexpression.types.expression.Bottom.make(var.getAstType()) , instruction) ;
    }
    // stick the flag declaration also
    return  tom.engine.adt.tominstruction.types.instruction.LetRef.make(flag,  tom.engine.adt.tomexpression.types.expression.FalseTL.make() , instruction) ;
  }

  private Instruction buildDisjunctionIfElse(Expression orDisjunction,Instruction assignFlagTrue)
      throws VisitFailure {    
    {{if ( (orDisjunction instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( (((( tom.engine.adt.tomexpression.types.Expression )orDisjunction) instanceof tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction) || ((( tom.engine.adt.tomexpression.types.Expression )orDisjunction) instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction)) ) {if ( (  (( tom.engine.adt.tomexpression.types.Expression )orDisjunction).isEmptyOrExpressionDisjunction()  ||  ((( tom.engine.adt.tomexpression.types.Expression )orDisjunction)== tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction.make() )  ) ) {

        return  tom.engine.adt.tominstruction.types.instruction.Nop.make() ;
      }}}}{if ( (orDisjunction instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( (((( tom.engine.adt.tomexpression.types.Expression )orDisjunction) instanceof tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction) || ((( tom.engine.adt.tomexpression.types.Expression )orDisjunction) instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction)) ) {if (!( (  (( tom.engine.adt.tomexpression.types.Expression )orDisjunction).isEmptyOrExpressionDisjunction()  ||  ((( tom.engine.adt.tomexpression.types.Expression )orDisjunction)== tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction.make() )  ) )) { tom.engine.adt.tomexpression.types.Expression  tomMatch146NameNumber_freshVar_8=(( (((( tom.engine.adt.tomexpression.types.Expression )orDisjunction) instanceof tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction) || ((( tom.engine.adt.tomexpression.types.Expression )orDisjunction) instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction)) )?( (( tom.engine.adt.tomexpression.types.Expression )orDisjunction).getHeadOrExpressionDisjunction() ):((( tom.engine.adt.tomexpression.types.Expression )orDisjunction)));if ( (tomMatch146NameNumber_freshVar_8 instanceof tom.engine.adt.tomexpression.types.expression.And) ) {
        
        Instruction subtest = buildDisjunctionIfElse(tom_append_list_OrExpressionDisjunction((( (((( tom.engine.adt.tomexpression.types.Expression )orDisjunction) instanceof tom.engine.adt.tomexpression.types.expression.ConsOrExpressionDisjunction) || ((( tom.engine.adt.tomexpression.types.Expression )orDisjunction) instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction)) )?( (( tom.engine.adt.tomexpression.types.Expression )orDisjunction).getTailOrExpressionDisjunction() ):( tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction.make() )), tom.engine.adt.tomexpression.types.expression.EmptyOrExpressionDisjunction.make() ),assignFlagTrue);
        return  tom.engine.adt.tominstruction.types.instruction.If.make( tomMatch146NameNumber_freshVar_8.getArg1() ,  tom.engine.adt.tominstruction.types.instruction.UnamedBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(assignFlagTrue, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(generateAutomata( tomMatch146NameNumber_freshVar_8.getArg2() , tom.engine.adt.tominstruction.types.instruction.Nop.make() ), tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) , subtest) ;
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
    TomTerm flag = getCompiler().getFreshVariable(getCompiler().getSymbolTable().getBooleanType());    
    Instruction assignFlagTrue =  tom.engine.adt.tominstruction.types.instruction.Assign.make(flag,  tom.engine.adt.tomexpression.types.expression.TrueTL.make() ) ;
    Instruction automata = generateAutomata(expression, assignFlagTrue);    
    // add the final test
    Instruction result =  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(automata, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.EqualTerm.make(getCompiler().getSymbolTable().getBooleanType(), flag,  tom.engine.adt.tomterm.types.tomterm.ExpressionToTomTerm.make( tom.engine.adt.tomexpression.types.expression.FalseTL.make() ) ) , action,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) 
;
    return  tom.engine.adt.tominstruction.types.instruction.LetRef.make(flag,  tom.engine.adt.tomexpression.types.expression.FalseTL.make() , result) ;
  }

  /**
   * Collect the variables in a match constraint
   */
  public static class CollectVar extends tom.library.sl.AbstractBasicStrategy {private  java.util.Collection  varList;public CollectVar( java.util.Collection  varList) {super(( new tom.library.sl.Identity() ));this.varList=varList;}public  java.util.Collection  getvarList() {return varList;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch147NameNumber_freshVar_1= (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getPattern() ;if ( (tomMatch147NameNumber_freshVar_1 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomterm.types.TomTerm  tom_v=tomMatch147NameNumber_freshVar_1;


        if(!varList.contains(tom_v)) { varList.add(tom_v); }        
      }}}}}return _visit_Constraint(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  _visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomconstraint.types.Constraint )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {return ((T)visit_Constraint((( tom.engine.adt.tomconstraint.types.Constraint )v),introspector));}if (!((environment ==  null ))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_CollectVar( java.util.Collection  t0) { return new CollectVar(t0);}

// end strategy   
  
  /**
   * Collect the free variables in an expression (do not inspect under a anti)  
   */
  public static class CollectFreeVar extends tom.library.sl.AbstractBasicStrategy {private  java.util.Collection  varList;public CollectFreeVar( java.util.Collection  varList) {super(( new tom.library.sl.Identity() ));this.varList=varList;}public  java.util.Collection  getvarList() {return varList;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomexpression.types.Expression  visit_Expression( tom.engine.adt.tomexpression.types.Expression  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.ConstraintToExpression) ) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch148NameNumber_freshVar_1= (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getcons() ;if ( (tomMatch148NameNumber_freshVar_1 instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch148NameNumber_freshVar_3= tomMatch148NameNumber_freshVar_1.getPattern() ;if ( (tomMatch148NameNumber_freshVar_3 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomterm.types.TomTerm  tom_v=tomMatch148NameNumber_freshVar_3;


        if(!varList.contains(tom_v)) { varList.add(tom_v); }     
        throw new VisitFailure();
      }}}}}{if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.AntiMatchExpression) ) {
        
        throw new VisitFailure();
      }}}}return _visit_Expression(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tomexpression.types.Expression  _visit_Expression( tom.engine.adt.tomexpression.types.Expression  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomexpression.types.Expression )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomexpression.types.Expression) ) {return ((T)visit_Expression((( tom.engine.adt.tomexpression.types.Expression )v),introspector));}if (!((environment ==  null ))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_CollectFreeVar( java.util.Collection  t0) { return new CollectFreeVar(t0);}


  
  /**
   * check that the list is empty
   * when domain=codomain, the test is extended to:
   *   is_empty(l) || l==make_empty()
   *   this is needed because get_tail() may return the neutral element 
   */ 
  public Expression genIsEmptyList(TomName opName, TomTerm var) {
    TomSymbol tomSymbol = getCompiler().getSymbolTable().getSymbolFromName(opName.getString());
    TomType domain = TomBase.getSymbolDomain(tomSymbol).getHeadconcTomType();
    TomType codomain = TomBase.getSymbolCodomain(tomSymbol);
    if(domain==codomain) {
      return  tom.engine.adt.tomexpression.types.expression.Or.make( tom.engine.adt.tomexpression.types.expression.IsEmptyList.make(opName, var) ,  tom.engine.adt.tomexpression.types.expression.EqualTerm.make(codomain, var,  tom.engine.adt.tomterm.types.tomterm.BuildEmptyList.make(opName) ) ) ;
    }
    return  tom.engine.adt.tomexpression.types.expression.IsEmptyList.make(opName, var) ;
  }
  
  private Instruction buildNumericCondition(Constraint c, Instruction action) {
    {{if ( (c instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )c) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) { tom.engine.adt.tomterm.types.TomTerm  tom_left= (( tom.engine.adt.tomconstraint.types.Constraint )c).getPattern() ; tom.engine.adt.tomterm.types.TomTerm  tom_right= (( tom.engine.adt.tomconstraint.types.Constraint )c).getSubject() ; tom.engine.adt.tomconstraint.types.NumericConstraintType  tom_type= (( tom.engine.adt.tomconstraint.types.Constraint )c).getType() ;
        
        Expression leftExpr =  tom.engine.adt.tomexpression.types.expression.TomTermToExpression.make(tom_left) ;
        Expression rightExpr =  tom.engine.adt.tomexpression.types.expression.TomTermToExpression.make(tom_right) ;
        {{if ( (tom_left instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_left) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch150NameNumber_freshVar_1= (( tom.engine.adt.tomterm.types.TomTerm )tom_left).getOption() ; tom.engine.adt.tomname.types.TomNameList  tomMatch150NameNumber_freshVar_2= (( tom.engine.adt.tomterm.types.TomTerm )tom_left).getNameList() ;if ( ((tomMatch150NameNumber_freshVar_1 instanceof tom.engine.adt.tomoption.types.optionlist.ConsconcOption) || (tomMatch150NameNumber_freshVar_1 instanceof tom.engine.adt.tomoption.types.optionlist.EmptyconcOption)) ) { tom.engine.adt.tomoption.types.OptionList  tomMatch150NameNumber_end_7=tomMatch150NameNumber_freshVar_1;do {{if (!( tomMatch150NameNumber_end_7.isEmptyconcOption() )) {if ( ( tomMatch150NameNumber_end_7.getHeadconcOption()  instanceof tom.engine.adt.tomoption.types.option.Constant) ) {if ( ((tomMatch150NameNumber_freshVar_2 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch150NameNumber_freshVar_2 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch150NameNumber_freshVar_2.isEmptyconcTomName() )) {if (  tomMatch150NameNumber_freshVar_2.getTailconcTomName() .isEmptyconcTomName() ) {

            leftExpr =  tom.engine.adt.tomexpression.types.expression.TomTermToExpression.make( tom.engine.adt.tomterm.types.tomterm.BuildConstant.make( tomMatch150NameNumber_freshVar_2.getHeadconcTomName() ) ) ;            
          }}}}}if ( tomMatch150NameNumber_end_7.isEmptyconcOption() ) {tomMatch150NameNumber_end_7=tomMatch150NameNumber_freshVar_1;} else {tomMatch150NameNumber_end_7= tomMatch150NameNumber_end_7.getTailconcOption() ;}}} while(!( (tomMatch150NameNumber_end_7==tomMatch150NameNumber_freshVar_1) ));}}}}}{{if ( (tom_type instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )tom_type) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumLessThan) ) {


 return  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.LessThan.make(leftExpr, rightExpr) , action,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ;}}}{if ( (tom_type instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )tom_type) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumLessOrEqualThan) ) {
 return  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.LessOrEqualThan.make(leftExpr, rightExpr) , action,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ;}}}{if ( (tom_type instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )tom_type) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumGreaterThan) ) {
 return  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.GreaterThan.make(leftExpr, rightExpr) , action,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ;}}}{if ( (tom_type instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )tom_type) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumGreaterOrEqualThan) ) {
 return  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.GreaterOrEqualThan.make(leftExpr, rightExpr) , action,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ;}}}{if ( (tom_type instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )tom_type) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumEqual) ) {
 TomType tomType = getCompiler().getTermTypeFromTerm(tom_left);
                                         return  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.EqualTerm.make(tomType, tom_right, tom_left) , action,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ; }}}{if ( (tom_type instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )tom_type) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumDifferent) ) {
 TomType tomType = getCompiler().getTermTypeFromTerm(tom_left);
                                         return  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.Negation.make( tom.engine.adt.tomexpression.types.expression.EqualTerm.make(tomType, tom_right, tom_left) ) , action,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) ; }}}}

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
    TomTerm flag = getCompiler().getFreshVariable(getCompiler().getSymbolTable().getBooleanType());
    Instruction assignFlagTrue =  tom.engine.adt.tominstruction.types.instruction.Assign.make(flag,  tom.engine.adt.tomexpression.types.expression.TrueTL.make() ) ;
    TomType intType = getCompiler().getSymbolTable().getIntType();
    TomTerm counter = getCompiler().getFreshVariable(intType);    
    // build the ifs
    Instruction instruction = buildTestsInConstraintDisjuction(0,assignFlagTrue,counter,intType,orConnector);    
    // add the final test
    instruction =  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(instruction, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.EqualTerm.make(getCompiler().getSymbolTable().getBooleanType(), flag,  tom.engine.adt.tomterm.types.tomterm.ExpressionToTomTerm.make( tom.engine.adt.tomexpression.types.expression.TrueTL.make() ) ) , action,  tom.engine.adt.tominstruction.types.instruction.Nop.make() ) , tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) 
;
    // counter++ : expression at the end of the loop 
    Instruction counterIncrement =  tom.engine.adt.tominstruction.types.instruction.Assign.make(counter,  tom.engine.adt.tomexpression.types.expression.AddOne.make(counter) ) ;
    //  stick the flag declaration and the counterIncrement   
    instruction =  tom.engine.adt.tominstruction.types.instruction.LetRef.make(flag,  tom.engine.adt.tomexpression.types.expression.FalseTL.make() ,  tom.engine.adt.tominstruction.types.instruction.AbstractBlock.make( tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(instruction, tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make(counterIncrement, tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) ) ) ) ;      
    instruction =  tom.engine.adt.tominstruction.types.instruction.DoWhile.make(instruction,  tom.engine.adt.tomexpression.types.expression.LessThan.make( tom.engine.adt.tomexpression.types.expression.TomTermToExpression.make(counter) ,  tom.engine.adt.tomexpression.types.expression.Integer.make(orConnector.length()) ) ) ;    
    // add fresh variables' declarations
    Collection<TomTerm> freshVarList = new HashSet<TomTerm>();
    // collect free variables    
    tom_make_TopDownCollect(tom_make_CollectFreeVar(freshVarList)).visitLight(orConnector);
    for(TomTerm var:freshVarList) {
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
      TomTerm counter, TomType intType, Expression orConnector) throws VisitFailure {
    {{if ( (orConnector instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( (((( tom.engine.adt.tomexpression.types.Expression )orConnector) instanceof tom.engine.adt.tomexpression.types.expression.ConsOrConnector) || ((( tom.engine.adt.tomexpression.types.Expression )orConnector) instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrConnector)) ) {if (!( (  (( tom.engine.adt.tomexpression.types.Expression )orConnector).isEmptyOrConnector()  ||  ((( tom.engine.adt.tomexpression.types.Expression )orConnector)== tom.engine.adt.tomexpression.types.expression.EmptyOrConnector.make() )  ) )) {
        
        return  tom.engine.adt.tominstruction.types.instruction.If.make( tom.engine.adt.tomexpression.types.expression.And.make( tom.engine.adt.tomexpression.types.expression.GreaterOrEqualThan.make( tom.engine.adt.tomexpression.types.expression.TomTermToExpression.make(counter) ,  tom.engine.adt.tomexpression.types.expression.Integer.make(cnt) ) ,  tom.engine.adt.tomexpression.types.expression.LessOrEqualThan.make( tom.engine.adt.tomexpression.types.expression.TomTermToExpression.make(counter) ,  tom.engine.adt.tomexpression.types.expression.Integer.make(cnt) ) ) , generateAutomata((( (((( tom.engine.adt.tomexpression.types.Expression )orConnector) instanceof tom.engine.adt.tomexpression.types.expression.ConsOrConnector) || ((( tom.engine.adt.tomexpression.types.Expression )orConnector) instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrConnector)) )?( (( tom.engine.adt.tomexpression.types.Expression )orConnector).getHeadOrConnector() ):((( tom.engine.adt.tomexpression.types.Expression )orConnector))),assignFlagTrue), buildTestsInConstraintDisjuction(cnt,assignFlagTrue,counter,intType,tom_append_list_OrConnector((( (((( tom.engine.adt.tomexpression.types.Expression )orConnector) instanceof tom.engine.adt.tomexpression.types.expression.ConsOrConnector) || ((( tom.engine.adt.tomexpression.types.Expression )orConnector) instanceof tom.engine.adt.tomexpression.types.expression.EmptyOrConnector)) )?( (( tom.engine.adt.tomexpression.types.Expression )orConnector).getTailOrConnector() ):( tom.engine.adt.tomexpression.types.expression.EmptyOrConnector.make() )), tom.engine.adt.tomexpression.types.expression.EmptyOrConnector.make() ))) 


;        
      }}}}}

    return  tom.engine.adt.tominstruction.types.instruction.Nop.make() ;
  }
}
