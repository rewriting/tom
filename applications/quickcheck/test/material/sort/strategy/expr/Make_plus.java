
package sort.strategy.expr;

public class Make_plus implements tom.library.sl.Strategy {

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

  private tom.library.sl.Strategy _a;
  private tom.library.sl.Strategy _b;


  public int getChildCount() {
    return 2;
  }
  public tom.library.sl.Visitable getChildAt(int index) {
    switch(index) {
      case 0: return _a;
      case 1: return _b;

      default: throw new IndexOutOfBoundsException();
    }
  }
  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
    switch(index) {
      case 0: _a = (tom.library.sl.Strategy) child; return this;
      case 1: _b = (tom.library.sl.Strategy) child; return this;

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[]{_a, _b};
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
        this._a = (tom.library.sl.Strategy)children[0];
    this._b = (tom.library.sl.Strategy)children[1];

    return this;
  }

  @SuppressWarnings("unchecked")
  public <T extends tom.library.sl.Visitable> T visit(tom.library.sl.Environment envt) throws tom.library.sl.VisitFailure {
    return (T) visit(envt,tom.library.sl.VisitableIntrospector.getInstance());
  }

  public <T extends tom.library.sl.Visitable> T visit(T any) throws tom.library.sl.VisitFailure{
    return visit(any,tom.library.sl.VisitableIntrospector.getInstance());
  }

  public <T extends tom.library.sl.Visitable> T visitLight(T any) throws tom.library.sl.VisitFailure {
    return visitLight(any,tom.library.sl.VisitableIntrospector.getInstance());
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

  @SuppressWarnings("unchecked")
  public <T> T visit(T any, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {
    tom.library.sl.AbstractStrategy.init(this,new tom.library.sl.Environment());
    getEnvironment().setRoot(any);
    int status = visit(i);
    if(status == tom.library.sl.Environment.SUCCESS) {
      return (T) getEnvironment().getRoot();
    } else {
      throw new tom.library.sl.VisitFailure();
    }
  }

  public Make_plus(tom.library.sl.Strategy _a, tom.library.sl.Strategy _b) {
    this._a = _a;
    this._b = _b;

  }

  /**
    * Builds a new plus
    * If one of the sub-strategies application fails, throw a VisitFailure
    */

  @SuppressWarnings("unchecked")
  public <T> T visitLight(T any, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {

    Object tmp_a = _a.visitLight(any,i);
    if (! (tmp_a instanceof sort.types.Expr)) {
      throw new tom.library.sl.VisitFailure();
    }
    sort.types.Expr new_a = (sort.types.Expr) tmp_a;

    Object tmp_b = _b.visitLight(any,i);
    if (! (tmp_b instanceof sort.types.Expr)) {
      throw new tom.library.sl.VisitFailure();
    }
    sort.types.Expr new_b = (sort.types.Expr) tmp_b;

    return (T) sort.types.expr.plus.make( new_a,  new_b);
  }

  public int visit(tom.library.sl.Introspector i) {

    (_a).visit(i);
    if (! (getEnvironment().getSubject() instanceof sort.types.Expr)) {
      return tom.library.sl.Environment.FAILURE;
    }
    sort.types.Expr new_a = (sort.types.Expr) getEnvironment().getSubject();

    (_b).visit(i);
    if (! (getEnvironment().getSubject() instanceof sort.types.Expr)) {
      return tom.library.sl.Environment.FAILURE;
    }
    sort.types.Expr new_b = (sort.types.Expr) getEnvironment().getSubject();

    getEnvironment().setSubject(sort.types.expr.plus.make( new_a,  new_b));
    return tom.library.sl.Environment.SUCCESS;
  }
}
