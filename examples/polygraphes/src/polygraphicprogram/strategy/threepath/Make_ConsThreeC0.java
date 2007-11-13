
package polygraphicprogram.strategy.threepath;

public class Make_ConsThreeC0 implements tom.library.sl.Strategy {

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

  private tom.library.sl.Strategy _HeadThreeC0;
  private tom.library.sl.Strategy _TailThreeC0;


  public int getChildCount() {
    return 2;
  }
  public tom.library.sl.Visitable getChildAt(int index) {
    switch(index) {
      case 0: return _HeadThreeC0;
      case 1: return _TailThreeC0;

      default: throw new IndexOutOfBoundsException();
    }
  }
  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
    switch(index) {
      case 0: _HeadThreeC0 = (tom.library.sl.Strategy) child; return this;
      case 1: _TailThreeC0 = (tom.library.sl.Strategy) child; return this;

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[]{_HeadThreeC0, _TailThreeC0};
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
        this._HeadThreeC0 = (tom.library.sl.Strategy)children[0];
    this._TailThreeC0 = (tom.library.sl.Strategy)children[1];

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

  public Make_ConsThreeC0(tom.library.sl.Strategy _HeadThreeC0, tom.library.sl.Strategy _TailThreeC0) {
    this._HeadThreeC0 = _HeadThreeC0;
    this._TailThreeC0 = _TailThreeC0;

  }

  /**
    * Builds a new ConsThreeC0
    * If one of the sub-strategies application fails, throw a VisitFailure
    */
  public tom.library.sl.Visitable visitLight(tom.library.sl.Visitable any) throws tom.library.sl.VisitFailure {

    polygraphicprogram.types.ThreePath new_HeadThreeC0 = (polygraphicprogram.types.ThreePath) _HeadThreeC0.visit(any);

    polygraphicprogram.types.ThreePath new_TailThreeC0 = (polygraphicprogram.types.ThreePath) _TailThreeC0.visit(any);

    return polygraphicprogram.types.threepath.ConsThreeC0.make( new_HeadThreeC0,  new_TailThreeC0);
  }

  public int visit() {

    (_HeadThreeC0).visit();
    polygraphicprogram.types.ThreePath new_HeadThreeC0 = (polygraphicprogram.types.ThreePath) getEnvironment().getSubject();

    (_TailThreeC0).visit();
    polygraphicprogram.types.ThreePath new_TailThreeC0 = (polygraphicprogram.types.ThreePath) getEnvironment().getSubject();

    getEnvironment().setSubject(polygraphicprogram.types.threepath.ConsThreeC0.make( new_HeadThreeC0,  new_TailThreeC0));
    return tom.library.sl.Environment.SUCCESS;
  }
}
