
package polygraphicprogram.strategy.twopath;

public class Make_TwoCell implements tom.library.sl.Strategy {

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

  private String _Name;
  private tom.library.sl.Strategy _Source;
  private tom.library.sl.Strategy _Target;
  private tom.library.sl.Strategy _Type;


  public int getChildCount() {
    return 3;
  }
  public tom.library.sl.Visitable getChildAt(int index) {
    switch(index) {
      case 0: return _Source;
      case 1: return _Target;
      case 2: return _Type;

      default: throw new IndexOutOfBoundsException();
    }
  }
  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
    switch(index) {
      case 0: _Source = (tom.library.sl.Strategy) child; return this;
      case 1: _Target = (tom.library.sl.Strategy) child; return this;
      case 2: _Type = (tom.library.sl.Strategy) child; return this;

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[]{_Source, _Target, _Type};
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
        this._Source = (tom.library.sl.Strategy)children[0];
    this._Target = (tom.library.sl.Strategy)children[1];
    this._Type = (tom.library.sl.Strategy)children[2];

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

  public Make_TwoCell(String _Name, tom.library.sl.Strategy _Source, tom.library.sl.Strategy _Target, tom.library.sl.Strategy _Type) {
    this._Name = _Name;
    this._Source = _Source;
    this._Target = _Target;
    this._Type = _Type;

  }

  /**
    * Builds a new TwoCell
    * If one of the sub-strategies application fails, throw a VisitFailure
    */
  public tom.library.sl.Visitable visitLight(tom.library.sl.Visitable any) throws tom.library.sl.VisitFailure {

    polygraphicprogram.types.OnePath new_Source = (polygraphicprogram.types.OnePath) _Source.visit(any);

    polygraphicprogram.types.OnePath new_Target = (polygraphicprogram.types.OnePath) _Target.visit(any);

    polygraphicprogram.types.CellType new_Type = (polygraphicprogram.types.CellType) _Type.visit(any);

    return polygraphicprogram.types.twopath.TwoCell.make( _Name,  new_Source,  new_Target,  new_Type);
  }

  public int visit() {

    (_Source).visit();
    polygraphicprogram.types.OnePath new_Source = (polygraphicprogram.types.OnePath) getEnvironment().getSubject();

    (_Target).visit();
    polygraphicprogram.types.OnePath new_Target = (polygraphicprogram.types.OnePath) getEnvironment().getSubject();

    (_Type).visit();
    polygraphicprogram.types.CellType new_Type = (polygraphicprogram.types.CellType) getEnvironment().getSubject();

    getEnvironment().setSubject(polygraphicprogram.types.twopath.TwoCell.make( _Name,  new_Source,  new_Target,  new_Type));
    return tom.library.sl.Environment.SUCCESS;
  }
}
