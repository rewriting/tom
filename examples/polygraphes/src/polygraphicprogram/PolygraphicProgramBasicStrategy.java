
package polygraphicprogram;
import tom.library.sl.*;
   
  public class PolygraphicProgramBasicStrategy implements tom.library.sl.Strategy,PolygraphicProgramVisitor {
  private tom.library.sl.Environment environment;
  protected Strategy any;
  
  public PolygraphicProgramBasicStrategy(Strategy v) {
    this.any = v;
  }
    
  public int getChildCount() {
    return 1;
  }
    
  public Visitable getChildAt(int i) {
    switch (i) {
      case 0: return any;
      default: throw new IndexOutOfBoundsException();
    }
  }
    
  public Visitable setChildAt(int i, Visitable child) {
    switch (i) {
      case 0: any = (Strategy) child; return this;
      default: throw new IndexOutOfBoundsException();
    }
  }

  public Visitable[] getChildren() {
    return new Visitable[]{any};
  }

  public Visitable setChildren(Visitable[] children) {
    any = (Strategy)children[0];
    return this;
  }

  public tom.library.sl.Strategy accept(tom.library.sl.reflective.StrategyFwd v) throws tom.library.sl.VisitFailure {
    return v.visit_Strategy(this);
  } 

  public tom.library.sl.Environment getEnvironment() {
    if(environment!=null) {
      return environment;
    } else {
      throw new java.lang.RuntimeException("environment not initialized");
    }
  }

  public void setEnvironment(tom.library.sl.Environment env) {
    this.environment = env;
  }
 
  public Visitable visit(Environment envt) throws VisitFailure {
    setEnvironment(envt);
    int status = visit();
    if(status == Environment.SUCCESS) {
      return environment.getSubject();
    } else {
      throw new VisitFailure();
    }
  }


  public tom.library.sl.Visitable visit(tom.library.sl.Visitable any) throws VisitFailure {
    tom.library.sl.AbstractStrategy.init(this,new tom.library.sl.Environment());
    environment.setRoot(any);
    int status = visit();
    if(status == tom.library.sl.Environment.SUCCESS) {
      return environment.getRoot();
    } else {
      throw new VisitFailure();
    }
  }

  public int visit() {
    try {
      environment.setSubject(this.visitLight(environment.getSubject()));
      return tom.library.sl.Environment.SUCCESS;
    } catch(VisitFailure f) {
      return tom.library.sl.Environment.FAILURE;
    }
  }

  public Visitable visitLight(Visitable v) throws VisitFailure {
    if (v instanceof polygraphicprogram.PolygraphicProgramAbstractType) {
      return ((polygraphicprogram.PolygraphicProgramAbstractType) v).accept(this);
    }

    else {
      return any.visitLight(v);
    }
  }

  public polygraphicprogram.types.ThreePath visit_ThreePath(polygraphicprogram.types.ThreePath arg) throws VisitFailure {
   if(environment!=null) {
      //TODO: must be removed
      assert(arg.equals(environment.getSubject()));
   return (polygraphicprogram.types.ThreePath) any.visit(environment);
   } else {
    return (polygraphicprogram.types.ThreePath) any.visitLight(arg);
   }
 }

  public polygraphicprogram.types.CellType visit_CellType(polygraphicprogram.types.CellType arg) throws VisitFailure {
   if(environment!=null) {
      //TODO: must be removed
      assert(arg.equals(environment.getSubject()));
   return (polygraphicprogram.types.CellType) any.visit(environment);
   } else {
    return (polygraphicprogram.types.CellType) any.visitLight(arg);
   }
 }

  public polygraphicprogram.types.TwoPath visit_TwoPath(polygraphicprogram.types.TwoPath arg) throws VisitFailure {
   if(environment!=null) {
      //TODO: must be removed
      assert(arg.equals(environment.getSubject()));
   return (polygraphicprogram.types.TwoPath) any.visit(environment);
   } else {
    return (polygraphicprogram.types.TwoPath) any.visitLight(arg);
   }
 }

  public polygraphicprogram.types.OnePath visit_OnePath(polygraphicprogram.types.OnePath arg) throws VisitFailure {
   if(environment!=null) {
      //TODO: must be removed
      assert(arg.equals(environment.getSubject()));
   return (polygraphicprogram.types.OnePath) any.visit(environment);
   } else {
    return (polygraphicprogram.types.OnePath) any.visitLight(arg);
   }
 }

}
