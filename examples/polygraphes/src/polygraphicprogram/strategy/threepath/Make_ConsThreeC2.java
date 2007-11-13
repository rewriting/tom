
package polygraphicprogram.strategy.threepath;

public class Make_ConsThreeC2 implements tom.library.sl.Strategy {

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

  private tom.library.sl.Strategy _HeadThreeC2;
  private tom.library.sl.Strategy _TailThreeC2;


  public int getChildCount() {
    return 2;
  }
  public tom.library.sl.Visitable getChildAt(int index) {
    switch(index) {
      case 0: return _HeadThreeC2;
      case 1: return _TailThreeC2;

      default: throw new IndexOutOfBoundsException();
    }
  }
  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
    switch(index) {
      case 0: _HeadThreeC2 = (tom.library.sl.Strategy) child; return this;
      case 1: _TailThreeC2 = (tom.library.sl.Strategy) child; return this;

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[]{_HeadThreeC2, _TailThreeC2};
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
        this._HeadThreeC2 = (tom.library.sl.Strategy)children[0];
    this._TailThreeC2 = (tom.library.sl.Strategy)children[1];

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

  public Make_ConsThreeC2(tom.library.sl.Strategy _HeadThreeC2, tom.library.sl.Strategy _TailThreeC2) {
    this._HeadThreeC2 = _HeadThreeC2;
    this._TailThreeC2 = _TailThreeC2;

  }

  /**
    * Builds a new ConsThreeC2
    * If one of the sub-strategies application fails, throw a VisitFailure
    */
  public tom.library.sl.Visitable visitLight(tom.library.sl.Visitable any) throws tom.library.sl.VisitFailure {

    polygraphicprogram.types.ThreePath new_HeadThreeC2 = (polygraphicprogram.types.ThreePath) _HeadThreeC2.visit(any);

    polygraphicprogram.types.ThreePath new_TailThreeC2 = (polygraphicprogram.types.ThreePath) _TailThreeC2.visit(any);

    return polygraphicprogram.types.threepath.ConsThreeC2.make( new_HeadThreeC2,  new_TailThreeC2);
  }

  public int visit() {

    (_HeadThreeC2).visit();
    polygraphicprogram.types.ThreePath new_HeadThreeC2 = (polygraphicprogram.types.ThreePath) getEnvironment().getSubject();

    (_TailThreeC2).visit();
    polygraphicprogram.types.ThreePath new_TailThreeC2 = (polygraphicprogram.types.ThreePath) getEnvironment().getSubject();

    getEnvironment().setSubject(polygraphicprogram.types.threepath.ConsThreeC2.make( new_HeadThreeC2,  new_TailThreeC2));
    return tom.library.sl.Environment.SUCCESS;
  }
}
