
package mi.data.strategy.t1;

public class Make_f implements tom.library.sl.Strategy {

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

  private tom.library.sl.Strategy _s1;
  private tom.library.sl.Strategy _s2;


  public int getChildCount() {
    return 2;
  }
  public tom.library.sl.Visitable getChildAt(int index) {
    switch(index) {
      case 0: return _s1;
      case 1: return _s2;

      default: throw new IndexOutOfBoundsException();
    }
  }
  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
    switch(index) {
      case 0: _s1 = (tom.library.sl.Strategy) child; return this;
      case 1: _s2 = (tom.library.sl.Strategy) child; return this;

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[]{_s1, _s2};
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
        this._s1 = (tom.library.sl.Strategy)children[0];
    this._s2 = (tom.library.sl.Strategy)children[1];

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

  public Make_f(tom.library.sl.Strategy _s1, tom.library.sl.Strategy _s2) {
    this._s1 = _s1;
    this._s2 = _s2;

  }

  /**
    * Builds a new f
    * If one of the sub-strategies application fails, throw a VisitFailure
    */

  @SuppressWarnings("unchecked")
  public <T> T visitLight(T any, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {

    Object tmp_s1 = _s1.visit(any,i);
    if (! (tmp_s1 instanceof mi.data.types.T1)) {
      throw new tom.library.sl.VisitFailure();
    }
    mi.data.types.T1 new_s1 = (mi.data.types.T1) tmp_s1; 

    Object tmp_s2 = _s2.visit(any,i);
    if (! (tmp_s2 instanceof mi.data.types.T2)) {
      throw new tom.library.sl.VisitFailure();
    }
    mi.data.types.T2 new_s2 = (mi.data.types.T2) tmp_s2; 

    return (T) mi.data.types.t1.f.make( new_s1,  new_s2);
  }

  public int visit(tom.library.sl.Introspector i) {

    (_s1).visit(i);
    if (! (getEnvironment().getSubject() instanceof mi.data.types.T1)) {
      return tom.library.sl.Environment.FAILURE;
    }
    mi.data.types.T1 new_s1 = (mi.data.types.T1) getEnvironment().getSubject();

    (_s2).visit(i);
    if (! (getEnvironment().getSubject() instanceof mi.data.types.T2)) {
      return tom.library.sl.Environment.FAILURE;
    }
    mi.data.types.T2 new_s2 = (mi.data.types.T2) getEnvironment().getSubject();

    getEnvironment().setSubject(mi.data.types.t1.f.make( new_s1,  new_s2));
    return tom.library.sl.Environment.SUCCESS;
  }
}
