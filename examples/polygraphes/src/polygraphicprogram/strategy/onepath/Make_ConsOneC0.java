
package polygraphicprogram.strategy.onepath;

public class Make_ConsOneC0 implements tom.library.sl.Strategy {

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

  private tom.library.sl.Strategy _HeadOneC0;
  private tom.library.sl.Strategy _TailOneC0;


  public int getChildCount() {
    return 2;
  }
  public tom.library.sl.Visitable getChildAt(int index) {
    switch(index) {
      case 0: return _HeadOneC0;
      case 1: return _TailOneC0;

      default: throw new IndexOutOfBoundsException();
    }
  }
  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
    switch(index) {
      case 0: _HeadOneC0 = (tom.library.sl.Strategy) child; return this;
      case 1: _TailOneC0 = (tom.library.sl.Strategy) child; return this;

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[]{_HeadOneC0, _TailOneC0};
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
        this._HeadOneC0 = (tom.library.sl.Strategy)children[0];
    this._TailOneC0 = (tom.library.sl.Strategy)children[1];

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

  public Make_ConsOneC0(tom.library.sl.Strategy _HeadOneC0, tom.library.sl.Strategy _TailOneC0) {
    this._HeadOneC0 = _HeadOneC0;
    this._TailOneC0 = _TailOneC0;

  }

  /**
    * Builds a new ConsOneC0
    * If one of the sub-strategies application fails, throw a VisitFailure
    */
  public tom.library.sl.Visitable visitLight(tom.library.sl.Visitable any) throws tom.library.sl.VisitFailure {

    polygraphicprogram.types.OnePath new_HeadOneC0 = (polygraphicprogram.types.OnePath) _HeadOneC0.visit(any);

    polygraphicprogram.types.OnePath new_TailOneC0 = (polygraphicprogram.types.OnePath) _TailOneC0.visit(any);

    return polygraphicprogram.types.onepath.ConsOneC0.make( new_HeadOneC0,  new_TailOneC0);
  }

  public int visit() {

    (_HeadOneC0).visit();
    polygraphicprogram.types.OnePath new_HeadOneC0 = (polygraphicprogram.types.OnePath) getEnvironment().getSubject();

    (_TailOneC0).visit();
    polygraphicprogram.types.OnePath new_TailOneC0 = (polygraphicprogram.types.OnePath) getEnvironment().getSubject();

    getEnvironment().setSubject(polygraphicprogram.types.onepath.ConsOneC0.make( new_HeadOneC0,  new_TailOneC0));
    return tom.library.sl.Environment.SUCCESS;
  }
}
