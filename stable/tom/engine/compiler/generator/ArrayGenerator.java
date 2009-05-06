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
package tom.engine.compiler.generator;

import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tomexpression.types.expression.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomterm.types.tomterm.*;
import tom.library.sl.*;
import tom.engine.tools.SymbolTable;
import tom.engine.exception.TomRuntimeException;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.TomBase;
import tom.engine.compiler.*;

/**
 * Array Generator
 */
public class ArrayGenerator implements IBaseGenerator{

           private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Sequence) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) );       } else {         return ( (tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Sequence(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin):new tom.library.sl.Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)) );   }          private static Strategy makeTopDownWhenExpression(Strategy s) {   return ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( makeWhenExpression(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?s:new tom.library.sl.Sequence(s,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) )) );  }  public static class WhenExpression extends tom.library.sl.AbstractBasicStrategy {    private  tom.library.sl.Strategy  s;      public WhenExpression( tom.library.sl.Strategy  s) {     super(( new tom.library.sl.Identity() ));     this.s=s;   }      public  tom.library.sl.Strategy  gets() {     return s;   }    public tom.library.sl.Visitable[] getChildren() {     tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];     stratChilds[0] = super.getChildAt(0);     stratChilds[1] = gets();     return stratChilds;   }    public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {     super.setChildAt(0, children[0]);     s = ( tom.library.sl.Strategy ) children[1];     return this;   }    public int getChildCount() {     return 2;   }    public tom.library.sl.Visitable getChildAt(int index) {     switch (index) {       case 0: return super.getChildAt(0);       case 1: return gets();       default: throw new IndexOutOfBoundsException();      }   }    public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {     switch (index) {       case 0: return super.setChildAt(0, child);       case 1: s = ( tom.library.sl.Strategy )child;               return this;       default: throw new IndexOutOfBoundsException();     }   }    public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {     if ( (v instanceof tom.engine.adt.tomexpression.types.Expression) ) {       return s.visitLight(v,introspector);     }     return any.visitLight(v,introspector);   }  }    private static  tom.library.sl.Strategy  makeWhenExpression( tom.library.sl.Strategy  t0) { return new WhenExpression(t0);}           private static Strategy makeTopDownWhenExprConstrOrTerm(Strategy s) {   return ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( makeWhenExprConstrOrTerm(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?s:new tom.library.sl.Sequence(s,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) )) );  }  public static class WhenExprConstrOrTerm extends tom.library.sl.AbstractBasicStrategy {    private  tom.library.sl.Strategy  s;      public WhenExprConstrOrTerm( tom.library.sl.Strategy  s) {     super(( new tom.library.sl.Identity() ));     this.s=s;   }      public  tom.library.sl.Strategy  gets() {     return s;   }    public tom.library.sl.Visitable[] getChildren() {     tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];     stratChilds[0] = super.getChildAt(0);     stratChilds[1] = gets();     return stratChilds;   }    public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {     super.setChildAt(0, children[0]);     s = ( tom.library.sl.Strategy ) children[1];     return this;   }    public int getChildCount() {     return 2;   }    public tom.library.sl.Visitable getChildAt(int index) {     switch (index) {       case 0: return super.getChildAt(0);       case 1: return gets();       default: throw new IndexOutOfBoundsException();      }   }    public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {     switch (index) {       case 0: return super.setChildAt(0, child);       case 1: s = ( tom.library.sl.Strategy )child;               return this;       default: throw new IndexOutOfBoundsException();     }   }    public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {     if (v instanceof tom.engine.adt.tomexpression.types.Expression ||          v instanceof tom.engine.adt.tomterm.types.TomTerm ||          v instanceof tom.engine.adt.tomconstraint.types.Constraint) {       return s.visitLight(v,introspector);     }     return any.visitLight(v,introspector);   }  }    private static  tom.library.sl.Strategy  makeWhenExprConstrOrTerm( tom.library.sl.Strategy  t0) { return new WhenExprConstrOrTerm(t0);}     

	

  private tom.engine.compiler.Compiler compiler;  
  private ConstraintGenerator constraintGenerator; // only present for "compatibility" : cf. ConstraintGenerator.t and look around ".newInstance(this.getCompiler(),this);" 
 
  public ArrayGenerator(tom.engine.compiler.Compiler myCompiler, ConstraintGenerator myConstraintGenerator) {
    this.compiler = myCompiler;
    this.constraintGenerator = myConstraintGenerator; // only present for "compatibility" : cf. ConstraintGenerator.t and look around ".newInstance(this.getCompiler(),this);" 
  }

  public tom.engine.compiler.Compiler getCompiler() {
    return this.compiler;
  }
 
  public ConstraintGenerator getConstraintGenerator() {
    return this.constraintGenerator;
  }
 
  public Expression generate(Expression expression) throws VisitFailure {
    return ( makeTopDownWhenExpression(tom_make_Generator()) ).visitLight(expression);		
  }

  // If we find ConstraintToExpression it means that this constraint was not processed	
  public static class Generator extends tom.library.sl.AbstractBasicStrategy {public Generator() {super(( new tom.library.sl.Identity() ));}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomexpression.types.Expression  visit_Expression( tom.engine.adt.tomexpression.types.Expression  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.ConstraintToExpression) ) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch1NameNumber_freshVar_1= (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getcons() ;if ( (tomMatch1NameNumber_freshVar_1 instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch1NameNumber_freshVar_3= tomMatch1NameNumber_freshVar_1.getPattern() ; tom.engine.adt.tomterm.types.TomTerm  tomMatch1NameNumber_freshVar_4= tomMatch1NameNumber_freshVar_1.getSubject() ;boolean tomMatch1NameNumber_freshVar_12= false ;if ( (tomMatch1NameNumber_freshVar_3 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {tomMatch1NameNumber_freshVar_12= true ;} else {if ( (tomMatch1NameNumber_freshVar_3 instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariableStar) ) {tomMatch1NameNumber_freshVar_12= true ;}}if ((tomMatch1NameNumber_freshVar_12 ==  true )) { tom.engine.adt.tomterm.types.TomTerm  tom_v=tomMatch1NameNumber_freshVar_3;if ( (tomMatch1NameNumber_freshVar_4 instanceof tom.engine.adt.tomterm.types.tomterm.VariableHeadArray) ) { tom.engine.adt.tomname.types.TomName  tom_opName= tomMatch1NameNumber_freshVar_4.getOpname() ; tom.engine.adt.tomterm.types.TomTerm  tom_subject= tomMatch1NameNumber_freshVar_4.getSubject() ; tom.engine.adt.tomterm.types.TomTerm  tom_end= tomMatch1NameNumber_freshVar_4.getEndIndex() ;











        Expression doWhileTest =  tom.engine.adt.tomexpression.types.expression.Negation.make( tom.engine.adt.tomexpression.types.expression.GreaterThan.make( tom.engine.adt.tomexpression.types.expression.TomTermToExpression.make(tom_end) ,  tom.engine.adt.tomexpression.types.expression.GetSize.make(tom_opName, tom_subject) ) ) ;
        // expression at the end of the loop 
        Expression endExpression =  tom.engine.adt.tomexpression.types.expression.ConstraintToExpression.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(tom_end,  tom.engine.adt.tomterm.types.tomterm.ExpressionToTomTerm.make( tom.engine.adt.tomexpression.types.expression.AddOne.make(tom_end) ) ) ) ;        
        // if we have a varStar, then add its declaration also
        if(tom_v.isVariableStar()) {
          Expression varDeclaration =  tom.engine.adt.tomexpression.types.expression.ConstraintToExpression.make( tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(tom_v,  tom.engine.adt.tomterm.types.tomterm.ExpressionToTomTerm.make( tom.engine.adt.tomexpression.types.expression.GetSliceArray.make(tom_opName, tom_subject,  tomMatch1NameNumber_freshVar_4.getBeginIndex() , tom_end) ) ) ) 
;
          return  tom.engine.adt.tomexpression.types.expression.And.make( tom.engine.adt.tomexpression.types.expression.DoWhileExpression.make(endExpression, doWhileTest) , varDeclaration) ;
        }
        return  tom.engine.adt.tomexpression.types.expression.DoWhileExpression.make(endExpression, doWhileTest) ;		        		      
      }}}}}}}return _visit_Expression(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tomexpression.types.Expression  _visit_Expression( tom.engine.adt.tomexpression.types.Expression  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomexpression.types.Expression )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomexpression.types.Expression) ) {return ((T)visit_Expression((( tom.engine.adt.tomexpression.types.Expression )v),introspector));}if (!((environment ==  null ))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_Generator() { return new Generator();}


 // end strategy
  
  /**
   * return an element of the list
   * when domain=codomain, the test is extended to:
   *   is_fsym_f(t)?get_element(t):t 
   *   the element itself is returned when it is not an array operator operator
   *   this occurs because the last element of an array may not be an array
   */ 
  private Expression genGetElement(TomName opName, TomTerm var, Expression getElem) {
    TomSymbol tomSymbol = getCompiler().getSymbolTable().getSymbolFromName(((Name)opName).getString());
    TomType domain = TomBase.getSymbolDomain(tomSymbol).getHeadconcTomType();
    TomType codomain = TomBase.getSymbolCodomain(tomSymbol);
    if(domain==codomain) {
      return  tom.engine.adt.tomexpression.types.expression.Conditional.make( tom.engine.adt.tomexpression.types.expression.IsFsym.make(opName, var) , getElem,  tom.engine.adt.tomexpression.types.expression.TomTermToExpression.make(var) ) ;
    }
    return getElem;
  }
}
