/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2008, INRIA
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
package tom.engine.compiler.propagator;

import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomterm.types.tomterm.*;
import tom.library.sl.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.compiler.*;
import tom.engine.TomBase;
import tom.engine.exception.TomRuntimeException;
import java.util.ArrayList;
import tom.engine.compiler.Compiler;

/**
 * AC propagator
 */
public class ACPropagator implements IBasePropagator {

//--------------------------------------------------------	
        private static   tom.engine.adt.tomname.types.TomNameList  tom_append_list_concTomName( tom.engine.adt.tomname.types.TomNameList l1,  tom.engine.adt.tomname.types.TomNameList  l2) {     if( l1.isEmptyconcTomName() ) {       return l2;     } else if( l2.isEmptyconcTomName() ) {       return l1;     } else if(  l1.getTailconcTomName() .isEmptyconcTomName() ) {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,l2) ;     } else {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,tom_append_list_concTomName( l1.getTailconcTomName() ,l2)) ;     }   }   private static   tom.engine.adt.tomname.types.TomNameList  tom_get_slice_concTomName( tom.engine.adt.tomname.types.TomNameList  begin,  tom.engine.adt.tomname.types.TomNameList  end, tom.engine.adt.tomname.types.TomNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomName()  ||  (end== tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( begin.getHeadconcTomName() ,( tom.engine.adt.tomname.types.TomNameList )tom_get_slice_concTomName( begin.getTailconcTomName() ,end,tail)) ;   }         private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Sequence) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) );       } else {         return ( (tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Sequence(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin):new tom.library.sl.Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)) );   }       private static  tom.library.sl.Strategy  tom_make_BottomUp( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?v:new tom.library.sl.Sequence(v,( null )) )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( (( null )==null)?v:new tom.library.sl.Sequence(v,( null )) )) )) ) );}   

//--------------------------------------------------------

  




  private Compiler compiler;  
  private ConstraintPropagator constraintPropagator; 
 
  public Compiler getCompiler() {
    return this.compiler;
  }
 
  public ACPropagator(Compiler compiler, ConstraintPropagator constraintPropagator) {
    this.compiler = compiler;
    this.constraintPropagator = constraintPropagator;
  }

  public Constraint propagate(Constraint constraint) throws VisitFailure {
    return tom_make_BottomUp(tom_make_ACMatching(this)).visitLight(constraint);		
  }	

  public static class ACMatching extends tom.library.sl.AbstractStrategyBasic {private  ACPropagator  acp;public ACMatching( ACPropagator  acp) {super(( new tom.library.sl.Identity() ));this.acp=acp;}public  ACPropagator  getacp() {return acp;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch138NameNumber_freshVar_1= (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getPattern() ;if ( (tomMatch138NameNumber_freshVar_1 instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch138NameNumber_freshVar_4= tomMatch138NameNumber_freshVar_1.getNameList() ;if ( ((tomMatch138NameNumber_freshVar_4 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch138NameNumber_freshVar_4 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch138NameNumber_freshVar_4.isEmptyconcTomName() )) {if ( ( tomMatch138NameNumber_freshVar_4.getHeadconcTomName()  instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch138NameNumber_freshVar_4.getTailconcTomName() .isEmptyconcTomName() ) {





        //decompose the pattern to only f(X*,Y*) matching constraints
        //return acp.decompose(`c); 
      }}}}}}}}}return _visit_Constraint(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  _visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomconstraint.types.Constraint )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {return ((T)visit_Constraint((( tom.engine.adt.tomconstraint.types.Constraint )v),introspector));}if (!((environment ==  null ))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_ACMatching( ACPropagator  t0) { return new ACMatching(t0);}



  /*
  public Constraint decompose(Constraint c) {
    %match(c) {
      MatchConstraint(pattern@RecordAppl[Option=option, NameList=(name@Name(tomName)), Slots=slots],subject) -> {
        %match(slots) {
          concSlot(slot@!PairSlotAppl[Appl=VariableStar[]],tail*) -> {

            // get fresh variables
            TomType listType = getCompiler().getTermTypeFromTerm(`pattern);
            TomTerm X1 = getCompiler().getFreshVariableStar(listType);				
            TomName X1_Name = X1.getAstName();				
            TomTerm X2 = getCompiler().getFreshVariableStar(listType);
            TomName X2_Name = X2.getAstName();				

            //generate: f(X1,X2) << subject && f(slot) << X1 && decompose(f(tail) << X2) 
            Constraint c1 = 
              `ACMatchConstraint(RecordAppl(option,
                    concTomName(Name(tomName)),concSlot(PairSlotAppl(X1_Name,X1),PairSlotAppl(X2_Name,X2)),
                    concConstraint()),subject);
            Constraint c2 = 
              `MatchConstraint(RecordAppl(option,
                    concTomName(Name(tomName)),concSlot(slot),concConstraint()),X1);
            Constraint c3 = decompose(`MatchConstraint(RecordAppl(option,
                    concTomName(Name(tomName)),tail,concConstraint()),X2));
            return `AndConstraint(c1, c2, c3);
          }

          concSlot(vstar@PairSlotAppl[Appl=VariableStar[]], tail*) -> {
            // get fresh variables
            TomType listType = getCompiler().getTermTypeFromTerm(`pattern);
            TomTerm X1 = getCompiler().getFreshVariableStar(listType);				
            TomName X1_Name = X1.getAstName();				

            //generate: f(vstar,X1) << subject && decompose(f(tail) << X1) 
            Constraint c1 = 
              `ACMatchConstraint(RecordAppl(option,
                    concTomName(Name(tomName)),concSlot(vstar,PairSlotAppl(X1_Name,X1)),
                    concConstraint()),subject);
            Constraint c2 = decompose(`MatchConstraint(RecordAppl(option,
                    concTomName(Name(tomName)),tail,concConstraint()),X1));
            System.out.println(tom.engine.tools.TomConstraintPrettyPrinter.prettyPrint(`AndConstraint(c1, c2)));
            return `AndConstraint(c1, c2);
          }
          concSlot() -> {
            return `EmptyListConstraint(name,subject);
          }
        }
      }
    }
    return c; 
  }
  */

}
