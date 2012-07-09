
package sort.strategy.surexpr;

public class Make_rec implements tom.library.sl.Strategy {

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

  private tom.library.sl.Strategy _s;


  public int getChildCount() {
    return 1;
  }
  public tom.library.sl.Visitable getChildAt(int index) {
    switch(index) {
      case 0: return _s;

      default: throw new IndexOutOfBoundsException();
    }
  }
  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
    switch(index) {
      case 0: _s = (tom.library.sl.Strategy) child; return this;

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[]{_s};
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
        this._s = (tom.library.sl.Strategy)children[0];

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

  public Make_rec(tom.library.sl.Strategy _s) {
    this._s = _s;

  }

  /**
    * Builds a new rec
    * If one of the sub-strategies application fails, throw a VisitFailure
    */

  @SuppressWarnings("unchecked")
  public <T> T visitLight(T any, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {

    Object tmp_s = _s.visitLight(any,i);
    if (! (tmp_s instanceof sort.types.Surexpr)) {
      throw new tom.library.sl.VisitFailure();
    }
    sort.types.Surexpr new_s = (sort.types.Surexpr) tmp_s;

    return (T) sort.types.surexpr.rec.make( new_s);
  }

  public int visit(tom.library.sl.Introspector i) {

    (_s).visit(i);
    if (! (getEnvironment().getSubject() instanceof sort.types.Surexpr)) {
      return tom.library.sl.Environment.FAILURE;
    }
    sort.types.Surexpr new_s = (sort.types.Surexpr) getEnvironment().getSubject();

    getEnvironment().setSubject(sort.types.surexpr.rec.make( new_s));
    return tom.library.sl.Environment.SUCCESS;
  }
}
