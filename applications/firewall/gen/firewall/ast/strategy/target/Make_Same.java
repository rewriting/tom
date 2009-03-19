
package firewall.ast.strategy.target;

public class Make_Same implements tom.library.sl.Strategy {

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



  public int getChildCount() {
    return 0;
  }
  public tom.library.sl.Visitable getChildAt(int index) {
    switch(index) {

      default: throw new IndexOutOfBoundsException();
    }
  }
  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
    switch(index) {

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[]{};
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
    
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

  public Object visit(tom.library.sl.Environment envt, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {
    tom.library.sl.AbstractStrategy.init(this,envt);
    int status = visit(i);
    if(status == tom.library.sl.Environment.SUCCESS) {
      return environment.getSubject();
    } else {
      throw new tom.library.sl.VisitFailure();
    }
  }

  public Object visit(Object any, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {
    tom.library.sl.AbstractStrategy.init(this,new tom.library.sl.Environment());
    getEnvironment().setRoot(any);
    int status = visit(i);
    if(status == tom.library.sl.Environment.SUCCESS) {
      return getEnvironment().getRoot();
    } else {
      throw new tom.library.sl.VisitFailure();
    }
  }

  public Make_Same() {

  }

  /**
    * Builds a new Same
    * If one of the sub-strategies application fails, throw a VisitFailure
    */
  public Object visitLight(Object any, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {

    return firewall.ast.types.target.Same.make();
  }

  public int visit(tom.library.sl.Introspector i) {

    getEnvironment().setSubject(firewall.ast.types.target.Same.make());
    return tom.library.sl.Environment.SUCCESS;
  }
}
