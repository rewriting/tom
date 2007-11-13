
package polygraphicprogram.strategy.twopath;

public class Make_TwoId implements tom.library.sl.Strategy {

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

  private tom.library.sl.Strategy _onePath;


  public int getChildCount() {
    return 1;
  }
  public tom.library.sl.Visitable getChildAt(int index) {
    switch(index) {
      case 0: return _onePath;

      default: throw new IndexOutOfBoundsException();
    }
  }
  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
    switch(index) {
      case 0: _onePath = (tom.library.sl.Strategy) child; return this;

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[]{_onePath};
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
        this._onePath = (tom.library.sl.Strategy)children[0];

    return this;
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

  public tom.library.sl.Visitable visit(tom.library.sl.Visitable any) throws tom.library.sl.VisitFailure {
    tom.library.sl.AbstractStrategy.init(this,new tom.library.sl.Environment());
    getEnvironment().setRoot(any);
    int status = visit();
    if(status == tom.library.sl.Environment.SUCCESS) {
      return getEnvironment().getRoot();
    } else {
      throw new tom.library.sl.VisitFailure();
    }
  }

  public tom.library.sl.Strategy accept(tom.library.sl.reflective.StrategyFwd v) throws tom.library.sl.VisitFailure {
    return v.visit_Strategy(this);
  }

  public Make_TwoId(tom.library.sl.Strategy _onePath) {
    this._onePath = _onePath;

  }

  /**
    * Builds a new TwoId
    * If one of the sub-strategies application fails, throw a VisitFailure
    */
  public tom.library.sl.Visitable visitLight(tom.library.sl.Visitable any) throws tom.library.sl.VisitFailure {

    polygraphicprogram.types.OnePath new_onePath = (polygraphicprogram.types.OnePath) _onePath.visit(any);

    return polygraphicprogram.types.twopath.TwoId.make( new_onePath);
  }

  public int visit() {

    (_onePath).visit();
    polygraphicprogram.types.OnePath new_onePath = (polygraphicprogram.types.OnePath) getEnvironment().getSubject();

    getEnvironment().setSubject(polygraphicprogram.types.twopath.TwoId.make( new_onePath));
    return tom.library.sl.Environment.SUCCESS;
  }
}
