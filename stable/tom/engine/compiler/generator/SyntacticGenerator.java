
























package tom.engine.compiler.generator;



import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomterm.types.tomterm.*;
import tom.engine.adt.code.types.*;
import tom.library.sl.*;
import tom.engine.tools.SymbolTable;
import tom.engine.exception.TomRuntimeException;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.TomBase;
import tom.engine.compiler.*;
import tom.engine.adt.theory.types.*;
import tom.engine.compiler.Compiler;






public class SyntacticGenerator implements IBaseGenerator {

     private static   tom.engine.adt.tomname.types.TomNameList  tom_append_list_concTomName( tom.engine.adt.tomname.types.TomNameList l1,  tom.engine.adt.tomname.types.TomNameList  l2) {     if( l1.isEmptyconcTomName() ) {       return l2;     } else if( l2.isEmptyconcTomName() ) {       return l1;     } else if(  l1.getTailconcTomName() .isEmptyconcTomName() ) {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,l2) ;     } else {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,tom_append_list_concTomName( l1.getTailconcTomName() ,l2)) ;     }   }   private static   tom.engine.adt.tomname.types.TomNameList  tom_get_slice_concTomName( tom.engine.adt.tomname.types.TomNameList  begin,  tom.engine.adt.tomname.types.TomNameList  end, tom.engine.adt.tomname.types.TomNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomName()  ||  (end== tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( begin.getHeadconcTomName() ,( tom.engine.adt.tomname.types.TomNameList )tom_get_slice_concTomName( begin.getTailconcTomName() ,end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ): null ),end,tail)) ;   }   private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) , tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ), null ) ) , null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ), null ) ) , null ) ) ) ));}private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return (  tom.library.sl.Choice.make(s, tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) )  );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.MuVar("_x") ), null ) ) , tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}  private static Strategy makeTopDownWhenExpression(Strategy s) {   return ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( makeWhenExpression( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) )) );  }    public static class WhenExpression extends tom.library.sl.AbstractStrategyBasic {    private  tom.library.sl.Strategy  s;      public WhenExpression( tom.library.sl.Strategy  s) {     super(( new tom.library.sl.Identity() ));     this.s=s;   }      public  tom.library.sl.Strategy  gets() {     return s;   }    public tom.library.sl.Visitable[] getChildren() {     tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];     stratChildren[0] = super.getChildAt(0);     stratChildren[1] = gets();     return stratChildren;   }    public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {     super.setChildAt(0, children[0]);     s = ( tom.library.sl.Strategy ) children[1];     return this;   }    public int getChildCount() {     return 2;   }    public tom.library.sl.Visitable getChildAt(int index) {     switch (index) {       case 0: return super.getChildAt(0);       case 1: return gets();       default: throw new IndexOutOfBoundsException();      }   }    public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {     switch (index) {       case 0: return super.setChildAt(0, child);       case 1: s = ( tom.library.sl.Strategy )child;               return this;       default: throw new IndexOutOfBoundsException();     }   }    public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {     if ( (v instanceof tom.engine.adt.tomexpression.types.Expression) ) {       return s.visitLight(v,introspector);     }     return any.visitLight(v,introspector);   }    }    private static  tom.library.sl.Strategy  makeWhenExpression( tom.library.sl.Strategy  t0) { return new WhenExpression(t0);}           private static Strategy makeTopDownWhenExprConstrOrTerm(Strategy s) {   return ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( makeWhenExprConstrOrTerm( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) )) );  }    public static class WhenExprConstrOrTerm extends tom.library.sl.AbstractStrategyBasic {    private  tom.library.sl.Strategy  s;      public WhenExprConstrOrTerm( tom.library.sl.Strategy  s) {     super(( new tom.library.sl.Identity() ));     this.s=s;   }      public  tom.library.sl.Strategy  gets() {     return s;   }    public tom.library.sl.Visitable[] getChildren() {     tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];     stratChildren[0] = super.getChildAt(0);     stratChildren[1] = gets();     return stratChildren;   }    public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {     super.setChildAt(0, children[0]);     s = ( tom.library.sl.Strategy ) children[1];     return this;   }    public int getChildCount() {     return 2;   }    public tom.library.sl.Visitable getChildAt(int index) {     switch (index) {       case 0: return super.getChildAt(0);       case 1: return gets();       default: throw new IndexOutOfBoundsException();      }   }    public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {     switch (index) {       case 0: return super.setChildAt(0, child);       case 1: s = ( tom.library.sl.Strategy )child;               return this;       default: throw new IndexOutOfBoundsException();     }   }    public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {     if (v instanceof tom.engine.adt.tomexpression.types.Expression ||          v instanceof tom.engine.adt.code.types.BQTerm ||          v instanceof tom.engine.adt.tomterm.types.TomTerm ||          v instanceof tom.engine.adt.tomconstraint.types.Constraint) {       return s.visitLight(v,introspector);     }     return any.visitLight(v,introspector);   }    }    private static  tom.library.sl.Strategy  makeWhenExprConstrOrTerm( tom.library.sl.Strategy  t0) { return new WhenExprConstrOrTerm(t0);}     








  private Compiler compiler; 
  private ConstraintGenerator constraintGenerator; 
 
  public SyntacticGenerator(Compiler myCompiler, ConstraintGenerator myConstraintGenerator) {
    this.compiler = myCompiler;
    this.constraintGenerator = myConstraintGenerator; 
  }

  public Compiler getCompiler() {
    return this.compiler;
  }
 
  public ConstraintGenerator getConstraintGenerator() {
    return this.constraintGenerator;
  }
 
  public Expression generate(Expression expression) throws VisitFailure {
    return  ( makeTopDownWhenExpression( new Generator(this) ) ).visitLight(expression);
  }

  
  public static class Generator extends tom.library.sl.AbstractStrategyBasic {private  SyntacticGenerator  sg;public Generator( SyntacticGenerator  sg) {super(( new tom.library.sl.Identity() ));this.sg=sg;}public  SyntacticGenerator  getsg() {return sg;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomexpression.types.Expression) ) {return ((T)visit_Expression((( tom.engine.adt.tomexpression.types.Expression )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomexpression.types.Expression  _visit_Expression( tom.engine.adt.tomexpression.types.Expression  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( tom.engine.adt.tomexpression.types.Expression )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomexpression.types.Expression  visit_Expression( tom.engine.adt.tomexpression.types.Expression  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{ /* unamed block */{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.ConstraintToExpression) ) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch215_1= (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getcons() ;if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tomMatch215_1) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch215_4= tomMatch215_1.getPattern() ; tom.engine.adt.code.types.BQTerm  tomMatch215_5= tomMatch215_1.getSubject() ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch215_4) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch215_9= tomMatch215_4.getNameList() ;if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch215_9) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch215_9) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch215_9.isEmptyconcTomName() )) {if (  tomMatch215_9.getTailconcTomName() .isEmptyconcTomName() ) {if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch215_5) instanceof tom.engine.adt.code.types.bqterm.SymbolOf) ) {



        
        
        return sg.buildEqualFunctionSymbol( tomMatch215_1.getAstType() , tomMatch215_5.getGroundTerm() , tomMatch215_9.getHeadconcTomName() ,TomBase.getTheory(tomMatch215_4));
      }}}}}}}}}{ /* unamed block */if ( (tom__arg instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )tom__arg) instanceof tom.engine.adt.tomexpression.types.expression.ConstraintToExpression) ) { tom.engine.adt.tomconstraint.types.Constraint  tomMatch215_18= (( tom.engine.adt.tomexpression.types.Expression )tom__arg).getcons() ;if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tomMatch215_18) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch215_21= tomMatch215_18.getPattern() ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch215_21) instanceof tom.engine.adt.tomterm.types.tomterm.TestVar) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch215_25= tomMatch215_21.getVariable() ;boolean tomMatch215_32= false ; tom.engine.adt.tomterm.types.TomTerm  tomMatch215_30= null ; tom.engine.adt.tomtype.types.TomType  tomMatch215_28= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch215_31= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch215_25) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{ /* unamed block */tomMatch215_32= true ;tomMatch215_30=tomMatch215_25;tomMatch215_28= tomMatch215_30.getAstType() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch215_25) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{ /* unamed block */tomMatch215_32= true ;tomMatch215_31=tomMatch215_25;tomMatch215_28= tomMatch215_31.getAstType() ;}}}if (tomMatch215_32) {


        return  tom.engine.adt.tomexpression.types.expression.EqualBQTerm.make(tomMatch215_28,  tomMatch215_18.getSubject() , TomBase.convertFromVarToBQVar(tomMatch215_25)) ;
      }}}}}}}return _visit_Expression(tom__arg,introspector);}}

 
  
  private Expression buildEqualFunctionSymbol(TomType type, BQTerm subject, TomName name, Theory theory) {
    TomSymbol tomSymbol = getCompiler().getSymbolTable().getSymbolFromName(name.getString());
    if(getCompiler().getSymbolTable().isBuiltinType(TomBase.getTomType(type))) {
      if(TomBase.isListOperator(tomSymbol) || TomBase.isArrayOperator(tomSymbol) || TomBase.hasIsFsymDecl(tomSymbol)) {
        return  tom.engine.adt.tomexpression.types.expression.IsFsym.make(name, subject) ;
      } else {
        return  tom.engine.adt.tomexpression.types.expression.EqualBQTerm.make(type,  tom.engine.adt.code.types.bqterm.BuildConstant.make(name) , subject) ;
      }
    } else if(TomBase.hasTheory(theory,  tom.engine.adt.theory.types.elementarytheory.AU.make() )) {
      return  tom.engine.adt.tomexpression.types.expression.IsSort.make(type, subject) ;
    }
    
    return
       tom.engine.adt.tomexpression.types.expression.IsFsym.make(name,  tom.engine.adt.code.types.bqterm.ExpressionToBQTerm.make( tom.engine.adt.tomexpression.types.expression.Cast.make(type,  tom.engine.adt.tomexpression.types.expression.BQTermToExpression.make(subject) ) ) ) ;
      
  }
}
