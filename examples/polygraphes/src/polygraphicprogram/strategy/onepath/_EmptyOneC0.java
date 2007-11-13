
package polygraphicprogram.strategy.onepath;

public class _EmptyOneC0 implements tom.library.sl.Strategy {
  private static final String msg = "Not an EmptyOneC0";
  /* Manage an internal environment */
  protected tom.library.sl.Environment environment;
  
  public void setEnvironment(tom.library.sl.Environment env) {
    this.environment = env;
  }

  public tom.library.sl.Environment getEnvironment() {
    if(environment!=null) {
      return environment;
    } else {
      throw new RuntimeException("environment not initialized");
    }
  }

  private tom.library.sl.Strategy[] args;

  public tom.library.sl.Strategy getArgument(int i) {
    return args[i];
  }
  public void setArgument(int i, tom.library.sl.Strategy child) {
    args[i]= child;
  }
  public int getChildCount() {
    return args.length;
  }
  public tom.library.sl.Visitable getChildAt(int i) {
      return args[i];
  }
  public tom.library.sl.Visitable setChildAt(int i, tom.library.sl.Visitable child) {
    args[i]= (tom.library.sl.Strategy) child;
    return this;
  }

  public tom.library.sl.Visitable[] getChildren() {
    return args.clone();
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
    tom.library.sl.Strategy[] newArgs = new tom.library.sl.Strategy[children.length];
    for(int i = 0; i < children.length; i++) {
      newArgs[i] = (tom.library.sl.Strategy) children[i];
    }
    args = newArgs;
    return this;
  }

  public tom.library.sl.Visitable visit(tom.library.sl.Visitable any) throws tom.library.sl.VisitFailure {
    tom.library.sl.AbstractStrategy.init(this,new tom.library.sl.Environment());
    environment.setRoot(any);
    int status = visit();
    if(status == tom.library.sl.Environment.SUCCESS) {
      return environment.getRoot();
    } else {
      throw new tom.library.sl.VisitFailure();
    }
  }

  public tom.library.sl.Strategy accept(tom.library.sl.reflective.StrategyFwd v) throws tom.library.sl.VisitFailure {
    return v.visit_Strategy(this);
  }


  private static boolean[] nonbuiltin = new boolean[]{};
  public _EmptyOneC0() {
    args = new tom.library.sl.Strategy[] {};
  }

  public tom.library.sl.Visitable visit(tom.library.sl.Environment envt) throws tom.library.sl.VisitFailure {
    setEnvironment(envt);
    int status = visit();
    if(status == tom.library.sl.Environment.SUCCESS) {
      return environment.getRoot();
    } else {
      throw new tom.library.sl.VisitFailure();
    }
  }

  public tom.library.sl.Visitable visitLight(tom.library.sl.Visitable any) throws tom.library.sl.VisitFailure {
    if(any instanceof polygraphicprogram.types.onepath.EmptyOneC0) {
      tom.library.sl.Visitable result = any;
      tom.library.sl.Visitable[] childs = null;
      for (int i = 0, nbi = 0; i < 0; i++) {
        if(nonbuiltin[i]) {
          tom.library.sl.Visitable oldChild = any.getChildAt(nbi);
          tom.library.sl.Visitable newChild = args[i].visitLight(oldChild);
          if(childs != null) {
            childs[nbi] = newChild;
          } else if(newChild != oldChild) {
            // allocate the array, and fill it
            childs = any.getChildren();
            childs[nbi] = newChild;
          }
          nbi++;
        }
      }
      if(childs!=null) {
        result = any.setChildren(childs);
      }
      return result;
    } else {
      throw new tom.library.sl.VisitFailure(msg);
    }
  }

  public int visit() {
    tom.library.sl.Visitable any = environment.getSubject();
    if(any instanceof polygraphicprogram.types.onepath.EmptyOneC0) {
      tom.library.sl.Visitable[] childs = null;
      for(int i = 0, nbi = 0; i < 0; i++) {
        if(nonbuiltin[i]) {
          tom.library.sl.Visitable oldChild = any.getChildAt(nbi);
          environment.down(nbi+1);
          int status = args[i].visit();
          if(status != tom.library.sl.Environment.SUCCESS) {
            environment.upLocal();
            return status;
          }
          tom.library.sl.Visitable newChild = environment.getSubject();
          if(childs != null) {
            childs[nbi] = newChild;
          } else if(newChild != oldChild) {
            childs = any.getChildren();
            childs[nbi] = newChild;
          } 
          environment.upLocal();
          nbi++;
        }
      }
      if(childs!=null) {
        environment.setSubject(any.setChildren(childs));
      }
      return tom.library.sl.Environment.SUCCESS;
    } else {
      return tom.library.sl.Environment.FAILURE;
    }
  }
}
