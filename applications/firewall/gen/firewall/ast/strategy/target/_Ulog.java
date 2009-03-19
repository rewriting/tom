
package firewall.ast.strategy.target;

public class _Ulog implements tom.library.sl.Strategy {
  private static final String msg = "Not an Ulog";
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

  public tom.library.sl.Visitable visit(tom.library.sl.Environment envt) throws tom.library.sl.VisitFailure {
    return (tom.library.sl.Visitable) visit(envt,tom.library.sl.VisitableIntrospector.getInstance());
  }

  public tom.library.sl.Visitable visit(tom.library.sl.Visitable any) throws tom.library.sl.VisitFailure{
    return (tom.library.sl.Visitable) visit(any,tom.library.sl.VisitableIntrospector.getInstance());
  }

  public tom.library.sl.Visitable visitLight(tom.library.sl.Visitable any) throws tom.library.sl.VisitFailure {
    return (tom.library.sl.Visitable) visitLight(any,tom.library.sl.VisitableIntrospector.getInstance());
  }

  public Object visit(Object any, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {
    tom.library.sl.AbstractStrategy.init(this,new tom.library.sl.Environment());
    environment.setRoot(any);
    int status = visit(i);
    if(status == tom.library.sl.Environment.SUCCESS) {
      return environment.getRoot();
    } else {
      throw new tom.library.sl.VisitFailure();
    }
  }

  public _Ulog() {
    args = new tom.library.sl.Strategy[] {};
  }

  public Object visit(tom.library.sl.Environment envt, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {
    tom.library.sl.AbstractStrategy.init(this,envt);
    int status = visit(i);
    if(status == tom.library.sl.Environment.SUCCESS) {
      return environment.getSubject();
    } else {
      throw new tom.library.sl.VisitFailure();
    }
  }

  public Object visitLight(Object any, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {
    if(any instanceof firewall.ast.types.target.Ulog) {
      Object result = any;
      Object[] childs = null;
      for (int i = 0, nbi = 0; i < 0; i++) {
          Object oldChild = introspector.getChildAt(any,nbi);
          Object newChild = args[i].visitLight(oldChild,introspector);
          if(childs != null) {
            childs[nbi] = newChild;
          } else if(newChild != oldChild) {
            // allocate the array, and fill it
            childs = introspector.getChildren(any);
            childs[nbi] = newChild;
          }
          nbi++;
      }
      if(childs!=null) {
        result = introspector.setChildren(any,childs);
      }
      return result;
    } else {
      throw new tom.library.sl.VisitFailure(msg);
    }
  }

  public int visit(tom.library.sl.Introspector introspector) {
    Object any = environment.getSubject();
    if(any instanceof firewall.ast.types.target.Ulog) {
      Object[] childs = null;
      for(int i = 0, nbi = 0; i < 0; i++) {
          Object oldChild = introspector.getChildAt(any,nbi);
          environment.down(nbi+1);
          int status = args[i].visit(introspector);
          if(status != tom.library.sl.Environment.SUCCESS) {
            environment.upLocal();
            return status;
          }
          Object newChild = environment.getSubject();
          if(childs != null) {
            childs[nbi] = newChild;
          } else if(newChild != oldChild) {
            childs = introspector.getChildren(any);
            childs[nbi] = newChild;
          } 
          environment.upLocal();
          nbi++;
      }
      if(childs!=null) {
        environment.setSubject(introspector.setChildren(any,childs));
      }
      return tom.library.sl.Environment.SUCCESS;
    } else {
      return tom.library.sl.Environment.FAILURE;
    }
  }
}
