
package firewall.ast.strategy.file;

public class Make_ConsBlocks implements tom.library.sl.Strategy {

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

  private tom.library.sl.Strategy _HeadBlocks;
  private tom.library.sl.Strategy _TailBlocks;


  public int getChildCount() {
    return 2;
  }
  public tom.library.sl.Visitable getChildAt(int index) {
    switch(index) {
      case 0: return _HeadBlocks;
      case 1: return _TailBlocks;

      default: throw new IndexOutOfBoundsException();
    }
  }
  public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
    switch(index) {
      case 0: _HeadBlocks = (tom.library.sl.Strategy) child; return this;
      case 1: _TailBlocks = (tom.library.sl.Strategy) child; return this;

      default: throw new IndexOutOfBoundsException();
    }
  }

  public tom.library.sl.Visitable[] getChildren() {
    return new tom.library.sl.Visitable[]{_HeadBlocks, _TailBlocks};
  }

  public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
        this._HeadBlocks = (tom.library.sl.Strategy)children[0];
    this._TailBlocks = (tom.library.sl.Strategy)children[1];

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

  public Make_ConsBlocks(tom.library.sl.Strategy _HeadBlocks, tom.library.sl.Strategy _TailBlocks) {
    this._HeadBlocks = _HeadBlocks;
    this._TailBlocks = _TailBlocks;

  }

  /**
    * Builds a new ConsBlocks
    * If one of the sub-strategies application fails, throw a VisitFailure
    */
  public Object visitLight(Object any, tom.library.sl.Introspector i) throws tom.library.sl.VisitFailure {

    firewall.ast.types.Block new_HeadBlocks = (firewall.ast.types.Block) _HeadBlocks.visit(any,i);

    firewall.ast.types.File new_TailBlocks = (firewall.ast.types.File) _TailBlocks.visit(any,i);

    return firewall.ast.types.file.ConsBlocks.make( new_HeadBlocks,  new_TailBlocks);
  }

  public int visit(tom.library.sl.Introspector i) {

    (_HeadBlocks).visit(i);
    firewall.ast.types.Block new_HeadBlocks = (firewall.ast.types.Block) getEnvironment().getSubject();

    (_TailBlocks).visit(i);
    firewall.ast.types.File new_TailBlocks = (firewall.ast.types.File) getEnvironment().getSubject();

    getEnvironment().setSubject(firewall.ast.types.file.ConsBlocks.make( new_HeadBlocks,  new_TailBlocks));
    return tom.library.sl.Environment.SUCCESS;
  }
}
