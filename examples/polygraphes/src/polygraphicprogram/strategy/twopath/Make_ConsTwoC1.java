
package polygraphicprogram.strategy.twopath;

public class Make_ConsTwoC1 implements tom.library.sl.Strategy {

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

  private tom.library.sl.Strategy _HeadTwoC1;
  private tom.library.sl.Strategy _TailTwoC1;


  public int getChildCount() {
    return 2;
  }
  public tom.library.sl.Visitable getChildAt(int index) {
    switch(index) {
      case 0: return _HeadTwoC1;
      case 1: return _TailTwoC1;

      default: throw new IndexOutOfBoundsException();
    }
  }
  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
    switch(index) {
      case 0: _HeadTwoC1 = (tom.library.sl.Strategy) child; return this;
      case 1: _TailTwoC1 = (tom.library.sl.Strategy) child; return this;

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[]{_HeadTwoC1, _TailTwoC1};
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
        this._HeadTwoC1 = (tom.library.sl.Strategy)children[0];
    this._TailTwoC1 = (tom.library.sl.Strategy)children[1];

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

  public Make_ConsTwoC1(tom.library.sl.Strategy _HeadTwoC1, tom.library.sl.Strategy _TailTwoC1) {
    this._HeadTwoC1 = _HeadTwoC1;
    this._TailTwoC1 = _TailTwoC1;

  }

  /**
    * Builds a new ConsTwoC1
    * If one of the sub-strategies application fails, throw a VisitFailure
    */
  public tom.library.sl.Visitable visitLight(tom.library.sl.Visitable any) throws tom.library.sl.VisitFailure {

    polygraphicprogram.types.TwoPath new_HeadTwoC1 = (polygraphicprogram.types.TwoPath) _HeadTwoC1.visit(any);

    polygraphicprogram.types.TwoPath new_TailTwoC1 = (polygraphicprogram.types.TwoPath) _TailTwoC1.visit(any);

    return polygraphicprogram.types.twopath.ConsTwoC1.make( new_HeadTwoC1,  new_TailTwoC1);
  }

  public int visit() {

    (_HeadTwoC1).visit();
    polygraphicprogram.types.TwoPath new_HeadTwoC1 = (polygraphicprogram.types.TwoPath) getEnvironment().getSubject();

    (_TailTwoC1).visit();
    polygraphicprogram.types.TwoPath new_TailTwoC1 = (polygraphicprogram.types.TwoPath) getEnvironment().getSubject();

    getEnvironment().setSubject(polygraphicprogram.types.twopath.ConsTwoC1.make( new_HeadTwoC1,  new_TailTwoC1));
    return tom.library.sl.Environment.SUCCESS;
  }
}
